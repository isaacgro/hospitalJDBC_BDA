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

    private JLabel lblNombre, lblApellidoPaterno, lblApellidoMaterno, lblFechaNacimiento, lblEdad, lblTelefono, lblCorreo, lblContrasena;
    private JLabel lblCalle, lblNumExt, lblColonia;
    private JTextField txtNombre, txtApellidoPaterno, txtApellidoMaterno, txtFechaNacimiento, txtEdad, txtTelefono, txtCorreo;
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
        setSize(450, 500);
        setLayout(null);

        // (Mantiene la inicialización de componentes y posiciones)
        // Eventos de botones
        btnAceptar.addActionListener(e -> registrarPaciente());
        btnCancelar.addActionListener(e -> limpiarCampos());
    }

    private void registrarPaciente() {
        if (validarCampos()) {
            try {
                // Verificar si el correo ya está registrado
                if (usuarioBO.existeCorreo(txtCorreo.getText())) {
                    JOptionPane.showMessageDialog(this, "El correo ya está registrado. Usa otro.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                UsuarioDTO usuarioDTO = new UsuarioDTO(0, txtNombre.getText(), txtApellidoPaterno.getText(), txtApellidoMaterno.getText(), new String(contrasenaField.getPassword()));
                usuarioDTO = usuarioBO.crearUsuario(usuarioDTO);

                if (usuarioDTO.getId_Usuario() > 0) {
                    PacienteDTO pacienteDTO = new PacienteDTO(0, LocalDate.parse(txtFechaNacimiento.getText()), Integer.parseInt(txtEdad.getText()), txtTelefono.getText(), txtCorreo.getText(), usuarioDTO);
                    boolean pacienteCreado = pacienteBO.registrarPaciente(pacienteDTO);

                    if (pacienteCreado) {
                        DireccionPacienteDTO direccionDTO = new DireccionPacienteDTO(0, txtCalle.getText(), txtNumExt.getText(), txtColonia.getText(), pacienteDTO);
                        direccionBO.registrarDireccion(direccionDTO);

                        JOptionPane.showMessageDialog(this, "Registro exitoso");
                        dispose();
                        menuFrame.setVisible(true);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Error al crear el usuario", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NegocioException ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void limpiarCampos() {
        txtNombre.setText("");
        txtApellidoPaterno.setText("");
        txtApellidoMaterno.setText("");
        txtFechaNacimiento.setText("");
        txtEdad.setText("");
        txtTelefono.setText("");
        txtCorreo.setText("");
        contrasenaField.setText("");
        txtCalle.setText("");
        txtNumExt.setText("");
        txtColonia.setText("");
    }

    private boolean validarCampos() {
        if (txtNombre.getText().trim().isEmpty()
                || txtApellidoPaterno.getText().trim().isEmpty()
                || txtFechaNacimiento.getText().trim().isEmpty()
                || txtEdad.getText().trim().isEmpty()
                || txtTelefono.getText().trim().isEmpty()
                || txtCorreo.getText().trim().isEmpty()
                || new String(contrasenaField.getPassword()).trim().isEmpty()
                || txtCalle.getText().trim().isEmpty()
                || txtNumExt.getText().trim().isEmpty()
                || txtColonia.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

}
