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

    /**
     * Constructor que nos permite crear un usuario con todos sus paramentros
     *
     * @param id_Usuario el id del usuario que se desea ingresar
     * @param contra la password del usuario que se desea ingresar
     * @param nombre el nombre del usuairo
     * @param apellidoP apellido paterno del usuario
     * @param apellidoM apellido materno del usuairo
     */
    

    public int getId_Usuario() {
        return id_Usuario;
    }

    /**
     * Constructor que nos permite generar un usuario sin ingresar el id del
     * mismo
     *
     * @param contra la password del usuario que se desea ingresar
     */
    public Usuario(String contra) {
        this.contra = contra;
    }

    /**
     * Establece el ID del usuario.
     *
     * @param id_Usuario Identificador único que se asignará al usuario.
     */
    public void setId_Usuario(int id_Usuario) {
        this.id_Usuario = id_Usuario;
    }

    /**
     * Obtiene la contraseña del usuario.
     *
     * @return contra Contraseña del usuario.
     */
    public String getContra() {
        return contra;
    }

    /**
     * Establece la contraseña del usuario.
     *
     * @param contra Nueva contraseña que se asignará al usuario.
     */
    public void setContra(String contra) {
        this.contra = contra;
    }

    

    

   

    /**
     * Devuelve una representación en cadena del objeto Usuario.
     *
     * @return Cadena que representa al usuario con sus atributos.
     */
    @Override
    public String toString() {
        return "Usuario{"
                + "id_Usuario=" + id_Usuario
                + ", contra=" + contra;
    }

}
