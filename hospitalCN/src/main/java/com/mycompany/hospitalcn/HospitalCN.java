/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.hospitalcn;

import BO.CitaBO;
import Conexion.Conexion;
import Conexion.IConexion;
import DTO.CitaDTO;
import DTO.ConsultaDTO;
import DTO.HorarioDTO;
import DTO.MedicoDTO;
import DTO.PacienteDTO;
import DTO.UsuarioDTO;
import Excepciones.PersistenciaExcption;
import Exception.NegocioException;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;

/**
 *
 * @author janethcristinagalvanquinonez
 */
public class HospitalCN {

    public static void main(String[] args) {
        try {
            IConexion conexion = new Conexion();
            CitaBO citaBO = new CitaBO(conexion);

            
            UsuarioDTO usuarioDTO = new UsuarioDTO(1, "Juan", "Pérez", "García", "12345");
            PacienteDTO pacienteDTO = new PacienteDTO(1, LocalDate.of(1990, 5, 20), 33, "555-1234", "juan@mail.com", usuarioDTO);
            MedicoDTO medicoDTO = new MedicoDTO(1, "ABC123", "Cardiología", null, usuarioDTO);

            CitaDTO nuevaCita = new CitaDTO(
                    0, 
                    "Pendiente",
                    Timestamp.valueOf("2025-03-10 10:00:00"),
                    pacienteDTO,
                    medicoDTO,
                    "regular"
            );

           
            citaBO.registrarCitaR(nuevaCita);
            System.out.println(" Cita registrada correctamente.");

        } catch (NegocioException e) {
            System.out.println(" Error al registrar la cita: " + e.getMessage());
        }
    }
}
