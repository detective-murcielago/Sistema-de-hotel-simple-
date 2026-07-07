package controlador;

import Entidades.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class PersistenciaRRHH {

    private final Connection con;

    public PersistenciaRRHH() {
        this.con = ConexionDB.getConexion();
    }

    // =========================================================
    // F-017 · CONFIGURACIÓN DE TOLERANCIA Y TURNOS
    // =========================================================

    /** Obtiene los minutos de tolerancia configurados (fila única id=1). Por defecto 10. */
    public int obtenerMinutosTolerancia() {
        String sql = "SELECT minutos_tolerancia FROM configuracion_asistencia WHERE id = 1";
        try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) return rs.getInt("minutos_tolerancia");
        } catch (SQLException ex) { ex.printStackTrace(); }
        return 10;
    }

    public void actualizarMinutosTolerancia(int minutos) {
        String sql = "INSERT INTO configuracion_asistencia (id, minutos_tolerancia) VALUES (1, ?) "
                + "ON DUPLICATE KEY UPDATE minutos_tolerancia = VALUES(minutos_tolerancia)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, minutos);
            ps.executeUpdate();
        } catch (SQLException ex) { ex.printStackTrace(); }
    }

    /** Devuelve la hora de entrada programada del empleado, o null si no tiene turno asignado. */
    public LocalTime obtenerHoraEntradaTurno(int idEmpleado) {
        String sql = "SELECT hora_entrada FROM turno_empleado WHERE id_empleado = ? AND activo = 1";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idEmpleado);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getTime("hora_entrada").toLocalTime();
            }
        } catch (SQLException ex) { ex.printStackTrace(); }
        return null;
    }

    public void asignarTurno(int idEmpleado, LocalTime horaEntrada, LocalTime horaSalida) {
        String sql = "INSERT INTO turno_empleado (id_empleado, hora_entrada, hora_salida, activo) "
                + "VALUES (?,?,?,1) ON DUPLICATE KEY UPDATE hora_entrada=VALUES(hora_entrada), "
                + "hora_salida=VALUES(hora_salida), activo=1";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idEmpleado);
            ps.setTime(2, Time.valueOf(horaEntrada));
            ps.setTime(3, Time.valueOf(horaSalida));
            ps.executeUpdate();
        } catch (SQLException ex) { ex.printStackTrace(); }
    }

    // =========================================================
    // F-017 · MARCAJE DE ASISTENCIA
    // =========================================================

    public void registrarAsistencia(Asistencia a) {
        String sql = "INSERT INTO asistencia (id_empleado, nombre_empleado, tipo_marca, "
                + "fecha_hora, estado, minutos_retraso, observacion) VALUES (?,?,?,?,?,?,?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, a.getIdEmpleado());
            ps.setString(2, a.getNombreEmpleado());
            ps.setString(3, a.getTipoMarca());
            ps.setTimestamp(4, Timestamp.valueOf(a.getFechaHoraMarcaje()));
            ps.setString(5, a.getEstado());
            ps.setInt(6, a.getMinutosRetraso());
            ps.setString(7, a.getObservacion());
            ps.executeUpdate();
        } catch (SQLException ex) { ex.printStackTrace(); }
    }

    public List<Asistencia> listarAsistencias() {
        List<Asistencia> lista = new ArrayList<>();
        String sql = "SELECT * FROM asistencia ORDER BY fecha_hora DESC";
        try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Asistencia a = new Asistencia(
                        rs.getInt("id_empleado"), rs.getString("nombre_empleado"),
                        rs.getString("tipo_marca"), rs.getTimestamp("fecha_hora").toLocalDateTime(),
                        rs.getString("estado"), rs.getInt("minutos_retraso"), rs.getString("observacion"));
                a.setId(rs.getInt("id"));
                lista.add(a);
            }
        } catch (SQLException ex) { ex.printStackTrace(); }
        return lista;
    }

    public List<Asistencia> listarAsistenciasPorEmpleado(int idEmpleado) {
        List<Asistencia> lista = new ArrayList<>();
        String sql = "SELECT * FROM asistencia WHERE id_empleado = ? ORDER BY fecha_hora DESC";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idEmpleado);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Asistencia a = new Asistencia(
                            rs.getInt("id_empleado"), rs.getString("nombre_empleado"),
                            rs.getString("tipo_marca"), rs.getTimestamp("fecha_hora").toLocalDateTime(),
                            rs.getString("estado"), rs.getInt("minutos_retraso"), rs.getString("observacion"));
                    a.setId(rs.getInt("id"));
                    lista.add(a);
                }
            }
        } catch (SQLException ex) { ex.printStackTrace(); }
        return lista;
    }

    /** Registra en la bitácora de auditoría de RR.HH. un intento fallido de marcaje. */
    public void registrarBitacora(int idEmpleado, String nombreEmpleado, String mensaje) {
        String sql = "INSERT INTO bitacora_auditoria (id_empleado, nombre_empleado, mensaje, fecha_hora) "
                + "VALUES (?,?,?,?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idEmpleado);
            ps.setString(2, nombreEmpleado);
            ps.setString(3, mensaje);
            ps.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
            ps.executeUpdate();
        } catch (SQLException ex) { ex.printStackTrace(); }
    }

    /** Devuelve las filas de la bitácora de auditoría (intentos fallidos de marcaje), más recientes primero. */
    public List<Object[]> listarBitacora() {
        List<Object[]> lista = new ArrayList<>();
        String sql = "SELECT nombre_empleado, mensaje, fecha_hora FROM bitacora_auditoria ORDER BY fecha_hora DESC";
        try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Object[]{
                        rs.getString("nombre_empleado"),
                        rs.getString("mensaje"),
                        rs.getTimestamp("fecha_hora").toLocalDateTime()
                });
            }
        } catch (SQLException ex) { ex.printStackTrace(); }
        return lista;
    }

    // =========================================================
    // F-018 · HISTORIAL LABORAL (HISTORIAL DE CARGOS)
    // =========================================================

    public void registrarHistorialCargo(HistorialCargo h) {
        String sql = "INSERT INTO historial_cargo (id_empleado, cargo, departamento, "
                + "sueldo_asignado, fecha_inicio, fecha_fin) VALUES (?,?,?,?,?,?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, h.getIdEmpleado());
            ps.setString(2, h.getCargo());
            ps.setString(3, h.getDepartamento());
            ps.setDouble(4, h.getSueldoAsignado());
            ps.setDate(5, Date.valueOf(h.getFechaInicio()));
            ps.setDate(6, h.getFechaFin() != null ? Date.valueOf(h.getFechaFin()) : null);
            ps.executeUpdate();
        } catch (SQLException ex) { ex.printStackTrace(); }
    }

    public List<HistorialCargo> listarHistorialPorEmpleado(int idEmpleado) {
        List<HistorialCargo> lista = new ArrayList<>();
        String sql = "SELECT * FROM historial_cargo WHERE id_empleado = ? ORDER BY fecha_inicio DESC";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idEmpleado);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Date fFin = rs.getDate("fecha_fin");
                    HistorialCargo h = new HistorialCargo(
                            rs.getInt("id_empleado"), rs.getString("cargo"), rs.getString("departamento"),
                            rs.getDouble("sueldo_asignado"), rs.getDate("fecha_inicio").toLocalDate(),
                            fFin != null ? fFin.toLocalDate() : null);
                    h.setId(rs.getInt("id"));
                    lista.add(h);
                }
            }
        } catch (SQLException ex) { ex.printStackTrace(); }
        return lista;
    }

    // =========================================================
    // F-019 · SOLICITUDES DE VACACIONES Y PERMISOS
    // =========================================================

    public void registrarSolicitud(SolicitudPermiso s) {
        String sql = "INSERT INTO permiso (id_empleado, nombre_empleado, tipo, fecha_inicio, "
                + "fecha_fin, motivo, estado, fecha_solicitud) VALUES (?,?,?,?,?,?,?,?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, s.getIdEmpleado());
            ps.setString(2, s.getNombreEmpleado());
            ps.setString(3, s.getTipo());
            ps.setDate(4, Date.valueOf(s.getFechaInicio()));
            ps.setDate(5, Date.valueOf(s.getFechaFin()));
            ps.setString(6, s.getMotivo());
            ps.setString(7, s.getEstado());
            ps.setTimestamp(8, Timestamp.valueOf(s.getFechaSolicitud()));
            ps.executeUpdate();
        } catch (SQLException ex) { ex.printStackTrace(); }
    }

    public List<SolicitudPermiso> listarSolicitudes() {
        List<SolicitudPermiso> lista = new ArrayList<>();
        String sql = "SELECT * FROM permiso ORDER BY fecha_solicitud DESC";
        try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapearSolicitud(rs));
        } catch (SQLException ex) { ex.printStackTrace(); }
        return lista;
    }

    /** Actualiza el estado (Aprobada/Rechazada) de una solicitud. Una vez resuelta, no puede volver a Pendiente. */
    public void actualizarEstadoSolicitud(int idSolicitud, String nuevoEstado, String comentarioRRHH) {
        String sql = "UPDATE permiso SET estado = ?, comentario_rrhh = ? "
                + "WHERE id = ? AND estado = 'Pendiente'";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nuevoEstado);
            ps.setString(2, comentarioRRHH);
            ps.setInt(3, idSolicitud);
            ps.executeUpdate();
        } catch (SQLException ex) { ex.printStackTrace(); }
    }

    private SolicitudPermiso mapearSolicitud(ResultSet rs) throws SQLException {
        SolicitudPermiso s = new SolicitudPermiso(
                rs.getInt("id_empleado"), rs.getString("nombre_empleado"), rs.getString("tipo"),
                rs.getDate("fecha_inicio").toLocalDate(), rs.getDate("fecha_fin").toLocalDate(),
                rs.getString("motivo"));
        s.setId(rs.getInt("id"));
        s.setEstado(rs.getString("estado"));
        s.setFechaSolicitud(rs.getTimestamp("fecha_solicitud").toLocalDateTime());
        s.setComentarioRRHH(rs.getString("comentario_rrhh"));
        return s;
    }

    // =========================================================
    // F-020 · EVALUACIONES DE DESEMPEÑO
    // =========================================================

    public void registrarEvaluacion(EvaluacionDesempeno e) {
        String sql = "INSERT INTO evaluacion_desempeno (id_empleado, nombre_empleado, "
                + "tipo_calificacion, calificacion, observaciones, evaluador, fecha_evaluacion) "
                + "VALUES (?,?,?,?,?,?,?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, e.getIdEmpleado());
            ps.setString(2, e.getNombreEmpleado());
            ps.setString(3, e.getTipoCalificacion());
            ps.setString(4, e.getCalificacion());
            ps.setString(5, e.getObservaciones());
            ps.setString(6, e.getEvaluador());
            ps.setDate(7, Date.valueOf(e.getFechaEvaluacion()));
            ps.executeUpdate();
        } catch (SQLException ex) { ex.printStackTrace(); }
    }

    public List<EvaluacionDesempeno> listarEvaluacionesPorEmpleado(int idEmpleado) {
        List<EvaluacionDesempeno> lista = new ArrayList<>();
        String sql = "SELECT * FROM evaluacion_desempeno WHERE id_empleado = ? ORDER BY fecha_evaluacion DESC";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idEmpleado);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    EvaluacionDesempeno e = new EvaluacionDesempeno(
                            rs.getInt("id_empleado"), rs.getString("nombre_empleado"),
                            rs.getString("tipo_calificacion"), rs.getString("calificacion"),
                            rs.getString("observaciones"), rs.getString("evaluador"));
                    e.setId(rs.getInt("id"));
                    e.setFechaEvaluacion(rs.getDate("fecha_evaluacion").toLocalDate());
                    lista.add(e);
                }
            }
        } catch (SQLException ex) { ex.printStackTrace(); }
        return lista;
    }

    // =========================================================
    // F-021 · HORARIO SEMANAL Y DOTACIÓN (PLANTILLA)
    //   Requiere el script sql/rrhh_horarios_plantilla.sql
    // =========================================================

    /** Guarda (inserta o actualiza) el horario de un empleado en un día (1=Lun..7=Dom). */
    public void guardarHorarioSemanal(int idEmpleado, int diaSemana,
                                      LocalTime entrada, LocalTime salida, boolean descanso) {
        String sql = "INSERT INTO horario_semanal (id_empleado, dia_semana, hora_entrada, hora_salida, descanso) "
                   + "VALUES (?,?,?,?,?) ON DUPLICATE KEY UPDATE "
                   + "hora_entrada=VALUES(hora_entrada), hora_salida=VALUES(hora_salida), descanso=VALUES(descanso)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idEmpleado);
            ps.setInt(2, diaSemana);
            if (descanso) {
                ps.setNull(3, Types.TIME);
                ps.setNull(4, Types.TIME);
            } else {
                ps.setTime(3, Time.valueOf(entrada));
                ps.setTime(4, Time.valueOf(salida));
            }
            ps.setInt(5, descanso ? 1 : 0);
            ps.executeUpdate();
        } catch (SQLException ex) { ex.printStackTrace(); }
    }

    /** Elimina la asignación de un día para un empleado (deja la celda vacía). */
    public void eliminarHorarioDia(int idEmpleado, int diaSemana) {
        String sql = "DELETE FROM horario_semanal WHERE id_empleado=? AND dia_semana=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idEmpleado);
            ps.setInt(2, diaSemana);
            ps.executeUpdate();
        } catch (SQLException ex) { ex.printStackTrace(); }
    }

    /**
     * Devuelve el horario de todos los empleados como matriz:
     *   clave = id de empleado, valor = arreglo de 8 posiciones (índices 1..7 = Lun..Dom).
     * Cada celda es "08:00-17:00", "D" (descanso), o "" (sin asignar).
     */
    public java.util.Map<Integer, String[]> obtenerMatrizHorarios() {
        java.util.Map<Integer, String[]> mapa = new java.util.LinkedHashMap<>();
        String sql = "SELECT id_empleado, dia_semana, hora_entrada, hora_salida, descanso FROM horario_semanal";
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int idEmp = rs.getInt("id_empleado");
                int dia = rs.getInt("dia_semana");
                String[] fila = mapa.computeIfAbsent(idEmp, k -> new String[]{"", "", "", "", "", "", "", ""});
                if (rs.getInt("descanso") == 1) {
                    fila[dia] = "D";
                } else {
                    Time e = rs.getTime("hora_entrada");
                    Time s = rs.getTime("hora_salida");
                    String he = (e == null) ? "" : e.toString().substring(0, 5);
                    String hs = (s == null) ? "" : s.toString().substring(0, 5);
                    fila[dia] = he + "-" + hs;
                }
            }
        } catch (SQLException ex) { ex.printStackTrace(); }
        return mapa;
    }

    /**
     * Dotación por rol: cada Object[] = { rol(String), maximo(int), ocupados(int) }.
     * Lee la vista v_dotacion.
     */
    public List<Object[]> listarDotacion() {
        List<Object[]> lista = new ArrayList<>();
        String sql = "SELECT rol, maximo, ocupados FROM v_dotacion ORDER BY rol";
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Object[]{ rs.getString("rol"), rs.getInt("maximo"), rs.getInt("ocupados") });
            }
        } catch (SQLException ex) { ex.printStackTrace(); }
        return lista;
    }

    /**
     * Indica si queda al menos una vacante libre para el rol dado.
     * Cuenta directamente sobre la tabla empleado vs el tope de rol_plantilla,
     * así refleja el estado real aunque los contadores auxiliares se desfasen.
     * Si el rol no tiene tope definido, devuelve true (sin límite).
     */
    public boolean hayCupoParaRol(String rol) {
        String sql = "SELECT rp.max_empleados - COALESCE(c.n, 0) AS libre "
                   + "FROM rol_plantilla rp "
                   + "LEFT JOIN (SELECT rol, COUNT(*) n FROM empleado WHERE rol = ? GROUP BY rol) c "
                   + "  ON c.rol = rp.rol "
                   + "WHERE rp.rol = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, rol);
            ps.setString(2, rol);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("libre") > 0;
            }
        } catch (SQLException ex) { ex.printStackTrace(); }
        return true; // rol sin tope definido en rol_plantilla
    }
}
