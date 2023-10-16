package neoAtlantis.utilidades.entity.utils.operations;

import neoAtlantis.utilidades.entity.ContainerEntities;

/**
 *
 * @author Hiryu (asl_hiryu@yahoo.com)
 */
public class AveragingContainer {
    public static double calcular(ContainerEntities cto, int propiedad) {
        //reviso que tenga contenido
        if (cto.size() == 0) {
            return 0;
        }

        //realizo la operacion
        return PlusContainer.calcular(cto, propiedad)/ cto.size();
    }

    public static double calcular(ContainerEntities cto, String propiedad) {
        //reviso que tenga contenido
        if (cto.size() == 0) {
            return 0;
        }

        //realizo la operacion
        return PlusContainer.calcular(cto, propiedad) / cto.size();
    }
}
