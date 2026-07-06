package controlador;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO del modulo contable - HU F-001 (Gerente General).
 *
 * Implementa los 3 escenarios de la historia de usuario:
 *  1) Generacion automatica del asiento contable (pago exitoso).
 *  2) Generacion de asiento de REVERSION (estorno) ante anulacion/cancelacion.
 *  3) Manejo de excepcion cuando el servicio NO tiene cuenta mapeada:
 *     - No genera asiento (no duplica ni cae).
 *     - Marca la transaccion como PENDIENTE_ASIENTO.
 *     - Registra un log detallado y una alerta para Finanzas.
 *
 * Reglas de partida doble aplicadas (PCGE simplificado):
 *   DEBE  = cuenta de caja/banco segun metodo de pago.
 *   HABER = cuenta de ingreso segun el mapeo servicio->cuenta.
 */
public class DaoContabilidad {

    // ---- Resultado del intento de generar un asiento ------------------
    public static class ResultadoAsiento {
        public boolean exito;
        public boolean pendiente;      // escenario 3
        public int idAsiento;
        public String mensaje;
        public ResultadoAsiento(boolean exito, boolean pendiente, int idAsiento, String mensaje) {
            this.exito = exito; this.pendiente = pendiente;
            this.idAsiento = idAsiento; this.mensaje = mensaje;
        }
    }

    // ===================================================================
    //  Determina la cuenta de DEBE (caja/banco) segun el metodo de pago
    // ===================================================================
    private String cuentaPorMetodoPago(String metodoPago) {
        if (metodoPago == null) return "1011";
        String m = metodoPago.trim().toLowerCase();
        if (m.contains("tarjeta") || m.contains("pos") || m.contains("visa") || m.contains("credito") || m.contains("debito"))
            return "1041";
        if (m.contains("qr") || m.contains("yape") || m.contains("plin") || m.contains("digital") || m.contains("transfer"))
            return "1042";
        return "1011"; // efectivo por defecto
    }

    // ===================================================================
    //  Busca la cuenta de HABER (ingreso) mapeada al servicio.
    //  Devuelve null si NO existe mapeo -> dispara escenario 3.
    // ===================================================================
    private String cuentaIngresoPorServicio(Connection cn, String servicio) throws SQLException {
        String sql = "SELECT cuenta_ingreso FROM mapeo_cuenta_servicio WHERE servicio = ?";
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, servicio);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getString(1);
            }
        }
        return null;
    }

    // ===================================================================
    //  ESCENARIO 1: generar asiento automatico por un pago exitoso.
    //  ESCENARIO 3: si no hay mapeo -> PENDIENTE_ASIENTO + log + alerta.
    // ===================================================================
    public ResultadoAsiento generarAsientoPorPago(int idPago, String servicio, String metodoPago,
                                                  double monto, LocalDateTime fecha) {
        Connection cn = ConexionDB.getConexion();
        try {
            // Evitar duplicados: si ya existe asiento NORMAL para este pago, no re-generar.
            if (idPago > 0 && existeAsientoNormal(cn, idPago)) {
                return new ResultadoAsiento(false, false, 0,
                        "Ya existe un asiento para el pago #" + idPago + ". No se duplica.");
            }

            String cuentaHaber = cuentaIngresoPorServicio(cn, servicio);

            // ---------- ESCENARIO 3: cuenta no mapeada ----------
            if (cuentaHaber == null) {
                registrarLog(cn, "ADVERTENCIA", idPago, servicio,
                    "Cuenta contable no mapeada para el servicio '" + servicio +
                    "'. Pago marcado como PENDIENTE DE ASIENTO. Requiere configuracion en el ERP.");
                // Se crea una cabecera marcada como PENDIENTE (sin movimientos = sin afectar el mayor)
                int idPend = insertarCabecera(cn, fecha,
                        "PENDIENTE: pago servicio '" + servicio + "' sin cuenta mapeada",
                        "NORMAL", "PENDIENTE_ASIENTO", idPago, null, 0.0, 0.0);
                return new ResultadoAsiento(false, true, idPend,
                    "Servicio '" + servicio + "' sin cuenta contable mapeada.\n" +
                    "El pago quedo PENDIENTE DE ASIENTO. Se notifico al Administrador de Finanzas.");
            }

            // ---------- ESCENARIO 1: asiento normal ----------
            String cuentaDebe = cuentaPorMetodoPago(metodoPago);
            String glosa = "Cobro servicio '" + servicio + "' (" + metodoPago + ")";

            cn.setAutoCommit(false);
            try {
                int idAsiento = insertarCabecera(cn, fecha, glosa, "NORMAL", "REGISTRADO",
                        idPago, null, monto, monto);
                insertarMovimiento(cn, idAsiento, cuentaDebe, monto, 0.0);  // DEBE  caja/banco
                insertarMovimiento(cn, idAsiento, cuentaHaber, 0.0, monto); // HABER ingreso
                registrarLog(cn, "INFO", idPago, servicio,
                    "Asiento #" + idAsiento + " generado. Debe " + cuentaDebe +
                    " / Haber " + cuentaHaber + " por S/ " + String.format("%.2f", monto));
                cn.commit();
                return new ResultadoAsiento(true, false, idAsiento,
                    "Asiento #" + idAsiento + " registrado correctamente en el libro mayor.");
            } catch (SQLException ex) {
                cn.rollback();
                throw ex;
            } finally {
                cn.setAutoCommit(true);
            }

        } catch (SQLException e) {
            return new ResultadoAsiento(false, false, 0,
                    "Error contable: " + e.getMessage());
        }
    }

    // ===================================================================
    //  ESCENARIO 2: asiento de REVERSION (estorno).
    //  Invierte Debe/Haber del asiento original para neutralizar el ingreso.
    // ===================================================================
    public ResultadoAsiento generarReversion(int idAsientoOriginal) {
        Connection cn = ConexionDB.getConexion();
        try {
            // Traer cabecera original
            String estado; String tipo; String glosa; int idPago;
            String sqlCab = "SELECT estado,tipo,glosa,id_pago FROM asiento_contable WHERE id_asiento=?";
            try (PreparedStatement ps = cn.prepareStatement(sqlCab)) {
                ps.setInt(1, idAsientoOriginal);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next())
                        return new ResultadoAsiento(false, false, 0, "El asiento original no existe.");
                    estado = rs.getString(1); tipo = rs.getString(2);
                    glosa  = rs.getString(3); idPago = rs.getInt(4);
                }
            }
            if ("REVERSION".equals(tipo))
                return new ResultadoAsiento(false, false, 0, "No se puede estornar un asiento de reversion.");
            if (!"REGISTRADO".equals(estado))
                return new ResultadoAsiento(false, false, 0,
                        "Solo se puede estornar un asiento REGISTRADO (estado actual: " + estado + ").");
            if (existeReversion(cn, idAsientoOriginal))
                return new ResultadoAsiento(false, false, 0, "Este asiento ya fue estornado previamente.");

            // Leer movimientos originales
            List<double[]> movs = new ArrayList<>();       // [debe, haber]
            List<String>  cuentas = new ArrayList<>();
            double totDebe = 0, totHaber = 0;
            String sqlMov = "SELECT cuenta,debe,haber FROM mayor_movimiento WHERE id_asiento=?";
            try (PreparedStatement ps = cn.prepareStatement(sqlMov)) {
                ps.setInt(1, idAsientoOriginal);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        cuentas.add(rs.getString("cuenta"));
                        double d = rs.getDouble("debe"), h = rs.getDouble("haber");
                        movs.add(new double[]{d, h});
                        totDebe += d; totHaber += h;
                    }
                }
            }
            if (movs.isEmpty())
                return new ResultadoAsiento(false, false, 0,
                        "El asiento no tiene movimientos que estornar.");

            cn.setAutoCommit(false);
            try {
                int idRev = insertarCabecera(cn, LocalDateTime.now(),
                        "REVERSION (estorno) de asiento #" + idAsientoOriginal + " - " + glosa,
                        "REVERSION", "REGISTRADO", idPago, idAsientoOriginal,
                        totHaber, totDebe); // totales invertidos
                // Insertar movimientos invertidos (Debe<->Haber)
                for (int i = 0; i < movs.size(); i++) {
                    double d = movs.get(i)[0], h = movs.get(i)[1];
                    insertarMovimiento(cn, idRev, cuentas.get(i), h, d); // invertido
                }
                // Marcar original como ANULADO
                try (PreparedStatement ps = cn.prepareStatement(
                        "UPDATE asiento_contable SET estado='ANULADO' WHERE id_asiento=?")) {
                    ps.setInt(1, idAsientoOriginal);
                    ps.executeUpdate();
                }
                registrarLog(cn, "INFO", idPago, null,
                    "Asiento de reversion #" + idRev + " generado sobre el asiento #" +
                    idAsientoOriginal + ". Ingreso neutralizado.");
                cn.commit();
                return new ResultadoAsiento(true, false, idRev,
                    "Reversion #" + idRev + " registrada. El asiento original #" +
                    idAsientoOriginal + " quedo ANULADO.");
            } catch (SQLException ex) {
                cn.rollback();
                throw ex;
            } finally {
                cn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            return new ResultadoAsiento(false, false, 0, "Error en reversion: " + e.getMessage());
        }
    }

    // ===================================================================
    //  Reintento del escenario 3: una vez mapeada la cuenta, generar
    //  el asiento del pago que quedo pendiente.
    // ===================================================================
    public ResultadoAsiento resolverPendiente(int idAsientoPendiente, String servicio,
                                              String metodoPago, double monto) {
        Connection cn = ConexionDB.getConexion();
        try {
            int idPago = 0;
            String sql = "SELECT id_pago,estado FROM asiento_contable WHERE id_asiento=?";
            try (PreparedStatement ps = cn.prepareStatement(sql)) {
                ps.setInt(1, idAsientoPendiente);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next())
                        return new ResultadoAsiento(false, false, 0, "Registro pendiente no encontrado.");
                    if (!"PENDIENTE_ASIENTO".equals(rs.getString("estado")))
                        return new ResultadoAsiento(false, false, 0, "El registro ya no esta pendiente.");
                    idPago = rs.getInt("id_pago");
                }
            }
            String cuentaHaber = cuentaIngresoPorServicio(cn, servicio);
            if (cuentaHaber == null)
                return new ResultadoAsiento(false, true, 0,
                        "El servicio '" + servicio + "' sigue sin cuenta mapeada.");

            String cuentaDebe = cuentaPorMetodoPago(metodoPago);
            cn.setAutoCommit(false);
            try {
                // Convertir el pendiente en asiento real
                try (PreparedStatement ps = cn.prepareStatement(
                        "UPDATE asiento_contable SET estado='REGISTRADO', total_debe=?, total_haber=?, " +
                        "glosa=? WHERE id_asiento=?")) {
                    ps.setDouble(1, monto); ps.setDouble(2, monto);
                    ps.setString(3, "Cobro servicio '" + servicio + "' (regularizado)");
                    ps.setInt(4, idAsientoPendiente);
                    ps.executeUpdate();
                }
                insertarMovimiento(cn, idAsientoPendiente, cuentaDebe, monto, 0.0);
                insertarMovimiento(cn, idAsientoPendiente, cuentaHaber, 0.0, monto);
                marcarLogsResueltos(cn, idPago);
                registrarLog(cn, "INFO", idPago, servicio,
                    "Pendiente regularizado. Asiento #" + idAsientoPendiente + " ahora REGISTRADO.");
                cn.commit();
                return new ResultadoAsiento(true, false, idAsientoPendiente,
                    "Pendiente regularizado. Asiento #" + idAsientoPendiente + " registrado.");
            } catch (SQLException ex) {
                cn.rollback(); throw ex;
            } finally {
                cn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            return new ResultadoAsiento(false, false, 0, "Error al regularizar: " + e.getMessage());
        }
    }

    // ===================================================================
    //  CONFIGURACION: mapeo servicio -> cuenta de ingreso
    // ===================================================================
    public boolean guardarMapeo(String servicio, String cuentaIngreso) {
        Connection cn = ConexionDB.getConexion();
        String sql = "INSERT INTO mapeo_cuenta_servicio(servicio,cuenta_ingreso) VALUES(?,?) " +
                     "ON DUPLICATE KEY UPDATE cuenta_ingreso=VALUES(cuenta_ingreso)";
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, servicio);
            ps.setString(2, cuentaIngreso);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error guardarMapeo: " + e.getMessage());
            return false;
        }
    }

    // ===================================================================
    //  CONSULTAS para el JFrame
    // ===================================================================

    /** Libro mayor completo (para la tabla principal). */
    public List<Object[]> listarLibroMayor() {
        List<Object[]> filas = new ArrayList<>();
        String sql = "SELECT id_asiento,fecha,tipo,estado,cuenta,nombre_cuenta,debe,haber,glosa " +
                     "FROM v_libro_mayor";
        try (Statement st = ConexionDB.getConexion().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                filas.add(new Object[]{
                    rs.getInt("id_asiento"),
                    rs.getTimestamp("fecha"),
                    rs.getString("tipo"),
                    rs.getString("estado"),
                    rs.getString("cuenta"),
                    rs.getString("nombre_cuenta"),
                    rs.getDouble("debe"),
                    rs.getDouble("haber"),
                    rs.getString("glosa")
                });
            }
        } catch (SQLException e) {
            System.err.println("Error listarLibroMayor: " + e.getMessage());
        }
        return filas;
    }

    /** Asientos NORMALES vigentes (para poder estornarlos). */
    public List<Object[]> listarAsientosEstornables() {
        List<Object[]> filas = new ArrayList<>();
        String sql = "SELECT id_asiento,fecha,glosa,total_debe FROM asiento_contable " +
                     "WHERE tipo='NORMAL' AND estado='REGISTRADO' ORDER BY id_asiento DESC";
        try (Statement st = ConexionDB.getConexion().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                filas.add(new Object[]{
                    rs.getInt("id_asiento"),
                    rs.getTimestamp("fecha"),
                    rs.getString("glosa"),
                    rs.getDouble("total_debe")
                });
            }
        } catch (SQLException e) {
            System.err.println("Error listarAsientosEstornables: " + e.getMessage());
        }
        return filas;
    }

    /** Pendientes de asiento (escenario 3). */
    public List<Object[]> listarPendientes() {
        List<Object[]> filas = new ArrayList<>();
        String sql = "SELECT a.id_asiento,a.fecha,a.glosa,a.id_pago,p.servicio,p.metodo_pago,p.monto " +
                     "FROM asiento_contable a LEFT JOIN pago_servicio p ON p.id_pago=a.id_pago " +
                     "WHERE a.estado='PENDIENTE_ASIENTO' ORDER BY a.id_asiento DESC";
        try (Statement st = ConexionDB.getConexion().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                filas.add(new Object[]{
                    rs.getInt("id_asiento"),
                    rs.getTimestamp("fecha"),
                    rs.getString("glosa"),
                    rs.getInt("id_pago"),
                    rs.getString("servicio"),
                    rs.getString("metodo_pago"),
                    rs.getDouble("monto")
                });
            }
        } catch (SQLException e) {
            System.err.println("Error listarPendientes: " + e.getMessage());
        }
        return filas;
    }

    /** Log contable / alertas para Finanzas. */
    public List<Object[]> listarLog() {
        List<Object[]> filas = new ArrayList<>();
        String sql = "SELECT fecha,nivel,servicio,mensaje,resuelto FROM log_contable ORDER BY id_log DESC";
        try (Statement st = ConexionDB.getConexion().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                filas.add(new Object[]{
                    rs.getTimestamp("fecha"),
                    rs.getString("nivel"),
                    rs.getString("servicio"),
                    rs.getString("mensaje"),
                    rs.getBoolean("resuelto") ? "Si" : "No"
                });
            }
        } catch (SQLException e) {
            System.err.println("Error listarLog: " + e.getMessage());
        }
        return filas;
    }

    /** Cuentas de tipo INGRESO para combos de mapeo. */
    public List<String[]> listarCuentasIngreso() {
        List<String[]> l = new ArrayList<>();
        String sql = "SELECT codigo,nombre FROM cuenta_contable WHERE tipo='INGRESO' AND activa=1";
        try (Statement st = ConexionDB.getConexion().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) l.add(new String[]{rs.getString(1), rs.getString(2)});
        } catch (SQLException e) {
            System.err.println("Error listarCuentasIngreso: " + e.getMessage());
        }
        return l;
    }

    /** Saldos por cuenta (Debe, Haber, Saldo) - solo asientos vigentes. */
    public List<Object[]> saldosPorCuenta() {
        List<Object[]> filas = new ArrayList<>();
        String sql = "SELECT m.cuenta,c.nombre,SUM(m.debe) d,SUM(m.haber) h " +
                     "FROM mayor_movimiento m " +
                     "JOIN asiento_contable a ON a.id_asiento=m.id_asiento " +
                     "JOIN cuenta_contable c ON c.codigo=m.cuenta " +
                     "WHERE a.estado<>'ANULADO' GROUP BY m.cuenta,c.nombre ORDER BY m.cuenta";
        try (Statement st = ConexionDB.getConexion().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                double d = rs.getDouble("d"), h = rs.getDouble("h");
                filas.add(new Object[]{rs.getString("cuenta"), rs.getString("nombre"),
                                       d, h, (d - h)});
            }
        } catch (SQLException e) {
            System.err.println("Error saldosPorCuenta: " + e.getMessage());
        }
        return filas;
    }

    // ===================================================================
    //  Helpers privados de insercion / verificacion
    // ===================================================================
    private int insertarCabecera(Connection cn, LocalDateTime fecha, String glosa, String tipo,
                                 String estado, int idPago, Integer idRef,
                                 double totDebe, double totHaber) throws SQLException {
        String sql = "INSERT INTO asiento_contable(fecha,glosa,tipo,estado,id_pago,id_asiento_ref," +
                     "total_debe,total_haber) VALUES(?,?,?,?,?,?,?,?)";
        try (PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setTimestamp(1, Timestamp.valueOf(fecha));
            ps.setString(2, glosa);
            ps.setString(3, tipo);
            ps.setString(4, estado);
            if (idPago > 0) ps.setInt(5, idPago); else ps.setNull(5, Types.INTEGER);
            if (idRef != null) ps.setInt(6, idRef); else ps.setNull(6, Types.INTEGER);
            ps.setDouble(7, totDebe);
            ps.setDouble(8, totHaber);
            ps.executeUpdate();
            try (ResultSet gk = ps.getGeneratedKeys()) {
                if (gk.next()) return gk.getInt(1);
            }
        }
        return 0;
    }

    private void insertarMovimiento(Connection cn, int idAsiento, String cuenta,
                                    double debe, double haber) throws SQLException {
        String sql = "INSERT INTO mayor_movimiento(id_asiento,cuenta,debe,haber) VALUES(?,?,?,?)";
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, idAsiento);
            ps.setString(2, cuenta);
            ps.setDouble(3, debe);
            ps.setDouble(4, haber);
            ps.executeUpdate();
        }
    }

    private void registrarLog(Connection cn, String nivel, int idPago, String servicio,
                              String mensaje) throws SQLException {
        String sql = "INSERT INTO log_contable(fecha,nivel,id_pago,servicio,mensaje) VALUES(?,?,?,?,?)";
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            ps.setString(2, nivel);
            if (idPago > 0) ps.setInt(3, idPago); else ps.setNull(3, Types.INTEGER);
            ps.setString(4, servicio);
            ps.setString(5, mensaje);
            ps.executeUpdate();
        }
    }

    private void marcarLogsResueltos(Connection cn, int idPago) throws SQLException {
        try (PreparedStatement ps = cn.prepareStatement(
                "UPDATE log_contable SET resuelto=1 WHERE id_pago=? AND nivel='ADVERTENCIA'")) {
            ps.setInt(1, idPago);
            ps.executeUpdate();
        }
    }

    private boolean existeAsientoNormal(Connection cn, int idPago) throws SQLException {
        try (PreparedStatement ps = cn.prepareStatement(
                "SELECT 1 FROM asiento_contable WHERE id_pago=? AND tipo='NORMAL' " +
                "AND estado IN('REGISTRADO','PENDIENTE_ASIENTO') LIMIT 1")) {
            ps.setInt(1, idPago);
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        }
    }

    private boolean existeReversion(Connection cn, int idAsientoOriginal) throws SQLException {
        try (PreparedStatement ps = cn.prepareStatement(
                "SELECT 1 FROM asiento_contable WHERE id_asiento_ref=? AND tipo='REVERSION' LIMIT 1")) {
            ps.setInt(1, idAsientoOriginal);
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        }
    }
}
