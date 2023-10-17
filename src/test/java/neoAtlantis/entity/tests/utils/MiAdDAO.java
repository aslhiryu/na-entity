package neoAtlantis.entity.tests.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.naming.NamingEnumeration;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchResult;
import neoatlantis.entity.ldap.ActiveDirectoryDAO;
import neoatlantis.utils.ldap.exceptions.LdapConfigurationException;

/**
 *
 * @author Hiryu (aslhiryu@gmail.com)
 */
public class MiAdDAO extends ActiveDirectoryDAO<Persona> {

    public MiAdDAO(Properties props) throws LdapConfigurationException {
        super(props);
    }

    @Override
    public Persona parse(NamingEnumeration res) throws LdapConfigurationException {
        Persona p=null;
        SearchResult sr;
        Attributes attrs;
        Attribute at;
        
        try{
            if( res.hasMore() ){
                p=new Persona();
                
                sr = (SearchResult)res.next();
                attrs = sr.getAttributes();
                
                if (attrs != null) {                    
                    //recupera el usuario
                    at=attrs.get(this.config.getProperty("dataUser"));
                    if( at!=null ){
                        p.setUser((String)at.get());
                    }
                    //recupera el correo
                    at=attrs.get(this.config.getProperty("dataMail"));
                    if( at!=null ){
                        p.setMail((String)at.get());
                    }
                    //recupera el nombre
                    at=attrs.get(this.config.getProperty("dataName"));
                    if( at!=null ){
                        p.setNombre((String)at.get());
                    }
                }                
            }
        }
        catch(Exception ex){
            throw new LdapConfigurationException("Error al parsear el objeto", ex);
        }
        
        return p;
    }

    @Override
    public List<Persona> parseMultiple(NamingEnumeration res) throws LdapConfigurationException {
        List<Persona> lTmp=new ArrayList();
        Persona p=null;
        SearchResult sr;
        Attributes attrs;
        Attribute at;
        
        try{
            while( res.hasMore() ){
                p=new Persona();
                
                sr = (SearchResult)res.next();
                attrs = sr.getAttributes();
                
                if (attrs != null) {                    
                    //recupera el usuario
                    at=attrs.get(this.config.getProperty("dataUser"));
                    if( at!=null ){
                        p.setUser((String)at.get());
                    }
                    //recupera el correo
                    at=attrs.get(this.config.getProperty("dataMail"));
                    if( at!=null ){
                        p.setMail((String)at.get());
                    }
                    //recupera el nombre
                    at=attrs.get(this.config.getProperty("dataName"));
                    if( at!=null ){
                        p.setNombre((String)at.get());
                    }
                }          
                lTmp.add(p);
            }
        }
        catch(Exception ex){
            throw new LdapConfigurationException("Error al parsear el objeto", ex);
        }
        
        return lTmp;
    }
    
}
