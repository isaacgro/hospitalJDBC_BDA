/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;

import BO.DireccionPacienteBO;
import BO.PacienteBO;
import DTO.DireccionPacienteDTO;
import DTO.PacienteDTO;
import Exception.NegocioException;
import javax.swing.*;
import java.awt.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author isaac
 */
public class EditarPerfilPaciente extends JFrame {

    private JTextField txtNombre, txtApellidoPaterno, txtApellidoMaterno, txtTelefono, txtCorreo, txtCalle, txtNumExt, txtColonia;
    private JButton btnGuardar, btnRegresar;
    private final PacienteBO pacienteBO;
    private final DireccionPacienteBO direccionBO;
    private final PacienteDTO pacienteDTO;
    private final MenuFrame menuFrame;

    public EditarPerfilPaciente(PacienteBO pacienteBO, DireccionPacienteBO direccionBO, PacienteDTO pacienteDTO, MenuFrame menuFrame) throws NegocioException {
        this.pacienteBO = pacienteBO;
        this.direccionBO = direccionBO;
        this.pacienteDTO = pacienteDTO;
        this.menuFrame = menuFrame;
        initComponents();
        cargarDatosPaciente();
    }

    private void initComponents() {
        setTitle("Editar Perfil - Paciente");
        setSize(400, 450);
        setLayout(null);
        setLocationRelativeTo(null);

        // Etiquetas y campos de texto
        JLabel lblNombre = new JLabel("Nombre:");
        JLabel lblApellidoPaterno = new JLabel("Apellido Paterno:");
        JLabel lblApellidoMaterno = new JLabel("Apellido Materno:");
        JLabel lblTelefono = new JLabel("Teléfono:");
        JLabel lblCorreo = new JLabel("Correo Electrónico:");
        JLabel lblCalle = new JLabel("Calle:");
        JLabel lblNumExt = new JLabel("Número Exterior:");
        JLabel lblColonia = new JLabel("Colonia:");

        txtNombre = new JTextField();
        txtApellidoPaterno = new JTextField();
        txtApellidoMaterno = new JTextField();
        txtTelefono = new JTextField();
        txtCorreo = new JTextField();
        txtCalle = new JTextField();
        txtNumExt = new JTextField();
        txtColonia = new JTextField();

        btnGuardar = new JButton("Guardar");
        btnRegresar = new JButton("Regresar");

        int xLabel = 20, xField = 150, y = 20, width = 200, height = 25;
        lblNombre.setBounds(xLabel, y, 120, height);
        txtNombre.setBounds(xField, y, width, height);
        y += 35;
        lblApellidoPaterno.setBounds(xLabel, y, 120, height);
        txtApellidoPaterno.setBounds(xField, y, width, height);
        y += 35;
        lblApellidoMaterno.setBounds(xLabel, y, 120, height);
        txtApellidoMaterno.setBounds(xField, y, width, height);
        y += 35;
        lblTelefono.setBounds(xLabel, y, 120, height);
        txtTelefono.setBounds(xField, y, width, height);
        y += 35;
        lblCorreo.setBounds(xLabel, y, 120, height);
        txtCorreo.setBounds(xField, y, width, height);
        y += 35;
        lblCalle.setBounds(xLabel, y, 120, height);
        txtCalle.setBounds(xField, y, width, height);
        y += 35;
        lblNumExt.setBounds(xLabel, y, 120, height);
        txtNumExt.setBounds(xField, y, width, height);
        y += 35;
        lblColonia.setBounds(xLabel, y, 120, height);
        txtColonia.setBounds(xField, y, width, height);
        y += 40;
        btnGuardar.setBounds(80, y, 100, 30);
        btnRegresar.setBounds(200, y, 100, 30);

        add(lblNombre);
        add(txtNombre);
        add(lblApellidoPaterno);
        add(txtApellidoPaterno);
        add(lblApellidoMaterno);
        add(txtApellidoMaterno);
        add(lblTelefono);
        add(txtTelefono);
        add(lblCorreo);
        add(txtCorreo);
        add(lblCalle);
        add(txtCalle);
        add(lblNumExt);
        add(txtNumExt);
        add(lblColonia);
        add(txtColonia);
        add(btnGuardar);
        add(btnRegresar);

        btnGuardar.addActionListener(e -> guardarCambios());
        btnRegresar.addActionListener(e -> regresarAlMenu());

        setVisible(true);
    }

    private void cargarDatosPaciente() throws NegocioException {
        if (pacienteDTO != null) {
            txtNombre.setText(pacienteDTO.getUsuario().getNombre());
            txtApellidoPaterno.setText(pacienteDTO.getUsuario().getApellidoP());
            txtApellidoMaterno.setText(pacienteDTO.getUsuario().getApellidoM());
            txtTelefono.setText(pacienteDTO.getTelefono());
            txtCorreo.setText(pacienteDTO.getCorreoE());

            DireccionPacienteDTO direccionDTO = direccionBO.obtenerDireccionPorPaciente(pacienteDTO);
            if (direccionDTO != null) {
                txtCalle.setText(direccionDTO.getCalle());
                txtNumExt.setText(direccionDTO.getNumExt());
                txtColonia.setText(direccionDTO.getColonia());
            }
        }
    }

    private void guardarCambios() {
        if (validarCampos()) {
            try {
                // Actualizar paciente
                pacienteDTO.getUsuario().setNombre(txtNombre.getText());
                pacienteDTO.getUsuario().setApellidoP(txtApellidoPaterno.getText());
                pacienteDTO.getUsuario().setApellidoM(txtApellidoMaterno.getText());
                pacienteDTO.setTelefono(txtTelefono.getText());
                pacienteDTO.setCorreoE(txtCorreo.getText());

                boolean actualizadoPaciente = pacienteBO.actualizarPaciente(pacienteDTO);

                // Actualizar dirección
                DireccionPacienteDTO direccionDTO = new DireccionPacienteDTO(
                        pacienteDTO.getId_Paciente(),
                        txtCalle.getText(),
                        txtNumExt.getText(),
                        txtColonia.getText(),
                        pacienteDTO
                );

                boolean actualizadaDireccion = direccionBO.actualizarDireccion(direccionDTO);

                if (actualizadoPaciente && actualizadaDireccion) {
                    JOptionPane.showMessageDialog(this, "Perfil actualizado correctamente.");
                    dispose();
                    menuFrame.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(this, "Error al actualizar el perfil.", "Error", JOptionPane.ERROR_MESSAGE);
                }

            } catch (NegocioException ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private boolean validarCampos() {
        if (txtNombre.getText().trim().isEmpty()
                || txtApellidoPaterno.getText().trim().isEmpty()
                || txtTelefono.getText().trim().isEmpty()
                || txtCorreo.getText().trim().isEmpty()
                || txtCalle.getText().trim().isEmpty()
                || txtNumExt.getText().trim().isEmpty()
                || txtColonia.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private void regresarAlMenu() {
        dispose();
        menuFrame.setVisible(true);
    }
}
