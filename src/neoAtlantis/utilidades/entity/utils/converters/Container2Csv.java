package neoAtlantis.utilidades.entity.utils.converters;

import neoAtlantis.utilidades.entity.utils.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Properties;
import neoAtlantis.utilidades.entity.ContainerEntities;
import neoAtlantis.utilidades.entity.SimpleEntity;

/**
 *
 * @author Hiryu (asl_hiryu@yahoo.com)
 */
public class Container2Csv {

    public static String parse(ContainerEntities cto) {
        return parse(cto, null, null);
    }

    public static String parse(ContainerEntities cto, Properties nombres) {
        return parse(cto, nombres, null);
    }

    public static String parse(ContainerEntities cto, Properties nombres, Properties visibles) {
        return parse(cto, nombres, visibles, null, null, null);
    }

    public static String parse(ContainerEntities cto, Properties nombres, Properties visibles, String delCol, String delRen, String delCad) {
        StringBuffer sb = new StringBuffer("");
        int i=0;

        //revisa delimitadores
        if (delCol == null || delCol.length() == 0) {
            delCol = ",";
        }
        if (delCad == null || delCad.length() == 0) {
            delCad = "\"";
        }
        if (delRen == null || delRen.length() == 0) {
            delRen = System.getProperty("line.separator");
        }

        //si el objeto no existe
        if (cto == null || cto.size() == 0) {
            return null;
        }

        //expongo los nombres
        for(List alTmp: cto.get(0).getCache()) {
            //omite las propiedades que no son ni publicas ni privadas
            if( (!Modifier.isPrivate(((Field)alTmp.get(0)).getModifiers()) && !Modifier.isPublic(((Field)alTmp.get(0)).getModifiers()))
                    || !UtilsEntities.esVisible((Field)alTmp.get(0), visibles)){
                 continue;
            }

            if(i>0){
                sb.append(delCol);
            }
            sb.append(delCad).append( UtilsEntities.obtieneNombre((Field)alTmp.get(0), nombres) ).append(delCad);
            i++;
        }
        sb.append(delRen);

        //expongo los valores
        for(int j=0; j<cto.size(); j++){
            i=0;
            for(List alTmp: cto.get(0).getCache()) {
                //omite las propiedades que no son ni publicas ni privadas
                if( (!Modifier.isPrivate(((Field)alTmp.get(0)).getModifiers()) && !Modifier.isPublic(((Field)alTmp.get(0)).getModifiers()))
                        || !UtilsEntities.esVisible((Field)alTmp.get(0), visibles)){
                     continue;
                }

                if(i>0){
                    sb.append(delCol);
                }

                if( Modifier.isPublic(((Field)alTmp.get(0)).getModifiers()) ){
                    try{
                        sb.append(delCad).append( ((Field)alTmp.get(0)).get(cto.get(j)) ).append(delCad);
                    }catch(Exception ex){
                        throw new RuntimeException("No se logro recuperar el valor de '"+((Field)alTmp.get(0)).getName()+"': "+ex);
                    }
                }
                //es una entidad
                else if( SimpleEntity.isEntity(((Field)alTmp.get(0)).getType()) ){
                    try{
                        //data[i]=parse( (SimpleEntity)((Method)alTmp.get(1)).invoke(to) );
                         sb.append(delCad).append( "[Entity]").append(delCad);
                    }catch(Exception ex){
                        throw new RuntimeException("No se logro recuperar el valor de la entidad '"+((Method)alTmp.get(1)).getName()+"': "+ex);
                    }
                }
                //es privado
                else{
                    try{
                         sb.append(delCad).append( ((Method)alTmp.get(1)).invoke(cto.get(j)) ).append(delCad);
                    }catch(Exception ex){
                        throw new RuntimeException("No se logro recuperar el valor de '"+((Method)alTmp.get(1)).getName()+"': "+ex);
                    }
                }

                i++;
            }
            sb.append(delRen);
        }

        return sb.toString();
    }
}
