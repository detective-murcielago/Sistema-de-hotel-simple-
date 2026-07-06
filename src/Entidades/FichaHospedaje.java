package Entidades;

import java.io.Serializable;
import java.time.temporal.ChronoUnit;
import java.time.LocalDateTime;
import java.util.List;

public class FichaHospedaje implements Serializable {

    private static final long serialVersionUID = 1L;
    private String idFicha;
    private List<Huesped> huespedes;
    private Habitacion habitacion;
    private int nochesEsperadas;
    private LocalDateTime fechaIngreso;
    private LocalDateTime fechaSalida;
    private char estado;
    private int cantidadPersonas;
    private boolean arqueada = false;   // Para saber si esta venta ya fue procesada en un cierre de turno anterior
    // --- Atributos para servicios extras ---
    private boolean incluyeDesayuno;
    private boolean incluyeAlmuerzo;
    private boolean incluyeCena;
    private String estadoComida = "Por entregar";

    // <-- constructor 
    public FichaHospedaje(String idFicha, List<Huesped> huespedes, Habitacion habitacion, int nochesEsperadas, LocalDateTime fechaIngreso, int cantidadPersona, boolean incluyeDesayuno, boolean incluyeAlmuerzo, boolean incluyeCena) {
        this.idFicha = idFicha;
        this.huespedes = huespedes;
        this.habitacion = habitacion;
        this.nochesEsperadas = nochesEsperadas;
        this.fechaIngreso = fechaIngreso;
        this.fechaSalida = null;
        this.estado = 'A';
        this.cantidadPersonas = cantidadPersona;
        this.incluyeDesayuno = incluyeDesayuno;
        this.incluyeAlmuerzo = incluyeAlmuerzo;
        this.incluyeCena = incluyeCena;
    }

    public double calcularImporteTotal() {
        if (fechaSalida == null) {
            return 0.0;
        }

        long noches = ChronoUnit.DAYS.between(fechaIngreso.toLocalDate(), fechaSalida.toLocalDate());

        if (noches == 0) {
            noches = 1;
        }

        // Costo base de la habitación
        double totalHabitacion = noches * habitacion.getPrecio();

        // --- Cálculo de servicios extras ---
        double costoExtras = 0.0;
        double precioPorComida = 20.0;
        double costoComidaTotalPorNoche = 0.0;

        if (incluyeDesayuno) {
            costoComidaTotalPorNoche += precioPorComida;
        }
        if (incluyeAlmuerzo) {
            costoComidaTotalPorNoche += precioPorComida;
        }
        if (incluyeCena) {
            costoComidaTotalPorNoche += precioPorComida;
        }

        // Se multiplica la comida por las personas y por las noches que se quedan
        costoExtras = costoComidaTotalPorNoche * cantidadPersonas * noches;

        return totalHabitacion + costoExtras;
    }

    // Calculamos la salida estimada sumando DÍAS
    public LocalDateTime getFechaSalidaEstimada() {
        return fechaIngreso.plusDays(nochesEsperadas);
    }

    /**
     * Total a cobrar de la ficha usando las NOCHES ESPERADAS (no depende de la
     * fecha de salida). Se usa en F-010 "Pago de servicios": el huésped paga el
     * total de su ficha ANTES de recibir las llaves, cuando la estadía aún está
     * activa y todavía no tiene fecha de salida registrada.
     */
    public double calcularTotalFicha() {
        int noches = (nochesEsperadas <= 0) ? 1 : nochesEsperadas;
        double totalHabitacion = noches * habitacion.getPrecio();

        double precioPorComida = 20.0;
        double comidaPorNoche = 0.0;
        if (incluyeDesayuno) comidaPorNoche += precioPorComida;
        if (incluyeAlmuerzo) comidaPorNoche += precioPorComida;
        if (incluyeCena)     comidaPorNoche += precioPorComida;

        double costoExtras = comidaPorNoche * cantidadPersonas * noches;
        return totalHabitacion + costoExtras;
    }

    // Getters y setters
    public String getIdFicha() {
        return idFicha;
    }

    public void setIdFicha(String idFicha) {
        this.idFicha = idFicha;
    }

    public List<Huesped> getHuespedes() {
        return huespedes;
    }

    public void setHuespedes(List<Huesped> huespedes) {
        this.huespedes = huespedes;
    }

    public Habitacion getHabitacion() {
        return habitacion;
    }

    public void setHabitacion(Habitacion habitacion) {
        this.habitacion = habitacion;
    }

    public int getNochesEsperadas() {
        return nochesEsperadas;
    }

    public void setNochesEsperadas(int nochesEsperadas) {
        this.nochesEsperadas = nochesEsperadas;
    }

    public String getEstadoComida() {
        return estadoComida;
    }

    public void setEstadoComida(String estadoComida) {
        this.estadoComida = estadoComida;
    }

    public LocalDateTime getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(LocalDateTime fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public LocalDateTime getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(LocalDateTime fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    public char getEstado() {
        return estado;
    }

    public void setEstado(char estado) {
        this.estado = estado;
    }

    public int getCantidadPersonas() {
        return cantidadPersonas;
    }

    public void setCantidadPersonas(int cantidadPersonas) {
        this.cantidadPersonas = cantidadPersonas;
    }

    // --- NUEVO: Getters y Setters de los extras ---
    public boolean isIncluyeDesayuno() {
        return incluyeDesayuno;
    }

    public void setIncluyeDesayuno(boolean incluyeDesayuno) {
        this.incluyeDesayuno = incluyeDesayuno;
    }

    public boolean isIncluyeAlmuerzo() {
        return incluyeAlmuerzo;
    }

    public void setIncluyeAlmuerzo(boolean incluyeAlmuerzo) {
        this.incluyeAlmuerzo = incluyeAlmuerzo;
    }

    public boolean isIncluyeCena() {
        return incluyeCena;
    }

    public void setIncluyeCena(boolean incluyeCena) {
        this.incluyeCena = incluyeCena;
    }

    public void registrarSalida() {
        this.fechaSalida = LocalDateTime.now();
        this.estado = 'F';
    }

    public boolean isArqueada() {
        return arqueada;
    }

    public void setArqueada(boolean arqueada) {
        this.arqueada = arqueada;
    }

    @Override
    public String toString() {
        return "FichaHospedaje{"
                + "idFicha='" + idFicha + '\''
                + ", habitacionNumero=" + (habitacion != null ? habitacion.getNumero() : "Sin asignar")
                + ", cantidadHuespedes=" + (huespedes != null ? huespedes.size() : 0)
                + ", nochesEsperadas=" + nochesEsperadas
                + ", fechaIngreso=" + fechaIngreso
                + ", fechaSalida=" + (fechaSalida != null ? fechaSalida : "No registrada")
                + ", estado=" + estado
                + ", cantidadPersonas=" + cantidadPersonas
                + ", desayuno=" + (incluyeDesayuno ? "Si" : "No")
                + ", almuerzo=" + (incluyeAlmuerzo ? "Si" : "No")
                + ", cena=" + (incluyeCena ? "Si" : "No")
                + '}';
    }
}
