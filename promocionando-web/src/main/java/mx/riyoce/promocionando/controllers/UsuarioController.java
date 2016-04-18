/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.riyoce.promocionando.controllers;

import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import mx.riyoce.promocionando.entities.Usuario;
import mx.riyoce.promocionando.sessions.UsuarioSessionBean;

/**
 *
 * @author ricardo
 */
@Named
@SessionScoped
public class UsuarioController implements Serializable {

    @EJB
    private UsuarioSessionBean usb;

    private String email;
    private String password;
    private Usuario usuario;

    public UsuarioController() {
        usuario = null;
    }

    public void login() {
        try {
            HttpServletRequest r = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            r.login(email, password);
            usuario = usb.getUsuarioByEmail(email);
            if (usuario == null) {
                r.getSession().invalidate();
                FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_WARN, "Correo o contraseña son incorrectos", ""));
            } else {
                FacesContext.getCurrentInstance().getExternalContext().redirect("/promocionando-web/faces/admin/solicitudes.xhtml");
            }
        } catch (ServletException | IOException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al hacer el login", e);
            FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_WARN, "Correo o contraseña son incorrectos", ""));
        }
    }

    public void logout() {
        try {
            HttpServletRequest r = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            r.getSession().invalidate();
            FacesContext.getCurrentInstance().getExternalContext().redirect("/promocionando-web/faces/index.xhtml");
        } catch (IOException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al hacer el login", e);
        }
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the usuario
     */
    public Usuario getUsuario() {
        return usuario;
    }

    /**
     * @param usuario the usuario to set
     */
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

}
