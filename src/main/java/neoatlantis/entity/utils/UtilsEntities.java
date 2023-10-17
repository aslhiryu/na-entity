package neoatlantis.entity.utils;

import java.lang.reflect.Field;
import java.util.Properties;

/**
 *
 * @author Hiryu (asl_hiryu@yahoo.com)
 */
public class UtilsEntities {
    public static String obtieneNombre(Field f, Properties n){
        if( n==null || n.getProperty(f.getName())==null){
            return f.getName();
        }

        return n.getProperty(f.getName());
    }

    public static boolean esVisible(Field f, Properties n){
        if( n==null ){
            return true;
        }

        if( n.getProperty(f.getName())!=null &&
                (n.getProperty(f.getName()).equalsIgnoreCase("true") ||
                n.getProperty(f.getName()).equalsIgnoreCase("yes") ||
                n.getProperty(f.getName()).equalsIgnoreCase("si") ||
                n.getProperty(f.getName()).equalsIgnoreCase("t") ||
                n.getProperty(f.getName()).equalsIgnoreCase("y") ||
                n.getProperty(f.getName()).equalsIgnoreCase("s") ||
                n.getProperty(f.getName()).equalsIgnoreCase("1") )){
            return true;
        }

        return false;
    }
}
