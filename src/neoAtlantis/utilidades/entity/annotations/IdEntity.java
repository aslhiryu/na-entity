package neoAtlantis.utilidades.entity.annotations;

import java.lang.annotation.*;

/**
 * Componente que sirve para declarar un identificador en una entidad.
 * @author Hiryu (asl_hiryu@yahoo.com)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface IdEntity {
    //public String id();
}
