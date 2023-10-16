package pruebas;

import java.util.Date;
import javax.swing.table.TableModel;
import neoAtlantis.utilidades.entity.objects.TableModelEntity;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Hiryu (asl_hiryu@yahoo.com)
 */
public class ProbadorTableModel {
    private Entidad e;

    public ProbadorTableModel() {
        e=new Entidad();

        e.setCadena("cad");
        e.setCaracter('F');
        e.setDob(12.56);
        e.setEs(true);
        e.setIdUnico(12316);
        e.setObjeto(new Date());
        e.setTiene(false);

        System.out.println(e);
    }

    @Test
    public void pruebaTableEntity(){
        TableModel t=new TableModelEntity(e);

        System.out.println( "C: "+t.getColumnCount() );
        System.out.println( "R: "+t.getRowCount() );
    }
}