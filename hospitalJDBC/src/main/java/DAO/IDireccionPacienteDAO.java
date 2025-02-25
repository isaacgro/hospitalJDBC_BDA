/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Entidades.DireccionPaciente;
import Entidades.Paciente;
import Excepciones.PersistenciaExcption;

/**
 *
 * @author isaac
 */
public interface IDireccionPacienteDAO {
    
    boolean registrarDireccion(DireccionPaciente direccion) throws PersistenciaExcption;
    
    DireccionPaciente obtenerDireccionPorPaciente(Paciente paciente) throws PersistenciaExcption;
    
    boolean actualizarDireccion(DireccionPaciente direccion) throws PersistenciaExcption;
    
            
          
}

