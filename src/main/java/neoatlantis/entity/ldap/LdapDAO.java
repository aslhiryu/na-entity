package neoatlantis.entity.ldap;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;
import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import neoatlantis.entity.SimpleEntity;
import neoatlantis.utils.ldap.ConfigurationLDAP;
import neoatlantis.utils.ldap.exceptions.LdapConfigurationException;
import neoatlantis.utils.ldap.exceptions.LdapConnectionException;
import neoatlantis.utils.ldap.exceptions.LdapInvalidUserException;
import org.apache.log4j.Logger;

/**
 *
 * @author Hiryu (aslhiryu@gmail.com)
 */
public abstract class LdapDAO<E extends SimpleEntity> {
    private static final Logger DEBUGER = Logger.getLogger(LdapDAO.class);
    
    public static final String USER_DATA="user";
    public static final String PASS_DATA="password";

    protected Properties config;    
    protected Hashtable auth = new Hashtable();
    protected String user;
    protected String pass;


    public LdapDAO(Properties props) throws LdapConfigurationException{
        try{
            ConfigurationLDAP.validateConfig(props);
        }catch(Exception ex){
            throw new LdapConfigurationException("No se logro generar el objeto", ex);
        }
        this.config=props;
        
        //verifica si trae datos del usuario
        if (this.config.containsKey(USER_DATA)) {
            this.user=this.config.getProperty(USER_DATA);
        }
        if (this.config.containsKey(PASS_DATA)) {
            this.pass=this.config.getProperty(PASS_DATA);
        }
    }

    public LdapDAO(Properties props, String user, String pass) throws LdapConfigurationException{
        this(props);
        this.user=user;
        this.pass=pass;
    }
    

    
    
    
    public void setUser(String user){
        this.user=user;
    }
    
    public String getUser(){
        return this.user;
    }

    public void setPass(String pass){
        this.pass=pass;
    }
    
    public String getPass(){
        return this.pass;
    }
    
    public E getPerson(String user) throws LdapConfigurationException, LdapConnectionException, LdapInvalidUserException{
        String filtro=this.config.getProperty(ConfigurationLDAP.USER_PARAM)+"="+user;
        E to=null;
        
        DirContext ctx=this.createContext();
        
        try{
            //recupero información del usuario
           SearchControls searchCtls = new SearchControls();
           searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
           NamingEnumeration res = ctx.search(this.config.getProperty(ConfigurationLDAP.BASE_PARAM), filtro, searchCtls);
           DEBUGER.debug("Realiza la busqueda del usuario con: "+filtro);
           to=this.parse(res);
        } 
        catch (AuthenticationException authEx) {
            DEBUGER.debug("No existe el usuario '"+this.getUser()+"': "+authEx);
            throw new LdapInvalidUserException("El usuario no es valido", authEx);
        } 
        catch (NamingException namEx) {
            DEBUGER.error("No se logro contactar con el LDAP.", namEx);
            throw new LdapConnectionException("Problema al contactar al LDAP", namEx);
        }
        finally{
            try{
                ctx.close();
            }
            catch(Exception ex){
                ctx=null;
            }
        }
        
        
        return to;
    }
    
    public List<E> getPersons(String filtro) throws LdapConfigurationException, LdapConnectionException, LdapInvalidUserException{
        List<E> lTmp=new ArrayList();
        
        DirContext ctx=this.createContext();
        
        try{
            //recupero información del usuario
           SearchControls searchCtls = new SearchControls();
           searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
           NamingEnumeration res = ctx.search(this.config.getProperty(ConfigurationLDAP.BASE_PARAM), filtro, searchCtls);
           DEBUGER.debug("Realiza la busqueda del usuario con: "+filtro);
           lTmp=this.parseMultiple(res);
        } 
        catch (AuthenticationException authEx) {
            DEBUGER.debug("No existe el usuario '"+this.getUser()+"': "+authEx);
            throw new LdapInvalidUserException("El usuario no es valido", authEx);
        } 
        catch (NamingException namEx) {
            DEBUGER.error("No se logro contactar con el LDAP.", namEx);
            throw new LdapConnectionException("Problema al contactar al LDAP", namEx);
        }
        finally{
            try{
                ctx.close();
            }
            catch(Exception ex){
                ctx=null;
            }
        }
        
        
        return lTmp;
    }
    
    
    
    
    // Metodos abstractos --------------------------------------------------------------------------------------------------
    
    abstract public E parse(NamingEnumeration res) throws LdapConfigurationException;
    abstract public List<E> parseMultiple(NamingEnumeration res) throws LdapConfigurationException;

    
    
    
    
    
    
    // Metodos protegidos--------------------------------------------------------------------------------------------------

/**
     * Recupera la cadena de conexión al LDAP
     * @return 
     */
    protected String getDNString(){
        return "cn="+this.user+","+this.config.getProperty(ConfigurationLDAP.BASE_PARAM);
    }

    /**
     * Prepara el contexto para conectarse al LDAP
     * @return 
     * @throws LdapConfigurationException 
     */
    protected DirContext createContext() throws LdapConfigurationException{
        DirContext ctx=null;
        DEBUGER.debug("Genera contexto de LDAP con: "+this.config);
        
        try{
            DEBUGER.debug("DN : "+this.getDNString());
            
            auth.put(Context.SECURITY_PRINCIPAL, this.getDNString());
            auth.put(Context.SECURITY_CREDENTIALS, this.getPass());
            auth.put(Context.INITIAL_CONTEXT_FACTORY,"com.sun.jndi.ldap.LdapCtxFactory");
            auth.put(Context.PROVIDER_URL, this.config.getProperty(ConfigurationLDAP.URL_PARAM));
            auth.put(Context.SECURITY_AUTHENTICATION, "simple");
            auth.put(Context.REFERRAL, "follow");
            auth.put("com.sun.jndi.ldap.read.timeout", ""+Integer.parseInt(this.config.getProperty(ConfigurationLDAP.TIMEOUT_PARAM))*1000);
            
            ctx = new InitialDirContext(auth); 
        }catch(Exception ex){
            DEBUGER.error("No se logro  generar la conexión al LDAP.", ex);
            throw new LdapConfigurationException("Se encontro un problema en la configuración", ex);
        }
        
        DEBUGER.debug("Genera conexión al LDAP");
        return ctx;
    }





    
    // Metodos estaticos ----------------------------------------------------------------------------------------------------

    public static String getDetailError(String error){
        DEBUGER.debug("Error  a evaluar: "+error);
        
        if( error!=null && error.trim().startsWith("[LDAP: error code 49") && error.indexOf(" error, data ")>0 ){
            int posIni=error.indexOf(" error, data ")+13;
            int posFin=error.indexOf(",", posIni+1);
            String errorCode="";
            
            DEBUGER.debug("Posicional Inicial: "+posIni+", Posicion final: "+posFin);
            errorCode=error.substring(posIni, posFin);
            DEBUGER.debug("Codigo de error: "+errorCode);
            
            if(errorCode.equals("525")){
                return "User not found";
            }
            else if(errorCode.equals("52e")){
                return "Invalid credentials";
            }
            else if(errorCode.equals("530")){
                return "Not permitted to logon at this time";
            }
            else if(errorCode.equals("531")){
                return "Not permitted to logon at this workstation";
            }
            else if(errorCode.equals("532")){
                return "Password expired";
            }
            else if(errorCode.equals("533")){
                return "Account disabled";
            }
            else if(errorCode.equals("701")){
                return "Account expired";
            }
            else if(errorCode.equals("773")){
                return "User must reset password";
            }
            else if(errorCode.equals("775")){
                return "User account locked";
            }
            else{
                return "Unknow error";
            }            
        }
        else{
            return "Unknow detail";
        }
    }
}
