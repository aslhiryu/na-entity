package neoatlantis.entity.csv.objects;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Objeto que contiene toda la información de una importación de CSV
 * @author Hiryu (aslhiryu@gmail.com)
 */
@XmlRootElement(name="ResultadoGeneral")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ResultadoGeneral", propOrder = {
    "data"
})
public class ResultDataGeneral {
    @XmlElement(name = "Resultado", required = true)
    private List<ResultData> data;


    /**
     * @return the data
     */
    public List<ResultData> getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(List<ResultData> data) {
        this.data = data;
    }
}
