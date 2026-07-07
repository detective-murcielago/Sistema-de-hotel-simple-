package Interfaz;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Diálogo reutilizable para registrar el CONSUMO de insumos.
 *
 * Lo usan tanto el módulo de Limpieza (área = "LIMPIEZA", referencia = nro de
 * habitación) como el módulo del Chef (área = "COCINA", referencia = id de
 * pedido). Cada línea agregada descuenta stock real del inventario a través de
 * SistemaHotel.consumirInsumo(...), que valida stock, persiste en MySQL y deja
 * el registro en el kardex (tabla consumo_insumo).
 *
 * Se construye por código (no por el editor de formularios), por lo que es
 * seguro invocarlo desde cualquier JFrame sin tocar su GEN-code.
 */
public class DialogoConsumoInsumos extends JDialog {

    private final String area;          // "LIMPIEZA" | "COCINA"
    private final String responsable;   // empleado que registra
    private final JTextField txtReferencia;
    private final JComboBox<String> cmbProducto;
    private final JSpinner spnCantidad;
    private final DefaultTableModel modeloConsumos;
    private final JLabel lblEstado;

    public DialogoConsumoInsumos(Frame padre, String area, String tipoFiltro,
                                 String etiquetaReferencia, String responsable) {
        super(padre, "Registrar consumo de insumos", true);
        this.area = area;
        this.tipoActualCache = tipoFiltro;
        this.responsable = (responsable == null || responsable.isBlank()) ? "Sistema" : responsable;

        setSize(560, 480);
        setLocationRelativeTo(padre);
        setLayout(new BorderLayout(10, 10));

        // ── Encabezado ────────────────────────────────────────────────
        JLabel titulo = new JLabel("Consumo de insumos — " + area);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titulo.setBorder(BorderFactory.createEmptyBorder(12, 12, 0, 12));
        add(titulo, BorderLayout.NORTH);

        // ── Formulario ────────────────────────────────────────────────
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(4, 4, 4, 4);
        g.anchor = GridBagConstraints.WEST;
        g.fill = GridBagConstraints.HORIZONTAL;

        g.gridx = 0; g.gridy = 0;
        form.add(new JLabel(etiquetaReferencia + ":"), g);
        g.gridx = 1;
        txtReferencia = new JTextField(14);
        form.add(txtReferencia, g);

        g.gridx = 0; g.gridy = 1;
        form.add(new JLabel("Producto:"), g);
        g.gridx = 1;
        cmbProducto = new JComboBox<>();
        cargarProductos(tipoFiltro);
        form.add(cmbProducto, g);

        g.gridx = 0; g.gridy = 2;
        form.add(new JLabel("Cantidad usada:"), g);
        g.gridx = 1;
        spnCantidad = new JSpinner(new SpinnerNumberModel(1, 1, 9999, 1));
        form.add(spnCantidad, g);

        g.gridx = 1; g.gridy = 3; g.anchor = GridBagConstraints.EAST; g.fill = GridBagConstraints.NONE;
        JButton btnAgregar = new JButton("Descontar del inventario");
        btnAgregar.setBackground(new Color(30, 70, 140));
        btnAgregar.setForeground(Color.WHITE);
        btnAgregar.setFocusPainted(false);
        btnAgregar.addActionListener(e -> registrarConsumo());
        form.add(btnAgregar, g);

        add(form, BorderLayout.CENTER);

        // ── Tabla de consumos de la sesión + estado ───────────────────
        JPanel sur = new JPanel(new BorderLayout(6, 6));
        sur.setBorder(BorderFactory.createEmptyBorder(0, 12, 12, 12));

        modeloConsumos = new DefaultTableModel(
                new String[]{"Producto", "Cantidad", etiquetaReferencia}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tabla = new JTable(modeloConsumos);
        JScrollPane sp = new JScrollPane(tabla);
        sp.setPreferredSize(new Dimension(520, 150));
        sur.add(sp, BorderLayout.CENTER);

        JPanel barra = new JPanel(new BorderLayout());
        lblEstado = new JLabel(" ");
        lblEstado.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        barra.add(lblEstado, BorderLayout.WEST);

        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dispose());
        barra.add(btnCerrar, BorderLayout.EAST);
        sur.add(barra, BorderLayout.SOUTH);

        add(sur, BorderLayout.SOUTH);
    }

    // Carga en el combo solo los productos del tipo indicado (ej. "Limpieza"
    // o "Alimentos"). Si tipoFiltro es null, muestra todos.
    private void cargarProductos(String tipoFiltro) {
        cmbProducto.removeAllItems();
        List<Entidades.Producto> inv =
                controlador.SistemaHotel.getInstancia().getHotel().getInventario();
        if (inv != null) {
            for (Entidades.Producto p : inv) {
                if (tipoFiltro == null || p.getTipo().equalsIgnoreCase(tipoFiltro)) {
                    cmbProducto.addItem(p.getNombre() + "  (Stock: " + p.getStock() + ")");
                }
            }
        }
        if (cmbProducto.getItemCount() == 0) {
            cmbProducto.addItem("(No hay productos de este tipo)");
            cmbProducto.setEnabled(false);
        }
    }

    private void registrarConsumo() {
        if (!cmbProducto.isEnabled()) {
            return;
        }
        String referencia = txtReferencia.getText().trim();
        if (referencia.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Indique la referencia (habitación o pedido) antes de registrar.",
                    "Falta referencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String seleccion = String.valueOf(cmbProducto.getSelectedItem());
        String nombre = seleccion.split("  \\(Stock:")[0].trim();
        int cantidad = (Integer) spnCantidad.getValue();

        String resultado = controlador.SistemaHotel.getInstancia()
                .consumirInsumo(nombre, cantidad, area, referencia, responsable);

        if (resultado.startsWith("OK")) {
            modeloConsumos.insertRow(0, new Object[]{nombre, cantidad, referencia});
            lblEstado.setText(resultado.substring(4)); // sin el prefijo "OK: "
            lblEstado.setForeground(new Color(20, 110, 40));
            cargarProductos(tipoActual());             // refresca stocks del combo
            spnCantidad.setValue(1);
        } else {
            lblEstado.setText(resultado);
            lblEstado.setForeground(new Color(170, 30, 30));
            JOptionPane.showMessageDialog(this, resultado, "No se pudo descontar",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    // Recuerda el filtro con el que se abrió el diálogo para refrescar el combo.
    private String tipoActualCache;
    private String tipoActual() { return tipoActualCache; }

    /** Fija el filtro de tipo usado al refrescar (se llama desde el constructor). */
    public void setTipoFiltro(String tipo) { this.tipoActualCache = tipo; }

    /** Prellena la referencia (nro habitación o id pedido) si el frame ya la tiene. */
    public void prellenarReferencia(String referencia) {
        if (referencia != null) {
            txtReferencia.setText(referencia);
        }
    }

    /**
     * Fábrica cómoda: crea, configura y muestra el diálogo en un solo paso.
     */
    public static void abrir(Frame padre, String area, String tipoFiltro,
                             String etiquetaReferencia, String responsable) {
        DialogoConsumoInsumos d =
                new DialogoConsumoInsumos(padre, area, tipoFiltro, etiquetaReferencia, responsable);
        d.setTipoFiltro(tipoFiltro);
        d.setVisible(true);
    }
}
