/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;

import BO.PacienteBO;
import BO.UsuarioBO;
import BO.DireccionPacienteBO;
import Conexion.Conexion;
import Conexion.IConexion;
import DTO.PacienteDTO;
import Exception.NegocioException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 *
 * @author isaac
 */
public class PacienteInicio extends JFrame {

    private JLabel correoLabel;
    private JLabel contrasenaLabel;
    private JTextField correoField;
    private JPasswordField contrasenaField;
    private JButton aceptarButton;
    private JButton regresarButton;
    private JButton registrarButton;
    private final PacienteBO pacienteBO;
    private final UsuarioBO usuarioBO;
    private final DireccionPacienteBO direccionBO;
    private final MenuFrame menuFrame;
    
    

    public PacienteInicio(PacienteBO pacienteBO, MenuFrame menuFrame, DireccionPacienteBO direccionBO, UsuarioBO usuarioBO) {
        this.pacienteBO = pacienteBO;
        this.menuFrame = menuFrame;
        this.direccionBO = direccionBO;
        this.usuarioBO = usuarioBO;
        initComponents();
    }

    private void initComponents() {
        this.setTitle("Inicio de Sesión - Paciente");
        this.setSize(400, 300);
        this.setLayout(null);

        // Elementos
        correoLabel = new JLabel("Correo electrónico:");
        contrasenaLabel = new JLabel("Contraseña:");
        correoField = new JTextField();
        contrasenaField = new JPasswordField();
        aceptarButton = new JButton("Aceptar");
        regresarButton = new JButton("Regresar");
        registrarButton = new JButton("Registrar");

        // Posiciones
        correoLabel.setBounds(50, 100, 150, 20);
        correoField.setBounds(50, 130, 300, 20);
        contrasenaLabel.setBounds(50, 170, 150, 20);
        contrasenaField.setBounds(50, 200, 300, 20);
        aceptarButton.setBounds(50, 250, 100, 30);
        regresarButton.setBounds(250, 250, 100, 30);
        registrarButton.setBounds(150, 250, 100, 30);

        this.add(correoLabel);
        this.add(correoField);
        this.add(contrasenaLabel);
        this.add(contrasenaField);
        this.add(aceptarButton);
        this.add(regresarButton);
        this.add(registrarButton);

        // Oyentes de Eventos
        aceptarButton.addActionListener(e -> autenticarPaciente());
        regresarButton.addActionListener(e -> regresarMenu());
        registrarButton.addActionListener(e -> {
            dispose();
            new RegistroPaciente(pacienteBO, usuarioBO, direccionBO, menuFrame).setVisible(true); 
        });
    }



    private void autenticarPaciente() {
        String correo = correoField.getText();
        String contraseña = new String(contrasenaField.getPassword());

        try {
            PacienteDTO pacienteDTO = pacienteBO.buscarPacientePorCorreoyContra(correo, contraseña);

            if (pacienteDTO != null) {
                JOptionPane.showMessageDialog(this, "Inicio de sesión exitoso como paciente.");
                dispose();
                new PacienteMenu(pacienteDTO, pacienteBO, direccionBO, menuFrame).setVisible(true); 
            } else {
                JOptionPane.showMessageDialog(this, "Credenciales incorrectas.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NegocioException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void regresarMenu() {
        dispose();
        menuFrame.setVisible(true);
    }
}
