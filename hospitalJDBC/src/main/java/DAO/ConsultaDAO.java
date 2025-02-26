/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Conexion.IConexion;
import Entidades.Consulta;
import Entidades.Medico;
import Entidades.Usuario;
import Excepciones.PersistenciaExcption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author isaac
 */
public class ConsultaDAO implements IConsultaDAO {

    private final IConexion conexion;

    public ConsultaDAO(IConexion conexion) {
        this.conexion = conexion;
    }

    @Override
    public Map<Consulta, Medico> obtenerConsultasConMedicoPorPaciente(int idPaciente) throws PersistenciaExcption {
        String consultaSQL = "SELECT c.id_Consulta, c.fecha_hora, c.tratamiento, c.diagnostico, " +
                          "m.id_Medico, m.cedulaPro, m.especialidad, " +
                          "u.id_Usuario, u.nombre, u.apellidoP, u.apellidoM, u.contra " +
                          "FROM consultas c " +
                          "JOIN horarios h ON c.id_Consulta = h.id_Consulta " +
                          "JOIN horarios_consultas hc ON h.id_Horario = hc.id_Horario " +
                          "JOIN citas ct ON hc.id_Consulta = ct.id_Cita " +
                          "JOIN medicos m ON ct.id_Medico = m.id_Medico " +
                          "JOIN usuarios u ON m.id_Usuario = u.id_Usuario " +
                          "WHERE ct.id_Paciente = ? " +
                          "ORDER BY c.fecha_hora DESC";

        Map<Consulta, Medico> consultasConMedico = new HashMap<>();

        try (Connection cone = this.conexion.crearConexion();
             PreparedStatement ps = cone.prepareStatement(consultaSQL)) {

            ps.setInt(1, idPaciente);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    // Crear el objeto Usuario para el médico
                    Usuario usuarioMedico = new Usuario(
                        rs.getInt("id_Usuario"),
                        rs.getString("contra"),
                        rs.getString("nombre"),
                        rs.getString("apellidoP"),
                        rs.getString("apellidoM")
                    );
                    
                    // Crear el objeto Médico
                    Medico medico = new Medico(
                        rs.getInt("id_Medico"),
                        rs.getString("cedulaPro"),
                        rs.getString("especialidad"),
                        null, // Horario no necesario para este caso
                        usuarioMedico
                    );
                    
                    // Convertir Timestamp a LocalDateTime
                    Timestamp fechaHoraTimestamp = rs.getTimestamp("fecha_hora");
                    LocalDateTime fechaHora = fechaHoraTimestamp.toLocalDateTime();
                    
                    // Crear el objeto Consulta
                    Consulta consulta = new Consulta(
                        rs.getInt("id_Consulta"),
                        fechaHora,
                        rs.getString("tratamiento"),
                        rs.getString("diagnostico")
                    );
                    
                    // Añadir al mapa la consulta y el médico asociado
                    consultasConMedico.put(consulta, medico);
                }
            }
            
            return consultasConMedico;

        } catch (SQLException e) {
            throw new PersistenciaExcption("Error al obtener las consultas del paciente: " + e.getMessage(), e);
        }
    }

    @Override
    public Map<Consulta, Medico> obtenerConsultaConMedicoPorId(int idConsulta) throws PersistenciaExcption {
        String consultaSQL = "SELECT c.id_Consulta, c.fecha_hora, c.tratamiento, c.diagnostico, " +
                          "m.id_Medico, m.cedulaPro, m.especialidad, " +
                          "u.id_Usuario, u.nombre, u.apellidoP, u.apellidoM, u.contra " +
                          "FROM consultas c " +
                          "JOIN horarios h ON c.id_Consulta = h.id_Consulta " +
                          "JOIN horarios_consultas hc ON h.id_Horario = hc.id_Horario " +
                          "JOIN citas ct ON hc.id_Consulta = ct.id_Cita " +
                          "JOIN medicos m ON ct.id_Medico = m.id_Medico " +
                          "JOIN usuarios u ON m.id_Usuario = u.id_Usuario " +
                          "WHERE c.id_Consulta = ?";

        Map<Consulta, Medico> consultaConMedico = new HashMap<>();

        try (Connection cone = this.conexion.crearConexion();
             PreparedStatement ps = cone.prepareStatement(consultaSQL)) {

            ps.setInt(1, idConsulta);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Crear el objeto Usuario para el médico
                    Usuario usuarioMedico = new Usuario(
                        rs.getInt("id_Usuario"),
                        rs.getString("contra"),
                        rs.getString("nombre"),
                        rs.getString("apellidoP"),
                        rs.getString("apellidoM")
                    );
                    
                    // Crear el objeto Médico
                    Medico medico = new Medico(
                        rs.getInt("id_Medico"),
                        rs.getString("cedulaPro"),
                        rs.getString("especialidad"),
                        null, // Horario no necesario para este caso
                        usuarioMedico
                    );
                    
                    // Convertir Timestamp a LocalDateTime
                    Timestamp fechaHoraTimestamp = rs.getTimestamp("fecha_hora");
                    LocalDateTime fechaHora = fechaHoraTimestamp.toLocalDateTime();
                    
                    // Crear el objeto Consulta
                    Consulta consulta = new Consulta(
                        rs.getInt("id_Consulta"),
                        fechaHora,
                        rs.getString("tratamiento"),
                        rs.getString("diagnostico")
                    );
                    
                    // Añadir al mapa la consulta y el médico asociado
                    consultaConMedico.put(consulta, medico);
                }
            }
            
            return consultaConMedico;

        } catch (SQLException e) {
            throw new PersistenciaExcption("Error al obtener la consulta: " + e.getMessage(), e);
        }
    }
}
