package Interfaz;

import controlador.DaoContabilidad;
import controlador.DaoContabilidad.ResultadoAsiento;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

/**
 * HU F-001 (Gerente General) - Libro Mayor / Asientos automaticos.
 *
 * Implementado como JPanel para incrustarse como una pestana mas dentro del
 * Dashboard del Gerente General (no como ventana flotante).
 */
public class LibroMayor extends javax.swing.JPanel {

    private static final Color VERDE_HEADER   = new Color(33, 87, 50);
    private static final Color VERDE_TEXTO    = new Color(15, 110, 86);
    private static final Color ROJO_TEXTO     = new Color(153, 45, 45);
    private static final Color AMBAR_TEXTO    = new Color(133, 79, 11);
    private static final Color GRIS_FONDO     = new Color(245, 245, 245);
    private static final Color GRIS_BORDE     = new Color(222, 222, 222);
    private static final Color NEGRO          = Color.BLACK;
    private static final Color BLANCO         = Color.WHITE;

    private final DecimalFormat soles = new DecimalFormat("S/ #,##0.00");
    private final SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    private final DaoContabilidad dao = new DaoContabilidad();

    private JTable tblMayor, tblSaldos, tblEstornables, tblPendientes, tblLog;
    private DefaultTableModel mMayor, mSaldos, mEstornables, mPendientes, mLog;

    private JTextField txtServicio, txtMonto;
    private JComboBox<String> cboMetodo;
    private JLabel lblEstado;
    private JComboBox<String> cboCuentaConfig;

    public LibroMayor() {
        setLayout(new BorderLayout());
        setBackground(GRIS_FONDO);
        initUI();
        recargarTodo();
    }

    private void initUI() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(VERDE_HEADER);
        header.setBorder(new EmptyBorder(12, 18, 12, 18));
        JLabel titulo = new JLabel("Libro Mayor  ·  Generacion automatica de asientos");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 17));
        titulo.setForeground(BLANCO);
        JLabel sub = new JLabel("HU F-001  ·  Ingresos de hospedaje y restaurante");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        sub.setForeground(new Color(210, 230, 215));
        JPanel titWrap = new JPanel(new GridLayout(2, 1));
        titWrap.setOpaque(false);
        titWrap.add(titulo); titWrap.add(sub);
        header.add(titWrap, BorderLayout.WEST);

        JButton btnRefrescar = crearBoton("Refrescar", VERDE_TEXTO);
        btnRefrescar.addActionListener(e -> recargarTodo());
        header.add(btnRefrescar, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabs.setForeground(NEGRO);
        tabs.addTab("Libro Mayor", panelLibroMayor());
        tabs.addTab("Registrar / Estornar", panelRegistroEstorno());
        tabs.addTab("Pendientes de Asiento", panelPendientes());
        tabs.addTab("Config. cuentas / Log", panelConfig());
        add(tabs, BorderLayout.CENTER);
    }

    private JPanel panelLibroMayor() {
        JPanel p = new JPanel(new BorderLayout(10, 10));
        p.setBackground(GRIS_FONDO);
        p.setBorder(new EmptyBorder(12, 12, 12, 12));

        mMayor = modeloNoEditable(new String[]{
                "Asiento", "Fecha", "Tipo", "Estado", "Cuenta", "Nombre cuenta",
                "Debe", "Haber", "Glosa"});
        tblMayor = crearTabla(mMayor);
        pintarEstadoColumna(tblMayor, 3);
        JScrollPane spMayor = new JScrollPane(tblMayor);
        spMayor.setBorder(tituloBorde("Movimientos del Libro Mayor"));

        mSaldos = modeloNoEditable(new String[]{"Cuenta", "Nombre", "Debe", "Haber", "Saldo"});
        tblSaldos = crearTabla(mSaldos);
        JScrollPane spSaldos = new JScrollPane(tblSaldos);
        spSaldos.setBorder(tituloBorde("Saldos por cuenta (asientos vigentes)"));
        spSaldos.setPreferredSize(new Dimension(1100, 170));

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, spMayor, spSaldos);
        split.setResizeWeight(0.7);
        split.setBorder(null);
        p.add(split, BorderLayout.CENTER);
        return p;
    }

    private JPanel panelRegistroEstorno() {
        JPanel p = new JPanel(new BorderLayout(10, 10));
        p.setBackground(GRIS_FONDO);
        p.setBorder(new EmptyBorder(12, 12, 12, 12));

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(BLANCO);
        form.setBorder(tituloBorde("Escenario 1 · Generar asiento a partir de un pago"));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6, 8, 6, 8);
        g.anchor = GridBagConstraints.WEST;

        g.gridx = 0; g.gridy = 0; form.add(etiqueta("Servicio:"), g);
        txtServicio = new JTextField(16); txtServicio.setForeground(NEGRO);
        g.gridx = 1; form.add(txtServicio, g);

        g.gridx = 2; form.add(etiqueta("Metodo de pago:"), g);
        cboMetodo = new JComboBox<>(new String[]{"Efectivo", "Tarjeta", "QR", "Transferencia"});
        cboMetodo.setForeground(NEGRO);
        g.gridx = 3; form.add(cboMetodo, g);

        g.gridx = 0; g.gridy = 1; form.add(etiqueta("Monto (S/):"), g);
        txtMonto = new JTextField(10); txtMonto.setForeground(NEGRO);
        g.gridx = 1; form.add(txtMonto, g);

        JButton btnGenerar = crearBoton("Generar asiento", VERDE_TEXTO);
        btnGenerar.addActionListener(e -> onGenerarAsiento());
        g.gridx = 3; form.add(btnGenerar, g);

        g.gridx = 0; g.gridy = 2; g.gridwidth = 4;
        lblEstado = new JLabel(" ");
        lblEstado.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        form.add(lblEstado, g);

        JLabel ayuda = new JLabel("<html><i>Prueba con <b>Hospedaje</b> o <b>Restaurante</b> "
                + "(mapeados) y con <b>Lavanderia</b> (no mapeado, quedara pendiente).</i></html>");
        ayuda.setForeground(NEGRO);
        g.gridy = 3; form.add(ayuda, g);
        p.add(form, BorderLayout.NORTH);

        mEstornables = modeloNoEditable(new String[]{"Asiento", "Fecha", "Glosa", "Importe"});
        tblEstornables = crearTabla(mEstornables);
        JScrollPane sp = new JScrollPane(tblEstornables);
        sp.setBorder(tituloBorde("Escenario 2 · Asientos vigentes (selecciona uno para estornar)"));

        JButton btnEstornar = crearBoton("Estornar seleccionado", ROJO_TEXTO);
        btnEstornar.addActionListener(e -> onEstornar());
        JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        south.setBackground(GRIS_FONDO);
        south.add(btnEstornar);

        JPanel centro = new JPanel(new BorderLayout());
        centro.setBackground(GRIS_FONDO);
        centro.add(sp, BorderLayout.CENTER);
        centro.add(south, BorderLayout.SOUTH);
        p.add(centro, BorderLayout.CENTER);
        return p;
    }

    private JPanel panelPendientes() {
        JPanel p = new JPanel(new BorderLayout(10, 10));
        p.setBackground(GRIS_FONDO);
        p.setBorder(new EmptyBorder(12, 12, 12, 12));

        JLabel info = new JLabel("<html>Pagos cuyo servicio <b>no tiene cuenta contable mapeada</b>. "
                + "No se genero asiento para no duplicar ni corromper el mayor. "
                + "Mapea la cuenta en la pestana de configuracion y luego <b>regulariza</b> aqui.</html>");
        info.setBorder(new EmptyBorder(0, 4, 8, 4));
        info.setForeground(NEGRO);
        p.add(info, BorderLayout.NORTH);

        mPendientes = modeloNoEditable(new String[]{
                "Asiento", "Fecha", "Glosa", "IdPago", "Servicio", "Metodo", "Monto"});
        tblPendientes = crearTabla(mPendientes);
        JScrollPane sp = new JScrollPane(tblPendientes);
        sp.setBorder(tituloBorde("Pendientes de asiento"));
        p.add(sp, BorderLayout.CENTER);

        JButton btnResolver = crearBoton("Regularizar seleccionado", VERDE_TEXTO);
        btnResolver.addActionListener(e -> onResolverPendiente());
        JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        south.setBackground(GRIS_FONDO);
        south.add(btnResolver);
        p.add(south, BorderLayout.SOUTH);
        return p;
    }

    private JPanel panelConfig() {
        JPanel p = new JPanel(new BorderLayout(10, 10));
        p.setBackground(GRIS_FONDO);
        p.setBorder(new EmptyBorder(12, 12, 12, 12));

        JPanel form = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        form.setBackground(BLANCO);
        form.setBorder(tituloBorde("Mapear servicio -> cuenta de ingreso"));

        JTextField txtServ = new JTextField(14); txtServ.setForeground(NEGRO);
        cboCuentaConfig = new JComboBox<>();
        cboCuentaConfig.setForeground(NEGRO);
        recargarCombosCuentas();

        JButton btnMapear = crearBoton("Guardar mapeo", VERDE_TEXTO);
        btnMapear.addActionListener(e -> {
            String serv = txtServ.getText().trim();
            if (serv.isEmpty()) { alerta("Ingresa el nombre del servicio."); return; }
            String sel = (String) cboCuentaConfig.getSelectedItem();
            if (sel == null) { alerta("No hay cuentas de ingreso."); return; }
            String cod = sel.split(" - ")[0];
            if (dao.guardarMapeo(serv, cod)) {
                info("Mapeo guardado: " + serv + " -> " + cod);
                txtServ.setText("");
                recargarTodo();
            } else alerta("No se pudo guardar el mapeo.");
        });

        form.add(etiqueta("Servicio:")); form.add(txtServ);
        form.add(etiqueta("Cuenta ingreso:")); form.add(cboCuentaConfig);
        form.add(btnMapear);
        p.add(form, BorderLayout.NORTH);

        mLog = modeloNoEditable(new String[]{"Fecha", "Nivel", "Servicio", "Mensaje", "Resuelto"});
        tblLog = crearTabla(mLog);
        pintarEstadoColumna(tblLog, 1);
        JScrollPane sp = new JScrollPane(tblLog);
        sp.setBorder(tituloBorde("Log contable / alertas para Finanzas"));
        p.add(sp, BorderLayout.CENTER);
        return p;
    }

    private void onGenerarAsiento() {
        String serv = txtServicio.getText().trim();
        String metodo = (String) cboMetodo.getSelectedItem();
        if (serv.isEmpty()) { setEstado("Ingresa el servicio.", ROJO_TEXTO); return; }
        double monto;
        try {
            monto = Double.parseDouble(txtMonto.getText().trim());
            if (monto <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            setEstado("Monto invalido.", ROJO_TEXTO); return;
        }
        ResultadoAsiento r = dao.generarAsientoPorPago(0, serv, metodo, monto, LocalDateTime.now());
        if (r.exito)          setEstado(r.mensaje, VERDE_TEXTO);
        else if (r.pendiente) setEstado(r.mensaje, AMBAR_TEXTO);
        else                  setEstado(r.mensaje, ROJO_TEXTO);
        recargarTodo();
    }

    private void onEstornar() {
        int fila = tblEstornables.getSelectedRow();
        if (fila < 0) { alerta("Selecciona un asiento para estornar."); return; }
        int idAsiento = (int) mEstornables.getValueAt(fila, 0);
        int ok = JOptionPane.showConfirmDialog(this,
                "Se generara un asiento de REVERSION que neutraliza el asiento #" + idAsiento +
                ".\n¿Confirmar el estorno?", "Confirmar estorno",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (ok != JOptionPane.YES_OPTION) return;
        ResultadoAsiento r = dao.generarReversion(idAsiento);
        if (r.exito) info(r.mensaje); else alerta(r.mensaje);
        recargarTodo();
    }

    private void onResolverPendiente() {
        int fila = tblPendientes.getSelectedRow();
        if (fila < 0) { alerta("Selecciona un pendiente para regularizar."); return; }
        int idAsiento = (int) mPendientes.getValueAt(fila, 0);
        String servicio = String.valueOf(mPendientes.getValueAt(fila, 4));
        String metodo   = String.valueOf(mPendientes.getValueAt(fila, 5));
        Object montoObj = mPendientes.getValueAt(fila, 6);
        double monto = (montoObj instanceof Number) ? ((Number) montoObj).doubleValue() : 0;
        if (monto <= 0) {
            String s = JOptionPane.showInputDialog(this, "Monto a regularizar (S/):");
            try { monto = Double.parseDouble(s); } catch (Exception e) { alerta("Monto invalido."); return; }
        }
        ResultadoAsiento r = dao.resolverPendiente(idAsiento, servicio, metodo, monto);
        if (r.exito) info(r.mensaje);
        else if (r.pendiente) alerta(r.mensaje + "\nMapea la cuenta primero en la pestana de configuracion.");
        else alerta(r.mensaje);
        recargarTodo();
    }

    /** Recarga todas las tablas. Publico para que el Dashboard lo invoque. */
    public void recargarTodo() {
        cargarMayor();
        cargarSaldos();
        cargarEstornables();
        cargarPendientes();
        cargarLog();
        recargarCombosCuentas();
    }

    private void recargarCombosCuentas() {
        if (cboCuentaConfig == null) return;
        cboCuentaConfig.removeAllItems();
        for (String[] c : dao.listarCuentasIngreso())
            cboCuentaConfig.addItem(c[0] + " - " + c[1]);
    }

    private void cargarMayor() {
        mMayor.setRowCount(0);
        for (Object[] f : dao.listarLibroMayor())
            mMayor.addRow(new Object[]{f[0], fmtFecha(f[1]), f[2], f[3], f[4], f[5],
                    money(f[6]), money(f[7]), f[8]});
    }

    private void cargarSaldos() {
        mSaldos.setRowCount(0);
        for (Object[] f : dao.saldosPorCuenta())
            mSaldos.addRow(new Object[]{f[0], f[1], money(f[2]), money(f[3]), money(f[4])});
    }

    private void cargarEstornables() {
        mEstornables.setRowCount(0);
        for (Object[] f : dao.listarAsientosEstornables())
            mEstornables.addRow(new Object[]{f[0], fmtFecha(f[1]), f[2], money(f[3])});
    }

    private void cargarPendientes() {
        mPendientes.setRowCount(0);
        for (Object[] f : dao.listarPendientes())
            mPendientes.addRow(new Object[]{f[0], fmtFecha(f[1]), f[2], f[3], f[4], f[5], f[6]});
    }

    private void cargarLog() {
        mLog.setRowCount(0);
        for (Object[] f : dao.listarLog())
            mLog.addRow(new Object[]{fmtFecha(f[0]), f[1], f[2], f[3], f[4]});
    }

    // ---- Helpers de UI (texto en NEGRO) ----
    private DefaultTableModel modeloNoEditable(String[] cols) {
        return new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
    }

    private JTable crearTabla(DefaultTableModel m) {
        JTable t = new JTable(m);
        t.setRowHeight(24);
        t.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        t.setForeground(NEGRO);
        t.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        t.getTableHeader().setBackground(VERDE_HEADER);
        t.getTableHeader().setForeground(BLANCO);
        t.setGridColor(GRIS_BORDE);
        t.setSelectionBackground(new Color(213, 232, 212));
        t.setSelectionForeground(NEGRO);
        DefaultTableCellRenderer black = new DefaultTableCellRenderer();
        black.setForeground(NEGRO);
        for (int i = 0; i < t.getColumnCount(); i++)
            t.getColumnModel().getColumn(i).setCellRenderer(black);
        return t;
    }

    private JLabel etiqueta(String txt) {
        JLabel l = new JLabel(txt);
        l.setForeground(NEGRO);
        return l;
    }

    private javax.swing.border.Border tituloBorde(String txt) {
        javax.swing.border.TitledBorder tb = BorderFactory.createTitledBorder(txt);
        tb.setTitleColor(NEGRO);
        return tb;
    }

    private void pintarEstadoColumna(JTable t, int col) {
        t.getColumnModel().getColumn(col).setCellRenderer(new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(
                    JTable tb, Object v, boolean sel, boolean foc, int r, int c) {
                Component comp = super.getTableCellRendererComponent(tb, v, sel, foc, r, c);
                String s = v == null ? "" : v.toString();
                if (s.contains("REGISTRADO") || s.equals("INFO"))            comp.setForeground(VERDE_TEXTO);
                else if (s.contains("PENDIENTE") || s.equals("ADVERTENCIA")) comp.setForeground(AMBAR_TEXTO);
                else if (s.contains("ANULADO") || s.equals("ERROR"))         comp.setForeground(ROJO_TEXTO);
                else comp.setForeground(NEGRO);
                setFont(new Font("Segoe UI", Font.BOLD, 12));
                return comp;
            }
        });
    }

    private JButton crearBoton(String texto, Color color) {
        JButton b = new JButton(texto);
        b.setFocusPainted(false);
        b.setBackground(color);
        b.setForeground(BLANCO);
        b.setFont(new Font("Segoe UI", Font.BOLD, 12));
        b.setBorder(new EmptyBorder(8, 16, 8, 16));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return b;
    }

    private void setEstado(String msg, Color c) {
        lblEstado.setText(msg.replace("\n", "  |  "));
        lblEstado.setForeground(c);
    }

    private String money(Object o) {
        double d = (o instanceof Number) ? ((Number) o).doubleValue() : 0;
        return soles.format(d);
    }

    private String fmtFecha(Object o) {
        if (o instanceof java.util.Date) return fmt.format((java.util.Date) o);
        return String.valueOf(o);
    }

    private void alerta(String m) {
        JOptionPane.showMessageDialog(this, m, "Aviso", JOptionPane.WARNING_MESSAGE);
    }
    private void info(String m) {
        JOptionPane.showMessageDialog(this, m, "Contabilidad", JOptionPane.INFORMATION_MESSAGE);
    }
}
