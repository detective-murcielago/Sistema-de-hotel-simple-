package Interfaz;

import controlador.Hotel;
import controlador.SistemaHotel;
import Entidades.Habitacion;
import Entidades.FichaHospedaje;
import Entidades.Producto;
import Entidades.OrdenCompra;
import Entidades.TurnoCaja;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.geom.*;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import controlador.ConexionDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.temporal.WeekFields;

public class Dashboard_GerenteGeneral extends javax.swing.JFrame {

    // =========================================================
    // COLORES
    // =========================================================
    private static final Color VERDE_HEADER    = new Color(33,  87,  50);
    private static final Color VERDE_TEXTO     = new Color(15,  110, 86);
    private static final Color ROJO_TEXTO      = new Color(153, 45,  45);
    private static final Color AMBAR_TEXTO     = new Color(133, 79,  11);
    private static final Color AZUL_TEXTO      = new Color(12,  68,  124);
    private static final Color GRIS_FONDO      = new Color(245, 245, 245);
    private static final Color GRIS_BORDE      = new Color(222, 222, 222);
    private static final Color GRIS_TEXTO_SEC  = new Color(110, 110, 105);
    private static final Color GRIS_TEXTO_TER  = new Color(150, 150, 145);
    private static final Color AMBAR_BORDE     = new Color(239, 159, 39);
    private static final Color VERDE_BG_BADGE  = new Color(234, 243, 222);
    private static final Color VERDE_BADGE_TXT = new Color(39,  80,  10);
    private static final Color AMBAR_BG_BADGE  = new Color(250, 238, 218);
    private static final Color AMBAR_BADGE_TXT = new Color(99,  56,  6);

    private final DecimalFormat soles = new DecimalFormat("S/ #,##0.00");

    // =========================================================
    // REFERENCIA AL SISTEMA (serialización)
    // =========================================================
    private final Hotel hotel;

    // =========================================================
    // COMPONENTES DINÁMICOS
    // =========================================================
    private JLabel  lblOcupacionValor, lblMantenimientoValor, lblAlertasValor;
    private JLabel  lblIngresosValor,  lblEgresosValor,       lblUtilidadValor;
    private JPanel  panelGraficoContenedor, panelTarjetaResultados, panelTarjetaBalance;
    private JLabel  lblTituloGrafico;
    private DefaultTableModel modeloTablaAlertas;

    // HU F-001: panel del Libro Mayor incrustado como pestana
    private LibroMayor panelLibroMayor;

    // Graficos de preferencias de huespedes (pestana Preferencias)
    private JPanel  panelPrefHabitacion, panelPrefComida;
    private JComboBox<String> comboPeriodoPref;

    // =========================================================
    // CONSTRUCTOR
    // =========================================================
    public Dashboard_GerenteGeneral() {
        // Obtener hotel desde el Singleton (serialización)
        this.hotel = SistemaHotel.getInstancia().getHotel();
        initComponents();
        postInit();
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
    }

    private void postInit() {
        // Labels resumen
        lblOcupacionValor     = new JLabel("--%");
        lblMantenimientoValor = new JLabel("--");
        lblAlertasValor       = new JLabel("--");
        lblIngresosValor      = new JLabel("S/ --");
        lblEgresosValor       = new JLabel("S/ --");
        lblUtilidadValor      = new JLabel("S/ --");

        panelResumen.setLayout(new GridLayout(2, 3, 16, 16));
        panelResumen.setBorder(new EmptyBorder(24, 24, 24, 24));
        panelResumen.add(crearTarjetaResumen("Ocupación actual",              lblOcupacionValor,     VERDE_TEXTO));
        panelResumen.add(crearTarjetaResumen("Habitaciones en mantenimiento", lblMantenimientoValor, AMBAR_TEXTO));
        panelResumen.add(crearTarjetaResumen("Productos en alerta de stock",  lblAlertasValor,       ROJO_TEXTO));
        panelResumen.add(crearTarjetaResumen("Ingresos del mes",              lblIngresosValor,      VERDE_TEXTO));
        panelResumen.add(crearTarjetaResumen("Egresos del mes",               lblEgresosValor,       ROJO_TEXTO));
        panelResumen.add(crearTarjetaResumen("Utilidad del mes",              lblUtilidadValor,      AZUL_TEXTO));

        // Gráfico
        lblTituloGrafico = new JLabel("Ingresos vs Egresos");
        lblTituloGrafico.setFont(new Font("Segoe UI", Font.BOLD, 15));
        panelGraficoContenedor = new JPanel(new BorderLayout());
        panelGraficoContenedor.setBackground(Color.WHITE);
        panelWrapperGrafico.setLayout(new BorderLayout(0, 8));
        panelWrapperGrafico.add(lblTituloGrafico,       BorderLayout.NORTH);
        panelWrapperGrafico.add(panelGraficoContenedor, BorderLayout.CENTER);

        // Tarjetas financieras
        panelTarjetaResultados = new JPanel();
        panelTarjetaBalance    = new JPanel();
        panelFilaTarjetas.setLayout(new GridLayout(1, 2, 16, 0));
        panelFilaTarjetas.add(panelTarjetaResultados);
        panelFilaTarjetas.add(panelTarjetaBalance);

        // Tabla alertas
        modeloTablaAlertas = new DefaultTableModel(
            new Object[]{"Producto","Tipo","Stock actual","Stock mínimo","Estado"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaAlertas.setModel(modeloTablaAlertas);
        tablaAlertas.setRowHeight(28);

        cargarTodo();
    }

    // =========================================================
    // CÁLCULOS REALES DESDE SERIALIZACIÓN
    // =========================================================

    /** Ingresos del mes actual: suma el dinero físico de los cierres de caja de este mes */
    private double calcularIngresosMesActual() {
        double total = 0;
        int mesActual = LocalDate.now().getMonthValue();
        int anioActual = LocalDate.now().getYear();
        for (Entidades.TurnoCaja t : hotel.getListaTurnos()) {
            if (t.getFechaCierre() != null
                    && t.getFechaCierre().getMonthValue() == mesActual
                    && t.getFechaCierre().getYear() == anioActual) {
                total += t.getTotalFisico();
            }
        }
        return total;
    }

    /** Egresos del mes actual: suma órdenes de compra APROBADAS de este mes */
    private double calcularEgresosMesActual() {
        double total = 0;
        int mesActual = LocalDate.now().getMonthValue();
        int anioActual = LocalDate.now().getYear();
        for (OrdenCompra o : hotel.getListaOrdenes()) {
            if ("Aprobado".equalsIgnoreCase(o.getEstado()) && o.getFechaEmision() != null) {
                java.util.Calendar cal = java.util.Calendar.getInstance();
                cal.setTime(o.getFechaEmision());
                int mes  = cal.get(java.util.Calendar.MONTH) + 1;
                int anio = cal.get(java.util.Calendar.YEAR);
                if (mes == mesActual && anio == anioActual) {
                    total += o.getPrecioTotal();
                }
            }
        }
        return total;
    }

    /** Ingresos agrupados por mes para el año dado (dinero físico de cierres de caja) */
    private double[] calcularIngresosPorMes(int anio) {
        double[] ing = new double[12];
        for (Entidades.TurnoCaja t : hotel.getListaTurnos()) {
            if (t.getFechaCierre() != null && t.getFechaCierre().getYear() == anio) {
                ing[t.getFechaCierre().getMonthValue() - 1] += t.getTotalFisico();
            }
        }
        return ing;
    }

    /** Ingresos por semana del mes actual (4 semanas), según cierres de caja */
    private double[] calcularIngresosPorSemana(int mes, int anio) {
        double[] ing = new double[4];
        for (Entidades.TurnoCaja t : hotel.getListaTurnos()) {
            if (t.getFechaCierre() != null
                    && t.getFechaCierre().getMonthValue() == mes
                    && t.getFechaCierre().getYear() == anio) {
                int dia = t.getFechaCierre().getDayOfMonth();
                int idx = Math.min((dia - 1) / 7, 3);
                ing[idx] += t.getTotalFisico();
            }
        }
        return ing;
    }

    /** Ingresos por cada día de la semana actual (lun a dom), según cierres de caja */
    private double[] calcularIngresosPorDiaSemana() {
        double[] ing = new double[7];
        LocalDate hoy = LocalDate.now();
        LocalDate lunes = hoy.minusDays(hoy.getDayOfWeek().getValue() - 1);
        for (Entidades.TurnoCaja t : hotel.getListaTurnos()) {
            if (t.getFechaCierre() != null) {
                LocalDate fecha = t.getFechaCierre().toLocalDate();
                if (!fecha.isBefore(lunes) && !fecha.isAfter(lunes.plusDays(6))) {
                    int idx = (int) java.time.temporal.ChronoUnit.DAYS.between(lunes, fecha);
                    ing[idx] += t.getTotalFisico();
                }
            }
        }
        return ing;
    }

    /** Egresos agrupados por mes para el año dado */
    private double[] calcularEgresosPorMes(int anio) {
        double[] egr = new double[12];
        for (OrdenCompra o : hotel.getListaOrdenes()) {
            if ("Aprobado".equalsIgnoreCase(o.getEstado()) && o.getFechaEmision() != null) {
                java.util.Calendar cal = java.util.Calendar.getInstance();
                cal.setTime(o.getFechaEmision());
                if (cal.get(java.util.Calendar.YEAR) == anio) {
                    egr[cal.get(java.util.Calendar.MONTH)] += o.getPrecioTotal();
                }
            }
        }
        return egr;
    }

    /** Egresos por semana del mes actual (4 semanas) */
    private double[] calcularEgresosPorSemana(int mes, int anio) {
        double[] egr = new double[4];
        for (OrdenCompra o : hotel.getListaOrdenes()) {
            if ("Aprobado".equalsIgnoreCase(o.getEstado()) && o.getFechaEmision() != null) {
                java.util.Calendar cal = java.util.Calendar.getInstance();
                cal.setTime(o.getFechaEmision());
                if (cal.get(java.util.Calendar.MONTH) + 1 == mes
                        && cal.get(java.util.Calendar.YEAR) == anio) {
                    int dia = cal.get(java.util.Calendar.DAY_OF_MONTH);
                    int idx = Math.min((dia - 1) / 7, 3);
                    egr[idx] += o.getPrecioTotal();
                }
            }
        }
        return egr;
    }

    /** Egresos por cada día de la semana actual (lun a dom) */
    private double[] calcularEgresosPorDiaSemana() {
        double[] egr = new double[7];
        LocalDate hoy = LocalDate.now();
        LocalDate lunes = hoy.minusDays(hoy.getDayOfWeek().getValue() - 1);
        for (OrdenCompra o : hotel.getListaOrdenes()) {
            if ("Aprobado".equalsIgnoreCase(o.getEstado()) && o.getFechaEmision() != null) {
                java.util.Calendar cal = java.util.Calendar.getInstance();
                cal.setTime(o.getFechaEmision());
                LocalDate fecha = cal.toInstant()
                    .atZone(java.time.ZoneId.systemDefault()).toLocalDate();
                if (!fecha.isBefore(lunes) && !fecha.isAfter(lunes.plusDays(6))) {
                    int idx = (int) java.time.temporal.ChronoUnit.DAYS.between(lunes, fecha);
                    egr[idx] += o.getPrecioTotal();
                }
            }
        }
        return egr;
    }

    /** Último cierre de turno (caja) */
    private double calcularUltimaCaja() {
        List<TurnoCaja> turnos = hotel.getListaTurnos();
        if (turnos.isEmpty()) return 0;
        TurnoCaja ultimo = turnos.get(turnos.size() - 1);
        return ultimo.getTotalFisico();
    }

    /** Número de fichas finalizadas este mes */
    private int contarFichasFinalizadasMes() {
        int cnt = 0, mes = LocalDate.now().getMonthValue(), anio = LocalDate.now().getYear();
        for (FichaHospedaje f : hotel.getListaFHospedaje()) {
            if (f.getEstado() == 'F' && f.getFechaSalida() != null
                    && f.getFechaSalida().getMonthValue() == mes
                    && f.getFechaSalida().getYear() == anio) cnt++;
        }
        return cnt;
    }

    /** Órdenes aprobadas este mes */
    private int contarOrdenesAprobadasMes() {
        int cnt = 0, mes = LocalDate.now().getMonthValue(), anio = LocalDate.now().getYear();
        for (OrdenCompra o : hotel.getListaOrdenes()) {
            if ("Aprobado".equalsIgnoreCase(o.getEstado()) && o.getFechaEmision() != null) {
                java.util.Calendar cal = java.util.Calendar.getInstance();
                cal.setTime(o.getFechaEmision());
                if (cal.get(java.util.Calendar.MONTH) + 1 == mes
                        && cal.get(java.util.Calendar.YEAR) == anio) cnt++;
            }
        }
        return cnt;
    }

    /** Valor total del inventario (precio implícito no disponible, usamos 0 o futuro precio) */
    private double calcularValorInventario() {
        // Producto no tiene precio unitario en tu modelo; retornamos 0 hasta que se agregue
        return 0.0;
    }

    /** Cuentas por pagar: órdenes pendientes */
    private double calcularCuentasPorPagar() {
        double total = 0;
        for (OrdenCompra o : hotel.getListaOrdenes()) {
            if ("Pendiente".equalsIgnoreCase(o.getEstado())) {
                total += o.getPrecioTotal();
            }
        }
        return total;
    }

    /** Productos con stock crítico */
    private List<Producto> obtenerProductosCriticos() {
        List<Producto> criticos = new java.util.ArrayList<>();
        for (Producto p : hotel.getInventario()) {
            if (p.isStockCritico()) criticos.add(p);
        }
        return criticos;
    }

    /** Nombre del mes actual en español */
    private String nombreMesActual() {
        return LocalDate.now().getMonth()
                .getDisplayName(TextStyle.FULL, new Locale("es", "PE"))
                + " " + LocalDate.now().getYear();
    }

    // =========================================================
    // CARGA DE DATOS — usa hotel serializado
    // =========================================================
    private void cargarTodo() {
        actualizarResumen();
        actualizarEstadosFinancieros();
        actualizarGrafico();
        actualizarAlertas();
        poblarPanelOcupacion();
        poblarPanelMantenimiento();
    }

    private void actualizarResumen() {
        int tot  = hotel.getListaHabitacion().size();
        int ocu  = hotel.contarHabitacionesPorEstado('O');
        int mant = hotel.contarHabitacionesPorEstado('M');
        double pct  = tot > 0 ? (ocu * 100.0 / tot) : 0;
        double ing  = calcularIngresosMesActual();
        double egr  = calcularEgresosMesActual();
        double util = ing - egr;
        int alertas = obtenerProductosCriticos().size();

        lblOcupacionValor.setText(String.format("%.0f%%", pct));
        lblMantenimientoValor.setText(String.valueOf(mant));
        lblAlertasValor.setText(String.valueOf(alertas));
        lblIngresosValor.setText(soles.format(ing));
        lblEgresosValor.setText(soles.format(egr));
        lblUtilidadValor.setText(soles.format(util));
    }

    private void actualizarEstadosFinancieros() {
        actualizarTarjetaResultados();
        actualizarTarjetaBalance();
    }

    private void actualizarAlertas() {
        modeloTablaAlertas.setRowCount(0);
        List<Producto> criticos = obtenerProductosCriticos();
        if (criticos.isEmpty()) {
            modeloTablaAlertas.addRow(new Object[]{"No hay productos con stock bajo", "", "", "", ""});
        } else {
            for (Producto p : criticos) {
                modeloTablaAlertas.addRow(new Object[]{
                    p.getNombre(), p.getTipo(),
                    p.getStock(), p.getStockMinimo(),
                    p.getEstadoVisual()
                });
            }
        }
    }

    // =====================================================================
    //  PREFERENCIAS DE HUESPEDES  (2 graficos + periodo Semana/Mes/Año)
    // =====================================================================

    /** Construye la pestana de preferencias con selector de periodo y 2 graficos. */
    private JPanel crearPanelPreferencias() {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(GRIS_FONDO);
        card.setBorder(new EmptyBorder(16, 16, 16, 16));

        // Barra superior con el combo de periodo propio
        JPanel barra = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 6));
        barra.setOpaque(false);
        JLabel lbl = new JLabel("Preferencias de huespedes — Periodo:");
        lbl.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
        lbl.setForeground(java.awt.Color.BLACK);
        comboPeriodoPref = new JComboBox<>(new String[]{"Semana", "Mes", "Año"});
        comboPeriodoPref.setSelectedItem("Mes");
        comboPeriodoPref.setForeground(java.awt.Color.BLACK);
        comboPeriodoPref.addActionListener(e -> actualizarGraficosPreferencias());
        JButton btnRef = new JButton("Actualizar");
        btnRef.setBackground(VERDE_TEXTO);
        btnRef.setForeground(java.awt.Color.WHITE);
        btnRef.setFocusPainted(false);
        btnRef.addActionListener(e -> actualizarGraficosPreferencias());
        barra.add(lbl); barra.add(comboPeriodoPref); barra.add(btnRef);
        card.add(barra, BorderLayout.NORTH);

        // Dos graficos lado a lado
        JPanel grid = new JPanel(new GridLayout(1, 2, 16, 16));
        grid.setOpaque(false);

        panelPrefHabitacion = new JPanel(new BorderLayout());
        panelPrefHabitacion.setBackground(java.awt.Color.WHITE);
        panelPrefHabitacion.setBorder(BorderFactory.createTitledBorder("Tipo de habitacion preferida"));

        panelPrefComida = new JPanel(new BorderLayout());
        panelPrefComida.setBackground(java.awt.Color.WHITE);
        panelPrefComida.setBorder(BorderFactory.createTitledBorder("Servicio de comida mas preferido"));

        grid.add(panelPrefHabitacion);
        grid.add(panelPrefComida);
        card.add(grid, BorderLayout.CENTER);

        actualizarGraficosPreferencias();
        return card;
    }

    /** Devuelve la fecha de inicio del periodo seleccionado (desde hoy hacia atras). */
    private LocalDate inicioPeriodoPref() {
        String p = comboPeriodoPref == null ? "Mes" : (String) comboPeriodoPref.getSelectedItem();
        LocalDate hoy = LocalDate.now();
        if ("Semana".equals(p)) return hoy.minusDays(6);   // ultimos 7 dias
        if ("Año".equals(p))    return hoy.withDayOfYear(1); // desde 1-ene
        return hoy.withDayOfMonth(1);                        // Mes: desde 1 del mes
    }

    /** Recalcula y redibuja los 2 graficos de preferencias segun el periodo. */
    private void actualizarGraficosPreferencias() {
        if (panelPrefHabitacion == null || panelPrefComida == null) return;
        LocalDate desde = inicioPeriodoPref();
        java.sql.Date fDesde = java.sql.Date.valueOf(desde);

        // ---------- GRAFICO 1: PIE tipo de habitacion (por fichas) ----------
        DefaultPieDataset dsHab = new DefaultPieDataset();
        int simple = 0, doble = 0, matri = 0;
        String sqlHab =
            "SELECT h.tipo, COUNT(*) c " +
            "FROM ficha_hospedaje f JOIN habitacion h ON h.numero = f.numero_habitacion " +
            "WHERE f.fecha_ingreso >= ? GROUP BY h.tipo";
        Connection cn = ConexionDB.getConexion();
        try (PreparedStatement ps = cn.prepareStatement(sqlHab)) {
            ps.setDate(1, fDesde);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String tipo = rs.getString("tipo");
                    int c = rs.getInt("c");
                    if ("S".equalsIgnoreCase(tipo)) simple = c;
                    else if ("D".equalsIgnoreCase(tipo)) doble = c;
                    else if ("M".equalsIgnoreCase(tipo)) matri = c;
                }
            }
        } catch (Exception ex) {
            System.err.println("Pref habitacion: " + ex.getMessage());
        }
        dsHab.setValue("Simple (" + simple + ")", simple);
        dsHab.setValue("Doble (" + doble + ")", doble);
        dsHab.setValue("Matrimonial (" + matri + ")", matri);

        JFreeChart pie = ChartFactory.createPieChart(null, dsHab, true, true, false);
        try {
            PiePlot plot = (PiePlot) pie.getPlot();
            plot.setSectionPaint("Simple (" + simple + ")",      new Color(15, 110, 86));
            plot.setSectionPaint("Doble (" + doble + ")",        new Color(239, 159, 39));
            plot.setSectionPaint("Matrimonial (" + matri + ")",  new Color(12, 68, 124));
            plot.setLabelFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 12));
            plot.setBackgroundPaint(java.awt.Color.WHITE);
        } catch (Exception ignore) {}
        panelPrefHabitacion.removeAll();
        panelPrefHabitacion.add(new ChartPanel(pie), BorderLayout.CENTER);
        panelPrefHabitacion.revalidate();
        panelPrefHabitacion.repaint();

        // ---------- GRAFICO 2: BARRAS servicio de comida (por fichas) ----------
        int des = 0, alm = 0, cen = 0;
        String sqlCom =
            "SELECT " +
            " SUM(incluye_desayuno) d, SUM(incluye_almuerzo) a, SUM(incluye_cena) c " +
            "FROM ficha_hospedaje WHERE fecha_ingreso >= ?";
        Connection cn2 = ConexionDB.getConexion();
        try (PreparedStatement ps = cn2.prepareStatement(sqlCom)) {
            ps.setDate(1, fDesde);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) { des = rs.getInt("d"); alm = rs.getInt("a"); cen = rs.getInt("c"); }
            }
        } catch (Exception ex) {
            System.err.println("Pref comida: " + ex.getMessage());
        }
        DefaultCategoryDataset dsCom = new DefaultCategoryDataset();
        dsCom.addValue(des, "Fichas", "Desayuno");
        dsCom.addValue(alm, "Fichas", "Almuerzo");
        dsCom.addValue(cen, "Fichas", "Cena");

        JFreeChart bar = ChartFactory.createBarChart(
            null, "Servicio", "Cantidad de fichas", dsCom, PlotOrientation.VERTICAL, false, true, false);
        try {
            BarRenderer r = (BarRenderer) bar.getCategoryPlot().getRenderer();
            r.setSeriesPaint(0, new Color(15, 110, 86));
        } catch (Exception ignore) {}
        panelPrefComida.removeAll();
        panelPrefComida.add(new ChartPanel(bar), BorderLayout.CENTER);
        panelPrefComida.revalidate();
        panelPrefComida.repaint();
    }

    private void actualizarGrafico() {
        if (panelGraficoContenedor == null || comboPeriodoGrafico == null) return;
        String periodo = (String) comboPeriodoGrafico.getSelectedItem();
        int anioSel = (Integer) comboAnio.getSelectedItem();
        LocalDate hoy = LocalDate.now();

        String[] labels;
        double[] ing, egr;
        String tit;

        switch (periodo == null ? "Año" : periodo) {
            case "Semana": {
                // Día a día de la semana actual (Lun-Dom)
                LocalDate lunes = hoy.minusDays(hoy.getDayOfWeek().getValue() - 1);
                labels = new String[7];
                String[] dias = {"Lun", "Mar", "Mié", "Jue", "Vie", "Sáb", "Dom"};
                for (int i = 0; i < 7; i++) {
                    labels[i] = dias[i] + " " + lunes.plusDays(i).getDayOfMonth();
                }
                ing = calcularIngresosPorDiaSemana();
                egr = calcularEgresosPorDiaSemana();
                tit = "Semana del " + lunes.getDayOfMonth() + " al "
                    + lunes.plusDays(6).getDayOfMonth() + " "
                    + hoy.getMonth().getDisplayName(TextStyle.SHORT, new Locale("es", "PE"))
                    + " " + hoy.getYear();
                break;
            }
            case "Mes": {
                // Semanas 1-4 del mes actual
                String nombreMes = hoy.getMonth().getDisplayName(TextStyle.FULL, new Locale("es", "PE"));
                labels = new String[]{"Sem 1\n(1-7)", "Sem 2\n(8-14)", "Sem 3\n(15-21)", "Sem 4\n(22+)"};
                ing = calcularIngresosPorSemana(hoy.getMonthValue(), hoy.getYear());
                egr = calcularEgresosPorSemana(hoy.getMonthValue(), hoy.getYear());
                tit = "Por Semana — " + nombreMes.substring(0,1).toUpperCase()
                    + nombreMes.substring(1) + " " + hoy.getYear();
                break;
            }
            default: { // "Año"
                labels = new String[12];
                for (int i = 0; i < 12; i++) {
                    labels[i] = Month.of(i + 1).getDisplayName(TextStyle.SHORT, new Locale("es"));
                }
                ing = calcularIngresosPorMes(anioSel);
                egr = calcularEgresosPorMes(anioSel);
                tit = "Por Mes — " + anioSel;
                break;
            }
        }

        lblTituloGrafico.setText("Ingresos vs Egresos — " + tit);
        DefaultCategoryDataset ds = new DefaultCategoryDataset();
        for (int i = 0; i < labels.length; i++) {
            ds.addValue(ing[i], "Ingresos", labels[i]);
            ds.addValue(egr[i], "Egresos",  labels[i]);
        }
        JFreeChart chart = ChartFactory.createBarChart(
            null, null, "Monto (S/)", ds, PlotOrientation.VERTICAL, true, true, false);
        BarRenderer r = (BarRenderer) chart.getCategoryPlot().getRenderer();
        r.setSeriesPaint(0, new Color(15, 110, 86));
        r.setSeriesPaint(1, new Color(226, 75, 74));
        panelGraficoContenedor.removeAll();
        panelGraficoContenedor.add(new ChartPanel(chart), BorderLayout.CENTER);
        panelGraficoContenedor.revalidate();
        panelGraficoContenedor.repaint();
    }

    private void actualizarTarjetaResultados() {
        double ing  = calcularIngresosMesActual();
        double egr  = calcularEgresosMesActual();
        double util = ing - egr;
        int fichas  = contarFichasFinalizadasMes();
        int ordenes = contarOrdenesAprobadasMes();
        String periodo = "Ene - " +
            LocalDate.now().getMonth().getDisplayName(TextStyle.SHORT, new Locale("es"))
            + " " + LocalDate.now().getYear();

        panelTarjetaResultados.removeAll();
        panelTarjetaResultados.setLayout(new BoxLayout(panelTarjetaResultados, BoxLayout.Y_AXIS));
        panelTarjetaResultados.setBackground(Color.WHITE);
        panelTarjetaResultados.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRIS_BORDE, 1, true), new EmptyBorder(16, 16, 16, 16)));
        panelTarjetaResultados.add(crearEncabezadoTarjeta(TipoIcono.FACTURA, "Estado de Resultados", periodo));
        panelTarjetaResultados.add(Box.createVerticalStrut(10));
        panelTarjetaResultados.add(crearFilaReporte(TipoIcono.MAS, "Ingresos por hospedaje", soles.format(ing), VERDE_TEXTO, false));
        panelTarjetaResultados.add(crearSubtexto(fichas + " ficha(s) finalizada(s) este mes"));
        panelTarjetaResultados.add(crearFilaReporte(TipoIcono.MENOS, "Costos operativos", soles.format(egr), ROJO_TEXTO, false));
        panelTarjetaResultados.add(crearSubtexto(ordenes + " orden(es) aprobada(s) este mes"));
        panelTarjetaResultados.add(crearDivisor());
        panelTarjetaResultados.add(crearFilaReporte(null, "Utilidad neta", soles.format(util),
            util >= 0 ? VERDE_TEXTO : ROJO_TEXTO, true));
        panelTarjetaResultados.add(Box.createVerticalStrut(10));
        panelTarjetaResultados.add(crearBadgeMargen(ing == 0 ? 0 : (util / ing) * 100));
        panelTarjetaResultados.add(Box.createVerticalGlue());
        panelTarjetaResultados.revalidate();
        panelTarjetaResultados.repaint();
    }

    private void actualizarTarjetaBalance() {
        double caja      = calcularUltimaCaja();
        double inventario = calcularValorInventario();
        double porPagar  = calcularCuentasPorPagar();
        double totalActivos = caja + inventario;
        double patrimonio   = totalActivos - porPagar;

        panelTarjetaBalance.removeAll();
        panelTarjetaBalance.setLayout(new BoxLayout(panelTarjetaBalance, BoxLayout.Y_AXIS));
        panelTarjetaBalance.setBackground(Color.WHITE);
        panelTarjetaBalance.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRIS_BORDE, 1, true), new EmptyBorder(16, 16, 16, 16)));
        panelTarjetaBalance.add(crearEncabezadoTarjeta(TipoIcono.BALANZA, "Balance General", "A la fecha"));
        panelTarjetaBalance.add(Box.createVerticalStrut(10));
        panelTarjetaBalance.add(crearTituloSeccion("Activos"));
        panelTarjetaBalance.add(crearFilaReporte(TipoIcono.CAJA,      "Caja (últ. cierre de turno)", soles.format(caja),      null, false));
        panelTarjetaBalance.add(crearFilaReporte(TipoIcono.INVENTARIO, "Valor de inventario",          soles.format(inventario), null, false));
        panelTarjetaBalance.add(crearDivisorFino());
        panelTarjetaBalance.add(crearFilaReporte(null, "Total activos", soles.format(totalActivos), null, false));
        panelTarjetaBalance.add(Box.createVerticalStrut(10));
        panelTarjetaBalance.add(crearTituloSeccion("Pasivos"));
        panelTarjetaBalance.add(crearFilaReporte(TipoIcono.FACTURA, "Órdenes pendientes de pago", soles.format(porPagar), null, false));
        panelTarjetaBalance.add(crearDivisorFino());
        panelTarjetaBalance.add(crearFilaReporte(null, "Total pasivos", soles.format(porPagar), null, false));
        panelTarjetaBalance.add(crearDivisor());
        panelTarjetaBalance.add(crearFilaReporte(null, "Patrimonio", soles.format(patrimonio),
            patrimonio >= 0 ? VERDE_TEXTO : ROJO_TEXTO, true));
        panelTarjetaBalance.add(Box.createVerticalGlue());
        panelTarjetaBalance.revalidate();
        panelTarjetaBalance.repaint();
    }

    private void poblarPanelOcupacion() {
        panelOcupacionInner.removeAll();
        int tot  = hotel.getListaHabitacion().size();
        int ocu  = hotel.contarHabitacionesPorEstado('O');
        int mant = hotel.contarHabitacionesPorEstado('M');

        JPanel met = new JPanel(new GridLayout(1, 3, 12, 0));
        met.setOpaque(false);
        met.setAlignmentX(Component.LEFT_ALIGNMENT);
        met.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        met.add(crearMetrica("Porcentaje de ocupación",
            String.format("%.0f%%", tot > 0 ? (ocu * 100.0 / tot) : 0),
            ocu + " de " + tot + " habitaciones", VERDE_TEXTO));
        met.add(crearMetrica("Habitaciones ocupadas",
            String.valueOf(ocu), "con huéspedes activos", VERDE_TEXTO));
        met.add(crearMetrica("En mantenimiento",
            String.valueOf(mant), "fuera de servicio", AMBAR_TEXTO));
        panelOcupacionInner.add(met);
        panelOcupacionInner.add(Box.createVerticalStrut(20));
        panelOcupacionInner.add(crearTituloGrupo("Habitaciones ocupadas"));
        panelOcupacionInner.add(Box.createVerticalStrut(8));

        JPanel gO = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        gO.setOpaque(false);
        gO.setAlignmentX(Component.LEFT_ALIGNMENT);
        for (Habitacion h : hotel.getListaHabitacion()) {
            if (h.getEstado() == 'O') {
                // Buscar el huésped desde las fichas activas
                FichaHospedaje ficha = hotel.buscarFichaPorHabitacion(h);
                String huesped = "";
                if (ficha != null && !ficha.getHuespedes().isEmpty()) {
                    Entidades.Huesped hu = ficha.getHuespedes().get(0);
                    huesped = hu.getNombre() + " " + hu.getApellido();
                }
                gO.add(crearTarjetaHabitacionOcupada(h.getNumero(), tipoHabitacion(h.getTipo()), huesped));
            }
        }
        if (gO.getComponentCount() == 0) {
            JLabel s = new JLabel("No hay habitaciones ocupadas.");
            s.setForeground(GRIS_TEXTO_SEC);
            gO.add(s);
        }
        panelOcupacionInner.add(gO);
        panelOcupacionInner.add(Box.createVerticalStrut(20));
        panelOcupacionInner.add(crearTituloGrupo("En mantenimiento / limpieza"));
        panelOcupacionInner.add(Box.createVerticalStrut(8));

        JPanel gM = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        gM.setOpaque(false);
        gM.setAlignmentX(Component.LEFT_ALIGNMENT);
        boolean hay = false;
        for (Habitacion h : hotel.getListaHabitacion()) {
            if (h.getEstado() == 'M') {
                gM.add(crearTarjetaHabitacionMantenimiento(
                    h.getNumero(), tipoHabitacion(h.getTipo()),
                    h.getDescripcionProblema(), h.getEncargadoLimpieza()));
                hay = true;
            }
        }
        if (!hay) {
            JLabel s = new JLabel("No hay habitaciones en mantenimiento.");
            s.setForeground(GRIS_TEXTO_SEC);
            gM.add(s);
        }
        panelOcupacionInner.add(gM);
        panelOcupacionInner.add(Box.createVerticalGlue());
        panelOcupacionInner.revalidate();
        panelOcupacionInner.repaint();
    }

    private void poblarPanelMantenimiento() {
        panelMantCentro.removeAll();
        int mant = hotel.contarHabitacionesPorEstado('M');
        JLabel g = new JLabel(String.valueOf(mant));
        g.setFont(new Font("Segoe UI", Font.BOLD, 80));
        g.setForeground(AMBAR_TEXTO);
        g.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel s = new JLabel("Habitaciones fuera de servicio");
        s.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        s.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel h = new JLabel("Ver detalle en «Ocupación en Vivo»");
        h.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        h.setForeground(GRIS_TEXTO_TER);
        h.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelMantCentro.add(Box.createVerticalGlue());
        panelMantCentro.add(s);
        panelMantCentro.add(Box.createVerticalStrut(12));
        panelMantCentro.add(g);
        panelMantCentro.add(Box.createVerticalStrut(12));
        panelMantCentro.add(h);
        panelMantCentro.add(Box.createVerticalGlue());
        panelMantCentro.revalidate();
        panelMantCentro.repaint();
    }

    /** Convierte char tipo de habitación a texto legible */
    private String tipoHabitacion(char t) {
        switch (t) {
            case 'S': return "Simple";
            case 'D': return "Doble";
            case 'M': return "Matrimonial";
            default:  return String.valueOf(t);
        }
    }

    // =========================================================
    // COMPONENTES GENERADOS — NO MODIFICAR
    // =========================================================
    @SuppressWarnings("unchecked")
    private void initComponents() {

        panelRaiz            = new javax.swing.JPanel();
        panelCabecera        = new javax.swing.JPanel();
        lblCabecera          = new javax.swing.JLabel();
        btnRegresar          = new javax.swing.JButton();
        panelCuerpo          = new javax.swing.JPanel();
        panelMenu            = new javax.swing.JPanel();
        btnMenuResumen       = new javax.swing.JButton();
        btnMenuFinanciero    = new javax.swing.JButton();
        btnMenuOcupacion     = new javax.swing.JButton();
        btnMenuAlertas       = new javax.swing.JButton();
        btnMenuMantenimiento = new javax.swing.JButton();
        btnActualizar        = new javax.swing.JButton();
        jTabbedPane1         = new javax.swing.JTabbedPane();
        panelCardResumen     = new javax.swing.JPanel();
        panelResumen         = new javax.swing.JPanel();
        panelCardFinanciero  = new javax.swing.JPanel();
        panelControlesFinan  = new javax.swing.JPanel();
        lblPorGrafico        = new javax.swing.JLabel();
        comboPeriodoGrafico  = new javax.swing.JComboBox<>();
        lblAnioReporte       = new javax.swing.JLabel();
        comboAnio            = new javax.swing.JComboBox<>();
        btnDescargar         = new javax.swing.JButton();
        scrollFinanciero     = new javax.swing.JScrollPane();
        panelScrollContenido = new javax.swing.JPanel();
        panelTarjetaGrafico  = new javax.swing.JPanel();
        panelWrapperGrafico  = new javax.swing.JPanel();
        panelFilaTarjetas    = new javax.swing.JPanel();
        panelCardOcupacion   = new javax.swing.JPanel();
        scrollOcupacion      = new javax.swing.JScrollPane();
        panelOcupacionInner  = new javax.swing.JPanel();
        panelCardAlertas     = new javax.swing.JPanel();
        lblTituloAlertas     = new javax.swing.JLabel();
        scrollAlertas        = new javax.swing.JScrollPane();
        tablaAlertas         = new javax.swing.JTable();
        panelCardMantenimiento = new javax.swing.JPanel();
        panelMantCentro      = new javax.swing.JPanel();

        setTitle("Hotel TruGarden - Dashboard Gerente General");
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(1024, 700));
        setPreferredSize(new java.awt.Dimension(1280, 800));

        // CABECERA
        panelCabecera.setBackground(VERDE_HEADER);
        panelCabecera.setPreferredSize(new java.awt.Dimension(100, 64));
        panelCabecera.setBorder(new EmptyBorder(10, 20, 10, 20));
        panelCabecera.setLayout(new java.awt.BorderLayout());
        lblCabecera.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 20));
        lblCabecera.setForeground(java.awt.Color.WHITE);
        lblCabecera.setText("Hotel TruGarden  |  Dashboard Gerente General");
        panelCabecera.add(lblCabecera, java.awt.BorderLayout.WEST);
        btnRegresar.setText("Regresar al Menú");
        btnRegresar.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 13));
        btnRegresar.addActionListener(e -> { new Menu_principal().setVisible(true); dispose(); });
        panelCabecera.add(btnRegresar, java.awt.BorderLayout.EAST);

        // MENÚ LATERAL
        panelMenu.setBackground(new java.awt.Color(55, 71, 79));
        panelMenu.setPreferredSize(new java.awt.Dimension(230, 100));
        panelMenu.setBorder(new EmptyBorder(20, 12, 20, 12));
        panelMenu.setLayout(new javax.swing.BoxLayout(panelMenu, javax.swing.BoxLayout.Y_AXIS));
        configurarBtnMenu(btnMenuResumen,       "Resumen General");
        configurarBtnMenu(btnMenuFinanciero,    "Estados Financieros");
        configurarBtnMenu(btnMenuOcupacion,     "Ocupación en Vivo");
        configurarBtnMenu(btnMenuAlertas,       "Alertas de Stock");
        configurarBtnMenu(btnMenuMantenimiento, "Mantenimiento");
        btnMenuResumen.addActionListener(e       -> jTabbedPane1.setSelectedIndex(0));
        btnMenuFinanciero.addActionListener(e    -> jTabbedPane1.setSelectedIndex(1));
        btnMenuOcupacion.addActionListener(e     -> jTabbedPane1.setSelectedIndex(2));
        btnMenuAlertas.addActionListener(e       -> jTabbedPane1.setSelectedIndex(3));
        btnMenuMantenimiento.addActionListener(e -> jTabbedPane1.setSelectedIndex(4));
        panelMenu.add(btnMenuResumen);
        panelMenu.add(Box.createVerticalStrut(8));
        panelMenu.add(btnMenuFinanciero);
        panelMenu.add(Box.createVerticalStrut(8));
        panelMenu.add(btnMenuOcupacion);
        panelMenu.add(Box.createVerticalStrut(8));
        panelMenu.add(btnMenuAlertas);
        panelMenu.add(Box.createVerticalStrut(8));
        panelMenu.add(btnMenuMantenimiento);

        // --- Preferencias de huespedes (pestana con 2 graficos) ---
        javax.swing.JButton btnMenuPreferencias = new javax.swing.JButton();
        configurarBtnMenu(btnMenuPreferencias, "Preferencias");
        btnMenuPreferencias.addActionListener(e -> jTabbedPane1.setSelectedIndex(5));
        panelMenu.add(Box.createVerticalStrut(8));
        panelMenu.add(btnMenuPreferencias);

        // --- HU F-001: acceso al Libro Mayor (pestana incrustada, indice 6) ---
        javax.swing.JButton btnMenuLibroMayor = new javax.swing.JButton();
        configurarBtnMenu(btnMenuLibroMayor, "Libro Mayor");
        btnMenuLibroMayor.addActionListener(e -> {
            jTabbedPane1.setSelectedIndex(6);
            if (panelLibroMayor != null) panelLibroMayor.recargarTodo();
        });
        panelMenu.add(Box.createVerticalStrut(8));
        panelMenu.add(btnMenuLibroMayor);

        panelMenu.add(Box.createVerticalGlue());

        btnActualizar.setText("⟳  Actualizar datos");
        btnActualizar.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));
        btnActualizar.setBackground(VERDE_TEXTO);
        btnActualizar.setForeground(java.awt.Color.WHITE);
        btnActualizar.setFocusPainted(false);
        btnActualizar.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
        btnActualizar.setMaximumSize(new java.awt.Dimension(210, 38));
        btnActualizar.addActionListener(e -> cargarTodo());
        panelMenu.add(btnActualizar);

        // Combos financiero
        comboPeriodoGrafico.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"Año", "Mes", "Semana"}));
        comboPeriodoGrafico.addActionListener(e -> {
            // Mostrar/ocultar el combo de año según la selección
            String sel = (String) comboPeriodoGrafico.getSelectedItem();
            lblAnioReporte.setVisible("Año".equals(sel));
            comboAnio.setVisible("Año".equals(sel));
            actualizarGrafico();
        });
        // Años desde 2026 hasta el año actual + 5 (hacia adelante)
        int anioActual = LocalDate.now().getYear();
        for (int a = anioActual + 5; a >= 2026; a--) comboAnio.addItem(a);
        comboAnio.setSelectedItem(anioActual);
        comboAnio.addActionListener(e -> { actualizarEstadosFinancieros(); actualizarGrafico(); });
        btnDescargar.setText("Descargar reporte (.txt)");
        btnDescargar.addActionListener(e -> descargarReporte());

        // TAB 0: RESUMEN
        panelCardResumen.setBackground(GRIS_FONDO);
        panelCardResumen.setLayout(new java.awt.BorderLayout());
        panelResumen.setBackground(GRIS_FONDO);
        panelCardResumen.add(panelResumen, java.awt.BorderLayout.CENTER);
        jTabbedPane1.addTab("Resumen", panelCardResumen);

        // TAB 1: FINANCIERO
        panelCardFinanciero.setBackground(GRIS_FONDO);
        panelCardFinanciero.setBorder(new EmptyBorder(16, 16, 16, 16));
        panelCardFinanciero.setLayout(new java.awt.BorderLayout(0, 12));
        panelControlesFinan.setOpaque(false);
        panelControlesFinan.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 8, 0));
        lblPorGrafico.setText("Ver gráfico por:");
        panelControlesFinan.add(lblPorGrafico);
        panelControlesFinan.add(comboPeriodoGrafico);
        panelControlesFinan.add(Box.createHorizontalStrut(16));
        lblAnioReporte.setText("Año del reporte:");
        panelControlesFinan.add(lblAnioReporte);
        panelControlesFinan.add(comboAnio);
        panelControlesFinan.add(Box.createHorizontalStrut(16));
        panelControlesFinan.add(btnDescargar);
        panelCardFinanciero.add(panelControlesFinan, java.awt.BorderLayout.NORTH);

        panelScrollContenido.setOpaque(false);
        panelScrollContenido.setLayout(new javax.swing.BoxLayout(panelScrollContenido, javax.swing.BoxLayout.Y_AXIS));
        panelTarjetaGrafico.setBackground(java.awt.Color.WHITE);
        panelTarjetaGrafico.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRIS_BORDE, 1, true), new EmptyBorder(16, 16, 16, 16)));
        panelTarjetaGrafico.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
        panelTarjetaGrafico.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, 380));
        panelTarjetaGrafico.setLayout(new java.awt.BorderLayout());
        panelWrapperGrafico.setBackground(java.awt.Color.WHITE);
        panelWrapperGrafico.setPreferredSize(new java.awt.Dimension(100, 330));
        panelTarjetaGrafico.add(panelWrapperGrafico, java.awt.BorderLayout.CENTER);
        panelScrollContenido.add(panelTarjetaGrafico);
        panelScrollContenido.add(Box.createVerticalStrut(16));
        panelFilaTarjetas.setOpaque(false);
        panelFilaTarjetas.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
        panelFilaTarjetas.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, 440));
        panelScrollContenido.add(panelFilaTarjetas);
        panelScrollContenido.add(Box.createVerticalGlue());
        scrollFinanciero.setBorder(null);
        scrollFinanciero.setViewportView(panelScrollContenido);
        scrollFinanciero.getVerticalScrollBar().setUnitIncrement(16);
        panelCardFinanciero.add(scrollFinanciero, java.awt.BorderLayout.CENTER);
        jTabbedPane1.addTab("Financiero", panelCardFinanciero);

        // TAB 2: OCUPACIÓN
        panelCardOcupacion.setBackground(GRIS_FONDO);
        panelCardOcupacion.setBorder(new EmptyBorder(16, 16, 16, 16));
        panelCardOcupacion.setLayout(new java.awt.BorderLayout());
        panelOcupacionInner.setLayout(new javax.swing.BoxLayout(panelOcupacionInner, javax.swing.BoxLayout.Y_AXIS));
        panelOcupacionInner.setOpaque(false);
        scrollOcupacion.setBorder(null);
        scrollOcupacion.setViewportView(panelOcupacionInner);
        scrollOcupacion.getVerticalScrollBar().setUnitIncrement(16);
        panelCardOcupacion.add(scrollOcupacion, java.awt.BorderLayout.CENTER);
        jTabbedPane1.addTab("Ocupación", panelCardOcupacion);

        // TAB 3: ALERTAS
        panelCardAlertas.setBackground(GRIS_FONDO);
        panelCardAlertas.setBorder(new EmptyBorder(16, 16, 16, 16));
        panelCardAlertas.setLayout(new java.awt.BorderLayout(0, 12));
        lblTituloAlertas.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 18));
        lblTituloAlertas.setText("Productos con stock bajo o crítico");
        panelCardAlertas.add(lblTituloAlertas, java.awt.BorderLayout.NORTH);
        scrollAlertas.setViewportView(tablaAlertas);
        panelCardAlertas.add(scrollAlertas, java.awt.BorderLayout.CENTER);
        jTabbedPane1.addTab("Alertas", panelCardAlertas);

        // TAB 4: MANTENIMIENTO
        panelCardMantenimiento.setBackground(GRIS_FONDO);
        panelCardMantenimiento.setBorder(new EmptyBorder(16, 16, 16, 16));
        panelCardMantenimiento.setLayout(new java.awt.BorderLayout());
        panelMantCentro.setOpaque(false);
        panelMantCentro.setLayout(new javax.swing.BoxLayout(panelMantCentro, javax.swing.BoxLayout.Y_AXIS));
        panelCardMantenimiento.add(panelMantCentro, java.awt.BorderLayout.CENTER);
        jTabbedPane1.addTab("Mantenimiento", panelCardMantenimiento);

        // TAB 5: PREFERENCIAS DE HUESPEDES (2 graficos)
        jTabbedPane1.addTab("Preferencias", crearPanelPreferencias());

        // TAB 6: LIBRO MAYOR (HU F-001) incrustado como panel
        panelLibroMayor = new LibroMayor();
        jTabbedPane1.addTab("Libro Mayor", panelLibroMayor);

        // CUERPO
        panelCuerpo.setLayout(new java.awt.BorderLayout());
        panelCuerpo.add(panelMenu,    java.awt.BorderLayout.WEST);
        panelCuerpo.add(jTabbedPane1, java.awt.BorderLayout.CENTER);

        // RAÍZ
        panelRaiz.setBackground(java.awt.Color.WHITE);
        panelRaiz.setLayout(new java.awt.BorderLayout());
        panelRaiz.add(panelCabecera, java.awt.BorderLayout.NORTH);
        panelRaiz.add(panelCuerpo,   java.awt.BorderLayout.CENTER);
        setContentPane(panelRaiz);
        pack();
    }

    private void descargarReporte() {
        JFileChooser c = new JFileChooser();
        c.setSelectedFile(new java.io.File("ReporteFinanciero_" + LocalDate.now() + ".txt"));
        if (c.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try (java.io.PrintWriter w = new java.io.PrintWriter(c.getSelectedFile(), "UTF-8")) {
                double ing = calcularIngresosMesActual();
                double egr = calcularEgresosMesActual();
                w.println("=== Reporte Financiero — Hotel TruGarden ===");
                w.println("Fecha de generación: " + LocalDate.now());
                w.println("Mes: " + nombreMesActual());
                w.println("--------------------------------------------");
                w.println("Ingresos del mes  : " + soles.format(ing));
                w.println("Egresos del mes   : " + soles.format(egr));
                w.println("Utilidad neta     : " + soles.format(ing - egr));
                w.println("--------------------------------------------");
                w.println("Habitaciones totales   : " + hotel.getListaHabitacion().size());
                w.println("Habitaciones ocupadas  : " + hotel.contarHabitacionesPorEstado('O'));
                w.println("En mantenimiento       : " + hotel.contarHabitacionesPorEstado('M'));
                w.println("Disponibles            : " + hotel.contarHabitacionesPorEstado('D'));
                w.println("--------------------------------------------");
                w.println("Caja (últ. cierre)     : " + soles.format(calcularUltimaCaja()));
                w.println("Órdenes pendientes     : " + soles.format(calcularCuentasPorPagar()));
                w.println("Productos en alerta    : " + obtenerProductosCriticos().size());
                JOptionPane.showMessageDialog(this, "Reporte guardado correctamente.", "OK", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al guardar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // =========================================================
    // HELPERS UI
    // =========================================================
    private void configurarBtnMenu(JButton b, String texto) {
        b.setText(texto);
        b.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        b.setBackground(new Color(69, 90, 100));
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setAlignmentX(Component.CENTER_ALIGNMENT);
        b.setMaximumSize(new Dimension(210, 40));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private JPanel crearTarjetaResumen(String titulo, JLabel valor, Color color) {
        JPanel t = new JPanel(new BorderLayout());
        t.setBackground(Color.WHITE);
        t.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRIS_BORDE, 1, true), new EmptyBorder(20, 20, 20, 20)));
        JLabel l = new JLabel(titulo);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        l.setForeground(GRIS_TEXTO_SEC);
        valor.setFont(new Font("Segoe UI", Font.BOLD, 34));
        valor.setForeground(color);
        t.add(l, BorderLayout.NORTH);
        t.add(valor, BorderLayout.CENTER);
        return t;
    }

    private JPanel crearMetrica(String titulo, String valor, String sub, Color c) {
        JPanel card = new JPanel(new BorderLayout(0, 4));
        card.setBackground(new Color(240, 240, 240));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRIS_BORDE, 1, true), new EmptyBorder(16, 18, 16, 18)));
        JLabel lt = new JLabel(titulo);
        lt.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lt.setForeground(GRIS_TEXTO_SEC);
        JLabel lv = new JLabel(valor);
        lv.setFont(new Font("Segoe UI", Font.BOLD, 34));
        lv.setForeground(c);
        JLabel ls = new JLabel(sub);
        ls.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        ls.setForeground(GRIS_TEXTO_TER);
        JPanel ce = new JPanel(new BorderLayout());
        ce.setOpaque(false);
        ce.add(lv, BorderLayout.CENTER);
        ce.add(ls, BorderLayout.SOUTH);
        card.add(lt, BorderLayout.NORTH);
        card.add(ce, BorderLayout.CENTER);
        return card;
    }

    private JLabel crearTituloGrupo(String t) {
        JLabel l = new JLabel(t.toUpperCase());
        l.setFont(new Font("Segoe UI", Font.BOLD, 11));
        l.setForeground(GRIS_TEXTO_TER);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    private JPanel crearTarjetaHabitacionOcupada(String num, String tipo, String huesped) {
        JPanel card = new JPanel(new BorderLayout(0, 8));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRIS_BORDE, 1, true), new EmptyBorder(12, 14, 12, 14)));
        card.setPreferredSize(new Dimension(200, 110));
        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        JPanel izq = new JPanel();
        izq.setLayout(new BoxLayout(izq, BoxLayout.Y_AXIS));
        izq.setOpaque(false);
        JLabel ln = new JLabel("Hab. " + num);
        ln.setFont(new Font("Segoe UI", Font.BOLD, 18));
        JLabel lt = new JLabel(tipo);
        lt.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lt.setForeground(GRIS_TEXTO_TER);
        izq.add(ln);
        izq.add(lt);
        JLabel badge = new JLabel("Ocupada");
        badge.setFont(new Font("Segoe UI", Font.BOLD, 11));
        badge.setForeground(VERDE_BADGE_TXT);
        badge.setBackground(VERDE_BG_BADGE);
        badge.setOpaque(true);
        badge.setBorder(new EmptyBorder(3, 7, 3, 7));
        top.add(izq, BorderLayout.WEST);
        top.add(badge, BorderLayout.EAST);
        String textoHuesped = huesped.isEmpty() ? "Huésped no registrado" : "\u2022 " + huesped;
        JLabel lh = new JLabel(textoHuesped);
        lh.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lh.setForeground(GRIS_TEXTO_SEC);
        lh.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, GRIS_BORDE), new EmptyBorder(6, 0, 0, 0)));
        card.add(top, BorderLayout.NORTH);
        card.add(lh, BorderLayout.SOUTH);
        return card;
    }

    private JPanel crearTarjetaHabitacionMantenimiento(String num, String tipo, String desc, String enc) {
        JPanel card = new JPanel(new BorderLayout(0, 8));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(AMBAR_BORDE, 1, true), new EmptyBorder(12, 14, 12, 14)));
        card.setPreferredSize(new Dimension(300, 130));
        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        JPanel izq = new JPanel();
        izq.setLayout(new BoxLayout(izq, BoxLayout.Y_AXIS));
        izq.setOpaque(false);
        JLabel ln = new JLabel("Hab. " + num);
        ln.setFont(new Font("Segoe UI", Font.BOLD, 18));
        JLabel lt = new JLabel(tipo);
        lt.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lt.setForeground(GRIS_TEXTO_TER);
        izq.add(ln);
        izq.add(lt);
        JLabel badge = new JLabel("Mantenimiento");
        badge.setFont(new Font("Segoe UI", Font.BOLD, 11));
        badge.setForeground(AMBAR_BADGE_TXT);
        badge.setBackground(AMBAR_BG_BADGE);
        badge.setOpaque(true);
        badge.setBorder(new EmptyBorder(3, 7, 3, 7));
        top.add(izq, BorderLayout.WEST);
        top.add(badge, BorderLayout.EAST);
        JPanel det = new JPanel();
        det.setLayout(new BoxLayout(det, BoxLayout.Y_AXIS));
        det.setOpaque(false);
        det.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, GRIS_BORDE), new EmptyBorder(6, 0, 0, 0)));
        JLabel ld = new JLabel("<html><body style='width:240px'>\uD83D\uDD27 " +
            (desc == null || desc.isEmpty() ? "Sin descripción." : desc) + "</body></html>");
        ld.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        ld.setForeground(GRIS_TEXTO_SEC);
        det.add(ld);
        if (enc != null && !enc.isEmpty()) {
            JLabel le = new JLabel("\uD83D\uDC64 Encargado: " + enc);
            le.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            le.setForeground(GRIS_TEXTO_TER);
            le.setBorder(new EmptyBorder(4, 0, 0, 0));
            det.add(le);
        }
        card.add(top, BorderLayout.NORTH);
        card.add(det, BorderLayout.CENTER);
        return card;
    }

    private JPanel crearEncabezadoTarjeta(TipoIcono ic, String titulo, String periodo) {
        JPanel f = new JPanel(new BorderLayout(8, 0));
        f.setOpaque(false);
        f.setAlignmentX(Component.LEFT_ALIGNMENT);
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, GRIS_BORDE), new EmptyBorder(0, 0, 10, 0)));
        JPanel izq = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        izq.setOpaque(false);
        izq.add(new JLabel(crearIcono(ic, 18, AZUL_TEXTO)));
        JLabel lt = new JLabel(titulo);
        lt.setFont(new Font("Segoe UI", Font.BOLD, 14));
        izq.add(lt);
        JLabel lp = new JLabel(periodo);
        lp.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lp.setForeground(GRIS_TEXTO_TER);
        f.add(izq, BorderLayout.WEST);
        f.add(lp, BorderLayout.EAST);
        return f;
    }

    private JLabel crearTituloSeccion(String t) {
        JLabel l = new JLabel(t.toUpperCase());
        l.setFont(new Font("Segoe UI", Font.BOLD, 11));
        l.setForeground(GRIS_TEXTO_TER);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        l.setBorder(new EmptyBorder(0, 0, 4, 0));
        return l;
    }

    private JPanel crearFilaReporte(TipoIcono ic, String et, String val, Color cv, boolean dest) {
        JPanel f = new JPanel(new BorderLayout());
        f.setOpaque(false);
        f.setAlignmentX(Component.LEFT_ALIGNMENT);
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE, dest ? 36 : 28));
        f.setBorder(new EmptyBorder(3, 0, 3, 0));
        JPanel izq = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        izq.setOpaque(false);
        if (ic != null) izq.add(new JLabel(crearIcono(ic, 14, GRIS_TEXTO_TER)));
        JLabel le = new JLabel(et);
        le.setFont(new Font("Segoe UI", dest ? Font.BOLD : Font.PLAIN, 13));
        le.setForeground(dest ? Color.DARK_GRAY : GRIS_TEXTO_SEC);
        izq.add(le);
        JLabel lv = new JLabel(val);
        lv.setFont(new Font("Segoe UI", Font.BOLD, dest ? 18 : 13));
        lv.setForeground(cv != null ? cv : Color.DARK_GRAY);
        lv.setHorizontalAlignment(SwingConstants.RIGHT);
        f.add(izq, BorderLayout.WEST);
        f.add(lv, BorderLayout.EAST);
        return f;
    }

    private JLabel crearSubtexto(String t) {
        JLabel l = new JLabel(t);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        l.setForeground(GRIS_TEXTO_TER);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        l.setBorder(new EmptyBorder(0, 20, 4, 0));
        return l;
    }

    private JPanel crearDivisor() {
        JPanel d = new JPanel();
        d.setBackground(GRIS_BORDE);
        d.setPreferredSize(new Dimension(10, 1));
        JPanel c = new JPanel(new BorderLayout());
        c.setOpaque(false);
        c.setAlignmentX(Component.LEFT_ALIGNMENT);
        c.setMaximumSize(new Dimension(Integer.MAX_VALUE, 9));
        c.setBorder(new EmptyBorder(8, 0, 8, 0));
        c.add(d, BorderLayout.CENTER);
        return c;
    }

    private JPanel crearDivisorFino() {
        JPanel d = new JPanel();
        d.setBackground(GRIS_BORDE);
        d.setPreferredSize(new Dimension(10, 1));
        JPanel c = new JPanel(new BorderLayout());
        c.setOpaque(false);
        c.setAlignmentX(Component.LEFT_ALIGNMENT);
        c.setMaximumSize(new Dimension(Integer.MAX_VALUE, 3));
        c.setBorder(new EmptyBorder(1, 0, 1, 0));
        c.add(d, BorderLayout.CENTER);
        return c;
    }

    private JPanel crearBadgeMargen(double m) {
        boolean pos = m >= 0;
        Color tc = pos ? new Color(59, 109, 17) : ROJO_TEXTO;
        JPanel b = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 6));
        b.setBackground(pos ? new Color(234, 243, 222) : new Color(252, 235, 235));
        b.setAlignmentX(Component.LEFT_ALIGNMENT);
        b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        b.add(new JLabel(crearIcono(pos ? TipoIcono.TENDENCIA_ARRIBA : TipoIcono.TENDENCIA_ABAJO, 14, tc)));
        JLabel l = new JLabel(String.format("Margen de utilidad: %.1f%%", m));
        l.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        l.setForeground(tc);
        b.add(l);
        return b;
    }

    // =========================================================
    // ICONOS VECTORIALES
    // =========================================================
    private enum TipoIcono { CAJA, INVENTARIO, FACTURA, TENDENCIA_ABAJO, TENDENCIA_ARRIBA, BALANZA, MAS, MENOS }

    private Icon crearIcono(TipoIcono tipo, int tam, Color color) {
        return new Icon() {
            public int getIconWidth()  { return tam; }
            public int getIconHeight() { return tam; }
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.translate(x, y);
                g2.setColor(color);
                float s = tam;
                g2.setStroke(new BasicStroke(Math.max(1.4f, s / 11f), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                switch (tipo) {
                    case CAJA:
                        g2.draw(new RoundRectangle2D.Float(s*.05f,s*.22f,s*.9f,s*.56f,s*.12f,s*.12f));
                        g2.draw(new Ellipse2D.Float(s*.38f,s*.38f,s*.24f,s*.24f));
                        break;
                    case INVENTARIO:
                        Path2D.Float bx = new Path2D.Float();
                        bx.moveTo(s*.08f,s*.32f); bx.lineTo(s*.5f,s*.12f);
                        bx.lineTo(s*.92f,s*.32f); bx.lineTo(s*.5f,s*.52f); bx.closePath();
                        g2.draw(bx);
                        g2.draw(new Line2D.Float(s*.08f,s*.32f,s*.08f,s*.74f));
                        g2.draw(new Line2D.Float(s*.92f,s*.32f,s*.92f,s*.74f));
                        g2.draw(new Line2D.Float(s*.08f,s*.74f,s*.5f,s*.94f));
                        g2.draw(new Line2D.Float(s*.92f,s*.74f,s*.5f,s*.94f));
                        g2.draw(new Line2D.Float(s*.5f,s*.52f,s*.5f,s*.94f));
                        break;
                    case FACTURA:
                        g2.draw(new RoundRectangle2D.Float(s*.18f,s*.06f,s*.64f,s*.88f,s*.08f,s*.08f));
                        g2.draw(new Line2D.Float(s*.32f,s*.3f,s*.68f,s*.3f));
                        g2.draw(new Line2D.Float(s*.32f,s*.48f,s*.68f,s*.48f));
                        g2.draw(new Line2D.Float(s*.32f,s*.66f,s*.52f,s*.66f));
                        break;
                    case TENDENCIA_ABAJO: case TENDENCIA_ARRIBA:
                        boolean ab = tipo == TipoIcono.TENDENCIA_ABAJO;
                        float y1 = ab ? s*.25f : s*.75f, y2 = ab ? s*.75f : s*.25f;
                        g2.draw(new Line2D.Float(s*.15f,y1,s*.85f,y2));
                        Path2D.Float pt = new Path2D.Float();
                        pt.moveTo(s*.85f,y2); pt.lineTo(s*.85f-s*.22f,y2);
                        pt.moveTo(s*.85f,y2); pt.lineTo(s*.85f,y2+(ab?-s*.22f:s*.22f));
                        g2.draw(pt);
                        break;
                    case BALANZA:
                        g2.draw(new Line2D.Float(s*.5f,s*.1f,s*.5f,s*.85f));
                        g2.draw(new Line2D.Float(s*.15f,s*.28f,s*.85f,s*.28f));
                        g2.draw(new Line2D.Float(s*.2f,s*.85f,s*.8f,s*.85f));
                        g2.draw(new Arc2D.Float(s*.05f,s*.28f,s*.26f,s*.22f,200,140,Arc2D.OPEN));
                        g2.draw(new Arc2D.Float(s*.69f,s*.28f,s*.26f,s*.22f,200,140,Arc2D.OPEN));
                        break;
                    case MAS:
                        g2.draw(new Line2D.Float(s*.5f,s*.15f,s*.5f,s*.85f));
                        g2.draw(new Line2D.Float(s*.15f,s*.5f,s*.85f,s*.5f));
                        break;
                    case MENOS:
                        g2.draw(new Line2D.Float(s*.15f,s*.5f,s*.85f,s*.5f));
                        break;
                }
                g2.dispose();
            }
        };
    }

    // =========================================================
    // VARIABLES (NO MODIFICAR)
    // =========================================================
    private javax.swing.JButton        btnActualizar;
    private javax.swing.JButton        btnDescargar;
    private javax.swing.JButton        btnMenuAlertas;
    private javax.swing.JButton        btnMenuFinanciero;
    private javax.swing.JButton        btnMenuMantenimiento;
    private javax.swing.JButton        btnMenuOcupacion;
    private javax.swing.JButton        btnMenuResumen;
    private javax.swing.JButton        btnRegresar;
    private javax.swing.JComboBox<Integer> comboAnio;
    private javax.swing.JComboBox<String>  comboPeriodoGrafico;
    private javax.swing.JTabbedPane    jTabbedPane1;
    private javax.swing.JLabel         lblAnioReporte;
    private javax.swing.JLabel         lblCabecera;
    private javax.swing.JLabel         lblPorGrafico;
    private javax.swing.JLabel         lblTituloAlertas;
    private javax.swing.JPanel         panelCardAlertas;
    private javax.swing.JPanel         panelCardFinanciero;
    private javax.swing.JPanel         panelCardMantenimiento;
    private javax.swing.JPanel         panelCardOcupacion;
    private javax.swing.JPanel         panelCardResumen;
    private javax.swing.JPanel         panelCabecera;
    private javax.swing.JPanel         panelControlesFinan;
    private javax.swing.JPanel         panelCuerpo;
    private javax.swing.JPanel         panelFilaTarjetas;
    private javax.swing.JPanel         panelMantCentro;
    private javax.swing.JPanel         panelMenu;
    private javax.swing.JPanel         panelOcupacionInner;
    private javax.swing.JPanel         panelRaiz;
    private javax.swing.JPanel         panelResumen;
    private javax.swing.JPanel         panelScrollContenido;
    private javax.swing.JPanel         panelTarjetaGrafico;
    private javax.swing.JPanel         panelWrapperGrafico;
    private javax.swing.JScrollPane    scrollAlertas;
    private javax.swing.JScrollPane    scrollFinanciero;
    private javax.swing.JScrollPane    scrollOcupacion;
    private javax.swing.JTable         tablaAlertas;
}