package pruebas;

import neoAtlantis.utilidades.entity.SimpleEntity;

/**
 *
 * @author Hiryu (asl_hiryu@yahoo.com)
 */
public class Entidad2 extends SimpleEntity {
    public static final String vers="1.0";

    private int id;
    private double val;

    public Entidad2(){
        this.id=30;
        this.val=15646.56;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the val
     */
    public double getVal() {
        return val;
    }

    /**
     * @param val the val to set
     */
    public void setVal(double val) {
        this.val = val;
    }

}
