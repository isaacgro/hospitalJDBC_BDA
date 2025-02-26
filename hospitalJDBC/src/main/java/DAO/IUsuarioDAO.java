/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package DAO;

import Entidades.Usuario;
import Excepciones.PersistenciaExcption;

/**
 *
 * @author isaac
 */
public interface IUsuarioDAO {
    
    boolean autenticarUsuario(int id_Usuario, String contra ) throws PersistenciaExcption;
    
    boolean crearUsuario(Usuario usuario) throws PersistenciaExcption;
    
    boolean existeCorreo(String correo) throws PersistenciaExcption;
    
    public Usuario obtenerUsuario (int id_Usuario)throws PersistenciaExcption;
    
}
