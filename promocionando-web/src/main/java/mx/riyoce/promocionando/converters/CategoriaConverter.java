/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.riyoce.promocionando.converters;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import mx.riyoce.promocionando.entities.Categoria;
import mx.riyoce.promocionando.sessions.CategoriaSessionBean;

/**
 *
 * @author ricardo
 */
@FacesConverter(value = "CategoriaConverter")
public class CategoriaConverter implements Converter {

    @EJB
    private CategoriaSessionBean csb;

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        try {
            long id = Long.valueOf(value);
            Categoria c = csb.getCategoriaById(id);
            return c;
        } catch (NullPointerException npe) {
            return null;
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al convertir a marca", e);
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        try {
            Categoria c = (Categoria) value;
            String isAsString = String.valueOf(c.getId());
            return isAsString;
        } catch (NullPointerException npe) {
            return null;
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al convertir a string", e);
            return null;
        }
    }

}
