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
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import mx.riyoce.promocionando.entities.Categoria;
import mx.riyoce.promocionando.entities.Color;
import mx.riyoce.promocionando.entities.Material;
import mx.riyoce.promocionando.sessions.CategoriaSessionBean;
import org.primefaces.model.DualListModel;

/**
 *
 * @author ricardo
 */
@Named
@SessionScoped
public class CategoriaController implements Serializable {

    @EJB
    private CategoriaSessionBean csb;

    private Categoria categoria;
    private Material material;
    private Color color;

    private List<Categoria> categorias;
    private List<Material> materiales;
    private List<Color> colores;

    private DualListModel<Material> materialesCategoria;
    private DualListModel<Color> coloresCategoria;

    private boolean editMaterial;
    private boolean editColor;
    private boolean editaMarca;
    private boolean editCategoria;

    public CategoriaController() {
        this.categoria = new Categoria();
        this.material = new Material();
        this.color = new Color();

        editMaterial = false;
        editColor = false;
        editaMarca = false;
        editCategoria = false;
    }

    @PostConstruct
    public void init() {
        initLists();
    }

    public void initLists() {
        this.categorias = csb.getCategorias();
        this.materiales = csb.getMateriales();
        this.colores = csb.getColores();

        this.materialesCategoria = new DualListModel<>(materiales, new LinkedList<Material>());
        this.coloresCategoria = new DualListModel<>(colores, new LinkedList<Color>());
    }

    /* Inician métodos para categorias */
    public void crearCategoria() {
        try {
            csb.crearCategoria(categoria);
            categoria = new Categoria();
            this.categorias = csb.getCategorias();
            materialesCategoria = new DualListModel<>(materiales, new LinkedList<Material>());
            coloresCategoria = new DualListModel<>(colores, new LinkedList<Color>());
            FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Categoría creado con éxito", ""));
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al crear la categoria", e);
        }
    }

    public void habilitarEdicionCategoria() {
        editCategoria = true;
    }

    public void deshabilitarEdicionCategoria() {
        categoria = new Categoria();
        this.materialesCategoria = new DualListModel<>(materiales, new LinkedList<Material>());
        this.coloresCategoria = new DualListModel<>(colores, new LinkedList<Color>());
        editCategoria = false;
    }

    public void actualizarCategoria() {
        try {
            csb.actualizarCategoria(categoria);
            categoria = new Categoria();
            this.categorias = csb.getCategorias();
            editCategoria = false;
            materialesCategoria = new DualListModel<>(materiales, new LinkedList<Material>());
            coloresCategoria = new DualListModel<>(colores, new LinkedList<Color>());
            FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Categoría actualizada con éxito", ""));
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al crear la categoria", e);
        }
    }
    /* Terminan métodos para categorias */

    /* Inician métodos para materiales */
    public void crearMaterial() {
        try {
            csb.crearMaterial(material);
            material = new Material();
            this.materiales = csb.getMateriales();
            FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Material creado con éxitoo", ""));
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al crear el material", e);
        }
    }

    public void actualizarMaterial() {
        try {
            csb.actualizarMaterial(material);
            deshabilitarEdicionMaterial();
            FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Material actualizado con éxitoo", ""));
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al crear el material", e);
        }
    }

    public void deshabilitarEdicionMaterial() {
        material = new Material();
        materiales = csb.getMateriales();
        editMaterial = false;
    }
    /* Terminan métodos para materiales */

    /* Inician métodos para colores */
    public void crearColor() {
        try {
            csb.crearColor(color);
            color = new Color();
            this.colores = csb.getColores();
            FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Color creado con éxitoo", ""));
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al crear el color", e);
        }
    }

    public void actualizarColor() {
        try {
            csb.actualizarColor(color);
            deshabilitarEdicionColor();
            FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Color actuzalido con éxito", ""));
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error al actualizar el color", e);
        }
    }

    public void deshabilitarEdicionColor() {
        color = new Color();
        colores = csb.getColores();
        editColor = false;
    }
    /* Terminan métodos para colores */

    /**
     * @return the categoria
     */
    public Categoria getCategoria() {
        return categoria;
    }

    /**
     * @param categoria the categoria to set
     */
    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    /**
     * @return the material
     */
    public Material getMaterial() {
        return material;
    }

    /**
     * @param material the material to set
     */
    public void setMaterial(Material material) {
        this.material = material;
    }

    /**
     * @return the color
     */
    public Color getColor() {
        return color;
    }

    /**
     * @param color the color to set
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * @return the categorias
     */
    public List<Categoria> getCategorias() {
        return categorias;
    }

    /**
     * @param categorias the categorias to set
     */
    public void setCategorias(List<Categoria> categorias) {
        this.categorias = categorias;
    }

    /**
     * @return the materiales
     */
    public List<Material> getMateriales() {
        return materiales;
    }

    /**
     * @param materiales the materiales to set
     */
    public void setMateriales(List<Material> materiales) {
        this.materiales = materiales;
    }

    /**
     * @return the colores
     */
    public List<Color> getColores() {
        return colores;
    }

    /**
     * @param colores the colores to set
     */
    public void setColores(List<Color> colores) {
        this.colores = colores;
    }

    /**
     * @return the editMaterial
     */
    public boolean isEditMaterial() {
        return editMaterial;
    }

    /**
     * @param editMaterial the editMaterial to set
     */
    public void setEditMaterial(boolean editMaterial) {
        this.editMaterial = editMaterial;
    }

    /**
     * @return the editColor
     */
    public boolean isEditColor() {
        return editColor;
    }

    /**
     * @param editColor the editColor to set
     */
    public void setEditColor(boolean editColor) {
        this.editColor = editColor;
    }

    /**
     * @return the editaMarca
     */
    public boolean isEditaMarca() {
        return editaMarca;
    }

    /**
     * @param editaMarca the editaMarca to set
     */
    public void setEditaMarca(boolean editaMarca) {
        this.editaMarca = editaMarca;
    }

    /**
     * @return the editCategoria
     */
    public boolean isEditCategoria() {
        return editCategoria;
    }

    /**
     * @param editCategoria the editCategoria to set
     */
    public void setEditCategoria(boolean editCategoria) {
        this.editCategoria = editCategoria;
    }

    /**
     * @return the materialesCategoria
     */
    public DualListModel<Material> getMaterialesCategoria() {
        return materialesCategoria;
    }

    /**
     * @param materialesCategoria the materialesCategoria to set
     */
    public void setMaterialesCategoria(DualListModel<Material> materialesCategoria) {
        this.materialesCategoria = materialesCategoria;
    }

    /**
     * @return the coloresCategoria
     */
    public DualListModel<Color> getColoresCategoria() {
        return coloresCategoria;
    }

}
