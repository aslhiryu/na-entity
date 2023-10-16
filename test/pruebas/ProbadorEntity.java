package pruebas;

import java.util.Date;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Hiryu (asl_hiryu@yahoo.com)
 */
public class ProbadorEntity {
    private Entidad e, e2;

    public ProbadorEntity(){
        e=new Entidad();
        e2=new Entidad();
        
        e.setCadena("cad");
        e.setCaracter('F');
        e.setDob(12.56);
        e.setEs(true);
        e.setIdUnico(12316);
        e.setObjeto(new Date());
        e.setTiene(false);
    }

    @Test
    public void pruebaEntidad(){
        System.out.println(e);
    }

    @Test
    public void pruebaXml(){
        System.out.println(e.toStringXml());
    }

    @Test
    public void pruebaComparacion(){
        System.out.println(e.equals(e2));
    }

    @Test
    public void pruebaClonacion(){
        Entidad eTmp=(Entidad)e.clone();
        System.out.println(e.equals(eTmp));
    }

    @Test
    public void pruebaIdEntidad(){
        for(String n: e.getIdName()){
            System.out.println("KEY: "+n);
        }
    }
}