/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package BO;

import Conexion.IConexion;
import DAO.CitaDAO;
import DAO.ICitaDAO;
import DAO.IPacienteDAO;
import DAO.PacienteDAO;
import DTO.CitaDTO;
import DTO.PacienteDTO;
import Entidades.Paciente;
import Excepciones.PersistenciaExcption;
import Exception.NegocioException;
import Mapper.Mapper;



/**
 *
 * @author isaac
 */
public class PacienteBO {

    private final IPacienteDAO pacienteDAO;

    public PacienteBO(IConexion conexion) {
        this.pacienteDAO = new PacienteDAO(conexion);
    }

    public boolean registrarPaciente(PacienteDTO pacienteDTO) throws NegocioException {
        try {
            return pacienteDAO.registrarPaciente(Mapper.toEntity(pacienteDTO));
        } catch (PersistenciaExcption e) {
            throw new NegocioException("Error al registrar el paciente: " + e.getMessage(), e);
        }
    }

    public PacienteDTO buscarPacientePorCorreoyContra(String correo, String contraseña) throws NegocioException {
        try {
            Paciente paciente = pacienteDAO.buscarPacientePorCorreoyContra(correo, contraseña);
            return paciente != null ? Mapper.toDTO(paciente) : null;
        } catch (PersistenciaExcption e) {
            throw new NegocioException("Error al autenticar paciente: " + e.getMessage(), e);
        }
    }
}
