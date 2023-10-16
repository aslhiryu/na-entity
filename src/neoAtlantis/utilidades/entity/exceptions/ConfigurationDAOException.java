package neoAtlantis.utilidades.entity.exceptions;

/**
 *
 * @author Hiryu (aslhiryu@gmail.com)
 */
public class ConfigurationDAOException extends RuntimeException {
    public ConfigurationDAOException(Exception ex){
        super(ex);
    }

    public ConfigurationDAOException(String ex){
        super(ex);
    }
}
