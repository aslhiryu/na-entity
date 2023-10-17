package neoatlantis.entity.csv.objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Objeto que representa un dato del resultado de la importación de un CSV
 * @author Hiryu (aslhiryu@gmail.com)
 */
@XmlRootElement(name="Resultado")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Resultado", propOrder = {
    "data","right", "details", "row"
})
public class ResultData {
    @XmlElement(name = "Datos", required = true)
    private String[] data;
    @XmlAttribute(name = "procesado", required = true)
    private boolean right;
    @XmlAttribute(name = "detalles", required = true)
    private String details;
    @XmlAttribute(name = "registro", required = true)
    private long row;

    /**
     * @return the data
     */
    public String[] getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(String[] data) {
        this.data = data;
    }

    /**
     * @return the right
     */
    public boolean isRight() {
        return right;
    }

    /**
     * @param right the right to set
     */
    public void setRight(boolean right) {
        this.right = right;
    }

    /**
     * @return the details
     */
    public String getDetails() {
        return details;
    }

    /**
     * @param details the details to set
     */
    public void setDetails(String details) {
        this.details = details;
    }

    /**
     * @return the row
     */
    public long getRow() {
        return row;
    }

    /**
     * @param row the row to set
     */
    public void setRow(long row) {
        this.row = row;
    }
}
