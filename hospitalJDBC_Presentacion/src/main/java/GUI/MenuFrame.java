package GUI;

import BO.DireccionPacienteBO;
import BO.MedicoBO;
import BO.PacienteBO;
import BO.UsuarioBO;
import Conexion.Conexion;
import Conexion.IConexion;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 *
 * @author isaac
 */

public class MenuFrame extends JFrame {

    private JLabel bienvenidaLabel;
    private JButton medicoButton;
    private JButton pacienteButton;
    private final MedicoBO medicoBO;
    private final PacienteBO pacienteBO;
    private final UsuarioBO usuarioBO;
    private final DireccionPacienteBO direccionBO;

    public MenuFrame(IConexion conexion) {
        this.medicoBO = new MedicoBO(conexion);
        this.pacienteBO = new PacienteBO(conexion);
        this.usuarioBO = new UsuarioBO(conexion);
        this.direccionBO = new DireccionPacienteBO(conexion);
        initComponents();
        initializeComponents();
    }

    private void initComponents() {
        this.setTitle("Menú Principal - Hospital");
        this.setSize(400, 300);
        this.setLayout(null);

        // Elementos
        bienvenidaLabel = new JLabel("¡Hola! Selecciona tu perfil");
        medicoButton = new JButton("Médico");
        pacienteButton = new JButton("Paciente");

        // Posiciones
        bienvenidaLabel.setBounds(100, 50, 200, 20);
        medicoButton.setBounds(100, 100, 200, 30);
        pacienteButton.setBounds(100, 150, 200, 30);

        this.add(bienvenidaLabel);
        this.add(medicoButton);
        this.add(pacienteButton);

        // Oyentes de Eventos
        medicoButton.addActionListener(e -> mostrarFrameMedico());
        pacienteButton.addActionListener(e -> mostrarFramePaciente());
    }

    private void initializeComponents() {
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private void mostrarFrameMedico() {
        this.dispose();
        new MedicoInicio(medicoBO, this).setVisible(true);
    }

    private void mostrarFramePaciente() {
        this.dispose();
        new PacienteInicio(pacienteBO, this, direccionBO, usuarioBO ).setVisible(true);
    }

    public static void main(String[] args) {
        IConexion conexion = new Conexion();
        java.awt.EventQueue.invokeLater(() -> new MenuFrame(conexion).setVisible(true));
    }
}
