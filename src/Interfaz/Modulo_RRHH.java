package Interfaz;

import controlador.Hotel;
import controlador.SistemaHotel;
import controlador.PersistenciaRRHH;
import Entidades.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.border.TitledBorder;
import javax.swing.border.LineBorder;

/**
 * Módulo unificado de Recursos Humanos.
 *
 * Agrupa en un solo JFrame, mediante pestañas (JTabbedPane), las 4 historias
 * de usuario:
 *   F-017  Control de asistencia (marcaje digital + tolerancia + bitácora)
 *   F-018  Historial laboral (historial de cargos)
 *   F-019  Solicitudes de vacaciones y permisos
 *   F-020  Evaluaciones de desempeño
 *
 * Acceso: rol "Gerente de RRHH" (directo desde el Login) y rol
 * "Gerente General" (botón "Módulo RRHH" en el Menú Principal).
 *
 * NOTA: esta pantalla fue construida a mano (layouts estándar de Swing) en
 * lugar del editor visual de NetBeans, por lo que no tiene archivo .form
 * asociado. Si se desea editar visualmente, puede reconstruirse dentro de
 * NetBeans a partir de este código.
 */
public class Modulo_RRHH extends javax.swing.JFrame {

    // =========================================================
    // PALETA DE COLORES (coherente con Dashboard_GerenteGeneral)
    // =========================================================
    private static final Color VERDE_HEADER   = new Color(33, 87, 50);
    private static final Color VERDE_TEXTO    = new Color(15, 110, 86);
    private static final Color ROJO_TEXTO     = new Color(153, 45, 45);
    private static final Color AMBAR_TEXTO    = new Color(133, 79, 11);
    private static final Color GRIS_FONDO     = new Color(245, 245, 245);
    private static final Color GRIS_BORDE     = new Color(222, 222, 222);
    private static final DateTimeFormatter FMT_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter FMT_HORA  = DateTimeFormatter.ofPattern("HH:mm");
    private static final DateTimeFormatter FMT_FECHA_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final Hotel hotel;
    private final PersistenciaRRHH rrhh;

    // Componentes que se refrescan entre pestañas
    private DefaultTableModel modeloAsistencia;
    private DefaultTableModel modeloBitacora;
    private DefaultTableModel modeloHistorialCargo;
    private DefaultTableModel modeloSolicitudes;
    private DefaultTableModel modeloEvaluaciones;

    private JComboBox<Empleado> comboEmpleadoHistorial;
    private JComboBox<Empleado> comboEmpleadoEvaluacion;
    private JTable tablaSolicitudes;
    private JLabel lblResultadoMarcaje;

    public Modulo_RRHH() {
        this.hotel = SistemaHotel.getInstancia().getHotel();
        this.rrhh = new PersistenciaRRHH();
        initComponents();
        setTitle("Módulo RRHH · TruGarden Hotel");
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setSize(1150, 720);
        setMinimumSize(new Dimension(950, 600));
        setLocationRelativeTo(null);
    }

    // =========================================================
    // CONSTRUCCIÓN DE LA INTERFAZ
    // =========================================================
    private void initComponents() {
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(construirCabecera(), BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tabs.addTab("Asistencia (F-017)", construirPanelAsistencia());
        tabs.addTab("Historial Laboral (F-018)", construirPanelHistorialLaboral());
        tabs.addTab("Permisos / Vacaciones (F-019)", construirPanelPermisos());
        tabs.addTab("Evaluación de Desempeño (F-020)", construirPanelEvaluaciones());
        tabs.addTab("Crear Horarios (F-021)", construirPanelCrearHorarios());

        getContentPane().add(tabs, BorderLayout.CENTER);
    }

    private JPanel construirCabecera() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(VERDE_HEADER);
        panel.setBorder(new EmptyBorder(14, 20, 14, 20));

        JLabel titulo = new JLabel("Módulo de Recursos Humanos");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titulo.setForeground(Color.WHITE);

        Empleado actual = hotel.getEmpleadoActual();
        String nombreSesion = actual != null ? (actual.getNombre() + " " + actual.getApellido()) : "Administrador";
        String rolSesion = hotel.getRolActual() == null || hotel.getRolActual().isEmpty() ? "Gerente General" : hotel.getRolActual();
        JLabel subtitulo = new JLabel("Sesión: " + nombreSesion + "  ·  Rol: " + rolSesion);
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitulo.setForeground(new Color(220, 235, 225));

        JPanel textos = new JPanel();
        textos.setOpaque(false);
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));
        textos.add(titulo);
        textos.add(subtitulo);

        JButton btnRegresar = new JButton("Cerrar módulo");
        btnRegresar.addActionListener(e -> {
            new Menu_principal().setVisible(true);
            dispose();
        });

        panel.add(textos, BorderLayout.WEST);
        panel.add(btnRegresar, BorderLayout.EAST);
        return panel;
    }

    // =========================================================
    // TAB 1 · F-017 CONTROL DE ASISTENCIA
    // =========================================================
    private JPanel construirPanelAsistencia() {
        JPanel raiz = new JPanel(new BorderLayout(10, 10));
        raiz.setBorder(new EmptyBorder(12, 12, 12, 12));
        raiz.setBackground(GRIS_FONDO);

        // --- Panel izquierdo: marcaje + configuración ---
        JPanel izquierda = new JPanel();
        izquierda.setLayout(new BoxLayout(izquierda, BoxLayout.Y_AXIS));
        izquierda.setOpaque(false);
        izquierda.setPreferredSize(new Dimension(340, 0));

        // -- Sub-panel: registrar marcaje --
        JPanel panelMarcaje = panelConTitulo("Registrar marcaje de asistencia");
        JComboBox<Empleado> comboEmpleado = new JComboBox<>(listaEmpleadosComoArray());
        aplicarRendererEmpleado(comboEmpleado);
        JComboBox<String> comboTipo = new JComboBox<>(new String[]{"ENTRADA", "SALIDA"});
        lblResultadoMarcaje = new JLabel(" ");
        lblResultadoMarcaje.setFont(new Font("Segoe UI", Font.BOLD, 13));

        GridBagConstraints gbc = gbcBase();
        agregarFila(panelMarcaje, gbc, 0, "Empleado:", comboEmpleado);
        agregarFila(panelMarcaje, gbc, 1, "Tipo de marca:", comboTipo);
        JButton btnMarcar = new JButton("Registrar marcaje");
        btnMarcar.setBackground(VERDE_HEADER);
        btnMarcar.setForeground(Color.WHITE);
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; gbc.insets = new Insets(10, 6, 4, 6);
        panelMarcaje.add(btnMarcar, gbc);
        gbc.gridy = 3;
        panelMarcaje.add(lblResultadoMarcaje, gbc);

        btnMarcar.addActionListener(e -> {
            Empleado emp = (Empleado) comboEmpleado.getSelectedItem();
            String tipo = (String) comboTipo.getSelectedItem();
            if (emp == null) {
                JOptionPane.showMessageDialog(this, "No hay empleados registrados en el sistema.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            procesarMarcaje(emp, tipo);
        });

        // -- Sub-panel: PLANTILLA / DOTACIÓN (reemplaza "Asignar turno") --
        JPanel contenidoDotacion = new JPanel();
        contenidoDotacion.setBackground(Color.WHITE);
        construirContenidoDotacion(contenidoDotacion);
        JScrollPane scrollDotacion = new JScrollPane(contenidoDotacion,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollDotacion.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(GRIS_BORDE),
                BorderFactory.createTitledBorder("Plantilla / Dotación de personal")));
        scrollDotacion.getVerticalScrollBar().setUnitIncrement(16);
        scrollDotacion.setPreferredSize(new Dimension(330, 260));
        scrollDotacion.setMaximumSize(new Dimension(Integer.MAX_VALUE, 260));

        izquierda.add(panelMarcaje);
        izquierda.add(Box.createVerticalStrut(10));
        izquierda.add(scrollDotacion);
        izquierda.add(Box.createVerticalGlue());

        // --- Panel derecho: reporte de marcajes + bitácora ---
        JPanel derecha = new JPanel(new GridLayout(2, 1, 0, 10));
        derecha.setOpaque(false);

        modeloAsistencia = new DefaultTableModel(
                new Object[]{"Fecha / Hora", "Empleado", "Tipo", "Estado", "Retraso (min)"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tablaAsistencia = new JTable(modeloAsistencia);
        JPanel panelReporte = panelConTitulo("Historial de asistencia en tiempo real");
        panelReporte.setLayout(new BorderLayout());
        panelReporte.add(new JScrollPane(tablaAsistencia), BorderLayout.CENTER);

        modeloBitacora = new DefaultTableModel(new Object[]{"Empleado", "Mensaje", "Fecha / Hora"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tablaBitacora = new JTable(modeloBitacora);
        JPanel panelBitacora = panelConTitulo("Bitácora de auditoría (marcajes rechazados)");
        panelBitacora.setLayout(new BorderLayout());
        panelBitacora.add(new JScrollPane(tablaBitacora), BorderLayout.CENTER);

        derecha.add(panelReporte);
        derecha.add(panelBitacora);

        refrescarAsistencia();
        refrescarBitacora();

        JScrollPane scrollIzq = new JScrollPane(izquierda);
        scrollIzq.setBorder(null);
        scrollIzq.getVerticalScrollBar().setUnitIncrement(14);

        raiz.add(scrollIzq, BorderLayout.WEST);
        raiz.add(derecha, BorderLayout.CENTER);
        return raiz;
    }

    /** Lógica de validación y registro del marcaje (F-017, filas 1 a 3). */
    private void procesarMarcaje(Empleado emp, String tipo) {
        String nombreCompleto = emp.getNombre() + " " + emp.getApellido();
        LocalDateTime ahora = LocalDateTime.now();

        if ("SALIDA".equals(tipo)) {
            // El marcaje de salida no exige validación de tolerancia, solo se registra.
            Asistencia a = new Asistencia(emp.getId(), nombreCompleto, "SALIDA", ahora, "Registrado", 0, null);
            rrhh.registrarAsistencia(a);
            lblResultadoMarcaje.setForeground(VERDE_TEXTO);
            lblResultadoMarcaje.setText("Salida registrada a las " + ahora.format(FMT_FECHA_HORA));
            refrescarAsistencia();
            return;
        }

        LocalTime horaEntradaTurno = rrhh.obtenerHoraEntradaTurno(emp.getId());
        if (horaEntradaTurno == null) {
            // Escenario 2: sin turno asignado -> rechazar y auditar
            rrhh.registrarBitacora(emp.getId(), nombreCompleto,
                    "Intento de marcaje de ENTRADA sin turno asignado para la fecha actual.");
            lblResultadoMarcaje.setForeground(ROJO_TEXTO);
            lblResultadoMarcaje.setText("Horario no habilitado (sin turno asignado)");
            JOptionPane.showMessageDialog(this, "Horario no habilitado: el empleado no tiene un turno asignado.",
                    "Marcaje rechazado", JOptionPane.ERROR_MESSAGE);
            refrescarBitacora();
            return;
        }

        LocalTime horaActual = ahora.toLocalTime();
        LocalTime ventanaInicio = horaEntradaTurno.minusHours(2); // no se puede marcar demasiado antes del turno
        if (horaActual.isBefore(ventanaInicio)) {
            // Escenario 2: fuera del horario establecido -> rechazar y auditar
            rrhh.registrarBitacora(emp.getId(), nombreCompleto,
                    "Intento de marcaje de ENTRADA fuera del horario establecido (" + horaActual.format(FMT_HORA) + ").");
            lblResultadoMarcaje.setForeground(ROJO_TEXTO);
            lblResultadoMarcaje.setText("Horario no habilitado (fuera de rango)");
            JOptionPane.showMessageDialog(this, "Horario no habilitado: intento de marcaje fuera del horario establecido.",
                    "Marcaje rechazado", JOptionPane.ERROR_MESSAGE);
            refrescarBitacora();
            return;
        }

        int toleranciaMin = rrhh.obtenerMinutosTolerancia();
        LocalTime limiteTolerancia = horaEntradaTurno.plusMinutes(toleranciaMin);

        String estado;
        int minutosRetraso = 0;
        if (!horaActual.isAfter(limiteTolerancia)) {
            // Escenario 1: dentro del rango de tolerancia -> Asistencia Puntual
            estado = "Asistencia Puntual";
        } else {
            // Escenario 3: tolerancia expirada -> Tardanza + cálculo del retraso
            estado = "Tardanza";
            minutosRetraso = (int) Duration.between(horaEntradaTurno, horaActual).toMinutes();
        }

        Asistencia a = new Asistencia(emp.getId(), nombreCompleto, "ENTRADA", ahora, estado, minutosRetraso, null);
        rrhh.registrarAsistencia(a);

        if ("Tardanza".equals(estado)) {
            lblResultadoMarcaje.setForeground(AMBAR_TEXTO);
            lblResultadoMarcaje.setText("Tardanza registrada: " + minutosRetraso + " min de retraso.");
        } else {
            lblResultadoMarcaje.setForeground(VERDE_TEXTO);
            lblResultadoMarcaje.setText("Asistencia Puntual registrada correctamente.");
        }
        refrescarAsistencia();
    }

    private void refrescarAsistencia() {
        modeloAsistencia.setRowCount(0);
        List<Asistencia> lista = rrhh.listarAsistencias();
        for (Asistencia a : lista) {
            modeloAsistencia.addRow(new Object[]{
                    a.getFechaHoraMarcaje().format(FMT_FECHA_HORA), a.getNombreEmpleado(),
                    a.getTipoMarca(), a.getEstado(),
                    a.getMinutosRetraso() > 0 ? String.valueOf(a.getMinutosRetraso()) : ""
            });
        }
    }

    private void refrescarBitacora() {
        modeloBitacora.setRowCount(0);
        for (Object[] fila : rrhh.listarBitacora()) {
            LocalDateTime fh = (LocalDateTime) fila[2];
            modeloBitacora.addRow(new Object[]{fila[0], fila[1], fh.format(FMT_FECHA_HORA)});
        }
    }

    // =========================================================
    // TAB 2 · F-018 HISTORIAL LABORAL
    // =========================================================
    private JPanel construirPanelHistorialLaboral() {
        JPanel raiz = new JPanel(new BorderLayout(10, 10));
        raiz.setBorder(new EmptyBorder(12, 12, 12, 12));
        raiz.setBackground(GRIS_FONDO);

        JPanel superior = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 6));
        superior.setOpaque(false);
        comboEmpleadoHistorial = new JComboBox<>(listaEmpleadosComoArray());
        aplicarRendererEmpleado(comboEmpleadoHistorial);
        JButton btnVerHistorial = new JButton("Ver Historial de Cargos");
        superior.add(new JLabel("Empleado:"));
        superior.add(comboEmpleadoHistorial);
        superior.add(btnVerHistorial);

        modeloHistorialCargo = new DefaultTableModel(
                new Object[]{"Cargo", "Departamento", "Sueldo (S/.)", "Desde", "Hasta"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tablaHistorial = new JTable(modeloHistorialCargo);
        JPanel panelTabla = panelConTitulo("Historial de cargos y modificaciones salariales");
        panelTabla.setLayout(new BorderLayout());
        panelTabla.add(new JScrollPane(tablaHistorial), BorderLayout.CENTER);

        btnVerHistorial.addActionListener(e -> refrescarHistorialCargo());

        // --- Formulario para que RR.HH. registre un cambio de cargo ---
        JPanel panelNuevo = panelConTitulo("Registrar cambio de cargo");
        JTextField txtCargo = new JTextField();
        JTextField txtDepartamento = new JTextField();
        JTextField txtSueldo = new JTextField();
        JTextField txtFechaInicio = new JTextField(LocalDate.now().format(FMT_FECHA));
        GridBagConstraints gbc = gbcBase();
        agregarFila(panelNuevo, gbc, 0, "Cargo:", txtCargo);
        agregarFila(panelNuevo, gbc, 1, "Departamento:", txtDepartamento);
        agregarFila(panelNuevo, gbc, 2, "Sueldo asignado (S/.):", txtSueldo);
        agregarFila(panelNuevo, gbc, 3, "Fecha inicio (dd/MM/yyyy):", txtFechaInicio);
        JButton btnAgregar = new JButton("Agregar al historial");
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2; gbc.insets = new Insets(10, 6, 4, 6);
        panelNuevo.add(btnAgregar, gbc);

        btnAgregar.addActionListener(e -> {
            Empleado emp = (Empleado) comboEmpleadoHistorial.getSelectedItem();
            if (emp == null) return;
            try {
                double sueldo = Double.parseDouble(txtSueldo.getText().trim().replace(",", "."));
                LocalDate fecha = LocalDate.parse(txtFechaInicio.getText().trim(), FMT_FECHA);
                if (txtCargo.getText().trim().isEmpty() || txtDepartamento.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Complete cargo y departamento.", "Datos incompletos", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                HistorialCargo h = new HistorialCargo(emp.getId(), txtCargo.getText().trim(),
                        txtDepartamento.getText().trim(), sueldo, fecha, null);
                rrhh.registrarHistorialCargo(h);
                txtCargo.setText(""); txtDepartamento.setText(""); txtSueldo.setText("");
                refrescarHistorialCargo();
                JOptionPane.showMessageDialog(this, "Registro agregado al historial laboral.", "Listo", JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(this, "El sueldo debe ser un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Fecha inválida. Use dd/MM/yyyy.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel centro = new JPanel(new BorderLayout(0, 10));
        centro.setOpaque(false);
        centro.add(superior, BorderLayout.NORTH);
        centro.add(panelTabla, BorderLayout.CENTER);

        JScrollPane scrollForm = new JScrollPane(panelNuevo);
        scrollForm.setPreferredSize(new Dimension(320, 0));
        scrollForm.setBorder(null);

        raiz.add(centro, BorderLayout.CENTER);
        raiz.add(scrollForm, BorderLayout.EAST);
        return raiz;
    }

    private void refrescarHistorialCargo() {
        modeloHistorialCargo.setRowCount(0);
        Empleado emp = (Empleado) comboEmpleadoHistorial.getSelectedItem();
        if (emp == null) return;
        List<HistorialCargo> lista = rrhh.listarHistorialPorEmpleado(emp.getId());
        for (HistorialCargo h : lista) {
            modeloHistorialCargo.addRow(new Object[]{
                    h.getCargo(), h.getDepartamento(), String.format("%.2f", h.getSueldoAsignado()),
                    h.getFechaInicio().format(FMT_FECHA),
                    h.getFechaFin() != null ? h.getFechaFin().format(FMT_FECHA) : "Actualidad"
            });
        }
        // Si no hay historial previo, la tabla queda vacía con las columnas visibles
        // para que el Jefe de RRHH pueda registrar la información (comportamiento esperado en F-018).
    }

    // =========================================================
    // TAB 3 · F-019 PERMISOS Y VACACIONES
    // =========================================================
    private JPanel construirPanelPermisos() {
        JPanel raiz = new JPanel(new BorderLayout(10, 10));
        raiz.setBorder(new EmptyBorder(12, 12, 12, 12));
        raiz.setBackground(GRIS_FONDO);

        // --- Panel izquierdo: nueva solicitud ---
        JPanel panelNueva = panelConTitulo("Nueva solicitud");
        JComboBox<Empleado> comboEmpleado = new JComboBox<>(listaEmpleadosComoArray());
        aplicarRendererEmpleado(comboEmpleado);
        JComboBox<String> comboTipo = new JComboBox<>(new String[]{"Vacaciones", "Permiso"});
        JTextField txtInicio = new JTextField(LocalDate.now().format(FMT_FECHA));
        JTextField txtFin = new JTextField(LocalDate.now().plusDays(1).format(FMT_FECHA));
        JTextArea txtMotivo = new JTextArea(3, 15);
        txtMotivo.setLineWrap(true);
        txtMotivo.setWrapStyleWord(true);

        GridBagConstraints gbc = gbcBase();
        agregarFila(panelNueva, gbc, 0, "Empleado:", comboEmpleado);
        agregarFila(panelNueva, gbc, 1, "Tipo:", comboTipo);
        agregarFila(panelNueva, gbc, 2, "Fecha inicio (dd/MM/yyyy):", txtInicio);
        agregarFila(panelNueva, gbc, 3, "Fecha fin (dd/MM/yyyy):", txtFin);
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 1; gbc.insets = new Insets(6, 6, 2, 6);
        panelNueva.add(new JLabel("Motivo:"), gbc);
        gbc.gridx = 1;
        panelNueva.add(new JScrollPane(txtMotivo), gbc);

        JButton btnGuardar = new JButton("Guardar solicitud");
        btnGuardar.setBackground(VERDE_HEADER);
        btnGuardar.setForeground(Color.WHITE);
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2; gbc.insets = new Insets(10, 6, 4, 6);
        panelNueva.add(btnGuardar, gbc);

        btnGuardar.addActionListener(e -> {
            Empleado emp = (Empleado) comboEmpleado.getSelectedItem();
            if (emp == null) return;
            try {
                LocalDate inicio = LocalDate.parse(txtInicio.getText().trim(), FMT_FECHA);
                LocalDate fin = LocalDate.parse(txtFin.getText().trim(), FMT_FECHA);
                if (fin.isBefore(inicio)) {
                    JOptionPane.showMessageDialog(this, "La fecha fin no puede ser anterior a la fecha inicio.", "Fechas inválidas", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                SolicitudPermiso s = new SolicitudPermiso(emp.getId(), emp.getNombre() + " " + emp.getApellido(),
                        (String) comboTipo.getSelectedItem(), inicio, fin, txtMotivo.getText().trim());
                rrhh.registrarSolicitud(s);
                txtMotivo.setText("");
                refrescarSolicitudes();
                JOptionPane.showMessageDialog(this, "Solicitud registrada con éxito. Estado: Pendiente.", "Listo", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Revise el formato de las fechas (dd/MM/yyyy).", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // --- Panel derecho: bandeja de solicitudes ---
        JPanel panelBandeja = panelConTitulo("Bandeja de solicitudes");
        panelBandeja.setLayout(new BorderLayout(0, 8));

        modeloSolicitudes = new DefaultTableModel(
                new Object[]{"ID", "Empleado", "Tipo", "Desde", "Hasta", "Días", "Estado", "Motivo"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaSolicitudes = new JTable(modeloSolicitudes);
        tablaSolicitudes.getColumnModel().getColumn(0).setMaxWidth(40);
        panelBandeja.add(new JScrollPane(tablaSolicitudes), BorderLayout.CENTER);

        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        JButton btnAprobar = new JButton("Aprobar");
        JButton btnRechazar = new JButton("Rechazar");
        JButton btnRefrescar = new JButton("Actualizar lista");
        panelAcciones.add(btnAprobar);
        panelAcciones.add(btnRechazar);
        panelAcciones.add(btnRefrescar);
        panelBandeja.add(panelAcciones, BorderLayout.SOUTH);

        btnAprobar.addActionListener(e -> resolverSolicitudSeleccionada("Aprobada"));
        btnRechazar.addActionListener(e -> resolverSolicitudSeleccionada("Rechazada"));
        btnRefrescar.addActionListener(e -> refrescarSolicitudes());

        refrescarSolicitudes();

        JScrollPane scrollForm = new JScrollPane(panelNueva);
        scrollForm.setPreferredSize(new Dimension(340, 0));
        scrollForm.setBorder(null);

        raiz.add(scrollForm, BorderLayout.WEST);
        raiz.add(panelBandeja, BorderLayout.CENTER);
        return raiz;
    }

    private void resolverSolicitudSeleccionada(String nuevoEstado) {
        int fila = tablaSolicitudes.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione una solicitud de la lista.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String estadoActual = (String) modeloSolicitudes.getValueAt(fila, 6);
        if (!"Pendiente".equals(estadoActual)) {
            JOptionPane.showMessageDialog(this, "Esta solicitud ya fue resuelta y no puede modificarse.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int idSolicitud = (int) modeloSolicitudes.getValueAt(fila, 0);
        String comentario = JOptionPane.showInputDialog(this, "Comentario (opcional):", "");
        rrhh.actualizarEstadoSolicitud(idSolicitud, nuevoEstado, comentario);
        refrescarSolicitudes();
    }

    private void refrescarSolicitudes() {
        modeloSolicitudes.setRowCount(0);
        for (SolicitudPermiso s : rrhh.listarSolicitudes()) {
            modeloSolicitudes.addRow(new Object[]{
                    s.getId(), s.getNombreEmpleado(), s.getTipo(),
                    s.getFechaInicio().format(FMT_FECHA), s.getFechaFin().format(FMT_FECHA),
                    s.getDiasSolicitados(), s.getEstado(), s.getMotivo()
            });
        }
    }

    // =========================================================
    // TAB 4 · F-020 EVALUACIÓN DE DESEMPEÑO
    // =========================================================
    private JPanel construirPanelEvaluaciones() {
        JPanel raiz = new JPanel(new BorderLayout(10, 10));
        raiz.setBorder(new EmptyBorder(12, 12, 12, 12));
        raiz.setBackground(GRIS_FONDO);

        JPanel panelForm = panelConTitulo("Registrar evaluación de desempeño");
        comboEmpleadoEvaluacion = new JComboBox<>(listaEmpleadosComoArray());
        aplicarRendererEmpleado(comboEmpleadoEvaluacion);
        JComboBox<String> comboTipoCalificacion = new JComboBox<>(new String[]{"Numérica", "Cualitativa"});
        JSpinner spinnerNumerica = new JSpinner(new SpinnerNumberModel(15, 0, 20, 1));
        JComboBox<String> comboCualitativa = new JComboBox<>(new String[]{"Excelente", "Bueno", "Regular", "Deficiente"});
        comboCualitativa.setVisible(false);
        JTextArea txtObservaciones = new JTextArea(5, 15);
        txtObservaciones.setLineWrap(true);
        txtObservaciones.setWrapStyleWord(true);

        JPanel panelCalificacion = new JPanel(new CardLayout());
        panelCalificacion.add(spinnerNumerica, "Numérica");
        panelCalificacion.add(comboCualitativa, "Cualitativa");

        comboTipoCalificacion.addActionListener(e -> {
            CardLayout cl = (CardLayout) panelCalificacion.getLayout();
            cl.show(panelCalificacion, (String) comboTipoCalificacion.getSelectedItem());
        });

        GridBagConstraints gbc = gbcBase();
        agregarFila(panelForm, gbc, 0, "Empleado:", comboEmpleadoEvaluacion);
        agregarFila(panelForm, gbc, 1, "Tipo de calificación:", comboTipoCalificacion);
        agregarFila(panelForm, gbc, 2, "Calificación:", panelCalificacion);
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 1; gbc.insets = new Insets(6, 6, 2, 6);
        panelForm.add(new JLabel("Observaciones:"), gbc);
        gbc.gridx = 1;
        panelForm.add(new JScrollPane(txtObservaciones), gbc);

        JButton btnGuardar = new JButton("Finalizar y guardar evaluación");
        btnGuardar.setBackground(VERDE_HEADER);
        btnGuardar.setForeground(Color.WHITE);
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2; gbc.insets = new Insets(10, 6, 4, 6);
        panelForm.add(btnGuardar, gbc);

        // --- Tabla con el historial de evaluaciones del empleado seleccionado ---
        modeloEvaluaciones = new DefaultTableModel(
                new Object[]{"Fecha", "Calificación", "Evaluador", "Observaciones"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tablaEvaluaciones = new JTable(modeloEvaluaciones);
        JPanel panelHistorial = panelConTitulo("Evaluaciones registradas para el empleado seleccionado");
        panelHistorial.setLayout(new BorderLayout());
        panelHistorial.add(new JScrollPane(tablaEvaluaciones), BorderLayout.CENTER);

        comboEmpleadoEvaluacion.addActionListener(e -> refrescarEvaluaciones());

        btnGuardar.addActionListener(e -> {
            Empleado emp = (Empleado) comboEmpleadoEvaluacion.getSelectedItem();
            if (emp == null) return;
            String tipo = (String) comboTipoCalificacion.getSelectedItem();
            String calificacion = "Numérica".equals(tipo)
                    ? String.valueOf(spinnerNumerica.getValue())
                    : (String) comboCualitativa.getSelectedItem();

            Empleado evaluadorActual = hotel.getEmpleadoActual();
            String nombreEvaluador = evaluadorActual != null
                    ? evaluadorActual.getNombre() + " " + evaluadorActual.getApellido() : "Gerente General";

            EvaluacionDesempeno ev = new EvaluacionDesempeno(emp.getId(), emp.getNombre() + " " + emp.getApellido(),
                    tipo, calificacion, txtObservaciones.getText().trim(), nombreEvaluador);
            rrhh.registrarEvaluacion(ev);
            txtObservaciones.setText("");
            refrescarEvaluaciones();
            JOptionPane.showMessageDialog(this, "Evaluación guardada y vinculada al perfil del empleado.", "Listo", JOptionPane.INFORMATION_MESSAGE);
        });

        refrescarEvaluaciones();

        JScrollPane scrollForm = new JScrollPane(panelForm);
        scrollForm.setPreferredSize(new Dimension(360, 0));
        scrollForm.setBorder(null);

        raiz.add(scrollForm, BorderLayout.WEST);
        raiz.add(panelHistorial, BorderLayout.CENTER);
        return raiz;
    }

    private void refrescarEvaluaciones() {
        modeloEvaluaciones.setRowCount(0);
        Empleado emp = (Empleado) comboEmpleadoEvaluacion.getSelectedItem();
        if (emp == null) return;
        for (EvaluacionDesempeno ev : rrhh.listarEvaluacionesPorEmpleado(emp.getId())) {
            modeloEvaluaciones.addRow(new Object[]{
                    ev.getFechaEvaluacion().format(FMT_FECHA), ev.getCalificacion(),
                    ev.getEvaluador(), ev.getObservaciones()
            });
        }
    }

    // =========================================================
    // F-021 · DOTACIÓN (tarjetas ocupados/máximo por rol)
    // =========================================================
    private void construirContenidoDotacion(JPanel contenedor) {
        contenedor.setLayout(new BoxLayout(contenedor, BoxLayout.Y_AXIS));
        try {
            List<Object[]> datos = rrhh.listarDotacion();
            if (datos.isEmpty()) {
                JLabel aviso = new JLabel("Ejecuta el script SQL de horarios para ver la dotación.");
                aviso.setForeground(Color.BLACK);
                contenedor.add(aviso);
            }
            for (Object[] d : datos) {
                String rol = (String) d[0];
                int maximo = (Integer) d[1];
                int ocupados = (Integer) d[2];
                JPanel tarjeta = crearTarjetaDotacion(rol, maximo, ocupados);
                tarjeta.setAlignmentX(Component.LEFT_ALIGNMENT);
                tarjeta.setMaximumSize(new Dimension(Integer.MAX_VALUE, 74));
                contenedor.add(tarjeta);
                contenedor.add(Box.createVerticalStrut(6));
            }
        } catch (Exception ex) {
            JLabel err = new JLabel("No se pudo cargar la dotación.");
            err.setForeground(Color.BLACK);
            contenedor.add(err);
        }
    }

    private JPanel crearTarjetaDotacion(String rol, int maximo, int ocupados) {
        boolean lleno = ocupados >= maximo;
        JPanel card = new JPanel(new BorderLayout(6, 2));
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(GRIS_BORDE, 1, true), new EmptyBorder(6, 10, 6, 10)));
        card.setBackground(lleno ? new Color(0xFD, 0xEC, 0xEA) : new Color(0xE8, 0xF5, 0xE9));

        JLabel lblRol = new JLabel(rol);
        lblRol.setFont(new Font("Segoe UI", Font.BOLD, 13));

        JLabel lblConteo = new JLabel(ocupados + " / " + maximo);
        lblConteo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblConteo.setForeground(lleno ? ROJO_TEXTO : VERDE_TEXTO);

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.add(lblRol, BorderLayout.WEST);
        top.add(lblConteo, BorderLayout.EAST);

        JProgressBar barra = new JProgressBar(0, Math.max(1, maximo));
        barra.setValue(ocupados);
        barra.setForeground(lleno ? ROJO_TEXTO : VERDE_TEXTO);

        JLabel estado = new JLabel(lleno ? "Cupo completo" : (maximo - ocupados) + " cupo(s) disponible(s)");
        estado.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        estado.setForeground(lleno ? ROJO_TEXTO : new Color(0x55, 0x55, 0x55));

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setOpaque(false);
        bottom.add(barra, BorderLayout.CENTER);
        bottom.add(estado, BorderLayout.SOUTH);

        card.add(top, BorderLayout.NORTH);
        card.add(bottom, BorderLayout.CENTER);
        return card;
    }

    // =========================================================
    // F-021 · CREAR HORARIOS (lista + asignación + tabla general)
    // =========================================================
    private static final String[] DIAS_SEMANA =
            {"Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"};

    private DefaultTableModel modeloHorarioGeneral;
    private JList<Empleado> listaEmpHorario;
    private JComboBox<String> comboDiaHorario;
    private JTextField txtEntradaHorario;
    private JTextField txtSalidaHorario;
    private JCheckBox chkDescansoHorario;

    private JPanel construirPanelCrearHorarios() {
        JPanel raiz = new JPanel(new BorderLayout(10, 10));
        raiz.setBorder(new EmptyBorder(12, 12, 12, 12));
        raiz.setBackground(GRIS_FONDO);

        // ---------- LATERAL IZQUIERDO ----------
        JPanel izquierda = new JPanel(new BorderLayout(0, 10));
        izquierda.setOpaque(false);
        izquierda.setPreferredSize(new Dimension(330, 0));

        // Lista de empleados
        listaEmpHorario = new JList<>(listaEmpleadosComoArray());
        listaEmpHorario.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaEmpHorario.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Empleado) {
                    Empleado emp = (Empleado) value;
                    setText(emp.getNombre() + " " + emp.getApellido() + "  ·  " + emp.getRol());
                }
                return this;
            }
        });
        JScrollPane spLista = new JScrollPane(listaEmpHorario);
        spLista.setBorder(new TitledBorder("Empleados"));
        spLista.setPreferredSize(new Dimension(320, 240));

        // Formulario de asignación
        JPanel form = panelConTitulo("Asignar horario");
        comboDiaHorario = new JComboBox<>(DIAS_SEMANA);
        txtEntradaHorario = new JTextField("08:00");
        txtSalidaHorario = new JTextField("17:00");
        chkDescansoHorario = new JCheckBox("Marcar como descanso (D)");
        chkDescansoHorario.setOpaque(false);
        chkDescansoHorario.addActionListener(e -> {
            boolean d = chkDescansoHorario.isSelected();
            txtEntradaHorario.setEnabled(!d);
            txtSalidaHorario.setEnabled(!d);
        });

        GridBagConstraints g = gbcBase();
        agregarFila(form, g, 0, "Día:", comboDiaHorario);
        agregarFila(form, g, 1, "Hora entrada (HH:mm):", txtEntradaHorario);
        agregarFila(form, g, 2, "Hora salida (HH:mm):", txtSalidaHorario);
        g.gridx = 0; g.gridy = 3; g.gridwidth = 2; g.insets = new Insets(6, 6, 4, 6);
        form.add(chkDescansoHorario, g);

        JButton btnGuardar = new JButton("Guardar horario del día");
        btnGuardar.setBackground(VERDE_HEADER);
        btnGuardar.setForeground(Color.WHITE);
        g.gridy = 4;
        form.add(btnGuardar, g);
        JButton btnQuitar = new JButton("Quitar día");
        g.gridy = 5;
        form.add(btnQuitar, g);

        btnGuardar.addActionListener(e -> guardarHorarioDia());
        btnQuitar.addActionListener(e -> quitarHorarioDia());

        izquierda.add(spLista, BorderLayout.NORTH);

        // Contenedor central: formulario de asignación + tolerancia
        JPanel centro = new JPanel();
        centro.setOpaque(false);
        centro.setLayout(new BoxLayout(centro, BoxLayout.Y_AXIS));
        form.setAlignmentX(Component.LEFT_ALIGNMENT);
        centro.add(form);
        centro.add(Box.createVerticalStrut(10));

        // -- Sub-panel: configuración de tolerancia (movido aquí) --
        JPanel panelTolerancia = panelConTitulo("Configuración de tolerancia");
        panelTolerancia.setAlignmentX(Component.LEFT_ALIGNMENT);
        JSpinner spinnerTolerancia = new JSpinner(new SpinnerNumberModel(rrhh.obtenerMinutosTolerancia(), 0, 120, 1));
        GridBagConstraints gbcT = gbcBase();
        agregarFila(panelTolerancia, gbcT, 0, "Minutos de tolerancia:", spinnerTolerancia);
        JButton btnGuardarTolerancia = new JButton("Guardar tolerancia");
        gbcT.gridx = 0; gbcT.gridy = 1; gbcT.gridwidth = 2; gbcT.insets = new Insets(10, 6, 4, 6);
        panelTolerancia.add(btnGuardarTolerancia, gbcT);
        btnGuardarTolerancia.addActionListener(e -> {
            rrhh.actualizarMinutosTolerancia((Integer) spinnerTolerancia.getValue());
            JOptionPane.showMessageDialog(this, "Tolerancia actualizada correctamente.", "Listo", JOptionPane.INFORMATION_MESSAGE);
        });
        centro.add(panelTolerancia);

        izquierda.add(centro, BorderLayout.CENTER);

        // ---------- TABLA GENERAL (DERECHA) ----------
        String[] cols = new String[DIAS_SEMANA.length + 1];
        cols[0] = "Colaborador";
        System.arraycopy(DIAS_SEMANA, 0, cols, 1, DIAS_SEMANA.length);
        modeloHorarioGeneral = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tabla = new JTable(modeloHorarioGeneral);
        tabla.setRowHeight(30);
        tabla.setFillsViewportHeight(true);
        tabla.getTableHeader().setReorderingAllowed(false);
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        // Renderer de cabecera: fondo gris claro y letras negras (legible en Windows LAF)
        final DefaultTableCellRenderer headerRender = new DefaultTableCellRenderer();
        headerRender.setHorizontalAlignment(SwingConstants.CENTER);
        headerRender.setOpaque(true);
        headerRender.setForeground(Color.BLACK);
        headerRender.setBackground(new Color(0xD5, 0xE8, 0xD9));
        headerRender.setBorder(BorderFactory.createLineBorder(GRIS_BORDE));
        for (int i = 0; i < tabla.getColumnCount(); i++) {
            tabla.getColumnModel().getColumn(i).setHeaderRenderer(headerRender);
        }

        DefaultTableCellRenderer render = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val, boolean sel,
                    boolean foc, int row, int col) {
                Component c = super.getTableCellRendererComponent(t, val, sel, foc, row, col);
                String s = val == null ? "" : val.toString();
                c.setForeground(Color.BLACK);
                if (col == 0) {
                    setHorizontalAlignment(LEFT);
                    c.setBackground(Color.WHITE);
                    c.setFont(c.getFont().deriveFont(Font.BOLD));
                } else {
                    setHorizontalAlignment(CENTER);
                    if (s.equals("D")) c.setBackground(new Color(0xFF, 0xF3, 0xE0));
                    else if (!s.isEmpty()) c.setBackground(new Color(0xE8, 0xF5, 0xE9));
                    else c.setBackground(Color.WHITE);
                    c.setFont(c.getFont().deriveFont(Font.PLAIN));
                }
                return c;
            }
        };
        for (int i = 0; i < tabla.getColumnCount(); i++)
            tabla.getColumnModel().getColumn(i).setCellRenderer(render);
        tabla.getColumnModel().getColumn(0).setPreferredWidth(180);

        JScrollPane spTabla = new JScrollPane(tabla);
        spTabla.setBorder(new TitledBorder("Horario general de la semana"));

        raiz.add(izquierda, BorderLayout.WEST);
        raiz.add(spTabla, BorderLayout.CENTER);

        cargarTablaHorarioGeneral();
        return raiz;
    }

    private void guardarHorarioDia() {
        Empleado emp = listaEmpHorario.getSelectedValue();
        if (emp == null) {
            JOptionPane.showMessageDialog(this, "Selecciona un empleado de la lista.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int dia = comboDiaHorario.getSelectedIndex() + 1;
        boolean descanso = chkDescansoHorario.isSelected();
        LocalTime entrada = null, salida = null;
        if (!descanso) {
            try {
                entrada = LocalTime.parse(txtEntradaHorario.getText().trim(), FMT_HORA);
                salida = LocalTime.parse(txtSalidaHorario.getText().trim(), FMT_HORA);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Formato de hora inválido. Use HH:mm (ej. 08:00).", "Atención", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!salida.isAfter(entrada)) {
                JOptionPane.showMessageDialog(this, "La hora de salida debe ser mayor que la de entrada.", "Atención", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }
        rrhh.guardarHorarioSemanal(emp.getId(), dia, entrada, salida, descanso);
        cargarTablaHorarioGeneral();
    }

    private void quitarHorarioDia() {
        Empleado emp = listaEmpHorario.getSelectedValue();
        if (emp == null) {
            JOptionPane.showMessageDialog(this, "Selecciona un empleado de la lista.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int dia = comboDiaHorario.getSelectedIndex() + 1;
        rrhh.eliminarHorarioDia(emp.getId(), dia);
        cargarTablaHorarioGeneral();
    }

    private void cargarTablaHorarioGeneral() {
        modeloHorarioGeneral.setRowCount(0);
        List<Empleado> empleados = hotel.getListaEmpleados();
        if (empleados == null) return;
        Map<Integer, String[]> matriz = rrhh.obtenerMatrizHorarios();
        for (Empleado e : empleados) {
            Object[] fila = new Object[DIAS_SEMANA.length + 1];
            fila[0] = e.getNombre() + " " + e.getApellido() + " (" + e.getRol() + ")";
            String[] dias = matriz.get(e.getId());
            for (int d = 1; d <= 7; d++) {
                fila[d] = (dias == null || dias[d] == null) ? "" : dias[d];
            }
            modeloHorarioGeneral.addRow(fila);
        }
    }

    // =========================================================
    // UTILIDADES DE CONSTRUCCIÓN DE FORMULARIOS
    // =========================================================
    private Empleado[] listaEmpleadosComoArray() {
        List<Empleado> lista = hotel.getListaEmpleados();
        if (lista == null) return new Empleado[0];
        return lista.toArray(new Empleado[0]);
    }

    /** Los combos de Empleado deben mostrar "Nombre Apellido (Rol)" en vez del toString() completo. */
    private void aplicarRendererEmpleado(JComboBox<Empleado> combo) {
        combo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Empleado) {
                    Empleado emp = (Empleado) value;
                    setText(emp.getNombre() + " " + emp.getApellido() + " (" + emp.getRol() + ")");
                }
                return this;
            }
        });
    }

    private JPanel panelConTitulo(String titulo) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(GRIS_BORDE),
                BorderFactory.createTitledBorder(titulo)));
        return panel;
    }

    private GridBagConstraints gbcBase() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 2, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        return gbc;
    }

    private void agregarFila(JPanel panel, GridBagConstraints gbc, int fila, String etiqueta, JComponent campo) {
        gbc.gridx = 0; gbc.gridy = fila; gbc.gridwidth = 1; gbc.weightx = 0;
        panel.add(new JLabel(etiqueta), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        panel.add(campo, gbc);
    }

    /**
     * Permite ejecutar esta ventana de forma independiente para pruebas rápidas.
     */
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> new Modulo_RRHH().setVisible(true));
    }
}
