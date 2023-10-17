package neoatlantis.entity.utils.converters;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Properties;
import neoatlantis.entity.ContainerEntities;
import neoatlantis.entity.SimpleEntity;
import neoatlantis.entity.utils.UtilsEntities;

/**
 *
 * @author Hiryu (aslhiryu@gmail.com)
 */
public class Container2Html {
    public static String parse(ContainerEntities cto) {
        return parse(cto, null, null);
    }

    public static String parse(ContainerEntities cto, Properties nombres) {
        return parse(cto, nombres, null);
    }

    public static String parse(ContainerEntities cto, Properties nombres, Properties visibles) {
        StringBuffer sb = new StringBuffer("");

        //si el objeto no existe
        if (cto == null || cto.size() == 0) {
            return null;
        }

        sb.append("<table border='0' class='NA_Tabla_Contenedor'>").append(System.getProperty("line.separator"));


        //expongo los nombres
        sb.append("  <tr>").append(System.getProperty("line.separator"));
        for(List alTmp: cto.get(0).getCache()) {
            //omite las propiedades que no son ni publicas ni privadas
            if( (!Modifier.isPrivate(((Field)alTmp.get(0)).getModifiers()) && !Modifier.isPublic(((Field)alTmp.get(0)).getModifiers()))
                    || !UtilsEntities.esVisible((Field)alTmp.get(0), visibles)){
                 continue;
            }

            sb.append("    <td class='titulo'>").append( UtilsEntities.obtieneNombre((Field)alTmp.get(0), nombres) ).append("</td>").append(System.getProperty("line.separator"));
        }
        sb.append("  </tr>").append(System.getProperty("line.separator"));


        //expongo los valores
        for(int j=0; j<cto.size(); j++){
            sb.append("  <tr class='renglon").append(j%2==0?"Par": "Non").append("'>>").append(System.getProperty("line.separator"));

            for(List alTmp: cto.get(0).getCache()) {
                //omite las propiedades que no son ni publicas ni privadas
                if( (!Modifier.isPrivate(((Field)alTmp.get(0)).getModifiers()) && !Modifier.isPublic(((Field)alTmp.get(0)).getModifiers()))
                        || !UtilsEntities.esVisible((Field)alTmp.get(0), visibles)){
                     continue;
                }

                if( Modifier.isPublic(((Field)alTmp.get(0)).getModifiers()) ){
                    try{
                        sb.append("    <td>").append( ((Field)alTmp.get(0)).get(cto.get(j)) ).append("</td>").append(System.getProperty("line.separator"));
                    }catch(Exception ex){
                        throw new RuntimeException("No se logro recuperar el valor de '"+((Field)alTmp.get(0)).getName()+"': "+ex);
                    }
                }
                //es una entidad
                else if( SimpleEntity.isEntity(((Field)alTmp.get(0)).getType()) ){
                    try{
                        //data[i]=parse( (SimpleEntity)((Method)alTmp.get(1)).invoke(to) );
                         sb.append("    <td>").append( "[Entity]").append("</td>").append(System.getProperty("line.separator"));
                    }catch(Exception ex){
                        throw new RuntimeException("No se logro recuperar el valor de la entidad '"+((Method)alTmp.get(1)).getName()+"': "+ex);
                    }
                }
                //es privado
                else{
                    try{
                         sb.append("    <td>").append( ((Method)alTmp.get(1)).invoke(cto.get(j)) ).append("</td>").append(System.getProperty("line.separator"));
                    }catch(Exception ex){
                        throw new RuntimeException("No se logro recuperar el valor de '"+((Method)alTmp.get(1)).getName()+"': "+ex);
                    }
                }
            }

            sb.append("  </tr>").append(System.getProperty("line.separator"));
        }

        sb.append("</table>").append(System.getProperty("line.separator"));

        return sb.toString();
    }
}
