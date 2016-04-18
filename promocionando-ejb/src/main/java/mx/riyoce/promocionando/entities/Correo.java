/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mx.riyoce.promocionando.entities;

import javax.persistence.CascadeType;
import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import static javax.persistence.FetchType.EAGER;

/**
 *
 * @author alfonso
 */
@Entity
public class Correo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombreDe;
    private String mailDe;
    private String nombrePara;    
    private String mailPara;

    @Lob @Basic(fetch=EAGER)
    private String cuerpo;
    private String type;
    private String acerca;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fecha;

    @OneToMany(mappedBy="mensaje", cascade=CascadeType.ALL)
    private List<Attachment> attachments;

    public Correo(){
        this.attachments = new LinkedList<>();
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

        if (!(object instanceof Correo)) {
            return false;
        }
        Correo other = (Correo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "entities.Correo[ id=" + id + " ]";
    }



    /**
     * @return the cuerpo
     */
    public String getCuerpo() {
        return cuerpo;
    }

    /**
     * @param cuerpo the cuerpo to set
     */
    public void setCuerpo(String cuerpo) {
        this.cuerpo = cuerpo;
    }

    /**
     * @return the acerca
     */
    public String getAcerca() {
        return acerca;
    }

    /**
     * @param acerca the acerca to set
     */
    public void setAcerca(String acerca) {
        this.acerca = acerca;
    }

    /**
     * @return the attachments
     */
    public List<Attachment> getAttachments() {
        return attachments;
    }

    /**
     * @param attachments the attachments to set
     */
    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }

    /**
     * @return the fecha
     */
    public Date getFecha() {
        return fecha;
    }

    /**
     * @param fecha the fecha to set
     */
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }



    /**
     * @return the nombreDe
     */
    public String getNombreDe() {
        return nombreDe;
    }

    /**
     * @param nombreDe the nombreDe to set
     */
    public void setNombreDe(String nombreDe) {
        this.nombreDe = nombreDe;
    }

    /**
     * @return the nombrePara
     */
    public String getNombrePara() {
        return nombrePara;
    }

    /**
     * @param nombrePara the nombrePara to set
     */
    public void setNombrePara(String nombrePara) {
        this.nombrePara = nombrePara;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the mailDe
     */
    public String getMailDe() {
        return mailDe;
    }

    /**
     * @param mailDe the mailDe to set
     */
    public void setMailDe(String mailDe) {
        this.mailDe = mailDe;
    }

    /**
     * @return the mailPara
     */
    public String getMailPara() {
        return mailPara;
    }

    /**
     * @param mailPara the mailPara to set
     */
    public void setMailPara(String mailPara) {
        this.mailPara = mailPara;
    }


}
