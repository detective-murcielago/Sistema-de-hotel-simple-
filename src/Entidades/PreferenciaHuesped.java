package Entidades;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Preferencia registrada de un huésped.
 * Historias de usuario F-004 (registrar preferencias) y F-011 (mantener
 * historial de preferencias para personalizar el servicio).
 *
 * Se relaciona con el huésped por su número de documento para que sea
 * independiente del id autonumérico de la BD.
 */
public class PreferenciaHuesped implements Serializable {

    private static final long serialVersionUID = 1L;

    private int idPreferencia;              // id en BD (0 si aún no persistido)
    private String numDocumentoHuesped;     // huésped al que pertenece
    private String tipoPreferencia;         // Ej: Tipo de habitación, Piso, Servicios
    private String detalle;                 // Descripción libre de la preferencia
    private LocalDateTime fechaRegistro;

    public PreferenciaHuesped(String numDocumentoHuesped, String tipoPreferencia,
                              String detalle, LocalDateTime fechaRegistro) {
        this.numDocumentoHuesped = numDocumentoHuesped;
        this.tipoPreferencia = tipoPreferencia;
        this.detalle = detalle;
        this.fechaRegistro = fechaRegistro;
    }

    public int getIdPreferencia() {
        return idPreferencia;
    }

    public void setIdPreferencia(int idPreferencia) {
        this.idPreferencia = idPreferencia;
    }

    public String getNumDocumentoHuesped() {
        return numDocumentoHuesped;
    }

    public void setNumDocumentoHuesped(String numDocumentoHuesped) {
        this.numDocumentoHuesped = numDocumentoHuesped;
    }

    public String getTipoPreferencia() {
        return tipoPreferencia;
    }

    public void setTipoPreferencia(String tipoPreferencia) {
        this.tipoPreferencia = tipoPreferencia;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    @Override
    public String toString() {
        return tipoPreferencia + ": " + detalle;
    }
}
