package controlador;

import Entidades.PagoServicio;
import Entidades.PreferenciaHuesped;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Acceso a datos para las Historias de Usuario del módulo Recepcionista:
 *
 *   F-004  registrarPreferencia / listarPreferencias
 *   F-005  listarEstadias (con y sin filtro por fecha)
 *   F-010  registrarPagoServicio / listarPagosServicio
 *   F-011  usa listarEstadias + listarPreferencias
 *
 * Reutiliza la conexión MySQL existente (ConexionDB).
 */
public class DaoRecepcionista {

    private Connection con() {
        return ConexionDB.getConexion();
    }

    // =========================================================
    // F-004 / F-011 : PREFERENCIAS
    // =========================================================

    /** Registra una preferencia. Devuelve true si se guardó. */
    public boolean registrarPreferencia(PreferenciaHuesped p) {
        String sql = "INSERT INTO preferencia_huesped "
                + "(num_documento, tipo_preferencia, detalle, fecha_registro) "
                + "VALUES (?,?,?,?)";
        try (PreparedStatement ps = con().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, p.getNumDocumentoHuesped());
            ps.setString(2, p.getTipoPreferencia());
            ps.setString(3, p.getDetalle());
            ps.setTimestamp(4, Timestamp.valueOf(p.getFechaRegistro()));
            int filas = ps.executeUpdate();
            try (ResultSet gk = ps.getGeneratedKeys()) {
                if (gk.next()) {
                    p.setIdPreferencia(gk.getInt(1));
                }
            }
            return filas > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /** F-004/F-011: lista todas las preferencias de un huésped. */
    public List<PreferenciaHuesped> listarPreferencias(String numDocumento) {
        List<PreferenciaHuesped> lista = new ArrayList<>();
        String sql = "SELECT * FROM preferencia_huesped WHERE num_documento = ? "
                + "ORDER BY fecha_registro DESC";
        try (PreparedStatement ps = con().prepareStatement(sql)) {
            ps.setString(1, numDocumento);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    PreferenciaHuesped p = new PreferenciaHuesped(
                            rs.getString("num_documento"),
                            rs.getString("tipo_preferencia"),
                            rs.getString("detalle"),
                            rs.getTimestamp("fecha_registro").toLocalDateTime());
                    p.setIdPreferencia(rs.getInt("id_preferencia"));
                    lista.add(p);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return lista;
    }

    // =========================================================
    // F-005 / F-011 : HISTORIAL DE ESTADÍAS
    //   Reutiliza la tabla ficha_hospedaje ya existente.
    // =========================================================

    /** Fila liviana para poblar la tabla de historial en la interfaz. */
    public static class EstadiaRow {
        public String idFicha;
        public String habitacion;
        public String fechaIngreso;
        public String fechaSalida;
        public String estado;
        public int personas;
        public String servicios;
    }

    /**
     * F-005: historial de estadías de un huésped.
     * Si desde/hasta son null se traen todas; si no, filtra por fecha_ingreso.
     */
    public List<EstadiaRow> listarEstadias(String numDocumento,
                                           LocalDateTime desde, LocalDateTime hasta) {
        List<EstadiaRow> lista = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT f.id_ficha, f.numero_habitacion, f.fecha_ingreso, f.fecha_salida, "
                + "f.estado, f.cantidad_personas, f.incluye_desayuno, f.incluye_almuerzo, "
                + "f.incluye_cena "
                + "FROM ficha_hospedaje f "
                + "JOIN ficha_huesped fh ON fh.id_ficha = f.id_ficha "
                + "JOIN huesped h ON h.id = fh.id_huesped "
                + "WHERE h.num_documento = ? ");
        if (desde != null && hasta != null) {
            sql.append("AND f.fecha_ingreso BETWEEN ? AND ? ");
        }
        sql.append("ORDER BY f.fecha_ingreso DESC");

        try (PreparedStatement ps = con().prepareStatement(sql.toString())) {
            ps.setString(1, numDocumento);
            if (desde != null && hasta != null) {
                ps.setTimestamp(2, Timestamp.valueOf(desde));
                ps.setTimestamp(3, Timestamp.valueOf(hasta));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    EstadiaRow r = new EstadiaRow();
                    r.idFicha = rs.getString("id_ficha");
                    r.habitacion = rs.getString("numero_habitacion");
                    Timestamp tin = rs.getTimestamp("fecha_ingreso");
                    Timestamp tsa = rs.getTimestamp("fecha_salida");
                    r.fechaIngreso = tin != null ? tin.toLocalDateTime().toLocalDate().toString() : "-";
                    r.fechaSalida = tsa != null ? tsa.toLocalDateTime().toLocalDate().toString() : "En curso";
                    char est = rs.getString("estado").charAt(0);
                    r.estado = (est == 'A') ? "Activa" : (est == 'F') ? "Finalizada" : String.valueOf(est);
                    r.personas = rs.getInt("cantidad_personas");
                    List<String> serv = new ArrayList<>();
                    if (rs.getBoolean("incluye_desayuno")) serv.add("Desayuno");
                    if (rs.getBoolean("incluye_almuerzo")) serv.add("Almuerzo");
                    if (rs.getBoolean("incluye_cena")) serv.add("Cena");
                    r.servicios = serv.isEmpty() ? "Ninguno" : String.join(", ", serv);
                    lista.add(r);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return lista;
    }

    // =========================================================
    // F-010 : PAGO DE SERVICIOS
    // =========================================================

    /** Registra un pago de servicio. Devuelve true si se guardó. */
    public boolean registrarPagoServicio(PagoServicio p) {
        String sql = "INSERT INTO pago_servicio "
                + "(num_documento, servicio, metodo_pago, monto, comprobante, fecha_pago) "
                + "VALUES (?,?,?,?,?,?)";
        try (PreparedStatement ps = con().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, p.getNumDocumentoHuesped());
            ps.setString(2, p.getServicio());
            ps.setString(3, p.getMetodoPago());
            ps.setDouble(4, p.getMonto());
            ps.setString(5, p.getComprobante());
            ps.setTimestamp(6, Timestamp.valueOf(p.getFechaPago()));
            int filas = ps.executeUpdate();
            try (ResultSet gk = ps.getGeneratedKeys()) {
                if (gk.next()) {
                    p.setIdPago(gk.getInt(1));
                }
            }

            // === HU F-001: generar asiento contable automatico ===
            // Escenario 1 (asiento normal) o Escenario 3 (pendiente si no hay
            // cuenta mapeada). No interrumpe el registro del pago si algo falla.
            if (filas > 0) {
                try {
                    new DaoContabilidad().generarAsientoPorPago(
                            p.getIdPago(),
                            p.getServicio(),
                            p.getMetodoPago(),
                            p.getMonto(),
                            p.getFechaPago());
                } catch (Exception exContab) {
                    System.err.println("Aviso contable (F-001): " + exContab.getMessage());
                }
            }
            return filas > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /** F-010: consulta de pagos registrados de un huésped. */
    public List<PagoServicio> listarPagosServicio(String numDocumento) {
        List<PagoServicio> lista = new ArrayList<>();
        String sql = "SELECT * FROM pago_servicio WHERE num_documento = ? "
                + "ORDER BY fecha_pago DESC";
        try (PreparedStatement ps = con().prepareStatement(sql)) {
            ps.setString(1, numDocumento);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    PagoServicio p = new PagoServicio(
                            rs.getString("num_documento"),
                            rs.getString("servicio"),
                            rs.getString("metodo_pago"),
                            rs.getDouble("monto"),
                            rs.getString("comprobante"),
                            rs.getTimestamp("fecha_pago").toLocalDateTime());
                    p.setIdPago(rs.getInt("id_pago"));
                    lista.add(p);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return lista;
    }
}
