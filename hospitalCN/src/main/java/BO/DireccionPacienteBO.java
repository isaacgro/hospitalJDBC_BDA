/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BO;

import Conexion.IConexion;
import DAO.DireccionPacienteDAO;
import DAO.IDireccionPacienteDAO;
import DTO.DireccionPacienteDTO;
import Entidades.DireccionPaciente;
import Excepciones.PersistenciaExcption;
import Exception.NegocioException;
import Mapper.Mapper;

/**
 *
 * @author isaac
 */
public class DireccionPacienteBO {
    private final IDireccionPacienteDAO direccionDAO;

    public DireccionPacienteBO(IConexion conexion) {
        this.direccionDAO = new DireccionPacienteDAO(conexion);
    }

    public boolean registrarDireccion(DireccionPacienteDTO direccionDTO) throws NegocioException {
        try {
            DireccionPaciente direccion = Mapper.toEntity(direccionDTO);
            return direccionDAO.registrarDireccion(direccion);
        } catch (PersistenciaExcption e) {
            throw new NegocioException("Error al registrar dirección: " + e.getMessage(), e);
        }
    }
}


