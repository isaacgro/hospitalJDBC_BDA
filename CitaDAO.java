package DAO;

import Conexion.IConexion;
import Entidades.Cita;
import Excepciones.PersistenciaExcption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CitaDAO implements ICitaDAO {

    private final IConexion conexion;

    public CitaDAO(IConexion conexion) {
        this.conexion = conexion;
    }

    public boolean registraCitaR(Cita cita) throws PersistenciaExcption {
        String consultaSQL = "INSERT INTO citas (estado, fecha_hora, id_Paciente, id_Medico, tipo) VALUES (?,?,?,?,?)";
        String verificacionSQL = "SELECT COUNT(*) FROM citas WHERE fecha_hora = ? AND id_Medico = ?";
        String proximaFechaSQL = "SELECT fecha_hora FROM citas WHERE id_Medico = ? AND fecha_hora > NOW() ORDER BY fecha_hora ASC LIMIT 1";

        int r = 0;
        Connection cone = null;

        try {
            cone = this.conexion.crearConexion();
            cone.setAutoCommit(false);

            // Si la cita es de emergencia, buscar el próximo horario disponible o crear uno nuevo
            if (cita.getTipo().equalsIgnoreCase("emergencia")) {
                try (PreparedStatement ps = cone.prepareStatement(proximaFechaSQL)) {
                    ps.setInt(1, cita.getMedico().getId_Medico());
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            // Si hay una cita futura, asignar la siguiente
                            cita.setFecha_hora(rs.getTimestamp(1));
                        } else {
                            // Si no hay citas futuras, buscar la última cita registrada
                            String buscarHorarioSQL = "SELECT MAX(fecha_hora) FROM citas WHERE id_Medico = ?";
                            try (PreparedStatement buscarHorarioPS = cone.prepareStatement(buscarHorarioSQL)) {
                                buscarHorarioPS.setInt(1, cita.getMedico().getId_Medico());
                                try (ResultSet horarioRS = buscarHorarioPS.executeQuery()) {
                                    if (horarioRS.next() && horarioRS.getTimestamp(1) != null) {
                                        // Si hay una cita registrada, programar la nueva ya que se acabe la que en esta (30 mins despues)
                                        cita.setFecha_hora(new Timestamp(horarioRS.getTimestamp(1).getTime() + 1800000));
                                    } else {
                                        // Si no hay citas registradas, asignar la cita inmediatamente
                                        cita.setFecha_hora(new Timestamp(System.currentTimeMillis()));
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                // Verificar si la fecha/hora ya está ocupada para citas regulares
                try (PreparedStatement vrs = cone.prepareStatement(verificacionSQL)) {
                    vrs.setTimestamp(1, cita.getFecha_hora());
                    vrs.setInt(2, cita.getMedico().getId_Medico());
                    try (ResultSet res = vrs.executeQuery()) {
                        if (res.next() && res.getInt(1) > 0) {
                            throw new PersistenciaExcption("El horario se encuentra ocupado");
                        }
                    }
                }
            }

            // Inserción de la cita
            try (PreparedStatement ps = cone.prepareStatement(consultaSQL, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, cita.getEstado());
                ps.setTimestamp(2, cita.getFecha_hora());
                ps.setInt(3, cita.getPaciente().getId_Paciente());
                ps.setInt(4, cita.getMedico().getId_Medico());
                ps.setString(5, cita.getTipo().toLowerCase());

                r = ps.executeUpdate();

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        int idCita = rs.getInt(1);
                        if (cita.getTipo().equalsIgnoreCase("regular")) {
                            String consultaSQLRegular = "INSERT INTO citasR (id_Cita) VALUES (?)";
                            try (PreparedStatement psRegular = cone.prepareStatement(consultaSQLRegular)) {
                                psRegular.setInt(1, idCita);
                                psRegular.executeUpdate();
                            }
                        } else {
                            String consultaSQLEmergencia = "INSERT INTO citasE (id_Cita) VALUES (?)";
                            try (PreparedStatement psEmergencia = cone.prepareStatement(consultaSQLEmergencia)) {
                                psEmergencia.setInt(1, idCita);
                                psEmergencia.executeUpdate();
                            }
                        }
                    } else {
                        throw new PersistenciaExcption("No se pudo generar la conexión");
                    }
                }
            }

            cone.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            if (cone != null) {
                try {
                    cone.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            throw new PersistenciaExcption("No se pudo realizar la operación: " + e.getMessage());
        } finally {
            if (cone != null) {
                try {
                    cone.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return r != 0;
    }
}
