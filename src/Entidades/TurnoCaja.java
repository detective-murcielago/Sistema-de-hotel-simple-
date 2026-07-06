package Entidades;

import java.io.Serializable;
import java.time.LocalDateTime;

public class TurnoCaja implements Serializable {

    private static final long serialVersionUID = 1L;
    private LocalDateTime fechaCierre;
    private String empleadoTurno;
    private double totalSistema;
    private double totalFisico;
    private String estado;
    private String motivo = "";   // Motivo de la diferencia (faltante/sobrante) al cerrar caja


    public TurnoCaja(String empleadoTurno, double totalSistema, double totalFisico) {
        this.empleadoTurno = empleadoTurno;
        this.fechaCierre = LocalDateTime.now();
        this.totalSistema = totalSistema;
        this.totalFisico = totalFisico;
        this.estado = calcularEstado();
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    private String calcularEstado() {
        double diferencia = totalFisico - totalSistema;
        if (diferencia == 0) {
            return "Cuadrado (Perfecto)";
        }
        if (diferencia < 0) {
            return "Faltante de S/ " + Math.abs(diferencia);
        }
        return "Sobrante de S/ " + diferencia;
    }

    // Getters y Setters
    public String getEmpleadoTurno() {
        return empleadoTurno;
    }

    public void setEmpleadoTurno(String empleadoTurno) {
        this.empleadoTurno = empleadoTurno;
    }

    public LocalDateTime getFechaCierre() {
        return fechaCierre;
    }

    public void setFechaCierre(LocalDateTime fechaCierre) {
        this.fechaCierre = fechaCierre;
    }

    public double getTotalSistema() {
        return totalSistema;
    }

    public void setTotalSistema(double totalSistema) {
        this.totalSistema = totalSistema;
    }

    public double getTotalFisico() {
        return totalFisico;
    }

    public void setTotalFisico(double totalFisico) {
        this.totalFisico = totalFisico;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
