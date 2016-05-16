/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.riyoce.promocionando.controllers;

import java.io.Serializable;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import mx.riyoce.promocionando.entities.ImagenProducto;
import mx.riyoce.promocionando.entities.Producto;
import mx.riyoce.promocionando.sessions.ProductoSessionBean;
import org.apache.commons.io.IOUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author ricardo
 */
@Named
@SessionScoped
public class ProductoController implements Serializable {

    @EJB
    private ProductoSessionBean psb;

    private Producto producto;
    private List<Producto> productos;

    private boolean editarProducto;

    public ProductoController() {
        producto = new Producto();
    }

    @PostConstruct
    public void init() {
        productos = psb.getProductos();
    }

    public void crearProducto() {
        try {
            if (producto.getImagenes().size() > 0) {
                Random rand = new Random();
                int n = rand.nextInt(50) + 1;
                String clave = producto.getNombre().replaceAll(" ", "_");
                clave = clave.toLowerCase();
                clave = clave.concat("_");
                clave += n;
                
                producto.setClave(clave);
                
                psb.crearProducto(producto);
                producto = new Producto();
                productos = psb.getProductos();
                FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Producto creado con éxito", ""));
            } else {
                FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_WARN, "Debes cargar al menos una imagen del producto", ""));
            }
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al crear el producto", e);
        }
    }

    public void actualizarProducto() {
        try {
            psb.actualizarProducto(producto);
            desHabilitarEdicionProducto();
            FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Producto editado con éxito", ""));
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al actualizar el producto", e);
        }
    }

    public void addFileToCliente(FileUploadEvent evt) {
        try {
            UploadedFile file = evt.getFile();
            if (file != null) {
                ImagenProducto img = new ImagenProducto();
                img.setFileName(file.getFileName());
                img.setMime(file.getContentType());
                byte[] bytes = IOUtils.toByteArray(file.getInputstream());
                img.setArchivo(bytes);
                img.setProducto(producto);
                producto.getImagenes().add(img);
                FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Imagen cargada con éxito", ""));
            } else {
                System.out.println("El archivo es null");
            }
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al cargar el archivo", e);
        }
    }

    public void removeFileFromProduct(ImagenProducto img) {
        try {
            producto.getImagenes().remove(img);
            FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Imagen eliminada con éxito", ""));
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al eliminar la imagen del producto", e);
        }
    }

    public void habilitarEdicionProducto() {
        editarProducto = true;
    }

    public void desHabilitarEdicionProducto() {
        editarProducto = false;
        producto = new Producto();
        productos = psb.getProductos();
    }
    
    public List<Producto> getNovedades(){
        try {
            return psb.getProductosNovedad();
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al traer los productos por novedad", e);
            return null;
        }
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
     * @return the editarProducto
     */
    public boolean isEditarProducto() {
        return editarProducto;
    }

    /**
     * @param editarProducto the editarProducto to set
     */
    public void setEditarProducto(boolean editarProducto) {
        this.editarProducto = editarProducto;
    }

}
