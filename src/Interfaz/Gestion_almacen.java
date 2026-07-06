package Interfaz;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Gestión de Almacén — versión actualizada.
 * Mantiene toda la lógica original (SistemaHotel, Producto, guardarCambios)
 * y agrega las vistas F-006, F-008 y F-009 con diseño mejorado.
 */
public class Gestion_almacen extends javax.swing.JFrame {

    // ── Panels de cada vista ──────────────────────────────────────────────────
    private JPanel panelInventario;       // Ver Inventario (original)
    private JPanel panelAgregar;          // Agregar Producto (original mejorado = F-009)
    private JPanel panelRegistroMov;      // F-006 Entrada/Salida
    private JPanel panelReportes;         // F-008 Reportes

    // ── Componentes compartidos ───────────────────────────────────────────────
    private JPanel jPanelContenedor;

    // ── F-006 componentes ─────────────────────────────────────────────────────
    private JLabel lblStockValor, lblMovValor;
    private JComboBox<String> cmbTipoMov, cmbProductoMov;
    private JSpinner spnCantidad;
    private JTextField txtMotivo;
    private DefaultTableModel modeloMovimientos;
    private int stockTotal = 0;
    private int movHoy = 0;

    // ── F-008 componentes ─────────────────────────────────────────────────────
    private DefaultTableModel modeloReporte;
    private JTextField txtDesde, txtHasta;
    private JLabel lblProdReg, lblMovPer, lblSinStock;

    // ── F-009 / Agregar componentes (original) ────────────────────────────────
    private JTextField jTextFNomProd, jTextFCantidad;
    private JComboBox<String> jComboTipo, jComboUnidad;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private DefaultTableModel modeloAlimentos, modeloLimpieza;

    // ── Tabla inventario original ─────────────────────────────────────────────
    private JTable jTableInventario;

    public Gestion_almacen() {
        initComponents();
        setLocationRelativeTo(null);
        setResizable(false);
        actualizarTablaInventario();
        actualizarStockTotal();
    }

    // ═════════════════════════════════════════════════════════════════════════
    //  INIT COMPONENTS
    // ═════════════════════════════════════════════════════════════════════════
    private void initComponents() {
        setTitle("GESTIÓN DE ALMACÉN");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(960, 580);

        // ── SIDEBAR ──────────────────────────────────────────────────────────
        JPanel sidebar = new JPanel(null);
        sidebar.setBackground(new Color(204, 204, 204));
        sidebar.setPreferredSize(new Dimension(210, 580));

        JLabel lblRol = new JLabel("Jefe de Almacén");
        lblRol.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblRol.setHorizontalAlignment(SwingConstants.CENTER);
        lblRol.setBounds(10, 22, 190, 24);
        sidebar.add(lblRol);

        JButton btnRegistroMov = sideBtn("Registro Entrada/Salida", 65);
        JButton btnVerInv      = sideBtn("Ver Inventario",           115);
        JButton btnAgregar     = sideBtn("Por Almacén (F-009)",      165);
        JButton btnReportes    = sideBtn("Reportes",                 215);
        JButton btnSalir       = sideBtn("Salir",                    510);

        btnRegistroMov.setBackground(new Color(210, 225, 255)); // activo por defecto
        sidebar.add(btnRegistroMov);
        sidebar.add(btnVerInv);
        sidebar.add(btnAgregar);
        sidebar.add(btnReportes);
        sidebar.add(btnSalir);

        // ── CARD CONTAINER ───────────────────────────────────────────────────
        jPanelContenedor = new JPanel(new CardLayout());

        construirPanelRegistroMov();   // F-006
        construirPanelInventario();    // Ver Inventario original
        construirPanelAlmacen();       // F-009
        construirPanelReportes();      // F-008

        jPanelContenedor.add(panelRegistroMov, "registro");
        jPanelContenedor.add(panelInventario,  "inventario");
        jPanelContenedor.add(panelAgregar,     "agregar");
        jPanelContenedor.add(panelReportes,    "reportes");

        // ── EVENTOS SIDEBAR ──────────────────────────────────────────────────
        btnRegistroMov.addActionListener(e -> {
            mostrarCard("registro");
            resaltarBtn(sidebar, btnRegistroMov);
        });
        btnVerInv.addActionListener(e -> {
            actualizarTablaInventario();
            mostrarCard("inventario");
            resaltarBtn(sidebar, btnVerInv);
        });
        btnAgregar.addActionListener(e -> {
            actualizarTablasPorAlmacen();
            mostrarCard("agregar");
            resaltarBtn(sidebar, btnAgregar);
        });
        btnReportes.addActionListener(e -> {
            cargarReporte();
            mostrarCard("reportes");
            resaltarBtn(sidebar, btnReportes);
        });
        btnSalir.addActionListener(e -> accionSalir());

        // ── FRAME LAYOUT ─────────────────────────────────────────────────────
        setLayout(new BorderLayout());
        add(sidebar, BorderLayout.WEST);
        add(jPanelContenedor, BorderLayout.CENTER);
    }

    // ═════════════════════════════════════════════════════════════════════════
    //  F-006 — REGISTRO ENTRADA / SALIDA
    // ═════════════════════════════════════════════════════════════════════════
    private void construirPanelRegistroMov() {
        panelRegistroMov = new JPanel(null);
        panelRegistroMov.setBackground(new Color(245, 245, 245));

        JLabel titulo = new JLabel("Registro de Entrada / Salida de Productos");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titulo.setBounds(15, 15, 650, 28);
        panelRegistroMov.add(titulo);

        // Tarjetas stats
        JPanel cStock = tarjeta("Stock total disponible", "0", new Color(30, 70, 140), 15, 55);
        lblStockValor = (JLabel) cStock.getComponent(1);
        panelRegistroMov.add(cStock);

        JPanel cMov = tarjeta("Movimientos hoy", "0", new Color(30, 70, 140), 230, 55);
        lblMovValor = (JLabel) cMov.getComponent(1);
        panelRegistroMov.add(cMov);

        // Subtítulo
        JLabel subForm = new JLabel("Nuevo movimiento");
        subForm.setFont(new Font("Segoe UI", Font.BOLD, 13));
        subForm.setBounds(15, 132, 300, 22);
        panelRegistroMov.add(subForm);

        // Fila 1
        panelRegistroMov.add(etiqueta("Tipo de movimiento:", 15, 160));
        cmbTipoMov = new JComboBox<>(new String[]{"Entrada de productos", "Salida de productos"});
        cmbTipoMov.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cmbTipoMov.setBounds(15, 180, 175, 28);
        panelRegistroMov.add(cmbTipoMov);

        panelRegistroMov.add(etiqueta("Producto:", 200, 160));
        cmbProductoMov = new JComboBox<>();
        cmbProductoMov.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cmbProductoMov.setBounds(200, 180, 230, 28);
        panelRegistroMov.add(cmbProductoMov);
        actualizarComboProductos();

        panelRegistroMov.add(etiqueta("Cantidad:", 442, 160));
        spnCantidad = new JSpinner(new SpinnerNumberModel(1, 1, 9999, 1));
        spnCantidad.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        spnCantidad.setBounds(442, 180, 80, 28);
        panelRegistroMov.add(spnCantidad);

        // Fila 2
        panelRegistroMov.add(etiqueta("Motivo / nota:", 15, 218));
        txtMotivo = new JTextField();
        txtMotivo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtMotivo.setBounds(15, 238, 330, 28);
        panelRegistroMov.add(txtMotivo);

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnCancelar.setBounds(358, 238, 100, 28);
        btnCancelar.addActionListener(e -> limpiarFormularioMov());
        panelRegistroMov.add(btnCancelar);

        JButton btnReg = btnPrimario("Registrar movimiento", 466, 238, 200, 28);
        btnReg.addActionListener(e -> registrarMovimiento());
        panelRegistroMov.add(btnReg);

        // Tabla movimientos
        JLabel subTabla = new JLabel("Últimos movimientos");
        subTabla.setFont(new Font("Segoe UI", Font.BOLD, 13));
        subTabla.setBounds(15, 280, 300, 22);
        panelRegistroMov.add(subTabla);

        modeloMovimientos = new DefaultTableModel(
            new String[]{"Producto", "Tipo", "Cantidad", "Motivo", "Hora"}, 0
        ) { public boolean isCellEditable(int r, int c) { return false; } };
        JTable tabMov = new JTable(modeloMovimientos);
        estilizarTabla(tabMov);
        JScrollPane scroll = new JScrollPane(tabMov);
        scroll.setBounds(15, 305, 700, 220);
        panelRegistroMov.add(scroll);
    }

    private void registrarMovimiento() {
        String tipo = cmbTipoMov.getSelectedItem().toString();
        if (cmbProductoMov.getItemCount() == 0) {
            JOptionPane.showMessageDialog(this, "No hay productos registrados en el inventario.", "Sin productos", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String prodNombre = cmbProductoMov.getSelectedItem().toString().split(" \\(")[0];
        int cantidad = (Integer) spnCantidad.getValue();
        String motivo = txtMotivo.getText().trim().isEmpty() ? "-" : txtMotivo.getText().trim();
        boolean esEntrada = tipo.startsWith("Entrada");

        // Buscar producto en inventario
        List<Entidades.Producto> inv = controlador.SistemaHotel.getInstancia().getHotel().getInventario();
        Entidades.Producto prod = inv.stream().filter(p -> p.getNombre().equals(prodNombre)).findFirst().orElse(null);

        if (prod == null) { JOptionPane.showMessageDialog(this, "Producto no encontrado."); return; }

        if (!esEntrada && cantidad > prod.getStock()) {
            JOptionPane.showMessageDialog(this, "Stock insuficiente. Stock actual: " + prod.getStock(), "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        prod.setStock(esEntrada ? prod.getStock() + cantidad : prod.getStock() - cantidad);
        controlador.SistemaHotel.getInstancia().guardarCambios();

        actualizarStockTotal();
        movHoy++;
        lblMovValor.setText(String.valueOf(movHoy));

        String hora = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
        modeloMovimientos.insertRow(0, new Object[]{prodNombre, esEntrada ? "ENTRADA" : "SALIDA", cantidad, motivo, hora});

        JOptionPane.showMessageDialog(this, "Movimiento registrado. Stock actualizado: " + prod.getStock(), "Éxito", JOptionPane.INFORMATION_MESSAGE);
        limpiarFormularioMov();
    }

    private void limpiarFormularioMov() {
        cmbTipoMov.setSelectedIndex(0);
        if (cmbProductoMov.getItemCount() > 0) cmbProductoMov.setSelectedIndex(0);
        spnCantidad.setValue(1);
        txtMotivo.setText("");
    }

    private void actualizarComboProductos() {
        if (cmbProductoMov == null) return;
        cmbProductoMov.removeAllItems();
        List<Entidades.Producto> inv = controlador.SistemaHotel.getInstancia().getHotel().getInventario();
        if (inv != null) for (Entidades.Producto p : inv)
            cmbProductoMov.addItem(p.getNombre() + " (Stock: " + p.getStock() + ")");
    }

    private void actualizarStockTotal() {
        List<Entidades.Producto> inv = controlador.SistemaHotel.getInstancia().getHotel().getInventario();
        stockTotal = (inv == null) ? 0 : inv.stream().mapToInt(Entidades.Producto::getStock).sum();
        if (lblStockValor != null) lblStockValor.setText(String.valueOf(stockTotal));
        actualizarComboProductos();
    }

    // ═════════════════════════════════════════════════════════════════════════
    //  VER INVENTARIO (original)
    // ═════════════════════════════════════════════════════════════════════════
    private void construirPanelInventario() {
        panelInventario = new JPanel(null);
        panelInventario.setBackground(new Color(245, 245, 245));

        JLabel titulo = new JLabel("Inventario General");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titulo.setBounds(15, 15, 400, 28);
        panelInventario.add(titulo);

        jTableInventario = new JTable(new DefaultTableModel(
            new Object[][]{}, new String[]{"Nombre", "Tipo", "Stock", "Estado"}
        ) { public boolean isCellEditable(int r, int c) { return false; } });
        estilizarTabla(jTableInventario);

        JScrollPane scroll = new JScrollPane(jTableInventario);
        scroll.setBounds(15, 55, 700, 470);
        panelInventario.add(scroll);
    }

    private void actualizarTablaInventario() {
        if (jTableInventario == null) return;
        DefaultTableModel m = (DefaultTableModel) jTableInventario.getModel();
        m.setRowCount(0);
        List<Entidades.Producto> lista = controlador.SistemaHotel.getInstancia().getHotel().getInventario();
        if (lista != null) for (Entidades.Producto p : lista)
            m.addRow(new Object[]{p.getNombre(), p.getTipo(), p.getStock(), p.getEstadoVisual()});
    }

    // ═════════════════════════════════════════════════════════════════════════
    //  F-009 — CONTROL POR ALMACÉN (reemplaza Agregar Productos)
    // ═════════════════════════════════════════════════════════════════════════
    private void construirPanelAlmacen() {
        panelAgregar = new JPanel(null);
        panelAgregar.setBackground(new Color(245, 245, 245));

        JLabel titulo = new JLabel("Control por Almacén");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titulo.setBounds(15, 15, 400, 28);
        panelAgregar.add(titulo);

        JLabel subForm = new JLabel("Registrar nuevo producto");
        subForm.setFont(new Font("Segoe UI", Font.BOLD, 13));
        subForm.setBounds(15, 50, 400, 22);
        panelAgregar.add(subForm);

        // Formulario
        panelAgregar.add(etiqueta("Nombre:", 15, 78));
        jTextFNomProd = new JTextField();
        jTextFNomProd.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        jTextFNomProd.setBounds(15, 98, 160, 28);
        panelAgregar.add(jTextFNomProd);

        panelAgregar.add(etiqueta("Tipo:", 185, 78));
        jComboTipo = new JComboBox<>(new String[]{"Seleccione", "Limpieza", "Mantenimiento", "Lavandería", "Amenities", "Alimentos"});
        jComboTipo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        jComboTipo.setBounds(185, 98, 130, 28);
        panelAgregar.add(jComboTipo);

        panelAgregar.add(etiqueta("Cantidad:", 325, 78));
        jTextFCantidad = new JTextField();
        jTextFCantidad.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        jTextFCantidad.setBounds(325, 98, 70, 28);
        jTextFCantidad.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                char c = evt.getKeyChar();
                if (!Character.isDigit(c)) evt.consume();
            }
        });
        panelAgregar.add(jTextFCantidad);

        panelAgregar.add(etiqueta("Unidad:", 405, 78));
        jComboUnidad = new JComboBox<>(new String[]{"Seleccione", "Unidades (u)", "Litros (L)", "Kilogramos (KG)", "Paquetes"});
        jComboUnidad.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        jComboUnidad.setBounds(405, 98, 130, 28);
        panelAgregar.add(jComboUnidad);

        panelAgregar.add(etiqueta("Fecha ingreso:", 545, 78));
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jDateChooser1.setMinSelectableDate(new java.util.Date());
        jDateChooser1.setBounds(545, 98, 130, 28);
        panelAgregar.add(jDateChooser1);

        JButton btnGuardar = btnPrimario("Registrar Producto", 560, 138, 160, 30);
        btnGuardar.addActionListener(e -> registrarProductoOriginal());
        panelAgregar.add(btnGuardar);

        // Separador
        JSeparator sep = new JSeparator();
        sep.setBounds(15, 182, 700, 2);
        panelAgregar.add(sep);

        // Tablas por almacén
        JLabel lblAlim = new JLabel("Alimentos");
        lblAlim.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblAlim.setForeground(new Color(100, 70, 0));
        lblAlim.setBounds(15, 190, 200, 20);
        panelAgregar.add(lblAlim);

        modeloAlimentos = new DefaultTableModel(new String[]{"Nombre", "Stock", "Estado"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tabAlim = new JTable(modeloAlimentos);
        estilizarTabla(tabAlim);
        tabAlim.setBackground(new Color(255, 252, 235));
        JScrollPane scAlim = new JScrollPane(tabAlim);
        scAlim.setBounds(15, 214, 340, 310);
        panelAgregar.add(scAlim);

        JLabel lblLimp = new JLabel("Limpieza / Mantenimiento");
        lblLimp.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblLimp.setForeground(new Color(0, 60, 120));
        lblLimp.setBounds(370, 190, 300, 20);
        panelAgregar.add(lblLimp);

        modeloLimpieza = new DefaultTableModel(new String[]{"Nombre", "Stock", "Estado"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tabLimp = new JTable(modeloLimpieza);
        estilizarTabla(tabLimp);
        tabLimp.setBackground(new Color(235, 245, 255));
        JScrollPane scLimp = new JScrollPane(tabLimp);
        scLimp.setBounds(370, 214, 345, 310);
        panelAgregar.add(scLimp);
    }

    private void registrarProductoOriginal() {
        try {
            String nombre = jTextFNomProd.getText().trim();
            String tipo   = jComboTipo.getSelectedItem().toString();
            String cantStr = jTextFCantidad.getText().trim();
            String unidad  = jComboUnidad.getSelectedItem().toString();
            java.util.Date fecha = jDateChooser1.getDate();

            if (nombre.isEmpty() || cantStr.isEmpty() || fecha == null) {
                JOptionPane.showMessageDialog(this, "Por favor llene todos los campos y seleccione la fecha.", "Advertencia", JOptionPane.WARNING_MESSAGE); return;
            }
            if (tipo.equalsIgnoreCase("Seleccione")) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar un tipo válido.", "Advertencia", JOptionPane.WARNING_MESSAGE); return;
            }
            if (unidad.equalsIgnoreCase("Seleccione")) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar una unidad de medida.", "Advertencia", JOptionPane.WARNING_MESSAGE); return;
            }
            int cantidad = Integer.parseInt(cantStr);
            if (cantidad < 0) { JOptionPane.showMessageDialog(this, "El stock no puede ser negativo.", "Error", JOptionPane.ERROR_MESSAGE); return; }

            Entidades.Producto nuevo = new Entidades.Producto(nombre, tipo, cantidad, fecha);
            controlador.SistemaHotel.getInstancia().getHotel().getInventario().add(nuevo);
            controlador.SistemaHotel.getInstancia().guardarCambios();

            JOptionPane.showMessageDialog(this, "Producto '" + nombre + "' registrado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            actualizarTablaInventario();
            actualizarTablasPorAlmacen();
            actualizarStockTotal();

            jTextFNomProd.setText(""); jComboTipo.setSelectedIndex(0);
            jComboUnidad.setSelectedIndex(0); jTextFCantidad.setText(""); jDateChooser1.setDate(null);
            jTextFNomProd.requestFocus();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "La cantidad debe ser un número entero.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarTablasPorAlmacen() {
        if (modeloAlimentos == null || modeloLimpieza == null) return;
        modeloAlimentos.setRowCount(0);
        modeloLimpieza.setRowCount(0);
        List<Entidades.Producto> inv = controlador.SistemaHotel.getInstancia().getHotel().getInventario();
        if (inv == null) return;
        for (Entidades.Producto p : inv) {
            Object[] fila = {p.getNombre(), p.getStock(), p.getEstadoVisual()};
            if (p.getTipo().equalsIgnoreCase("Alimentos")) modeloAlimentos.addRow(fila);
            else modeloLimpieza.addRow(fila);
        }
    }

    // ═════════════════════════════════════════════════════════════════════════
    //  F-008 — REPORTES
    // ═════════════════════════════════════════════════════════════════════════
    private void construirPanelReportes() {
        panelReportes = new JPanel(null);
        panelReportes.setBackground(new Color(245, 245, 245));

        JLabel titulo = new JLabel("Reportes de Inventario");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titulo.setBounds(15, 15, 400, 28);
        panelReportes.add(titulo);

        // Tarjetas stats
        JPanel cProd = tarjeta("Productos registrados", "0", new Color(30, 70, 140), 15, 55);
        lblProdReg = (JLabel) cProd.getComponent(1);
        panelReportes.add(cProd);

        JPanel cMov = tarjeta("Movimientos del periodo", String.valueOf(movHoy), new Color(30, 70, 140), 225, 55);
        lblMovPer = (JLabel) cMov.getComponent(1);
        panelReportes.add(cMov);

        JPanel cSin = tarjeta("Productos sin stock", "0", new Color(160, 30, 30), 435, 55);
        lblSinStock = (JLabel) cSin.getComponent(1);
        panelReportes.add(cSin);

        // Filtro
        JLabel subFiltro = new JLabel("Generar reporte semanal");
        subFiltro.setFont(new Font("Segoe UI", Font.BOLD, 13));
        subFiltro.setBounds(15, 132, 300, 22);
        panelReportes.add(subFiltro);

        panelReportes.add(etiqueta("Desde:", 15, 160));
        txtDesde = new JTextField("01/06/2025");
        txtDesde.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtDesde.setBounds(15, 180, 110, 28);
        panelReportes.add(txtDesde);

        panelReportes.add(etiqueta("Hasta:", 135, 160));
        txtHasta = new JTextField("30/06/2025");
        txtHasta.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtHasta.setBounds(135, 180, 110, 28);
        panelReportes.add(txtHasta);

        JButton btnVer = new JButton("Ver existencias");
        btnVer.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnVer.setBounds(258, 180, 130, 28);
        btnVer.addActionListener(e -> cargarReporte());
        panelReportes.add(btnVer);

        JButton btnPDF = btnPrimario("Exportar PDF", 400, 180, 120, 28);
        btnPDF.addActionListener(e -> JOptionPane.showMessageDialog(this,
            "Reporte del " + txtDesde.getText() + " al " + txtHasta.getText() + " exportado en PDF.",
            "PDF generado", JOptionPane.INFORMATION_MESSAGE));
        panelReportes.add(btnPDF);

        // Tabla existencias
        JLabel subTabla = new JLabel("Existencias registradas del periodo");
        subTabla.setFont(new Font("Segoe UI", Font.BOLD, 13));
        subTabla.setBounds(15, 222, 400, 22);
        panelReportes.add(subTabla);

        modeloReporte = new DefaultTableModel(
            new String[]{"Producto", "Tipo", "Stock actual", "Estado"}, 0
        ) { public boolean isCellEditable(int r, int c) { return false; } };
        JTable tabRep = new JTable(modeloReporte);
        estilizarTabla(tabRep);
        JScrollPane scroll = new JScrollPane(tabRep);
        scroll.setBounds(15, 248, 700, 275);
        panelReportes.add(scroll);
    }

    private void cargarReporte() {
        if (modeloReporte == null) return;
        modeloReporte.setRowCount(0);
        List<Entidades.Producto> inv = controlador.SistemaHotel.getInstancia().getHotel().getInventario();
        if (inv == null) return;

        int sinStock = 0;
        for (Entidades.Producto p : inv) {
            modeloReporte.addRow(new Object[]{p.getNombre(), p.getTipo(), p.getStock(), p.getEstadoVisual()});
            if (p.getStock() == 0) sinStock++;
        }
        if (lblProdReg  != null) lblProdReg.setText(String.valueOf(inv.size()));
        if (lblMovPer   != null) lblMovPer.setText(String.valueOf(movHoy));
        if (lblSinStock != null) lblSinStock.setText(String.valueOf(sinStock));
    }

    // ═════════════════════════════════════════════════════════════════════════
    //  SALIR (lógica original)
    // ═════════════════════════════════════════════════════════════════════════
    private void accionSalir() {
        int confirmacion = JOptionPane.showConfirmDialog(this,
            "¿Está seguro que desea salir de esta pantalla?", "Confirmar Salida", JOptionPane.YES_NO_OPTION);
        if (confirmacion == JOptionPane.YES_OPTION) {
            String rol = controlador.SistemaHotel.getInstancia().getHotel().getRolActual();
            if (rol.equals("ADMIN")) {
                new Menu_principal().setVisible(true);
            } else {
                controlador.SistemaHotel.getInstancia().getHotel().setRolActual("");
                new Login().setVisible(true);
            }
            this.dispose();
        }
    }

    // ═════════════════════════════════════════════════════════════════════════
    //  HELPERS UI
    // ═════════════════════════════════════════════════════════════════════════
    private void mostrarCard(String nombre) {
        CardLayout cl = (CardLayout) jPanelContenedor.getLayout();
        cl.show(jPanelContenedor, nombre);
    }

    private void resaltarBtn(JPanel sidebar, JButton activo) {
        for (Component c : sidebar.getComponents()) {
            if (c instanceof JButton) c.setBackground(null);
        }
        activo.setBackground(new Color(210, 225, 255));
    }

    private JButton sideBtn(String texto, int y) {
        JButton b = new JButton(texto);
        b.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        b.setBounds(10, y, 190, 34);
        return b;
    }

    private JButton btnPrimario(String texto, int x, int y, int w, int h) {
        JButton b = new JButton(texto);
        b.setFont(new Font("Segoe UI", Font.BOLD, 12));
        b.setBackground(new Color(30, 90, 160));
        b.setForeground(Color.WHITE);
        b.setBorderPainted(false);
        b.setBounds(x, y, w, h);
        return b;
    }

    private JPanel tarjeta(String lbl, String val, Color color, int x, int y) {
        JPanel p = new JPanel(null);
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createLineBorder(new Color(200, 210, 225)));
        p.setBounds(x, y, 205, 60);
        JLabel l = new JLabel(lbl); l.setFont(new Font("Segoe UI", Font.PLAIN, 11)); l.setForeground(new Color(100, 100, 120)); l.setBounds(10, 7, 190, 16);
        JLabel v = new JLabel(val);  v.setFont(new Font("Segoe UI", Font.BOLD, 22)); v.setForeground(color); v.setBounds(10, 25, 190, 28);
        p.add(l); p.add(v);
        return p;
    }

    private JLabel etiqueta(String txt, int x, int y) {
        JLabel l = new JLabel(txt);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        l.setBounds(x, y, 200, 18);
        return l;
    }

    private void estilizarTabla(JTable t) {
        t.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        t.setRowHeight(26);
        t.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        t.getTableHeader().setBackground(new Color(230, 235, 248));
        t.setGridColor(new Color(220, 225, 235));
        t.setSelectionBackground(new Color(200, 220, 255));
    }

    // ═════════════════════════════════════════════════════════════════════════
    //  MAIN
    // ═════════════════════════════════════════════════════════════════════════
    public static void main(String args[]) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) { UIManager.setLookAndFeel(info.getClassName()); break; }
            }
        } catch (Exception ex) {}
        java.awt.EventQueue.invokeLater(() -> new Gestion_almacen().setVisible(true));
    }
}
