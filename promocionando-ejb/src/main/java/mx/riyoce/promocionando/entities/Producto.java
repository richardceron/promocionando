/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.riyoce.promocionando.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.UniqueConstraint;

/**
 *
 * @author ricardo
 */
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"CLAVE"})})
public class Producto implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String clave;
    
    private String nombre;
    @Lob
    private String descripcion;
    private String tamano;
    private String capaciad;

    private String tecnicaImpresion;
    private String areaImpresion;

    private String tamanoEmpaque;
    private String pesoEmpaque;
    private String cantidadEmpaque;

    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaPublicacion;

    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaVigencia;
    
    private int existencia;

    private boolean novedad;

    @ManyToOne
    private Categoria categoria;

    @ManyToOne
    private Material material;

    @ManyToOne
    private Color color;

    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL)
    private List<ImagenProducto> imagenes;

    public Producto() {        
        this.fechaPublicacion = new Date();
        this.novedad = false;
        this.imagenes = new LinkedList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Producto)) {
            return false;
        }
        Producto other = (Producto) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return String.valueOf(id);
    }

    /**
     * @return the nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @param nombre the nombre to set
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * @return the descripcion
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * @param descripcion the descripcion to set
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * @return the tamano
     */
    public String getTamano() {
        return tamano;
    }

    /**
     * @param tamano the tamano to set
     */
    public void setTamano(String tamano) {
        this.tamano = tamano;
    }

    /**
     * @return the capaciad
     */
    public String getCapaciad() {
        return capaciad;
    }

    /**
     * @param capaciad the capaciad to set
     */
    public void setCapaciad(String capaciad) {
        this.capaciad = capaciad;
    }

    /**
     * @return the tecnicaImpresion
     */
    public String getTecnicaImpresion() {
        return tecnicaImpresion;
    }

    /**
     * @param tecnicaImpresion the tecnicaImpresion to set
     */
    public void setTecnicaImpresion(String tecnicaImpresion) {
        this.tecnicaImpresion = tecnicaImpresion;
    }

    /**
     * @return the areaImpresion
     */
    public String getAreaImpresion() {
        return areaImpresion;
    }

    /**
     * @param areaImpresion the areaImpresion to set
     */
    public void setAreaImpresion(String areaImpresion) {
        this.areaImpresion = areaImpresion;
    }

    /**
     * @return the tamanoEmpaque
     */
    public String getTamanoEmpaque() {
        return tamanoEmpaque;
    }

    /**
     * @param tamanoEmpaque the tamanoEmpaque to set
     */
    public void setTamanoEmpaque(String tamanoEmpaque) {
        this.tamanoEmpaque = tamanoEmpaque;
    }

    /**
     * @return the pesoEmpaque
     */
    public String getPesoEmpaque() {
        return pesoEmpaque;
    }

    /**
     * @param pesoEmpaque the pesoEmpaque to set
     */
    public void setPesoEmpaque(String pesoEmpaque) {
        this.pesoEmpaque = pesoEmpaque;
    }

    /**
     * @return the cantidadEmpaque
     */
    public String getCantidadEmpaque() {
        return cantidadEmpaque;
    }

    /**
     * @param cantidadEmpaque the cantidadEmpaque to set
     */
    public void setCantidadEmpaque(String cantidadEmpaque) {
        this.cantidadEmpaque = cantidadEmpaque;
    }

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
     * @return the fechaPublicacion
     */
    public Date getFechaPublicacion() {
        return fechaPublicacion;
    }

    /**
     * @param fechaPublicacion the fechaPublicacion to set
     */
    public void setFechaPublicacion(Date fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    /**
     * @return the fechaVigencia
     */
    public Date getFechaVigencia() {
        return fechaVigencia;
    }

    /**
     * @param fechaVigencia the fechaVigencia to set
     */
    public void setFechaVigencia(Date fechaVigencia) {
        this.fechaVigencia = fechaVigencia;
    }

    /**
     * @return the novedad
     */
    public boolean isNovedad() {
        return novedad;
    }

    /**
     * @param novedad the novedad to set
     */
    public void setNovedad(boolean novedad) {
        this.novedad = novedad;
    }

    /**
     * @return the imagenes
     */
    public List<ImagenProducto> getImagenes() {
        return imagenes;
    }

    /**
     * @param imagenes the imagenes to set
     */
    public void setImagenes(List<ImagenProducto> imagenes) {
        this.imagenes = imagenes;
    }        

    /**
     * @return the existencia
     */
    public int getExistencia() {
        return existencia;
    }

    /**
     * @param existencia the existencia to set
     */
    public void setExistencia(int existencia) {
        this.existencia = existencia;
    }

    /**
     * @return the clave
     */
    public String getClave() {
        return clave;
    }

    /**
     * @param clave the clave to set
     */
    public void setClave(String clave) {
        this.clave = clave;
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

}
