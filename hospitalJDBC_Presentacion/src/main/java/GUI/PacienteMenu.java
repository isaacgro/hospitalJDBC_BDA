/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;

import BO.CitaBO;
import BO.ConsultaBO;
import BO.DireccionPacienteBO;
import BO.MedicoBO;
import BO.PacienteBO;
import Conexion.Conexion;
import DTO.PacienteDTO;
import Exception.NegocioException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 *
 * @author isaac
 */
public class PacienteMenu extends JFrame {
    private static final Logger LOGGER = Logger.getLogger(PacienteMenu.class.getName());
    
    private JLabel lblBienvenido;
    private JLabel lblNombreCompleto;
    private JButton btnAgendarCita;
    private JButton btnVerCitas;
    private JButton btnVerHistorial;
    private JButton btnEditarPerfil;
    private JButton btnRegresar;
    
    private final MenuFrame menuFrame;
    private final PacienteBO pacienteBO;
    private final DireccionPacienteBO direccionBO;
    private final PacienteDTO pacienteDTO;
    // Nuevos campos
    private final CitaBO citaBO;
    private final MedicoBO medicoBO;
    
    /**
     * Constructor actualizado con CitaBO y MedicoBO
     */
    public PacienteMenu(PacienteDTO pacienteDTO, PacienteBO pacienteBO, 
                        DireccionPacienteBO direccionBO, CitaBO citaBO, 
                        MedicoBO medicoBO, MenuFrame menuFrame) {
        this.pacienteDTO = pacienteDTO;
        this.pacienteBO = pacienteBO;
        this.direccionBO = direccionBO;
        this.citaBO = citaBO;
        this.medicoBO = medicoBO;
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
        btnEditarPerfil.addActionListener(e -> {
            dispose();
            try {
                new EditarPerfilPaciente(pacienteBO, direccionBO, pacienteDTO, menuFrame).setVisible(true);
            } catch (NegocioException ex) {
                Logger.getLogger(PacienteMenu.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        // Añadir listener para btnAgendarCita
        btnAgendarCita.addActionListener(e -> abrirAgendarCita());
        
        // Listener para btnVerHistorial
        btnVerHistorial.addActionListener(e -> mostrarHistorialConsultas());
    }
    
    private void initializeComponents(PacienteDTO pacienteDTO) {
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        lblNombreCompleto.setText(pacienteDTO.getUsuario().getNombre() + " "
                + pacienteDTO.getUsuario().getApellidoP() + " "
                + pacienteDTO.getUsuario().getApellidoM());
    }
    
    /**
     * Método nuevo para abrir el frame de agendar cita
     */
    private void abrirAgendarCita() {
        try {
            this.setVisible(false);
            new AgendarCita(citaBO, medicoBO, pacienteDTO, this).setVisible(true);
        } catch (Exception ex) {
            Logger.getLogger(PacienteMenu.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, 
                    "Error al abrir ventana para agendar cita: " + ex.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void regresarMenu() {
        dispose();
        menuFrame.setVisible(true);
    }
    
    // Método para mostrar el historial de consultas
    private void mostrarHistorialConsultas() {
        try {
            // Ocultar este frame y mostrar el historial
            this.setVisible(false);
            new HistorialConsultaPaciente(pacienteDTO, this).setVisible(true);
        } catch (Exception ex) {
            Logger.getLogger(PacienteMenu.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this,
                    "Error al abrir el historial de consultas: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
