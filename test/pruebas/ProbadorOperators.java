package pruebas;

import neoAtlantis.utilidades.entity.utils.operations.PlusContainer;
import neoAtlantis.utilidades.entity.utils.operations.PercentContainer;
import neoAtlantis.utilidades.entity.utils.operations.AveragingContainer;
import java.util.Date;
import neoAtlantis.utilidades.entity.ContainerEntities;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Hiryu (asl_hiryu@yahoo.com)
 */
public class ProbadorOperators {
    private Entidad e;
    private ContainerEntities<Entidad> con;

    public ProbadorOperators() {
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
    public void pruebaSuma(){
        System.out.println("=> "+PlusContainer.calcular(con, 2));
    }

    @Test
    public void pruebaSuma2(){
        System.out.println("-> "+PlusContainer.calcular(con, "dob"));
    }

    @Test
    public void pruebaSuma3(){
        System.out.println("-> "+PlusContainer.calcular(con));
    }

    @Test
    public void pruebaPromedio(){
        System.out.println("=> "+AveragingContainer.calcular(con, 2));
    }

    @Test
    public void pruebaPromedio2(){
        System.out.println("-> "+AveragingContainer.calcular(con, "dob"));
    }

    @Test
    public void pruebaPorcentaje(){
        System.out.println("=> "+PercentContainer.calcular(con, 2));
    }

    @Test
    public void pruebaPorcentaje2(){
        System.out.println("-> "+PercentContainer.calcular(con, "dob"));
    }
}