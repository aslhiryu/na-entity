package neoatlantis.entity.utils.converters;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import neoatlantis.entity.SimpleEntity;

/**
 *
 * @author Hiryu (aslhiryu@gmail.com)
 */
public class Entity2Html {
    public static enum TipoHtml{TABLE, LIST};

    public static String parse(SimpleEntity entidad, TipoHtml tipo) {
        StringBuffer sb=new StringBuffer("");

        if( tipo==TipoHtml.TABLE ){
            sb.append("<table border='0' class='NA_Tabla_Entidad'>").append(System.getProperty("line.separator"));
            sb.append("  <tr>").append(System.getProperty("line.separator"));
            sb.append("    <td class='titulo'>Propiedad</td>").append(System.getProperty("line.separator"));
            sb.append("    <td class='titulo'>Valor</td>").append(System.getProperty("line.separator"));
            sb.append("  </tr>").append(System.getProperty("line.separator"));

            int i=0;
            for(List alTmp: entidad.getCache()) {
                if( !Modifier.isPrivate(((Field)alTmp.get(0)).getModifiers()) && !Modifier.isPublic(((Field)alTmp.get(0)).getModifiers())){
                     continue;
                }

                sb.append("  <tr class='renglon").append(i%2==0?"Par": "Non").append("'>").append(System.getProperty("line.separator"));
                sb.append("    <td class='propiedad'>").append( ((Field)alTmp.get(0)).getName() ).append("</td>").append(System.getProperty("line.separator"));
                sb.append("    <td class='valor'>");

                if( Modifier.isPublic(((Field)alTmp.get(0)).getModifiers()) ){
                    try{
                        sb.append( ((Field)alTmp.get(0)).get(entidad) );
                    }catch(Exception ex){
                        throw new RuntimeException("No se logro recuperar el valor de '"+((Field)alTmp.get(0)).getName()+"': "+ex);
                    }
                }
                //es una entidad
                else if( SimpleEntity.isEntity(((Field)alTmp.get(0)).getType()) ){
                    try{
                        //data[i]=parse( (SimpleEntity)((Method)alTmp.get(1)).invoke(to) );
                         sb.append( "[Entity]");
                    }catch(Exception ex){
                        throw new RuntimeException("No se logro recuperar el valor de la entidad '"+((Method)alTmp.get(1)).getName()+"': "+ex);
                    }
                }
                //es privado
                else{
                    try{
                         sb.append( ((Method)alTmp.get(1)).invoke(entidad) );
                    }catch(Exception ex){
                        throw new RuntimeException("No se logro recuperar el valor de '"+((Method)alTmp.get(1)).getName()+"': "+ex);
                    }
                }

                sb.append("</td>").append(System.getProperty("line.separator"));
                sb.append("  </tr>").append(System.getProperty("line.separator"));
                i++;
            }
            sb.append("</table>").append(System.getProperty("line.separator"));
        }
        else{
            sb.append("<ul class='NA_Lista_Entidad'>").append(System.getProperty("line.separator"));

            for(List alTmp: entidad.getCache()) {
                //omite las propiedades que no son ni publicas ni privadas
                if( !Modifier.isPrivate(((Field)alTmp.get(0)).getModifiers()) && !Modifier.isPublic(((Field)alTmp.get(0)).getModifiers())){
                     continue;
                }

                sb.append("  <li><span class='titulo'>").append( ((Field)alTmp.get(0)).getName() ).append(":</span> ");

                if( Modifier.isPublic(((Field)alTmp.get(0)).getModifiers()) ){
                    try{
                        sb.append( ((Field)alTmp.get(0)).get(entidad) );
                    }catch(Exception ex){
                        throw new RuntimeException("No se logro recuperar el valor de '"+((Field)alTmp.get(0)).getName()+"': "+ex);
                    }
                }
                //es una entidad
                else if( SimpleEntity.isEntity(((Field)alTmp.get(0)).getType()) ){
                    try{
                        //data[i]=parse( (SimpleEntity)((Method)alTmp.get(1)).invoke(to) );
                         sb.append( "[Entity]");
                    }catch(Exception ex){
                        throw new RuntimeException("No se logro recuperar el valor de la entidad '"+((Method)alTmp.get(1)).getName()+"': "+ex);
                    }
                }
                //es privado
                else{
                    try{
                         sb.append( ((Method)alTmp.get(1)).invoke(entidad) );
                    }catch(Exception ex){
                        throw new RuntimeException("No se logro recuperar el valor de '"+((Method)alTmp.get(1)).getName()+"': "+ex);
                    }
                }

                sb.append("</li>").append(System.getProperty("line.separator"));
            }

            sb.append("</ul>").append(System.getProperty("line.separator"));
        }

        return sb.toString();
    }

    public static String parse(SimpleEntity entidad) {
        return parse(entidad, TipoHtml.TABLE);
    }
}
