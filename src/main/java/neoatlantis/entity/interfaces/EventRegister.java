package neoatlantis.entity.interfaces;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import neoatlantis.entity.Event;

/**
 * Interface que define el comportamiento que debe de tener un <i>Registrador de eventos</i>.
 * @version 1.0
 * @author Hiryu (aslhiryu@gmail.com)
 */
public abstract class EventRegister {
    /**
     * Variable que almacena el valor para un fin de linea
     */
    protected String finLinea = System.getProperty("line.separator");
    /**
     * Variable que define el formateador de fechas
     */
    protected SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
    /**
     * Variable que define la posici&oacute;n de los registros
     */
    protected long posicion=0;


    /**
     * Definicion del metodo para extraer los registros.
     * @param param Filtros que se desean aplicar para obtener los registros
     * @param regs N&uacute;mero de registro que se desean obtener
     * @param offset Registro desde donde se desea obtener la informaci&oacute;n
     * @return Registros obtenidos
     * @throws java.lang.Exception
     */
    public abstract List<Event> getEvents(Map<String,Object> param, int regs, int offset) throws Exception;

    /**
     * Modifica el formato de la fechas quese utiliza. El formato por default es del tipo '25/12/08 23:56:20'.
     * @param cad Nueva mascara para el formateo de fechas
     */
    public void setDateFormat(String cad) {
        this.sdf = new SimpleDateFormat(cad);
    }

    /**
     * Obtiene la posici&oacute;n actual sobre el reporte de registros.
     * @return Posici&oacue;n actual
     */
    public long getActualPosition(){
        return this.posicion;
    }
}
