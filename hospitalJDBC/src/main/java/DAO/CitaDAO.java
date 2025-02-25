package DAO;

import Conexion.IConexion;
import Entidades.Cita;
import Excepciones.PersistenciaExcption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
}
