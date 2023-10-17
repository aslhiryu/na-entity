package neoatlantis.entity.csv.objects;

import java.util.ArrayList;
import java.util.List;
import neoatlantis.entity.csv.exceptions.CsvImportException;

/**
 * Objeto que representa el modelo utilizado para realizar una importación de CSV
 * @author Hiryu (aslhiryu@gmail.com)
 */
public class DataModel {
    private char columnDelimiter;
    private char stringDelimiter;
    private String dateFormat;
    private List<CellDefinition> cells;
    private String key;
    private String table;
    private boolean withTitles;

    /**
     * Constructor
     * @param columnDelimiter Delimitador de columna
     * @param stringDelimiter Delimitador de cadenas
     * @param table Nombre d ela tabla
     * @param key  Nombre del campo llave
     */
    public DataModel(char columnDelimiter, char stringDelimiter, String table, String key){
        this.cells=new ArrayList();
        this.dateFormat="dd/MM/yyyy";
        this.columnDelimiter=columnDelimiter;
        this.stringDelimiter=stringDelimiter;
        this.table=table;
        this.key=key;
    }
    
    /**
     * Recupera la posicion del campo llave
     * @return Entero que indica en que posision esta el campo llave
     * @throws CsvImportException 
     */
    public int getColumnKey() throws CsvImportException{
        for(CellDefinition c: this.cells){
            if( c.getColumnDB().equalsIgnoreCase(this.key) ){
                return c.getColumnPosition();
            }
        }
        
        throw new CsvImportException("No se tienen datos asignado para la clave");
    }
    
    /**
     * Valida si existen campos sin asignar
     * @return true, si existen campos sin asignar
     */
    public boolean existCellsNoAssigned(){
        for(CellDefinition c: this.cells){
            if( c.getColumnPosition()==CellDefinition.NO_ASSIGNED ){
                return true;
            }
        }

        return false;
    }
    
    /**
     * @return the columnDelimiter
     */
    public char getColumnDelimiter() {
        return columnDelimiter;
    }

    /**
     * @param columnDelimiter the columnDelimiter to set
     */
    public void setColumnDelimiter(char columnDelimiter) {
        this.columnDelimiter = columnDelimiter;
    }

    /**
     * @return the stringDelimiter
     */
    public char getStringDelimiter() {
        return stringDelimiter;
    }

    /**
     * @param stringDelimiter the stringDelimiter to set
     */
    public void setStringDelimiter(char stringDelimiter) {
        this.stringDelimiter = stringDelimiter;
    }

    /**
     * @return the dateFormat
     */
    public String getDateFormat() {
        return dateFormat;
    }

    /**
     * @param dateFormat the dateFormat to set
     */
    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    /**
     * @return the cells
     */
    public List<CellDefinition> getCells() {
        return cells;
    }

    /**
     * @param cells the cells to set
     */
    public void setCells(List<CellDefinition> cells) {
        this.cells = cells;
    }

    /**
     * @return the key
     */
    public String getKey() {
        return key;
    }

    /**
     * @param key the key to set
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * @return the table
     */
    public String getTable() {
        return table;
    }

    /**
     * @param table the table to set
     */
    public void setTable(String table) {
        this.table = table;
    }

    /**
     * @return the withTitles
     */
    public boolean isWithTitles() {
        return withTitles;
    }

    /**
     * @param withTitles the withTitles to set
     */
    public void setWithTitles(boolean withTitles) {
        this.withTitles = withTitles;
    }
    
}
