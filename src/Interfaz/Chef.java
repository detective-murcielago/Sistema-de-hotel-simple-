package Interfaz;

/**
 *
 * @author tonyl
 */
public class Chef extends javax.swing.JFrame {

    public Chef() {
        initComponents();
        cargarTablaPedidos();
        setLocationRelativeTo(null);
        this.setResizable(false);
        agregarBotonConsumo();
    }

    /**
     * Agrega (por código, fuera del GEN-code) una barra de menú con la opción
     * para registrar los insumos de COCINA usados al preparar un pedido. Cada
     * registro descuenta stock real del inventario (productos tipo "Alimentos").
     */
    private void agregarBotonConsumo() {
        javax.swing.JMenuBar barraMenu = new javax.swing.JMenuBar();
        javax.swing.JMenu menu = new javax.swing.JMenu("Inventario");
        javax.swing.JMenuItem item = new javax.swing.JMenuItem("Registrar insumos usados...");
        item.setToolTipText("Descontar del inventario los alimentos usados para preparar el pedido");
        item.addActionListener(e -> {
            String ref = jTextFNroHabi.getText().trim();
            DialogoConsumoInsumos d = new DialogoConsumoInsumos(
                    this, "COCINA", "Alimentos", "Nro Habitación / Pedido", "Chef");
            d.setTipoFiltro("Alimentos");
            if (!ref.isEmpty()) {
                d.prellenarReferencia(ref);
            }
            d.setVisible(true);
        });
        menu.add(item);
        barraMenu.add(menu);
        setJMenuBar(barraMenu);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTablePedidos = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jTextFNroHabi = new javax.swing.JTextField();
        jButtonBuscarHabitacion = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jTextFDesayuno = new javax.swing.JTextField();
        jTextFAlmuerzo = new javax.swing.JTextField();
        jTextFCena = new javax.swing.JTextField();
        jButtonEntregado = new javax.swing.JButton();
        jButtonSalir = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(102, 102, 255));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setText("GESTIÓN DE PEDIDOS");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(302, 302, 302)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jLabel1)
                .addContainerGap(38, Short.MAX_VALUE))
        );

        jTablePedidos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Nro. Habitación", "Desayuno", "Almuerzo", "Cena ", "Estado"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTablePedidos);

        jLabel2.setText("Nro. Habitación");

        jTextFNroHabi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFNroHabiActionPerformed(evt);
            }
        });
        jTextFNroHabi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFNroHabiKeyTyped(evt);
            }
        });

        jButtonBuscarHabitacion.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/Buscar2.gif"))); // NOI18N
        jButtonBuscarHabitacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBuscarHabitacionActionPerformed(evt);
            }
        });

        jLabel4.setText("Desayunos:");

        jLabel5.setText("Almuerzos:");

        jLabel6.setText("Cenas:");

        jTextFDesayuno.setEditable(false);

        jTextFAlmuerzo.setEditable(false);

        jTextFCena.setEditable(false);

        jButtonEntregado.setBackground(new java.awt.Color(153, 255, 153));
        jButtonEntregado.setText("ENTREGADO");
        jButtonEntregado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonEntregadoActionPerformed(evt);
            }
        });

        jButtonSalir.setText("Salir");
        jButtonSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSalirActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextFNroHabi, javax.swing.GroupLayout.DEFAULT_SIZE, 78, Short.MAX_VALUE)
                            .addComponent(jTextFDesayuno)
                            .addComponent(jTextFAlmuerzo)
                            .addComponent(jTextFCena))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButtonBuscarHabitacion, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(68, 68, 68)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButtonEntregado, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButtonSalir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 57, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 589, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(43, 43, 43)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButtonBuscarHabitacion)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel2)
                                .addComponent(jTextFNroHabi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jTextFDesayuno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jTextFAlmuerzo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(16, 16, 16)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextFCena, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6))
                        .addGap(44, 44, 44)
                        .addComponent(jButtonEntregado)
                        .addGap(63, 63, 63)
                        .addComponent(jButtonSalir))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 436, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 37, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextFNroHabiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFNroHabiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFNroHabiActionPerformed

    private void jButtonEntregadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonEntregadoActionPerformed

        String numHab = jTextFNroHabi.getText().trim();

        if (numHab.isEmpty() || jTextFDesayuno.getText().isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this, "Primero busque una habitación válida.", "Aviso", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Buscamos la ficha nuevamente para actualizarla
        Entidades.FichaHospedaje fichaParaActualizar = null;
        for (Entidades.FichaHospedaje ficha : controlador.SistemaHotel.getInstancia().getHotel().getListaFHospedaje()) {
            if (ficha.getEstado() == 'A' && ficha.getHabitacion().getNumero().equals(numHab)) {
                fichaParaActualizar = ficha;
                break;
            }
        }

        if (fichaParaActualizar != null) {
            // Comprobamos que el pedido no esté ya entregado
            if (fichaParaActualizar.getEstadoComida().equals("Entregado")) {
                javax.swing.JOptionPane.showMessageDialog(this, "El pedido de esta habitación ya figura como ENTREGADO.", "Aviso", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // Cambiamos el estado, guardamos en archivo .dat y actualizamos
            fichaParaActualizar.setEstadoComida("Entregado");
            controlador.SistemaHotel.getInstancia().guardarCambios();

            // Limpiamos los campos visuales
            jTextFNroHabi.setText("");
            jTextFDesayuno.setText("");
            jTextFAlmuerzo.setText("");
            jTextFCena.setText("");

            // ¡Recargamos la tabla para que se vea el cambio en tiempo real!
            cargarTablaPedidos();

            javax.swing.JOptionPane.showMessageDialog(this, "¡Pedido marcado como Entregado!", "Éxito", javax.swing.JOptionPane.INFORMATION_MESSAGE);
        }

    }//GEN-LAST:event_jButtonEntregadoActionPerformed

    private void jButtonSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSalirActionPerformed
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
    }//GEN-LAST:event_jButtonSalirActionPerformed

    private void jButtonBuscarHabitacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBuscarHabitacionActionPerformed

        String numHab = jTextFNroHabi.getText().trim();

        if (numHab.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this, "Ingrese un número de habitación.", "Aviso", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Buscamos la ficha ACTIVA de esa habitación
        Entidades.FichaHospedaje fichaEncontrada = null;
        for (Entidades.FichaHospedaje ficha : controlador.SistemaHotel.getInstancia().getHotel().getListaFHospedaje()) {
            if (ficha.getEstado() == 'A' && ficha.getHabitacion().getNumero().equals(numHab)) {
                fichaEncontrada = ficha;
                break;
            }
        }

        if (fichaEncontrada == null) {
            javax.swing.JOptionPane.showMessageDialog(this, "No hay ningún huésped actual en la habitación " + numHab + ".", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Si lo encuentra, llenamos las casillas con las cantidades
        int personas = fichaEncontrada.getCantidadPersonas();
        jTextFDesayuno.setText(fichaEncontrada.isIncluyeDesayuno() ? String.valueOf(personas) : "0");
        jTextFAlmuerzo.setText(fichaEncontrada.isIncluyeAlmuerzo() ? String.valueOf(personas) : "0");
        jTextFCena.setText(fichaEncontrada.isIncluyeCena() ? String.valueOf(personas) : "0");

    }//GEN-LAST:event_jButtonBuscarHabitacionActionPerformed

    private void jTextFNroHabiKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFNroHabiKeyTyped
        // 1. Capturamos el carácter exacto de la tecla presionada
        char c = evt.getKeyChar();

        // 2. Verificamos si NO es un número y NO es un punto
        if (!Character.isDigit(c) && c != '.') {
            evt.consume(); // ¡Destruye la pulsación! La letra no aparecerá en pantalla.
            return;
        }

        // 3. Evitar que el usuario escriba más de un punto decimal
        if (c == '.' && jTextFNroHabi.getText().contains(".")) {
            evt.consume(); // Destruye el segundo punto
        }

        //4. Bloque si hay mas de 4 digitos
        if (jTextFNroHabi.getText().length() >= 4) {
            evt.consume();
        }
    }//GEN-LAST:event_jTextFNroHabiKeyTyped

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
            java.util.logging.Logger.getLogger(Chef.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Chef.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Chef.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Chef.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Chef().setVisible(true);
            }
        });
    }

    //metodos
    private void cargarTablaPedidos() {
        javax.swing.table.DefaultTableModel modelo = (javax.swing.table.DefaultTableModel) jTablePedidos.getModel();
        modelo.setRowCount(0); // Limpiar la tabla

        // Obtenemos la lista general
        java.util.List<Entidades.FichaHospedaje> listaFichas = controlador.SistemaHotel.getInstancia().getHotel().getListaFHospedaje();

        if (listaFichas != null) {
            for (Entidades.FichaHospedaje ficha : listaFichas) {
                // Filtramos: Solo huéspedes actuales ('A') y que tengan al menos una comida marcada
                if (ficha.getEstado() == 'A' && (ficha.isIncluyeDesayuno() || ficha.isIncluyeAlmuerzo() || ficha.isIncluyeCena())) {

                    int personas = ficha.getCantidadPersonas();

                    // Calculamos porciones: Si marcó la comida, le tocan tantas como personas haya
                    String desayuno = ficha.isIncluyeDesayuno() ? String.valueOf(personas) : "0";
                    String almuerzo = ficha.isIncluyeAlmuerzo() ? String.valueOf(personas) : "0";
                    String cena = ficha.isIncluyeCena() ? String.valueOf(personas) : "0";

                    // Agregamos la fila
                    Object[] fila = {
                        ficha.getHabitacion().getNumero(),
                        desayuno,
                        almuerzo,
                        cena,
                        ficha.getEstadoComida() // "Por entregar" o "Entregado"
                    };
                    modelo.addRow(fila);
                }
            }
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonBuscarHabitacion;
    private javax.swing.JButton jButtonEntregado;
    private javax.swing.JButton jButtonSalir;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTablePedidos;
    private javax.swing.JTextField jTextFAlmuerzo;
    private javax.swing.JTextField jTextFCena;
    private javax.swing.JTextField jTextFDesayuno;
    private javax.swing.JTextField jTextFNroHabi;
    // End of variables declaration//GEN-END:variables
}
