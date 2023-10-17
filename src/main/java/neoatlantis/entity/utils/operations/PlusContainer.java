package neoatlantis.entity.utils.operations;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import neoatlantis.entity.ContainerEntities;

/**
 *
 * @author Hiryu (aslhiryu@gmail.com)
 */
public class PlusContainer {
    public static double calcular(ContainerEntities cto, int propiedad) {
        int con=0, pos=0;
        double suma = 0;
        ArrayList prop=null;

        //reviso que tenga contenido
        if (cto.size() == 0) {
            return 0;
        }

        for(List alTmp: cto.get(0).getCache()) {
            pos++;
            if( !Modifier.isPrivate(((Field)alTmp.get(0)).getModifiers()) && !Modifier.isPublic(((Field)alTmp.get(0)).getModifiers())){
                continue;
            }
            con++;
            if( con==propiedad ){
                prop=(ArrayList)cto.get(0).getCache().get(pos);
                break;
            }
        }

        if(prop==null){
            throw new ArrayIndexOutOfBoundsException("La propiedad '"+propiedad+"' no se encuentra.");
        }

//System.out.println( ((Field)prop.get(0)).getName() );
        if( !((Field)prop.get(0)).getType().getName().equals("int") &&
                !((Field)prop.get(0)).getType().getName().equals("long") &&
                !((Field)prop.get(0)).getType().getName().equals("short") &&
                !((Field)prop.get(0)).getType().getName().equals("float") &&
                !((Field)prop.get(0)).getType().getName().equals("double") ){
            return -1;
        }

        for(int j=0; j<cto.size(); j++){
            if( Modifier.isPublic(((Field)prop.get(0)).getModifiers()) ){
                try{
                    suma+=Double.parseDouble(""+((Field)prop.get(0)).get(cto.get(j)));
                }catch(Exception ex){
                    throw new RuntimeException("No se logro recuperar el valor de '"+((Field)prop.get(0)).getName()+"': "+ex);
                }
            }
            //es privado
            else{
                try{
                    suma+=Double.parseDouble(""+((Method)prop.get(1)).invoke(cto.get(j)));
                }catch(Exception ex){
                    throw new RuntimeException("No se logro recuperar el valor de '"+((Method)prop.get(1)).getName()+"': "+ex);
                }
            }
        }

        return suma;
    }

    public static double calcular(ContainerEntities cto, String propiedad) {
        //reviso que tenga contenido
        if (cto.size() == 0) {
            return 0;
        }

        //reviso que la propiedad sea valida
        int pos=0;
        for(List alTmp: cto.get(0).getCache()) {
            if( !Modifier.isPrivate(((Field)alTmp.get(0)).getModifiers()) && !Modifier.isPublic(((Field)alTmp.get(0)).getModifiers())){
                continue;
            }
            if( ((Field)alTmp.get(0)).getName().equalsIgnoreCase(propiedad) ){
                return calcular(cto, pos);
            }
            pos++;
        }
        throw new ArrayIndexOutOfBoundsException("El atributo '"+propiedad+"' no existe.");
    }

    public static Map<String,Double> calcular(ContainerEntities cto) {
        Hashtable<String,Double> valores = new Hashtable<String,Double>();

        //reviso que tenga contenido
        if (cto.size() == 0) {
            return valores;
        }

        for(int j=0; j<cto.size(); j++){
            for(List alTmp: cto.get(0).getCache()) {
                if( !Modifier.isPrivate(((Field)alTmp.get(0)).getModifiers()) && !Modifier.isPublic(((Field)alTmp.get(0)).getModifiers())){
                    continue;
                }
                if( !((Field)alTmp.get(0)).getType().getName().equals("int") &&
                        !((Field)alTmp.get(0)).getType().getName().equals("long") &&
                        !((Field)alTmp.get(0)).getType().getName().equals("short") &&
                        !((Field)alTmp.get(0)).getType().getName().equals("float") &&
                        !((Field)alTmp.get(0)).getType().getName().equals("double") ){
                    continue;
                }

                if( valores.get( ((Field)alTmp.get(0)).getName() )==null ){
                    if( Modifier.isPublic(((Field)alTmp.get(0)).getModifiers()) ){
                        try{
                            valores.put(((Field)alTmp.get(0)).getName(), new Double(""+((Field)alTmp.get(0)).get(cto.get(j))));
                        }catch(Exception ex){
                            throw new RuntimeException("No se logro recuperar el valor de '"+((Field)alTmp.get(0)).getName()+"': "+ex);
                        }
                    }
                    //es privado
                    else{
                        try{
                            valores.put(((Field)alTmp.get(0)).getName(), new Double(""+((Method)alTmp.get(1)).invoke(cto.get(j))));
                        }catch(Exception ex){
                            throw new RuntimeException("No se logro recuperar el valor de '"+((Method)alTmp.get(1)).getName()+"': "+ex);
                        }
                    }
                }
                else{
                    if( Modifier.isPublic(((Field)alTmp.get(0)).getModifiers()) ){
                        try{
                            valores.put(((Field)alTmp.get(0)).getName(), valores.get(((Field)alTmp.get(0)).getName())+Double.parseDouble(""+((Field)alTmp.get(0)).get(cto.get(j))));
                        }catch(Exception ex){
                            throw new RuntimeException("No se logro recuperar el valor de '"+((Field)alTmp.get(0)).getName()+"': "+ex);
                        }
                    }
                    //es privado
                    else{
                        try{
                            valores.put(((Field)alTmp.get(0)).getName(), valores.get(((Field)alTmp.get(0)).getName())+Double.parseDouble(""+((Method)alTmp.get(1)).invoke(cto.get(j))));
                        }catch(Exception ex){
                            throw new RuntimeException("No se logro recuperar el valor de '"+((Method)alTmp.get(1)).getName()+"': "+ex);
                        }
                    }
                }
            }
        }

        return valores;
    }
}
