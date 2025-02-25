/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entidades;

/**
 * Clase que representa la entidad usuario de la base de datos
 *
 * @author mmax2
 */
public class Usuario {

    private int id_Usuario; // Atributo que hace referencia al id del usuario
    private String contra; // Atributo que hace referencia a la password del
    private String nombre;
    private String apellidoP;
    private String apellidoM;

    /**
     * Constructor por omision que no permite crear un usuario sin parametros
     * especificos
     */
    public Usuario() {
    }

    public Usuario(int id_Usuario, String contra, String nombre, String apellidoP, String apellidoM) {
        this.id_Usuario = id_Usuario;
        this.contra = contra;
        this.nombre = nombre;
        this.apellidoP = apellidoP;
        this.apellidoM = apellidoM;
        
    }

    public int getId_Usuario() {
        return id_Usuario;
    }

    public void setId_Usuario(int id_Usuario) {
        this.id_Usuario = id_Usuario;
    }

    public String getContra() {
        return contra;
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

    @Override
    public String toString() {
        return "Usuario{" + "id_Usuario=" + id_Usuario + ", contra=" + contra + ", nombre=" + nombre + ", apellidoP=" + apellidoP + ", apellidoM=" + apellidoM + '}';
    }
}