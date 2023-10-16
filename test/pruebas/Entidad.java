package pruebas;

import neoAtlantis.utilidades.entity.SimpleEntity;
import neoAtlantis.utilidades.entity.annotations.IdEntity;

/**
 *
 * @author Hiryu (asl_hiryu@yahoo.com)
 */
public class Entidad extends SimpleEntity {
    public static final String vers="1.25";    
    protected int prote;
    
    @IdEntity
    private int idUnico;
    private double dob;
    private char caracter;
    private String cadena;
    private Object objeto;
    private boolean es;
    private boolean tiene;
    private Entidad2 entidad=new Entidad2();

    /**
     * @return the idUnico
     */
    public int getIdUnico() {
        return idUnico;
    }

    /**
     * @param idUnico the idUnico to set
     */
    public void setIdUnico(int idUnico) {
        this.idUnico = idUnico;
    }

    /**
     * @return the dob
     */
    public double getDob() {
        return dob;
    }

    /**
     * @param dob the dob to set
     */
    public void setDob(double dob) {
        this.dob = dob;
    }

    /**
     * @return the caracter
     */
    public char getCaracter() {
        return caracter;
    }

    /**
     * @param caracter the caracter to set
     */
    public void setCaracter(char caracter) {
        this.caracter = caracter;
    }

    /**
     * @return the cadena
     */
    public String getCadena() {
        return cadena;
    }

    /**
     * @param cadena the cadena to set
     */
    public void setCadena(String cadena) {
        this.cadena = cadena;
    }

    /**
     * @return the objeto
     */
    public Object getObjeto() {
        return objeto;
    }

    /**
     * @param objeto the objeto to set
     */
    public void setObjeto(Object objeto) {
        this.objeto = objeto;
    }

    /**
     * @return the es
     */
    public boolean isEs() {
        return es;
    }

    /**
     * @param es the es to set
     */
    public void setEs(boolean es) {
        this.es = es;
    }

    /**
     * @return the tiene
     */
    public boolean hasTiene() {
        return tiene;
    }

    /**
     * @param tiene the tiene to set
     */
    public void setTiene(boolean tiene) {
        this.tiene = tiene;
    }

    /**
     * @return the entidad
     */
    public Entidad2 getEntidad() {
        return entidad;
    }

    /**
     * @param entidad the entidad to set
     */
    public void setEntidad(Entidad2 entidad) {
        this.entidad = entidad;
    }
}
