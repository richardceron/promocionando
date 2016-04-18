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
import mx.riyoce.promocionando.entities.Material;
import mx.riyoce.promocionando.sessions.CategoriaSessionBean;

/**
 *
 * @author ricardo
 */

@FacesConverter(value = "MaterialConverter")
public class MaterialConverter implements Converter{

    @EJB
    private CategoriaSessionBean csb;
    
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        try {
            long id = Long.valueOf(value);
            Material m = csb.getMaterialById(id);
            return m;
        } catch (NullPointerException npe) {
            return null;
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al convertir a material", e);
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        try {
            Material m = (Material) value;
            String isAsString = String.valueOf(m.getId());
            return isAsString;
        } catch (NullPointerException npe) {
            return null;
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al convertir a string", e);
            return null;
        }
    }
    
}
