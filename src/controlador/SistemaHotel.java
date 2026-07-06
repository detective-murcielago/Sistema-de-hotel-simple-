package controlador;

/**
 * SistemaHotel modificado — usa MySQL en vez del archivo .dat Reemplaza el
 * contenido de src/controlador/SistemaHotel.java Solo cambia la línea: new
 * PersistenciaArchivos() → new PersistenciaMySQL()
 */
public class SistemaHotel {

    private static SistemaHotel instancia;
    private Hotel hotel;
    private PersistenciaHotel persistencia;

    private SistemaHotel() {
        // ← ÚNICO CAMBIO: swapping de PersistenciaArchivos a PersistenciaMySQL
        persistencia = new PersistenciaMySQL();

        hotel = persistencia.cargarDatos();
        if (hotel == null) {
            hotel = new Hotel();
        }
    }

    public static SistemaHotel getInstancia() {
        if (instancia == null) {
            instancia = new SistemaHotel();
        }
        return instancia;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void guardarCambios() {
        persistencia.guardarDatos(hotel);
    }

    /**
     * Cierra la conexión MySQL al salir de la aplicación
     */
    public void cerrarConexion() {
        ConexionDB.cerrar();
    }
}
