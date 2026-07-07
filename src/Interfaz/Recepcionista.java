package Interfaz;

import Entidades.Huesped;
import controlador.Hotel;
import controlador.SistemaHotel;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author tonyl
 */
public class Recepcionista extends javax.swing.JFrame {

    private Hotel hotel = SistemaHotel.getInstancia().getHotel();
    private Entidades.FichaHospedaje fichaActual = null;

    // Campo de motivo de diferencia en el arqueo de caja (corrección 5).
    // Se crea por código y se inserta en el panel de arqueo en iniciarArqueoCaja().
    private javax.swing.JTextField jTextFMotivoDiferencia = new javax.swing.JTextField();

    // --- Paneles de las historias de usuario (F-004, F-005, F-010, F-011) ---
    private javax.swing.JPanel panelPreferencias;   // F-004
    private javax.swing.JPanel panelHistEstadias;   // F-005
    private javax.swing.JPanel panelPagoServicios;  // F-010
    private javax.swing.JPanel panelHistCompleto;   // F-011

    public Recepcionista() {
        initComponents();
        initPanelesHU();          // Construye e integra los paneles/botones nuevos
        verificarSalidasAutomaticas();
        dibujarCuadriculaHabitaciones("Todas las opciones");
        setLocationRelativeTo(null);
        this.setResizable(false);

        java.util.Date hoy = new java.util.Date();
        jDateChooserFechaIngreso.setMinSelectableDate(hoy); // no antes de hoy
        jDateChooserFechaIngreso.setMaxSelectableDate(hoy); // no después de hoy
        jDateChooserFechaIngreso.setDate(hoy);              // preselecciona hoy
    }

    private void initPanelesHU() {
        // 1) Construir los paneles (cards)
        panelPreferencias = PanelesHU.registrarPreferenciasPanel(); // F-004
        panelHistEstadias = PanelesHU.historialEstadiasPanel();     // F-005
        panelPagoServicios = PanelesHU.pagoServiciosPanel();         // F-010
        panelHistCompleto = PanelesHU.historialCompletoPanel();     // F-011

        // 2) Registrarlos en el contenedor CardLayout
        jPanelContenedor.add(panelPreferencias, "cardF004");
        jPanelContenedor.add(panelHistEstadias, "cardF005");
        jPanelContenedor.add(panelPagoServicios, "cardF010");
        jPanelContenedor.add(panelHistCompleto, "cardF011");

        // 3) Crear los botones del menú con la estética existente
        java.awt.Color azul = new java.awt.Color(102, 153, 255);

        javax.swing.JButton btnF004 = crearBotonMenu("Preferencias");
        javax.swing.JButton btnF005 = crearBotonMenu("Historial Estadías");
        javax.swing.JButton btnF011 = crearBotonMenu("Historial + Pref.");

        btnF004.addActionListener(evt -> mostrarCard(panelPreferencias));
        btnF005.addActionListener(evt -> mostrarCard(panelHistEstadias));
        btnF011.addActionListener(evt -> mostrarCard(panelHistCompleto));
        // Nota: el pago de servicios (F-010) se abre desde el botón "Pagos" del
        // apartado CAJA. No se duplica aquí para evitar dos accesos al mismo panel.

        // 4) Insertar los botones en el menú lateral.
        //    El jPanelMenu original usa GroupLayout (autogenerado). En lugar de
        //    modificar ese layout (frágil), lo envolvemos: sustituimos el
        //    jPanelMenu en el JFrame por un contenedor vertical que arriba lleva
        //    el menú original intacto y abajo nuestra sección nueva de botones.
        java.awt.Container padre = jPanelMenu.getParent();
        // Capturamos el ancho preferido original del menú para conservarlo.
        int anchoMenu = jPanelMenu.getPreferredSize().width;
        if (anchoMenu <= 0) {
            anchoMenu = 200;
        }

        // Sub-panel con los botones nuevos (misma estética gris del menú)
        javax.swing.JPanel subMenu = new javax.swing.JPanel();
        subMenu.setBackground(new java.awt.Color(204, 204, 204));
        subMenu.setLayout(new javax.swing.BoxLayout(subMenu, javax.swing.BoxLayout.Y_AXIS));
        subMenu.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 6, 10, 6));

        javax.swing.JLabel lblSeccion = new javax.swing.JLabel("Gestión Huésped");
        lblSeccion.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12));
        lblSeccion.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
        lblSeccion.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 0, 6, 0));
        subMenu.add(lblSeccion);

        for (javax.swing.JButton b : new javax.swing.JButton[]{btnF004, btnF005, btnF011}) {
            b.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
            b.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, 34));
            subMenu.add(javax.swing.Box.createVerticalStrut(5));
            subMenu.add(b);
        }

        // Contenedor que reemplaza al jPanelMenu en la posición original
        javax.swing.JPanel menuWrapper = new javax.swing.JPanel(new java.awt.BorderLayout());
        menuWrapper.setBackground(new java.awt.Color(204, 204, 204));
        menuWrapper.setPreferredSize(new java.awt.Dimension(anchoMenu, 0));

        // Quitamos el jPanelMenu de su padre y lo reinsertamos dentro del wrapper
        padre.remove(jPanelMenu);
        menuWrapper.add(jPanelMenu, java.awt.BorderLayout.CENTER);
        menuWrapper.add(subMenu, java.awt.BorderLayout.SOUTH);

        // Colocamos el wrapper donde estaba el menú. El JFrame usa GroupLayout;
        // añadirlo directamente lo posiciona a la izquierda por el flujo, así que
        // reconstruimos el layout raíz de forma simple y equivalente.
        padre.setLayout(new java.awt.BorderLayout());
        padre.add(menuWrapper, java.awt.BorderLayout.WEST);
        padre.add(jPanelContenedor, java.awt.BorderLayout.CENTER);

        padre.revalidate();
        padre.repaint();
    }

    /**
     * Crea un botón con la estética azul del menú del recepcionista.
     */
    private javax.swing.JButton crearBotonMenu(String texto) {
        javax.swing.JButton b = new javax.swing.JButton(texto);
        b.setBackground(new java.awt.Color(102, 153, 255));
        b.setForeground(java.awt.Color.WHITE);
        b.setFocusPainted(false);
        b.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12));
        return b;
    }

    /**
     * Muestra un panel dentro del jPanelContenedor (mismo patrón del proyecto).
     */
    private void mostrarCard(javax.swing.JPanel panel) {
        jPanelContenedor.removeAll();
        jPanelContenedor.add(panel);
        jPanelContenedor.repaint();
        jPanelContenedor.revalidate();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanelMenu = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jButtonHabitaciones = new javax.swing.JButton();
        jButtonRegistrar = new javax.swing.JButton();
        jButtonAsignar = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jButtonArqueo = new javax.swing.JButton();
        jButtonRegresar = new javax.swing.JButton();
        jButtonListarHuéspedes = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jPanelContenedor = new javax.swing.JPanel();
        jPanelHabitaciones = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jComboFIltroxEstado = new javax.swing.JComboBox<>();
        jScrollPoneHabitaciones = new javax.swing.JScrollPane();
        jPanelCuadricula = new javax.swing.JPanel();
        jPanelRegistro = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jComboBTipoDocH = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        jTextFNumDocH = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jTextFNombreH = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jTextFApellidosH = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jTextFTelefonoH = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jTextFDireccionH = new javax.swing.JTextField();
        jButtonRegistrarH = new javax.swing.JButton();
        jButtonLimpiar = new javax.swing.JButton();
        jPanelArqueo = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        jTextFTotalSistema = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jTextFIngresoManual = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jTextFDiferenciaIngreso = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jLabel28 = new javax.swing.JLabel();
        jTextFEstado = new javax.swing.JTextField();
        jButtonValidar = new javax.swing.JButton();
        jButtonCerraTurno = new javax.swing.JButton();
        jPanelAsignacion = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jTextFNroH = new javax.swing.JTextField();
        jButtonBuscarH = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel15 = new javax.swing.JLabel();
        jComboBoxHabitacionDisponible = new javax.swing.JComboBox<>();
        jLabel16 = new javax.swing.JLabel();
        jComboBoxNroH = new javax.swing.JComboBox<>();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel17 = new javax.swing.JLabel();
        jCheckBoxDesayuno = new javax.swing.JCheckBox();
        jCheckBoxAlmuerzo = new javax.swing.JCheckBox();
        jCheckBoxCena = new javax.swing.JCheckBox();
        jSeparator3 = new javax.swing.JSeparator();
        jLabel18 = new javax.swing.JLabel();
        jDateChooserFechaIngreso = new com.toedter.calendar.JDateChooser();
        jLabel19 = new javax.swing.JLabel();
        jDateChooserFechaSalida = new com.toedter.calendar.JDateChooser();
        jButtonAsignarHabitacion = new javax.swing.JButton();
        jTextFDatosH = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        jButtonImprimirVoucher = new javax.swing.JButton();
        jPanelListarHuespedes = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableListarH = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("RECEPCIONISTA");

        jPanelMenu.setBackground(new java.awt.Color(204, 204, 204));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setText("RECEPCIÓN");

        jButtonHabitaciones.setText("Habitaciones");
        jButtonHabitaciones.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonHabitacionesActionPerformed(evt);
            }
        });

        jButtonRegistrar.setText("Registrar Huesped");
        jButtonRegistrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRegistrarActionPerformed(evt);
            }
        });

        jButtonAsignar.setText("Asignar Habitación");
        jButtonAsignar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAsignarActionPerformed(evt);
            }
        });
        jButtonAsignar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jButtonAsignarKeyTyped(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setText("CAJA");

        jButtonArqueo.setText("Arqueo de Caja");
        jButtonArqueo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonArqueoActionPerformed(evt);
            }
        });

        jButtonRegresar.setBackground(new java.awt.Color(102, 153, 255));
        jButtonRegresar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/Regresar.png"))); // NOI18N
        jButtonRegresar.setText("Regresar");
        jButtonRegresar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRegresarActionPerformed(evt);
            }
        });

        jButtonListarHuéspedes.setText("Listar Huéspedes");
        jButtonListarHuéspedes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonListarHuéspedesActionPerformed(evt);
            }
        });

        jButton1.setText("Pagos");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelMenuLayout = new javax.swing.GroupLayout(jPanelMenu);
        jPanelMenu.setLayout(jPanelMenuLayout);
        jPanelMenuLayout.setHorizontalGroup(
            jPanelMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jButtonHabitaciones, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jButtonRegistrar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jButtonAsignar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jButtonArqueo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jButtonListarHuéspedes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanelMenuLayout.createSequentialGroup()
                .addGroup(jPanelMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelMenuLayout.createSequentialGroup()
                        .addGap(87, 87, 87)
                        .addComponent(jLabel1))
                    .addGroup(jPanelMenuLayout.createSequentialGroup()
                        .addGap(115, 115, 115)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(110, Short.MAX_VALUE))
            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanelMenuLayout.createSequentialGroup()
                .addComponent(jButtonRegresar)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanelMenuLayout.setVerticalGroup(
            jPanelMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelMenuLayout.createSequentialGroup()
                .addComponent(jButtonRegresar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jButtonHabitaciones, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonRegistrar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonListarHuéspedes, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonAsignar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addGap(11, 11, 11)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(jButtonArqueo, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanelContenedor.setBackground(new java.awt.Color(255, 102, 0));
        jPanelContenedor.setLayout(new java.awt.CardLayout());

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel3.setText("HABITACIONES");

        jLabel4.setText("Ver estado de las habitaciones ");

        jComboFIltroxEstado.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todas las opciones", "Disponibles", "Ocupadas", "En mantenimiento" }));
        jComboFIltroxEstado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboFIltroxEstadoActionPerformed(evt);
            }
        });

        jPanelCuadricula.setLayout(new java.awt.GridLayout(1, 5, 10, 10));
        jScrollPoneHabitaciones.setViewportView(jPanelCuadricula);

        javax.swing.GroupLayout jPanelHabitacionesLayout = new javax.swing.GroupLayout(jPanelHabitaciones);
        jPanelHabitaciones.setLayout(jPanelHabitacionesLayout);
        jPanelHabitacionesLayout.setHorizontalGroup(
            jPanelHabitacionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelHabitacionesLayout.createSequentialGroup()
                .addGroup(jPanelHabitacionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelHabitacionesLayout.createSequentialGroup()
                        .addGroup(jPanelHabitacionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanelHabitacionesLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jComboFIltroxEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 303, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanelHabitacionesLayout.createSequentialGroup()
                                .addGap(238, 238, 238)
                                .addGroup(jPanelHabitacionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel4))))
                        .addGap(0, 336, Short.MAX_VALUE))
                    .addGroup(jPanelHabitacionesLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPoneHabitaciones)))
                .addContainerGap())
        );
        jPanelHabitacionesLayout.setVerticalGroup(
            jPanelHabitacionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelHabitacionesLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addGap(29, 29, 29)
                .addComponent(jComboFIltroxEstado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPoneHabitaciones, javax.swing.GroupLayout.DEFAULT_SIZE, 402, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanelContenedor.add(jPanelHabitaciones, "card3");

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/Huespedes.png"))); // NOI18N

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel5.setText("REGISTRO DE HUÉSPED");

        jLabel7.setText("Tipo de documento:");

        jComboBTipoDocH.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccione", "DNI", "Pasaporte", "Carnet de extranjería" }));

        jLabel8.setText("Nro. Documento:");

        jLabel9.setText("Nombre: ");

        jTextFNombreH.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFNombreHKeyTyped(evt);
            }
        });

        jLabel10.setText("Apellidos:");

        jTextFApellidosH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFApellidosHActionPerformed(evt);
            }
        });
        jTextFApellidosH.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFApellidosHKeyTyped(evt);
            }
        });

        jLabel11.setText("Nro. Teléfono:");

        jTextFTelefonoH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFTelefonoHActionPerformed(evt);
            }
        });
        jTextFTelefonoH.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFTelefonoHKeyTyped(evt);
            }
        });

        jLabel12.setText("Dirección:");

        jButtonRegistrarH.setBackground(new java.awt.Color(153, 255, 153));
        jButtonRegistrarH.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/Guardar.gif"))); // NOI18N
        jButtonRegistrarH.setText("Registrar");
        jButtonRegistrarH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRegistrarHActionPerformed(evt);
            }
        });

        jButtonLimpiar.setBackground(new java.awt.Color(0, 102, 102));
        jButtonLimpiar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/Retro.png"))); // NOI18N
        jButtonLimpiar.setText("Limpiar");
        jButtonLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLimpiarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelRegistroLayout = new javax.swing.GroupLayout(jPanelRegistro);
        jPanelRegistro.setLayout(jPanelRegistroLayout);
        jPanelRegistroLayout.setHorizontalGroup(
            jPanelRegistroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelRegistroLayout.createSequentialGroup()
                .addGroup(jPanelRegistroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelRegistroLayout.createSequentialGroup()
                        .addGap(201, 201, 201)
                        .addGroup(jPanelRegistroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanelRegistroLayout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(jLabel5))
                            .addComponent(jLabel6)))
                    .addGroup(jPanelRegistroLayout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addGroup(jPanelRegistroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(jLabel9)
                            .addComponent(jLabel11))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanelRegistroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jComboBTipoDocH, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTextFNombreH)
                            .addComponent(jTextFTelefonoH))
                        .addGap(63, 63, 63)
                        .addGroup(jPanelRegistroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addComponent(jLabel10)
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanelRegistroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextFNumDocH)
                            .addComponent(jTextFApellidosH)
                            .addComponent(jTextFDireccionH, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)))
                    .addGroup(jPanelRegistroLayout.createSequentialGroup()
                        .addGap(132, 132, 132)
                        .addComponent(jButtonRegistrarH)
                        .addGap(139, 139, 139)
                        .addComponent(jButtonLimpiar)))
                .addContainerGap(191, Short.MAX_VALUE))
        );
        jPanelRegistroLayout.setVerticalGroup(
            jPanelRegistroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelRegistroLayout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addGap(18, 18, 18)
                .addGroup(jPanelRegistroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jComboBTipoDocH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(jTextFNumDocH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanelRegistroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel9)
                    .addGroup(jPanelRegistroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextFNombreH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel10)
                        .addComponent(jTextFApellidosH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(29, 29, 29)
                .addGroup(jPanelRegistroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jTextFTelefonoH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12)
                    .addComponent(jTextFDireccionH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(65, 65, 65)
                .addGroup(jPanelRegistroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonRegistrarH)
                    .addComponent(jButtonLimpiar))
                .addContainerGap(132, Short.MAX_VALUE))
        );

        jPanelContenedor.add(jPanelRegistro, "card2");

        jLabel21.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel21.setText("Arqueo de Caja ");

        jPanel1.setBackground(new java.awt.Color(204, 204, 204));

        jLabel22.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel22.setText("Total Sistema (Registrado)");

        jTextFTotalSistema.setEditable(false);
        jTextFTotalSistema.setBackground(new java.awt.Color(204, 204, 204));
        jTextFTotalSistema.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextFTotalSistema.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFTotalSistemaActionPerformed(evt);
            }
        });

        jLabel27.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel27.setText("S/.");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel22)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel27)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFTotalSistema, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(41, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel22)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFTotalSistema, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel27))
                .addContainerGap(59, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(204, 204, 204));

        jLabel23.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel23.setText("Efectivo Ingresado (manual)");

        jLabel24.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel24.setText("S/.");

        jTextFIngresoManual.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jTextFIngresoManual.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFIngresoManualActionPerformed(evt);
            }
        });
        jTextFIngresoManual.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFIngresoManualKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFIngresoManualKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel23)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel24)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTextFIngresoManual)))
                .addContainerGap(61, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel23)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24)
                    .addComponent(jTextFIngresoManual, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(61, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(204, 204, 204));

        jLabel25.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel25.setText("Diferencia");

        jLabel26.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel26.setText("S/.");

        jTextFDiferenciaIngreso.setEditable(false);
        jTextFDiferenciaIngreso.setBackground(new java.awt.Color(204, 204, 204));
        jTextFDiferenciaIngreso.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jTextFDiferenciaIngreso.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFDiferenciaIngresoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel25)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel26)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFDiferenciaIngreso, javax.swing.GroupLayout.DEFAULT_SIZE, 128, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel25)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel26)
                    .addComponent(jTextFDiferenciaIngreso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(62, Short.MAX_VALUE))
        );

        jPanel4.setBackground(new java.awt.Color(204, 204, 204));

        jLabel28.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel28.setText("Estado del Turno");

        jTextFEstado.setEditable(false);
        jTextFEstado.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel28)
                        .addGap(0, 78, Short.MAX_VALUE))
                    .addComponent(jTextFEstado))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel28)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTextFEstado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(36, Short.MAX_VALUE))
        );

        jButtonValidar.setText("Validar Arqueo");
        jButtonValidar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonValidarActionPerformed(evt);
            }
        });

        jButtonCerraTurno.setText("Cerrar Turno");
        jButtonCerraTurno.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCerraTurnoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelArqueoLayout = new javax.swing.GroupLayout(jPanelArqueo);
        jPanelArqueo.setLayout(jPanelArqueoLayout);
        jPanelArqueoLayout.setHorizontalGroup(
            jPanelArqueoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelArqueoLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButtonValidar)
                .addGap(59, 59, 59)
                .addComponent(jButtonCerraTurno)
                .addGap(144, 144, 144))
            .addGroup(jPanelArqueoLayout.createSequentialGroup()
                .addGroup(jPanelArqueoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelArqueoLayout.createSequentialGroup()
                        .addGap(45, 45, 45)
                        .addGroup(jPanelArqueoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanelArqueoLayout.createSequentialGroup()
                                .addGap(13, 13, 13)
                                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(93, 93, 93)
                        .addGroup(jPanelArqueoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelArqueoLayout.createSequentialGroup()
                                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(40, 40, 40))))
                    .addGroup(jPanelArqueoLayout.createSequentialGroup()
                        .addGap(64, 64, 64)
                        .addComponent(jLabel21)))
                .addContainerGap(154, Short.MAX_VALUE))
        );
        jPanelArqueoLayout.setVerticalGroup(
            jPanelArqueoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelArqueoLayout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addComponent(jLabel21)
                .addGap(45, 45, 45)
                .addGroup(jPanelArqueoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(46, 46, 46)
                .addGroup(jPanelArqueoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                .addGroup(jPanelArqueoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonValidar)
                    .addComponent(jButtonCerraTurno))
                .addGap(77, 77, 77))
        );

        jPanelContenedor.add(jPanelArqueo, "card4");

        jPanelAsignacion.setPreferredSize(new java.awt.Dimension(670, 590));

        jLabel13.setFont(new java.awt.Font("Segoe UI Black", 1, 18)); // NOI18N
        jLabel13.setText("Asignar Habitación a Huésped");

        jLabel14.setText("Número de documentoo de huésped");

        jTextFNroH.setToolTipText("");

        jButtonBuscarH.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/Buscar.gif"))); // NOI18N
        jButtonBuscarH.setText("Buscar Huésped");
        jButtonBuscarH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBuscarHActionPerformed(evt);
            }
        });

        jLabel15.setText("Habitación a asignar");

        jComboBoxHabitacionDisponible.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccione" }));
        jComboBoxHabitacionDisponible.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxHabitacionDisponibleActionPerformed(evt);
            }
        });

        jLabel16.setText("Número de huespedes");

        jComboBoxNroH.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccione", "1", "2", "3", "4" }));

        jLabel17.setText("Servicio extra");

        jCheckBoxDesayuno.setText("Desayuno");

        jCheckBoxAlmuerzo.setText("Almuerzo");

        jCheckBoxCena.setText("Cena");

        jLabel18.setText("Fecha de Ingreso");

        jDateChooserFechaIngreso.setMinSelectableDate(new java.util.Date());

        jLabel19.setText("Fecha de Salida");

        jDateChooserFechaSalida.setMinSelectableDate(new java.util.Date());

        jButtonAsignarHabitacion.setText("Asignar Habitación");
        jButtonAsignarHabitacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAsignarHabitacionActionPerformed(evt);
            }
        });

        jTextFDatosH.setEditable(false);

        jLabel20.setText("Nombres y Apellidos: ");

        jButtonImprimirVoucher.setText("Imprimir Voucher");
        jButtonImprimirVoucher.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonImprimirVoucherActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelAsignacionLayout = new javax.swing.GroupLayout(jPanelAsignacion);
        jPanelAsignacion.setLayout(jPanelAsignacionLayout);
        jPanelAsignacionLayout.setHorizontalGroup(
            jPanelAsignacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelAsignacionLayout.createSequentialGroup()
                .addGroup(jPanelAsignacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelAsignacionLayout.createSequentialGroup()
                        .addGap(165, 165, 165)
                        .addComponent(jLabel13))
                    .addGroup(jPanelAsignacionLayout.createSequentialGroup()
                        .addGap(56, 56, 56)
                        .addGroup(jPanelAsignacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanelAsignacionLayout.createSequentialGroup()
                                .addGap(19, 19, 19)
                                .addComponent(jTextFNroH, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jButtonBuscarH))
                            .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 562, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 562, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 562, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanelAsignacionLayout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addGroup(jPanelAsignacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel17)
                                    .addGroup(jPanelAsignacionLayout.createSequentialGroup()
                                        .addGroup(jPanelAsignacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel15)
                                            .addComponent(jComboBoxHabitacionDisponible, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(123, 123, 123)
                                        .addGroup(jPanelAsignacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jComboBoxNroH, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                    .addGroup(jPanelAsignacionLayout.createSequentialGroup()
                                        .addComponent(jCheckBoxDesayuno)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jCheckBoxAlmuerzo)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jCheckBoxCena))
                                    .addGroup(jPanelAsignacionLayout.createSequentialGroup()
                                        .addComponent(jLabel18)
                                        .addGap(28, 28, 28)
                                        .addComponent(jDateChooserFechaIngreso, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(32, 32, 32)
                                        .addComponent(jLabel19)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jDateChooserFechaSalida, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(jPanelAsignacionLayout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addGroup(jPanelAsignacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel14)
                                    .addGroup(jPanelAsignacionLayout.createSequentialGroup()
                                        .addComponent(jLabel20)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jTextFDatosH, javax.swing.GroupLayout.PREFERRED_SIZE, 304, javax.swing.GroupLayout.PREFERRED_SIZE)))))))
                .addContainerGap(52, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelAsignacionLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jButtonAsignarHabitacion)
                .addGap(31, 31, 31)
                .addComponent(jButtonImprimirVoucher)
                .addGap(132, 132, 132))
        );
        jPanelAsignacionLayout.setVerticalGroup(
            jPanelAsignacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelAsignacionLayout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addComponent(jLabel13)
                .addGap(29, 29, 29)
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanelAsignacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonBuscarH)
                    .addComponent(jTextFNroH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanelAsignacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFDatosH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel20))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 8, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelAsignacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(jLabel16))
                .addGap(18, 18, 18)
                .addGroup(jPanelAsignacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBoxHabitacionDisponible, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBoxNroH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(46, 46, 46)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 8, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(7, 7, 7)
                .addComponent(jLabel17)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanelAsignacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBoxDesayuno)
                    .addComponent(jCheckBoxAlmuerzo)
                    .addComponent(jCheckBoxCena))
                .addGap(18, 18, 18)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 8, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanelAsignacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel18)
                    .addComponent(jLabel19)
                    .addComponent(jDateChooserFechaSalida, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jDateChooserFechaIngreso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 67, Short.MAX_VALUE)
                .addGroup(jPanelAsignacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonAsignarHabitacion)
                    .addComponent(jButtonImprimirVoucher))
                .addGap(48, 48, 48))
        );

        jPanelContenedor.add(jPanelAsignacion, "card5");

        jTableListarH.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4", "Title 5", "Title 6"
            }
        ));
        jScrollPane1.setViewportView(jTableListarH);

        javax.swing.GroupLayout jPanelListarHuespedesLayout = new javax.swing.GroupLayout(jPanelListarHuespedes);
        jPanelListarHuespedes.setLayout(jPanelListarHuespedesLayout);
        jPanelListarHuespedesLayout.setHorizontalGroup(
            jPanelListarHuespedesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelListarHuespedesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 748, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(23, Short.MAX_VALUE))
        );
        jPanelListarHuespedesLayout.setVerticalGroup(
            jPanelListarHuespedesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelListarHuespedesLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(40, Short.MAX_VALUE))
        );

        jPanelContenedor.add(jPanelListarHuespedes, "card6");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanelMenu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelContenedor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanelContenedor, javax.swing.GroupLayout.DEFAULT_SIZE, 564, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(jPanelMenu, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonRegistrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRegistrarActionPerformed

        jPanelContenedor.removeAll();
        jPanelContenedor.add(jPanelRegistro);
        jPanelContenedor.repaint();
        jPanelContenedor.revalidate();
    }//GEN-LAST:event_jButtonRegistrarActionPerformed

    private void jButtonHabitacionesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonHabitacionesActionPerformed

        verificarSalidasAutomaticas();
        jPanelContenedor.removeAll();
        jPanelContenedor.add(jPanelHabitaciones);
        jPanelContenedor.repaint();
        jPanelContenedor.revalidate();
        String filtroActual = jComboFIltroxEstado.getSelectedItem().toString();
        dibujarCuadriculaHabitaciones(filtroActual);
    }//GEN-LAST:event_jButtonHabitacionesActionPerformed

    private void jButtonArqueoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonArqueoActionPerformed

        jPanelContenedor.removeAll();
        jPanelContenedor.add(jPanelArqueo);
        jPanelContenedor.repaint();
        jPanelContenedor.revalidate();
        iniciarArqueoCaja();
    }//GEN-LAST:event_jButtonArqueoActionPerformed

    private void jButtonAsignarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAsignarActionPerformed

        jPanelContenedor.removeAll();
        jPanelContenedor.add(jPanelAsignacion);
        jPanelContenedor.repaint();
        jPanelContenedor.revalidate();
        cargarHabitacionesDisponibles();
    }//GEN-LAST:event_jButtonAsignarActionPerformed

    private void jComboFIltroxEstadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboFIltroxEstadoActionPerformed

        // Capturamos lo que el usuario seleccionó 
        String seleccion = jComboFIltroxEstado.getSelectedItem().toString();
        // Mandamos a redibujar todo con ese filtro
        dibujarCuadriculaHabitaciones(seleccion);

    }//GEN-LAST:event_jComboFIltroxEstadoActionPerformed

    private void jButtonRegresarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRegresarActionPerformed
// 1. Preguntamos por seguridad
        int confirmacion = javax.swing.JOptionPane.showConfirmDialog(this,
                "¿Está seguro que desea salir de esta pantalla?",
                "Confirmar Salida",
                javax.swing.JOptionPane.YES_NO_OPTION);

        if (confirmacion == javax.swing.JOptionPane.YES_OPTION) {

            // 2. Le preguntamos al sistema quién está conectado actualmente
            String rol = controlador.SistemaHotel.getInstancia().getHotel().getRolActual();

            // 3. Tomamos la decisión dinámica
            if (rol.equals("ADMIN")) {
                // Si es el administrador, lo devolvemos al menú principal
                new Menu_principal().setVisible(true);
            } else {
                // Si es un empleado normal (Chef, Limpieza, etc.), cerramos su sesión y se le manda al Login
                // También se limpia la memoria por seguridad
                controlador.SistemaHotel.getInstancia().getHotel().setRolActual("");
                new Login().setVisible(true);
            }

            // 4. Cerramos la ventana actual
            this.dispose();
        }
    }//GEN-LAST:event_jButtonRegresarActionPerformed

    private void jTextFApellidosHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFApellidosHActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFApellidosHActionPerformed

    private void jButtonRegistrarHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRegistrarHActionPerformed
        try {
            // 1. CAPTURAR DATOS (Con tus nombres de variables actuales)
            String nombre = jTextFNombreH.getText().trim();
            String apellido = jTextFApellidosH.getText().trim();
            String numDocStr = jTextFNumDocH.getText().trim();
            String telefonoStr = jTextFTelefonoH.getText().trim();
            String direccion = jTextFDireccionH.getText().trim();
            String tipoDoc = jComboBTipoDocH.getSelectedItem().toString().trim();

            // 2. VALIDAR CAMPOS VACÍOS
            if (tipoDoc.equalsIgnoreCase("Seleccione")) {
                javax.swing.JOptionPane.showMessageDialog(this, "Por favor, seleccione un Tipo de Documento.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (nombre.isEmpty() || apellido.isEmpty() || numDocStr.isEmpty() || telefonoStr.isEmpty() || direccion.isEmpty()) {
                javax.swing.JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos, incluyendo la dirección.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 3. VALIDAR NOMBRES Y APELLIDOS (Solo letras)
            if (!nombre.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+")) {
                javax.swing.JOptionPane.showMessageDialog(this, "Nombre y Apellido solo deben contener letras.", "Error de formato", javax.swing.JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 4. VALIDAR EL TIPO DE DOCUMENTO CON REGEX
            if (tipoDoc.equals("DNI")) {
                if (!numDocStr.matches("\\d{8}")) {
                    javax.swing.JOptionPane.showMessageDialog(this, "El DNI debe tener exactamente 8 dígitos numéricos.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } else if (tipoDoc.equals("Carnet de extranjería")) {
                if (!numDocStr.matches("\\d{9,12}")) {
                    javax.swing.JOptionPane.showMessageDialog(this, "El Carnet de Extranjería debe tener entre 9 y 12 dígitos numéricos.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } else if (tipoDoc.equals("Pasaporte")) {
                if (!numDocStr.matches("^[a-zA-Z0-9]{6,20}$")) {
                    javax.swing.JOptionPane.showMessageDialog(this, "Formato de Pasaporte inválido (solo letras y números, entre 6 y 20 caracteres).", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            // =========================================================
            // 5. VALIDAR TELÉFONO (MODIFICADO: Exactamente 9 dígitos)
            if (!telefonoStr.matches("\\d{9}")) {
                javax.swing.JOptionPane.showMessageDialog(this, "El número de celular debe tener exactamente 9 dígitos.", "Error de formato", javax.swing.JOptionPane.ERROR_MESSAGE);
                return;
            }
            int telefono = Integer.parseInt(telefonoStr);
            // =========================================================

            // 6. CREAR HUÉSPED
            Entidades.Huesped nuevoHuesped = new Entidades.Huesped(nombre, apellido, tipoDoc, numDocStr, telefono, direccion);

            // 7. REGISTRAR Y GUARDAR EN MEMORIA
            boolean registrado = controlador.SistemaHotel.getInstancia().getHotel().registrarHuesped(nuevoHuesped);

            if (registrado) {
                // Guardado físico en el archivo .dat
                controlador.SistemaHotel.getInstancia().guardarCambios();

                javax.swing.JOptionPane.showMessageDialog(this, "Huésped registrado y guardado correctamente.", "Registro exitoso", javax.swing.JOptionPane.INFORMATION_MESSAGE);

            } else {
                javax.swing.JOptionPane.showMessageDialog(this, "Ya existe un huésped registrado con ese número de documento.", "Registro duplicado", javax.swing.JOptionPane.WARNING_MESSAGE);
            }

        } catch (NumberFormatException e) {
            javax.swing.JOptionPane.showMessageDialog(this, "El teléfono debe ser un número válido.", "Error de formato", javax.swing.JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(this, "Ocurrió un error al registrar al huésped: " + e.getMessage(), "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButtonRegistrarHActionPerformed

    private void jTextFTelefonoHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFTelefonoHActionPerformed

    }//GEN-LAST:event_jTextFTelefonoHActionPerformed

    private void jButtonLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLimpiarActionPerformed
// 1. Limpiar todas las cajas de texto
        jTextFNumDocH.setText("");
        jTextFNombreH.setText("");
        jTextFApellidosH.setText("");
        jTextFTelefonoH.setText("");
        jTextFDireccionH.setText("");

        // 2. Restaurar el ComboBox a la primera opción (por lo general es "Seleccione")
        jComboBTipoDocH.setSelectedIndex(0);

        // 3. Colocar el cursor en la primera casilla (Número de Documento)
        jTextFNumDocH.requestFocus();
    }//GEN-LAST:event_jButtonLimpiarActionPerformed

    private void jComboBoxHabitacionDisponibleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxHabitacionDisponibleActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBoxHabitacionDisponibleActionPerformed

    private void jButtonBuscarHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBuscarHActionPerformed
        String numDocumento = jTextFNroH.getText().trim();

        if (numDocumento.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese el número de documento del huésped.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Huesped huesped = hotel.buscarHuespedPorDocumento(numDocumento);

        if (huesped == null) {
            JOptionPane.showMessageDialog(this, "Huésped no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            jTextFDatosH.setText(huesped.getNombre() + " " + huesped.getApellido());
        }
    }//GEN-LAST:event_jButtonBuscarHActionPerformed

    private void jButtonAsignarHabitacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAsignarHabitacionActionPerformed
        try {
            // 1. OBTENER Y VERIFICAR EL HUÉSPED TITULAR
            String numDocumento = jTextFNroH.getText().trim();

            if (numDocumento.isEmpty()) {
                javax.swing.JOptionPane.showMessageDialog(this, "Primero debe ingresar y buscar el documento del huésped.", "Advertencia", javax.swing.JOptionPane.WARNING_MESSAGE);
                return;
            }

            Entidades.Huesped huespedEncontrado = controlador.SistemaHotel.getInstancia().getHotel().buscarHuespedPorDocumento(numDocumento);

            if (huespedEncontrado == null) {
                javax.swing.JOptionPane.showMessageDialog(this, "El huésped con documento " + numDocumento + " no existe. Regístrelo primero.", "Huésped no encontrado", javax.swing.JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 2. OBTENER LA HABITACIÓN
            String comboHabStr = jComboBoxHabitacionDisponible.getSelectedItem().toString();
            if (comboHabStr.startsWith("Seleccione")) {
                javax.swing.JOptionPane.showMessageDialog(this, "Debe seleccionar una habitación disponible.", "Advertencia", javax.swing.JOptionPane.WARNING_MESSAGE);
                return;
            }

            String numHabitacion = comboHabStr.split(" - ")[0];
            Entidades.Habitacion habSeleccionada = controlador.SistemaHotel.getInstancia().getHotel().buscarHabitacionporNumero(numHabitacion);

            // 3. CALCULAR NOCHES CON LOS JDATECHOOSER
            java.util.Date dateIngreso = jDateChooserFechaIngreso.getDate();
            java.util.Date dateSalida = jDateChooserFechaSalida.getDate();

            if (dateIngreso == null || dateSalida == null) {
                javax.swing.JOptionPane.showMessageDialog(this, "Debe seleccionar las fechas de Ingreso y Salida.", "Advertencia", javax.swing.JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!dateSalida.after(dateIngreso)) {
                javax.swing.JOptionPane.showMessageDialog(this, "La fecha de salida debe ser MAYOR a la fecha de ingreso.", "Error de fechas", javax.swing.JOptionPane.ERROR_MESSAGE);
                return;
            }

            long difMilisegundos = dateSalida.getTime() - dateIngreso.getTime();
            int noches = (int) java.util.concurrent.TimeUnit.DAYS.convert(difMilisegundos, java.util.concurrent.TimeUnit.MILLISECONDS);
            java.time.LocalDateTime fechaEntradaLocal = dateIngreso.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime();

            // 4. OBTENER CANTIDAD DE PERSONAS
            String personasStr = jComboBoxNroH.getSelectedItem().toString();
            if (personasStr.startsWith("Seleccione")) {
                javax.swing.JOptionPane.showMessageDialog(this, "Debe seleccionar la cantidad de personas.", "Advertencia", javax.swing.JOptionPane.WARNING_MESSAGE);
                return;
            }
            int cantidadPersonas = Integer.parseInt(personasStr);

            // =======================================================================
            // ---> CONTROL LEGAL DE ACOMPAÑANTES (CON SEGURIDAD EN VIVO) <---
            java.util.List<Entidades.Huesped> listaAcompañantes = new java.util.ArrayList<>();
            int personasAdicionales = cantidadPersonas - 1;

            for (int k = 1; k <= personasAdicionales; k++) {

                javax.swing.JPanel panelFormulario = new javax.swing.JPanel(new java.awt.GridLayout(5, 2, 5, 10));

                javax.swing.JComboBox<String> cmbTipoDoc = new javax.swing.JComboBox<>(new String[]{"DNI", "Pasaporte", "Carnet de extranjería"});
                javax.swing.JTextField txtNumDoc = new javax.swing.JTextField();
                javax.swing.JTextField txtNombres = new javax.swing.JTextField();
                javax.swing.JTextField txtApellidos = new javax.swing.JTextField();
                javax.swing.JTextField txtCelular = new javax.swing.JTextField();

                // ---> ESCUDO 1: Bloquear letras en DNI
                txtNumDoc.addKeyListener(new java.awt.event.KeyAdapter() {
                    public void keyTyped(java.awt.event.KeyEvent evt) {
                        if (!Character.isDigit(evt.getKeyChar())) {
                            evt.consume();
                        }
                    }
                });

                // ---> ESCUDO 2: Bloquear números en Nombres y Apellidos
                java.awt.event.KeyAdapter filtroLetras = new java.awt.event.KeyAdapter() {
                    public void keyTyped(java.awt.event.KeyEvent evt) {
                        char c = evt.getKeyChar();
                        // Permite letras, espacios y borrar. Destruye lo demás.
                        if (!Character.isLetter(c) && c != ' ' && !Character.isISOControl(c)) {
                            evt.consume();
                        }
                    }
                };
                txtNombres.addKeyListener(filtroLetras);
                txtApellidos.addKeyListener(filtroLetras);

                // ---> ESCUDO 3: Bloquear letras en Celular y Máximo 9 dígitos
                txtCelular.addKeyListener(new java.awt.event.KeyAdapter() {
                    public void keyTyped(java.awt.event.KeyEvent evt) {
                        char c = evt.getKeyChar();
                        if (!Character.isDigit(c)) {
                            evt.consume(); // Destruye si es letra
                            return;
                        }
                        if (txtCelular.getText().length() >= 9) {
                            evt.consume(); // Destruye si intenta poner 10 números
                        }
                    }
                });

                // Armamos la estructura del formulario
                panelFormulario.add(new javax.swing.JLabel("Tipo Documento:"));
                panelFormulario.add(cmbTipoDoc);
                panelFormulario.add(new javax.swing.JLabel("Nro. Documento:"));
                panelFormulario.add(txtNumDoc);
                panelFormulario.add(new javax.swing.JLabel("Nombres:"));
                panelFormulario.add(txtNombres);
                panelFormulario.add(new javax.swing.JLabel("Apellidos:"));
                panelFormulario.add(txtApellidos);
                panelFormulario.add(new javax.swing.JLabel("Nro. Celular:"));
                panelFormulario.add(txtCelular);

                int resultadoVentana = javax.swing.JOptionPane.showConfirmDialog(this, panelFormulario,
                        "Datos del Acompañante " + k + " de " + personasAdicionales + " (Habitación " + numHabitacion + ")",
                        javax.swing.JOptionPane.OK_CANCEL_OPTION, javax.swing.JOptionPane.PLAIN_MESSAGE);

                // Si cancela, abortamos
                if (resultadoVentana != javax.swing.JOptionPane.OK_OPTION) {
                    javax.swing.JOptionPane.showMessageDialog(this, "Asignación cancelada. Todos los huéspedes ocupantes deben ser registrados por ley.", "Registro Incompleto", javax.swing.JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Capturamos lo que escribió
                String tDoc = cmbTipoDoc.getSelectedItem().toString();
                String nDoc = txtNumDoc.getText().trim();
                String nom = txtNombres.getText().trim();
                String ape = txtApellidos.getText().trim();
                String celStr = txtCelular.getText().trim();

                // ---> ESCUDO 4: Validar que no haya campos vacíos
                if (nDoc.isEmpty() || nom.isEmpty() || ape.isEmpty() || celStr.isEmpty()) {
                    javax.swing.JOptionPane.showMessageDialog(this, "Error: Debe completar todos los campos del acompañante.", "Campos Vacíos", javax.swing.JOptionPane.ERROR_MESSAGE);
                    k--; // Le repetimos la ventana de este acompañante
                    continue;
                }

                // ---> ESCUDO 5: Validar que el celular sea EXACTAMENTE 9 dígitos (Mínimo)
                if (!celStr.matches("\\d{9}")) {
                    javax.swing.JOptionPane.showMessageDialog(this, "El número de celular debe tener exactamente 9 dígitos.", "Error de formato", javax.swing.JOptionPane.ERROR_MESSAGE);
                    k--; // Le repetimos la ventana
                    continue;
                }

                int celular = Integer.parseInt(celStr);
                String direccionCompartida = huespedEncontrado.getDireccion();

                Entidades.Huesped acompañante = new Entidades.Huesped(nom, ape, tDoc, nDoc, celular, direccionCompartida);
                listaAcompañantes.add(acompañante);
            }
            // =======================================================================

            // 5. OBTENER SERVICIOS EXTRAS (Checkboxes)
            boolean pideDesayuno = jCheckBoxDesayuno.isSelected();
            boolean pideAlmuerzo = jCheckBoxAlmuerzo.isSelected();
            boolean pideCena = jCheckBoxCena.isSelected();

            // 6. GENERAR LA FICHA DE HOSPEDAJE
            String idFicha = "F-" + numHabitacion + "-" + (System.currentTimeMillis() % 10000);

            java.util.List<Entidades.Huesped> listaHuespedesFicha = new java.util.ArrayList<>();
            listaHuespedesFicha.add(huespedEncontrado);

            for (Entidades.Huesped acmp : listaAcompañantes) {
                listaHuespedesFicha.add(acmp);
                if (controlador.SistemaHotel.getInstancia().getHotel().buscarHuespedPorDocumento(acmp.getNumDocumento()) == null) {
                    controlador.SistemaHotel.getInstancia().getHotel().getListaHuespedes().add(acmp);
                }
            }

            Entidades.FichaHospedaje nuevaFicha = new Entidades.FichaHospedaje(idFicha, listaHuespedesFicha, habSeleccionada, noches, fechaEntradaLocal, cantidadPersonas, pideDesayuno, pideAlmuerzo, pideCena);

            // 7. ACTUALIZAR EL ESTADO DE LA HABITACIÓN A OCUPADA
            habSeleccionada.setEstado('O');

            // 8. GUARDAR EN EL SISTEMA Y EN EL ARCHIVO .DAT
            controlador.SistemaHotel.getInstancia().getHotel().getListaFHospedaje().add(nuevaFicha);
            controlador.SistemaHotel.getInstancia().guardarCambios();

            // 9. MENSAJE DE ÉXITO Y LIMPIEZA
            javax.swing.JOptionPane.showMessageDialog(this, "Asignación exitosa.\nLa habitación " + numHabitacion + " ha sido ocupada por " + cantidadPersonas + " personas.\nFicha generada: " + idFicha, "Asignación Completa", javax.swing.JOptionPane.INFORMATION_MESSAGE);

            jTextFNroH.setText("");
            jTextFDatosH.setText("");
            jComboBoxHabitacionDisponible.setSelectedIndex(0);
            jComboBoxNroH.setSelectedIndex(0);
            jDateChooserFechaIngreso.setDate(null);
            jDateChooserFechaSalida.setDate(null);
            jCheckBoxDesayuno.setSelected(false);
            jCheckBoxAlmuerzo.setSelected(false);
            jCheckBoxCena.setSelected(false);

            cargarHabitacionesDisponibles();

        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(this, "Ocurrió un error inesperado al asignar la habitación: " + e.getMessage(), "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButtonAsignarHabitacionActionPerformed

    private void jTextFIngresoManualActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFIngresoManualActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFIngresoManualActionPerformed

    private void jTextFDiferenciaIngresoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFDiferenciaIngresoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFDiferenciaIngresoActionPerformed

    private void jTextFTotalSistemaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFTotalSistemaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFTotalSistemaActionPerformed

    private void jButtonValidarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonValidarActionPerformed
// Validamos que hayan ingresado al menos algo de dinero (incluso si es 0)
        if (jTextFIngresoManual.getText().trim().isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this, "Debe ingresar el monto del conteo manual antes de validar.", "Aviso", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        jTextFEstado.setText("Validado");
        javax.swing.JOptionPane.showMessageDialog(this, "Arqueo validado correctamente. Puede proceder a cerrar el turno cuando esté listo.", "Validación", javax.swing.JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_jButtonValidarActionPerformed

    private void jButtonImprimirVoucherActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonImprimirVoucherActionPerformed

        try {
            // 1. Obtenemos el DNI de la caja de texto
            String dniBusqueda = jTextFNroH.getText().trim();

            if (dniBusqueda.isEmpty()) {
                javax.swing.JOptionPane.showMessageDialog(this, "Debe buscar a un cliente para imprimir su voucher.", "Aviso", javax.swing.JOptionPane.WARNING_MESSAGE);
                return;
            }

            // 2. Buscamos la última ficha de este cliente
            java.util.List<Entidades.FichaHospedaje> listaFichas = controlador.SistemaHotel.getInstancia().getHotel().getListaFHospedaje();
            Entidades.FichaHospedaje fichaEncontrada = null;

            // Buscamos de atrás para adelante para agarrar la asignación más nueva
            for (int i = listaFichas.size() - 1; i >= 0; i--) {
                Entidades.FichaHospedaje f = listaFichas.get(i);
                if (f.getHuespedes().get(0).getNumDocumento().equals(dniBusqueda)) {
                    fichaEncontrada = f;
                    break;
                }
            }

            if (fichaEncontrada == null) {
                javax.swing.JOptionPane.showMessageDialog(this, "No se encontró una asignación reciente para este documento.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 3. Calculamos el Importe Total proyectado (usando noches esperadas)
            int noches = fichaEncontrada.getNochesEsperadas();
            int personas = fichaEncontrada.getCantidadPersonas();
            double costoHab = noches * fichaEncontrada.getHabitacion().getPrecio();

            double costoComidas = 0.0;
            if (fichaEncontrada.isIncluyeDesayuno()) {
                costoComidas += (20 * personas * noches);
            }
            if (fichaEncontrada.isIncluyeAlmuerzo()) {
                costoComidas += (20 * personas * noches);
            }
            if (fichaEncontrada.isIncluyeCena()) {
                costoComidas += (20 * personas * noches);
            }

            double importeTotalProyectado = costoHab + costoComidas;

            // 4. Creamos el Voucher
            String codVoucher = "VOU-" + System.currentTimeMillis() % 10000;
            Entidades.ComprobantePago voucher = new Entidades.ComprobantePago(codVoucher, fichaEncontrada, importeTotalProyectado, java.time.LocalDateTime.now());

            // 5. ¡A imprimir en pantalla!
            javax.swing.JTextArea txtArea = new javax.swing.JTextArea(voucher.toString());
            txtArea.setFont(new java.awt.Font("Monospaced", java.awt.Font.PLAIN, 14));
            txtArea.setEditable(false);
            txtArea.setBackground(new java.awt.Color(250, 250, 250));

            javax.swing.JOptionPane.showMessageDialog(this, new javax.swing.JScrollPane(txtArea), "Voucher de Consumo", javax.swing.JOptionPane.PLAIN_MESSAGE);

        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(this, "Error al generar el voucher: " + e.getMessage(), "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonImprimirVoucherActionPerformed

    private void jTextFIngresoManualKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFIngresoManualKeyReleased
        try {
            // 1. Obtenemos el total del sistema que calculamos antes
            double totalSistema = Double.parseDouble(jTextFTotalSistema.getText());

            // 2. Obtenemos lo que el recepcionista está tipeando
            String manualStr = jTextFIngresoManual.getText().trim();

            // Si borra todo, asumimos que hay 0
            double ingresoManual = 0.0;
            if (!manualStr.isEmpty()) {
                ingresoManual = Double.parseDouble(manualStr);
            }

            // 3. Calculamos la diferencia (Manual - Sistema)
            // Si sobra dinero será positivo, si falta será negativo (descuadre)
            double diferencia = ingresoManual - totalSistema;

            // 4. Mostramos la diferencia en tiempo real
            jTextFDiferenciaIngreso.setText(String.format("%.2f", diferencia).replace(",", "."));

        } catch (NumberFormatException e) {
            // Si el usuario tipea letras por accidente, mostramos un error visual sutil
            jTextFDiferenciaIngreso.setText("Error Numérico");
        }
    }//GEN-LAST:event_jTextFIngresoManualKeyReleased

    private void jButtonListarHuéspedesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonListarHuéspedesActionPerformed
        jPanelContenedor.removeAll();
        jPanelContenedor.add(jPanelListarHuespedes);
        jPanelContenedor.repaint();
        jPanelContenedor.revalidate();
        cargarDatos();
    }//GEN-LAST:event_jButtonListarHuéspedesActionPerformed

    private void jButtonCerraTurnoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCerraTurnoActionPerformed
        // 1. Verificación: Exigimos que hayan validado primero
        if (!jTextFEstado.getText().equalsIgnoreCase("Validado")) {
            javax.swing.JOptionPane.showMessageDialog(this, "Debe VALIDAR el arqueo antes de poder cerrar el turno.", "Acción denegada", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // 2. Extraer los números con seguridad
            double totalSist = Double.parseDouble(jTextFTotalSistema.getText().replace(",", "."));
            double totalMan = Double.parseDouble(jTextFIngresoManual.getText().replace(",", "."));

            // 3. Leemos quién es el usuario actual
            String empleado = controlador.SistemaHotel.getInstancia().getHotel().getRolActual();
            if (empleado == null || empleado.isEmpty()) {
                empleado = "Recepcionista";
            }

            // 4. Creamos el objeto con el constructor actualizado
            Entidades.TurnoCaja nuevoTurno = new Entidades.TurnoCaja(empleado, totalSist, totalMan);

            // 4.1 Si hay diferencia (faltante/sobrante), exigimos un motivo.
            double diferencia = totalMan - totalSist;
            String motivo = jTextFMotivoDiferencia.getText().trim();
            if (Math.abs(diferencia) > 0.001 && motivo.isEmpty()) {
                javax.swing.JOptionPane.showMessageDialog(this,
                        "Hay una diferencia de S/ " + String.format("%.2f", diferencia)
                        + ".\nIndique el motivo (por qué falta o sobra dinero) antes de cerrar.",
                        "Motivo requerido", javax.swing.JOptionPane.WARNING_MESSAGE);
                return;
            }
            nuevoTurno.setMotivo(motivo);

            // ---> MAGIA 2: Archivamos todas las fichas procesadas en este turno
            java.util.List<Entidades.FichaHospedaje> todasLasFichas = controlador.SistemaHotel.getInstancia().getHotel().getListaFHospedaje();
            if (todasLasFichas != null) {
                for (Entidades.FichaHospedaje f : todasLasFichas) {
                    if (!f.isArqueada()) {
                        f.setArqueada(true); // Se marcan como procesadas
                    }
                }
            }

            // 5. Guardamos todo permanentemente (El turno nuevo y el estado de las fichas)
            controlador.SistemaHotel.getInstancia().getHotel().getListaTurnos().add(nuevoTurno);
            controlador.SistemaHotel.getInstancia().guardarCambios();

            // 6. Bloqueo visual
            jTextFEstado.setText("Turno cerrado");
            jTextFIngresoManual.setEditable(false);
            jButtonValidar.setEnabled(false);
            jButtonCerraTurno.setEnabled(false);

            // 7. Alerta de despedida
            String mensajeCierre = "Turno cerrado exitosamente.\n"
                    + "Resultado: " + nuevoTurno.getEstado() + "\n"
                    + "Ya no se podrá modificar el ingreso manual.";

            javax.swing.JOptionPane.showMessageDialog(this, mensajeCierre, "Cierre de Caja", javax.swing.JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException e) {
            javax.swing.JOptionPane.showMessageDialog(this, "Asegúrese de que los montos sean números válidos.", "Error Numérico", javax.swing.JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(this, "Error al guardar el turno: " + e.getMessage(), "Error Crítico", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButtonCerraTurnoActionPerformed

    private void jTextFIngresoManualKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFIngresoManualKeyTyped
        // 1. Capturamos el carácter exacto de la tecla presionada
        char c = evt.getKeyChar();

        // 2. Verificamos si NO es un número y NO es un punto
        if (!Character.isDigit(c) && c != '.') {
            evt.consume(); // ¡Destruye la pulsación! La letra no aparecerá en pantalla.
            return;
        }

        // 3. Evitar que el usuario escriba más de un punto decimal
        if (c == '.' && jTextFIngresoManual.getText().contains(".")) {
            evt.consume(); // Destruye el segundo punto
        }
    }//GEN-LAST:event_jTextFIngresoManualKeyTyped

    private void jTextFTelefonoHKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFTelefonoHKeyTyped
        // 1. Capturamos el carácter exacto de la tecla presionada
        char c = evt.getKeyChar();

        //2. Solo permite digitos
        if (!Character.isDigit(c) && c != '.') {
            evt.consume();
            return;
        }
        // 3. Evitar que el usuario escriba más de un punto decimal
        if (c == '.' && jTextFTelefonoH.getText().contains(".")) {
            evt.consume(); // Destruye el segundo punto
        }
        //4. Bloque si hay mas de 9 digitos
        if (jTextFTelefonoH.getText().length() >= 9) {
            evt.consume();
        }
    }//GEN-LAST:event_jTextFTelefonoHKeyTyped

    private void jTextFNombreHKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFNombreHKeyTyped
        char c = evt.getKeyChar();

        //Bloquear el limite de caracteres
        if (jTextFNombreH.getText().length() >= 50) {
            evt.consume();
            return;
        }

        //Permitir Espacio
        if (c == ' ') {
            return;
        }

        //Solo letras()
        if (!Character.isLetter(c)) {
            evt.consume();
        }
    }//GEN-LAST:event_jTextFNombreHKeyTyped

    private void jTextFApellidosHKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFApellidosHKeyTyped

        char c = evt.getKeyChar();

        //Bloquear el limite de caracteres
        if (jTextFApellidosH.getText().length() >= 50) {
            evt.consume();
            return;
        }

        //Permitir Espacio
        if (c == ' ') {
            return;
        }

        //Solo letras()
        if (!Character.isLetter(c)) {
            evt.consume();
        }

    }//GEN-LAST:event_jTextFApellidosHKeyTyped

    private void jButtonAsignarKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jButtonAsignarKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonAsignarKeyTyped

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // El panel de pagos antiguo fue reemplazado por el panel F-010 (Pago de
        // servicios contratados). El botón "Pagos" del menú ahora lo muestra.
        mostrarCard(panelPagoServicios);
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Recepcionista.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Recepcionista.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Recepcionista.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Recepcionista.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Recepcionista().setVisible(true);
            }
        });
    }

    private void verificarSalidasAutomaticas() {
        boolean huboCambios = false;
        java.util.List<Entidades.FichaHospedaje> fichas = controlador.SistemaHotel.getInstancia().getHotel().getListaFHospedaje();

        if (fichas != null) {
            for (Entidades.FichaHospedaje ficha : fichas) {
                // Solo revisamos las habitaciones que están actualmente Ocupadas
                if (ficha.getHabitacion().getEstado() == 'O' && ficha.getFechaSalida() == null) {

                    // Traemos la fecha en la que entró y las noches que pagó
                    java.time.LocalDateTime fechaEntrada = ficha.getFechaIngreso();
                    int noches = ficha.getNochesEsperadas();

                    // El sistema calcula mágicamente la fecha y hora exacta en la que debe salir
                    java.time.LocalDateTime fechaSalidaEsperada = fechaEntrada.plusDays(noches);

                    // Si el reloj de la computadora actual ya pasó esa fecha de salida...
                    if (java.time.LocalDateTime.now().isAfter(fechaSalidaEsperada)) {

                        // 1. Mandamos la habitación a Mantenimiento automáticamente ('M')
                        ficha.getHabitacion().setEstado('M');

                        // 2. Finalizamos la ficha ('F') y le ponemos fecha de salida para tus reportes de Admin
                        ficha.setEstado('F');
                        ficha.setFechaSalida(java.time.LocalDateTime.now());

                        huboCambios = true;
                    }
                }
            }
        }

        // Si el sistema detectó que alguien ya se pasó de su fecha, guarda los cambios en silencio
        if (huboCambios) {
            controlador.SistemaHotel.getInstancia().guardarCambios();
        }
    }
// --- INICIO DEL CÓDIGO PARA DIBUJAR LOS CUADRITOS ---

    private void dibujarCuadriculaHabitaciones(String filtroFijado) {
        // 1. Limpiamos el lienzo por si había cuadritos dibujados antes
        jPanelCuadricula.removeAll();

        // FlowLayout con alineación a la izquierda y separaciones de 20 píxeles
        jPanelCuadricula.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 20, 20));

        // 2. Traemos la lista real de habitaciones desde el cerebro (SistemaHotel)
        java.util.List<Entidades.Habitacion> lista = controlador.SistemaHotel.getInstancia().getHotel().getListaHabitacion();

        // Si la lista está vacía, no hacemos nada y evitamos errores
        if (lista == null || lista.isEmpty()) {
            jPanelCuadricula.revalidate();
            jPanelCuadricula.repaint();
            return;
        }

        // 3. Recorremos la lista para dibujar cada habitación
        for (Entidades.Habitacion hab : lista) {

            // Traducir los chars a textos legibles para comparar con el ComboBox
            String estadoTexto = "";
            java.awt.Color colorBordeTexto;

            if (hab.getEstado() == 'D') {
                estadoTexto = "Disponibles";
                colorBordeTexto = new java.awt.Color(0, 153, 0); // Verde oscuro
            } else if (hab.getEstado() == 'O') {
                estadoTexto = "Ocupadas";
                colorBordeTexto = new java.awt.Color(204, 0, 0); // Rojo oscuro
            } else {
                estadoTexto = "En mantenimiento";
                colorBordeTexto = java.awt.Color.GRAY; // Gris
            }

            // 4. Lógica del Filtro: 
            if (filtroFijado.equals("Todas las opciones") || filtroFijado.equals(estadoTexto)) {

                // --- INICIO DE CREACIÓN DEL CUADRITO VISUAL ---
                javax.swing.JPanel miniPanel = new javax.swing.JPanel();
                miniPanel.setLayout(new javax.swing.BoxLayout(miniPanel, javax.swing.BoxLayout.Y_AXIS));
                miniPanel.setBackground(new java.awt.Color(245, 245, 245)); // Color hueso claro de fondo

                // (140 de ancho por 120 de alto)
                java.awt.Dimension tamañoCuadrado = new java.awt.Dimension(140, 120);
                miniPanel.setPreferredSize(tamañoCuadrado);
                miniPanel.setMinimumSize(tamañoCuadrado);
                miniPanel.setMaximumSize(tamañoCuadrado);

                // Le ponemos un borde del color de su estado (Verde o Rojo)
                miniPanel.setBorder(javax.swing.BorderFactory.createLineBorder(colorBordeTexto, 2));

                // Etiqueta 1: Número de habitación (Grande y en negrita)
                javax.swing.JLabel lblNum = new javax.swing.JLabel(hab.getNumero());
                lblNum.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 18));
                lblNum.setForeground(colorBordeTexto);
                lblNum.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);

                // Etiqueta 2: Tipo de habitación (Simple, Doble...)
                String tipoStr = (hab.getTipo() == 'S') ? "Simple" : (hab.getTipo() == 'D') ? "Doble" : "Matrimonial";
                javax.swing.JLabel lblTipo = new javax.swing.JLabel(tipoStr);
                lblTipo.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 12));
                lblTipo.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);

                // Etiqueta 3: Estado (Disponible / Ocupada)
                javax.swing.JLabel lblEstado = new javax.swing.JLabel(estadoTexto.substring(0, estadoTexto.length() - 1)); // Le quitamos la "s" final
                lblEstado.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12));
                lblEstado.setForeground(colorBordeTexto);
                lblEstado.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);

                // Agregamos las etiquetas al mini panel con un poco de espacio
                miniPanel.add(javax.swing.Box.createVerticalStrut(15)); // Un poquito más de espacio arriba
                miniPanel.add(lblNum);
                miniPanel.add(lblTipo);
                miniPanel.add(lblEstado);
                miniPanel.add(javax.swing.Box.createVerticalStrut(15)); // Un poquito más de espacio abajo

                // =======================================================================
                // ---> HACER QUE EL CUADRITO SEA CLICKEABLE CON TODOS LOS HUÉSPEDES <---
                miniPanel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR)); // Cambia el cursor a una manito 👆

                miniPanel.addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseClicked(java.awt.event.MouseEvent e) {

                        // Si la habitación está ocupada, buscamos quiénes están adentro
                        if (hab.getEstado() == 'O') {

                            java.util.List<Entidades.FichaHospedaje> fichas = controlador.SistemaHotel.getInstancia().getHotel().getListaFHospedaje();
                            Entidades.FichaHospedaje fichaEncontrada = null;

                            // Buscamos de atrás hacia adelante para agarrar la asignación más reciente activa
                            if (fichas != null) {
                                for (int i = fichas.size() - 1; i >= 0; i--) {
                                    if (fichas.get(i).getHabitacion().getNumero().equals(hab.getNumero())) {
                                        fichaEncontrada = fichas.get(i);
                                        break;
                                    }
                                }
                            }

                            // Si encontramos la ficha y contiene huéspedes, armamos el reporte completo
                            if (fichaEncontrada != null && !fichaEncontrada.getHuespedes().isEmpty()) {

                                StringBuilder sb = new StringBuilder();
                                sb.append("=========================================\n");
                                sb.append("     DETALLES DE LA HABITACIÓN ").append(hab.getNumero()).append("\n");
                                sb.append("=========================================\n");
                                sb.append("• Noches contratadas: ").append(fichaEncontrada.getNochesEsperadas()).append("\n");
                                sb.append("• Total ocupantes en cuarto: ").append(fichaEncontrada.getCantidadPersonas()).append("\n");
                                sb.append("------------------------------------------------------------------------\n");
                                sb.append("   LISTA DE PASAJEROS REGISTRADOS:\n");

                                int numPasajero = 1;
                                for (Entidades.Huesped huesped : fichaEncontrada.getHuespedes()) {
                                    sb.append("\n[Ocupante ").append(numPasajero).append(numPasajero == 1 ? " - TITULAR" : " - ACOMPAÑANTE").append("]\n");
                                    sb.append(" -> Nombre completo: ").append(huesped.getNombre()).append(" ").append(huesped.getApellido()).append("\n");
                                    sb.append(" -> Documento:       ").append(huesped.getTipoDocumento()).append(" / ").append(huesped.getNumDocumento()).append("\n");
                                    sb.append(" -> Celular:         ").append(huesped.getTelefono()).append("\n");
                                    numPasajero++;
                                }
                                sb.append("\n=========================================");

                                // Mostramos todo en un JTextArea con fuente Monoespaciada para que se vea súper ordenado
                                javax.swing.JTextArea txtReporte = new javax.swing.JTextArea(sb.toString());
                                txtReporte.setFont(new java.awt.Font("Monospaced", java.awt.Font.PLAIN, 13));
                                txtReporte.setEditable(false);
                                txtReporte.setBackground(new java.awt.Color(250, 250, 250));

                                javax.swing.JOptionPane.showMessageDialog(null, new javax.swing.JScrollPane(txtReporte), "Control de Ocupantes - Ley de Hospedaje", javax.swing.JOptionPane.PLAIN_MESSAGE);

                            } else {
                                javax.swing.JOptionPane.showMessageDialog(null, "Error: No se encontraron los datos del huésped para esta habitación.", "Datos no encontrados", javax.swing.JOptionPane.ERROR_MESSAGE);
                            }

                        } else if (hab.getEstado() == 'D') {
                            // Si le hacen clic a una disponible, le decimos que está libre
                            javax.swing.JOptionPane.showMessageDialog(null, "La habitación " + hab.getNumero() + " está DISPONIBLE y lista para asignarse.", "Información", javax.swing.JOptionPane.INFORMATION_MESSAGE);

                        } else {
                            // Si está en mantenimiento, se lo recordamos
                            javax.swing.JOptionPane.showMessageDialog(null, "La habitación " + hab.getNumero() + " se encuentra EN MANTENIMIENTO.", "Información", javax.swing.JOptionPane.WARNING_MESSAGE);
                        }
                    }
                });
                // =======================================================================

                // Agregamos el cuadrito terminado a nuestro lienzo principal
                jPanelCuadricula.add(miniPanel);
            }
        }

        // 5. Le decimos a Java que refresque la pantalla para mostrar los cambios
        jPanelCuadricula.revalidate();
        jPanelCuadricula.repaint();
    }

    private void cargarHabitacionesDisponibles() {
        // 1. Limpiamos cualquier dato viejo que tenga el ComboBox
        jComboBoxHabitacionDisponible.removeAllItems();

        // 2. Agregamos la opción por defecto al inicio
        jComboBoxHabitacionDisponible.addItem("Seleccione...");

        // 3. Traemos la lista real de habitaciones desde tu Singleton
        java.util.List<Entidades.Habitacion> lista = controlador.SistemaHotel.getInstancia().getHotel().getListaHabitacion();

        // 4. Recorremos la lista buscando SOLO las disponibles ('D')
        if (lista != null) {
            for (Entidades.Habitacion hab : lista) {
                if (hab.getEstado() == 'D') {
                    // Traducimos el char a texto para que se vea bonito
                    String tipoStr = (hab.getTipo() == 'S') ? "Simple" : (hab.getTipo() == 'D') ? "Doble" : "Matrimonial";

                    // Armamos el texto final, ej: "101 - Doble (Disponible)"
                    String item = hab.getNumero() + " - " + tipoStr + " (Disponible)";

                    // Lo metemos al ComboBox
                    jComboBoxHabitacionDisponible.addItem(item);
                }
            }
        }
    }

    private void iniciarArqueoCaja() {
        // 1. Configuración inicial de las casillas
        jTextFTotalSistema.setEditable(false);
        jTextFDiferenciaIngreso.setEditable(false);
        jTextFEstado.setEditable(false);

        jTextFIngresoManual.setEditable(true); // Esta es la única que el recepcionista puede tocar
        jTextFIngresoManual.setText("");
        jTextFDiferenciaIngreso.setText("0.00");
        jTextFEstado.setText("Pendiente de validar");

        // Habilitamos los botones por si estaban bloqueados de un turno anterior
        jButtonValidar.setEnabled(true);
        jButtonCerraTurno.setEnabled(true);

        // 2. Calcular todo el dinero registrado en el sistema.
        //    Total Sistema = suma del TOTAL DE CADA FICHA de hospedaje (no arqueada).
        //    Se usa calcularTotalFicha() -mismo cálculo que cobra F-010- para que
        //    ambos módulos estén relacionados: lo que el huésped paga en "Pago de
        //    servicios" es exactamente lo que aquí se acumula en el Total Sistema.
        double sumaTotalSistema = 0.0;
        java.util.List<Entidades.FichaHospedaje> listaFichas = controlador.SistemaHotel.getInstancia().getHotel().getListaFHospedaje();

        if (listaFichas != null) {
            for (Entidades.FichaHospedaje ficha : listaFichas) {
                // Solo sumamos las fichas que NO han sido cerradas en turnos anteriores
                if (!ficha.isArqueada()) {
                    sumaTotalSistema += ficha.calcularTotalFicha();
                }
            }
        }

        // 3. Mostramos el total en la casilla del sistema
        jTextFTotalSistema.setText(String.format("%.2f", sumaTotalSistema).replace(",", "."));

        // 4. Asegurar que el campo "Motivo de diferencia" esté visible bajo el
        //    recuadro de Estado del Turno. Se hace tras el ciclo de layout para
        //    que los sub-paneles ya tengan geometría real.
        javax.swing.SwingUtilities.invokeLater(this::integrarCampoMotivo);
        jTextFMotivoDiferencia.setText("");
    }

    /**
     * Inserta -una sola vez- el label y el JTextField de "Motivo de diferencia"
     * en el panel de arqueo, en una zona libre debajo del panel "Estado del
     * Turno" (sin modificar el layout de jPanel4, para no solaparlos). Texto
     * libre para explicar por qué falta o sobra dinero al cerrar caja.
     */
    private void integrarCampoMotivo() {
        if (jTextFMotivoDiferencia.getParent() != null) {
            return; // ya integrado
        }
        // Forzamos que el layout actual calcule las posiciones reales de los
        // sub-paneles ANTES de pasar a posición absoluta, y luego fijamos esos
        // bounds para que nada se mueva a (0,0).
        jPanelArqueo.doLayout();
        java.awt.Rectangle b1 = jPanel1.getBounds();
        java.awt.Rectangle b2 = jPanel2.getBounds();
        java.awt.Rectangle b3 = jPanel3.getBounds();
        java.awt.Rectangle b4 = jPanel4.getBounds();
        java.awt.Rectangle bV = jButtonValidar.getBounds();
        java.awt.Rectangle bC = jButtonCerraTurno.getBounds();

        // Si el panel aún no tiene geometría real, no reconstruimos (evita 0,0).
        if (b4.width == 0 || b4.height == 0) {
            return;
        }

        jPanelArqueo.setLayout(null);

        jPanel1.setBounds(b1);
        jPanel2.setBounds(b2);
        jPanel3.setBounds(b3);
        jPanel4.setBounds(b4);
        jButtonValidar.setBounds(bV);
        jButtonCerraTurno.setBounds(bC);

        javax.swing.JLabel lblMotivo = new javax.swing.JLabel("Motivo de la diferencia (si falta o sobra):");
        lblMotivo.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 13));
        lblMotivo.setForeground(java.awt.Color.BLACK);

        // Ubicado bajo el recuadro "Estado del Turno" (jPanel4), en su misma columna.
        int x = b4.x;
        int anchoCampo = Math.max(b4.width, 300);
        int y = b4.y + b4.height + 14;
        lblMotivo.setBounds(x, y, anchoCampo, 20);
        jTextFMotivoDiferencia.setBounds(x, y + 22, anchoCampo, 28);

        jPanelArqueo.add(lblMotivo);
        jPanelArqueo.add(jTextFMotivoDiferencia);
        jPanelArqueo.setComponentZOrder(lblMotivo, 0);
        jPanelArqueo.setComponentZOrder(jTextFMotivoDiferencia, 0);

        jPanelArqueo.revalidate();
        jPanelArqueo.repaint();
    }

    private void cargarDatos() {
        // 1. Definir columnas
        String[] columnas = {"Tipo de documento", "Nro. Documento", "Nombre", "Apellido", "Telefono", "Dirección"};

        // 2. Crear el modelo de tabla
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0);

        // 3. Obtener la lista de huéspedes desde tu instancia de hotel
        List<Huesped> lista = hotel.getListaHuespedes();

        // 4. Llenar el modelo
        for (Huesped h : lista) {
            Object[] fila = {
                h.getTipoDocumento(),
                h.getNumDocumento(),
                h.getNombre(),
                h.getApellido(),
                h.getTelefono(),
                h.getDireccion(),};
            modelo.addRow(fila);
        }

        // 5. Asignar el modelo a la tabla
        jTableListarH.setModel(modelo);
    }
    // --- FIN DEL CÓDIGO ---
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButtonArqueo;
    private javax.swing.JButton jButtonAsignar;
    private javax.swing.JButton jButtonAsignarHabitacion;
    private javax.swing.JButton jButtonBuscarH;
    private javax.swing.JButton jButtonCerraTurno;
    private javax.swing.JButton jButtonHabitaciones;
    private javax.swing.JButton jButtonImprimirVoucher;
    private javax.swing.JButton jButtonLimpiar;
    private javax.swing.JButton jButtonListarHuéspedes;
    private javax.swing.JButton jButtonRegistrar;
    private javax.swing.JButton jButtonRegistrarH;
    private javax.swing.JButton jButtonRegresar;
    private javax.swing.JButton jButtonValidar;
    private javax.swing.JCheckBox jCheckBoxAlmuerzo;
    private javax.swing.JCheckBox jCheckBoxCena;
    private javax.swing.JCheckBox jCheckBoxDesayuno;
    private javax.swing.JComboBox<String> jComboBTipoDocH;
    private javax.swing.JComboBox<String> jComboBoxHabitacionDisponible;
    private javax.swing.JComboBox<String> jComboBoxNroH;
    private javax.swing.JComboBox<String> jComboFIltroxEstado;
    private com.toedter.calendar.JDateChooser jDateChooserFechaIngreso;
    private com.toedter.calendar.JDateChooser jDateChooserFechaSalida;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanelArqueo;
    private javax.swing.JPanel jPanelAsignacion;
    private javax.swing.JPanel jPanelContenedor;
    private javax.swing.JPanel jPanelCuadricula;
    private javax.swing.JPanel jPanelHabitaciones;
    private javax.swing.JPanel jPanelListarHuespedes;
    private javax.swing.JPanel jPanelMenu;
    private javax.swing.JPanel jPanelRegistro;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPoneHabitaciones;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JTable jTableListarH;
    private javax.swing.JTextField jTextFApellidosH;
    private javax.swing.JTextField jTextFDatosH;
    private javax.swing.JTextField jTextFDiferenciaIngreso;
    private javax.swing.JTextField jTextFDireccionH;
    private javax.swing.JTextField jTextFEstado;
    private javax.swing.JTextField jTextFIngresoManual;
    private javax.swing.JTextField jTextFNombreH;
    private javax.swing.JTextField jTextFNroH;
    private javax.swing.JTextField jTextFNumDocH;
    private javax.swing.JTextField jTextFTelefonoH;
    private javax.swing.JTextField jTextFTotalSistema;
    // End of variables declaration//GEN-END:variables
}
