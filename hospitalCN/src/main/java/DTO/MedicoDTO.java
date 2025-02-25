/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DTO;

import Entidades.Horario;
import Entidades.Usuario;

/**
 *
 * @author mmax2
 */
public class MedicoDTO {
    private int id_Medico;
    private String cedulaPro;
    private String especialidad;
    private HorarioDTO horario;
    private UsuarioDTO usuario; 

    public MedicoDTO(int id_Medico, String cedulaPro, String especialidad, HorarioDTO horario, UsuarioDTO usuario) {
        this.id_Medico = id_Medico;
        this.cedulaPro = cedulaPro;
        this.especialidad = especialidad;
        this.horario = horario;
        this.usuario = usuario;
    }

    public int getId_Medico() {
        return id_Medico;
    }

    public void setId_Medico(int id_Medico) {
        this.id_Medico = id_Medico;
    }

    public String getCedulaPro() {
        return cedulaPro;
    }

    public void setCedulaPro(String cedulaPro) {
        this.cedulaPro = cedulaPro;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public HorarioDTO getHorario() {
        return horario;
    }

    public void setHorario(HorarioDTO horario) {
        this.horario = horario;
    }

    public UsuarioDTO getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioDTO usuario) {
        this.usuario = usuario;
    }

    @Override
    public String toString() {
        return "MedicoDTO{" + "id_Medico=" + id_Medico + ", cedulaPro=" + cedulaPro + ", especialidad=" + especialidad + ", horario=" + horario + ", usuario=" + usuario + '}';
    }

}