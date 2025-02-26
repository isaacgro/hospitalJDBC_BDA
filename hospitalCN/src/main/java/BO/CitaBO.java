/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BO;

import Conexion.IConexion;
import DAO.CitaDAO;
import DAO.ICitaDAO;
import DTO.CitaDTO;
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
}
