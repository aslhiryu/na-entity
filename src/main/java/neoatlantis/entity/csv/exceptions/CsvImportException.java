package neoatlantis.entity.csv.exceptions;

/**
 * Excepcion que se genera por exporta un archivo CSV
 * @author Hiryu (aslhiryu@gmail.com)
 */
public class CsvImportException extends Exception {
    public CsvImportException(String msg){
        super(msg);
    }
    
    public CsvImportException(String msg, Throwable ex){
        super(msg, ex);
    }
}
