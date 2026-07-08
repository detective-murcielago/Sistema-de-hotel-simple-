package Interfaz;

import Entidades.OrdenCompra;
import Entidades.Producto;
import controlador.SistemaHotel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Hotel TruGarden – Módulo Jefe de Compras (ERP) Pestañas: 1. Gestión de
 * Productos 2. Aprobación de Órdenes → letras negras + motivo de rechazo 3.
 * Cuentas por Pagar → botón lupa busca orden y rellena datos 4. Consulta de
 * Stock 5. Reporte Financiero
 *
 * Conectado a: - SistemaHotel (datos reales del hotel) - Gestion_almacen
 * (inventario compartido)
 */
public class Modulo_de_compras extends JFrame {

    // ── Paleta del sistema ────────────────────────────────────────────
    private static final Color C_FONDO = new Color(204, 204, 204);
    private static final Color C_NARANJA = new Color(255, 102, 0);
    private static final Color C_VERDE = new Color(204, 255, 204);
    private static final Color C_AZUL = new Color(102, 153, 255);
    private static final Color C_ROJO = new Color(255, 102, 102);
    private static final Color C_TEAL = new Color(0, 102, 102);
    private static final Color C_FILA_P = new Color(240, 248, 255);
    private static final Color C_VENC = new Color(255, 230, 230);
    private static final Color C_PAGADA = new Color(230, 255, 230);
    private static final Color C_CRIT = new Color(255, 230, 230);

    private final SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy");

    // ── Pestaña 1 ────────────────────────────────────────────────────
    private JTextField jTextFProducto, jTextFProveedor, jTextFStock,
            jTextFCantidad, jTextFCostoTotal;
    private JComboBox<String> jComboBoxTipo;
    private com.toedter.calendar.JDateChooser jDateEmision, jDateEntrega;
    private DefaultTableModel modeloOrdenes;

    // ── Pestaña 2 ────────────────────────────────────────────────────
    private JTable jTableAprobacion;
    private DefaultTableModel modeloAprobacion;

    // ── Pestaña 3 ────────────────────────────────────────────────────
    private JTextField txtNroFactura, txtProveedor2, txtMonto, txtDescripcion;
    private JComboBox<String> cmbTipoPago, cmbEstadoPago;
    private com.toedter.calendar.JDateChooser jDateCuentaEmision, jDateCuentaVenc;
    private JTable tablaCuentas;
    private DefaultTableModel modeloCuentas;
    private JLabel lblPendientes, lblTotalDeuda;

    // ── Pestaña 4 ────────────────────────────────────────────────────
    private JTextField txtBuscar;
    private JTable tablaInventario;
    private DefaultTableModel modeloInventario;
    private JLabel lblTotalProd, lblCriticos, lblSuficientes;
    private JPanel pnlDetalle;
    private JLabel lblDetNombre, lblDetStock, lblDetEstado, lblDetTipo;

    // ── Pestaña 5 ────────────────────────────────────────────────────
    private JTextArea txtReporte;

    // ════════════════════════════════════════════════════════════════
    public Modulo_de_compras() {
        initComponents();
        setLocationRelativeTo(null);
        setResizable(false);
        actualizarTablaOrdenes();
        actualizarTablaAprobacion();
        actualizarTablaInventario();
    }

    // ════════════════════════════════════════════════════════════════
    //  INIT
    // ════════════════════════════════════════════════════════════════
    private void initComponents() {
        setTitle("Hotel TruGarden - Modulo Jefe de Compras");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabs.setBackground(C_FONDO);

        tabs.addTab("Gestión de Productos", buildTabOrdenes());
        tabs.addTab("Aprobación de Órdenes", buildTabAprobacion());
        tabs.addTab("Cuentas por Pagar", buildTabCuentas());
        tabs.addTab("Consulta de Stock", buildTabStock());
        tabs.addTab("Reporte Financiero", buildTabReporte());

        setContentPane(tabs);
        pack();
        setSize(980, 680);
    }

    // ════════════════════════════════════════════════════════════════
    //  PESTAÑA 1 – Gestión de Productos
    // ════════════════════════════════════════════════════════════════
    private JPanel buildTabOrdenes() {
        JPanel p = new JPanel(null);
        p.setBackground(Color.WHITE);
        p.setPreferredSize(new Dimension(960, 530));

        titulo(p, "Registro de Nueva Orden de Compra", 140, 18);

        int lx = 150, fx = 380, w = 210, ry = 75, gap = 36;

        p.add(lbl("Productos:", lx, ry));
        jTextFProducto = tf();
        jTextFProducto.setBounds(fx, ry, w, 26);
        jTextFProducto.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                autoStock();
            }
        });
        p.add(jTextFProducto);

        p.add(lbl("Tipo:", lx, ry + gap));
        jComboBoxTipo = new JComboBox<>(
                new String[]{"Seleccione", "Limpieza", "Mantenimiento", "Lavandería", "Amenities", "Gastronomía"});
        jComboBoxTipo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        jComboBoxTipo.setBounds(fx, ry + gap, w, 26);
        p.add(jComboBoxTipo);

        p.add(lbl("Proveedor:", lx, ry + gap * 2));
        jTextFProveedor = tf();
        jTextFProveedor.setBounds(fx, ry + gap * 2, w, 26);
        p.add(jTextFProveedor);

        p.add(lbl("Stock Actual vs Mínimo:", lx, ry + gap * 3));
        jTextFStock = tf();
        jTextFStock.setEditable(false);
        jTextFStock.setBackground(C_FONDO);
        jTextFStock.setBounds(fx, ry + gap * 3, w, 26);
        p.add(jTextFStock);

        p.add(lbl("Cantidad a Solicitar:", lx, ry + gap * 4));
        jTextFCantidad = tf();
        jTextFCantidad.setBounds(fx, ry + gap * 4, w, 26);
        jTextFCantidad.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                soloNum(e, jTextFCantidad);
            }
        });
        p.add(jTextFCantidad);

        p.add(lbl("Costo Total (S/.):", lx, ry + gap * 5));
        jTextFCostoTotal = tf();
        jTextFCostoTotal.setBounds(fx, ry + gap * 5, w, 26);
        jTextFCostoTotal.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                soloNum(e, jTextFCostoTotal);
            }
        });
        p.add(jTextFCostoTotal);

        p.add(lbl("Fecha emisión:", lx, ry + gap * 6));
        java.util.Date hoyP1 = new java.util.Date();
        jDateEmision = new com.toedter.calendar.JDateChooser();
        jDateEmision.setDateFormatString("dd/MM/yyyy");
        jDateEmision.setDate(hoyP1);
        jDateEmision.setMinSelectableDate(hoyP1);
        jDateEmision.setBounds(fx, ry + gap * 6, 145, 26);
        p.add(jDateEmision);

        p.add(lbl("Fecha entrega:", lx + 385, ry + gap * 6));
        jDateEntrega = new com.toedter.calendar.JDateChooser();
        jDateEntrega.setDateFormatString("dd/MM/yyyy");
        jDateEntrega.setDate(hoyP1);
        jDateEntrega.setMinSelectableDate(hoyP1);
        jDateEntrega.setBounds(fx + 265, ry + gap * 6, 145, 26);
        p.add(jDateEntrega);

        JButton btnGen = btn("Generar Orden", C_VERDE);
        btnGen.setBounds(220, ry + gap * 7 + 14, 155, 30);
        JButton btnLimp = btn("Limpiar", C_AZUL);
        btnLimp.setBounds(392, ry + gap * 7 + 14, 110, 30);
        JButton btnReg = btn("Regresar", Color.lightGray);
        btnReg.setBounds(518, ry + gap * 7 + 14, 110, 30);
        btnGen.addActionListener(e -> generarOrden());
        btnLimp.addActionListener(e -> limpiarOrden());
        btnReg.addActionListener(e -> regresar());
        p.add(btnGen);
        p.add(btnLimp);
        p.add(btnReg);
        return p;
    }

    // ════════════════════════════════════════════════════════════════
    //  PESTAÑA 2 – Aprobación de Órdenes
    // ════════════════════════════════════════════════════════════════
    private JPanel buildTabAprobacion() {
        JPanel p = new JPanel(new BorderLayout(0, 10));
        p.setBackground(Color.WHITE);
        p.setBorder(new EmptyBorder(16, 20, 16, 20));

        JLabel t = new JLabel("Bandeja de Revision - Jefe de Compras");
        t.setFont(new Font("Segoe UI", Font.BOLD, 22));
        t.setForeground(Color.BLACK);
        t.setHorizontalAlignment(SwingConstants.CENTER);
        p.add(t, BorderLayout.NORTH);

        String[] cols = {"ID Orden", "Producto", "Cantidad", "Fecha Emisión", "Proveedor", "Estado", "Motivo Rechazo"};
        modeloAprobacion = noEdit(cols);
        jTableAprobacion = tabla(modeloAprobacion);

        jTableAprobacion.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable tbl, Object v, boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(tbl, v, sel, foc, row, col);
                if (!sel) {
                    String estado = tbl.getValueAt(row, 5).toString();
                    if ("Aprobado".equals(estado)) {
                        setBackground(new Color(220, 255, 220));
                    } else if ("Rechazado".equals(estado)) {
                        setBackground(new Color(255, 220, 220));
                    } else {
                        setBackground(row % 2 == 0 ? C_FILA_P : Color.WHITE);
                    }
                }
                setForeground(Color.BLACK);
                if (col == 5) {
                    setFont(getFont().deriveFont(Font.BOLD));
                    setHorizontalAlignment(CENTER);
                }
                return this;
            }
        });

        JPanel leyenda = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 2));
        leyenda.setOpaque(false);
        leyenda.add(chip("  Pendiente  ", C_FILA_P));
        leyenda.add(chip("  Aprobado   ", new Color(220, 255, 220)));
        leyenda.add(chip("  Rechazado  ", new Color(255, 220, 220)));

        JPanel centro = new JPanel(new BorderLayout(0, 4));
        centro.setOpaque(false);
        centro.add(leyenda, BorderLayout.NORTH);
        centro.add(new JScrollPane(jTableAprobacion), BorderLayout.CENTER);
        p.add(centro, BorderLayout.CENTER);

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 8));
        btns.setOpaque(false);

        JButton btnAp = btn("Aprobar", C_VERDE);
        btnAp.setForeground(Color.BLACK);
        btnAp.addActionListener(e -> aprobarOrden());

        JButton btnRe = btn("Rechazar", C_ROJO);
        btnRe.setForeground(Color.BLACK);
        btnRe.addActionListener(e -> rechazarOrdenConMotivo());

        JButton btnRef = btn("Actualizar", C_AZUL);
        btnRef.setForeground(Color.BLACK);
        btnRef.addActionListener(e -> actualizarTablaAprobacion());

        btns.add(btnAp);
        btns.add(btnRe);
        btns.add(btnRef);
        p.add(btns, BorderLayout.SOUTH);
        return p;
    }

    // ════════════════════════════════════════════════════════════════
    //  PESTAÑA 3 – Cuentas por Pagar
    // ════════════════════════════════════════════════════════════════
    private JPanel buildTabCuentas() {
        JPanel p = new JPanel(new BorderLayout(0, 8));
        p.setBackground(Color.WHITE);
        p.setBorder(new EmptyBorder(12, 18, 10, 18));

        JLabel titLbl = new JLabel("Registro de Cuentas por Pagar");
        titLbl.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titLbl.setForeground(Color.BLACK);
        p.add(titLbl, BorderLayout.NORTH);

        JPanel mid = new JPanel(new BorderLayout(10, 0));
        mid.setOpaque(false);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(C_FONDO);
        form.setBorder(new EmptyBorder(12, 16, 12, 16));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6, 5, 6, 5);
        g.fill = GridBagConstraints.HORIZONTAL;
        g.anchor = GridBagConstraints.WEST;

        g.gridy = 0;
        g.gridwidth = 1;
        g.gridx = 0;
        g.weightx = 0;
        form.add(etiq("* Nro. Factura:"), g);

        txtNroFactura = tf2();
        JButton btnLupa = new JButton("[ Buscar ]");
        btnLupa.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnLupa.setBackground(C_AZUL);
        btnLupa.setForeground(Color.BLACK);
        btnLupa.setFocusPainted(false);
        btnLupa.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnLupa.setToolTipText("Buscar orden de compra por Nro. de Factura / ID");
        btnLupa.addActionListener(e -> buscarOrdenParaFactura());

        JPanel pnlFact = new JPanel(new BorderLayout(4, 0));
        pnlFact.setOpaque(false);
        pnlFact.add(txtNroFactura, BorderLayout.CENTER);
        pnlFact.add(btnLupa, BorderLayout.EAST);

        g.gridx = 1;
        g.weightx = 0.4;
        form.add(pnlFact, g);

        g.gridx = 2;
        g.weightx = 0;
        form.add(etiq("* Proveedor:"), g);
        txtProveedor2 = tf2();
        g.gridx = 3;
        g.weightx = 0.6;
        form.add(txtProveedor2, g);

        txtMonto = tf2();
        txtMonto.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                soloNum(e, txtMonto);
            }
        });
        cmbTipoPago = combo("Seleccione...", "Contado", "Credito 30 dias", "Credito 60 dias", "Credito 90 dias");
        row4(form, g, 1, "* Monto (S/.):", txtMonto, "* Tipo de Pago:", cmbTipoPago);

        java.util.Calendar hoy = java.util.Calendar.getInstance();
        jDateCuentaEmision = new com.toedter.calendar.JDateChooser();
        jDateCuentaEmision.setDateFormatString("dd/MM/yyyy");
        jDateCuentaEmision.setDate(hoy.getTime());
        jDateCuentaEmision.setMinSelectableDate(hoy.getTime());
        jDateCuentaVenc = new com.toedter.calendar.JDateChooser();
        jDateCuentaVenc.setDateFormatString("dd/MM/yyyy");
        jDateCuentaVenc.setDate(hoy.getTime());
        jDateCuentaVenc.setMinSelectableDate(hoy.getTime());
        row4(form, g, 2, "* Fecha Emisión:", jDateCuentaEmision, "* Fecha Vencim.:", jDateCuentaVenc);

        cmbEstadoPago = combo("Pendiente", "Pagado", "Vencido");
        txtDescripcion = tf2();
        row4(form, g, 3, "* Estado:", cmbEstadoPago, "Descripción:", txtDescripcion);

        g.gridy = 4;
        g.gridx = 0;
        g.gridwidth = 4;
        g.fill = GridBagConstraints.NONE;
        g.anchor = GridBagConstraints.CENTER;
        JPanel bRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 14, 0));
        bRow.setOpaque(false);
        JButton btnG = btn("Guardar", C_VERDE);
        btnG.addActionListener(e -> guardarCuenta());
        JButton btnL = btn("Limpiar", C_AZUL);
        btnL.addActionListener(e -> limpiarCuenta());
        JButton btnAct = btn("Actualizar", C_TEAL);
        btnAct.addActionListener(e -> actualizarCuentas());
        bRow.add(btnG);
        bRow.add(btnL);
        bRow.add(btnAct);
        form.add(bRow, g);

        mid.add(form, BorderLayout.CENTER);

        JPanel reglas = new JPanel();
        reglas.setLayout(new BoxLayout(reglas, BoxLayout.Y_AXIS));
        reglas.setBackground(new Color(235, 245, 255));
        reglas.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(C_AZUL, 1), new EmptyBorder(12, 12, 12, 12)));
        reglas.setPreferredSize(new Dimension(220, 0));

        JLabel rTit = new JLabel("<html><b>Reglas de negocio</b></html>");
        rTit.setFont(new Font("Segoe UI", Font.BOLD, 13));
        reglas.add(rTit);
        reglas.add(Box.createVerticalStrut(8));
        for (String r : new String[]{
            "Los campos con * son obligatorios.",
            "Usa el boton [ Buscar ] para buscar una orden",
            "  y rellenar los datos automáticamente.",
            "El Nro. de factura debe ser único.",
            "El monto debe ser mayor a 0.",
            "Vencimiento >= fecha de emision.",
            "Vencidas se resaltan en rojo."}) {
            JLabel li = new JLabel("<html>- " + r + "</html>");
            li.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            li.setBorder(new EmptyBorder(3, 0, 3, 0));
            reglas.add(li);
        }
        mid.add(reglas, BorderLayout.EAST);
        p.add(mid, BorderLayout.CENTER);

        JPanel sur = new JPanel(new BorderLayout(0, 4));
        sur.setOpaque(false);

        String[] colsC = {"Nro. Factura", "Proveedor", "Monto (S/.)", "Tipo Pago", "F. Emisión", "F. Vencim.", "Estado"};
        modeloCuentas = noEdit(colsC);
        tablaCuentas = tabla(modeloCuentas);
        tablaCuentas.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable tbl, Object v, boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(tbl, v, sel, foc, row, col);
                if (!sel) {
                    String est = tbl.getValueAt(row, 6).toString();
                    setBackground("Vencido".equals(est) ? C_VENC
                            : "Pagado".equals(est) ? C_PAGADA
                            : row % 2 == 0 ? C_FILA_P : Color.WHITE);
                }
                setForeground(Color.BLACK);
                if (col == 6) {
                    setHorizontalAlignment(CENTER);
                    setFont(getFont().deriveFont(Font.BOLD));
                }
                return this;
            }
        });
        JScrollPane sc = new JScrollPane(tablaCuentas);
        sc.setPreferredSize(new Dimension(0, 120));
        sur.add(sc, BorderLayout.CENTER);

        JPanel barra = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 4));
        barra.setBackground(new Color(44, 62, 80));
        lblPendientes = new JLabel();
        lblPendientes.setForeground(Color.WHITE);
        lblPendientes.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTotalDeuda = new JLabel();
        lblTotalDeuda.setForeground(Color.WHITE);
        lblTotalDeuda.setFont(new Font("Segoe UI", Font.BOLD, 13));
        barra.add(lblPendientes);
        barra.add(lblTotalDeuda);
        sur.add(barra, BorderLayout.SOUTH);
        actualizarResumenCuentas();

        p.add(sur, BorderLayout.SOUTH);
        return p;
    }

    // ════════════════════════════════════════════════════════════════
    //  PESTAÑA 4 – Consulta de Stock
    // ════════════════════════════════════════════════════════════════
    private JPanel buildTabStock() {
        JPanel p = new JPanel(new BorderLayout(0, 10));
        p.setBackground(Color.WHITE);
        p.setBorder(new EmptyBorder(14, 18, 14, 18));

        JLabel t = new JLabel("Consulta de Inventario / Stock  [F-007]");
        t.setFont(new Font("Segoe UI", Font.BOLD, 20));
        t.setForeground(C_NARANJA);
        p.add(t, BorderLayout.NORTH);

        JPanel dash = new JPanel(new GridLayout(1, 3, 12, 0));
        dash.setOpaque(false);
        dash.setBorder(new EmptyBorder(0, 0, 8, 0));
        lblTotalProd = new JLabel("0");
        lblTotalProd.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblCriticos = new JLabel("0");
        lblCriticos.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblSuficientes = new JLabel("0");
        lblSuficientes.setFont(new Font("Segoe UI", Font.BOLD, 26));
        dash.add(tarjetaDash("Total productos", lblTotalProd, C_AZUL, new Color(230, 240, 255)));
        dash.add(tarjetaDash("Stock crítico", lblCriticos, new Color(200, 60, 60), new Color(255, 230, 230)));
        dash.add(tarjetaDash("Stock suficiente", lblSuficientes, new Color(40, 140, 70), new Color(220, 255, 225)));

        JPanel busq = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 4));
        busq.setBackground(C_FONDO);
        busq.setBorder(new EmptyBorder(6, 10, 6, 10));
        JLabel lbBusq = new JLabel("Buscar producto:");
        lbBusq.setFont(new Font("Segoe UI", Font.BOLD, 13));
        busq.add(lbBusq);
        txtBuscar = new JTextField(26);
        txtBuscar.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtBuscar.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filtrarInventario();
            }
        });
        busq.add(txtBuscar);
        JButton btnB = btn("Buscar", C_AZUL);
        btnB.addActionListener(e -> buscarProducto());
        JButton btnT = btn("Ver todo", C_TEAL);
        btnT.addActionListener(e -> mostrarTodoInv());
        busq.add(btnB);
        busq.add(btnT);

        String[] colsI = {"Nombre", "Tipo / Categoría", "Stock Actual", "Stock Mínimo", "Estado"};
        modeloInventario = noEdit(colsI);
        tablaInventario = tabla(modeloInventario);
        tablaInventario.setDefaultRenderer(Object.class, rendererInv());
        tablaInventario.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int f = tablaInventario.getSelectedRow();
                if (f >= 0) {
                    mostrarDetalleProducto(f);
                }
            }
        });

        pnlDetalle = new JPanel(new GridLayout(2, 2, 10, 4));
        pnlDetalle.setBackground(new Color(230, 243, 255));
        pnlDetalle.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(C_AZUL, 1), new EmptyBorder(8, 14, 8, 14)));
        pnlDetalle.setPreferredSize(new Dimension(0, 68));
        pnlDetalle.setVisible(false);
        lblDetNombre = new JLabel();
        lblDetNombre.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblDetTipo = new JLabel();
        lblDetTipo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblDetStock = new JLabel();
        lblDetStock.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblDetEstado = new JLabel();
        lblDetEstado.setFont(new Font("Segoe UI", Font.BOLD, 13));
        pnlDetalle.add(lblDetNombre);
        pnlDetalle.add(lblDetStock);
        pnlDetalle.add(lblDetTipo);
        pnlDetalle.add(lblDetEstado);

        JPanel tablaWrap = new JPanel(new BorderLayout(0, 6));
        tablaWrap.setOpaque(false);
        tablaWrap.add(busq, BorderLayout.NORTH);
        tablaWrap.add(new JScrollPane(tablaInventario), BorderLayout.CENTER);
        tablaWrap.add(pnlDetalle, BorderLayout.SOUTH);

        p.add(dash, BorderLayout.NORTH);
        p.add(tablaWrap, BorderLayout.CENTER);
        return p;
    }

    // ════════════════════════════════════════════════════════════════
    //  PESTAÑA 5 – Reporte Financiero
    //  CAMBIO: botón "Exportar PDF" reemplazado por "Descargar reporte (.txt)"
    // ════════════════════════════════════════════════════════════════
    private JPanel buildTabReporte() {
        JPanel p = new JPanel(new BorderLayout(0, 10));
        p.setBackground(Color.WHITE);
        p.setBorder(new EmptyBorder(14, 20, 14, 20));

        JLabel t = new JLabel("Reporte Financiero - Hotel TruGarden");
        t.setFont(new Font("Segoe UI", Font.BOLD, 20));
        t.setForeground(Color.BLACK);
        p.add(t, BorderLayout.NORTH);

        txtReporte = new JTextArea();
        txtReporte.setEditable(false);
        txtReporte.setFont(new Font("Consolas", Font.PLAIN, 13));
        txtReporte.setBackground(new Color(250, 252, 255));
        txtReporte.setForeground(new Color(30, 40, 60));
        txtReporte.setBorder(new EmptyBorder(12, 14, 12, 14));
        JScrollPane scroll = new JScrollPane(txtReporte);
        scroll.setBorder(BorderFactory.createLineBorder(C_AZUL, 1));
        p.add(scroll, BorderLayout.CENTER);

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 6));
        btnRow.setOpaque(false);
        JButton btnGenerar = btn("Generar Reporte", C_AZUL);
        JButton btnOrdenes = btn("+ Incluir Ordenes", C_TEAL);
        JButton btnExportTXT = btn("Descargar reporte (.xlsx)", new Color(0, 120, 50));
        JButton btnLimpiarR = btn("Limpiar", C_ROJO);
        btnGenerar.addActionListener(e -> generarReporte(false));
        btnOrdenes.addActionListener(e -> generarReporte(true));
        btnExportTXT.addActionListener(e -> exportarExcel());
        btnLimpiarR.addActionListener(e -> txtReporte.setText(""));
        btnRow.add(btnLimpiarR);
        btnRow.add(btnOrdenes);
        btnRow.add(btnGenerar);
        btnRow.add(btnExportTXT);
        p.add(btnRow, BorderLayout.SOUTH);
        return p;
    }

    // ════════════════════════════════════════════════════════════════
    //  LÓGICA P1 – Gestión de Productos
    // ════════════════════════════════════════════════════════════════
    private void generarOrden() {
        String producto = jTextFProducto.getText().trim();
        String tipo = (String) jComboBoxTipo.getSelectedItem();
        String cantStr = jTextFCantidad.getText().trim();
        String proveedor = jTextFProveedor.getText().trim();
        String precioStr = jTextFCostoTotal.getText().trim();
        Date fEmision = jDateEmision.getDate();
        Date fEntrega = jDateEntrega.getDate();

        if (producto.isEmpty() || cantStr.isEmpty() || proveedor.isEmpty()
                || precioStr.isEmpty() || fEmision == null || fEntrega == null) {
            JOptionPane.showMessageDialog(this, "Complete todos los campos y fechas.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if ("Seleccione".equalsIgnoreCase(tipo)) {
            JOptionPane.showMessageDialog(this, "Seleccione un tipo de producto.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (fEntrega.before(fEmision)) {
            JOptionPane.showMessageDialog(this, "La fecha de entrega no puede ser anterior a la de emision.", "Error de fechas", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            int cantidad = Integer.parseInt(cantStr);
            double precio = Double.parseDouble(precioStr);
            if (cantidad <= 0 || precio <= 0) {
                JOptionPane.showMessageDialog(this, "Cantidad y precio deben ser mayores a 0.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String id = "ORD-" + (System.currentTimeMillis() % 100000);
            OrdenCompra nueva = new OrdenCompra(id, producto, tipo, cantidad, fEmision, fEntrega, proveedor, precio);
            SistemaHotel.getInstancia().getHotel().getListaOrdenes().add(nueva);
            SistemaHotel.getInstancia().guardarCambios();
            JOptionPane.showMessageDialog(this, "Orden generada.\nID: " + id, "Éxito", JOptionPane.INFORMATION_MESSAGE);
            actualizarTablaOrdenes();
            actualizarTablaAprobacion();
            limpiarOrden();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingrese números válidos en Cantidad y Precio.", "Formato incorrecto", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarOrden() {
        jTextFProducto.setText("");
        jTextFProveedor.setText("");
        jTextFStock.setText("");
        jTextFCantidad.setText("");
        jTextFCostoTotal.setText("");
        jComboBoxTipo.setSelectedIndex(0);
        jDateEmision.setDate(null);
        jDateEntrega.setDate(null);
        jTextFProducto.requestFocus();
    }

    private void autoStock() {
        String nombre = jTextFProducto.getText().trim();
        if (nombre.isEmpty()) {
            jTextFStock.setText("");
            return;
        }
        List<Producto> inv = SistemaHotel.getInstancia().getHotel().getInventario();
        if (inv != null) {
            for (Producto pr : inv) {
                if (pr.getNombre().equalsIgnoreCase(nombre)) {
                    jTextFStock.setText(pr.getStock() + " vs " + pr.getStockMinimo());
                    return;
                }
            }
        }
        jTextFStock.setText("0 vs 9 (Nuevo)");
    }

    private void actualizarTablaOrdenes() {
        if (modeloOrdenes == null) {
            modeloOrdenes = noEdit(new String[]{"ID Orden", "Producto", "Cantidad", "Fecha Emisión", "Proveedor", "Estado"});
        }
        modeloOrdenes.setRowCount(0);
        List<OrdenCompra> lista = SistemaHotel.getInstancia().getHotel().getListaOrdenes();
        if (lista != null) {
            for (OrdenCompra o : lista) {
                modeloOrdenes.addRow(new Object[]{
                    o.getIdOrden(), o.getNombreProducto(), o.getCantidad(),
                    SDF.format(o.getFechaEmision()), o.getProveedor(), o.getEstado()});
            }
        }
    }

    // ════════════════════════════════════════════════════════════════
    //  LÓGICA P2 – Aprobación de Órdenes
    // ════════════════════════════════════════════════════════════════
    private void aprobarOrden() {
        int fila = jTableAprobacion.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una orden de la tabla.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String estadoActual = jTableAprobacion.getValueAt(fila, 5).toString();
        if ("Aprobado".equals(estadoActual)) {
            JOptionPane.showMessageDialog(this, "Esta orden ya está Aprobada.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        String id = jTableAprobacion.getValueAt(fila, 0).toString();
        for (OrdenCompra o : SistemaHotel.getInstancia().getHotel().getListaOrdenes()) {
            if (o.getIdOrden().equals(id)) {
                o.setEstado("Aprobado");
                SistemaHotel.getInstancia().guardarCambios();
                JOptionPane.showMessageDialog(this,
                        "Orden " + id + " aprobada exitosamente.", "Aprobada", JOptionPane.INFORMATION_MESSAGE);
                break;
            }
        }
        actualizarTablaAprobacion();
        actualizarTablaOrdenes();
    }

    private void rechazarOrdenConMotivo() {
        int fila = jTableAprobacion.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una orden de la tabla.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String estadoActual = jTableAprobacion.getValueAt(fila, 5).toString();
        if ("Rechazado".equals(estadoActual)) {
            JOptionPane.showMessageDialog(this, "Esta orden ya fue Rechazada.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        JTextArea txtMotivo = new JTextArea(4, 36);
        txtMotivo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtMotivo.setLineWrap(true);
        txtMotivo.setWrapStyleWord(true);
        txtMotivo.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        JScrollPane sp = new JScrollPane(txtMotivo);

        JPanel pnlDlg = new JPanel(new BorderLayout(0, 8));
        pnlDlg.add(new JLabel("<html><b>Indique el motivo del rechazo:</b></html>"), BorderLayout.NORTH);
        pnlDlg.add(sp, BorderLayout.CENTER);
        pnlDlg.add(new JLabel("<html><i>Este motivo quedará registrado en la orden.</i></html>"), BorderLayout.SOUTH);

        int result = JOptionPane.showConfirmDialog(this, pnlDlg,
                "Rechazar Orden - " + jTableAprobacion.getValueAt(fila, 0),
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);

        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        String motivo = txtMotivo.getText().trim();
        if (motivo.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Debe ingresar un motivo para rechazar la orden.", "Motivo requerido", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String id = jTableAprobacion.getValueAt(fila, 0).toString();
        for (OrdenCompra o : SistemaHotel.getInstancia().getHotel().getListaOrdenes()) {
            if (o.getIdOrden().equals(id)) {
                o.setEstado("Rechazado");
                SistemaHotel.getInstancia().guardarCambios();
                JOptionPane.showMessageDialog(this,
                        "Orden " + id + " rechazada.\nMotivo: " + motivo,
                        "Rechazada", JOptionPane.WARNING_MESSAGE);
                break;
            }
        }
        actualizarTablaAprobacion();
        actualizarTablaOrdenes();
        // FIX 1: escribir el motivo en la columna 6 de la fila correspondiente
        for (int i = 0; i < modeloAprobacion.getRowCount(); i++) {
            if (id.equals(modeloAprobacion.getValueAt(i, 0).toString())) {
                modeloAprobacion.setValueAt(motivo, i, 6);
                break;
            }
        }
    }

    private void actualizarTablaAprobacion() {
        if (modeloAprobacion == null) {
            return;
        }
        modeloAprobacion.setRowCount(0);
        List<OrdenCompra> lista = SistemaHotel.getInstancia().getHotel().getListaOrdenes();
        if (lista != null) {
            for (OrdenCompra o : lista) {
                modeloAprobacion.addRow(new Object[]{
                    o.getIdOrden(), o.getNombreProducto(), o.getCantidad(),
                    SDF.format(o.getFechaEmision()), o.getProveedor(),
                    o.getEstado(), ""});
            }
        }
    }

    // ════════════════════════════════════════════════════════════════
    //  LÓGICA P3 – Cuentas por Pagar
    // ════════════════════════════════════════════════════════════════
    private void buscarOrdenParaFactura() {
        String termino = txtNroFactura.getText().trim();
        if (termino.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Ingrese el Nro. de Factura / ID de Orden a buscar.",
                    "Campo vacío", JOptionPane.WARNING_MESSAGE);
            return;
        }

        List<OrdenCompra> lista = SistemaHotel.getInstancia().getHotel().getListaOrdenes();
        OrdenCompra encontrada = null;
        if (lista != null) {
            for (OrdenCompra o : lista) {
                if (o.getIdOrden().equalsIgnoreCase(termino)
                        || o.getNombreProducto().toLowerCase().contains(termino.toLowerCase())) {
                    encontrada = o;
                    break;
                }
            }
        }

        if (encontrada == null) {
            JOptionPane.showMessageDialog(this,
                    "No se encontró ninguna orden con:\n  \"" + termino + "\"\n\n"
                    + "Verifique el ID o nombre de producto.",
                    "Orden no encontrada", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if ("Rechazado".equals(encontrada.getEstado())) {
            JOptionPane.showMessageDialog(this,
                    "ORDEN RECHAZADA: \"" + encontrada.getIdOrden() + "\" fue RECHAZADA.\n\n"
                    + "Una orden rechazada no puede convertirse en cuenta por pagar.\n"
                    + "Solo se pueden registrar ordenes Aprobadas o Pendientes.",
                    "Orden Rechazada - Operacion Bloqueada", JOptionPane.ERROR_MESSAGE);
            return;
        }

        OrdenCompra ord = encontrada;
        txtNroFactura.setText(ord.getIdOrden());
        txtProveedor2.setText(ord.getProveedor());
        txtMonto.setText(String.format("%.2f", ord.getPrecioTotal()));
        txtDescripcion.setText(ord.getNombreProducto() + " - Tipo: " + ord.getTipo()
                + " - Cant: " + ord.getCantidad());

        cmbTipoPago.setSelectedItem("Contado");
        jDateCuentaEmision.setDate(ord.getFechaEmision());
        jDateCuentaVenc.setDate(ord.getFechaEntrega());

        if ("Aprobado".equals(ord.getEstado())) {
            cmbEstadoPago.setSelectedItem("Pendiente");
        } else if ("Rechazado".equals(ord.getEstado())) {
            cmbEstadoPago.setSelectedItem("Vencido");
        } else {
            cmbEstadoPago.setSelectedItem("Pendiente");
        }

        JOptionPane.showMessageDialog(this,
                "Datos cargados desde la orden: " + ord.getIdOrden()
                + "\n\nVerifique y complete los campos antes de Guardar.",
                "Datos rellenados", JOptionPane.INFORMATION_MESSAGE);
    }

    private void guardarCuenta() {
        StringBuilder falt = new StringBuilder();
        if (txtNroFactura.getText().trim().isEmpty()) {
            falt.append("  * Numero de Factura\n");
        }
        if (txtProveedor2.getText().trim().isEmpty()) {
            falt.append("  * Proveedor\n");
        }
        if (txtMonto.getText().trim().isEmpty()) {
            falt.append("  * Monto\n");
        }
        if (cmbTipoPago.getSelectedIndex() == 0) {
            falt.append("  * Tipo de Pago\n");
        }
        if (jDateCuentaEmision.getDate() == null) {
            falt.append("  * Fecha de Emision\n");
        }
        if (jDateCuentaVenc.getDate() == null) {
            falt.append("  * Fecha de Vencimiento\n");
        }
        if (falt.length() > 0) {
            JOptionPane.showMessageDialog(this, "Los siguientes campos son obligatorios:\n\n" + falt,
                    "Formulario incompleto", JOptionPane.ERROR_MESSAGE);
            return;
        }
        double monto;
        try {
            monto = Double.parseDouble(txtMonto.getText().trim().replace(",", "."));
            if (monto <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El monto debe ser un número mayor a 0.", "Monto inválido", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Date fEm = jDateCuentaEmision.getDate();
        Date fVn = jDateCuentaVenc.getDate();
        if (fVn.before(fEm)) {
            JOptionPane.showMessageDialog(this, "La fecha de vencimiento no puede ser anterior a la de emisión.", "Fechas inválidas", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String nroFact = txtNroFactura.getText().trim().toUpperCase();
        for (int i = 0; i < modeloCuentas.getRowCount(); i++) {
            if (nroFact.equals(modeloCuentas.getValueAt(i, 0))) {
                JOptionPane.showMessageDialog(this,
                        "DUPLICADO - La factura \"" + nroFact + "\" ya existe en el sistema.\nDocumento duplicado - proceso bloqueado.",
                        "Documento Duplicado", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }
        modeloCuentas.addRow(new Object[]{
            nroFact, txtProveedor2.getText().trim(),
            String.format("S/ %,.2f", monto),
            cmbTipoPago.getSelectedItem(),
            SDF.format(fEm), SDF.format(fVn),
            cmbEstadoPago.getSelectedItem()
        });
        actualizarResumenCuentas();

        int ultimaFila = modeloCuentas.getRowCount() - 1;
        tablaCuentas.setRowSelectionInterval(ultimaFila, ultimaFila);
        tablaCuentas.scrollRectToVisible(tablaCuentas.getCellRect(ultimaFila, 0, true));

        limpiarCuenta();
    }

    private void limpiarCuenta() {
        txtNroFactura.setText("");
        txtProveedor2.setText("");
        txtMonto.setText("");
        txtDescripcion.setText("");
        cmbTipoPago.setSelectedIndex(0);
        cmbEstadoPago.setSelectedIndex(0);
        java.util.Date hoy = new java.util.Date();
        jDateCuentaEmision.setDate(hoy);
        jDateCuentaVenc.setDate(hoy);
    }

    private void actualizarCuentas() {
        int filaSeleccionada = tablaCuentas.getSelectedRow();

        if (filaSeleccionada >= 0) {
            String nroFact = modeloCuentas.getValueAt(filaSeleccionada, 0).toString();
            String proveedor = modeloCuentas.getValueAt(filaSeleccionada, 1).toString();
            String montoActual = modeloCuentas.getValueAt(filaSeleccionada, 2).toString();
            String tipoActual = modeloCuentas.getValueAt(filaSeleccionada, 3).toString();
            String estActual = modeloCuentas.getValueAt(filaSeleccionada, 6).toString();

            JPanel dlg = new JPanel(new GridBagLayout());
            GridBagConstraints gc = new GridBagConstraints();
            gc.insets = new Insets(6, 8, 6, 8);
            gc.anchor = GridBagConstraints.WEST;
            gc.fill = GridBagConstraints.HORIZONTAL;

            gc.gridy = 0;
            gc.gridx = 0;
            dlg.add(new JLabel("Factura:"), gc);
            gc.gridx = 1;
            JLabel lNro = new JLabel(nroFact);
            lNro.setFont(new Font("Segoe UI", Font.BOLD, 13));
            dlg.add(lNro, gc);
            gc.gridy = 1;
            gc.gridx = 0;
            dlg.add(new JLabel("Proveedor:"), gc);
            gc.gridx = 1;
            dlg.add(new JLabel(proveedor), gc);
            gc.gridy = 2;
            gc.gridx = 0;
            dlg.add(new JLabel("Monto:"), gc);
            gc.gridx = 1;
            dlg.add(new JLabel(montoActual), gc);

            JSeparator sep = new JSeparator();
            sep.setPreferredSize(new Dimension(320, 1));
            gc.gridy = 3;
            gc.gridx = 0;
            gc.gridwidth = 2;
            dlg.add(sep, gc);
            gc.gridwidth = 1;

            gc.gridy = 4;
            gc.gridx = 0;
            dlg.add(new JLabel("Nuevo Estado:"), gc);
            JComboBox<String> cmbNuevoEstado = new JComboBox<>(new String[]{"Pendiente", "Pagado", "Vencido"});
            cmbNuevoEstado.setSelectedItem(estActual);
            cmbNuevoEstado.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            gc.gridx = 1;
            dlg.add(cmbNuevoEstado, gc);

            gc.gridy = 5;
            gc.gridx = 0;
            dlg.add(new JLabel("Tipo de Pago:"), gc);
            JComboBox<String> cmbNuevoTipo = new JComboBox<>(new String[]{"Contado", "Credito 30 dias", "Credito 60 dias", "Credito 90 dias"});
            cmbNuevoTipo.setSelectedItem(tipoActual);
            cmbNuevoTipo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            gc.gridx = 1;
            dlg.add(cmbNuevoTipo, gc);

            gc.gridy = 6;
            gc.gridx = 0;
            gc.gridwidth = 2;
            JLabel hint = new JLabel("<html><i>Seleccione el nuevo estado y tipo de pago.</i></html>");
            hint.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            dlg.add(hint, gc);

            int result = JOptionPane.showConfirmDialog(
                    this, dlg,
                    "Editar Cuenta: " + nroFact,
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE);

            if (result != JOptionPane.OK_OPTION) {
                return;
            }

            String nuevoEstado = cmbNuevoEstado.getSelectedItem().toString();
            String nuevoTipo = cmbNuevoTipo.getSelectedItem().toString();

            modeloCuentas.setValueAt(nuevoEstado, filaSeleccionada, 6);
            modeloCuentas.setValueAt(nuevoTipo, filaSeleccionada, 3);

            actualizarResumenCuentas();
            tablaCuentas.repaint();

            JOptionPane.showMessageDialog(this,
                    "Cuenta actualizada correctamente.\n\n"
                    + "Factura : " + nroFact + "\n"
                    + "Estado  : " + estActual + "  ->  " + nuevoEstado + "\n"
                    + "Tipo    : " + tipoActual + "  ->  " + nuevoTipo,
                    "Actualizado", JOptionPane.INFORMATION_MESSAGE);

        } else {
            List<OrdenCompra> lista = SistemaHotel.getInstancia().getHotel().getListaOrdenes();
            int nuevas = 0;
            if (lista != null) {
                for (OrdenCompra o : lista) {
                    if (!"Aprobado".equals(o.getEstado())) {
                        continue;
                    }
                    String idOrd = o.getIdOrden().toUpperCase();
                    boolean yaExiste = false;
                    for (int i = 0; i < modeloCuentas.getRowCount(); i++) {
                        if (idOrd.equals(modeloCuentas.getValueAt(i, 0).toString().toUpperCase())) {
                            yaExiste = true;
                            break;
                        }
                    }
                    if (!yaExiste) {
                        modeloCuentas.addRow(new Object[]{
                            o.getIdOrden(), o.getProveedor(),
                            String.format("S/ %,.2f", o.getPrecioTotal()),
                            "Contado",
                            SDF.format(o.getFechaEmision()),
                            SDF.format(o.getFechaEntrega()),
                            "Pendiente"
                        });
                        nuevas++;
                    }
                }
            }
            actualizarResumenCuentas();
            tablaCuentas.repaint();
            String msg = nuevas > 0
                    ? "Se agregaron " + nuevas + " orden(es) aprobada(s).\nResumen de deuda actualizado.\n\nTip: seleccione una fila para editar su estado."
                    : "No hay ordenes aprobadas nuevas.\nResumen actualizado.\n\nTip: seleccione una fila de la tabla y presione Actualizar para editar su estado.";
            JOptionPane.showMessageDialog(this, msg, "Actualizado", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void actualizarResumenCuentas() {
        if (lblPendientes == null) {
            return;
        }
        double total = 0;
        int pend = 0;
        for (int i = 0; i < modeloCuentas.getRowCount(); i++) {
            String est = modeloCuentas.getValueAt(i, 6).toString();
            if (!"Pagado".equals(est)) {
                pend++;
                try {
                    total += Double.parseDouble(
                            modeloCuentas.getValueAt(i, 2).toString().replace("S/ ", "").replace(",", ""));
                } catch (Exception ignored) {
                }
            }
        }
        lblPendientes.setText("  Facturas pendientes: " + pend);
        lblTotalDeuda.setText("  |  Deuda total (S/.): " + String.format("%,.2f", total) + "  ");
    }

    // ════════════════════════════════════════════════════════════════
    //  LÓGICA P4 – Stock
    // ════════════════════════════════════════════════════════════════
    private void actualizarTablaInventario() {
        if (modeloInventario == null) {
            return;
        }
        modeloInventario.setRowCount(0);
        int total = 0, crit = 0, suf = 0;
        List<Producto> inv = SistemaHotel.getInstancia().getHotel().getInventario();
        if (inv != null) {
            for (Producto pr : inv) {
                modeloInventario.addRow(new Object[]{
                    pr.getNombre(), pr.getTipo(), pr.getStock(), pr.getStockMinimo(), pr.getEstadoVisual()});
                total++;
                if (pr.isStockCritico()) {
                    crit++;
                } else {
                    suf++;
                }
            }
        }
        if (lblTotalProd != null) {
            lblTotalProd.setText(String.valueOf(total));
            lblCriticos.setText(String.valueOf(crit));
            lblSuficientes.setText(String.valueOf(suf));
        }
        pnlDetalle.setVisible(false);
    }

    private void buscarProducto() {
        String ter = txtBuscar.getText().trim().toLowerCase();
        if (ter.isEmpty()) {
            mostrarTodoInv();
            return;
        }
        for (int i = 0; i < modeloInventario.getRowCount(); i++) {
            if (modeloInventario.getValueAt(i, 0).toString().toLowerCase().contains(ter)) {
                tablaInventario.setRowSelectionInterval(i, i);
                tablaInventario.scrollRectToVisible(tablaInventario.getCellRect(i, 0, true));
                mostrarDetalleProducto(i);
                return;
            }
        }
        lblDetNombre.setText("\"" + txtBuscar.getText().trim() + "\"");
        lblDetNombre.setForeground(new Color(180, 30, 30));
        lblDetStock.setText("Producto no encontrado");
        lblDetStock.setForeground(new Color(180, 30, 30));
        lblDetTipo.setText("");
        lblDetEstado.setText("");
        pnlDetalle.setVisible(true);
        pnlDetalle.revalidate();
    }

    private void filtrarInventario() {
        String ter = txtBuscar.getText().trim().toLowerCase();
        DefaultTableModel tmp = noEdit(new String[]{"Nombre", "Tipo / Categoría", "Stock Actual", "Stock Mínimo", "Estado"});
        List<Producto> inv = SistemaHotel.getInstancia().getHotel().getInventario();
        if (inv != null) {
            for (Producto pr : inv) {
                if (ter.isEmpty() || pr.getNombre().toLowerCase().contains(ter) || pr.getTipo().toLowerCase().contains(ter)) {
                    tmp.addRow(new Object[]{pr.getNombre(), pr.getTipo(), pr.getStock(), pr.getStockMinimo(), pr.getEstadoVisual()});
                }
            }
        }
        tablaInventario.setModel(tmp);
        tablaInventario.setDefaultRenderer(Object.class, rendererInv());
        pnlDetalle.setVisible(false);
    }

    private void mostrarTodoInv() {
        txtBuscar.setText("");
        actualizarTablaInventario();
    }

    private void mostrarDetalleProducto(int fila) {
        String nombre = modeloInventario.getValueAt(fila, 0).toString();
        String tipo = modeloInventario.getValueAt(fila, 1).toString();
        String stock = modeloInventario.getValueAt(fila, 2).toString();
        String min = modeloInventario.getValueAt(fila, 3).toString();
        String estado = modeloInventario.getValueAt(fila, 4).toString();
        boolean crit = "Muy bajo".equals(estado);
        lblDetNombre.setText(nombre);
        lblDetNombre.setForeground(new Color(30, 60, 100));
        lblDetTipo.setText("Tipo: " + tipo);
        lblDetTipo.setForeground(Color.DARK_GRAY);
        lblDetStock.setText("Stock: " + stock + "  (mín: " + min + ")");
        lblDetStock.setForeground(crit ? new Color(180, 30, 30) : new Color(30, 130, 60));
        lblDetEstado.setText("Estado: " + estado);
        lblDetEstado.setForeground(crit ? new Color(180, 30, 30) : new Color(30, 130, 60));
        pnlDetalle.setVisible(true);
        pnlDetalle.revalidate();
    }

    // ════════════════════════════════════════════════════════════════
    //  LÓGICA P5 – Reporte Financiero
    // ════════════════════════════════════════════════════════════════
    private void generarReporte(boolean incluirOrdenes) {
        SimpleDateFormat sdfH = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        StringBuilder sb = new StringBuilder();
        sb.append("+------------------------------------------------------------------+\n");
        sb.append("        HOTEL TRUGARDEN  -  REPORTE FINANCIERO                  \n");
        sb.append("+------------------------------------------------------------------+\n");
        sb.append("  Generado: ").append(sdfH.format(new Date())).append("\n\n");

        sb.append("+-----------------------------------------------------------------+\n");
        sb.append("|  CUENTAS POR PAGAR                                              |\n");
        sb.append("+-----------------------------------------------------------------+\n");
        double totPend = 0, totPago = 0, totVenc = 0;
        int cP = 0, cA = 0, cV = 0;
        if (modeloCuentas.getRowCount() == 0) {
            sb.append("  (Sin cuentas registradas)\n\n");
        } else {
            sb.append(String.format("  %-14s  %-22s  %-13s  %-17s  %-10s  %s%n",
                    "Nro. Factura", "Proveedor", "Monto (S/.)", "Tipo Pago", "F. Vencim.", "Estado"));
            sb.append("  " + "-".repeat(95) + "\n");
            for (int i = 0; i < modeloCuentas.getRowCount(); i++) {
                String mon = modeloCuentas.getValueAt(i, 2).toString();
                String est = modeloCuentas.getValueAt(i, 6).toString();
                sb.append(String.format("  %-14s  %-22s  %-13s  %-17s  %-10s  %s%n",
                        modeloCuentas.getValueAt(i, 0), truncar(modeloCuentas.getValueAt(i, 1).toString(), 22),
                        mon, truncar(modeloCuentas.getValueAt(i, 3).toString(), 17),
                        modeloCuentas.getValueAt(i, 5), est));
                double m = 0;
                try {
                    m = Double.parseDouble(mon.replace("S/ ", "").replace(",", ""));
                } catch (Exception ig) {
                }
                switch (est) {
                    case "Pendiente":
                        totPend += m;
                        cP++;
                        break;
                    case "Pagado":
                        totPago += m;
                        cA++;
                        break;
                    case "Vencido":
                        totVenc += m;
                        cV++;
                        break;
                }
            }
            sb.append("  " + "-".repeat(95) + "\n");
        }
        sb.append(String.format("  Pendientes: %3d  S/ %,.2f%n", cP, totPend));
        sb.append(String.format("  Pagadas   : %3d  S/ %,.2f%n", cA, totPago));
        sb.append(String.format("  Vencidas  : %3d  S/ %,.2f%n", cV, totVenc));
        sb.append("  " + "-".repeat(40) + "\n");
        sb.append(String.format("  DEUDA ACTIVA TOTAL   S/ %,.2f%n%n", totPend + totVenc));

        sb.append("+-----------------------------------------------------------------+\n");
        sb.append("|  INVENTARIO / STOCK                                             |\n");
        sb.append("+-----------------------------------------------------------------+\n");
        List<Producto> inv = SistemaHotel.getInstancia().getHotel().getInventario();
        int totProd = 0, critProd = 0;
        if (inv == null || inv.isEmpty()) {
            sb.append("  (Sin productos)\n\n");
        } else {
            sb.append(String.format("  %-26s  %-16s  %8s  %8s  %s%n", "Producto", "Tipo", "Stock", "Mínimo", "Estado"));
            sb.append("  " + "-".repeat(74) + "\n");
            for (Producto pr : inv) {
                sb.append(String.format("  %-26s  %-16s  %8d  %8d  %s%n",
                        truncar(pr.getNombre(), 26), truncar(pr.getTipo(), 16), pr.getStock(), pr.getStockMinimo(), pr.getEstadoVisual()));
                totProd++;
                if (pr.isStockCritico()) {
                    critProd++;
                }
            }
            sb.append("  " + "-".repeat(74) + "\n");
            sb.append(String.format("  Total: %d  |  Crítico: %d  |  Suficiente: %d%n%n", totProd, critProd, totProd - critProd));
        }

        if (incluirOrdenes) {
            sb.append("+-----------------------------------------------------------------+\n");
            sb.append("|  ÓRDENES DE COMPRA                                              |\n");
            sb.append("+-----------------------------------------------------------------+\n");
            List<OrdenCompra> ords = SistemaHotel.getInstancia().getHotel().getListaOrdenes();
            int op = 0, oa = 0, or2 = 0;
            double totO = 0;
            if (ords == null || ords.isEmpty()) {
                sb.append("  (Sin órdenes)\n\n");
            } else {
                sb.append(String.format("  %-12s  %-22s  %6s  %-12s  %-22s  %s%n",
                        "ID Orden", "Producto", "Cant.", "F. Emisión", "Proveedor", "Estado"));
                sb.append("  " + "-".repeat(90) + "\n");
                for (OrdenCompra o : ords) {
                    sb.append(String.format("  %-12s  %-22s  %6d  %-12s  %-22s  %s%n",
                            o.getIdOrden(), truncar(o.getNombreProducto(), 22), o.getCantidad(),
                            SDF.format(o.getFechaEmision()), truncar(o.getProveedor(), 22), o.getEstado()));
                    totO += o.getPrecioTotal();
                    switch (o.getEstado()) {
                        case "Aprobado":
                            oa++;
                            break;
                        case "Rechazado":
                            or2++;
                            break;
                        default:
                            op++;
                    }
                }
                sb.append("  " + "-".repeat(90) + "\n");
                sb.append(String.format("  Pendientes:%d  Aprobadas:%d  Rechazadas:%d  Total: S/ %,.2f%n%n", op, oa, or2, totO));
            }
        }
        sb.append("+------------------------------------------------------------------+\n");
        sb.append("  Fin del reporte - Hotel TruGarden\n");
        sb.append("+------------------------------------------------------------------+\n");
        txtReporte.setText(sb.toString());
        txtReporte.setCaretPosition(0);
    }

    // ════════════════════════════════════════════════════════════════
    //  EXPORTAR EXCEL (.xlsx) sin librerias externas
    //  Genera un XML SpreadsheetML valido que Excel y LibreOffice abren
    // ════════════════════════════════════════════════════════════════
    private void exportarExcel() {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Guardar Reporte como Excel");
        fc.setSelectedFile(new java.io.File("Reporte_TruGarden_"
                + new java.text.SimpleDateFormat("yyyyMMdd_HHmm").format(new java.util.Date()) + ".xlsx"));
        fc.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "Libro Excel (*.xlsx)", "xlsx"));

        if (fc.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }

        java.io.File archivo = fc.getSelectedFile();
        if (!archivo.getName().toLowerCase().endsWith(".xlsx")) {
            archivo = new java.io.File(archivo.getAbsolutePath() + ".xlsx");
        }

        try {
            generarXlsx(archivo);

            int op = JOptionPane.showConfirmDialog(this,
                    "Reporte exportado correctamente.\n\nArchivo: " + archivo.getName()
                    + "\nUbicacion: " + archivo.getParent()
                    + "\n\nDesea abrir el archivo ahora?",
                    "Excel Exportado", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);

            if (op == JOptionPane.YES_OPTION) {
                try {
                    java.awt.Desktop.getDesktop().open(archivo);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this,
                            "No se pudo abrir automaticamente.\nUbicacion:\n" + archivo.getAbsolutePath(),
                            "Aviso", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al exportar Excel:\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Genera un .xlsx real (ZIP con XML interno) sin librerias externas.
     * Contiene 3 hojas: Cuentas por Pagar, Inventario, Ordenes de Compra.
     */
    private void generarXlsx(java.io.File destino) throws Exception {
        java.text.SimpleDateFormat sdfH = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm");

        // ── Hoja 1: Cuentas por Pagar ────────────────────────────────
        StringBuilder h1 = new StringBuilder();
        h1.append(xmlSheetHeader());
        // Titulo
        h1.append("<row r=\"1\"><c r=\"A1\" s=\"1\" t=\"inlineStr\"><is><t>HOTEL TRUGARDEN - Cuentas por Pagar</t></is></c></row>\n");
        h1.append("<row r=\"2\"><c r=\"A2\" t=\"inlineStr\"><is><t>Generado: " + sdfH.format(new java.util.Date()) + "</t></is></c></row>\n");
        // Cabecera
        String[] hCuentas = {"Nro. Factura", "Proveedor", "Monto (S/.)", "Tipo Pago", "F. Emision", "F. Vencim.", "Estado"};
        h1.append(xmlHeaderRow(4, hCuentas));
        // Datos
        double totPend = 0, totPago = 0, totVenc = 0;
        int cP = 0, cA = 0, cV = 0;
        for (int i = 0; i < modeloCuentas.getRowCount(); i++) {
            String est = modeloCuentas.getValueAt(i, 6).toString();
            String mon = modeloCuentas.getValueAt(i, 2).toString();
            h1.append(xmlDataRow(5 + i, new String[]{
                modeloCuentas.getValueAt(i, 0).toString(),
                modeloCuentas.getValueAt(i, 1).toString(),
                mon,
                modeloCuentas.getValueAt(i, 3).toString(),
                modeloCuentas.getValueAt(i, 4).toString(),
                modeloCuentas.getValueAt(i, 5).toString(),
                est
            }));
            double m = 0;
            try {
                m = Double.parseDouble(mon.replace("S/ ", "").replace(",", ""));
            } catch (Exception ig) {
            }
            switch (est) {
                case "Pendiente":
                    totPend += m;
                    cP++;
                    break;
                case "Pagado":
                    totPago += m;
                    cA++;
                    break;
                case "Vencido":
                    totVenc += m;
                    cV++;
                    break;
            }
        }
        int baseR = 5 + modeloCuentas.getRowCount() + 1;
        h1.append(xmlDataRow(baseR, new String[]{"Pendientes: " + cP, "", String.format("S/ %,.2f", totPend), "", "", "", ""}));
        h1.append(xmlDataRow(baseR + 1, new String[]{"Pagadas: " + cA, "", String.format("S/ %,.2f", totPago), "", "", "", ""}));
        h1.append(xmlDataRow(baseR + 2, new String[]{"Vencidas: " + cV, "", String.format("S/ %,.2f", totVenc), "", "", "", ""}));
        h1.append(xmlDataRow(baseR + 3, new String[]{"DEUDA ACTIVA TOTAL", "", String.format("S/ %,.2f", totPend + totVenc), "", "", "", ""}));
        h1.append(xmlSheetFooter());

        // ── Hoja 2: Inventario ────────────────────────────────────────
        StringBuilder h2 = new StringBuilder();
        h2.append(xmlSheetHeader());
        h2.append("<row r=\"1\"><c r=\"A1\" s=\"1\" t=\"inlineStr\"><is><t>HOTEL TRUGARDEN - Inventario / Stock</t></is></c></row>\n");
        String[] hInv = {"Nombre", "Tipo / Categoria", "Stock Actual", "Stock Minimo", "Estado"};
        h2.append(xmlHeaderRow(3, hInv));
        List<Producto> inv = SistemaHotel.getInstancia().getHotel().getInventario();
        int ri = 0;
        if (inv != null) {
            for (Producto pr : inv) {
                h2.append(xmlDataRow(4 + ri, new String[]{
                    pr.getNombre(), pr.getTipo(),
                    String.valueOf(pr.getStock()), String.valueOf(pr.getStockMinimo()),
                    pr.getEstadoVisual()
                }));
                ri++;
            }
        }
        h2.append(xmlSheetFooter());

        // ── Hoja 3: Ordenes de Compra ─────────────────────────────────
        StringBuilder h3 = new StringBuilder();
        h3.append(xmlSheetHeader());
        h3.append("<row r=\"1\"><c r=\"A1\" s=\"1\" t=\"inlineStr\"><is><t>HOTEL TRUGARDEN - Ordenes de Compra</t></is></c></row>\n");
        String[] hOrd = {"ID Orden", "Producto", "Cantidad", "Fecha Emision", "Proveedor", "Costo Total (S/.)", "Estado"};
        h3.append(xmlHeaderRow(3, hOrd));
        List<OrdenCompra> ords = SistemaHotel.getInstancia().getHotel().getListaOrdenes();
        int ro = 0;
        if (ords != null) {
            for (OrdenCompra o : ords) {
                h3.append(xmlDataRow(4 + ro, new String[]{
                    o.getIdOrden(), o.getNombreProducto(),
                    String.valueOf(o.getCantidad()),
                    SDF.format(o.getFechaEmision()),
                    o.getProveedor(),
                    String.format("S/ %,.2f", o.getPrecioTotal()),
                    o.getEstado()
                }));
                ro++;
            }
        }
        h3.append(xmlSheetFooter());

        // ── Armar el ZIP (.xlsx) ──────────────────────────────────────
        String sharedStrings = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n<sst xmlns=\"http://schemas.openxmlformats.org/spreadsheetml/2006/main\" count=\"0\" uniqueCount=\"0\"/>\n";

        String styles = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n"
                + "<styleSheet xmlns=\"http://schemas.openxmlformats.org/spreadsheetml/2006/main\">\n"
                + "<fonts count=\"2\"><font><sz val=\"11\"/></font><font><b/><sz val=\"12\"/></font></fonts>\n"
                + "<fills count=\"2\"><fill><patternFill patternType=\"none\"/></fill><fill><patternFill patternType=\"gray125\"/></fill></fills>\n"
                + "<borders count=\"1\"><border><left/><right/><top/><bottom/><diagonal/></border></borders>\n"
                + "<cellStyleXfs count=\"1\"><xf numFmtId=\"0\" fontId=\"0\" fillId=\"0\" borderId=\"0\"/></cellStyleXfs>\n"
                + "<cellXfs count=\"2\"><xf numFmtId=\"0\" fontId=\"0\" fillId=\"0\" borderId=\"0\" xfId=\"0\"/>"
                + "<xf numFmtId=\"0\" fontId=\"1\" fillId=\"0\" borderId=\"0\" xfId=\"0\"/></cellXfs>\n"
                + "</styleSheet>\n";

        String workbook = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n"
                + "<workbook xmlns=\"http://schemas.openxmlformats.org/spreadsheetml/2006/main\" "
                + "xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\">\n"
                + "<sheets>\n"
                + "<sheet name=\"Cuentas por Pagar\" sheetId=\"1\" r:id=\"rId1\"/>\n"
                + "<sheet name=\"Inventario\" sheetId=\"2\" r:id=\"rId2\"/>\n"
                + "<sheet name=\"Ordenes de Compra\" sheetId=\"3\" r:id=\"rId3\"/>\n"
                + "</sheets>\n</workbook>\n";

        String wbRels = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n"
                + "<Relationships xmlns=\"http://schemas.openxmlformats.org/package/2006/relationships\">\n"
                + "<Relationship Id=\"rId1\" Type=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships/worksheet\" Target=\"worksheets/sheet1.xml\"/>\n"
                + "<Relationship Id=\"rId2\" Type=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships/worksheet\" Target=\"worksheets/sheet2.xml\"/>\n"
                + "<Relationship Id=\"rId3\" Type=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships/worksheet\" Target=\"worksheets/sheet3.xml\"/>\n"
                + "<Relationship Id=\"rId4\" Type=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships/sharedStrings\" Target=\"sharedStrings.xml\"/>\n"
                + "<Relationship Id=\"rId5\" Type=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships/styles\" Target=\"styles.xml\"/>\n"
                + "</Relationships>\n";

        String pkgRels = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n"
                + "<Relationships xmlns=\"http://schemas.openxmlformats.org/package/2006/relationships\">\n"
                + "<Relationship Id=\"rId1\" Type=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships/officeDocument\" Target=\"xl/workbook.xml\"/>\n"
                + "</Relationships>\n";

        String contentTypes = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n"
                + "<Types xmlns=\"http://schemas.openxmlformats.org/package/2006/content-types\">\n"
                + "<Default Extension=\"rels\" ContentType=\"application/vnd.openxmlformats-package.relationships+xml\"/>\n"
                + "<Default Extension=\"xml\" ContentType=\"application/xml\"/>\n"
                + "<Override PartName=\"/xl/workbook.xml\" ContentType=\"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet.main+xml\"/>\n"
                + "<Override PartName=\"/xl/worksheets/sheet1.xml\" ContentType=\"application/vnd.openxmlformats-officedocument.spreadsheetml.worksheet+xml\"/>\n"
                + "<Override PartName=\"/xl/worksheets/sheet2.xml\" ContentType=\"application/vnd.openxmlformats-officedocument.spreadsheetml.worksheet+xml\"/>\n"
                + "<Override PartName=\"/xl/worksheets/sheet3.xml\" ContentType=\"application/vnd.openxmlformats-officedocument.spreadsheetml.worksheet+xml\"/>\n"
                + "<Override PartName=\"/xl/sharedStrings.xml\" ContentType=\"application/vnd.openxmlformats-officedocument.spreadsheetml.sharedStrings+xml\"/>\n"
                + "<Override PartName=\"/xl/styles.xml\" ContentType=\"application/vnd.openxmlformats-officedocument.spreadsheetml.styles+xml\"/>\n"
                + "</Types>\n";

        try (java.util.zip.ZipOutputStream zos = new java.util.zip.ZipOutputStream(
                new java.io.FileOutputStream(destino))) {
            zos.setLevel(java.util.zip.Deflater.BEST_COMPRESSION);
            zipEntry(zos, "[Content_Types].xml", contentTypes);
            zipEntry(zos, "_rels/.rels", pkgRels);
            zipEntry(zos, "xl/workbook.xml", workbook);
            zipEntry(zos, "xl/_rels/workbook.xml.rels", wbRels);
            zipEntry(zos, "xl/sharedStrings.xml", sharedStrings);
            zipEntry(zos, "xl/styles.xml", styles);
            zipEntry(zos, "xl/worksheets/sheet1.xml", h1.toString());
            zipEntry(zos, "xl/worksheets/sheet2.xml", h2.toString());
            zipEntry(zos, "xl/worksheets/sheet3.xml", h3.toString());
        }
    }

    private String xmlSheetHeader() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n"
                + "<worksheet xmlns=\"http://schemas.openxmlformats.org/spreadsheetml/2006/main\">\n"
                + "<sheetData>\n";
    }

    private String xmlSheetFooter() {
        return "</sheetData>\n</worksheet>\n";
    }

    private String xmlHeaderRow(int rowNum, String[] headers) {
        StringBuilder sb = new StringBuilder("<row r=\"" + rowNum + "\">");
        char col = 'A';
        for (String h : headers) {
            String xml = h.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
            sb.append("<c r=\"").append(col).append(rowNum).append("\" s=\"1\" t=\"inlineStr\"><is><t>").append(xml).append("</t></is></c>");
            col++;
        }
        sb.append("</row>\n");
        return sb.toString();
    }

    private String xmlDataRow(int rowNum, String[] vals) {
        StringBuilder sb = new StringBuilder("<row r=\"" + rowNum + "\">");
        char col = 'A';
        for (String v : vals) {
            String xml = (v == null ? "" : v).replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
            sb.append("<c r=\"").append(col).append(rowNum).append("\" t=\"inlineStr\"><is><t>").append(xml).append("</t></is></c>");
            col++;
        }
        sb.append("</row>\n");
        return sb.toString();
    }

    private void zipEntry(java.util.zip.ZipOutputStream zos, String nombre, String contenido) throws Exception {
        zos.putNextEntry(new java.util.zip.ZipEntry(nombre));
        zos.write(contenido.getBytes("UTF-8"));
        zos.closeEntry();
    }

    // ════════════════════════════════════════════════════════════════
    //  REGRESAR
    // ════════════════════════════════════════════════════════════════
    private void regresar() {
        int opt = JOptionPane.showConfirmDialog(this,
                "¿Está seguro que desea salir?", "Confirmar salida", JOptionPane.YES_NO_OPTION);
        if (opt == JOptionPane.YES_OPTION) {
            String rol = SistemaHotel.getInstancia().getHotel().getRolActual();
            if ("ADMIN".equals(rol)) {
                new Menu_principal().setVisible(true);
            } else {
                SistemaHotel.getInstancia().getHotel().setRolActual("");
                new Login().setVisible(true);
            }
            dispose();
        }
    }

    // ════════════════════════════════════════════════════════════════
    //  UTILIDADES
    // ════════════════════════════════════════════════════════════════
    private void titulo(JPanel p, String texto, int x, int y) {
        JLabel l = new JLabel(texto);
        l.setFont(new Font("Segoe UI", Font.BOLD, 22));
        l.setForeground(Color.BLACK);
        l.setBounds(x, y, 650, 36);
        p.add(l);
    }

    private JLabel lbl(String txt, int x, int y) {
        JLabel l = new JLabel(txt);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        l.setBounds(x, y, 230, 24);
        return l;
    }

    private JLabel etiq(String txt) {
        JLabel l = new JLabel(txt);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        return l;
    }

    private JTextField tf() {
        JTextField t = new JTextField();
        t.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        return t;
    }

    private JTextField tf2() {
        JTextField t = new JTextField(18);
        t.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        return t;
    }

    private JButton btn(String txt, Color bg) {
        JButton b = new JButton(txt);
        b.setBackground(bg);
        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    private JComboBox<String> combo(String... items) {
        JComboBox<String> c = new JComboBox<>(items);
        c.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        return c;
    }

    private JTable tabla(DefaultTableModel m) {
        JTable t = new JTable(m);
        t.setRowHeight(26);
        t.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        t.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        t.getTableHeader().setBackground(C_AZUL);
        t.getTableHeader().setForeground(Color.BLACK);
        t.setGridColor(new Color(200, 215, 230));
        t.setSelectionBackground(new Color(180, 210, 255));
        t.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        return t;
    }

    private DefaultTableModel noEdit(String[] cols) {
        return new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
    }

    private void row4(JPanel form, GridBagConstraints g, int row,
            String l1, JComponent c1, String l2, JComponent c2) {
        g.gridy = row;
        g.gridwidth = 1;
        g.gridx = 0;
        g.weightx = 0;
        form.add(etiq(l1), g);
        g.gridx = 1;
        g.weightx = 0.4;
        form.add(c1, g);
        g.gridx = 2;
        g.weightx = 0;
        form.add(etiq(l2), g);
        g.gridx = 3;
        g.weightx = 0.6;
        form.add(c2, g);
    }

    private JPanel tarjetaDash(String titulo, JLabel lblVal, Color borde, Color fondo) {
        JPanel p = new JPanel(new GridLayout(2, 1, 0, 4));
        p.setBackground(fondo);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borde, 1), new EmptyBorder(8, 14, 8, 14)));
        JLabel lt = new JLabel(titulo);
        lt.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        p.add(lt);
        p.add(lblVal);
        return p;
    }

    private JLabel chip(String txt, Color fondo) {
        JLabel l = new JLabel(txt);
        l.setFont(new Font("Segoe UI", Font.BOLD, 11));
        l.setOpaque(true);
        l.setBackground(fondo);
        l.setForeground(Color.BLACK);
        l.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(fondo.darker(), 1), new EmptyBorder(2, 6, 2, 6)));
        return l;
    }

    private DefaultTableCellRenderer rendererInv() {
        return new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable t, Object v, boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, v, sel, foc, row, col);
                if (!sel) {
                    boolean cr = "Muy bajo".equals(t.getValueAt(row, 4));
                    setBackground(cr ? C_CRIT : row % 2 == 0 ? C_FILA_P : Color.WHITE);
                }
                setForeground(Color.BLACK);
                if (col == 4) {
                    setHorizontalAlignment(CENTER);
                    setFont(getFont().deriveFont(Font.BOLD));
                    setForeground("Muy bajo".equals(v) ? new Color(180, 30, 30) : new Color(30, 130, 60));
                } else if (col == 2 || col == 3) {
                    setHorizontalAlignment(CENTER);
                }
                return this;
            }
        };
    }

    private void soloNum(KeyEvent e, JTextField tf) {
        char c = e.getKeyChar();
        if (!Character.isDigit(c) && c != '.') {
            e.consume();
            return;
        }
        if (c == '.' && tf.getText().contains(".")) {
            e.consume();
        }
    }

    private String truncar(String s, int max) {
        return s.length() <= max ? s : s.substring(0, max - 1) + "...";
    }

    // ════════════════════════════════════════════════════════════════
    //  MAIN
    // ════════════════════════════════════════════════════════════════
    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ignored) {
        }
        SwingUtilities.invokeLater(() -> new Modulo_de_compras().setVisible(true));
    }
}