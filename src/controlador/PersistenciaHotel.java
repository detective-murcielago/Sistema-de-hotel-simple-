
package controlador;

/**
 *
 * @author tonyl
 */
public interface PersistenciaHotel {
    void guardarDatos(Hotel hotel);
    Hotel cargarDatos();
}
