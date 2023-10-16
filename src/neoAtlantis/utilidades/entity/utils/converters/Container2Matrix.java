package neoAtlantis.utilidades.entity.utils.converters;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import neoAtlantis.utilidades.entity.ContainerEntities;
import neoAtlantis.utilidades.entity.SimpleEntity;

/**
 *
 * @author Hiryu (asl_hiryu@yahoo.com)
 */
public class Container2Matrix {
    public static String[][] parse(ContainerEntities cto) {
        //si el objeto no existe
        if (cto == null || cto.size() == 0) {
            return null;
        }

        ArrayList<ArrayList> al=new ArrayList<ArrayList>();
        ArrayList al2;

        //expongo los valores
        for(int j=0; j<cto.size(); j++){
            al2=new ArrayList();

            for(List alTmp: cto.get(0).getCache()) {
                //omite las propiedades que no son ni publicas ni privadas
                if( !Modifier.isPrivate(((Field)alTmp.get(0)).getModifiers()) && !Modifier.isPublic(((Field)alTmp.get(0)).getModifiers())){
                     continue;
                }

                if( Modifier.isPublic(((Field)alTmp.get(0)).getModifiers()) ){
                    try{
                        al2.add( ((Field)alTmp.get(0)).get(cto.get(j)) );
                    }catch(Exception ex){
                        throw new RuntimeException("No se logro recuperar el valor de '"+((Field)alTmp.get(0)).getName()+"': "+ex);
                    }
                }
                //es una entidad
                else if( SimpleEntity.isEntity(((Field)alTmp.get(0)).getType()) ){
                    try{
                        //data[i]=parse( (SimpleEntity)((Method)alTmp.get(1)).invoke(to) );
                         al2.add("[Entity]");
                    }catch(Exception ex){
                        throw new RuntimeException("No se logro recuperar el valor de la entidad '"+((Method)alTmp.get(1)).getName()+"': "+ex);
                    }
                }
                //es privado
                else{
                    try{
                         al2.add( ((Method)alTmp.get(1)).invoke(cto.get(j)) );
                    }catch(Exception ex){
                        throw new RuntimeException("No se logro recuperar el valor de '"+((Method)alTmp.get(1)).getName()+"': "+ex);
                    }
                }
            }

            al.add(al2);
        }

        String[][] array=new String[al.size()][];
        String[] c;
        for(int i=0; i<al.size(); i++){
            al2=al.get(i);
            c=new String[al2.size()];

            for(int j=0; j<al2.size(); j++){
                c[j]=""+al2.get(j);
            }

            array[i]=c;
        }

        al=null;
        al2=null;
        c=null;

        return array;
    }
}
