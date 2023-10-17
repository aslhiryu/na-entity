package neoatlantis.entity.utils.operations;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import neoatlantis.entity.ContainerEntities;

/**
 *
 * @author Hiryu (aslhiryu@gmail.com)
 */
public class PercentContainer {
    public static List<Double> calcular(ContainerEntities cto, int propiedad) {
        ArrayList<Double> al = new ArrayList<Double>();
        int con=0, pos=0;
        ArrayList prop=null;

        //reviso que tenga contenido
        if (cto.size() == 0) {
            return al;
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

        double tot=PlusContainer.calcular(cto, propiedad);
        if( tot==-1 ){
            return al;
        }

        for(int j=0; j<cto.size(); j++){
            if( Modifier.isPublic(((Field)prop.get(0)).getModifiers()) ){
                try{
                    al.add(new Double( Double.parseDouble(""+((Field)prop.get(0)).get(cto.get(j)))/tot ));
                }catch(Exception ex){
                    throw new RuntimeException("No se logro recuperar el valor de '"+((Field)prop.get(0)).getName()+"': "+ex);
                }
            }
            //es privado
            else{
                try{
                    al.add(new Double( Double.parseDouble(""+((Method)prop.get(1)).invoke(cto.get(j)))/tot ));
                }catch(Exception ex){
                    throw new RuntimeException("No se logro recuperar el valor de '"+((Method)prop.get(1)).getName()+"': "+ex);
                }
            }
        }

        return al;
    }

    public static List<Double> calcular(ContainerEntities cto, String propiedad) {
        //reviso que tenga contenido
        if (cto.size() == 0) {
            return new ArrayList<Double>();
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
}
