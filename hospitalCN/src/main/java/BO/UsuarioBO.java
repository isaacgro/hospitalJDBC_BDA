/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BO;

import Conexion.IConexion;
import DAO.IUsuarioDAO;
import DAO.UsuarioDAO;
import DTO.UsuarioDTO;
import Entidades.Usuario;
import Excepciones.PersistenciaExcption;
import Exception.NegocioException;
import Mapper.Mapper;

/**
 *
 * @author isaac
 */
public class UsuarioBO {

    private final IUsuarioDAO usuarioDAO;

    public UsuarioBO(IConexion conexion) {
        this.usuarioDAO = new UsuarioDAO(conexion);
    }

    /**
     * Método para autenticar a un usuario
     *
     * @param id_Usuario ID del usuario
     * @param contra Contraseña del usuario
     * @return Verdadero si la autenticación es exitosa, falso en caso contrario
     * @throws NegocioException En caso de error durante la autenticación
     */
    public boolean autenticarUsuario(int id_Usuario, String contra) throws NegocioException {
        try {
            return usuarioDAO.autenticarUsuario(id_Usuario, contra);
        } catch (PersistenciaExcption e) {
            throw new NegocioException("Error al autenticar usuario: " + e.getMessage(), e);
        }
    }

    /**
     * Método para crear un usuario nuevo
     *
     * @param usuarioDTO Objeto UsuarioDTO con los datos del nuevo usuario
     * @return Verdadero si el usuario se creó correctamente, falso en caso
     * contrario
     * @throws NegocioException En caso de error durante la creación del usuario
     */
    public UsuarioDTO crearUsuario(UsuarioDTO usuarioDTO) throws NegocioException {
        try {
            Usuario usuario = new Usuario(
                    usuarioDTO.getId_Usuario(),
                    usuarioDTO.getContra(), // Correcto: contraseña
                    usuarioDTO.getNombre(), // Correcto: nombre
                    usuarioDTO.getApellidoP(), // Correcto: apellido paterno
                    usuarioDTO.getApellidoM() // Correcto: apellido materno
            );

            boolean creado = usuarioDAO.crearUsuario(usuario);
            if (creado) {
                usuarioDTO.setId_Usuario(usuario.getId_Usuario()); // Asignar el ID generado
                return usuarioDTO;
            } else {
                throw new NegocioException("No se pudo crear el usuario.");
            }
        } catch (PersistenciaExcption e) {
            throw new NegocioException("Error al crear usuario: " + e.getMessage(), e);
        }
    }

    public boolean existeCorreo(String correo) throws NegocioException {
        try {
            return usuarioDAO.existeCorreo(correo);
        } catch (PersistenciaExcption e) {
            throw new NegocioException("Error al verificar el correo: " + e.getMessage(), e);
        }
    }

}
