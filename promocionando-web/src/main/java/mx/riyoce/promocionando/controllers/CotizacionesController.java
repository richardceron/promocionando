/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.riyoce.promocionando.controllers;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import mx.riyoce.promocionando.entities.Cotizacion;
import mx.riyoce.promocionando.sessions.CotizacionesSessionBean;

/**
 *
 * @author ricardo
 */

@Named
@SessionScoped
public class CotizacionesController implements Serializable{
    
    @EJB
    private CotizacionesSessionBean csb;
    
    private List<Cotizacion> cotizaciones;
    private Cotizacion cotizacion;  
    
    public CotizacionesController(){
        cotizaciones = new LinkedList<>();
        cotizacion = new Cotizacion();
    }
    
    @PostConstruct
    public void init(){
        cotizaciones = csb.getCotizaciones();
    }

    /**
     * @return the cotizaciones
     */
    public List<Cotizacion> getCotizaciones() {
        return cotizaciones;
    }

    /**
     * @param cotizaciones the cotizaciones to set
     */
    public void setCotizaciones(List<Cotizacion> cotizaciones) {
        this.cotizaciones = cotizaciones;
    }

    /**
     * @return the cotizacion
     */
    public Cotizacion getCotizacion() {
        return cotizacion;
    }

    /**
     * @param cotizacion the cotizacion to set
     */
    public void setCotizacion(Cotizacion cotizacion) {
        this.cotizacion = cotizacion;
    }
    
}
