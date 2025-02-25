/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;

import BO.MedicoBO;
import BO.UsuarioBO;
import Conexion.Conexion;
import Conexion.IConexion;
import DTO.MedicoDTO;
import Exception.NegocioException;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

/**
 *
 * @author isaac
 */
public class MedicoInicio extends JFrame {

    private static final Logger LOGGER = Logger.getLogger(MedicoInicio.class.getName());
    private JTextField cedulaField;
    private JPasswordField contrasenaField;
    private final MedicoBO medicoBO;
    private final MenuFrame menuFrame;

    public MedicoInicio(MedicoBO medicoBO, MenuFrame menuFrame) {
        this.medicoBO = medicoBO;
        this.menuFrame = menuFrame;
        initComponents();
    }

    private void initComponents() {
        this.setTitle("Inicio de Sesión - Médico");
        this.setSize(400, 300);
        this.setLayout(new GridLayout(4, 1, 10, 10));

        JLabel cedulaLabel = new JLabel("Cédula profesional:", SwingConstants.CENTER);
        cedulaField = new JTextField();
        JLabel contrasenaLabel = new JLabel("Contraseña:", SwingConstants.CENTER);
        contrasenaField = new JPasswordField();
        JButton aceptarButton = new JButton("Aceptar");
        JButton regresarButton = new JButton("Regresar");

        this.add(cedulaLabel);
        this.add(cedulaField);
        this.add(contrasenaLabel);
        this.add(contrasenaField);
        this.add(aceptarButton);
        this.add(regresarButton);

        aceptarButton.addActionListener(e -> autenticarMedico());
        regresarButton.addActionListener(e -> regresarMenu());

        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private void autenticarMedico() {
        String cedula = cedulaField.getText().trim();
        String contrasena = new String(contrasenaField.getPassword()).trim();

        try {
            boolean autenticado = medicoBO.autenticarMedico(cedula, contrasena);

            if (autenticado) {
                MedicoDTO medicoDTO = medicoBO.obtenerMedicoPorCedula(cedula);
                JOptionPane.showMessageDialog(this, "Inicio de sesión exitoso como médico.");
                dispose();
                new MedicoMenu(medicoDTO, menuFrame).setVisible(true); // ✅ Pasamos MenuFrame
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
