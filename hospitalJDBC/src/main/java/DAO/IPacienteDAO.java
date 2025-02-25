/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package DAO;

import Entidades.Paciente;
import Excepciones.PersistenciaExcption;

/**
 *
 * @author isaac
 */
public interface IPacienteDAO {
    /**
     * Metodo para registrar un paciente en la base de datos
     * @param paciente Objeto Paciente con los datos del paciente
     * @return boolean Indica si el registro fue exitoso
     * @throws Excepciones.PersistenciaExcption
     */

    boolean registrarPaciente(Paciente paciente) throws PersistenciaExcption;
    
    Paciente buscarPacientePorCorreoyContra(String correo, String contraseña) throws PersistenciaExcption;
    
    boolean actualizarPaciente(Paciente paciente) throws PersistenciaExcption;

}
