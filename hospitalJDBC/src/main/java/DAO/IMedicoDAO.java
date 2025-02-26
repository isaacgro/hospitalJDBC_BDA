/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package DAO;

//import DTO.MedicoDTO;
import Entidades.Medico;
import Excepciones.PersistenciaExcption;

/**
 *
 * @author isaac
 */
public interface IMedicoDAO {
    
    public boolean autenticarMedico(String cedulaPro, String contra) throws PersistenciaExcption;
    
    public Medico obtenerMedicoPorCedula(String cedulaPro) throws PersistenciaExcption;
    
   
    
    
    
}
