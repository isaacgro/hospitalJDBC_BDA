/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package DAO;

import Entidades.Consulta;
import Entidades.Medico;
import Excepciones.PersistenciaExcption;
import java.util.List;
import java.util.Map;

/**
 *
 * @author isaac
 */
public interface IConsultaDAO {
    
    /**
     * Obtiene todas las consultas realizadas a un paciente
     * @param idPaciente ID del paciente
     * @return Lista de consultas
     * @throws PersistenciaExcption Si ocurre un error al acceder a la base de datos
     */
    Map<Consulta, Medico> obtenerConsultasConMedicoPorPaciente(int idPaciente) throws PersistenciaExcption;
    
    /**
     * Obtiene los detalles de una consulta específica
     * @param idConsulta ID de la consulta
     * @return Objeto Consulta o null si no se encuentra
     * @throws PersistenciaExcption Si ocurre un error al acceder a la base de datos
     */
    Map<Consulta, Medico> obtenerConsultaConMedicoPorId(int idConsulta) throws PersistenciaExcption;
}
