/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.riyoce.promocionando.sessions;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import mx.riyoce.promocionando.entities.Categoria;
import mx.riyoce.promocionando.entities.Color;
import mx.riyoce.promocionando.entities.Material;

/**
 *
 * @author ricardo
 */

@Stateless
public class CategoriaSessionBean implements Serializable{
    
    @PersistenceContext(unitName = "mx.riyoce_promocionando-ejb_ejb_1.0PU")
    private EntityManager em;
    
    /* Inician métodos para categorias */
    public void crearCategoria(Categoria c){
        try {            
            em.persist(c);
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al crear la categoria", e);
        }
    }
    
    public List<Categoria> getCategorias(){
        try {
            return em.createQuery("SELECT lc FROM Categoria lc ORDER BY lc.nombre ASC").getResultList();
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al traer las categorias", e);
            return null;
        }
    }
    
    public Categoria getCategoriaById(long id){
        try {
            return em.find(Categoria.class, id);
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al traer la categoria por id", e);
            return null;
        }
    }
    
    public void actualizarCategoria(Categoria c){
        try {
            em.merge(c);
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al actualizar la categoria", e);            
        }
    }
    /* Terminan métodos para categorias */
    
    /* Inician métodos para materiales */
    public void crearMaterial(Material m){
        try {
            em.persist(m);
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al crear el material", e);
        }
    }
    
    public List<Material> getMateriales(){
        try {
            return em.createQuery("SELECT lm FROM Material lm ORDER BY lm.nombre ASC").getResultList();
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al traer los materiales", e);
            return null;
        }
    }
    
    public Material getMaterialById(long id){
        try {
            return em.find(Material.class, id);
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al traer el material por id", e);
            return null;
        }
    }
    
    public void actualizarMaterial(Material m){
        try {
            em.merge(m);
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al actualizar el material", e);
        }
    }
    /* Terminan métodos para materiales */
    
    /* Inician métodos para colores */
    public void crearColor(Color c){
        try {
            em.persist(c);
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al crearl el color", e);
        }
    }
    
    public List<Color> getColores(){
        try {
            return em.createQuery("SELECT lc FROM Color lc ORDER BY lc.nombre ASC").getResultList();
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al traer los colores", e);
            return null;
        }
    }
    
    public Color getColorById(long id){
        try {
            return em.find(Color.class, id);
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al traer el color por id", e);
            return null;
        }
    }
    
    public void actualizarColor(Color c){
        try {
            em.merge(c);
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al actualizar el color", e);
        }
    }
    /* Terminan métodos para colores */
}
