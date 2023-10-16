package pruebas;

import java.util.Date;
import neoAtlantis.utilidades.entity.ContainerEntities;
import neoAtlantis.utilidades.entity.utils.sorters.*;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Hiryu (asl_hiryu@yahoo.com)
 */
public class ProbadorOrders {
    private Entidad e;
    private ContainerEntities<Entidad> con;

    public ProbadorOrders() {
        e=new Entidad();

        e.setCadena("cad");
        e.setCaracter('F');
        e.setDob(12.56);
        e.setEs(true);
        e.setIdUnico(12316);
        e.setObjeto(new Date());
        e.setTiene(false);

        con=new ContainerEntities<Entidad>(true);
        con.add(new Entidad());
        con.add(new Entidad());
        con.add(new Entidad());
        con.add(e);

        System.out.println(con);
    }

    @Test
    public void pruebaNatural(){
        NaturalSort.ordenar(con, 2);
        System.out.println("=> "+con);
    }

    @Test
    public void pruebaNatural2(){
        NaturalSort.ordenar(con, 2, TipoOrdenacion.DESCENDENTE);
        System.out.println("=> "+con);
    }
}