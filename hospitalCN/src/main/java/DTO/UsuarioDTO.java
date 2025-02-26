/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DTO;

/**
 *
 * @author isaac
 */
public class UsuarioDTO {

    private int id_Usuario; // Atributo que hace referencia al id del usuario
    private String contra;
    private String nombre;
    private String apellidoP;
    private String apellidoM;

    public UsuarioDTO() {
    }
    
    
    public UsuarioDTO(int id_Usuario, String contra, String nombre, String apellidoP, String apellidoM) {
        this.id_Usuario = id_Usuario;
        this.contra = contra;
        this.nombre = nombre;
        this.apellidoP = apellidoP;
        this.apellidoM = apellidoM;
        
    }

    public void setId_Usuario(int id_Usuario) {
        this.id_Usuario = id_Usuario;
    }

    public void setContra(String contra) {
        this.contra = contra;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidoP() {
        return apellidoP;
    }

    public void setApellidoP(String apellidoP) {
        this.apellidoP = apellidoP;
    }

    public String getApellidoM() {
        return apellidoM;
    }

    public void setApellidoM(String apellidoM) {
        this.apellidoM = apellidoM;
    }

    public int getId_Usuario() {
        return id_Usuario;
    }

    public String getContra() {
        return contra;
    }

    @Override
    public String toString() {
        return "UsuarioDTO{" + "id_Usuario=" + id_Usuario + ", contra=" + contra + ", nombre=" + nombre + ", apellidoP=" + apellidoP + ", apellidoM=" + apellidoM + '}';
    }

}
