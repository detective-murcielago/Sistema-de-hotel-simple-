package Entidades;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Representa una evaluación de desempeño registrada por el Gerente General
 * para un empleado (F-020).
 */
public class EvaluacionDesempeno implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private int idEmpleado;
    private String nombreEmpleado;
    private String tipoCalificacion; // "Numerica" o "Cualitativa"
    private String calificacion;     // valor numérico (0-20) o cualitativo (Excelente/Bueno/Regular/Deficiente)
    private String observaciones;
    private String evaluador;        // nombre de quien evalúa (Gerente General)
    private LocalDate fechaEvaluacion;

    public EvaluacionDesempeno(int idEmpleado, String nombreEmpleado, String tipoCalificacion,
            String calificacion, String observaciones, String evaluador) {
        this.idEmpleado = idEmpleado;
        this.nombreEmpleado = nombreEmpleado;
        this.tipoCalificacion = tipoCalificacion;
        this.calificacion = calificacion;
        this.observaciones = observaciones;
        this.evaluador = evaluador;
        this.fechaEvaluacion = LocalDate.now();
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdEmpleado() { return idEmpleado; }
    public void setIdEmpleado(int idEmpleado) { this.idEmpleado = idEmpleado; }

    public String getNombreEmpleado() { return nombreEmpleado; }
    public void setNombreEmpleado(String nombreEmpleado) { this.nombreEmpleado = nombreEmpleado; }

    public String getTipoCalificacion() { return tipoCalificacion; }
    public void setTipoCalificacion(String tipoCalificacion) { this.tipoCalificacion = tipoCalificacion; }

    public String getCalificacion() { return calificacion; }
    public void setCalificacion(String calificacion) { this.calificacion = calificacion; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    public String getEvaluador() { return evaluador; }
    public void setEvaluador(String evaluador) { this.evaluador = evaluador; }

    public LocalDate getFechaEvaluacion() { return fechaEvaluacion; }
    public void setFechaEvaluacion(LocalDate fechaEvaluacion) { this.fechaEvaluacion = fechaEvaluacion; }

    @Override
    public String toString() {
        return "EvaluacionDesempeno{id=" + id + ", empleado=" + nombreEmpleado
                + ", calificacion=" + calificacion + ", fecha=" + fechaEvaluacion + '}';
    }
}
