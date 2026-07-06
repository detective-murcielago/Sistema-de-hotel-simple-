package Entidades;

import java.io.Serializable;

public class Habitacion implements Serializable {

    private String numero;
    private char tipo;// Simple=S, Doble=D y Matrimonial=M
    private double precio;
    private char estado;//Ocupado= O,  Disponible=D y mantenimiento
    private int CantidadHuesped = 4;
    private String descripcionProblema = "";
    private String encargadoLimpieza = "";
    private static final long serialVersionUID = 1L;

    public Habitacion(String numero, char tipo, char estado, double precio) {
        this.numero = numero;
        this.tipo = tipo;
        this.precio = precio;
        this.estado = estado;

    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public char getTipo() {
        return tipo;
    }

    public void setTipo(char tipo) {
        this.tipo = tipo;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public char getEstado() {
        return estado;
    }

    public void setEstado(char estado) {
        this.estado = estado;
    }

    public void marcaOcupado() {
        this.estado = 'O';
    }

    public void marcaDisponible() {
        this.estado = 'D';
    }

    public String getDescripcionProblema() {
        return descripcionProblema;
    }

    public void setDescripcionProblema(String descripcionProblema) {
        this.descripcionProblema = descripcionProblema;
    }

    public String getEncargadoLimpieza() {
        return encargadoLimpieza;
    }

    public void setEncargadoLimpieza(String encargadoLimpieza) {
        this.encargadoLimpieza = encargadoLimpieza;
    }

    @Override
    public String toString() {
        return "Habitacion{"
                + "numero=" + numero
                + ", tipo=" + tipo
                + ", precio=" + precio
                + ", estado=" + estado
                + ", cantidadMaximaHuespedes=" + CantidadHuesped
                + '}';
    }

}
