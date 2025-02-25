/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entidades;

/**
 * Clase que representa el atributo compuesto deireccion_paciente de la base de
 * datos
 *
 * @author mmax2
 */
public class DireccionPaciente {
    private int id_Direccion;
    private String calle;
    private String numExt;
    private String colonia;
    private Paciente paciente;

    public DireccionPaciente(int id_Direccion, String calle, String numExt, String colonia, Paciente paciente) {
        this.id_Direccion = id_Direccion;
        this.calle = calle;
        this.numExt = numExt;
        this.colonia = colonia;
        this.paciente = paciente;
    }

    public DireccionPaciente(String calle, String numExt, String colonia, Paciente paciente) {
        this.calle = calle;
        this.numExt = numExt;
        this.colonia = colonia;
        this.paciente = paciente;
        
    }

    public int getId_Direccion() {
        return id_Direccion;
    }

    public void setId_Direccion(int id_Direccion) {
        this.id_Direccion = id_Direccion;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getNumExt() {
        return numExt;
    }

    public void setNumExt(String numExt) {
        this.numExt = numExt;
    }

    public String getColonia() {
        return colonia;
    }

    public void setColonia(String colonia) {
        this.colonia = colonia;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    @Override
    public String toString() {
        return "DireccionPaciente{" + "id_Direccion=" + id_Direccion + ", calle=" + calle + ", numExt=" + numExt + ", colonia=" + colonia + ", paciente=" + paciente + '}';
    }

}
   