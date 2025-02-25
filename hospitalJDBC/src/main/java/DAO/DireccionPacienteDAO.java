/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Conexion.IConexion;
import Entidades.DireccionPaciente;
import Entidades.Paciente;
import Excepciones.PersistenciaExcption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author isaac
 */
public class DireccionPacienteDAO implements IDireccionPacienteDAO {

    private final IConexion conexion;

    public DireccionPacienteDAO(IConexion conexion) {
        this.conexion = conexion;
    }

    @Override
    public boolean registrarDireccion(DireccionPaciente direccion) throws PersistenciaExcption {
        String consultaSQL = "INSERT INTO direccion_pacientes (calle, numExt, colonia, id_Paciente) VALUES (?, ?, ?, ?)";

        try (Connection cone = this.conexion.crearConexion(); PreparedStatement ps = cone.prepareStatement(consultaSQL)) {

            ps.setString(1, direccion.getCalle());
            ps.setString(2, direccion.getNumExt());
            ps.setString(3, direccion.getColonia());
            ps.setInt(4, direccion.getPaciente().getId_Paciente());

            int resultado = ps.executeUpdate();
            return resultado > 0;

        } catch (SQLException e) {
            throw new PersistenciaExcption("Error al registrar dirección: " + e.getMessage(), e);
        }
    }

    @Override
    public DireccionPaciente obtenerDireccionPorPaciente(Paciente paciente) throws PersistenciaExcption {
        String consultaSQL = "SELECT * FROM direccion_pacientes WHERE id_Paciente = ?";

        try (Connection cone = this.conexion.crearConexion(); PreparedStatement ps = cone.prepareStatement(consultaSQL)) {

            ps.setInt(1, paciente.getId_Paciente());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new DireccionPaciente(
                            rs.getInt("id_Direccion"),
                            rs.getString("calle"),
                            rs.getString("numExt"),
                            rs.getString("colonia"),
                            paciente
                    );
                }
            }
        } catch (SQLException e) {
            throw new PersistenciaExcption("Error al obtener dirección del paciente: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public boolean actualizarDireccion(DireccionPaciente direccion) throws PersistenciaExcption {
        String consultaSQL = "UPDATE direccion_pacientes SET calle = ?, numExt = ?, colonia = ? WHERE id_Paciente = ?";

        try (Connection cone = this.conexion.crearConexion(); PreparedStatement ps = cone.prepareStatement(consultaSQL)) {

            ps.setString(1, direccion.getCalle());
            ps.setString(2, direccion.getNumExt());
            ps.setString(3, direccion.getColonia());
            ps.setInt(4, direccion.getPaciente().getId_Paciente());

            int resultado = ps.executeUpdate();
            return resultado > 0;
        } catch (SQLException e) {
            throw new PersistenciaExcption("Error al actualizar dirección del paciente: " + e.getMessage(), e);
        }
    }
}
