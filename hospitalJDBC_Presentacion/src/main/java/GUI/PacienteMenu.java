/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;

import DTO.PacienteDTO;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 *
 * @author isaac
 */
public class PacienteMenu extends JFrame {

    private JLabel lblBienvenido;
    private JLabel lblNombreCompleto;
    private JButton btnAgendarCita;
    private JButton btnVerCitas;
    private JButton btnVerHistorial;
    private JButton btnEditarPerfil;
    private JButton btnRegresar;
    private final MenuFrame menuFrame;

    public PacienteMenu(PacienteDTO pacienteDTO, MenuFrame menuFrame) {
        this.menuFrame = menuFrame;
        initComponents();
        initializeComponents(pacienteDTO);
    }

    private void initComponents() {
        this.setTitle("Menú del Paciente");
        this.setSize(400, 300);
        this.setLayout(null);

        // Elementos
        lblBienvenido = new JLabel("Bienvenido de vuelta");
        lblNombreCompleto = new JLabel();
        btnAgendarCita = new JButton("Agendar Cita");
        btnVerCitas = new JButton("Ver Citas Próximas");
        btnVerHistorial = new JButton("Ver Historial");
        btnEditarPerfil = new JButton("Editar Perfil");
        btnRegresar = new JButton("Regresar");

        // Posiciones
        lblBienvenido.setBounds(50, 50, 200, 20);
        lblNombreCompleto.setBounds(50, 70, 300, 20);
        btnAgendarCita.setBounds(50, 100, 200, 30);
        btnVerCitas.setBounds(50, 140, 200, 30);
        btnVerHistorial.setBounds(50, 180, 200, 30);
        btnEditarPerfil.setBounds(50, 220, 200, 30);
        btnRegresar.setBounds(250, 220, 100, 30);

        this.add(lblBienvenido);
        this.add(lblNombreCompleto);
        this.add(btnAgendarCita);
        this.add(btnVerCitas);
        this.add(btnVerHistorial);
        this.add(btnEditarPerfil);
        this.add(btnRegresar);

        // Oyentes de Eventos
        btnRegresar.addActionListener(e -> regresarMenu());
    }

    private void initializeComponents(PacienteDTO pacienteDTO) {
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        lblNombreCompleto.setText(pacienteDTO.getUsuario().getNombre() + " "
                + pacienteDTO.getUsuario().getApellidoP() + " "
                + pacienteDTO.getUsuario().getApellidoM());
    }

    private void regresarMenu() {
        dispose();
        menuFrame.setVisible(true);
    }
}
