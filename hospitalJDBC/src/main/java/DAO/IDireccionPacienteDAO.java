/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Entidades.DireccionPaciente;
import Excepciones.PersistenciaExcption;

/**
 *
 * @author isaac
 */
public interface IDireccionPacienteDAO {
    
    boolean registrarDireccion(DireccionPaciente direccion) throws PersistenciaExcption;
}

