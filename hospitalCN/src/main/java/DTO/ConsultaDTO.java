/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DTO;

import Entidades.Medico;
import java.time.LocalDateTime;

/**
 *
 * @author mmax2
 */
public class ConsultaDTO {

    private int id_Consulta;
    private LocalDateTime fecha_hora;
    private String tratamiento;
    private String diagnostico;
    
     // Agregamos estas propiedades para almacenar los datos del médico asociado
    // Esto no está en la entidad Consulta, pero lo necesitamos para mostrar en la UI
    private MedicoDTO medicoAsociado;
    private PacienteDTO pacienteAsociado;
    

    public ConsultaDTO() {
    }

    

    public int getId_Consulta() {
        return id_Consulta;
    }

    public void setId_Consulta(int id_Consulta) {
        this.id_Consulta = id_Consulta;
    }

    public LocalDateTime getFecha_hora() {
        return fecha_hora;
    }

    public void setFecha_hora(LocalDateTime fecha_hora) {
        this.fecha_hora = fecha_hora;
    }

    public String getTratamiento() {
        return tratamiento;
    }

    public void setTratamiento(String tratamiento) {
        this.tratamiento = tratamiento;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    public MedicoDTO getMedicoAsociado() {
        return medicoAsociado;
    }

    public void setMedicoAsociado(MedicoDTO medicoAsociado) {
        this.medicoAsociado = medicoAsociado;
    }

    public PacienteDTO getPacienteAsociado() {
        return pacienteAsociado;
    }

    public void setPacienteAsociado(PacienteDTO pacienteAsociado) {
        this.pacienteAsociado = pacienteAsociado;
    }

    @Override
    public String toString() {
        return "ConsultaDTO{" + "id_Consulta=" + id_Consulta + ", fecha_hora=" + fecha_hora + ", tratamiento=" + tratamiento + ", diagnostico=" + diagnostico + ", medicoAsociado=" + medicoAsociado + ", pacienteAsociado=" + pacienteAsociado + '}';
    }
    
}