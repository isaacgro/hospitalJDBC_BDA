/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BO;

import Conexion.IConexion;
import DAO.ConsultaDAO;
import DAO.IConsultaDAO;
import DTO.ConsultaDTO;
import DTO.MedicoDTO;
import Entidades.Consulta;
import Entidades.Medico;
import Excepciones.PersistenciaExcption;
import Exception.NegocioException;
import Mapper.Mapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author isaac
 */
public class ConsultaBO {
    
    private final IConsultaDAO consultaDAO;
    
    public ConsultaBO(IConexion conexion) {
        this.consultaDAO = new ConsultaDAO(conexion);
    }
    
    /**
     * Obtiene la lista de consultas asociadas a un paciente, incluyendo la información del médico
     * @param idPaciente ID del paciente
     * @return Lista de ConsultaDTO con la información del médico incluida
     * @throws NegocioException En caso de error al recuperar los datos
     */
    public List<ConsultaDTO> obtenerConsultasPorPaciente(int idPaciente) throws NegocioException {
        try {
            // Obtener las consultas con los médicos asociados
            Map<Consulta, Medico> consultasConMedico = consultaDAO.obtenerConsultasConMedicoPorPaciente(idPaciente);
            
            // Convertir a lista de DTOs
            List<ConsultaDTO> consultasDTO = new ArrayList<>();
            
            for (Map.Entry<Consulta, Medico> entry : consultasConMedico.entrySet()) {
                Consulta consulta = entry.getKey();
                Medico medico = entry.getValue();
                
                // Convertir Consulta a DTO
                ConsultaDTO consultaDTO = Mapper.toDTO(consulta);
                
                // Asignar el médico convertido a DTO
                if (medico != null) {
                    MedicoDTO medicoDTO = Mapper.toDTO(medico);
                    consultaDTO.setMedicoAsociado(medicoDTO);
                }
                
                consultasDTO.add(consultaDTO);
            }
            
            return consultasDTO;
        } catch (PersistenciaExcption ex) {
            throw new NegocioException("Error al obtener el historial de consultas: " + ex.getMessage(), ex);
        }
    }
    
    /**
     * Obtiene una consulta específica por su ID
     * @param idConsulta ID de la consulta
     * @return ConsultaDTO con los detalles de la consulta y el médico
     * @throws NegocioException En caso de error al recuperar los datos
     */
    public ConsultaDTO obtenerConsultaPorId(int idConsulta) throws NegocioException {
        try {
            Map<Consulta, Medico> consultaConMedico = consultaDAO.obtenerConsultaConMedicoPorId(idConsulta);
            
            if (consultaConMedico.isEmpty()) {
                return null;
            }
            
            // Obtener la única entrada del mapa
            Map.Entry<Consulta, Medico> entry = consultaConMedico.entrySet().iterator().next();
            Consulta consulta = entry.getKey();
            Medico medico = entry.getValue();
            
            // Convertir Consulta a DTO
            ConsultaDTO consultaDTO = Mapper.toDTO(consulta);
            
            // Asignar el médico convertido a DTO
            if (medico != null) {
                MedicoDTO medicoDTO = Mapper.toDTO(medico);
                consultaDTO.setMedicoAsociado(medicoDTO);
            }
            
            return consultaDTO;
        } catch (PersistenciaExcption ex) {
            throw new NegocioException("Error al obtener detalles de la consulta: " + ex.getMessage(), ex);
        }
    }
}