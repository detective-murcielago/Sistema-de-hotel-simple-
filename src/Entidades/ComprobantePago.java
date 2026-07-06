package Entidades;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ComprobantePago implements Serializable {

    private static final long serialVersionUID = 1L;
    private String codigo;
    private FichaHospedaje fichahospedaje;
    private double importeTotal;
    private LocalDateTime fechaEmision;

    public ComprobantePago(String codigo, FichaHospedaje fichahospedaje, double importeTotal, LocalDateTime fechaEmision) {
        this.codigo = codigo;
        this.fichahospedaje = fichahospedaje;
        this.importeTotal = importeTotal;
        this.fechaEmision = fechaEmision;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public FichaHospedaje getFichahospedaje() {
        return fichahospedaje;
    }

    public void setFichahospedaje(FichaHospedaje fichahospedaje) {
        this.fichahospedaje = fichahospedaje;
    }

    public double getImporteTotal() {
        return importeTotal;
    }

    public void setImporteTotal(double importeTotal) {
        this.importeTotal = importeTotal;
    }

    public LocalDateTime getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(LocalDateTime fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

// Representación del comprobante tipo TICKET DE CAJA
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        // 1. Damos formato a las fechas para que se vean ordenadas
        java.time.format.DateTimeFormatter formatoFecha = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        java.time.format.DateTimeFormatter formatoSoloDia = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // 2. Extraemos las variables usando tus getters exactos
        Habitacion hab = fichahospedaje.getHabitacion();
        int noches = fichahospedaje.getNochesEsperadas();
        int personas = fichahospedaje.getCantidadPersonas();
        Huesped titular = fichahospedaje.getHuespedes().get(0);

        // --- INICIA EL DISEÑO DEL VOUCHER ---
        sb.append("===================================================\n");
        sb.append("          HOTEL TRUGARDEN - VOUCHER DE CONSUMO     \n");
        sb.append("====================================================\n");
        sb.append("Código V.: ").append(codigo).append("\n");
        sb.append("Emitido  : ").append(fechaEmision.format(formatoFecha)).append("\n");
        sb.append("-----------------------------------------\n");

        // DATOS DEL CLIENTE
        sb.append("Cliente  : ").append(titular.getNombre()).append(" ").append(titular.getApellido()).append("\n");
        sb.append("Número de Documento : ").append(titular.getNumDocumento()).append("\n");
        sb.append("-----------------------------------------\n");

        // DATOS DE ESTADÍA
        String tipoStr = (hab.getTipo() == 'S') ? "Simple" : (hab.getTipo() == 'D') ? "Doble" : "Matrimonial";
        sb.append("Habitación : ").append(hab.getNumero()).append(" (").append(tipoStr).append(")\n");
        sb.append("Ingreso    : ").append(fichahospedaje.getFechaIngreso().format(formatoSoloDia)).append("\n");

        // Usamos tu brillante método de fecha estimada
        sb.append("Salida Est.: ").append(fichahospedaje.getFechaSalidaEstimada().format(formatoSoloDia)).append("\n");
        sb.append("Noches     : ").append(noches).append("\n");
        sb.append("Huéspedes  : ").append(personas).append(" persona(s)\n");
        sb.append("-----------------------------------------\n");

        // CÁLCULO DE COSTOS - HABITACIÓN
        double subtotalHab = noches * hab.getPrecio();
        sb.append("DETALLE DE CARGOS EST. (AL CHECK-IN):\n");
        sb.append(String.format("- Alojamiento (%d noches x S/%.2f) : S/ %.2f\n", noches, hab.getPrecio(), subtotalHab));

        // CÁLCULO DE COSTOS - COMIDAS (20 soles por comida, por persona, multiplicados por las noches)
        double precioComida = 20.0;
        double subtotalComidas = 0.0;

        // Usamos tus booleanos exactos
        if (fichahospedaje.isIncluyeDesayuno()) {
            double totalDesayunos = precioComida * personas * noches;
            subtotalComidas += totalDesayunos;
            sb.append(String.format("- Desayuno (S/20 x %d pers. x %d d)  : S/ %.2f\n", personas, noches, totalDesayunos));
        }
        if (fichahospedaje.isIncluyeAlmuerzo()) {
            double totalAlmuerzos = precioComida * personas * noches;
            subtotalComidas += totalAlmuerzos;
            sb.append(String.format("- Almuerzo (S/20 x %d pers. x %d d)  : S/ %.2f\n", personas, noches, totalAlmuerzos));
        }
        if (fichahospedaje.isIncluyeCena()) {
            double totalCenas = precioComida * personas * noches;
            subtotalComidas += totalCenas;
            sb.append(String.format("- Cena     (S/20 x %d pers. x %d d)  : S/ %.2f\n", personas, noches, totalCenas));
        }

        sb.append("-----------------------------------------\n");
        // El importe total del voucher
        sb.append(String.format("IMPORTE TOTAL      : S/ %.2f\n", this.importeTotal));
        sb.append("=========================================\n");
        sb.append("      ¡Gracias por su preferencia!       \n");

        return sb.toString();
    }
}
