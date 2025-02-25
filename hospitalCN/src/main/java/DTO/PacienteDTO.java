/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DTO;

import java.time.LocalDate;

/**
 *
 * @author isaac
 */
public class PacienteDTO {
    
    private int id_Paciente;
    private LocalDate fecha_nacimiento;
    private int edad;
    private String telefono;
    private String correoE;
    private UsuarioDTO usuario;

    public PacienteDTO(int id_Paciente, LocalDate fecha_nacimiento, int edad, String telefono, String correoE, UsuarioDTO usuario) {
        this.id_Paciente = id_Paciente;
        this.fecha_nacimiento = fecha_nacimiento;
        this.edad = edad;
        this.telefono = telefono;
        this.correoE = correoE;
        this.usuario = usuario;
    }

    public int getId_Paciente() {
        return id_Paciente;
    }

    public void setId_Paciente(int id_Paciente) {
        this.id_Paciente = id_Paciente;
    }

    public LocalDate getFecha_nacimiento() {
        return fecha_nacimiento;
    }

    public void setFecha_nacimiento(LocalDate fecha_nacimiento) {
        this.fecha_nacimiento = fecha_nacimiento;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreoE() {
        return correoE;
    }

    public void setCorreoE(String correoE) {
        this.correoE = correoE;
    }

    public UsuarioDTO getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioDTO usuario) {
        this.usuario = usuario;
    }

    @Override
    public String toString() {
        return "PacienteDTO{" + "id_Paciente=" + id_Paciente + ", fecha_nacimiento=" + fecha_nacimiento + ", edad=" + edad + ", telefono=" + telefono + ", correoE=" + correoE + ", usuario=" + usuario + '}';
    }

}
    

