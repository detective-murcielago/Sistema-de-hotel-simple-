package Entidades;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Representa una solicitud de vacaciones o permiso de un empleado (F-019).
 * Estados posibles: "Pendiente", "Aprobada", "Rechazada".
 */
public class SolicitudPermiso implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private int idEmpleado;
    private String nombreEmpleado;
    private String tipo;           // "Vacaciones" o "Permiso"
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String motivo;
    private String estado;         // Pendiente / Aprobada / Rechazada
    private LocalDateTime fechaSolicitud;
    private String comentarioRRHH; // observación al aprobar/rechazar

    public SolicitudPermiso(int idEmpleado, String nombreEmpleado, String tipo,
            LocalDate fechaInicio, LocalDate fechaFin, String motivo) {
        this.idEmpleado = idEmpleado;
        this.nombreEmpleado = nombreEmpleado;
        this.tipo = tipo;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.motivo = motivo;
        this.estado = "Pendiente";
        this.fechaSolicitud = LocalDateTime.now();
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdEmpleado() { return idEmpleado; }
    public void setIdEmpleado(int idEmpleado) { this.idEmpleado = idEmpleado; }

    public String getNombreEmpleado() { return nombreEmpleado; }
    public void setNombreEmpleado(String nombreEmpleado) { this.nombreEmpleado = nombreEmpleado; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }

    public LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }

    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public LocalDateTime getFechaSolicitud() { return fechaSolicitud; }
    public void setFechaSolicitud(LocalDateTime fechaSolicitud) { this.fechaSolicitud = fechaSolicitud; }

    public String getComentarioRRHH() { return comentarioRRHH; }
    public void setComentarioRRHH(String comentarioRRHH) { this.comentarioRRHH = comentarioRRHH; }

    public long getDiasSolicitados() {
        if (fechaInicio == null || fechaFin == null) return 0;
        return java.time.temporal.ChronoUnit.DAYS.between(fechaInicio, fechaFin) + 1;
    }

    @Override
    public String toString() {
        return "SolicitudPermiso{id=" + id + ", empleado=" + nombreEmpleado + ", tipo=" + tipo
                + ", desde=" + fechaInicio + ", hasta=" + fechaFin + ", estado=" + estado + '}';
    }
}
