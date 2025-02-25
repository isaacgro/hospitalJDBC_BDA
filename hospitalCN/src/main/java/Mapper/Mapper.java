/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Mapper;

import DTO.*;
import Entidades.*;

/**
 *
 * @author janethcristinagalvanquinonez
 */
public class Mapper {

    public static Cita toEntity(CitaDTO citaDTO) {
        if (citaDTO == null) {
            return null;
        }

        return new Cita(
                citaDTO.getId_Cita(),
                citaDTO.getEstado(),
                citaDTO.getFecha_hora(),
                toEntity(citaDTO.getPaciente()),
                toEntity(citaDTO.getMedico()),
                citaDTO.getTipo()
        );
    }

    public static Paciente toEntity(PacienteDTO pacienteDTO) {
        if (pacienteDTO == null) {
            return null;
        }

        return new Paciente(
                pacienteDTO.getId_Paciente(),
                pacienteDTO.getFecha_nacimiento(),
                pacienteDTO.getEdad(),
                pacienteDTO.getTelefono(),
                pacienteDTO.getCorreoE(),
                toEntity(pacienteDTO.getUsuario())
        );
    }

    public static Usuario toEntity(UsuarioDTO usuarioDTO) { // ACOMODAR 
        if (usuarioDTO == null) {
            return null;
        }

        return new Usuario(
                usuarioDTO.getId_Usuario(),
                usuarioDTO.getContra(), // Correcto: contraseña
                usuarioDTO.getNombre(), // Correcto: nombre
                usuarioDTO.getApellidoP(), // Correcto: apellido paterno
                usuarioDTO.getApellidoM() // Correcto: apellido materno
        );
    }

    public static Medico toEntity(MedicoDTO medicoDTO) {
        if (medicoDTO == null) {
            return null;
        }

        return new Medico(
                medicoDTO.getId_Medico(),
                medicoDTO.getCedulaPro(),
                medicoDTO.getEspecialidad(),
                toEntity(medicoDTO.getHorario()),
                toEntity(medicoDTO.getUsuario())
        );
    }

    public static PacienteDTO toDTO(Paciente paciente) {
        if (paciente == null) {
            return null;
        }

        return new PacienteDTO(
                paciente.getId_Paciente(),
                paciente.getFecha_nacimiento(),
                paciente.getEdad(),
                paciente.getTelefono(),
                paciente.getCorreoE(),
                toDTO(paciente.getUsuario())
        );
    }

    public static MedicoDTO toDTO(Medico medico) {
        if (medico == null) {
            return null;
        }

        return new MedicoDTO(
                medico.getId_Medico(),
                medico.getCedulaPro(),
                medico.getEspecialidad(),
                toDTO(medico.getHorario()),
                toDTO(medico.getUsuario())
        );
    }

    public static UsuarioDTO toDTO(Usuario usuario) {
        if (usuario == null) {
            return null;
        }

        

        // mismo orden 
        UsuarioDTO dto = new UsuarioDTO(
                usuario.getId_Usuario(),
                usuario.getContra(), // Verifica que estos estén en el orden correcto
                usuario.getNombre(), // según tu constructor de UsuarioDTO
                usuario.getApellidoP(),
                usuario.getApellidoM()
        );

        // Depuración para confirmar
        System.out.println("=== UsuarioDTO creado ===");
        System.out.println("ID: " + dto.getId_Usuario());
        System.out.println("Nombre: " + dto.getNombre());
        System.out.println("ApellidoP: " + dto.getApellidoP());
        System.out.println("ApellidoM: " + dto.getApellidoM());
        System.out.println("Contra: " + dto.getContra());
        System.out.println("================================");

        return dto;
    }

    public static Horario toEntity(HorarioDTO horarioDTO) {
        if (horarioDTO == null) {
            return null;
        }

        return new Horario(
                horarioDTO.getId_Horario(),
                horarioDTO.getFecha(),
                horarioDTO.getHora_inicio(),
                horarioDTO.getHora_fin(),
                toEntity(horarioDTO.getConsulta())
        );
    }

    public static HorarioDTO toDTO(Horario horario) {
        if (horario == null) {
            return null;
        }

        return new HorarioDTO(
                horario.getId_Horario(),
                horario.getFecha(),
                horario.getHora_inicio(),
                horario.getHora_fin(),
                toDTO(horario.getConsulta())
        );
    }

    public static ConsultaDTO toDTO(Consulta consulta) {
        if (consulta == null) {
            return null;
        }

        return new ConsultaDTO(
                consulta.getId_Consulta(),
                consulta.getFecha_hora(),
                consulta.getTratamiento(),
                consulta.getDiagnostico()
        );
    }

    public static Consulta toEntity(ConsultaDTO consultaDTO) {
        if (consultaDTO == null) {
            return null;
        }

        return new Consulta(
                consultaDTO.getId_Consulta(),
                consultaDTO.getFecha_hora(),
                consultaDTO.getTratamiento(),
                consultaDTO.getDiagnostico()
        );
    }

    public static DireccionPaciente toEntity(DireccionPacienteDTO direccionDTO) {
        if (direccionDTO == null) {
            return null;
        }
        return new DireccionPaciente(
                direccionDTO.getId_Direccion(),
                direccionDTO.getCalle(),
                direccionDTO.getNumExt(),
                direccionDTO.getColonia(),
                direccionDTO.getPaciente() != null ? Mapper.toEntity(direccionDTO.getPaciente()) : null
        );
    }

    public static DireccionPacienteDTO toDTO(DireccionPaciente direccion) {
        if (direccion == null) {
            return null;
        }
        return new DireccionPacienteDTO(
                direccion.getId_Direccion(),
                direccion.getCalle(),
                direccion.getNumExt(),
                direccion.getColonia(),
                direccion.getPaciente() != null ? Mapper.toDTO((Paciente) direccion.getPaciente()) : null
        );
    }
}
