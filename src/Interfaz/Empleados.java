package Interfaz;

import Entidades.Empleado;
import javax.swing.*;
import controlador.*;

public class Empleados extends javax.swing.JFrame {

    public Empleados() {
        initComponents();
        setLocationRelativeTo(null);
        this.setResizable(false);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jRadioButton1 = new javax.swing.JRadioButton();
        jPasswordField1 = new javax.swing.JPasswordField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTextFNombreEmp = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jTextFApellidosEmp = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jTextFNumDocumentoEmp = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jTextFONOEmp = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jTextFIDEmp = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jButtonGuardar2 = new javax.swing.JButton();
        jButtonRegresar2 = new javax.swing.JButton();
        jComboBoxRolEmp = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        jTextFSueldoEmp = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jDateInicio = new com.toedter.calendar.JDateChooser();
        jLabel10 = new javax.swing.JLabel();
        jDateFin = new com.toedter.calendar.JDateChooser();
        jLabel11 = new javax.swing.JLabel();
        jTextFCorreo = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jComboBoxTipoDoc = new javax.swing.JComboBox<>();
        jLabel14 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jTextFDireccionEmp = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jTextFIdBuscar = new javax.swing.JTextField();
        JButtonBuscarEmp = new javax.swing.JButton();
        jButtonActualizarEmp = new javax.swing.JButton();
        jButtonEliminarEmp = new javax.swing.JButton();
        jButtonLimpiar = new javax.swing.JButton();

        jRadioButton1.setText("jRadioButton1");

        jPasswordField1.setText("jPasswordField1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("REGISTRO DE EMPLEADO");

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setText("DATOS DEL EMPLEADO");

        jLabel2.setText("Nombres:");

        jTextFNombreEmp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFNombreEmpActionPerformed(evt);
            }
        });
        jTextFNombreEmp.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFNombreEmpKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFNombreEmpKeyTyped(evt);
            }
        });

        jLabel3.setText("Apellidos: ");

        jTextFApellidosEmp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFApellidosEmpActionPerformed(evt);
            }
        });
        jTextFApellidosEmp.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFApellidosEmpKeyTyped(evt);
            }
        });

        jLabel4.setText("Nro. Documento:");

        jTextFNumDocumentoEmp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFNumDocumentoEmpActionPerformed(evt);
            }
        });

        jLabel5.setText("Nro Teléfono:");

        jTextFONOEmp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFONOEmpActionPerformed(evt);
            }
        });
        jTextFONOEmp.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFONOEmpKeyTyped(evt);
            }
        });

        jLabel6.setText("ID:");

        jTextFIDEmp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFIDEmpActionPerformed(evt);
            }
        });
        jTextFIDEmp.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFIDEmpKeyTyped(evt);
            }
        });

        jLabel7.setText("Rol:");

        jButtonGuardar2.setBackground(new java.awt.Color(102, 255, 102));
        jButtonGuardar2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/Guardar.gif"))); // NOI18N
        jButtonGuardar2.setText("Registrar");
        jButtonGuardar2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonGuardar2ActionPerformed(evt);
            }
        });

        jButtonRegresar2.setBackground(new java.awt.Color(51, 51, 255));
        jButtonRegresar2.setForeground(new java.awt.Color(255, 255, 255));
        jButtonRegresar2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/Regresar.png"))); // NOI18N
        jButtonRegresar2.setText("Regresar");
        jButtonRegresar2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRegresar2ActionPerformed(evt);
            }
        });

        jComboBoxRolEmp.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccione", "Recepcionista", "Gerente de compras", "Gerente de almacen", "Chef", "Limpieza", "Gerente de RRHH", "Gerente General" }));
        jComboBoxRolEmp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxRolEmpActionPerformed(evt);
            }
        });

        jLabel8.setText("Sueldo:");

        jTextFSueldoEmp.setEditable(false);
        jTextFSueldoEmp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFSueldoEmpActionPerformed(evt);
            }
        });

        jLabel9.setText("Inicio de contrato: ");

        jDateInicio.setMaxSelectableDate(new java.util.Date(253370786506000L));
        jDateInicio.setMinSelectableDate(new java.util.Date());

        jLabel10.setText("Fin de contrato:");

        jDateFin.setMinSelectableDate(new java.util.Date());

        jLabel11.setText("Correo: ");

        jTextFCorreo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFCorreoActionPerformed(evt);
            }
        });

        jLabel13.setText("Tipo de documento: ");

        jComboBoxTipoDoc.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccione", "DNI", "Carnet de extranjería" }));
        jComboBoxTipoDoc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxTipoDocActionPerformed(evt);
            }
        });

        jLabel14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/empleado.png"))); // NOI18N

        jLabel12.setText("Dirección: ");

        jTextFDireccionEmp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFDireccionEmpActionPerformed(evt);
            }
        });

        jLabel15.setText("Buscar por ID: ");

        jTextFIdBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFIdBuscarActionPerformed(evt);
            }
        });
        jTextFIdBuscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFIdBuscarKeyTyped(evt);
            }
        });

        JButtonBuscarEmp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/Buscar.gif"))); // NOI18N
        JButtonBuscarEmp.setText("Buscar");
        JButtonBuscarEmp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JButtonBuscarEmpActionPerformed(evt);
            }
        });

        jButtonActualizarEmp.setBackground(new java.awt.Color(153, 255, 204));
        jButtonActualizarEmp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/Edit_icon-icons.com_71853.png"))); // NOI18N
        jButtonActualizarEmp.setText("Actualizar");
        jButtonActualizarEmp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonActualizarEmpActionPerformed(evt);
            }
        });

        jButtonEliminarEmp.setBackground(new java.awt.Color(255, 51, 51));
        jButtonEliminarEmp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/Eliminar.png"))); // NOI18N
        jButtonEliminarEmp.setText("Eliminar");
        jButtonEliminarEmp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonEliminarEmpActionPerformed(evt);
            }
        });

        jButtonLimpiar.setBackground(new java.awt.Color(0, 102, 102));
        jButtonLimpiar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/iconos/Limpiar.png"))); // NOI18N
        jButtonLimpiar.setText("Limpiar");
        jButtonLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLimpiarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(98, 98, 98)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButtonGuardar2)
                                .addGap(29, 29, 29)
                                .addComponent(jButtonActualizarEmp)
                                .addGap(30, 30, 30)
                                .addComponent(jButtonEliminarEmp))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel15)
                                .addGap(35, 35, 35)
                                .addComponent(jTextFIdBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(59, 59, 59)
                                .addComponent(JButtonBuscarEmp))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(33, 33, 33)
                                .addComponent(jButtonLimpiar)
                                .addGap(32, 32, 32)
                                .addComponent(jButtonRegresar2))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(65, 65, 65)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel13))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextFONOEmp, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jComboBoxRolEmp, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTextFIDEmp, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTextFNombreEmp, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jComboBoxTipoDoc, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addGap(35, 35, 35)
                                .addComponent(jDateInicio, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(85, 85, 85)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel12)
                                    .addComponent(jLabel10))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(13, 13, 13)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jTextFApellidosEmp, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jTextFSueldoEmp, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jTextFDireccionEmp, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jDateFin, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextFCorreo, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTextFNumDocumentoEmp, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(351, 351, 351)
                        .addComponent(jLabel14))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(281, 281, 281)
                        .addComponent(jLabel1)))
                .addContainerGap(57, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFIdBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(JButtonBuscarEmp)
                    .addComponent(jLabel15))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 38, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBoxTipoDoc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13)
                    .addComponent(jTextFNumDocumentoEmp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextFNombreEmp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addGap(18, 18, 18))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextFApellidosEmp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addGap(28, 28, 28)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jTextFIDEmp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING))
                            .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jComboBoxRolEmp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel7))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jTextFONOEmp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel5))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jDateInicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addGap(19, 19, 19)
                                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTextFSueldoEmp, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jTextFDireccionEmp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel12))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel10)
                                    .addComponent(jDateFin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(29, 29, 29)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButtonGuardar2)
                            .addComponent(jButtonRegresar2, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonActualizarEmp)
                            .addComponent(jButtonEliminarEmp)
                            .addComponent(jButtonLimpiar)))
                    .addComponent(jTextFCorreo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextFNombreEmpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFNombreEmpActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFNombreEmpActionPerformed

    private void jTextFApellidosEmpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFApellidosEmpActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFApellidosEmpActionPerformed

    private void jTextFNumDocumentoEmpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFNumDocumentoEmpActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFNumDocumentoEmpActionPerformed

    private void jTextFONOEmpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFONOEmpActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFONOEmpActionPerformed

    private void jTextFIDEmpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFIDEmpActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFIDEmpActionPerformed

    private void jButtonGuardar2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonGuardar2ActionPerformed
        try {
            // 1. CAPTURAR DATOS DE TEXTO
            String nombre = jTextFNombreEmp.getText().trim();
            String apellido = jTextFApellidosEmp.getText().trim();
            String numDocStr = jTextFNumDocumentoEmp.getText().trim();
            String telStr = jTextFONOEmp.getText().trim();
            String idStr = jTextFIDEmp.getText().trim();
            String direccion = jTextFDireccionEmp.getText().trim();
            String correo = jTextFCorreo.getText().trim();
            String sueldoStr = jTextFSueldoEmp.getText().trim();

            // 2. CAPTURAR COMBOBOX Y FECHAS
            String rol = jComboBoxRolEmp.getSelectedItem().toString();
            String tipoDoc = jComboBoxTipoDoc.getSelectedItem().toString();

            java.util.Date fechaInicio = jDateInicio.getDate();
            java.util.Date fechaFin = jDateFin.getDate();

            // 3. VALIDAR CAMPOS VACÍOS BÁSICOS
            if (rol.equals("Seleccione un rol...")) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar un rol válido para el empleado.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (tipoDoc.equals("Seleccione un documento...")) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar un tipo de documento.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (nombre.isEmpty() || apellido.isEmpty() || numDocStr.isEmpty()
                    || telStr.isEmpty() || idStr.isEmpty() || direccion.isEmpty() || sueldoStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos los campos de texto deben estar completos (incluyendo Dirección y Sueldo).", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (fechaInicio == null || fechaFin == null) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar la fecha de inicio y fin de contrato.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (fechaFin.before(fechaInicio)) {
                JOptionPane.showMessageDialog(this, "La fecha de fin no puede ser anterior a la de inicio.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 4. VALIDACIONES CON REGEX (EXPRESIONES REGULARES)
            // Validar Tipo de Documento
            if (tipoDoc.equals("DNI")) {
                if (!numDocStr.matches("\\d{8}")) {
                    JOptionPane.showMessageDialog(this, "El DNI debe tener exactamente 8 dígitos numéricos.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } else if (tipoDoc.equals("Carnet de Extranjería")) {
                if (!numDocStr.matches("\\d{9,12}")) {
                    JOptionPane.showMessageDialog(this, "El Carnet de Extranjería debe tener entre 9 y 12 dígitos numéricos.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            // Validar Teléfono e ID (Solo números)
            if (!telStr.matches("\\d+") || !idStr.matches("\\d+")) {
                JOptionPane.showMessageDialog(this, "El Teléfono y el ID deben contener solo números.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validar Correo
            String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
            if (!correo.matches(emailRegex)) {
                JOptionPane.showMessageDialog(this, "Por favor, ingrese un correo electrónico válido.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 5. CONVERSIONES NUMÉRICAS FINALES
            int telefono = Integer.parseInt(telStr);
            int id = Integer.parseInt(idStr);
            double sueldo = Double.parseDouble(sueldoStr);

            // 6. VERIFICAR DUPLICADOS EN EL SISTEMA
            Hotel hotel = SistemaHotel.getInstancia().getHotel();

            if (hotel.buscarEmpleadoPorId(id) != null) {
                JOptionPane.showMessageDialog(this, "Ya existe un empleado con ese ID.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // CAMBIO: Ahora usamos el método de búsqueda por Documento (String)
            if (hotel.buscarEmpleadoPorDocumento(numDocStr) != null) {
                JOptionPane.showMessageDialog(this, "Ya existe un empleado con ese Número de Documento.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 7. CREAR Y GUARDAR EMPLEADO
            Empleado nuevoEmpleado = new Empleado(id, rol, sueldo, correo, fechaInicio, fechaFin, nombre, apellido, tipoDoc, numDocStr, telefono, direccion);

            hotel.registrarEmpleado(nuevoEmpleado);
            SistemaHotel.getInstancia().guardarCambios();

            JOptionPane.showMessageDialog(this, "Empleado registrado y guardado exitosamente.");

            // 8. LIMPIAR CAMPOS
            jTextFNombreEmp.setText("");
            jTextFApellidosEmp.setText("");
            jTextFNumDocumentoEmp.setText("");
            jTextFONOEmp.setText("");
            jTextFIDEmp.setText("");
            jTextFDireccionEmp.setText("");
            jTextFSueldoEmp.setText("");
            jTextFCorreo.setText("");
            jDateInicio.setDate(null);
            jDateFin.setDate(null);

            jComboBoxRolEmp.setSelectedIndex(0);
            jComboBoxTipoDoc.setSelectedIndex(0);

            jTextFNombreEmp.requestFocus();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error de formato numérico. Verifique el ID, Teléfono o Sueldo.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al registrar empleado: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButtonGuardar2ActionPerformed

    private void jButtonRegresar2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRegresar2ActionPerformed
        // Salir del registro de empleado
        new Menu_principal().setVisible(true);
        dispose();
    }//GEN-LAST:event_jButtonRegresar2ActionPerformed

    private void jComboBoxRolEmpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxRolEmpActionPerformed
        // Usamos trim() para mayor seguridad
        String selected = jComboBoxRolEmp.getSelectedItem().toString().trim();

        switch (selected) {
            case "Seleccione":
                jTextFSueldoEmp.setText(" ");
                break;
            case "Recepcionista":
                jTextFSueldoEmp.setText("1200.0");
                break;
            case "Gerente de almacen":
                jTextFSueldoEmp.setText("2500.0");
                break;
            case "Chef":
                jTextFSueldoEmp.setText("1300.0");
                break;
            case "Limpieza":
                jTextFSueldoEmp.setText("1100.0");
                break;
            case "Gerente de compras":
                jTextFSueldoEmp.setText("2800.0");
                break;
            case "Gerente de RRHH":
                jTextFSueldoEmp.setText("2600.0");
                break;
            case "Gerente General":
                jTextFSueldoEmp.setText("3000.0");
                break;
            default:
                jTextFSueldoEmp.setText("");
                break;
        }

    }//GEN-LAST:event_jComboBoxRolEmpActionPerformed

    private void jTextFSueldoEmpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFSueldoEmpActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFSueldoEmpActionPerformed

    private void jTextFCorreoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFCorreoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFCorreoActionPerformed

    private void jComboBoxTipoDocActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxTipoDocActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBoxTipoDocActionPerformed

    private void jTextFDireccionEmpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFDireccionEmpActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFDireccionEmpActionPerformed

    private void JButtonBuscarEmpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JButtonBuscarEmpActionPerformed
        // 1. Obtener el ID de la caja de búsqueda
        String idBuscarStr = jTextFIdBuscar.getText().trim();

        if (idBuscarStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese el ID del empleado a buscar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // 2. Convertir el texto a número
            int idBusqueda = Integer.parseInt(idBuscarStr);

            // 3. Buscar en el sistema
            Hotel hotel = SistemaHotel.getInstancia().getHotel();
            Empleado emp = hotel.buscarEmpleadoPorId(idBusqueda);

            // 4. Validar y llenar las casillas
            if (emp != null) {
                // Llenar Cajas de Texto
                jTextFIDEmp.setText(String.valueOf(emp.getId()));
                jTextFNombreEmp.setText(emp.getNombre());
                jTextFApellidosEmp.setText(emp.getApellido());
                jTextFNumDocumentoEmp.setText(emp.getNumDocumento());
                jTextFONOEmp.setText(String.valueOf(emp.getTelefono()));
                jTextFDireccionEmp.setText(emp.getDireccion());
                jTextFCorreo.setText(emp.getCorreo());
                jTextFSueldoEmp.setText(String.valueOf(emp.getSueldo()));

                // Llenar ComboBoxes 
                jComboBoxTipoDoc.setSelectedItem(emp.getTipoDocumento());
                jComboBoxRolEmp.setSelectedItem(emp.getRol());

                // Llenar JCalendars (Las fechas)
                jDateInicio.setDate(emp.getInicioContrato());
                jDateFin.setDate(emp.getFinContrato());

                JOptionPane.showMessageDialog(this, "Empleado encontrado y datos cargados.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró ningún empleado con el ID: " + idBusqueda, "No encontrado", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El ID debe ser un número entero válido (solo números).", "Error de formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ocurrió un error al buscar al empleado: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_JButtonBuscarEmpActionPerformed

    private void jButtonActualizarEmpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonActualizarEmpActionPerformed
        try {
            // 1. CAPTURAR EL ID PRIMERO 
            String idStr = jTextFIDEmp.getText().trim();

            if (idStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe buscar un empleado por ID primero para poder actualizarlo.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // 2. CAPTURAR EL RESTO DE DATOS
            String nombre = jTextFNombreEmp.getText().trim();
            String apellido = jTextFApellidosEmp.getText().trim();
            String numDocStr = jTextFNumDocumentoEmp.getText().trim();
            String telStr = jTextFONOEmp.getText().trim();
            String direccion = jTextFDireccionEmp.getText().trim();
            String correo = jTextFCorreo.getText().trim();
            String sueldoStr = jTextFSueldoEmp.getText().trim();

            String rol = jComboBoxRolEmp.getSelectedItem().toString();
            String tipoDoc = jComboBoxTipoDoc.getSelectedItem().toString();

            java.util.Date fechaInicio = jDateInicio.getDate();
            java.util.Date fechaFin = jDateFin.getDate();

            // 3. VALIDAR CAMPOS VACÍOS BÁSICOS
            if (rol.equals("Seleccione un rol...") || tipoDoc.equals("Seleccione un documento...")) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar un rol y un tipo de documento.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (nombre.isEmpty() || apellido.isEmpty() || numDocStr.isEmpty()
                    || telStr.isEmpty() || direccion.isEmpty() || sueldoStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos los campos de texto deben estar completos para actualizar.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (fechaInicio == null || fechaFin == null) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar la fecha de inicio y fin de contrato.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (fechaFin.before(fechaInicio)) {
                JOptionPane.showMessageDialog(this, "La fecha de fin no puede ser anterior a la de inicio.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 4. VALIDACIONES CON REGEX (EXPRESIONES REGULARES)
            if (tipoDoc.equals("DNI")) {
                if (!numDocStr.matches("\\d{8}")) {
                    JOptionPane.showMessageDialog(this, "El DNI debe tener exactamente 8 dígitos numéricos.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } else if (tipoDoc.equals("Carnet de Extranjería")) {
                if (!numDocStr.matches("\\d{9,12}")) {
                    JOptionPane.showMessageDialog(this, "El Carnet de Extranjería debe tener entre 9 y 12 dígitos numéricos.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            if (!telStr.matches("\\d+")) {
                JOptionPane.showMessageDialog(this, "El Teléfono debe contener solo números.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
            if (!correo.matches(emailRegex)) {
                JOptionPane.showMessageDialog(this, "Por favor, ingrese un correo electrónico válido.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 5. CONVERSIONES NUMÉRICAS
            int id = Integer.parseInt(idStr);
            int telefono = Integer.parseInt(telStr);
            double sueldo = Double.parseDouble(sueldoStr);

            // 6. CREAR EL OBJETO EMPLEADO CON LOS NUEVOS DATOS
            Empleado empleadoActualizado = new Empleado(id, rol, sueldo, correo, fechaInicio, fechaFin, nombre, apellido, tipoDoc, numDocStr, telefono, direccion);

            // 7. ENVIAR A ACTUALIZAR EN EL SISTEMA
            Hotel hotel = SistemaHotel.getInstancia().getHotel();
            boolean actualizado = hotel.actualizarEmpleado(empleadoActualizado);

            if (actualizado) {
                // Si fue exitoso, SE GUARDA EN LA MEMORIA FÍSICA (.dat)
                SistemaHotel.getInstancia().guardarCambios();

                JOptionPane.showMessageDialog(this, "Empleado actualizado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

                jTextFIdBuscar.setText(""); // Limpia la caja de busqueda

            } else {
                JOptionPane.showMessageDialog(this, "No se pudo actualizar. El ID no existe en el sistema.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error de formato numérico. Verifique el Teléfono o Sueldo.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al actualizar empleado: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButtonActualizarEmpActionPerformed

    private void jButtonEliminarEmpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonEliminarEmpActionPerformed
        try {
            // 1. CAPTURAR EL ID DEL EMPLEADO EN PANTALLA
            String idStr = jTextFIDEmp.getText().trim();

            if (idStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe buscar un empleado primero para poder eliminarlo.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // 2. CONVERTIR EL TEXTO A NÚMERO
            int id = Integer.parseInt(idStr);

            // 3. ¡MEDIDA DE SEGURIDAD! Pedir confirmación al usuario
            int confirmacion = JOptionPane.showConfirmDialog(this,
                    "¿Está completamente seguro de que desea eliminar al empleado con ID " + id + "?\nEsta acción no se puede deshacer.",
                    "Confirmar Eliminación",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            // Si el usuario hace clic en "Sí" (YES_OPTION)
            if (confirmacion == JOptionPane.YES_OPTION) {

                // 4. MANDAR A ELIMINAR EN EL SISTEMA
                Hotel hotel = SistemaHotel.getInstancia().getHotel();
                boolean eliminado = hotel.eliminarEmpleado(id);

                if (eliminado) {
                    // 5.  Guardar los cambios físicos para que el empleado desaparezca del .dat
                    SistemaHotel.getInstancia().guardarCambios();

                    JOptionPane.showMessageDialog(this, "Empleado eliminado exitosamente del sistema.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

                    // Limpia la caja de busqueda
                    if (jTextFIdBuscar != null) {
                        jTextFIdBuscar.setText("");
                    }

                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo eliminar. El ID no fue encontrado en el sistema.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            // Si el usuario hace clic en "No", simplemente no pasa nada y se cancela la acción.

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El ID debe ser un número entero válido.", "Error de formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ocurrió un error al intentar eliminar al empleado: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButtonEliminarEmpActionPerformed

    private void jButtonLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLimpiarActionPerformed
        // 1. Limpiar todas las cajas de texto
        jTextFIDEmp.setText("");
        jTextFNombreEmp.setText("");
        jTextFApellidosEmp.setText("");
        jTextFNumDocumentoEmp.setText("");
        jTextFONOEmp.setText("");
        jTextFDireccionEmp.setText("");
        jTextFCorreo.setText("");
        jTextFSueldoEmp.setText("");

        // Limpia también la caja de búsqueda 
        if (jTextFIdBuscar != null) {
            jTextFIdBuscar.setText("");
        }

        // 2. Limpiar las fechas (JCalendar)
        jDateInicio.setDate(null);
        jDateFin.setDate(null);

        // 3. Reiniciar los menús desplegables (ComboBox) a la opción 0 ("Seleccione...")
        jComboBoxRolEmp.setSelectedIndex(0);
        jComboBoxTipoDoc.setSelectedIndex(0);

        // 4.  Mueve el cursor automáticamente a la primera casilla
        jTextFIDEmp.requestFocus();
    }//GEN-LAST:event_jButtonLimpiarActionPerformed

    private void jTextFIdBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFIdBuscarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFIdBuscarActionPerformed

    private void jTextFONOEmpKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFONOEmpKeyTyped
        // 1. Capturamos el carácter exacto de la tecla presionada
        char c = evt.getKeyChar();

        //2. Solo permite digitos
        if (!Character.isDigit(c) && c != '.') {
            evt.consume();
            return;
        }
        // 3. Evitar que el usuario escriba más de un punto decimal
        if (c == '.' && jTextFONOEmp.getText().contains(".")) {
            evt.consume(); // Destruye el segundo punto
        }
        //4. Bloque si hay mas de 9 digitos
        if (jTextFONOEmp.getText().length() >= 9) {
            evt.consume();
        }

    }//GEN-LAST:event_jTextFONOEmpKeyTyped

    private void jTextFIDEmpKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFIDEmpKeyTyped
        // 1. Capturamos el carácter exacto de la tecla presionada
        char c = evt.getKeyChar();

        // 2. Verificamos si NO es un número y NO es un punto
        if (!Character.isDigit(c) && c != '.') {
            evt.consume(); // ¡Destruye la pulsación! La letra no aparecerá en pantalla.
            return;
        }

        // 3. Evitar que el usuario escriba más de un punto decimal
        if (c == '.' && jTextFIDEmp.getText().contains(".")) {
            evt.consume(); // Destruye el segundo punto
        }

        //4. Bloque si hay mas de 9 digitos
        if (jTextFIDEmp.getText().length() >= 4) {
            evt.consume();
        }
    }//GEN-LAST:event_jTextFIDEmpKeyTyped

    private void jTextFIdBuscarKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFIdBuscarKeyTyped
        // 1. Capturamos el carácter exacto de la tecla presionada
        char c = evt.getKeyChar();

        // 2. Verificamos si NO es un número y NO es un punto
        if (!Character.isDigit(c) && c != '.') {
            evt.consume(); // ¡Destruye la pulsación! La letra no aparecerá en pantalla.
            return;
        }

        // 3. Evitar que el usuario escriba más de un punto decimal
        if (c == '.' && jTextFIdBuscar.getText().contains(".")) {
            evt.consume(); // Destruye el segundo punto
        }
    }//GEN-LAST:event_jTextFIdBuscarKeyTyped

    private void jTextFNombreEmpKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFNombreEmpKeyTyped
        // 1. Capturamos la tecla presionada
        char c = evt.getKeyChar();

        // 2. Si NO es una letra, NO es un espacio y NO es la tecla de borrar (ISOControl)...
        if (!Character.isLetter(c) && c != ' ' && !Character.isISOControl(c)) {
            evt.consume(); // ...¡Destruimos la tecla!
        }
    }//GEN-LAST:event_jTextFNombreEmpKeyTyped

    private void jTextFNombreEmpKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFNombreEmpKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFNombreEmpKeyReleased

    private void jTextFApellidosEmpKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFApellidosEmpKeyTyped
        // 1. Capturamos la tecla presionada
        char c = evt.getKeyChar();

        // 2. Si NO es una letra, NO es un espacio y NO es la tecla de borrar (ISOControl)...
        if (!Character.isLetter(c) && c != ' ' && !Character.isISOControl(c)) {
            evt.consume(); // ...¡Destruimos la tecla!
        }
    }//GEN-LAST:event_jTextFApellidosEmpKeyTyped

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
            java.util.logging.Logger.getLogger(Empleados.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Empleados.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Empleados.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Empleados.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Empleados().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton JButtonBuscarEmp;
    private javax.swing.JButton jButtonActualizarEmp;
    private javax.swing.JButton jButtonEliminarEmp;
    private javax.swing.JButton jButtonGuardar2;
    private javax.swing.JButton jButtonLimpiar;
    private javax.swing.JButton jButtonRegresar2;
    private javax.swing.JComboBox<String> jComboBoxRolEmp;
    private javax.swing.JComboBox<String> jComboBoxTipoDoc;
    private com.toedter.calendar.JDateChooser jDateFin;
    private com.toedter.calendar.JDateChooser jDateInicio;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPasswordField jPasswordField1;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JTextField jTextFApellidosEmp;
    private javax.swing.JTextField jTextFCorreo;
    private javax.swing.JTextField jTextFDireccionEmp;
    private javax.swing.JTextField jTextFIDEmp;
    private javax.swing.JTextField jTextFIdBuscar;
    private javax.swing.JTextField jTextFNombreEmp;
    private javax.swing.JTextField jTextFNumDocumentoEmp;
    private javax.swing.JTextField jTextFONOEmp;
    private javax.swing.JTextField jTextFSueldoEmp;
    // End of variables declaration//GEN-END:variables
}
