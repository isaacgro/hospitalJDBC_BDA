/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Conexion.IConexion;
import Entidades.Paciente;
import Entidades.Usuario;
import Excepciones.PersistenciaExcption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author isaac
 */


public class PacienteDAO implements IPacienteDAO {

    private final IConexion conexion;

    public PacienteDAO(IConexion conexion) {
        this.conexion = conexion;
    }

    @Override
    public boolean registrarPaciente(Paciente paciente) throws PersistenciaExcption {
        String consultaSQL = "INSERT INTO pacientes (fecha_nacimiento, edad, telefono, correoE, id_Usuario) VALUES (?, ?, ?, ?, ?)";

        try (Connection cone = this.conexion.crearConexion();
             PreparedStatement ps = cone.prepareStatement(consultaSQL, Statement.RETURN_GENERATED_KEYS)) {

            ps.setDate(1, java.sql.Date.valueOf(paciente.getFecha_nacimiento()));
            ps.setInt(2, paciente.getEdad());
            ps.setString(3, paciente.getTelefono());
            ps.setString(4, paciente.getCorreoE());
            ps.setInt(5, paciente.getUsuario().getId_Usuario());

            int resultado = ps.executeUpdate();
            if (resultado == 0) {
                throw new PersistenciaExcption("No se pudo registrar el paciente");
            }

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    paciente.setId_Paciente(rs.getInt(1));
                } else {
                    throw new PersistenciaExcption("No se pudo obtener el ID del paciente.");
                }
            }

        } catch (SQLException e) {
            throw new PersistenciaExcption("Error al registrar paciente: " + e.getMessage(), e);
        }

        return true;
    }

    @Override
    public Paciente buscarPacientePorCorreoyContra(String correo, String contra) throws PersistenciaExcption {
        String consultaSQL = "SELECT p.*, u.id_Usuario, u.contra, u.nombre, u.apellidoP, u.apellidoM "
                + "FROM pacientes p "
                + "INNER JOIN usuarios u ON p.id_Usuario = u.id_Usuario "
                + "WHERE p.correoE = ? AND u.contra = ?";

        try (Connection cone = this.conexion.crearConexion();
             PreparedStatement ps = cone.prepareStatement(consultaSQL)) {

            ps.setString(1, correo);
            ps.setString(2, contra);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Crear la entidad Usuario con todos sus atributos
                    Usuario usuario = new Usuario(
                            rs.getInt("id_Usuario"),
                            rs.getString("contra"),
                            rs.getString("nombre"),
                            rs.getString("apellidoP"),
                            rs.getString("apellidoM")
                    );

                    // Crear la entidad Paciente con la referencia al Usuario
                    return new Paciente(
                            rs.getInt("id_Paciente"),
                            rs.getDate("fecha_nacimiento").toLocalDate(),
                            rs.getInt("edad"),
                            rs.getString("telefono"),
                            rs.getString("correoE"),
                            usuario
                    );
                }
            }
        } catch (SQLException e) {
            throw new PersistenciaExcption("Error al buscar paciente: " + e.getMessage(), e);
        }

        return null;
    }
    
    
}

