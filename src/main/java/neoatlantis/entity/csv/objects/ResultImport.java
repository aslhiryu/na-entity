package neoatlantis.entity.csv.objects;

import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 * Objeto que representa el resultado obtenido de una importación de un CSV
 * @author Hiryu (aslhiryu@gmail.com)
 */
public class ResultImport {
    private long inserted;
    private long updated;
    private long withError;
    private String fileName;
    private List<ResultData> data;

    public ResultImport(){
        this.data=new ArrayList();
    }
    
    public long getTotal(){
        return this.inserted+this.updated+this.withError;
    }
    
    /**
     * @return the inserted
     */
    public long getInserted() {
        return inserted;
    }

    /**
     * @param inserted the inserted to set
     */
    public void setInserted(long inserted) {
        this.inserted = inserted;
    }

    /**
     * @return the updated
     */
    public long getUpdated() {
        return updated;
    }

    /**
     * @param updated the updated to set
     */
    public void setUpdated(long updated) {
        this.updated = updated;
    }

    /**
     * @return the withError
     */
    public long getWithError() {
        return withError;
    }

    /**
     * @param withError the withError to set
     */
    public void setWithError(long withError) {
        this.withError = withError;
    }

    /**
     * @return the fileName
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * @param fileName the fileName to set
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

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

    public String getDataXml() throws JAXBException{
        return getStringResultGeneral(this.data);
    }
    
    public void setDataXml(String xml) throws JAXBException{
        this.data=getObjectResultGeneral(xml);
    }

    
    public static String getStringResultData(ResultData data) throws JAXBException{
        ByteArrayOutputStream out=new ByteArrayOutputStream();
        JAXBContext jc = JAXBContext.newInstance(ResultData.class);
        Marshaller m=jc.createMarshaller();

        m.marshal(data, out);                   
        return new String(out.toByteArray(), Charset.forName("UTF-8"));
    }

    public static String getStringResultGeneral(List<ResultData> data) throws JAXBException{
        ResultDataGeneral res=new ResultDataGeneral();
        res.setData(data);
        
        ByteArrayOutputStream out=new ByteArrayOutputStream();
        JAXBContext jc = JAXBContext.newInstance(ResultDataGeneral.class);
        Marshaller m=jc.createMarshaller();

        m.marshal(res, out);                
        return new String(out.toByteArray(), Charset.forName("UTF-8"));
    }

    public static List<ResultData> getObjectResultGeneral(String xml) throws JAXBException{
        JAXBContext jc = JAXBContext.newInstance(ResultDataGeneral.class);
        Unmarshaller jaxbUnmarshaller = jc.createUnmarshaller();
        List<ResultData> obj = (List<ResultData>) jaxbUnmarshaller.unmarshal(new StringReader(xml));
        
        return obj;
    }

}
