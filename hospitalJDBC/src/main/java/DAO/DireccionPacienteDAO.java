/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Conexion.IConexion;
import Entidades.DireccionPaciente;
import Excepciones.PersistenciaExcption;
import java.sql.Connection;
import java.sql.PreparedStatement;
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
}
