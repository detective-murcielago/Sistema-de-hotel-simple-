package controlador;

import java.io.Serializable;
import Entidades.*; // Importa todas las clases del paquete Entidades
import java.util.*; // Importa las utilidades estándar de Java (listas, etc.)
import java.time.LocalDate; // Importa la clase LocalDate para manejar fechas

public class Hotel implements Serializable {

    // Atributos del hotel
    private static final long serialVersionUID = 1L;
    private String nombre; // Nombre del hotel
    private String direccion; // Dirección del hotel
    private List<Huesped> ListaHuespedes; // Lista de huéspedes
    private List<Empleado> ListaEmpleados; // Lista de empleados
    private List<Habitacion> ListaHabitacion; // Lista de habitaciones
    private List<FichaHospedaje> ListaFHospedaje; // Lista de fichas de hospedaje
    private java.util.List<Entidades.Producto> inventario = new java.util.ArrayList<>();// Lista para el Jefe de Almacén
    private java.util.List<Entidades.OrdenCompra> listaOrdenes = new java.util.ArrayList<>();//Lista para jede de compra
    private String rolActual = "";  // Variable para recordar quién está usando el sistema en este momento
    private Empleado empleadoActual; // Empleado que inició sesión (usado por el Módulo RRHH)
    private java.util.List<Entidades.TurnoCaja> listaTurnos = new java.util.ArrayList<>();    // Lista para guardar el historial de cajas
    // --- Listas para las historias de usuario del recepcionista (F-004, F-010, F-011) ---
    private java.util.List<Entidades.PreferenciaHuesped> listaPreferencias = new java.util.ArrayList<>(); // F-004 / F-011
    private java.util.List<Entidades.PagoServicio> listaPagosServicio = new java.util.ArrayList<>();       // F-010
    // Constructor que inicializa el hotel con nombre, dirección y listas vacías

    public Hotel() {
        this.nombre = "TruGarden Hotel"; // Asigna nombre al hotel
        this.direccion = "av. El Golf 69"; // Asigna dirección al hotel
        this.ListaHuespedes = new ArrayList<>(); // Inicializa la lista de huéspedes
        this.ListaEmpleados = new ArrayList<>(); // Inicializa la lista de empleados
        this.ListaHabitacion = new ArrayList<>(); // Inicializa la lista de habitaciones
        this.ListaFHospedaje = new ArrayList<>(); // Inicializa la lista de fichas de hospedaje

    }

    public java.util.List<Entidades.TurnoCaja> getListaTurnos() {
        // Si la lista viene como 'null' desde el archivo viejo, la creamos en ese instante
        if (listaTurnos == null) {
            listaTurnos = new java.util.ArrayList<>();
        }
        return listaTurnos;
    }

    public java.util.List<Entidades.Producto> getInventario() {
        if (inventario == null) {
            inventario = new java.util.ArrayList<>();
        }
        return inventario;
    }

    public void setInventario(java.util.List<Entidades.Producto> inventario) {
        this.inventario = (inventario != null) ? inventario : new java.util.ArrayList<>();
    }

    public java.util.List<Entidades.OrdenCompra> getListaOrdenes() {
        if (listaOrdenes == null) {
            listaOrdenes = new java.util.ArrayList<>();
        }
        return listaOrdenes;
    }

    // Métodos getter y setter para los atributos del hotel
    public String getNombre() {
        return nombre; // Retorna el nombre del hotel
    }

    public void setNombre(String nombre) {
        this.nombre = nombre; // Asigna un nuevo nombre al hotel
    }

    public String getDireccion() {
        return direccion; // Retorna la dirección del hotel
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion; // Asigna una nueva dirección al hotel
    }

    public List<Huesped> getListaHuespedes() {
        return ListaHuespedes; // Retorna la lista de huéspedes
    }

    public void setListaHuespedes(List<Huesped> ListaHuespedes) {
        this.ListaHuespedes = ListaHuespedes; // Asigna una nueva lista de huéspedes
    }

    public List<Empleado> getListaEmpleados() {
        return ListaEmpleados; // Retorna la lista de empleados
    }

    public void setListaEmpleados(List<Empleado> ListaEmpleados) {
        this.ListaEmpleados = ListaEmpleados; // Asigna una nueva lista de empleados
    }

    public List<Habitacion> getListaHabitacion() {
        return ListaHabitacion; // Retorna la lista de habitaciones
    }

    public void setListaHabitacion(List<Habitacion> ListaHabitacion) {
        this.ListaHabitacion = ListaHabitacion; // Asigna una nueva lista de habitaciones
    }

    public List<FichaHospedaje> getListaFHospedaje() {
        return ListaFHospedaje; // Retorna la lista de fichas de hospedaje
    }

    public void setListaFHospedaje(List<FichaHospedaje> ListaFHospedaje) {
        this.ListaFHospedaje = ListaFHospedaje; // Asigna una nueva lista de fichas
    }

    public String getRolActual() {
        return rolActual;
    }

    public void setRolActual(String rolActual) {
        this.rolActual = rolActual;
    }

    public Empleado getEmpleadoActual() {
        return empleadoActual;
    }

    public void setEmpleadoActual(Empleado empleadoActual) {
        this.empleadoActual = empleadoActual;
    }

    // Muestra la lista de huéspedes registrados
    public void ListadeHuespedes() {
        System.out.println("----Lista de huespedes----"); // Título
        for (Huesped h : ListaHuespedes) { // Recorre cada huésped
            System.out.println(h.toString()); // Imprime su información
        }
    }

    // Registra un nuevo huésped validando duplicados
    public boolean registrarHuesped(Huesped h) {
        if (h == null) { // Verifica si el huésped es nulo
            System.out.println("No se pudo registrar el huésped: objeto nulo.");
            return false;
        }

        // Verifica si ya existe un huésped con el mismo Documento o teléfono
        for (Huesped existente : ListaHuespedes) {
            if (existente.getNumDocumento().equals(h.getNumDocumento())) { // Duplicado por Documento
                System.out.println("Error: ya existe un huésped con el mismo número de documento.");
                return false;
            }
            if (existente.getTelefono() == h.getTelefono()) { // Duplicado por teléfono
                System.out.println("Error: ya existe un huésped con el mismo teléfono.");
                return false;
            }
        }

        ListaHuespedes.add(h); // Agrega el huésped a la lista
        System.out.println("Huésped registrado: " + h); // Confirma registro
        return true; // Registro exitoso
    }

    // Busca un huésped por su Número de Documento 
    public Huesped buscarHuespedPorDocumento(String numDocumento) {
        for (Huesped h : this.ListaHuespedes) { // Recorre la lista de huéspedes
            if (h.getNumDocumento().equals(numDocumento)) { // Compara Documento
                return h; // Retorna si lo encuentra
            }
        }
        return null; // Retorna null si no lo encuentra
    }

    // Devuelve los huéspedes hospedados en una habitación específica
    public List<Huesped> obtenerHuespedxHabitacion(String numeroHabitacion) {
        List<Huesped> resultado = new ArrayList<>(); // Lista temporal para almacenar huéspedes
        for (FichaHospedaje ficha : ListaFHospedaje) { // Recorre todas las fichas
            if (ficha.getHabitacion().getNumero().equals(numeroHabitacion)) { // Verifica número de habitación
                resultado.addAll(ficha.getHuespedes()); // Agrega todos los huéspedes de esa ficha
            }
        }
        return resultado; // Retorna la lista de huéspedes
    }

    // Registra un nuevo empleado, validando duplicados
    public boolean registrarEmpleado(Empleado empleado) {
        if (empleado == null) { // Verifica si es nulo
            System.out.println("No se pudo registrar el empleado: objeto nulo.");
            return false;
        }

        // Verifica duplicidad de ID, Documento y teléfono
        for (Empleado existente : ListaEmpleados) {
            if (existente.getId() == empleado.getId()) {
                System.out.println("Error: ya existe un empleado con el mismo ID.");
                return false;
            }
            if (existente.getNumDocumento().equals(empleado.getNumDocumento())) {
                System.out.println("Error: ya existe un empleado con el mismo número de documento.");
                return false;
            }
            if (existente.getTelefono() == empleado.getTelefono()) {
                System.out.println("Error: ya existe un empleado con el mismo teléfono.");
                return false;
            }
        }

        ListaEmpleados.add(empleado); // Agrega el empleado
        System.out.println("Empleado registrado: " + empleado); // Muestra confirmación
        return true;
    }

    // Busca un empleado por su ID
    public Empleado buscarEmpleadoPorId(int id) {
        for (Empleado empleado : ListaEmpleados) { // Recorre empleados
            if (empleado.getId() == id) { // Compara ID
                return empleado; // Retorna si lo encuentra
            }
        }
        return null; // Retorna null si no lo encuentra
    }

    // Busca un empleado por su Número de Documento 
    public Empleado buscarEmpleadoPorDocumento(String numDocumento) {
        for (Empleado e : ListaEmpleados) { // Recorre empleados
            if (e.getNumDocumento().equals(numDocumento)) { // Compara Documento
                return e; // Retorna si lo encuentra
            }
        }
        return null; // No encontrado
    }

    // Lista todos los empleados
    public void listarEmpleados() {
        System.out.println("---- Lista de empleados ----"); // Título
        for (Empleado emp : ListaEmpleados) { // Recorre y muestra empleados
            System.out.println(emp);
        }
    }

    // Marca una habitación como en mantenimiento
    public void Mantenimiento_Habitacion(String numero) {
        Habitacion h = buscarHabitacionporNumero(numero); // Busca la habitación
        if (h != null) { // Si la encuentra
            h.setEstado('M'); // M = Mantenimiento
            System.out.println("La habitación " + numero + " ha sido marcada como en mantenimiento.");
        }
    }

    // Calcula el total de ventas para un día específico
    public double reporteVentasxDia(LocalDate fecha) {
        double total = 0; // Total acumulado
        for (FichaHospedaje ficha : ListaFHospedaje) { // Recorre fichas
            if (ficha.getFechaSalida() != null // Verifica si tiene salida
                    && ficha.getFechaSalida().toLocalDate().equals(fecha) // Verifica fecha
                    && ficha.getEstado() == 'F') { // Verifica estado finalizado
                total += ficha.calcularImporteTotal(); // Suma el importe
            }
        }
        return total; // Retorna el total
    }

    // Registra una nueva habitación
    public void registrarHabitacion(Habitacion h) {
        ListaHabitacion.add(h); // Agrega la habitación a la lista
    }

    // Busca una habitación por número
    public Habitacion buscarHabitacionporNumero(String numero) {
        for (Habitacion h : ListaHabitacion) { // Recorre la lista
            if (h.getNumero().equals(numero)) { // Compara número
                return h; // Retorna si la encuentra
            }
        }
        return null; // No encontrada
    }

    // Elimina una habitación por número
    public void eliminarHabitacion(String numero) {
        ListaHabitacion.removeIf(h -> h.getNumero().equals(numero)); // Elimina si el número coincide
    }

    // Retorna todas las habitaciones
    public List<Habitacion> listarHabitacion() {
        return ListaHabitacion; // Devuelve la lista de habitaciones
    }

    // Registra una nueva ficha de hospedaje
    public void registrarFichaHospedaje(FichaHospedaje ficha) {
        this.ListaFHospedaje.add(ficha); // Agrega la ficha a la lista
    }

    // Busca una ficha activa por Número de Documento del huésped 
    public FichaHospedaje buscarFichaPorHuespedDocumento(String numDocumento) {
        for (FichaHospedaje ficha : ListaFHospedaje) { // Recorre fichas
            for (Huesped h : ficha.getHuespedes()) { // Recorre huéspedes
                if (h.getNumDocumento().equals(numDocumento) && ficha.getFechaSalida() == null) {
                    return ficha; // Retorna ficha activa
                }
            }
        }
        return null; // No encontrada
    }

    /**
     * Busca la ficha ACTIVA (estado 'A', sin fecha de salida) de un huésped por
     * su documento. Usado en F-010 para cobrar el total antes de dar las llaves.
     */
    public FichaHospedaje buscarFichaActivaPorDocumento(String numDocumento) {
        for (FichaHospedaje ficha : ListaFHospedaje) {
            if (ficha.getEstado() == 'A' && ficha.getFechaSalida() == null) {
                for (Huesped h : ficha.getHuespedes()) {
                    if (h.getNumDocumento().equals(numDocumento)) {
                        return ficha;
                    }
                }
            }
        }
        return null;
    }

    // Busca ficha por habitación
    public FichaHospedaje buscarFichaPorHabitacion(Habitacion habitacion) {
        for (FichaHospedaje ficha : ListaFHospedaje) {
            if (ficha.getHabitacion().equals(habitacion)) {
                return ficha;
            }
        }
        return null;
    }

    //eliminar empleado por id
    public boolean eliminarEmpleado(int id) {
        return ListaEmpleados.removeIf(e -> e.getId() == id);
    }

    public boolean actualizarEmpleado(Empleado empleadoActualizado) {
        for (int i = 0; i < ListaEmpleados.size(); i++) {
            if (ListaEmpleados.get(i).getId() == empleadoActualizado.getId()) {
                ListaEmpleados.set(i, empleadoActualizado);
                return true;
            }
        }
        return false;
    }
    // Método para actualizar una habitación existente

    public boolean actualizarHabitacion(Habitacion habitacionActualizada) {
        // Recorremos la lista de habitaciones
        for (int i = 0; i < ListaHabitacion.size(); i++) {
            // Buscamos si el número de habitación coincide (usamos .equals porque el número es un String)
            if (ListaHabitacion.get(i).getNumero().equals(habitacionActualizada.getNumero())) {
                // Si la encuentra, la reemplazamos con la nueva información
                ListaHabitacion.set(i, habitacionActualizada);
                return true; // Actualización exitosa
            }
        }
        return false; // No se encontró la habitación
    }
    // Agrega esto en controlador/Hotel.java

    public int contarHabitacionesPorEstado(char estado) {
        int contador = 0;
        for (Habitacion h : ListaHabitacion) {
            if (h.getEstado() == estado) {
                contador++;
            }
        }
        return contador;
    }

    // =====================================================================
    //  HISTORIAS DE USUARIO DEL RECEPCIONISTA
    //  F-004 / F-011  -> Preferencias del huésped
    //  F-010          -> Pago de servicios contratados
    //  F-005 / F-011  -> Historial de estadías (reutiliza ListaFHospedaje)
    // =====================================================================

    // ---------- Getters/Setters de las listas nuevas ----------
    public List<Entidades.PreferenciaHuesped> getListaPreferencias() {
        if (listaPreferencias == null) listaPreferencias = new ArrayList<>();
        return listaPreferencias;
    }

    public void setListaPreferencias(List<Entidades.PreferenciaHuesped> listaPreferencias) {
        this.listaPreferencias = listaPreferencias;
    }

    public List<Entidades.PagoServicio> getListaPagosServicio() {
        if (listaPagosServicio == null) listaPagosServicio = new ArrayList<>();
        return listaPagosServicio;
    }

    public void setListaPagosServicio(List<Entidades.PagoServicio> listaPagosServicio) {
        this.listaPagosServicio = listaPagosServicio;
    }

    // ---------------------------------------------------------------------
    //  F-004: Registrar preferencia del huésped
    //  - Escenario "Preferencia no registrada": valida datos vacíos
    //  - Escenario "Preferencia registrada exitosamente": la agrega a memoria
    // ---------------------------------------------------------------------
    public boolean registrarPreferencia(Entidades.PreferenciaHuesped pref) {
        if (pref == null
                || pref.getNumDocumentoHuesped() == null || pref.getNumDocumentoHuesped().trim().isEmpty()
                || pref.getTipoPreferencia() == null || pref.getTipoPreferencia().trim().isEmpty()
                || pref.getDetalle() == null || pref.getDetalle().trim().isEmpty()) {
            return false; // No se registró: faltan datos
        }
        // El huésped debe existir
        if (buscarHuespedPorDocumento(pref.getNumDocumentoHuesped()) == null) {
            return false;
        }
        getListaPreferencias().add(pref);
        return true;
    }

    // ---------------------------------------------------------------------
    //  F-004 / F-011: Consultar preferencias registradas de un huésped
    //  Devuelve lista vacía si el huésped no tiene preferencias.
    // ---------------------------------------------------------------------
    public List<Entidades.PreferenciaHuesped> obtenerPreferenciasHuesped(String numDocumento) {
        List<Entidades.PreferenciaHuesped> resultado = new ArrayList<>();
        if (numDocumento == null) return resultado;
        for (Entidades.PreferenciaHuesped p : getListaPreferencias()) {
            if (p.getNumDocumentoHuesped() != null
                    && p.getNumDocumentoHuesped().equalsIgnoreCase(numDocumento.trim())) {
                resultado.add(p);
            }
        }
        return resultado;
    }

    // ---------------------------------------------------------------------
    //  F-010: Registrar el pago de un servicio contratado
    //  - "Validación de datos": rechaza campos incompletos / monto <= 0
    //  - "Registro exitoso de pago": lo agrega a memoria
    // ---------------------------------------------------------------------
    public boolean registrarPagoServicio(Entidades.PagoServicio pago) {
        if (pago == null
                || pago.getNumDocumentoHuesped() == null || pago.getNumDocumentoHuesped().trim().isEmpty()
                || pago.getServicio() == null || pago.getServicio().trim().isEmpty()
                || pago.getMetodoPago() == null || pago.getMetodoPago().trim().isEmpty()
                || pago.getMonto() <= 0) {
            return false; // Datos incompletos o monto inválido
        }
        if (buscarHuespedPorDocumento(pago.getNumDocumentoHuesped()) == null) {
            return false;
        }
        // Regla de negocio: una estadía (ficha) no puede pagarse dos veces.
        // Si la ficha asociada al pago ya tiene un pago registrado, se rechaza.
        if (fichaYaPagada(pago.getIdFicha())) {
            return false;
        }
        getListaPagosServicio().add(pago);
        return true;
    }

    // ---------------------------------------------------------------------
    //  F-010: Indica si una ficha de hospedaje ya tiene un pago registrado.
    //  Evita cobrar dos veces la misma estadía.
    // ---------------------------------------------------------------------
    public boolean fichaYaPagada(String idFicha) {
        if (idFicha == null || idFicha.trim().isEmpty()) return false;
        for (Entidades.PagoServicio p : getListaPagosServicio()) {
            if (p.getIdFicha() != null
                    && p.getIdFicha().equalsIgnoreCase(idFicha.trim())) {
                return true;
            }
        }
        return false;
    }

    // ---------------------------------------------------------------------
    //  F-010: Consultar los pagos registrados de un huésped
    // ---------------------------------------------------------------------
    public List<Entidades.PagoServicio> obtenerPagosHuesped(String numDocumento) {
        List<Entidades.PagoServicio> resultado = new ArrayList<>();
        if (numDocumento == null) return resultado;
        for (Entidades.PagoServicio p : getListaPagosServicio()) {
            if (p.getNumDocumentoHuesped() != null
                    && p.getNumDocumentoHuesped().equalsIgnoreCase(numDocumento.trim())) {
                resultado.add(p);
            }
        }
        return resultado;
    }

    // ---------------------------------------------------------------------
    //  F-005 / F-011: Historial de estadías de un huésped
    //  Reutiliza ListaFHospedaje. Devuelve todas las fichas donde el
    //  huésped (por documento) aparezca como titular o acompañante.
    // ---------------------------------------------------------------------
    public List<FichaHospedaje> obtenerHistorialEstadias(String numDocumento) {
        List<FichaHospedaje> resultado = new ArrayList<>();
        if (numDocumento == null) return resultado;
        for (FichaHospedaje f : ListaFHospedaje) {
            if (f.getHuespedes() == null) continue;
            for (Huesped h : f.getHuespedes()) {
                if (h.getNumDocumento() != null
                        && h.getNumDocumento().equalsIgnoreCase(numDocumento.trim())) {
                    resultado.add(f);
                    break;
                }
            }
        }
        return resultado;
    }

    // ---------------------------------------------------------------------
    //  F-005: Historial de estadías filtrado por rango de fechas (por ingreso)
    // ---------------------------------------------------------------------
    public List<FichaHospedaje> obtenerHistorialEstadiasPorFecha(String numDocumento,
                                                                 java.time.LocalDate desde,
                                                                 java.time.LocalDate hasta) {
        List<FichaHospedaje> base = obtenerHistorialEstadias(numDocumento);
        if (desde == null && hasta == null) return base;
        List<FichaHospedaje> resultado = new ArrayList<>();
        for (FichaHospedaje f : base) {
            if (f.getFechaIngreso() == null) continue;
            java.time.LocalDate ingreso = f.getFechaIngreso().toLocalDate();
            boolean okDesde = (desde == null) || !ingreso.isBefore(desde);
            boolean okHasta = (hasta == null) || !ingreso.isAfter(hasta);
            if (okDesde && okHasta) {
                resultado.add(f);
            }
        }
        return resultado;
    }
}