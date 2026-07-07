
package controlador;

/**
 *
 * @author tonyl
 */
public interface PersistenciaHotel {
    void guardarDatos(Hotel hotel);
    Hotel cargarDatos();
    // Registra un consumo de insumo para trazabilidad (kardex).
    void registrarConsumo(String producto, int cantidad, String area,
                          String referencia, String responsable);
}
