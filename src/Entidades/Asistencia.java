package Entidades;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Representa un registro de marcaje de asistencia de un empleado (F-017).
 * Estados posibles: "Asistencia Puntual", "Tardanza", "Rechazado".
 */
public class Asistencia implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private int idEmpleado;
    private String nombreEmpleado;
    private String tipoMarca;      // "ENTRADA" o "SALIDA"
    private LocalDateTime fechaHoraMarcaje;
    private String estado;         // Asistencia Puntual / Tardanza / Rechazado
    private int minutosRetraso;    // 0 si no hubo tardanza
    private String observacion;    // motivo del rechazo, si aplica

    public Asistencia(int idEmpleado, String nombreEmpleado, String tipoMarca,
            LocalDateTime fechaHoraMarcaje, String estado, int minutosRetraso, String observacion) {
        this.idEmpleado = idEmpleado;
        this.nombreEmpleado = nombreEmpleado;
        this.tipoMarca = tipoMarca;
        this.fechaHoraMarcaje = fechaHoraMarcaje;
        this.estado = estado;
        this.minutosRetraso = minutosRetraso;
        this.observacion = observacion;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdEmpleado() { return idEmpleado; }
    public void setIdEmpleado(int idEmpleado) { this.idEmpleado = idEmpleado; }

    public String getNombreEmpleado() { return nombreEmpleado; }
    public void setNombreEmpleado(String nombreEmpleado) { this.nombreEmpleado = nombreEmpleado; }

    public String getTipoMarca() { return tipoMarca; }
    public void setTipoMarca(String tipoMarca) { this.tipoMarca = tipoMarca; }

    public LocalDateTime getFechaHoraMarcaje() { return fechaHoraMarcaje; }
    public void setFechaHoraMarcaje(LocalDateTime fechaHoraMarcaje) { this.fechaHoraMarcaje = fechaHoraMarcaje; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public int getMinutosRetraso() { return minutosRetraso; }
    public void setMinutosRetraso(int minutosRetraso) { this.minutosRetraso = minutosRetraso; }

    public String getObservacion() { return observacion; }
    public void setObservacion(String observacion) { this.observacion = observacion; }

    @Override
    public String toString() {
        return "Asistencia{id=" + id + ", empleado=" + nombreEmpleado + ", tipo=" + tipoMarca
                + ", fechaHora=" + fechaHoraMarcaje + ", estado=" + estado + '}';
    }
}
