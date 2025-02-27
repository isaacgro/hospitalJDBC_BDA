/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BO;

import Conexion.IConexion;
import DAO.CitaDAO;
import DAO.ICitaDAO;
import DTO.CitaDTO;
import DTO.MedicoDTO;
import DTO.PacienteDTO;
import DTO.UsuarioDTO;
import Entidades.Cita;
import Excepciones.PersistenciaExcption;
import Exception.NegocioException;
import Mapper.Mapper;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author janethcristinagalvanquinonez
 */
public class CitaBO {

    private final ICitaDAO citaDAO;

    public CitaBO(IConexion conexion) {
        this.citaDAO = new CitaDAO(conexion);
    }

    public void registrarCitaR(CitaDTO citaDTO) throws NegocioException {
        try {
            citaDAO.registraCitaR(Mapper.toEntity(citaDTO));
        } catch (PersistenciaExcption e) {
            throw new NegocioException("Error al registrar la cita: " + e.getMessage(), e);
        }
    }

    public List<CitaDTO> obtenerCitasPendientesPorPaciente(int idPaciente) throws NegocioException {
        try {
            List<Cita> citas = citaDAO.obtenerCitasPendientesPorPaciente(idPaciente);
            List<CitaDTO> citasDTO = new ArrayList<>();

            for (Cita cita : citas) {
                citasDTO.add(Mapper.toDTO(cita));
            }

            return citasDTO;
        } catch (PersistenciaExcption e) {
            throw new NegocioException("Error al obtener citas pendientes: " + e.getMessage(), e);
        }
    }

    public boolean actualizarEstadoCita(CitaDTO citaDTO) throws NegocioException {
        try {
            Cita cita = Mapper.toEntity(citaDTO);
            return citaDAO.actualizarEstadoCita(cita); // Añadir el nombre del método
        } catch (PersistenciaExcption e) {
            throw new NegocioException("Error al actualizar estado de la cita: " + e.getMessage(), e);
        }
    }
    
    public List<CitaDTO> obtenerCitasPorMedico(int idMedico) throws NegocioException {
        try {
            // Obtener las citas desde la capa de persistencia
            List<Cita> citas = citaDAO.obtenerCitasPorMedico(idMedico);
            
            // Convertir de entidades a DTOs
            List<CitaDTO> citasDTO = new ArrayList<>();
            for (Cita cita : citas) {
                CitaDTO citaDTO = new CitaDTO();
                citaDTO.setId_Cita(cita.getId_Cita());
                citaDTO.setEstado(cita.getEstado());
                citaDTO.setFecha_hora(cita.getFecha_hora());
                citaDTO.setTipo(cita.getTipo());

                // Convertir Paciente a PacienteDTO
                if (cita.getPaciente() != null) {
                    PacienteDTO pacienteDTO = new PacienteDTO(
                        cita.getPaciente().getId_Paciente(),
                        cita.getPaciente().getFecha_nacimiento(),
                        cita.getPaciente().getEdad(),
                        cita.getPaciente().getTelefono(),
                        cita.getPaciente().getCorreoE(),
                        new UsuarioDTO(
                            cita.getPaciente().getUsuario().getId_Usuario(),
                            cita.getPaciente().getUsuario().getContra(),
                            cita.getPaciente().getUsuario().getNombre(),
                            cita.getPaciente().getUsuario().getApellidoP(),
                            cita.getPaciente().getUsuario().getApellidoM()
                        )
                    );
                    citaDTO.setPaciente(pacienteDTO);
                }

                // Convertir Medico a MedicoDTO
                if (cita.getMedico() != null) {
                    MedicoDTO medicoDTO = new MedicoDTO(
                        cita.getMedico().getId_Medico(),
                        cita.getMedico().getCedulaPro(),
                        cita.getMedico().getEspecialidad(),
                            null, // aqui no ocupamos HorarioDTO realmente entocnes lo ponemos como nulo
                        new UsuarioDTO(
                            cita.getMedico().getUsuario().getId_Usuario(),
                            cita.getMedico().getUsuario().getContra(),
                            cita.getMedico().getUsuario().getNombre(),
                            cita.getMedico().getUsuario().getApellidoP(),
                            cita.getMedico().getUsuario().getApellidoM()
                        )
                    );
                    citaDTO.setMedico(medicoDTO);
                }

                citasDTO.add(citaDTO);
            }

            return citasDTO;
        } catch (PersistenciaExcption e) {
            throw new NegocioException("Error al obtener citas del médico", e);
        }
    }
}

