/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;
import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ArrayList;
import java.sql.*;
import DTO.CitaDTO;
import DTO.PacienteDTO;
import BO.CitaBO;
import Conexion.Conexion;
import DTO.UsuarioDTO;
import Excepciones.PersistenciaExcption;

public class AgendaCitasApp {
    private final CitaBO citaBO;
    private final int idMedicoActual; // ID del médico en sesión
    
    public AgendaCitasApp(int idMedicoActual) {
        this.citaBO = new CitaBO(new Conexion());
        this.idMedicoActual = idMedicoActual;
    }

    public static void main(String[] args) {
        int idMedico = 1; // Simulación del ID del médico en sesión
        SwingUtilities.invokeLater(() -> new AgendaCitasApp(idMedico).createAndShowGUI());
    }

    private void createAndShowGUI() {
        JFrame frame = new JFrame("Agenda de Citas");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Agenda de Citas", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        frame.add(titleLabel, BorderLayout.NORTH);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        List<CitaDTO> citas = obtenerCitasDesdeBD();

        for (CitaDTO cita : citas) {
            JPanel citaPanel = new JPanel();
            citaPanel.setLayout(new BorderLayout());

            JPanel bubbleWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JPanel bubble = createBubble(cita.getTipo(), cita.getTipo().equalsIgnoreCase("emergencia"));
            bubbleWrapper.add(bubble);
            citaPanel.add(bubbleWrapper, BorderLayout.CENTER);

            DateTimeFormatter horaFormatter = DateTimeFormatter.ofPattern("hh:mm a");
            JLabel horaLabel = new JLabel(cita.getFecha_hora().toLocalDateTime().format(horaFormatter));
            horaLabel.setFont(new Font("Arial", Font.BOLD, 12));
            citaPanel.add(horaLabel, BorderLayout.WEST);

            DateTimeFormatter fechaFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            JLabel fechaLabel = new JLabel(cita.getFecha_hora().toLocalDateTime().format(fechaFormatter), SwingConstants.CENTER);
            fechaLabel.setFont(new Font("Arial", Font.PLAIN, 8));
            citaPanel.add(fechaLabel, BorderLayout.SOUTH);

            JLabel pacienteLabel = new JLabel("Paciente: " + cita.getPaciente().getUsuario().getNombre());
            citaPanel.add(pacienteLabel, BorderLayout.EAST);

            contentPanel.add(citaPanel);
        }

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        frame.add(scrollPane, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    private JPanel createBubble(String text, boolean esEmergencia) {
        JPanel panel = new JPanel();
        panel.setBackground(esEmergencia ? Color.RED : Color.CYAN);
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        panel.setMaximumSize(new Dimension(250, 30));
        panel.add(new JLabel(text));
        return panel;
    }
    
    private List<CitaDTO> obtenerCitasDesdeBD() {
        List<CitaDTO> citas = new ArrayList<>();
        try {
            Connection conn = new Conexion().crearConexion();
            PreparedStatement stmt = conn.prepareStatement("SELECT id_Cita, estado, fecha_hora, id_Paciente, tipo FROM Citas WHERE id_Medico = ?");
            stmt.setInt(1, idMedicoActual);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                UsuarioDTO usuario = obtenerUsuarioDesdeBD(rs.getInt("id_Paciente"));
                PacienteDTO paciente = new PacienteDTO(rs.getInt("id_Paciente"), null, 0, "", "", usuario);
                CitaDTO cita = new CitaDTO(
                    rs.getInt("id_Cita"),
                    rs.getString("estado"),
                    rs.getTimestamp("fecha_hora"),
                    paciente,
                    rs.getString("tipo")
                );
                citas.add(cita);
            }
            
            rs.close();
            stmt.close();
            conn.close();
        } catch (PersistenciaExcption | SQLException e) {
            e.printStackTrace();
        }
        return citas;
    }
    
    private UsuarioDTO obtenerUsuarioDesdeBD(int idPaciente) {
        try {
            Connection conn = new Conexion().crearConexion();
            PreparedStatement stmt = conn.prepareStatement("SELECT nombre FROM Usuarios WHERE id_Usuario = ?");
            stmt.setInt(1, idPaciente);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new UsuarioDTO(rs.getString("nombre"));
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new UsuarioDTO();
    }
}











    

