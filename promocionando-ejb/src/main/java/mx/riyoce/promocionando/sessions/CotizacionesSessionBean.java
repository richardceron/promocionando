/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.riyoce.promocionando.sessions;

import java.io.Serializable;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import mx.riyoce.promocionando.entities.Cotizacion;

/**
 *
 * @author ricardo
 */

@Stateless
public class CotizacionesSessionBean implements Serializable{
    @PersistenceContext(unitName = "mx.riyoce_promocionando-ejb_ejb_1.0PU")
    private EntityManager em;
    
    public List<Cotizacion> getCotizaciones(){
        return em.createQuery("SELECT lc FROM Cotizacion lc ORDER BY lc.fecha DESC").getResultList();
    }
    
}
