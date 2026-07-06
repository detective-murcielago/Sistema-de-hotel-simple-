package Entidades;

import java.io.Serializable;
import java.util.Date;

public class Producto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String nombre;
    private String tipo;
    private int stock;
    private Date fechaAgregado; // Usamos Date para que sea 100% compatible con JDateChooser
    private int stockMinimo = 9; // umbral configurable por producto (default = comportamiento anterior)

    public Producto(String nombre, String tipo, int stock, Date fechaAgregado) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.stock = stock;
        this.fechaAgregado = fechaAgregado;
    }

    // NUEVO constructor: permite indicar el stock mínimo explícitamente.
    public Producto(String nombre, String tipo, int stock, Date fechaAgregado, int stockMinimo) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.stock = stock;
        this.fechaAgregado = fechaAgregado;
        this.stockMinimo = stockMinimo;
    }

    public int getStockMinimo() {
        return stockMinimo;
    }

    public void setStockMinimo(int stockMinimo) {
        this.stockMinimo = stockMinimo;
    }

    // calcula el estado en tiempo real, ahora en base al stockMinimo de CADA producto ---
    public String getEstadoVisual() {
        if (this.stock <= this.stockMinimo) {
            return "Muy bajo";
        } else {
            return "Suficiente";
        }
    }

    // NUEVO: indica si el producto está en estado crítico (igual o por debajo del mínimo)
    public boolean isStockCritico() {
        return this.stock <= this.stockMinimo;
    }

    // Getters y Setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public Date getFechaAgregado() {
        return fechaAgregado;
    }

    public void setFechaAgregado(Date fechaAgregado) {
        this.fechaAgregado = fechaAgregado;
    }
}
