package neoAtlantis.entity.tests;

import java.util.Properties;
import neoAtlantis.entity.tests.utils.MiAdDAO;
import neoAtlantis.entity.tests.utils.Persona;

/**
 *
 * @author Hiryu (aslhiryu@gmail.com)
 */
public class PruebaAD {
    public static void main(String[] args) throws Exception {
        getUser();
    }
    
    
    static public void getUser() throws Exception{
        Properties p=new Properties();
        p.load(PruebaAD.class.getResourceAsStream("/activeDirectory.properties"));
        
        MiAdDAO dao=new MiAdDAO(p);
        
        Persona pTmp= dao.getPerson("testcrm1");
        System.out.println("=>"+pTmp);
    }
}
