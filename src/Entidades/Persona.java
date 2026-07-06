package Entidades;

import java.io.Serializable;

public abstract class Persona implements Serializable {

    private String nombre;
    private String apellido;
    private String tipoDocumento; //  DNI, Carnet de Extranjería, etc.
    private String numDocumento;  // Antes era 'dni' int, ahora es String para soportar CE largos
    private int telefono;
    private String direccion;     
    private static final long serialVersionUID = 1L;

    // Constructor actualizado con los nuevos campos
    public Persona(String nombre, String apellido, String tipoDocumento, String numDocumento, int telefono, String direccion) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.tipoDocumento = tipoDocumento;
        this.numDocumento = numDocumento;
        this.telefono = telefono;
        this.direccion = direccion;
    }

    // Getters y Setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getNumDocumento() {
        return numDocumento;
    }

    public void setNumDocumento(String numDocumento) {
        this.numDocumento = numDocumento;
    }

    public int getTelefono() {
        return telefono;
    }

    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    @Override
    public String toString() {
        return "Persona{" + 
               "tipoDoc=" + tipoDocumento + 
               ", numDoc=" + numDocumento + 
               ", nombre=" + nombre + 
               ", apellido=" + apellido + 
               ", telefono=" + telefono + 
               ", direccion=" + direccion + '}';
    }
}
