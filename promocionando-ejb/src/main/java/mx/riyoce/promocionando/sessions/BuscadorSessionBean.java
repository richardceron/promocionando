/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.riyoce.promocionando.sessions;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import mx.riyoce.promocionando.entities.Categoria;
import mx.riyoce.promocionando.entities.Correo;
import mx.riyoce.promocionando.entities.Cotizacion;
import mx.riyoce.promocionando.entities.ItemCotizacion;
import mx.riyoce.promocionando.entities.Producto;
import mx.riyoce.promocionando.entities.Usuario;

/**
 *
 * @author ricardo
 */
@Stateless
public class BuscadorSessionBean implements Serializable {

    @PersistenceContext(unitName = "mx.riyoce_promocionando-ejb_ejb_1.0PU")
    private EntityManager em;
    
    public List<Correo> enviarCotizacion(Cotizacion c){
        List<Correo> lc = new LinkedList<>();
        try {
            em.persist(c);
            List<Usuario> lu = em.createQuery("SELECT lu FROM Usuario lu").getResultList();
            for(Usuario u : lu){
                Correo m = new Correo();
                m.setType("html");
                m.setFecha(new Date());
                m.setAcerca("Nuevo solicitud de cotización");
                m.setNombreDe("Promocionando");
                m.setMailDe("noreply@promocionando.com.mx");
                
                m.setNombrePara(u.getNombre());
                m.setMailPara(u.getEmail());
                
                String cuerpo = "Han solicitado una cotización.<br/>";
                cuerpo += "Datos de contacto:<br/>";
                cuerpo += "Nombre: ";
                cuerpo += c.getNombre()+"<br/>";
                cuerpo += "Email: ";
                cuerpo += c.getEmail()+"<br/>";
                cuerpo += "Teléfono: ";
                cuerpo += c.getTelefono()+"<br/>";
                
                cuerpo += "Productos:<br/>";
                
                for(ItemCotizacion p : c.getProductos()){
                    cuerpo += p.getProducto().getNombre() + "<br/>";
                }
                
                m.setCuerpo(cuerpo);
                
                lc.add(m);
            }
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al crear la cotización", e);
        }
        return lc;
    }
    
    public List<Correo> enviarContactoGeneral(Cotizacion c){
        List<Correo> lc = new LinkedList<>();
        try {
            em.persist(c);
            List<Usuario> lu = em.createQuery("SELECT lu FROM Usuario lu").getResultList();
            for(Usuario u : lu){
                Correo m = new Correo();
                m.setType("html");
                m.setFecha(new Date());
                m.setAcerca("Nuevo solicitud de contacto general");
                m.setNombreDe("Promocionando");
                m.setMailDe("noreply@promocionando.com.mx");
                
                m.setNombrePara(u.getNombre());
                m.setMailPara(u.getEmail());
                
                String cuerpo = "Han solicitado información general.<br/>";
                cuerpo += "Datos de contacto:<br/>";
                cuerpo += "Nombre: ";
                cuerpo += c.getNombre()+"<br/>";
                cuerpo += "Email: ";
                cuerpo += c.getEmail()+"<br/>";
                cuerpo += "Teléfono: ";
                cuerpo += c.getTelefono()+"<br/>"; 
                cuerpo += "Comentarios: ";
                cuerpo += c.getComentarios()+"<br/>"; 
                
                m.setCuerpo(cuerpo);
                
                lc.add(m);
            }
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al crear la cotización", e);
        }
        return lc;
    }

    public List<Producto> getProductosByNameAndCategoria(String name, Categoria categoria) {
        try {
            String query = "SELECT lp FROM Producto lp WHERE lp.fechaVigencia >= :hoy AND lp.nombre LIKE :filtro";
            if (categoria != null) {
                query += " AND lp.categoria = :categoria";
            }
            query += " ORDER BY lp.nombre ASC";
            Query q = em.createQuery(query);
            q.setParameter("hoy", new Date(), TemporalType.DATE);
            q.setParameter("filtro", "%" + name + "%");
            if (categoria != null) {
                q.setParameter("categoria", categoria);
            }
            return q.getResultList();
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al traer los productos por nombre y categoriar", e);
            return null;
        }
    }
    
    public List<Producto> getProductosByName(String name) {
        try {
            Query q = em.createQuery("SELECT DISTINCT lp FROM Producto lp WHERE (lp.categoria.nombre LIKE :filtro OR lp.clave LIKE :filtro) ORDER By lp.nombre ASC");
            q.setParameter("filtro", "%" + name + "%");
            return q.getResultList();
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al traer los productos por nombre y categoriar", e);
            return null;
        }
    }
    
    public List<Producto> getProductosByCategoria(Categoria categoria) {
        try {
            Query q = em.createQuery("SELECT lp FROM Producto lp WHERE lp.fechaVigencia >= :hoy AND lp.categoria = :categoria ORDER BY lp.fechaPublicacion DESC");
            q.setParameter("hoy", new Date(), TemporalType.DATE);
            q.setParameter("categoria", categoria);
            return q.getResultList();
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al traer los productos por categoriar", e);
            return null;
        }
    }

    public List<String> getAutoComplete(String filter) {
        try {
            Query q = em.createQuery("SELECT lp.nombre FROM Producto lp WHERE lp.nombre LIKE :filtro ORDER By lp.nombre ASC");
            q.setParameter("filtro", "%" + filter + "%");
            return q.getResultList();
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al traer los nombres para autocompletar", e);
            return null;
        }
    }
    
    public Categoria getCategoriaOfProduct(String name){
        try {
            Query q = em.createQuery("SELECT c FROM Categoria c, Producto p WHERE p.categoria = c AND p.nombre = :name");
            q.setParameter("name", name);
            return (Categoria) q.getSingleResult();
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al traer la categoria del producto por nombre de producto", e);
            return null;
        }
    }

}
