package DAO;

import Conexion.IConexion;
import Entidades.Cita;
import Entidades.Medico;
import Entidades.Paciente;
import Entidades.Usuario;
import Excepciones.PersistenciaExcption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CitaDAO implements ICitaDAO {

    private final IConexion conexion;

    public CitaDAO(IConexion conexion) {
        this.conexion = conexion;
    }

    @Override
    public boolean registraCitaR(Cita cita) throws PersistenciaExcption {
        String consultaSQL = "INSERT INTO citas (estado, fecha_hora, id_Paciente, id_Medico, tipo) VALUES (?,?,?,?,?)";
        String Verificacion = "SELECT COUNT(*) FROM CITAS WHERE fecha_hora = ? and id_Medico = ?";

        int r = 0;

        try (Connection cone = this.conexion.crearConexion();) {
            cone.setAutoCommit(false);

            // Verificación de horario ocupado
            try (PreparedStatement vrs = cone.prepareStatement(Verificacion);) {
                vrs.setTimestamp(1, cita.getFecha_hora());
                vrs.setInt(2, cita.getMedico().getId_Medico());

                try (ResultSet res = vrs.executeQuery()) {
                    if (res.next() && res.getInt(1) > 0) {
                        throw new PersistenciaExcption("El horario se encuentra ocupado");
                    }
                }
            }

            // Validar tipo de cita antes de la inserción
            String tipo = cita.getTipo().toLowerCase();  // Convertir a minúsculas para evitar errores de mayúsculas/minúsculas
            if (!tipo.equals("regular") && !tipo.equals("emergencia")) {
                throw new PersistenciaExcption("Tipo de cita inválido: " + tipo);
            }

            // Inserción de la cita
            try (PreparedStatement ps = cone.prepareStatement(consultaSQL, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, cita.getEstado());
                ps.setTimestamp(2, cita.getFecha_hora());
                ps.setInt(3, cita.getPaciente().getId_Paciente());
                ps.setInt(4, cita.getMedico().getId_Medico());
                ps.setString(5, tipo);  // Aseguramos que solo pase 'regular' o 'emergencia'

                r = ps.executeUpdate();

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (!rs.next()) {
                        throw new PersistenciaExcption("NO se pudo generar la conexión");
                    }
                    int idP = rs.getInt(1);
                }
            }
            cone.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new PersistenciaExcption("No se pudo realizar la operación: " + e.getMessage());
        }

        return r != 0;
    }
    // En CitaDAO

    // En CitaDAO
    public List<Cita> obtenerCitasPendientesPorPaciente(int idPaciente) throws PersistenciaExcption {
        String consultaSQL = "SELECT c.*, p.*, m.*, u.*, mu.* "
                + "FROM citas c "
                + "JOIN pacientes p ON c.id_Paciente = p.id_Paciente "
                + "JOIN medicos m ON c.id_Medico = m.id_Medico "
                + "JOIN usuarios u ON p.id_Usuario = u.id_Usuario "
                + "JOIN usuarios mu ON m.id_Usuario = mu.id_Usuario "
                + "WHERE c.id_Paciente = ? AND c.estado = 'pendiente' "
                + "ORDER BY c.fecha_hora ASC";

        List<Cita> citas = new ArrayList<>();

        try (Connection cone = this.conexion.crearConexion(); PreparedStatement ps = cone.prepareStatement(consultaSQL)) {

            ps.setInt(1, idPaciente);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    // Crear Usuario del paciente
                    Usuario usuarioPaciente = new Usuario(
                            rs.getInt("u.id_Usuario"),
                            rs.getString("u.contra"),
                            rs.getString("u.nombre"),
                            rs.getString("u.apellidoP"),
                            rs.getString("u.apellidoM")
                    );

                    // Crear Paciente
                    Paciente paciente = new Paciente(
                            rs.getInt("p.id_Paciente"),
                            rs.getDate("p.fecha_nacimiento").toLocalDate(),
                            rs.getInt("p.edad"),
                            rs.getString("p.telefono"),
                            rs.getString("p.correoE"),
                            usuarioPaciente
                    );

                    // Crear Usuario del médico
                    Usuario usuarioMedico = new Usuario(
                            rs.getInt("mu.id_Usuario"),
                            rs.getString("mu.contra"),
                            rs.getString("mu.nombre"),
                            rs.getString("mu.apellidoP"),
                            rs.getString("mu.apellidoM")
                    );

                    // Crear Médico
                    Medico medico = new Medico(
                            rs.getInt("m.id_Medico"),
                            rs.getString("m.cedulaPro"),
                            rs.getString("m.especialidad"),
                            null, // No necesitamos horario aquí
                            usuarioMedico
                    );

                    // Crear Cita
                    Cita cita = new Cita(
                            rs.getInt("c.id_Cita"),
                            rs.getString("c.estado"),
                            rs.getTimestamp("c.fecha_hora"),
                            paciente,
                            medico,
                            rs.getString("c.tipo")
                    );

                    citas.add(cita);
                }
            }

            return citas;

        } catch (SQLException e) {
            throw new PersistenciaExcption("Error al obtener citas pendientes: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean actualizarEstadoCita(Cita cita) throws PersistenciaExcption {
        String consultaSQL = "UPDATE citas SET estado = ? WHERE id_Cita = ?";

        try (Connection cone = this.conexion.crearConexion(); PreparedStatement ps = cone.prepareStatement(consultaSQL)) {

            ps.setString(1, cita.getEstado());
            ps.setInt(2, cita.getId_Cita());

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            throw new PersistenciaExcption("Error al actualizar estado de la cita: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<Cita> obtenerCitasPorMedico(int idMedico) throws PersistenciaExcption {
    String consultaSQL = "SELECT c.*, p.*, m.*, u.*, mu.* "
            + "FROM citas c "
            + "JOIN pacientes p ON c.id_Paciente = p.id_Paciente "
            + "JOIN medicos m ON c.id_Medico = m.id_Medico "
            + "JOIN usuarios u ON p.id_Usuario = u.id_Usuario "
            + "JOIN usuarios mu ON m.id_Usuario = mu.id_Usuario "
            + "WHERE c.id_Medico = ? "
            + "ORDER BY c.fecha_hora ASC";
    List<Cita> citas = new ArrayList<>();
    try (Connection cone = this.conexion.crearConexion(); 
         PreparedStatement ps = cone.prepareStatement(consultaSQL)) {
        ps.setInt(1, idMedico);
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                // El código de mapeo de objetos es similar al método existente
                // ... (mismo código que en obtenerCitasPendientesPorPaciente)
            }
            return citas;
        }
    } catch (SQLException e) {
        throw new PersistenciaExcption("Error al obtener citas del médico: " + e.getMessage(), e);
    }
}
    
}
