package Interfaz;

import Entidades.Habitacion;

import controlador.*;
import javax.swing.JOptionPane;

public class Habitaciones extends javax.swing.JFrame {

    private final Hotel hotel = SistemaHotel.getInstancia().getHotel(); // Usa el hotel compartido

    public Habitaciones() {
        initComponents();
        actualizarTablaHabitaciones();
        setLocationRelativeTo(null);
        jTextFPrecioHab.setEditable(false);
        this.setResizable(false);
    }

    // Este método va en tu JFrame, NO en la clase Hotel
    private void actualizarTablaHabitaciones() {
        // 1. Obtener el modelo visual de la tabla
        javax.swing.table.DefaultTableModel modelo = (javax.swing.table.DefaultTableModel) jTableHabitaciones.getModel();

        // 2. Limpiar la tabla visual (borrar todas las filas actuales para no duplicar)
        modelo.setRowCount(0);

        // 3. Pede a la clase Hotel la lista real de habitaciones
        java.util.List<Habitacion> lista = SistemaHotel.getInstancia().getHotel().getListaHabitacion();

        // 4. Recorrer la lista y dibujar fila por fila
        for (Habitacion hab : lista) {

            // Traducir los Char a palabras completas
            String tipoStr = (hab.getTipo() == 'S') ? "Simple" : (hab.getTipo() == 'D') ? "Doble" : "Matrimonial";
            String estadoStr = (hab.getEstado() == 'D') ? "Disponible" : (hab.getEstado() == 'O') ? "Ocupada" : "Mantenimiento";

            // Crear un arreglo con los datos en el orden de tus columnas
            Object[] fila = {
                hab.getNumero(),
                tipoStr,
                hab.getPrecio(),
                estadoStr
            };

            // Agregar la fila a la tabla visual
            modelo.addRow(fila);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jComboBox3 = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableHabitaciones = new javax.swing.JTable();
        jPanelTitulo = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jTextFNroHabitacion = new javax.swing.JTextField();
        jButtonBuscar = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jComboBTipoHab = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        jTextFPrecioHab = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jComboBEstadoHab = new javax.swing.JComboBox<>();
        jBEliminarHab = new javax.swing.JButton();
        jButton2Guardar = new javax.swing.JButton();
        jBActualizarHab = new javax.swing.JButton();
        jBLimpiarHab = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();

        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("REGISTRO DE HABITACIÓN");

        jTableHabitaciones.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Nro. Habitación", "Tipo", "Precio (S/.)", "Estado"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTableHabitaciones);

        jPanelTitulo.setBackground(new java.awt.Color(204, 204, 255));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setText("GESTIÓN DE HABITACIONES");

        javax.swing.GroupLayout jPanelTituloLayout = new javax.swing.GroupLayout(jPanelTitulo);
        jPanelTitulo.setLayout(jPanelTituloLayout);
        jPanelTituloLayout.setHorizontalGroup(
            jPanelTituloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelTituloLayout.createSequentialGroup()
                .addGap(292, 292, 292)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanelTituloLayout.setVerticalGroup(
            jPanelTituloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelTituloLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel1)
                .addContainerGap(21, Short.MAX_VALUE))
        );

        jPanel1.setBackground(new java.awt.Color(204, 204, 204));

        jLabel2.setText("Nro Habitación: ");

        jTextFNroHabitacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFNroHabitacionActionPerformed(evt);
            }
        });
        jTextFNroHabitacion.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFNroHabitacionKeyTyped(evt);
            }
        });

        jButtonBuscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/Buscar2.gif"))); // NOI18N
        jButtonBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBuscarActionPerformed(evt);
            }
        });

        jLabel3.setText("Tipo:");

        jComboBTipoHab.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccione", "Simple", "Doble", "Matrimonial" }));
        jComboBTipoHab.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBTipoHabActionPerformed(evt);
            }
        });

        jLabel4.setText("Precio por noche (S/.)");

        jTextFPrecioHab.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFPrecioHabActionPerformed(evt);
            }
        });

        jLabel5.setText("Estado:");

        jComboBEstadoHab.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccione", "Disponible", "Mantenimiento" }));
        jComboBEstadoHab.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBEstadoHabActionPerformed(evt);
            }
        });

        jBEliminarHab.setBackground(new java.awt.Color(204, 204, 255));
        jBEliminarHab.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/Eliminar.png"))); // NOI18N
        jBEliminarHab.setText("Eliminar");
        jBEliminarHab.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBEliminarHabActionPerformed(evt);
            }
        });

        jButton2Guardar.setBackground(new java.awt.Color(204, 204, 255));
        jButton2Guardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/Guardar.gif"))); // NOI18N
        jButton2Guardar.setText("Registrar");
        jButton2Guardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2GuardarActionPerformed(evt);
            }
        });

        jBActualizarHab.setBackground(new java.awt.Color(204, 204, 255));
        jBActualizarHab.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/Edit_icon-icons.com_71853.png"))); // NOI18N
        jBActualizarHab.setText("Actualizar");
        jBActualizarHab.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBActualizarHabActionPerformed(evt);
            }
        });

        jBLimpiarHab.setBackground(new java.awt.Color(204, 204, 255));
        jBLimpiarHab.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/Retro.png"))); // NOI18N
        jBLimpiarHab.setText("Limpiar");
        jBLimpiarHab.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBLimpiarHabActionPerformed(evt);
            }
        });

        jButton1.setBackground(new java.awt.Color(51, 51, 255));
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/Regresar.png"))); // NOI18N
        jButton1.setText("Regresar ");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addGap(83, 83, 83)
                                .addComponent(jComboBEstadoHab, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel4))
                                .addGap(5, 5, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextFPrecioHab, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jTextFNroHabitacion, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jButtonBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jComboBTipoHab, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton1)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jButton2Guardar)
                                .addComponent(jBActualizarHab)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jBLimpiarHab)
                            .addComponent(jBEliminarHab))))
                .addGap(36, 36, 36))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButtonBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTextFNroHabitacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jComboBTipoHab, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(22, 22, 22)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFPrecioHab, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(jComboBEstadoHab, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2Guardar)
                    .addComponent(jBEliminarHab))
                .addGap(31, 31, 31)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jBActualizarHab)
                    .addComponent(jBLimpiarHab))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addGap(26, 26, 26))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 575, Short.MAX_VALUE))
            .addComponent(jPanelTitulo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanelTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 515, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // Ir a menú principal
        new Menu_principal().setVisible(true);
        dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTextFNroHabitacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFNroHabitacionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFNroHabitacionActionPerformed

    private void jTextFPrecioHabActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFPrecioHabActionPerformed
        // caja de precio
    }//GEN-LAST:event_jTextFPrecioHabActionPerformed

    private void jComboBTipoHabActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBTipoHabActionPerformed
        // Prueba precio 
        String selected = (String) jComboBTipoHab.getSelectedItem();

        // Puedes hacer una lógica más avanzada aquí si quieres
        switch (selected) {
            case "Seleccione":
                jTextFPrecioHab.setText("");
                break;
            case "Simple":
                jTextFPrecioHab.setText("50");
                break;
            case "Doble":
                jTextFPrecioHab.setText("100");
                break;
            case "Matrimonial":
                jTextFPrecioHab.setText("150");
                break;
            default:
                jTextFPrecioHab.setText("");
                break;
        }

    }//GEN-LAST:event_jComboBTipoHabActionPerformed

    private void jButton2GuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2GuardarActionPerformed

        try {
            // 1. Capturar datos
            String numero = jTextFNroHabitacion.getText().trim();
            String precioStr = jTextFPrecioHab.getText().trim();

            // AGREGAMOS .trim() para limpiar espacios invisibles (ej: "Simple " -> "Simple")
            String tipoSeleccionado = jComboBTipoHab.getSelectedItem().toString().trim();
            String estadoSeleccionado = jComboBEstadoHab.getSelectedItem().toString().trim();

            // 2. Validar campos vacíos o sin seleccionar
            if (numero.isEmpty() || precioStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe ingresar el número y el precio de la habitación.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Usamos equalsIgnoreCase para que no le importe si dice "seleccione...", "Seleccione..." o "SELECCIONE..."
            if (tipoSeleccionado.equalsIgnoreCase("Seleccione") || estadoSeleccionado.equalsIgnoreCase("Seleccione")) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar un tipo y un estado para la habitación.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 3. Validar si la habitación ya existe
            if (SistemaHotel.getInstancia().getHotel().buscarHabitacionporNumero(numero) != null) {
                JOptionPane.showMessageDialog(this, "La habitación " + numero + " ya está registrada en el sistema.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 4. Convertir Tipo de String a Char (S, D, M)
            char tipo;

            // Quitamos el toLowerCase() para que compare tal cual viene del ComboBox
            switch (tipoSeleccionado) {
                case "Simple":
                    tipo = 'S';
                    break;
                case "Doble":
                    tipo = 'D';
                    break;
                case "Matrimonial":
                    tipo = 'M';
                    break;
                default:
                    JOptionPane.showMessageDialog(this, "Tipo de habitación no reconocido: [" + tipoSeleccionado + "]", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
            }
            // 5. Convertir Estado de String a Char (D, M)
            char estado = estadoSeleccionado.equalsIgnoreCase("Disponible") ? 'D' : 'M';

            // 6. Convertir Precio a double
            double precioNoche = Double.parseDouble(precioStr);

            if (precioNoche <= 0) {
                JOptionPane.showMessageDialog(this, "El precio por noche debe ser mayor a 0.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 7. Crear el objeto Habitación
            Habitacion hab = new Habitacion(numero, tipo, estado, precioNoche);

            // 8. Registrar usando el Singleton
            SistemaHotel.getInstancia().getHotel().registrarHabitacion(hab);

            // 9. Guardar los cambios físicos
            SistemaHotel.getInstancia().guardarCambios();

            JOptionPane.showMessageDialog(this, "Habitación registrada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

            actualizarTablaHabitaciones();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El precio por noche debe ser un número válido (ej: 150.50).", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ocurrió un error al registrar: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_jButton2GuardarActionPerformed

    private void jComboBEstadoHabActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBEstadoHabActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBEstadoHabActionPerformed

    private void jBActualizarHabActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBActualizarHabActionPerformed
        try {
            // 1. Capturar el NÚMERO 
            String numero = jTextFNroHabitacion.getText().trim();

            if (numero.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe buscar o ingresar el número de la habitación a actualizar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // 2. Capturar el resto de datos
            String precioStr = jTextFPrecioHab.getText().trim();
            String tipoSeleccionado = jComboBTipoHab.getSelectedItem().toString();
            String estadoSeleccionado = jComboBEstadoHab.getSelectedItem().toString();

            // Validaciones básicas de campos vacíos
            if (precioStr.isEmpty() || tipoSeleccionado.equals("Seleccione...") || estadoSeleccionado.equals("Seleccione...")) {
                JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos y seleccione un tipo y estado.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 3. Convertir Tipo a Char (SOLO: Simple, Doble, Matrimonial)
            char tipo;
            switch (tipoSeleccionado) {
                case "Simple":
                    tipo = 'S';
                    break;
                case "Doble":
                    tipo = 'D';
                    break;
                case "Matrimonial":
                    tipo = 'M';
                    break;
                default:
                    JOptionPane.showMessageDialog(this, "Tipo de habitación no reconocido.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
            }

            // 4. Convertir Estado a Char (D = Disponible, M = Mantenimiento)
            char estado = estadoSeleccionado.equals("Disponible") ? 'D' : 'M';

            // 5. Convertir Precio a double
            double precioNoche = Double.parseDouble(precioStr);
            if (precioNoche <= 0) {
                JOptionPane.showMessageDialog(this, "El precio por noche debe ser mayor a 0.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 6. Crear el objeto Habitación con los datos modificados
            Habitacion habActualizada = new Habitacion(numero, tipo, estado, precioNoche);

            // 7. Enviar a actualizar en el sistema
            boolean actualizado = SistemaHotel.getInstancia().getHotel().actualizarHabitacion(habActualizada);

            if (actualizado) {
                // 8. ¡GUARDAR EN MEMORIA!
                SistemaHotel.getInstancia().guardarCambios();

                JOptionPane.showMessageDialog(this, "Habitación actualizada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

                // 9. Limpiar los campos
                jTextFNroHabitacion.setText("");
                jTextFPrecioHab.setText("");
                jComboBTipoHab.setSelectedIndex(0);
                jComboBEstadoHab.setSelectedIndex(0);

                jTextFNroHabitacion.requestFocus();
                actualizarTablaHabitaciones();

            } else {
                JOptionPane.showMessageDialog(this, "No se encontró la habitación número " + numero + " en el sistema.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El precio por noche debe ser un número válido (ej: 150.50).", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ocurrió un error al actualizar: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jBActualizarHabActionPerformed

    private void jButtonBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBuscarActionPerformed
        // 1. Obtener el número de habitación ingresado
        String numero = jTextFNroHabitacion.getText().trim();

        if (numero.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese el número de la habitación a buscar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 2. Usar tu método buscarHabitacionporNumero para buscar en el sistema
        Habitacion hab = SistemaHotel.getInstancia().getHotel().buscarHabitacionporNumero(numero);

        // 3. Validar si la encontró y llenar las casillas
        if (hab != null) {

            // Llenar el Precio
            jTextFPrecioHab.setText(String.valueOf(hab.getPrecio()));

            // Llenar el Tipo (S, D, M)
            switch (hab.getTipo()) {
                case 'S':
                    jComboBTipoHab.setSelectedItem("Simple");
                    break;
                case 'D':
                    jComboBTipoHab.setSelectedItem("Doble");
                    break;
                case 'M':
                    jComboBTipoHab.setSelectedItem("Matrimonial");
                    break;
                default:
                    jComboBTipoHab.setSelectedIndex(0); // Vuelve a "Seleccione..." si hay un error
            }

            // Llenar el Estado (D, M, O)
            switch (hab.getEstado()) {
                case 'D':
                    jComboBEstadoHab.setSelectedItem("Disponible");
                    break;
                case 'M':
                    jComboBEstadoHab.setSelectedItem("Mantenimiento");
                    break;
                case 'O':
                    // Aviso importante de seguridad
                    JOptionPane.showMessageDialog(this, "Esta habitación está actualmente OCUPADA.\nEvite cambiar su estado manualmente.", "Información", JOptionPane.INFORMATION_MESSAGE);

                    // jComboBEstadoHab.setSelectedItem("Ocupada"); 
                    break;
                default:
                    jComboBEstadoHab.setSelectedIndex(0);
            }

        } else {
            // 4. Si la habitación es nula (no se encontró), limpiamos los otros campos y mostramos error
            jTextFPrecioHab.setText("");
            jComboBTipoHab.setSelectedIndex(0);
            jComboBEstadoHab.setSelectedIndex(0);

            JOptionPane.showMessageDialog(this, "No se encontró ninguna habitación con el número: " + numero, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButtonBuscarActionPerformed

    private void jBEliminarHabActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBEliminarHabActionPerformed
        try {
            // 1. Obtener el número de habitación desde la caja de texto
            String numero = jTextFNroHabitacion.getText().trim();

            if (numero.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Primero debe buscar la habitación que desea eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // 2. Buscar la habitación para validar su estado actual
            Habitacion hab = SistemaHotel.getInstancia().getHotel().buscarHabitacionporNumero(numero);

            if (hab == null) {
                JOptionPane.showMessageDialog(this, "La habitación " + numero + " no existe en el sistema.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 3. No se puede borrar una habitación Ocupada ('O')
            if (hab.getEstado() == 'O') {
                JOptionPane.showMessageDialog(this, "¡ALERTA!\nNo puede eliminar esta habitación porque actualmente está OCUPADA por huéspedes.", "Acción Bloqueada", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 4. Pedir confirmación de seguridad al Administrador
            int confirmacion = JOptionPane.showConfirmDialog(this,
                    "¿Está completamente seguro de que desea ELIMINAR la habitación " + numero + "?\nEsta acción no se puede deshacer.",
                    "Confirmar Eliminación",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            // 5. Si el Admin presiona "Sí" (YES_OPTION)
            if (confirmacion == JOptionPane.YES_OPTION) {

                //  Simplemente llamamos a tu método void
                SistemaHotel.getInstancia().getHotel().eliminarHabitacion(numero);

                // ¡GUARDAR EN MEMORIA! Aseguramos que el .dat se actualice
                SistemaHotel.getInstancia().guardarCambios();

                JOptionPane.showMessageDialog(this, "Habitación eliminada exitosamente del sistema.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

                // Limpiar los campos para evitar datos fantasma
                jTextFNroHabitacion.setText("");
                jTextFPrecioHab.setText("");
                jComboBTipoHab.setSelectedIndex(0);
                jComboBEstadoHab.setSelectedIndex(0);
                actualizarTablaHabitaciones();
            }
            // Si elige "No", el programa simplemente no hace nada y cierra el cuadro de diálogo.

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ocurrió un error al intentar eliminar: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jBEliminarHabActionPerformed

    private void jBLimpiarHabActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBLimpiarHabActionPerformed
        // 1. Limpiar las cajas de texto
        jTextFNroHabitacion.setText("");
        jTextFPrecioHab.setText("");

        // 2. Reiniciar los menús desplegables a la opción por defecto (índice 0 = "Seleccione...")
        jComboBTipoHab.setSelectedIndex(0);
        jComboBEstadoHab.setSelectedIndex(0);

        // 3.  Mueve el cursor automáticamente a la casilla del número
        jTextFNroHabitacion.requestFocus();
    }//GEN-LAST:event_jBLimpiarHabActionPerformed

    private void jTextFNroHabitacionKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFNroHabitacionKeyTyped
        // 1. Capturamos el carácter exacto de la tecla presionada
        char c = evt.getKeyChar();

        // 2. Verificamos si NO es un número y NO es un punto
        if (!Character.isDigit(c) && c != '.') {
            evt.consume(); // ¡Destruye la pulsación! La letra no aparecerá en pantalla.
            return;
        }

        // 3. Evitar que el usuario escriba más de un punto decimal
        if (c == '.' && jTextFNroHabitacion.getText().contains(".")) {
            evt.consume(); // Destruye el segundo punto
        }

        //4. Bloque si hay mas de 4 digitos
        if (jTextFNroHabitacion.getText().length() >= 4) {
            evt.consume();
        }
    }//GEN-LAST:event_jTextFNroHabitacionKeyTyped

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
            java.util.logging.Logger.getLogger(Habitaciones.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Habitaciones.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Habitaciones.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Habitaciones.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Habitaciones().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBActualizarHab;
    private javax.swing.JButton jBEliminarHab;
    private javax.swing.JButton jBLimpiarHab;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2Guardar;
    private javax.swing.JButton jButtonBuscar;
    private javax.swing.JComboBox<String> jComboBEstadoHab;
    private javax.swing.JComboBox<String> jComboBTipoHab;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanelTitulo;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableHabitaciones;
    private javax.swing.JTextField jTextFNroHabitacion;
    private javax.swing.JTextField jTextFPrecioHab;
    // End of variables declaration//GEN-END:variables
}
