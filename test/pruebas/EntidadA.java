package pruebas;

import java.util.Date;
import neoAtlantis.utilidades.entity.SimpleEntity;

public class EntidadA extends SimpleEntity {
    private int id;
    private long id2;
    private String nombre;
    private char caracter;
    private char[] arreglo1;
    private int[] arreglo2;
    private double doble;
    private boolean boleano;
    private Boolean boleano2;
    private Date fecha;
    private EntidadB bajo;

    public EntidadA() {
        this.bajo = new EntidadB();
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
     * @return the id2
     */
    public long getId2() {
        return id2;
    }

    /**
     * @param id2 the id2 to set
     */
    public void setId2(long id2) {
        this.id2 = id2;
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
     * @return the arreglo1
     */
    public char[] getArreglo1() {
        return arreglo1;
    }

    /**
     * @param arreglo1 the arreglo1 to set
     */
    public void setArreglo1(char[] arreglo1) {
        this.arreglo1 = arreglo1;
    }

    /**
     * @return the arreglo2
     */
    public int[] getArreglo2() {
        return arreglo2;
    }

    /**
     * @param arreglo2 the arreglo2 to set
     */
    public void setArreglo2(int[] arreglo2) {
        this.arreglo2 = arreglo2;
    }

    /**
     * @return the doble
     */
    public double getDoble() {
        return doble;
    }

    /**
     * @param doble the doble to set
     */
    public void setDoble(double doble) {
        this.doble = doble;
    }

    /**
     * @return the boleano
     */
    public boolean isBoleano() {
        return boleano;
    }

    /**
     * @param boleano the boleano to set
     */
    public void setBoleano(boolean boleano) {
        this.boleano = boleano;
    }

    /**
     * @return the boleano2
     */
    public Boolean getBoleano2() {
        return boleano2;
    }

    /**
     * @param boleano2 the boleano2 to set
     */
    public void setBoleano2(Boolean boleano2) {
        this.boleano2 = boleano2;
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
     * @return the Bajo
     */
    public EntidadB getBajo() {
        return bajo;
    }

    /**
     * @param Bajo the Bajo to set
     */
    public void setBajo(EntidadB Bajo) {
        this.bajo = Bajo;
    }

}