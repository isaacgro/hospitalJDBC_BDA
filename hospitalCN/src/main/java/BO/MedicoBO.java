/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
 /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BO;

import Conexion.IConexion;
import DAO.IMedicoDAO;
import DAO.MedicoDAO;
import DTO.MedicoDTO;
import Entidades.Medico;
import Excepciones.PersistenciaExcption;
import Exception.NegocioException;
import Mapper.Mapper;

/**
 *
 * @author isaac
 */
public class MedicoBO {

    private final IMedicoDAO medicoDAO;

    public MedicoBO(IConexion conexion) {
        this.medicoDAO = new MedicoDAO(conexion);
    }

    public boolean autenticarMedico(String cedulaPro, String contraseña) throws NegocioException {
        try {
            return medicoDAO.autenticarMedico(cedulaPro, contraseña);
        } catch (PersistenciaExcption e) {
            throw new NegocioException("Error al autenticar médico: " + e.getMessage(), e);
        }
    }

    public MedicoDTO obtenerMedicoPorCedula(String cedulaPro) throws NegocioException {
        try {
            Medico medico = medicoDAO.obtenerMedicoPorCedula(cedulaPro);
            return Mapper.toDTO(medico);
        } catch (PersistenciaExcption e) {
            throw new NegocioException("Error al obtener datos del médico: " + e.getMessage(), e);
        }
    }

}
