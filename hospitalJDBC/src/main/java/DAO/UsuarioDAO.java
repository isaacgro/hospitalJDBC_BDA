/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Conexion.IConexion;
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
public class UsuarioDAO implements IUsuarioDAO {

    private final IConexion conexion;

    public UsuarioDAO(IConexion conexion) {
        this.conexion = conexion;
    }

    @Override
    public boolean autenticarUsuario(int id_Usuario, String contra) throws PersistenciaExcption {

        String consultaSQL = "SELECT COUNT(*) FROM usuarios WHERE id_Usuario = ? AND contra = ?";

        try (Connection cone = this.conexion.crearConexion(); PreparedStatement ps = cone.prepareStatement(consultaSQL)) {

            ps.setInt(1, id_Usuario);
            ps.setString(2, contra);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count > 0;
                }
                return false;
            }

        } catch (SQLException e) {
            throw new PersistenciaExcption("Error al autenticar usuario: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean crearUsuario(Usuario usuario) throws PersistenciaExcption {
        String consultaSQL = "INSERT INTO usuarios (contra, nombre, apellidoP, apellidoM) VALUES (?, ?, ?, ?)";

        try (Connection cone = this.conexion.crearConexion(); PreparedStatement ps = cone.prepareStatement(consultaSQL, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setString(1, usuario.getContra());
            ps.setString(2, usuario.getNombre());
            ps.setString(3, usuario.getApellidoP());
            ps.setString(4, usuario.getApellidoM());
           

            int resultado = ps.executeUpdate();
            if (resultado == 0) {
                throw new PersistenciaExcption("No se pudo insertar el usuario.");
            }

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    usuario.setId_Usuario(rs.getInt(1)); // Guardamos el ID generado
                } else {
                    throw new PersistenciaExcption("No se pudo obtener el ID del usuario.");
                }
            }
        } catch (SQLException e) {
            throw new PersistenciaExcption("Error al insertar usuario: " + e.getMessage(), e);
        }

        return true;
    }

    @Override
    public boolean existeCorreo(String correo) throws PersistenciaExcption {
        String consultaSQL = "SELECT COUNT(*) FROM pacientes WHERE correoE = ?";

        try (Connection cone = this.conexion.crearConexion(); PreparedStatement ps = cone.prepareStatement(consultaSQL)) {

            ps.setString(1, correo);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            return false;
        } catch (SQLException e) {
            throw new PersistenciaExcption("Error al verificar correo: " + e.getMessage(), e);
        }
    }
    
    public Usuario obtenerUsuario(int idUsuario) throws PersistenciaExcption {
    String sql = "SELECT id_Usuario, nombre, apellidoP, apellidoM FROM Usuarios WHERE id_Usuario = ?";

    try (Connection conn = conexion.crearConexion();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setInt(1, idUsuario);

        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                int idUsuarioResult = rs.getInt("id_Usuario");
                String nombre = rs.getString("nombre");
                String apellidoP = rs.getString("apellidoP");
                String apellidoM = rs.getString("apellidoM");

                return new Usuario(idUsuarioResult, "", nombre, apellidoP, apellidoM);
            } else {
                throw new PersistenciaExcption("No se encontró el usuario con ID: " + idUsuario);
            }
        }
    } catch (SQLException e) {
        throw new PersistenciaExcption("Error al obtener el usuario desde la base de datos", e);
    }
}

}
