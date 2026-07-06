package Entidades;

import java.io.Serializable;
import java.util.Date;

public class OrdenCompra implements Serializable {

    private static final long serialVersionUID = 1L;

    private String idOrden;
    private String nombreProducto;
    private String tipo;
    private int cantidad;
    private Date fechaEmision;
    private Date fechaEntrega;
    private String proveedor;
    private double precioTotal;
    private String estado; // Ejemplo: "Pendiente", "Aprobado" y "Rechazado"

    public OrdenCompra(String idOrden, String nombreProducto, String tipo, int cantidad, Date fechaEmision, Date fechaEntrega, String proveedor, double precioTotal) {
        this.idOrden = idOrden;
        this.nombreProducto = nombreProducto;
        this.tipo = tipo;
        this.cantidad = cantidad;
        this.fechaEmision = fechaEmision;
        this.fechaEntrega = fechaEntrega;
        this.proveedor = proveedor;
        this.precioTotal = precioTotal;
        this.estado = "Pendiente"; // Toda orden nace como Pendiente
    }

    // Getters y Setters
    public String getIdOrden() {
        return idOrden;
    }

    public void setIdOrden(String idOrden) {
        this.idOrden = idOrden;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public Date getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public Date getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(Date fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    public double getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(double precioTotal) {
        this.precioTotal = precioTotal;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
