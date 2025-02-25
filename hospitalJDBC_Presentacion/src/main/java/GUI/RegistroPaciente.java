/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;

import BO.CitaBO;
import BO.DireccionPacienteBO;
import BO.PacienteBO;
import BO.UsuarioBO;
import Conexion.Conexion;
import DTO.DireccionPacienteDTO;
import DTO.PacienteDTO;
import DTO.UsuarioDTO;
import Excepciones.PersistenciaExcption;
import Exception.NegocioException;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 *
 * @author isaac
 */
public class RegistroPaciente extends JFrame {

    private JLabel lblNombre, lblApellidoPaterno, lblApellidoMaterno, lblFechaNacimiento, lblTelefono, lblCorreo, lblContrasena;
    private JLabel lblCalle, lblNumExt, lblColonia;
    private JTextField txtNombre, txtApellidoPaterno, txtApellidoMaterno, txtFechaNacimiento, txtTelefono, txtCorreo;
    private JTextField txtCalle, txtNumExt, txtColonia;
    private JPasswordField contrasenaField;
    private JButton btnAceptar, btnCancelar;
    private final PacienteBO pacienteBO;
    private final UsuarioBO usuarioBO;
    private final DireccionPacienteBO direccionBO;
    private final MenuFrame menuFrame;

    public RegistroPaciente(PacienteBO pacienteBO, UsuarioBO usuarioBO, DireccionPacienteBO direccionBO, MenuFrame menuFrame) {
        this.pacienteBO = pacienteBO;
        this.usuarioBO = usuarioBO;
        this.direccionBO = direccionBO;
        this.menuFrame = menuFrame;
        initComponents();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initComponents() {
        setTitle("Registro de Paciente");
        setSize(500, 600);
        setLayout(null);

        // Etiquetas
        lblNombre = new JLabel("Nombre:");
        lblApellidoPaterno = new JLabel("Apellido Paterno:");
        lblApellidoMaterno = new JLabel("Apellido Materno:");
        lblFechaNacimiento = new JLabel("Fecha de Nacimiento (YYYY-MM-DD):");
        lblTelefono = new JLabel("Teléfono:");
        lblCorreo = new JLabel("Correo Electrónico:");
        lblContrasena = new JLabel("Contraseña:");
        lblCalle = new JLabel("Calle:");
        lblNumExt = new JLabel("Número Exterior:");
        lblColonia = new JLabel("Colonia:");

        // Campos de texto
        txtNombre = new JTextField();
        txtApellidoPaterno = new JTextField();
        txtApellidoMaterno = new JTextField();
        txtFechaNacimiento = new JTextField();
        txtTelefono = new JTextField();
        txtCorreo = new JTextField();
        contrasenaField = new JPasswordField();
        txtCalle = new JTextField();
        txtNumExt = new JTextField();
        txtColonia = new JTextField();

        // Botones
        btnAceptar = new JButton("Aceptar");
        btnCancelar = new JButton("Cancelar");

        // Posiciones en el frame
        int y = 50;
        int espacio = 40;
        lblNombre.setBounds(50, y, 150, 20);
        txtNombre.setBounds(220, y, 200, 20);
        y += espacio;
        lblApellidoPaterno.setBounds(50, y, 150, 20);
        txtApellidoPaterno.setBounds(220, y, 200, 20);
        y += espacio;
        lblApellidoMaterno.setBounds(50, y, 150, 20);
        txtApellidoMaterno.setBounds(220, y, 200, 20);
        y += espacio;
        lblFechaNacimiento.setBounds(50, y, 200, 20);
        txtFechaNacimiento.setBounds(270, y, 150, 20);
        y += espacio;
        lblTelefono.setBounds(50, y, 150, 20);
        txtTelefono.setBounds(220, y, 200, 20);
        y += espacio;
        lblCorreo.setBounds(50, y, 150, 20);
        txtCorreo.setBounds(220, y, 200, 20);
        y += espacio;
        lblContrasena.setBounds(50, y, 150, 20);
        contrasenaField.setBounds(220, y, 200, 20);
        y += espacio;
        lblCalle.setBounds(50, y, 150, 20);
        txtCalle.setBounds(220, y, 200, 20);
        y += espacio;
        lblNumExt.setBounds(50, y, 150, 20);
        txtNumExt.setBounds(220, y, 200, 20);
        y += espacio;
        lblColonia.setBounds(50, y, 150, 20);
        txtColonia.setBounds(220, y, 200, 20);
        y += espacio;

        btnAceptar.setBounds(100, y, 100, 30);
        btnCancelar.setBounds(250, y, 100, 30);

        // Agregar al frame
        this.add(lblNombre);
        this.add(txtNombre);
        this.add(lblApellidoPaterno);
        this.add(txtApellidoPaterno);
        this.add(lblApellidoMaterno);
        this.add(txtApellidoMaterno);
        this.add(lblFechaNacimiento);
        this.add(txtFechaNacimiento);
        this.add(lblTelefono);
        this.add(txtTelefono);
        this.add(lblCorreo);
        this.add(txtCorreo);
        this.add(lblContrasena);
        this.add(contrasenaField);
        this.add(lblCalle);
        this.add(txtCalle);
        this.add(lblNumExt);
        this.add(txtNumExt);
        this.add(lblColonia);
        this.add(txtColonia);
        this.add(btnAceptar);
        this.add(btnCancelar);

        // Eventos de botones
        btnAceptar.addActionListener(e -> {
            try {
                UsuarioDTO usuarioDTO = new UsuarioDTO(0, txtNombre.getText(), txtApellidoPaterno.getText(), txtApellidoMaterno.getText(), new String(contrasenaField.getPassword()));
                usuarioDTO = usuarioBO.crearUsuario(usuarioDTO); // Registrar usuario y recuperar su ID

                if (usuarioDTO.getId_Usuario() > 0) {
                    PacienteDTO pacienteDTO = new PacienteDTO(0, LocalDate.parse(txtFechaNacimiento.getText()), 0, txtTelefono.getText(), txtCorreo.getText(), usuarioDTO);
                    boolean registrado = pacienteBO.registrarPaciente(pacienteDTO);

                    if (registrado) {
                        JOptionPane.showMessageDialog(this, "Registro exitoso.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                        dispose();
                        menuFrame.setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(this, "No se pudo registrar el paciente.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Error al crear el usuario.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NegocioException | DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

    
    }
    private void limpiarCampos() {
        txtNombre.setText("");
        txtApellidoPaterno.setText("");
        txtApellidoMaterno.setText("");
        txtFechaNacimiento.setText("");
        txtTelefono.setText("");
        txtCorreo.setText("");
        contrasenaField.setText("");
        txtCalle.setText("");
        txtNumExt.setText("");
        txtColonia.setText("");
    }
}
