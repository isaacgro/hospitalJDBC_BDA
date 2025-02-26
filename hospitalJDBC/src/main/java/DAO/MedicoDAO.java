/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Conexion.IConexion;
import Entidades.Medico;
import Entidades.Usuario;
import Excepciones.PersistenciaExcption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author isaac
 */
public class MedicoDAO implements IMedicoDAO {

    private final IConexion conexion;

    public MedicoDAO(IConexion conexion) {
        this.conexion = conexion;
    }

    @Override
    public boolean autenticarMedico(String cedulaPro, String contra) throws PersistenciaExcption {
        String consultaSQL = "SELECT COUNT(*) FROM medicos m "
                + "INNER JOIN usuarios u ON m.id_Usuario = u.id_Usuario "
                + "WHERE m.cedulaPro = ? AND u.contra = ?";

        try (Connection cone = this.conexion.crearConexion(); PreparedStatement ps = cone.prepareStatement(consultaSQL)) {

            ps.setString(1, cedulaPro);
            ps.setString(2, contra);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            throw new PersistenciaExcption("Error al autenticar al médico: " + e.getMessage(), e);
        }

        return false;
    }

    @Override
    public Medico obtenerMedicoPorCedula(String cedulaPro) throws PersistenciaExcption {
        String consultaSQL = "SELECT m.*, u.id_Usuario, u.nombre, u.apellidoP, u.apellidoM, u.contra "
                + "FROM medicos m "
                + "INNER JOIN usuarios u ON m.id_Usuario = u.id_Usuario "
                + "WHERE m.cedulaPro = ?";

        try (Connection cone = this.conexion.crearConexion(); PreparedStatement ps = cone.prepareStatement(consultaSQL)) {

            ps.setString(1, cedulaPro);

            try (ResultSet rs = ps.executeQuery()) { // corregir orden aqui 
                if (rs.next()) {
                    Usuario usuario = new Usuario(
                            rs.getInt("id_Usuario"),
                            rs.getString("contra"), 
                            rs.getString("nombre"), 
                            rs.getString("apellidoP"), 
                            rs.getString("apellidoM") 
                    );

                    return new Medico(
                            rs.getInt("id_Medico"),
                            rs.getString("cedulaPro"),
                            rs.getString("especialidad"),
                            null,
                            usuario
                    );
                }
            }
        } catch (SQLException e) {
            throw new PersistenciaExcption("Error al obtener el médico: " + e.getMessage(), e);
        }

        return null;
    
}
}
