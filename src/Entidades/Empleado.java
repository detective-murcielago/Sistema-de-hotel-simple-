package Entidades;

import java.io.Serializable;
import java.util.Date; 

public class Empleado extends Persona implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private String rol;
    private double sueldo;
    private String correo;
    private Date inicioContrato;
    private Date finContrato;

    // CONSTRUCTOR
    public Empleado(int idEmpleado, String rol, double sueldo, String correo, Date inicioContrato, Date finContrato, String nombre, String apellido, String tipoDocumento, String numDocumento, int telefono, String direccion) {
        
        super(nombre, apellido, tipoDocumento, numDocumento, telefono, direccion);
        
        this.id = idEmpleado;
        this.rol = rol;
        this.sueldo = sueldo;
        this.correo = correo;
        this.inicioContrato = inicioContrato;
        this.finContrato = finContrato;
    }

    // --- GETTERS Y SETTERS ---
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public double getSueldo() {
        return sueldo;
    }

    public void setSueldo(double sueldo) {
        this.sueldo = sueldo;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public Date getInicioContrato() {
        return inicioContrato;
    }

    public void setInicioContrato(Date inicioContrato) {
        this.inicioContrato = inicioContrato;
    }

    public Date getFinContrato() {
        return finContrato;
    }

    public void setFinContrato(Date finContrato) {
        this.finContrato = finContrato;
    }

    // --- TO STRING  ---
    @Override
    public String toString() {
        return "Empleado{"
                + "id=" + id
                + ", rol='" + rol + '\''
                + ", sueldo=S/." + sueldo
                + ", correo='" + correo + '\''
                + ", nombre='" + getNombre() + " " + getApellido() + '\''
                + ", documento=" + getTipoDocumento() + " " + getNumDocumento() 
                + ", telefono=" + getTelefono()
                + ", direccion='" + getDireccion() + '\''
                + ", inicioContrato=" + inicioContrato
                + ", finContrato=" + finContrato
                + '}';
    }
}