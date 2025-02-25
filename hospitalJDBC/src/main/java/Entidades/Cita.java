/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entidades;

import java.sql.Timestamp;

/**
 *Clase que representa a la entidad cita de la base de datos
 * @author mmax2
 */
public class Cita {
   private int id_Cita;
   private String estado;
   private Timestamp fecha_hora;
   private Paciente paciente;
   private Medico medico;
   private String tipo;

    public Cita() {
    }

    public Cita(int id_Cita, String estado, Timestamp fecha_hora, Paciente paciente, Medico medico, String tipo) {
        this.id_Cita = id_Cita;
        this.estado = estado;
        this.fecha_hora = fecha_hora;
        this.paciente = paciente;
        this.medico = medico;
        this.tipo = tipo;
    }

    public Cita(String estado, Timestamp fecha_hora, Paciente paciente, Medico medico, String tipo) {
        this.estado = estado;
        this.fecha_hora = fecha_hora;
        this.paciente = paciente;
        this.medico = medico;
        this.tipo = tipo;
    }

    public int getId_Cita() {
        return id_Cita;
    }

    public void setId_Cita(int id_Cita) {
        this.id_Cita = id_Cita;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Timestamp getFecha_hora() {
        return fecha_hora;
    }

    public void setFecha_hora(Timestamp fecha_hora) {
        this.fecha_hora = fecha_hora;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public Medico getMedico() {
        return medico;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return "Cita{" + "id_Cita=" + id_Cita + ", estado=" + estado + ", fecha_hora=" + fecha_hora + ", paciente=" + paciente + ", medico=" + medico + ", tipo=" + tipo + '}';
    }

}

    
