package neoatlantis.entity.utils.converters;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import neoatlantis.entity.SimpleEntity;

/**
 *
 * @author Hiryu (aslhiryu@gmail.com)
 */
public class Entity2Array {
    public static String[] parse(SimpleEntity to) {
        ArrayList<String> alTmp2=new ArrayList<String>();

        for(List alTmp:to.getCache()) {

            if( !Modifier.isPrivate(((Field)alTmp.get(0)).getModifiers()) && !Modifier.isPublic(((Field)alTmp.get(0)).getModifiers())){
                 continue;
            }

            if( Modifier.isPublic(((Field)alTmp.get(0)).getModifiers()) ){
                try{
                    alTmp2.add(""+((Field)alTmp.get(0)).get(to));
                }catch(Exception ex){
                    throw new RuntimeException("No se logro recuperar el valor de '"+((Field)alTmp.get(0)).getName()+"': "+ex);
                }
            }
            //es una entidad
            else if( SimpleEntity.isEntity(((Field)alTmp.get(0)).getType()) ){
                try{
                    //data[i]=parse( (SimpleEntity)((Method)alTmp.get(1)).invoke(to) );
                    alTmp2.add("[Entity]");
                }catch(Exception ex){
                    throw new RuntimeException("No se logro recuperar el valor de la entidad '"+((Method)alTmp.get(1)).getName()+"': "+ex);
                }
            }
            //es privado
            else{
                try{
                    alTmp2.add(""+((Method)alTmp.get(1)).invoke(to));
                }catch(Exception ex){
                    throw new RuntimeException("No se logro recuperar el valor de '"+((Method)alTmp.get(1)).getName()+"': "+ex);
                }
            }
        }

        String data[]=new String[alTmp2.size()];

        for(int i=0; i<alTmp2.size(); i++){
            data[i]=alTmp2.get(i);
        }
        alTmp2=null;

        return data;
    }

}
