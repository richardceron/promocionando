/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.riyoce.promocionando.sessions;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import mx.riyoce.promocionando.entities.Perfil;
import mx.riyoce.promocionando.entities.Rol;
import mx.riyoce.promocionando.entities.Usuario;

/**
 *
 * @author ricardo
 */
@Stateless
public class UsuarioSessionBean implements Serializable {

    @PersistenceContext(unitName = "mx.riyoce_promocionando-ejb_ejb_1.0PU")
    private EntityManager em;

    public void crearUsuario(Usuario u) {
        try {
            u.setPassword(encodePassword(u.getPassword()));
            em.persist(u);
            
            Rol r = new Rol();
            r.setUsuario(u);
            r.setPerfil(getPerfilUsuarioGeneral());
            em.persist(r);
            
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al crear el usuario", e);
        }
    }
    
    public Usuario getUsuarioByEmail(String email) {
        try {            
            return (Usuario) em.createQuery("SELECT u FROM Usuario u WHERE u.email = :correo").setMaxResults(1).setParameter("correo", email).getSingleResult();
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al traer al usuario por email", e);
            return null;
        }
    }

    public String encodePassword(String pass) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(pass.getBytes());
            StringBuilder result = new StringBuilder();
            for (byte byt : md.digest()) {
                result.append(Integer.toString((byt & 0xff) + 0x100, 16).substring(1));
            }
            return result.toString();
        } catch (NoSuchAlgorithmException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al codificar el password", e);
            return "";
        }

    }

    public Perfil getPerfilUsuarioGeneral() {
        try {
            Query q = em.createQuery("SELECT p FROM Perfil p WHERE p.clave = 'usuario_general'");
            q.setMaxResults(1);
            return (Perfil) q.getSingleResult();
        } catch (NoResultException e) {
            Perfil p = new Perfil();
            p.setNombre("Usuario General");
            p.setClave("usuario_general");
            em.persist(p);
            return p;
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al traer el perfil de usuario general", e);
            return null;
        }
    }

    
}
