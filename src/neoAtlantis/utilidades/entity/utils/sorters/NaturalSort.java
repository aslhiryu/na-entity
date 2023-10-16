package neoAtlantis.utilidades.entity.utils.sorters;

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
public class NaturalSort {
    private enum TipoDato{CADENA, NUMERICO};

    public static void ordenar(ContainerEntities cto, String propiedad) {
        ordenar(cto, propiedad, TipoOrdenacion.ASCENDENTE);
    }

    public static void ordenar(ContainerEntities cto, String propiedad, TipoOrdenacion forma) {
        //reviso que la propiedad sea valida
        int pos=0;
        for(List alTmp: cto.get(0).getCache()) {
            if( !Modifier.isPrivate(((Field)alTmp.get(0)).getModifiers()) && !Modifier.isPublic(((Field)alTmp.get(0)).getModifiers())){
                continue;
            }
            if( ((Field)alTmp.get(0)).getName().equalsIgnoreCase(propiedad) ){
                ordenar(cto, pos);
            }
            pos++;
        }
        throw new ArrayIndexOutOfBoundsException("El atributo '"+propiedad+"' no existe.");
    }

    public static void ordenar(ContainerEntities cto, int propiedad) {
        ordenar(cto, propiedad, TipoOrdenacion.ASCENDENTE);
    }

    public static void ordenar(ContainerEntities cto, int propiedad, TipoOrdenacion forma) {
        SimpleEntity to1, to2;

        for (int i = 0; i < cto.size(); i++) {
            for (int j = 0; j < cto.size() - 1; j++) {
                to1 = cto.get(j);
                to2 = cto.get(j + 1);

                if (intercambio(to1, to2, propiedad, forma)) {
                    cto.switchEntities(j, j + 1);
                }
            }//fin del for con j

        }//fin del for con i
    }

    private static boolean intercambio(SimpleEntity to1, SimpleEntity to2, int index, TipoOrdenacion forma) {
        boolean inter = false;
        int con=0, pos=0;
        ArrayList prop=null;
        TipoDato tipo=TipoDato.CADENA;

        //accedo al elemento necesitado
        for(List alTmp: to1.getCache()) {
            pos++;
            if( !Modifier.isPrivate(((Field)alTmp.get(0)).getModifiers()) && !Modifier.isPublic(((Field)alTmp.get(0)).getModifiers())){
                continue;
            }
            con++;
            if( con==index ){
                prop=(ArrayList)to1.getCache().get(pos);
                break;
            }
        }

        if(prop==null){
            throw new ArrayIndexOutOfBoundsException("La propiedad '"+index+"' no se encuentra.");
        }

        //recupero el tipo de dato que contiene
        if( !((Field)prop.get(0)).getType().getName().equals("int") &&
                !((Field)prop.get(0)).getType().getName().equals("long") &&
                !((Field)prop.get(0)).getType().getName().equals("short") &&
                !((Field)prop.get(0)).getType().getName().equals("float") &&
                !((Field)prop.get(0)).getType().getName().equals("double") ){
            tipo=TipoDato.NUMERICO;
        }

        //intento el intercambio de valores
        if (tipo == TipoDato.NUMERICO) {//para valores numericos
            double d1, d2;

            if( Modifier.isPublic(((Field)prop.get(0)).getModifiers()) ){
                try{
                    d1 = Double.parseDouble(""+((Field)prop.get(0)).get(to1));
                    d2 = Double.parseDouble(""+((Field)prop.get(0)).get(to2));
                }catch(Exception ex){
                    throw new RuntimeException("No se logro recuperar el valor de '"+((Field)prop.get(0)).getName()+"': "+ex);
                }
            }
            //es privado
            else{
                try{
                    d1 = Double.parseDouble(""+((Method)prop.get(1)).invoke(to1));
                    d2 = Double.parseDouble(""+((Method)prop.get(1)).invoke(to2));
                }catch(Exception ex){
                    throw new RuntimeException("No se logro recuperar el valor de '"+((Method)prop.get(1)).getName()+"': "+ex);
                }
            }

            if (forma == TipoOrdenacion.ASCENDENTE) {
                if (d2 < d1) {
                    inter = true;
                }
            } else {
                if (d2 > d1) {
                    inter = true;
                }
            }
        }
        else {//para cadenas
            String s1, s2;

            if( Modifier.isPublic(((Field)prop.get(0)).getModifiers()) ){
                try{
                    s1=""+((Field)prop.get(0)).get(to1);
                    s2=""+((Field)prop.get(0)).get(to2);
                }catch(Exception ex){
                    throw new RuntimeException("No se logro recuperar el valor de '"+((Field)prop.get(0)).getName()+"': "+ex);
                }
            }
            //es privado
            else{
                try{
                    s1=""+((Method)prop.get(1)).invoke(to1);
                    s2=""+((Method)prop.get(1)).invoke(to2);
                }catch(Exception ex){
                    throw new RuntimeException("No se logro recuperar el valor de '"+((Method)prop.get(1)).getName()+"': "+ex);
                }
            }

            if (forma == TipoOrdenacion.ASCENDENTE) {
                if (s2.compareTo(s1) < 0) {
                    inter = true;
                }
            } else {
                if (s2.compareTo(s1) > 0) {
                    inter = true;
                }
            }
        }

        return inter;
    }
}
