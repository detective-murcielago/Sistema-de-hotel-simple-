package Entidades;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Representa un registro histórico de cargo/puesto ocupado por un empleado,
 * usado para la trazabilidad de cambios de cargo y sueldo (F-018).
 */
public class HistorialCargo implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private int idEmpleado;
    private String cargo;
    private String departamento;
    private double sueldoAsignado;
    private LocalDate fechaInicio;
    private LocalDate fechaFin; // null si es el cargo vigente

    public HistorialCargo(int idEmpleado, String cargo, String departamento,
            double sueldoAsignado, LocalDate fechaInicio, LocalDate fechaFin) {
        this.idEmpleado = idEmpleado;
        this.cargo = cargo;
        this.departamento = departamento;
        this.sueldoAsignado = sueldoAsignado;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdEmpleado() { return idEmpleado; }
    public void setIdEmpleado(int idEmpleado) { this.idEmpleado = idEmpleado; }

    public String getCargo() { return cargo; }
    public void setCargo(String cargo) { this.cargo = cargo; }

    public String getDepartamento() { return departamento; }
    public void setDepartamento(String departamento) { this.departamento = departamento; }

    public double getSueldoAsignado() { return sueldoAsignado; }
    public void setSueldoAsignado(double sueldoAsignado) { this.sueldoAsignado = sueldoAsignado; }

    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }

    public LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }

    @Override
    public String toString() {
        return "HistorialCargo{id=" + id + ", cargo=" + cargo + ", departamento=" + departamento
                + ", desde=" + fechaInicio + ", hasta=" + (fechaFin == null ? "Actualidad" : fechaFin) + '}';
    }
}
