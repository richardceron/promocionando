/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.riyoce.promocionando.controllers;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;
import mx.riyoce.promocionando.entities.Categoria;
import mx.riyoce.promocionando.entities.Color;
import mx.riyoce.promocionando.entities.Correo;
import mx.riyoce.promocionando.entities.Cotizacion;
import mx.riyoce.promocionando.entities.ItemCotizacion;
import mx.riyoce.promocionando.entities.Material;
import mx.riyoce.promocionando.entities.Producto;
import mx.riyoce.promocionando.entities.Usuario;
import mx.riyoce.promocionando.sessions.BuscadorSessionBean;
import mx.riyoce.promocionando.sessions.CategoriaSessionBean;
import mx.riyoce.promocionando.sessions.ProductoSessionBean;

/**
 *
 * @author ricardo
 */
@Named
@SessionScoped
public class BuscadorController implements Serializable {

    @EJB
    private BuscadorSessionBean bsb;
    @EJB
    private CategoriaSessionBean csb;
    @EJB
    private ProductoSessionBean psb;

    @Inject
    private CategoriaController cc;

    @Resource(name = "jms/PromoQueue")
    private Queue DTMACQueue;
    @Resource(name = "jms/PromoQueueFactory")
    private ConnectionFactory DTMACConnectionFactory;

    private String fitroBusqueda;
    private Categoria categoriaBusqueda;
    private List<Categoria> allCategorias;
    private Material materialBusqueda;
    private List<Material> allMateriales;
    private Color colorBusqueda;
    private List<Color> allcolores;

    private List<Producto> productos;
    private Producto producto;

    private List<ItemCotizacion> misProductos;

    private int cantidadProducto;

    private Usuario usuario;

    private Cotizacion cotizacion;
    
    private Cotizacion cotizacionContacto;

    private String search_type;
    private long object_id;

    public BuscadorController() {
        producto = new Producto();
        this.fitroBusqueda = "";
        categoriaBusqueda = new Categoria();
        materialBusqueda = new Material();
        colorBusqueda = new Color();        

        productos = new LinkedList<>();
        misProductos = new LinkedList<>();
        allCategorias = new LinkedList<>();        
        allMateriales = new LinkedList<>();
        allcolores = new LinkedList<>();
        usuario = new Usuario();

        cotizacion = new Cotizacion();
        cotizacionContacto = new Cotizacion();
    }

    @PostConstruct
    public void init() {
        initList();
    }

    public void initList() {
        System.out.println("refrescando listas");
        allCategorias = cc.getCategorias();        
        allMateriales = cc.getMateriales();
        allcolores = cc.getColores();
    }

    public void addProductToCoti() {
        if (cantidadProducto > 0) {
            ItemCotizacion i = new ItemCotizacion();
            i.setProducto(producto);
            i.setCantidad(cantidadProducto);
            misProductos.add(i);
            cantidadProducto = 0;
        } else {
            FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_WARN, "Debes indicar la cantidad a cotizar", ""));
        }
    }

    public boolean isInMyProducts(Producto p) {        
        for (ItemCotizacion i : misProductos) {
            if (i.getProducto().equals(p)) {
                return true;
            }
        }
        return false;
    }

    public void removeProductToCoti(ItemCotizacion p) {
        for (ItemCotizacion i : misProductos) {
            if (i.getProducto().equals(p)) {
                misProductos.remove(i);
            }
        }
    }

    public void crearCotizacion() {
        try {
            cotizacion.setProductos(getMisProductos());
            List<Correo> lc = bsb.enviarCotizacion(cotizacion);
            for (Correo lc1 : lc) {
                sendJMSMessageToMensajesQueue(lc1);
            }
            misProductos = new LinkedList<>();
            cotizacion = new Cotizacion();
            cantidadProducto = 0;
            FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Solicitud enviada con éxito", ""));
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al crear la cotizacion", e);
        }
    }
    
    public void enviarSolicitudCoontactoGeneral() {
        try {            
            List<Correo> lc = bsb.enviarContactoGeneral(cotizacionContacto);
            for (Correo lc1 : lc) {
                sendJMSMessageToMensajesQueue(lc1);
            }            
            cotizacionContacto = new Cotizacion();            
            FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Solicitud enviada con éxito", ""));
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al crear la cotizacion", e);
        }
    }

    public void indexSearch() {
        try {
            String url = "/promocionando-web/faces/productos.xhtml?search_type=name_and_cat&object_id=" + categoriaBusqueda.getId();
            FacesContext.getCurrentInstance().getExternalContext().redirect(url);
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al traer los productos del buscador", e);
        }
    }

    public void search() {
        try {
            if (search_type.equalsIgnoreCase("name_and_cat")) {
                if (fitroBusqueda != null) {
                    productos = bsb.getProductosByNameAndCategoria(fitroBusqueda, categoriaBusqueda);
                } else {
                    productos = bsb.getProductosByNameAndCategoria("", categoriaBusqueda);
                }
            }
            if (search_type.equalsIgnoreCase("only_cat")) {
                productos = bsb.getProductosByCategoria(csb.getCategoriaById(object_id));
            }
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al traer los productos", e);
        }
    }

    public void detailProducto() {
        try {
            producto = psb.getProductoById(object_id);
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al detallar el producto", e);
        }
    }

    private Message createJMSMessageForjmsMensajesQueue(Session session, Object messageData) throws JMSException {
        // TODO create and populate message to send
        ObjectMessage tm = session.createObjectMessage();
        tm.setObject((Serializable) messageData);
        return tm;
    }

    private void sendJMSMessageToMensajesQueue(Object messageData) throws JMSException {
        Connection connection = null;
        Session session = null;
        try {
            connection = DTMACConnectionFactory.createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            MessageProducer messageProducer = session.createProducer(DTMACQueue);
            messageProducer.send(createJMSMessageForjmsMensajesQueue(session, messageData));
        } finally {
            if (session != null) {
                try {
                    session.close();
                } catch (JMSException e) {
                    Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Cannot close session", e);
                }
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

    public List<String> completeText(String query) {
        return bsb.getAutoComplete(query);
    }

    /**
     * @return the fitroBusqueda
     */
    public String getFitroBusqueda() {
        return fitroBusqueda;
    }

    /**
     * @param fitroBusqueda the fitroBusqueda to set
     */
    public void setFitroBusqueda(String fitroBusqueda) {
        this.fitroBusqueda = fitroBusqueda;
    }

    /**
     * @return the categoriaBusqueda
     */
    public Categoria getCategoriaBusqueda() {
        return categoriaBusqueda;
    }

    /**
     * @param categoriaBusqueda the categoriaBusqueda to set
     */
    public void setCategoriaBusqueda(Categoria categoriaBusqueda) {
        this.categoriaBusqueda = categoriaBusqueda;
    }

    /**
     * @return the allCategorias
     */
    public List<Categoria> getAllCategorias() {
        return allCategorias;
    }

    /**
     * @param allCategorias the allCategorias to set
     */
    public void setAllCategorias(List<Categoria> allCategorias) {
        this.allCategorias = allCategorias;
    }

    /**
     * @return the materialBusqueda
     */
    public Material getMaterialBusqueda() {
        return materialBusqueda;
    }

    /**
     * @param materialBusqueda the materialBusqueda to set
     */
    public void setMaterialBusqueda(Material materialBusqueda) {
        this.materialBusqueda = materialBusqueda;
    }

    /**
     * @return the allMateriales
     */
    public List<Material> getAllMateriales() {
        return allMateriales;
    }

    /**
     * @param allMateriales the allMateriales to set
     */
    public void setAllMateriales(List<Material> allMateriales) {
        this.allMateriales = allMateriales;
    }

    /**
     * @return the colorBusqueda
     */
    public Color getColorBusqueda() {
        return colorBusqueda;
    }

    /**
     * @param colorBusqueda the colorBusqueda to set
     */
    public void setColorBusqueda(Color colorBusqueda) {
        this.colorBusqueda = colorBusqueda;
    }

    /**
     * @return the allcolores
     */
    public List<Color> getAllcolores() {
        return allcolores;
    }

    /**
     * @param allcolores the allcolores to set
     */
    public void setAllcolores(List<Color> allcolores) {
        this.allcolores = allcolores;
    }     

    /**
     * @return the productos
     */
    public List<Producto> getProductos() {
        return productos;
    }

    /**
     * @param productos the productos to set
     */
    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }

    /**
     * @return the producto
     */
    public Producto getProducto() {
        return producto;
    }

    /**
     * @param producto the producto to set
     */
    public void setProducto(Producto producto) {
        this.producto = producto;
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

    /**
     * @return the search_type
     */
    public String getSearch_type() {
        return search_type;
    }

    /**
     * @param search_type the search_type to set
     */
    public void setSearch_type(String search_type) {
        this.search_type = search_type;
    }

    /**
     * @return the object_id
     */
    public long getObject_id() {
        return object_id;
    }

    /**
     * @param object_id the object_id to set
     */
    public void setObject_id(long object_id) {
        this.object_id = object_id;
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

    /**
     * @return the misProductos
     */
    public List<ItemCotizacion> getMisProductos() {
        return misProductos;
    }

    /**
     * @param misProductos the misProductos to set
     */
    public void setMisProductos(List<ItemCotizacion> misProductos) {
        this.misProductos = misProductos;
    }

    /**
     * @return the cantidadProducto
     */
    public int getCantidadProducto() {
        return cantidadProducto;
    }

    /**
     * @param cantidadProducto the cantidadProducto to set
     */
    public void setCantidadProducto(int cantidadProducto) {
        this.cantidadProducto = cantidadProducto;
    }

    /**
     * @return the cotizacionContacto
     */
    public Cotizacion getCotizacionContacto() {
        return cotizacionContacto;
    }

    /**
     * @param cotizacionContacto the cotizacionContacto to set
     */
    public void setCotizacionContacto(Cotizacion cotizacionContacto) {
        this.cotizacionContacto = cotizacionContacto;
    }

}
