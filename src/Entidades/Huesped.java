package Entidades;
import java.io.Serializable;

public class Huesped extends Persona implements Serializable{

    private FichaHospedaje fichahospedaje;
    private static final long serialVersionUID = 1L;

    // Constructor 
    public Huesped(String nombre, String apellido, String tipoDocumento, String numDocumento, int telefono, String direccion) {
        super(nombre, apellido, tipoDocumento, numDocumento, telefono, direccion);
        this.fichahospedaje = null;
    }

    public FichaHospedaje getFichahospedaje() {
        return fichahospedaje;
    }

    public void setFichahospedaje(FichaHospedaje fichahospedaje) {
        this.fichahospedaje = fichahospedaje;
    }

    // Método para asignar la ficha posteriormente 
    public void asignarFichaHospedaje(FichaHospedaje ficha) {
        this.fichahospedaje = ficha;
    }

    // Mostrar hospedaje si existe
    public void registrarHospedaje() {
        if (fichahospedaje != null) {
            System.out.println(fichahospedaje.toString());
        } else {
            System.out.println("No se ha registrado el hospedaje.");
        }
    }

    @Override
    public String toString() {
        return "Huesped{"
                + "nombre='" + getNombre() + '\''
                + ", apellido='" + getApellido() + '\''
                // Actualizamos para mostrar el tipo y el número como String
                + ", documento=" + getTipoDocumento() + " " + getNumDocumento() 
                + ", telefono=" + getTelefono()
                + ", direccion='" + getDireccion() + '\''
                + ", fichahospedaje_id=" + (fichahospedaje != null ? fichahospedaje.getIdFicha() : "Ninguno")
                + '}';
    }
}