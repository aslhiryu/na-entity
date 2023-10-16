package neoAtlantis.utilidades.entity.objects;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import neoAtlantis.utilidades.entity.SimpleEntity;

/**
 *
 * @author Hiryu (asl_hiryu@yahoo.com)
 */
public class TableModelEntity  extends AbstractTableModel {
    private SimpleEntity entity;
    private ArrayList<List> elements=new ArrayList<List>();

    public TableModelEntity(SimpleEntity e){
        this.entity=e;

        for(List alTmp: e.getCache()){
            if( !Modifier.isPublic(((Field)alTmp.get(0)).getModifiers()) && !Modifier.isPrivate(((Field)alTmp.get(0)).getModifiers()) ){
                continue;
            }

            this.elements.add(alTmp);
        }
    }

    public int getRowCount() {
        return 1;
    }

    public int getColumnCount() {
        return this.elements.size();
    }

    public Object getValueAt(int ren, int col) {
        ArrayList alTmp;

        if (ren > 0 || col >= this.getColumnCount() || ren < 0 || col < 0) {
            throw new ArrayIndexOutOfBoundsException("Intenta accesar a [" + ren + "," + col + "] y este no existe.");
        }

        alTmp=(ArrayList)this.elements.get(col);

        if( Modifier.isPublic(((Field)alTmp.get(0)).getModifiers()) ){
            try{
                return ((Field)alTmp.get(0)).get(this.entity);
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
                return ((Method)alTmp.get(1)).invoke(this.entity);
            }catch(Exception ex){
                throw new RuntimeException("No se logro recuperar el valor de '"+((Method)alTmp.get(1)).getName()+"': "+ex);
            }
        }
    }

}
