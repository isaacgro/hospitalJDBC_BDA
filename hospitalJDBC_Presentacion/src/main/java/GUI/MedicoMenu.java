/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;

import DTO.MedicoDTO;
import Exception.NegocioException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

/**
 *
 * @author isaac
 */
public class MedicoMenu extends JFrame {

    private static final Logger LOGGER = Logger.getLogger(MedicoMenu.class.getName());
    private JLabel lblBienvenido;
    private JLabel lblNombreCompleto;
    private JButton btnBajaTemporal;
    private JButton btnAgendaCitas;
    private JButton btnHistorialCitas;
    private JButton btnCerrarSesion;
    private final MenuFrame menuFrame;

    public MedicoMenu(MedicoDTO medicoDTO, MenuFrame menuFrame) {
        this.menuFrame = menuFrame;
        initComponents();
        initializeComponents(medicoDTO);
    }

    private void initComponents() {
        this.setTitle("Menú del Médico");
        this.setSize(400, 300);
        this.setLayout(new GridLayout(5, 1, 10, 10));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Elementos
        lblBienvenido = new JLabel("¡Bienvenido de vuelta!", SwingConstants.CENTER);
        lblNombreCompleto = new JLabel("", SwingConstants.CENTER);
        btnBajaTemporal = new JButton("Baja Temporal");
        btnAgendaCitas = new JButton("Agenda de Citas");
        btnHistorialCitas = new JButton("Historial de Citas");
        btnCerrarSesion = new JButton("Cerrar Sesión");

        // Agregar elementos al Frame
        this.add(lblBienvenido);
        this.add(lblNombreCompleto);
        this.add(btnBajaTemporal);
        this.add(btnAgendaCitas);
        this.add(btnHistorialCitas);
        this.add(btnCerrarSesion);

        // Listeners
        btnCerrarSesion.addActionListener(e -> cerrarSesion());
    }

    private void initializeComponents(MedicoDTO medicoDTO) {
        this.setLocationRelativeTo(null);
        if (medicoDTO.getUsuario() != null) {
            lblNombreCompleto.setText(medicoDTO.getUsuario().getNombre() + " "
                    + medicoDTO.getUsuario().getApellidoP() + " "
                    + medicoDTO.getUsuario().getApellidoM());
        } else {
            lblNombreCompleto.setText("Información no disponible");
        }
        this.setVisible(true);
    }

    private void cerrarSesion() {
        LOGGER.log(Level.INFO, "Cierre de sesión exitoso");
        dispose();
        menuFrame.setVisible(true);
    }
}
