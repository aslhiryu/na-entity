package pruebas;

import neoAtlantis.utilidades.entity.utils.converters.Entity2Array;
import neoAtlantis.utilidades.entity.utils.converters.Container2Xls;
import neoAtlantis.utilidades.entity.utils.converters.Container2Matrix;
import neoAtlantis.utilidades.entity.utils.converters.Container2Html;
import neoAtlantis.utilidades.entity.utils.converters.Container2Csv;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;
import neoAtlantis.utilidades.entity.ContainerEntities;
import neoAtlantis.utilidades.entity.utils.converters.*;
import neoAtlantis.utilidades.entity.utils.converters.Entity2Html.TipoHtml;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Hiryu (asl_hiryu@yahoo.com)
 */
public class ProbadorParses {
    private Entidad e;
    private ContainerEntities<Entidad> con;
    private Properties n=new Properties();
    private Properties p=new Properties();

    public ProbadorParses() {
        e=new Entidad();

        e.setCadena("cad");
        e.setCaracter('F');
        e.setDob(12.56);
        e.setEs(true);
        e.setIdUnico(12316);
        e.setObjeto(new Date());
        e.setTiene(false);

        System.out.println(e);

        con=new ContainerEntities<Entidad>(true);
        con.add(new Entidad());
        con.add(new Entidad());
        con.add(new Entidad());
        con.add(e);

        n.setProperty("idUnico", "Identificador Unico");
        n.setProperty("objeto", "Fecha");
        n.setProperty("es", "Es Valido?");

        p.setProperty("idUnico", "true");
        p.setProperty("objeto", "Yes");
        p.setProperty("es", "1");
        p.setProperty("tiene", "si");
        p.setProperty("cadena", "");
    }

    @Test
    public void pruebaEntidadArray(){
        String[] s=Entity2Array.parse(e);

        for(String c: s){
            System.out.println("-> "+c);
        }
    }

    @Test
    public void pruebaEntidadHtml(){
        System.out.println("-> "+Entity2Html.parse(e));
    }

    @Test
    public void pruebaEntidadHtmlUl(){
        System.out.println("-> "+Entity2Html.parse(e, TipoHtml.LIST));
    }

    @Test
    public void pruebaContainerCsv(){
        System.out.println("-> "+Container2Csv.parse(con));
    }

    @Test
    public void pruebaContainerCsvNombres(){
        System.out.println("-> "+Container2Csv.parse(con, n, p));
    }

    @Test
    public void pruebaContainerHtml(){
        System.out.println("-> "+Container2Html.parse(con));
    }

    @Test
    public void pruebaContainerHtmlNombres(){
        System.out.println("-> "+Container2Html.parse(con, n, p));
    }

    @Test
    public void pruebaContainerXls() throws IOException{
        Container2Xls.parse(con, new FileOutputStream("c:\\tempo.xls"));
    }

    @Test
    public void pruebaContainerXlsNombres() throws IOException{
        Container2Xls.parse(con, new FileOutputStream("c:\\tempo2.xls"), n, p, 3);
    }

    @Test
    public void pruebaContainerMatriz() throws IOException{
        String[][] a=Container2Matrix.parse(con);

        for(String[] a2: a){
            for(String c: a2){
                System.out.print(c+",\t");
            }
            System.out.println();
        }
    }
}