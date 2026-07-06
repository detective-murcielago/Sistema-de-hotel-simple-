package Interfaz;

import Entidades.FichaHospedaje;
import Entidades.Huesped;
import Entidades.PagoServicio;
import Entidades.PreferenciaHuesped;
import controlador.Hotel;
import controlador.SistemaHotel;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 * Construye los paneles (cards) de las historias de usuario del recepcionista.
 * Se mantiene la estética del módulo Recepcionista:
 *   - Fondo del contenedor:  naranja (255,102,0)
 *   - Botones de acción:     azul   (102,153,255)
 *   - Títulos:               Segoe UI Bold, blancos
 *
 * Cada método devuelve un JPanel listo para agregarse al jPanelContenedor
 * (que usa CardLayout) del JFrame Recepcionista.
 *
 * Historias implementadas:
 *   F-004  registrarPreferenciasPanel()
 *   F-005  historialEstadiasPanel()
 *   F-010  pagoServiciosPanel()
 *   F-011  historialCompletoPanel()
 */
public class PanelesHU {

    // ---- Paleta / estilo compartido con el módulo Recepcionista ----
    // Esquema: fondo blanco/gris, azul noche para acentos, letras negras.
    static final Color FONDO      = Color.WHITE;              // fondo del panel de contenido
    static final Color AZUL_NOCHE = new Color(25, 42, 86);    // acentos / botones / títulos
    static final Color AZUL       = new Color(102, 153, 255); // azul del menú (se conserva)
    static final Color GRIS       = new Color(204, 204, 204);
    static final Color NEGRO      = Color.BLACK;              // color de todas las letras
    static final Color BLANCO     = Color.WHITE;
    static final Font  F_TITULO   = new Font("Segoe UI", Font.BOLD, 18);
    static final Font  F_SUBTITULO= new Font("Segoe UI", Font.BOLD, 14);
    static final Font  F_LABEL    = new Font("Segoe UI", Font.PLAIN, 13);
    static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    static final DateTimeFormatter FMT_DIA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private static Hotel hotel() {
        return SistemaHotel.getInstancia().getHotel();
    }

    // ============ Helpers de construcción (estética uniforme) ============

    private static JLabel titulo(String texto) {
        JLabel l = new JLabel(texto);
        l.setFont(F_TITULO);
        l.setForeground(AZUL_NOCHE);
        return l;
    }

    private static JLabel label(String texto) {
        JLabel l = new JLabel(texto);
        l.setFont(F_LABEL);
        l.setForeground(NEGRO);
        return l;
    }

    private static JButton boton(String texto) {
        JButton b = new JButton(texto);
        b.setBackground(AZUL_NOCHE);
        b.setForeground(BLANCO);
        b.setFocusPainted(false);
        b.setFont(F_SUBTITULO);
        return b;
    }

    private static JScrollPane tabla(JTable t) {
        t.setRowHeight(24);
        t.getTableHeader().setReorderingAllowed(false);
        JScrollPane sp = new JScrollPane(t);
        sp.getViewport().setBackground(BLANCO);
        return sp;
    }

    private static GridBagConstraints gbc(int x, int y, int ancho, int relleno) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = x; c.gridy = y;
        c.gridwidth = ancho;
        c.insets = new Insets(6, 8, 6, 8);
        c.anchor = GridBagConstraints.WEST;
        c.fill = relleno;
        return c;
    }

    // =====================================================================
    //  F-004 : Registrar preferencias del huésped
    // =====================================================================
    public static JPanel registrarPreferenciasPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(FONDO);

        JTextField txtDoc     = new JTextField(16);
        JComboBox<String> cboTipo = new JComboBox<>(new String[]{
            "Tipo de habitación", "Servicio preferido", "Restricción alimentaria",
            "Piso preferido", "Otro"
        });
        JTextField txtDetalle = new JTextField(20);

        String[] cols = {"Tipo", "Detalle", "Fecha registro"};
        DefaultTableModel modelo = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tblPref = new JTable(modelo);

        JButton btnRegistrar = boton("Registrar preferencia");
        JButton btnConsultar  = boton("Consultar preferencias");
        JButton btnLimpiar    = boton("Limpiar");

        // --- Layout ---
        panel.add(titulo("F-004 · REGISTRAR PREFERENCIAS DEL HUÉSPED"), gbc(0, 0, 3, GridBagConstraints.NONE));

        panel.add(label("Nro documento:"), gbc(0, 1, 1, GridBagConstraints.NONE));
        panel.add(txtDoc, gbc(1, 1, 2, GridBagConstraints.HORIZONTAL));

        panel.add(label("Tipo de preferencia:"), gbc(0, 2, 1, GridBagConstraints.NONE));
        panel.add(cboTipo, gbc(1, 2, 2, GridBagConstraints.HORIZONTAL));

        panel.add(label("Detalle:"), gbc(0, 3, 1, GridBagConstraints.NONE));
        panel.add(txtDetalle, gbc(1, 3, 2, GridBagConstraints.HORIZONTAL));

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        botones.setOpaque(false);
        botones.add(btnRegistrar);
        botones.add(btnConsultar);
        botones.add(btnLimpiar);
        panel.add(botones, gbc(0, 4, 3, GridBagConstraints.NONE));

        GridBagConstraints cTabla = gbc(0, 5, 3, GridBagConstraints.BOTH);
        cTabla.weightx = 1; cTabla.weighty = 1;
        panel.add(tabla(tblPref), cTabla);

        // --- Lógica ---
        Runnable cargar = () -> {
            modelo.setRowCount(0);
            String doc = txtDoc.getText().trim();
            if (doc.isEmpty()) return;
            List<PreferenciaHuesped> lista = hotel().obtenerPreferenciasHuesped(doc);
            if (lista.isEmpty()) {
                // Escenario "Preferencia no registrada" (consulta sin resultados)
                JOptionPane.showMessageDialog(panel,
                        "No se han registrado preferencias para este huésped.",
                        "Sin preferencias", JOptionPane.INFORMATION_MESSAGE);
            }
            for (PreferenciaHuesped p : lista) {
                modelo.addRow(new Object[]{
                    p.getTipoPreferencia(), p.getDetalle(),
                    p.getFechaRegistro() != null ? p.getFechaRegistro().format(FMT) : ""
                });
            }
        };

        btnRegistrar.addActionListener(e -> {
            String doc = txtDoc.getText().trim();
            String detalle = txtDetalle.getText().trim();
            String tipo = (String) cboTipo.getSelectedItem();

            // Escenario "Preferencia no registrada": faltan datos
            if (doc.isEmpty() || detalle.isEmpty()) {
                JOptionPane.showMessageDialog(panel,
                        "Debe indicar el documento y el detalle de la preferencia.",
                        "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (hotel().buscarHuespedPorDocumento(doc) == null) {
                JOptionPane.showMessageDialog(panel,
                        "No existe un huésped con el documento indicado.",
                        "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }
            PreferenciaHuesped pref = new PreferenciaHuesped(doc, tipo, detalle, LocalDateTime.now());
            boolean ok = hotel().registrarPreferencia(pref);
            if (ok) {
                SistemaHotel.getInstancia().guardarCambios();
                // Escenario "Preferencia registrada exitosamente"
                JOptionPane.showMessageDialog(panel,
                        "Preferencia registrada exitosamente.\nSe almacenará para futuras reservas.",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
                txtDetalle.setText("");
                cargar.run();
            } else {
                JOptionPane.showMessageDialog(panel,
                        "No se pudo registrar la preferencia. Verifique los datos.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnConsultar.addActionListener(e -> cargar.run());
        btnLimpiar.addActionListener(e -> {
            txtDoc.setText("");
            txtDetalle.setText("");
            cboTipo.setSelectedIndex(0);
            modelo.setRowCount(0);
        });

        return panel;
    }

    // =====================================================================
    //  F-005 : Consultar historial de estadías del huésped
    // =====================================================================
    public static JPanel historialEstadiasPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(FONDO);

        JTextField txtDoc   = new JTextField(16);
        JTextField txtDesde = new JTextField(10); // dd/MM/yyyy
        JTextField txtHasta = new JTextField(10);
        // Tamaño mínimo garantizado para que los campos de fecha sean usables
        // (con GridBag el fill horizontal los aplastaba a ~1px).
        java.awt.Dimension dimFecha = new java.awt.Dimension(110, 26);
        txtDesde.setPreferredSize(dimFecha);
        txtDesde.setMinimumSize(dimFecha);
        txtHasta.setPreferredSize(dimFecha);
        txtHasta.setMinimumSize(dimFecha);
        txtDoc.setMinimumSize(new java.awt.Dimension(160, 26));

        String[] cols = {"Ficha", "Habitación", "Ingreso", "Salida", "Noches", "Personas", "Estado", "Importe S/"};
        DefaultTableModel modelo = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tbl = new JTable(modelo);

        JButton btnConsultar = boton("Consultar historial");
        JButton btnFiltrar   = boton("Filtrar por fecha");
        JButton btnLimpiar   = boton("Limpiar");

        panel.add(titulo("F-005 · HISTORIAL DE ESTADÍAS DEL HUÉSPED"), gbc(0, 0, 6, GridBagConstraints.NONE));

        panel.add(label("Nro documento:"), gbc(0, 1, 1, GridBagConstraints.NONE));
        panel.add(txtDoc, gbc(1, 1, 2, GridBagConstraints.HORIZONTAL));

        JPanel filaFechas = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        filaFechas.setOpaque(false);
        filaFechas.add(label("Desde (dd/MM/yyyy):"));
        filaFechas.add(txtDesde);
        filaFechas.add(javax.swing.Box.createHorizontalStrut(12));
        filaFechas.add(label("Hasta (dd/MM/yyyy):"));
        filaFechas.add(txtHasta);
        panel.add(filaFechas, gbc(0, 2, 6, GridBagConstraints.NONE));

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        botones.setOpaque(false);
        botones.add(btnConsultar);
        botones.add(btnFiltrar);
        botones.add(btnLimpiar);
        panel.add(botones, gbc(0, 3, 6, GridBagConstraints.NONE));

        GridBagConstraints cTabla = gbc(0, 4, 6, GridBagConstraints.BOTH);
        cTabla.weightx = 1; cTabla.weighty = 1;
        panel.add(tabla(tbl), cTabla);

        // --- Lógica ---
        java.util.function.Consumer<List<FichaHospedaje>> pintar = (lista) -> {
            modelo.setRowCount(0);
            for (FichaHospedaje f : lista) {
                String salida = (f.getFechaSalida() != null)
                        ? f.getFechaSalida().format(FMT_DIA)
                        : "En curso";
                modelo.addRow(new Object[]{
                    f.getIdFicha(),
                    f.getHabitacion() != null ? f.getHabitacion().getNumero() : "-",
                    f.getFechaIngreso() != null ? f.getFechaIngreso().format(FMT_DIA) : "-",
                    salida,
                    f.getNochesEsperadas(),
                    f.getCantidadPersonas(),
                    estadoTexto(f.getEstado()),
                    String.format("%.2f", f.calcularImporteTotal())
                });
            }
        };

        btnConsultar.addActionListener(e -> {
            String doc = txtDoc.getText().trim();
            if (doc.isEmpty()) {
                JOptionPane.showMessageDialog(panel, "Ingrese el documento del huésped.",
                        "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (hotel().buscarHuespedPorDocumento(doc) == null) {
                JOptionPane.showMessageDialog(panel,
                        "No se encontraron estadías previas para este huésped.",
                        "Sin historial", JOptionPane.INFORMATION_MESSAGE);
                modelo.setRowCount(0);
                return;
            }
            List<FichaHospedaje> lista = hotel().obtenerHistorialEstadias(doc);
            if (lista.isEmpty()) {
                // Escenario "Sin historial previo"
                JOptionPane.showMessageDialog(panel,
                        "No se encontraron estadías previas para este huésped.",
                        "Sin historial", JOptionPane.INFORMATION_MESSAGE);
            }
            pintar.accept(lista);
        });

        btnFiltrar.addActionListener(e -> {
            String doc = txtDoc.getText().trim();
            if (doc.isEmpty()) {
                JOptionPane.showMessageDialog(panel, "Ingrese el documento del huésped.",
                        "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }
            LocalDate desde = parseFecha(txtDesde.getText().trim());
            LocalDate hasta = parseFecha(txtHasta.getText().trim());
            if ((!txtDesde.getText().trim().isEmpty() && desde == null)
                    || (!txtHasta.getText().trim().isEmpty() && hasta == null)) {
                JOptionPane.showMessageDialog(panel,
                        "Formato de fecha inválido. Use dd/MM/yyyy.",
                        "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }
            // Escenario "Filtro de historial por fecha"
            List<FichaHospedaje> lista = hotel().obtenerHistorialEstadiasPorFecha(doc, desde, hasta);
            if (lista.isEmpty()) {
                JOptionPane.showMessageDialog(panel,
                        "No hay estadías dentro del rango de fechas indicado.",
                        "Sin resultados", JOptionPane.INFORMATION_MESSAGE);
            }
            pintar.accept(lista);
        });

        btnLimpiar.addActionListener(e -> {
            txtDoc.setText("");
            txtDesde.setText("");
            txtHasta.setText("");
            modelo.setRowCount(0);
        });

        return panel;
    }

    // =====================================================================
    //  F-010 : Registrar el pago de servicios contratados
    // =====================================================================
    public static JPanel pagoServiciosPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(FONDO);

        JTextField txtDoc   = new JTextField(16);
        // Único "servicio": Pago total de la ficha de hospedaje del huésped.
        JComboBox<String> cboServicio = new JComboBox<>(new String[]{ "Pago total" });
        JComboBox<String> cboMetodo = new JComboBox<>(new String[]{
            "Seleccione", "QR", "Efectivo", "Tarjeta"
        });
        // El monto es el total de la ficha activa; se calcula al buscar (solo lectura).
        JTextField txtMonto = new JTextField(10);
        txtMonto.setEditable(false);
        txtMonto.setBackground(GRIS);

        // Referencia a la ficha activa encontrada (para cobrarla al registrar)
        final FichaHospedaje[] fichaSel = new FichaHospedaje[1];

        String[] cols = {"Comprobante", "Servicio", "Método", "Monto S/", "Fecha"};
        DefaultTableModel modelo = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tbl = new JTable(modelo);

        JButton btnBuscar    = boton("Buscar huésped");
        JButton btnRegistrar = boton("Registrar pago");
        JButton btnConsultar = boton("Consultar pagos");
        JButton btnLimpiar   = boton("Limpiar");

        panel.add(titulo("F-010 · PAGO DE SERVICIOS CONTRATADOS"), gbc(0, 0, 4, GridBagConstraints.NONE));

        panel.add(label("Nro documento:"), gbc(0, 1, 1, GridBagConstraints.NONE));
        panel.add(txtDoc, gbc(1, 1, 3, GridBagConstraints.HORIZONTAL));

        panel.add(label("Servicio:"), gbc(0, 2, 1, GridBagConstraints.NONE));
        panel.add(cboServicio, gbc(1, 2, 3, GridBagConstraints.HORIZONTAL));

        panel.add(label("Método de pago:"), gbc(0, 3, 1, GridBagConstraints.NONE));
        panel.add(cboMetodo, gbc(1, 3, 3, GridBagConstraints.HORIZONTAL));

        panel.add(label("Monto a pagar (S/):"), gbc(0, 4, 1, GridBagConstraints.NONE));
        panel.add(txtMonto, gbc(1, 4, 3, GridBagConstraints.HORIZONTAL));

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        botones.setOpaque(false);
        botones.add(btnBuscar);
        botones.add(btnRegistrar);
        botones.add(btnConsultar);
        botones.add(btnLimpiar);
        panel.add(botones, gbc(0, 5, 4, GridBagConstraints.NONE));

        GridBagConstraints cTabla = gbc(0, 6, 4, GridBagConstraints.BOTH);
        cTabla.weightx = 1; cTabla.weighty = 1;
        panel.add(tabla(tbl), cTabla);

        // --- Lógica ---
        Runnable cargar = () -> {
            modelo.setRowCount(0);
            String doc = txtDoc.getText().trim();
            if (doc.isEmpty()) return;
            for (PagoServicio p : hotel().obtenerPagosHuesped(doc)) {
                modelo.addRow(new Object[]{
                    p.getComprobante(), p.getServicio(), p.getMetodoPago(),
                    String.format("%.2f", p.getMonto()),
                    p.getFechaPago() != null ? p.getFechaPago().format(FMT) : ""
                });
            }
        };

        // Buscar: localiza la ficha ACTIVA (estado 'A') del huésped y muestra su total.
        btnBuscar.addActionListener(e -> {
            String doc = txtDoc.getText().trim();
            fichaSel[0] = null;
            txtMonto.setText("");
            if (doc.isEmpty()) {
                JOptionPane.showMessageDialog(panel, "Ingrese el documento del huésped.",
                        "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (hotel().buscarHuespedPorDocumento(doc) == null) {
                JOptionPane.showMessageDialog(panel,
                        "No existe un huésped con el documento indicado.",
                        "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }
            FichaHospedaje activa = hotel().buscarFichaActivaPorDocumento(doc);
            if (activa == null) {
                JOptionPane.showMessageDialog(panel,
                        "El huésped no tiene una estadía activa pendiente de pago.",
                        "Sin ficha activa", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            fichaSel[0] = activa;
            double total = activa.calcularTotalFicha();
            txtMonto.setText(String.format("%.2f", total));
            cargar.run();
        });

        btnRegistrar.addActionListener(e -> {
            String doc = txtDoc.getText().trim();
            String servicio = (String) cboServicio.getSelectedItem();
            String metodo   = (String) cboMetodo.getSelectedItem();

            // Debe haberse buscado antes una ficha activa
            if (fichaSel[0] == null) {
                JOptionPane.showMessageDialog(panel,
                        "Primero busque al huésped para calcular el monto a pagar.",
                        "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }
            // Escenario "Validación de datos": campos obligatorios
            if (doc.isEmpty() || "Seleccione".equals(metodo)) {
                JOptionPane.showMessageDialog(panel,
                        "Complete todos los campos obligatorios:\ndocumento y método de pago.",
                        "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }
            double monto = fichaSel[0].calcularTotalFicha();
            if (monto <= 0) {
                JOptionPane.showMessageDialog(panel, "El monto de la ficha debe ser mayor que cero.",
                        "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String comprobante = "PS-" + (System.currentTimeMillis() % 1000000);
            PagoServicio pago = new PagoServicio(doc, servicio, metodo, monto,
                    comprobante, LocalDateTime.now());
            pago.setIdFicha(fichaSel[0].getIdFicha());
            boolean ok = hotel().registrarPagoServicio(pago);
            if (ok) {
                SistemaHotel.getInstancia().guardarCambios();
                // Escenario "Registro exitoso de pago": muestra comprobante
                JTextArea area = new JTextArea(pago.generarComprobante());
                area.setFont(new Font("Monospaced", Font.PLAIN, 13));
                area.setEditable(false);
                area.setBackground(new Color(250, 250, 250));
                JOptionPane.showMessageDialog(panel, new JScrollPane(area),
                        "Pago registrado - Comprobante", JOptionPane.INFORMATION_MESSAGE);
                txtMonto.setText("");
                cboMetodo.setSelectedIndex(0);
                fichaSel[0] = null;
                cargar.run();
            } else {
                JOptionPane.showMessageDialog(panel,
                        "No se pudo registrar el pago. Verifique los datos.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnConsultar.addActionListener(e -> {
            String doc = txtDoc.getText().trim();
            if (doc.isEmpty()) {
                JOptionPane.showMessageDialog(panel, "Ingrese el documento del huésped.",
                        "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }
            // Escenario "Consulta de pago registrado"
            cargar.run();
            if (hotel().obtenerPagosHuesped(doc).isEmpty()) {
                JOptionPane.showMessageDialog(panel,
                        "El huésped no tiene pagos de servicios registrados.",
                        "Sin pagos", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        btnLimpiar.addActionListener(e -> {
            txtDoc.setText("");
            txtMonto.setText("");
            cboMetodo.setSelectedIndex(0);
            fichaSel[0] = null;
            modelo.setRowCount(0);
        });

        return panel;
    }

    // =====================================================================
    //  F-011 : Historial de estadías + preferencias (vista combinada)
    // =====================================================================
    public static JPanel historialCompletoPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(FONDO);

        JTextField txtDoc = new JTextField(16);
        JButton btnConsultar = boton("Consultar historial y preferencias");
        JButton btnLimpiar   = boton("Limpiar");

        // Tabla de estadías
        String[] colsEst = {"Ficha", "Habitación", "Ingreso", "Salida", "Noches", "Importe S/"};
        DefaultTableModel modeloEst = new DefaultTableModel(colsEst, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tblEst = new JTable(modeloEst);

        // Tabla de preferencias
        String[] colsPref = {"Tipo", "Detalle", "Fecha registro"};
        DefaultTableModel modeloPref = new DefaultTableModel(colsPref, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tblPref = new JTable(modeloPref);

        panel.add(titulo("F-011 · HISTORIAL Y PREFERENCIAS DEL HUÉSPED"), gbc(0, 0, 3, GridBagConstraints.NONE));

        panel.add(label("Nro documento:"), gbc(0, 1, 1, GridBagConstraints.NONE));
        panel.add(txtDoc, gbc(1, 1, 2, GridBagConstraints.HORIZONTAL));

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        botones.setOpaque(false);
        botones.add(btnConsultar);
        botones.add(btnLimpiar);
        panel.add(botones, gbc(0, 2, 3, GridBagConstraints.NONE));

        panel.add(subtitulo("Estadías anteriores"), gbc(0, 3, 3, GridBagConstraints.NONE));
        GridBagConstraints cEst = gbc(0, 4, 3, GridBagConstraints.BOTH);
        cEst.weightx = 1; cEst.weighty = 0.5;
        panel.add(tabla(tblEst), cEst);

        panel.add(subtitulo("Preferencias registradas"), gbc(0, 5, 3, GridBagConstraints.NONE));
        GridBagConstraints cPref = gbc(0, 6, 3, GridBagConstraints.BOTH);
        cPref.weightx = 1; cPref.weighty = 0.5;
        panel.add(tabla(tblPref), cPref);

        btnConsultar.addActionListener(e -> {
            String doc = txtDoc.getText().trim();
            modeloEst.setRowCount(0);
            modeloPref.setRowCount(0);
            if (doc.isEmpty()) {
                JOptionPane.showMessageDialog(panel, "Ingrese el documento del huésped.",
                        "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Huesped h = hotel().buscarHuespedPorDocumento(doc);
            if (h == null) {
                JOptionPane.showMessageDialog(panel,
                        "No existe un huésped con el documento indicado.",
                        "Sin datos", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            // Estadías
            for (FichaHospedaje f : hotel().obtenerHistorialEstadias(doc)) {
                String salida = (f.getFechaSalida() != null)
                        ? f.getFechaSalida().format(FMT_DIA) : "En curso";
                modeloEst.addRow(new Object[]{
                    f.getIdFicha(),
                    f.getHabitacion() != null ? f.getHabitacion().getNumero() : "-",
                    f.getFechaIngreso() != null ? f.getFechaIngreso().format(FMT_DIA) : "-",
                    salida,
                    f.getNochesEsperadas(),
                    String.format("%.2f", f.calcularImporteTotal())
                });
            }
            // Preferencias
            for (PreferenciaHuesped p : hotel().obtenerPreferenciasHuesped(doc)) {
                modeloPref.addRow(new Object[]{
                    p.getTipoPreferencia(), p.getDetalle(),
                    p.getFechaRegistro() != null ? p.getFechaRegistro().format(FMT) : ""
                });
            }
            if (modeloEst.getRowCount() == 0 && modeloPref.getRowCount() == 0) {
                JOptionPane.showMessageDialog(panel,
                        "El huésped no tiene estadías ni preferencias registradas.",
                        "Sin registros", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        btnLimpiar.addActionListener(e -> {
            txtDoc.setText("");
            modeloEst.setRowCount(0);
            modeloPref.setRowCount(0);
        });

        return panel;
    }

    // =========================== Utilidades ===========================

    private static JLabel subtitulo(String texto) {
        JLabel l = new JLabel(texto);
        l.setFont(F_SUBTITULO);
        l.setForeground(AZUL_NOCHE);
        return l;
    }

    private static String estadoTexto(char estado) {
        switch (estado) {
            case 'A': return "Activa";
            case 'F': return "Finalizada";
            case 'M': return "Mantenimiento";
            default:  return String.valueOf(estado);
        }
    }

    private static LocalDate parseFecha(String texto) {
        if (texto == null || texto.isEmpty()) return null;
        try {
            return LocalDate.parse(texto, FMT_DIA);
        } catch (Exception ex) {
            return null;
        }
    }
}
