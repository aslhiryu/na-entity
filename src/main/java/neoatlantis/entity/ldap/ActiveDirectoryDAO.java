package neoatlantis.entity.ldap;

import neoatlantis.entity.ldap.LdapDAO;
import java.util.Properties;
import neoatlantis.entity.SimpleEntity;
import neoatlantis.utils.ldap.ConfigurationLDAP;
import neoatlantis.utils.ldap.exceptions.LdapConfigurationException;
import org.apache.log4j.Logger;

/**
 *
 * @author Hiryu (aslhiryu@gmail.com)
 */
public abstract class ActiveDirectoryDAO<E extends SimpleEntity> extends LdapDAO<E> {
    private static final Logger DEBUGER = Logger.getLogger(ActiveDirectoryDAO.class);

    public ActiveDirectoryDAO(Properties props) throws LdapConfigurationException{
        super(props);
    }

    public ActiveDirectoryDAO(Properties props, String user, String pass) throws LdapConfigurationException{
        super(props, user, pass);
    }

    
    
    
    
    @Override
    protected String getDNString(){
        StringBuilder sb=new StringBuilder();
        sb.append(user).append("@");
        if( this.config.getProperty(ConfigurationLDAP.BASE_PARAM)!=null ){
            String[] cTmp=this.config.getProperty(ConfigurationLDAP.BASE_PARAM).split(",");

            for(int i=0; cTmp!=null&&i<cTmp.length; i++){
                if( cTmp[i].toLowerCase().indexOf("dc=")==0 ){
                    if(i>0){
                        sb.append(".");
                    }
                    sb.append(cTmp[i].substring(3));
                }
            }                
        }

        return sb.toString();
    }
}
