package neoatlantis.entity.csv.objects;

import neoatlantis.entity.SimpleEntity;

/**
 * Objeto que representa una celda a importar de un CSV
 * @author Hiryu (aslhiryu@gmail.com)
 */
public class CellDefinition extends SimpleEntity {
    public static final int SKIP_COLUM=-2;
    public static final int NO_ASSIGNED=-1;
    
    private String label;
    private String columnDB;
    private boolean date;
    private int columnPosition;

    public CellDefinition(String label, String columnDB, boolean date){
        this(label, columnDB, date, NO_ASSIGNED);
    }
    
    public CellDefinition(String label, String columnDB, boolean date, int columnPosition){
        this.label=label;
        this.columnDB=columnDB;
        this.date=date;
        this.columnPosition=columnPosition;
    }
    
    
    
    /**
     * @return the label
     */
    public String getLabel() {
        return label;
    }

    /**
     * @param label the label to set
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * @return the columnDB
     */
    public String getColumnDB() {
        return columnDB;
    }

    /**
     * @param columnDB the columnDB to set
     */
    public void setColumnDB(String columnDB) {
        this.columnDB = columnDB;
    }

    /**
     * @return the date
     */
    public boolean isDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(boolean date) {
        this.date = date;
    }

    /**
     * @return the columnPosition
     */
    public int getColumnPosition() {
        return columnPosition;
    }

    /**
     * @param columnPosition the columnPosition to set
     */
    public void setColumnPosition(int columnPosition) {
        this.columnPosition = columnPosition;
    }
}
