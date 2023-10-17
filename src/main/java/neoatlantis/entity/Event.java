package neoatlantis.entity;

import java.io.Serializable;
import java.util.Date;
import neoatlantis.entity.SimpleEntity;

/**
 * Clase que define a un evento que se puede generar en cualquier momento
 * @author Hiryu (aslhiryu@gmail.com)
 */
public class Event extends SimpleEntity implements  Serializable {
    private  String origen ;
    private  String detalle ;
    private  Date fecha ;

    /**
     * @return the origen
     */
    public String getOrigin() {
        return origen;
    }

    /**
     * @param origen the origen to set
     */
    public void setOrigin(String origen) {
        this.origen = origen;
    }

    /**
     * @return the detalle
     */
    public String getDetail() {
        return detalle;
    }

    /**
     * @param detalle the detalle to set
     */
    public void setDetail(String detalle) {
        this.detalle = detalle;
    }

    /**
     * @return the fecha
     */
    public Date getEventDate() {
        return fecha;
    }

    /**
     * @param fecha the fecha to set
     */
    public void setEventDate(Date fecha) {
        this.fecha = fecha;
    }
    
}
