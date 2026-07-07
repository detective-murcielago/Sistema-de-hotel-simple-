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
     * Descuenta un insumo del inventario por consumo real (limpieza o cocina).
     * Valida stock, aplica la baja, persiste y registra el consumo en el kardex.
     *
     * @param nombreProducto nombre exacto del producto en el inventario
     * @param cantidad       unidades consumidas (debe ser > 0)
     * @param area           "LIMPIEZA" o "COCINA"
     * @param referencia     nro de habitación o id de pedido asociado
     * @param responsable    empleado que registra el consumo
     * @return mensaje de resultado (empieza con "OK" si tuvo éxito)
     */
    public String consumirInsumo(String nombreProducto, int cantidad, String area,
                                 String referencia, String responsable) {
        if (cantidad <= 0) {
            return "ERROR: La cantidad debe ser mayor a cero.";
        }
        Entidades.Producto prod = null;
        for (Entidades.Producto p : hotel.getInventario()) {
            if (p.getNombre().equalsIgnoreCase(nombreProducto)) {
                prod = p;
                break;
            }
        }
        if (prod == null) {
            return "ERROR: El producto '" + nombreProducto + "' no existe en el inventario.";
        }
        if (cantidad > prod.getStock()) {
            return "ERROR: Stock insuficiente de '" + nombreProducto
                    + "'. Disponible: " + prod.getStock();
        }

        prod.setStock(prod.getStock() - cantidad);       // baja de stock
        persistencia.guardarDatos(hotel);                 // persiste inventario
        persistencia.registrarConsumo(nombreProducto, cantidad, area,
                referencia, responsable);                 // kardex / trazabilidad

        String aviso = prod.isStockCritico()
                ? "  ¡ATENCIÓN! '" + nombreProducto + "' quedó en nivel crítico (stock "
                  + prod.getStock() + ")."
                : "";
        return "OK: Se descontaron " + cantidad + " de '" + nombreProducto
                + "'. Stock restante: " + prod.getStock() + "." + aviso;
    }

    /**
     * Cierra la conexión MySQL al salir de la aplicación
     */
    public void cerrarConexion() {
        ConexionDB.cerrar();
    }
}
