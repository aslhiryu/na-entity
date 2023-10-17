package neoatlantis.entity.objects;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import neoatlantis.entity.ContainerEntities;
import neoatlantis.entity.SimpleEntity;

/**
 *
 * @author Hiryu (aslhiryu@gmail.com)
 */
public class TableModelContainer  extends AbstractTableModel {
    private ContainerEntities container;
    private ArrayList<List> elements=new ArrayList<List>();

    public TableModelContainer(ContainerEntities c){
        this.container=c;

        if( c!=null && c.size()>0 ){
            for(List alTmp: c.get(0).getCache()){
                if( !Modifier.isPublic(((Field)alTmp.get(0)).getModifiers()) && !Modifier.isPrivate(((Field)alTmp.get(0)).getModifiers()) ){
                    continue;
                }

                this.elements.add(alTmp);
            }
        }
    }

    public int getRowCount() {
        return (this.container!=null? this.container.size(): 0);
    }

    public int getColumnCount() {
        return this.elements.size();
    }

    public Object getValueAt(int ren, int col) {
        ArrayList alTmp;

        if (ren >= this.getRowCount() || col >= this.getColumnCount() || ren < 0 || col < 0) {
            throw new ArrayIndexOutOfBoundsException("Intenta accesar a [" + ren + "," + col + "] y este no existe.");
        }

        alTmp=(ArrayList)this.elements.get(col);

        if( Modifier.isPublic(((Field)alTmp.get(0)).getModifiers()) ){
            try{
                return ((Field)alTmp.get(0)).get(this.container.get(ren));
            }catch(Exception ex){
                throw new RuntimeException("No se logro recuperar el valor de '"+((Field)alTmp.get(0)).getName()+"': "+ex);
            }
        }
        //es una entidad
        else if( SimpleEntity.isEntity(((Field)alTmp.get(0)).getType()) ){
            try{
                return "[Entidad]";
            }catch(Exception ex){
                throw new RuntimeException("No se logro recuperar el valor de la entidad '"+((Method)alTmp.get(1)).getName()+"': "+ex);
            }
        }
        //es privado
        else{
            try{
                return ((Method)alTmp.get(1)).invoke(this.container.get(ren));
            }catch(Exception ex){
                throw new RuntimeException("No se logro recuperar el valor de '"+((Method)alTmp.get(1)).getName()+"': "+ex);
            }
        }
    }
}
