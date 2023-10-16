package neoAtlantis.utilidades.entity.utils;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import neoAtlantis.utilidades.entity.objects.ParamSQL;

/**
 * Clase que permite excluir ciertos attributos no necesarios del objeto SimpleEntity  de la serializacion de JSON
 * @author Hiryu (asl_hiryu@yahoo.com)
 */
public class SimpleEntityGsonExclusion implements ExclusionStrategy{

    @Override
    public boolean shouldSkipField(FieldAttributes fa) {
        return (
                fa.getDeclaredClass()==ParamSQL.class
                );
    }

    @Override
    public boolean shouldSkipClass(Class<?> type) {
        return false;
    }
    
}
