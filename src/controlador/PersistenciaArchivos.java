package controlador;

/**
 *
 * @author tonyl
 */
import java.io.*;

public class PersistenciaArchivos implements PersistenciaHotel {

    private final String RUTA_ARCHIVO = "datos_hotel.dat";

    @Override
    public void guardarDatos(Hotel hotel) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(RUTA_ARCHIVO))) {

            oos.writeObject(hotel);
            System.out.println("Datos guardados en archivos .dat");

        } catch (IOException mondongo) {
            System.out.println("Error al guardar: " + mondongo.getMessage());
        }
    }
    @Override
    public Hotel cargarDatos(){
        File archivo = new File(RUTA_ARCHIVO);
        if(archivo.exists()){
            try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))){
                System.out.println("Datos cargados correctamente. ");
                return (Hotel) ois.readObject();
            }catch(IOException | ClassNotFoundException e){
                System.out.println("Error al cargar: "+e.getMessage());
            }
        }
        return null;
    }
    
}
