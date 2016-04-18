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
import mx.riyoce.promocionando.entities.ImagenProducto;
import mx.riyoce.promocionando.entities.Producto;

/**
 *
 * @author ricardo
 */
@Stateless
public class ProductoSessionBean implements Serializable {

    @PersistenceContext(unitName = "mx.riyoce_promocionando-ejb_ejb_1.0PU")
    private EntityManager em;

    public void crearProducto(Producto p) {
        try {
            em.persist(p);            
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al crear el producto", e);
        }
    }        
    
    public List<Producto> getProductos() {
        try {
            return em.createQuery("SELECT lp FROM Producto lp ORDER BY lp.fechaPublicacion DESC").getResultList();
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al traer los productos", e);
            return null;
        }
    }
    
    public List<Producto> getProductosNovedad() {
        try {
            return em.createQuery("SELECT lp FROM Producto lp WHERE lp.novedad = true ORDER BY lp.fechaPublicacion DESC").getResultList();
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al traer los productos de novedad", e);
            return null;
        }
    }
    
    public Producto getProductoById(long id){
        try {
            return em.find(Producto.class, id);
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al traer el producto por id", e);
            return null;
        }
    }
    
    public void actualizarProducto(Producto p){
        try {
            em.merge(p);
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al actualziar el producto", e);
        }
    }
    
    
    public ImagenProducto getImagenProductoById(long id){
        try {
            return em.find(ImagenProducto.class, id);
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al eliminar traer la imagen producto por id", e);
            return null;
        }
    }
    
    public void removeFileFromProduct(long id){
        try {
            ImagenProducto img = em.find(ImagenProducto.class, id);
            em.remove(img);
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al eliminar la imagen del producto", e);
        }
    }

}
