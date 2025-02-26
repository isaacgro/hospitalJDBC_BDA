/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entidades;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 *Clase que referencia a la entidad consultas de la base de datos
 * @author mmax2
 */
public class Consulta {
    private int id_Consulta;
    private LocalDateTime fecha_hora;
    private String tratamiento;
    private String diagnostico;

    public Consulta(int id_Consulta, LocalDateTime fecha_hora, String tratamiento, String diagnostico) {
        this.id_Consulta = id_Consulta;
        this.fecha_hora = fecha_hora;
        this.tratamiento = tratamiento;
        this.diagnostico = diagnostico;
    }

    public Consulta(LocalDateTime fecha_hora, String tratamiento, String diagnostico) {
        this.fecha_hora = fecha_hora;
        this.tratamiento = tratamiento;
        this.diagnostico = diagnostico;
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
    

    public Consulta() {
    }

    @Override
    public String toString() {
        return "Consulta{" + "id_Consulta=" + id_Consulta + ", fecha_hora=" + fecha_hora + ", tratamiento=" + tratamiento + ", diagnostico=" + diagnostico + '}';
    }

    
    
}
