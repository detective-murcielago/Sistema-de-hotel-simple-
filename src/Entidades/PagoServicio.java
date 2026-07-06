package Entidades;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Pago de un servicio contratado por el huésped al momento de contratarlo.
 * Historia de usuario F-010: registrar el pago realizado por el huésped,
 * manteniendo control preciso de ingresos y trazabilidad de transacciones.
 */
public class PagoServicio implements Serializable {

    private static final long serialVersionUID = 1L;

    private int idPago;                     // id en BD (0 si aún no persistido)
    private String numDocumentoHuesped;     // huésped que paga
    private String servicio;                // servicio contratado (Desayuno, Lavandería, etc.)
    private String metodoPago;              // QR, Efectivo, Tarjeta
    private double monto;
    private String comprobante;             // código de comprobante asociado
    private LocalDateTime fechaPago;
    private String idFicha;                 // ficha de hospedaje cobrada (trazabilidad)

    public PagoServicio(String numDocumentoHuesped, String servicio, String metodoPago,
                        double monto, String comprobante, LocalDateTime fechaPago) {
        this.numDocumentoHuesped = numDocumentoHuesped;
        this.servicio = servicio;
        this.metodoPago = metodoPago;
        this.monto = monto;
        this.comprobante = comprobante;
        this.fechaPago = fechaPago;
    }

    public String getIdFicha() {
        return idFicha;
    }

    public void setIdFicha(String idFicha) {
        this.idFicha = idFicha;
    }

    public int getIdPago() {
        return idPago;
    }

    public void setIdPago(int idPago) {
        this.idPago = idPago;
    }

    public String getNumDocumentoHuesped() {
        return numDocumentoHuesped;
    }

    public void setNumDocumentoHuesped(String numDocumentoHuesped) {
        this.numDocumentoHuesped = numDocumentoHuesped;
    }

    public String getServicio() {
        return servicio;
    }

    public void setServicio(String servicio) {
        this.servicio = servicio;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public String getComprobante() {
        return comprobante;
    }

    public void setComprobante(String comprobante) {
        this.comprobante = comprobante;
    }

    public LocalDateTime getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(LocalDateTime fechaPago) {
        this.fechaPago = fechaPago;
    }

    /** Comprobante de pago tipo ticket para F-010 (registro exitoso de pago). */
    public String generarComprobante() {
        DateTimeFormatter f = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        StringBuilder sb = new StringBuilder();
        sb.append("=========================================\n");
        sb.append("     HOTEL TRUGARDEN - PAGO DE SERVICIO   \n");
        sb.append("=========================================\n");
        sb.append("Comprobante : ").append(comprobante).append("\n");
        sb.append("Fecha       : ").append(fechaPago.format(f)).append("\n");
        sb.append("Documento   : ").append(numDocumentoHuesped).append("\n");
        sb.append("-----------------------------------------\n");
        sb.append("Servicio    : ").append(servicio).append("\n");
        sb.append("Método pago : ").append(metodoPago).append("\n");
        sb.append(String.format("MONTO PAGADO: S/ %.2f%n", monto));
        sb.append("=========================================\n");
        sb.append("      ¡Gracias por su preferencia!       \n");
        return sb.toString();
    }

    @Override
    public String toString() {
        return servicio + " - S/ " + String.format("%.2f", monto) + " (" + metodoPago + ")";
    }
}
