package neoatlantis.entity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import neoatlantis.entity.annotations.IdEntity;
import neoatlantis.entity.objects.ParamSQL;
import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

/**
 * Conceptualizaci&oacute;n de una entidad, con la que se busca estandarizar el manejo de entidades
 * en los sistemas.
 * @author Hiryu (aslhiryu@gmail.com)
 */
public class SimpleEntity  implements Serializable {
    private static final Logger DEBUGGER = Logger.getLogger(SimpleEntity.class);

    /**
     * Cache que almacena toda la informac�&oacute;n de la clase.
     */
    protected static HashMap<Class,List<List>> cache=new HashMap<Class,List<List>>();
    /**
     * Colecci&oacute;n que almacena la relaci&oacute;n de identificaores de la clase.
     */
    protected static HashMap<Class,List<Field>> ids=new HashMap<Class,List<Field>>();

    protected ParamSQL paramSQL;

    
    
    
    public SimpleEntity(){
        this(false);
    }

    /**
     * Constructor basico.
     * @param omitsIntrospection Define si se desea que se autoexplore la clase
     */
    public SimpleEntity(boolean omitsIntrospection) {
        this.paramSQL=new ParamSQL();
        
        if (!omitsIntrospection){
            DEBUGGER.debug("Valida si existe información para: "+this.getClass());
            if(SimpleEntity.cache.get(this.getClass())==null){
                DEBUGGER.debug("Carga la información de '"+this.getClass()+"' en cache.");
                SimpleEntity.cache.put(this.getClass(), new ArrayList<List>());
                SimpleEntity.ids.put(this.getClass(), new ArrayList<Field>());
                this.recuperaElementos();
            }
        }
        else{
            DEBUGGER.debug("Omite la carga de información ");
        }
    }

    public ParamSQL getParamSQL(){
        return this.paramSQL;
    }
    
    /**
     * Obtiene la informaci�n de la constituci&oacute; propia de la clase.
     * @return
     */
    public List<List> getCache(){
        return SimpleEntity.cache.get(this.getClass());
    }

    /**
     * Genera la informaci�n propia de la clase y la almacena en el cache.
     */
    private void recuperaElementos() {
        ArrayList alTmp;

        Field[] fs = this.getClass().getDeclaredFields();
        Method[] mts = this.getClass().getDeclaredMethods();
        StringBuilder sb=new StringBuilder("");

        //recuperando las caracteristicas de los atributos
        for (Field f: fs) {
            if (f.getName().indexOf("$") == -1) {
                //recupero el nombre
                alTmp=new ArrayList();
                alTmp.add(f);
                DEBUGGER.debug("Recupero información del atributo: "+f.getName());

                if( Modifier.isFinal(f.getModifiers()) || Modifier.isStatic(f.getModifiers()) ){
                    DEBUGGER.debug("No se considera el atributo '"+f.getName()+"' por ser final o estatico.");
                    continue;
                }
                
                if( f.isAnnotationPresent(IdEntity.class) ){
                    SimpleEntity.ids.get(this.getClass()).add(f);
                    DEBUGGER.debug("Considero el atributo '"+f.getName()+"' como llave.");
                }
                
                //reviso si es una variable privada
                if( f.getModifiers()==Modifier.PRIVATE ){
                    DEBUGGER.debug("El atributo '"+f.getName()+"' es de ambito PRIVADO recupero sus metodos de encapsulamiento.");
                    //para geters
                    for (int j=0; mts!=null && j<mts.length; j++) {
                        if (mts[j].getName().equalsIgnoreCase("get" + f.getName())
                                || mts[j].getName().equalsIgnoreCase("is" + f.getName())
                                || mts[j].getName().equalsIgnoreCase("has" + f.getName())) {
                            //recuperando el metodo
                            alTmp.add(mts[j]);
                            break;
                        }
                    }
                    //para seters
                    for (int j=0; mts!=null && j<mts.length; j++) {
                        if (mts[j].getName().equalsIgnoreCase("set" + f.getName())) {
                            //recuperando el metodo
                            alTmp.add(mts[j]);
                            break;
                        }
                    }
                }
                else{
                    DEBUGGER.debug("El atributo '"+f.getName()+"' no es de ambito PRIVADO.");
                    alTmp.add(null);
                    alTmp.add(null);
                }

                //si no recupero las 3 caracteristicas, algo anda mal
                if( alTmp.size()!=3 ){
                    DEBUGGER.fatal("El POJO no cumple con el estandar de encapsulamiento.");
                    throw new RuntimeException("El POJO no cumple con el estandar de encapsulamiento.");
                }

                //agrego las caracteristica a la coleccion
                SimpleEntity.cache.get(this.getClass()).add(alTmp);
            }
        }
        
        DEBUGGER.debug("Almacena la siguiente información de '"+this.getClass()+"' en cache:");
        for(int i=0; SimpleEntity.cache.get(this.getClass())!=null&&i<SimpleEntity.cache.get(this.getClass()).size(); i++){
            sb.append("=> ").append(SimpleEntity.cache.get(this.getClass()).get(i).get(0));
            sb.append(", [").append(SimpleEntity.cache.get(this.getClass()).get(i).get(1)).append("]");
            sb.append(", [").append(SimpleEntity.cache.get(this.getClass()).get(i).get(2)).append("]");
            sb.append("\n");
        }
        DEBUGGER.debug(sb.toString());
    }

    /**
     * Recustituye una entidad serializada.
     * @param ois Flujo de donde se obtiene la entidad.
     * @throws java.lang.ClassNotFoundException
     * @throws java.io.IOException
     */
    protected void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
        ois.defaultReadObject();
    }

    /**
     * Serializa la  entidad.
     * @param oos Flujo en donde se serializa la entidad.
     * @throws java.io.IOException
     */
    protected void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
    }

    /**
     * Genera los caracteres necesarios para simmular un tabulador al momento de desplejar la informaci&oacute;n
     * de la entidad.
     * @param nivel Numero de nivel.
     * @return Cadana que simula los tabuladores dependiendo del nivel.
     */
    public static String generaPreNivel(int nivel){
        StringBuilder sb = new StringBuilder("");

        for(int i=0; i<nivel; i++){
            sb.append("    ");
        }

        return sb.toString();
    }

    /**
     * Genera una cadena que despliega la informaci&oacute;n de la entidad.
     * @param nivel Nivel para desplegar la informaci&oacute;n.
     * @return Cadena con la informaci&oacute;n de la entidad.
     */
    public String obtieneInfo(int nivel) {
        StringBuilder sb = new StringBuilder("");        

        //valida el nivel, para evitar  un ciclo sin fin
        if( nivel >10 ){
            DEBUGGER.debug("Finaliza por ir muy profundo");
            
            return sb.toString();
        }
        
        //escribo el monbre del objeto
        sb.append(generaPreNivel(nivel));
        sb.append("=================  (").append(this.getClass()).append(")  =================").append(System.getProperty("line.separator"));

        //escribo los atributos
        for (List alTmp: SimpleEntity.cache.get(this.getClass())) {
            //System.out.println("Carga informaci�n para: "+alTmp.get(0));
            
            if( Modifier.isPublic(((Field)alTmp.get(0)).getModifiers()) ){
                sb.append(generaPreNivel(nivel));
                sb.append("+ ");
            }
            else if( Modifier.isPrivate(((Field)alTmp.get(0)).getModifiers()) ){
                sb.append(generaPreNivel(nivel));
                sb.append("- ");
            }
            else{
                continue;
            }

            //revisa si es llave
            for (Field id: SimpleEntity.ids.get(this.getClass())) {
                if( ((Field)alTmp.get(0)).equals(id) ){
                    sb.append("*");
                }
            }
            //escribe el nombre del campo
            sb.append( ((Field)alTmp.get(0)).getName() );
            sb.append(": ");

            if( Modifier.isPublic(((Field)alTmp.get(0)).getModifiers()) ){
                try{
                    sb.append( ((Field)alTmp.get(0)).get(this) );
                    sb.append(System.getProperty("line.separator"));
                }catch(Exception ex){
                    DEBUGGER.error("Error al recuperar el atributo público: "+((Field)alTmp.get(0)).getName(), ex);
                    throw new RuntimeException("No se logro recuperar el valor de '"+((Field)alTmp.get(0)).getName()+"': "+ex);
                }
            }
            //es una entidad
            else if( SimpleEntity.isEntity(((Field)alTmp.get(0)).getType()) ){
                DEBUGGER.debug("El atributo solicitado es de tipo entidad, prepara el metodo a llamar: "+alTmp.get(1));
                try{
                    SimpleEntity ent=(SimpleEntity)((Method)alTmp.get(1)).invoke(this);
                    if( ent!=null ){
                        sb.append(System.getProperty("line.separator")).append( ent.obtieneInfo(nivel+1) );
                    }
                    else{
                        sb.append("NULL").append(System.getProperty("line.separator"));
                    }
                }catch(Exception ex){
                    DEBUGGER.error("Error al recuperar la entidad: "+((Field)alTmp.get(0)).getName(), ex);
                    throw new RuntimeException("No se logro recuperar el valor de la entidad '"+((Field)alTmp.get(0)).getName()+"': "+ex);
                }
            }
            //es privado
            else{
                try{
                    sb.append( ((Method)alTmp.get(1)).invoke(this) );
                    sb.append(System.getProperty("line.separator"));
                }catch(Exception ex){
                    DEBUGGER.error("Error al recuperar el atributo privado: "+((Field)alTmp.get(0)).getName()+"["+((Method)alTmp.get(1)).getName()+"]", ex);
                    throw new RuntimeException("No se logro ejecutar el metodo GET de '"+((Field)alTmp.get(0)).getName()+"': "+ex);
                }
            }
        }
        
        //agrego SQLParam
        sb.append(this.getParamSQL().obtieneInfo(nivel));

        //cierro el espacio de escritura
        sb.append(generaPreNivel(nivel));
        sb.append("====================");
        for (int i = 0; i<(""+this.getClass()).length(); i++) {
            sb.append("=");
        }
        sb.append("====================").append(System.getProperty("line.separator"));

        return sb.toString();
    }

    @Override
    public String toString() {
        return obtieneInfo(0);
    }

    @Override
    public boolean equals(Object o) {
        if( o==null || !this.getClass().equals(o.getClass()) ) {
            return false;
        }

        String cTmp1,cTmp2 ;
        int con=0, ind=0;

        for (List alTmp: SimpleEntity.cache.get(this.getClass())) {
            if( Modifier.isPublic(((Field)alTmp.get(0)).getModifiers()) ){
                try{
                    cTmp1=""+((Field)alTmp.get(0)).get(this);
                    cTmp2=""+((Field)alTmp.get(0)).get(o);
                }
                catch(Exception ex){
                     throw new RuntimeException("No se logro recuperar el valor de '"+((Field)alTmp.get(0)).getName()+"': "+ex);
                }
            }
            //privado
            else if( Modifier.isPrivate(((Field)alTmp.get(0)).getModifiers()) ){
                try{
                    cTmp1=""+((Method)alTmp.get(1)).invoke(this);
                    cTmp2=""+((Method)alTmp.get(1)).invoke(o);
                }
                catch(Exception ex){
                    throw new RuntimeException("No se logro recuperar el valor de '"+((Method)alTmp.get(1)).getName()+"': "+ex);
                }
            }
            else{
                continue;
            }

            if (cTmp1 != null && cTmp1.equals(cTmp2)) {
                con++;
            }
            ind++;
        }

        if (ind == con) {
            return true;
        }

        return false;
    }

    @Override
    public SimpleEntity clone(){
        Object oTmp;
        SimpleEntity e;

        //genero la instancia de la clase
        try{
            e=this.getClass().newInstance();
        }catch(Exception ex){
             throw new RuntimeException("No se logro generar la instancia de la clase': "+ex);
        }

        //recorro los elementos
        for (List alTmp: SimpleEntity.cache.get(this.getClass())) {

            if( Modifier.isPublic(((Field)alTmp.get(0)).getModifiers()) &&  !Modifier.isFinal(((Field)alTmp.get(0)).getModifiers()) ){
                try{
                    oTmp=((Field)alTmp.get(0)).get(this);
                    ((Field)alTmp.get(0)).set(e, oTmp);
                }
                catch(Exception ex){
                     throw new RuntimeException("No se logro asignar el valor de '"+((Field)alTmp.get(0)).getName()+"': "+ex);
                }
            }
            //privado
            else if( Modifier.isPrivate(((Field)alTmp.get(0)).getModifiers()) ){
                try{
                    oTmp=((Method)alTmp.get(1)).invoke(this);
                    ((Method)alTmp.get(2)).invoke(e, oTmp);
                }
                catch(Exception ex){
                    throw new RuntimeException("No se logro asignar el valor de '"+((Method)alTmp.get(2)).getName()+"': "+ex);
                }
            }
            else{
                continue;
            }
        }

        return e;
    }
    
    public void setPropertyStringValue(String property, String value){
        String[] props = property.split("\\.");
        Field[] fs = this.getClass().getDeclaredFields();
        Method[] ms;
        Object ent;
        boolean found=false;

        DEBUGGER.debug("Cantidad de propiedades: "+(fs!=null? fs.length: -1));
        for (Field f: fs){
            if (props != null && props.length > 1 && f.getName().equalsIgnoreCase(props[0])){
                DEBUGGER.debug("Se trata de un atributo de la entidad: " + f.getType());
                //valido que este instanciada la entidad
                ent=recoveryValueFromProperty(this, f);
                if( ent!=null){
                    DEBUGGER.debug("La entidad esta instanciada, se intenta asignar el valor");
                    this.setPropertyExternal(ent, property.substring(props[0].length()+1), value);
                }
                
                found=true;
                break;
            }
            else if (props!=null && props.length==1 && f.getName().equalsIgnoreCase(props[0])){
                this.assignValueToProperty(this, f, parseStringValue(value, f.getType()));
                
                found=true;
                break;
            }            
        }
        
        if( !found ){
            DEBUGGER.debug("No se logró asignar el valor por atributo, intento asignarlo por método.");
            ms=this.getClass().getDeclaredMethods();
            
            for(Method m: ms){
                if (props!=null && props.length==1 && m.getName().equalsIgnoreCase("set"+props[0])){
                    try{
                        m.invoke(this, parseStringValue(value, m.getParameterTypes()[0]));
                        break;
                    }
                    catch(Exception ex){
                        DEBUGGER.error("No se logro asignar el valor por método", ex);
                    }
                }
            }
        }
    }

    protected void setPropertyExternal(Object entidad, String propiedad, String valor){
        Field[] fs = entidad.getClass().getDeclaredFields();
        String[] props = propiedad.split("\\.");
        Object ent;

        for(Field f: fs){
            if (props != null && props.length > 1 && f.getName().equalsIgnoreCase(props[0])){
                DEBUGGER.debug("Se trata de un atributo de la entidad hija: " + f.getType());
                //valido que este instanciada la entidad
                ent=recoveryValueFromProperty(entidad, f);
                if( ent!=null){
                    DEBUGGER.debug("La entidad hija esta instanciada, se intenta asignar el valor");
                    this.setPropertyExternal(ent, propiedad.substring(props[0].length() + 1), valor);
                }
                break;
            }
            else if (props != null && props.length == 1 && f.getName().equalsIgnoreCase(props[0])){
                DEBUGGER.debug("Intenta Asignar el valor '" + valor + "' a la propiedad  '" + propiedad + "' de la entidad hija de tipo '" + f.getType() + "'");
                this.assignValueToProperty(entidad, f, parseStringValue(valor, f.getType()));
                break;
            }
        }
    }
    
    private Object recoveryValueFromProperty(Object obj, Field prop){
        Method[] ms;

        try{
            DEBUGGER.debug("Intenta recuperar el valor de la propiedad '"+prop+"' de tipo '"+prop.getType()+"'");
            try{
                return prop.get(obj);
            }
            catch(Exception ex1){
                DEBUGGER.debug("Falla al recuperar el valor por atributo, intenta recuperar el valor por metodo");
                    ms=obj.getClass().getDeclaredMethods();
                    for(Method m: ms){
                        if( m.getName().equalsIgnoreCase("get"+prop.getName()) ||
                                m.getName().equalsIgnoreCase("is"+prop.getName()) ){
                            DEBUGGER.debug("Localiza el metodo para el get: "+m.getName());
                            try{
                                return m.invoke(obj);
                            }
                            catch(Exception ex2){
                                DEBUGGER.error("No se logro recuperar el valor ni por atributo, ni por metodo", ex2);
                            }
                        }
                    }                
                return null;
            }
        }
        catch(Exception ex){
            DEBUGGER.error("Problema al recuperar el valor del atributo'"+prop+"'", ex);
            return null;
        }
    }
    
    private void assignValueToProperty(Object obj, Field prop, Object val){        
        Method[] ms;

        try{
            DEBUGGER.debug("Intenta Asignar el valor '"+val+"' a la propiedad '"+prop+"' de tipo '"+prop.getType()+"'");
            try{
                prop.set(obj, val);
            }
            catch(Exception ex1){
                    DEBUGGER.debug("Falla al asignar el valor por atributo, intenta asignar el valor por metodo");
                    ms=obj.getClass().getDeclaredMethods();
                    for(Method m: ms){
                        if( m.getName().equalsIgnoreCase("set"+prop.getName()) ){
                            DEBUGGER.debug("Localiza el metodo para el set: "+m.getName());
                            try{
                                m.invoke(obj, val);
                            }
                            catch(Exception ex2){
                                DEBUGGER.error("No se logro asignar el valor ni por atributo, ni por metodo", ex2);
                            }
                            break;
                        }
                    }
            }
        }
        catch(Exception ex){
            DEBUGGER.error("Problema al asignar el valor del atributo'"+prop+"'", ex);
        }
    }
    
    /**
     * Genera un documento xml que representa a la entidad.
     * @return Documento de JDom.
     */
    public Document toXml(){
        Element raiz=new Element("entity");
        Document doc=new Document(raiz);

        raiz.setAttribute("type", this.getClass().getName());

        this.defineEstructuraXml(raiz);

        return doc;
    }

    /**
     * Genera una estructura de XML que representa a la entidad.
     * @return Cadena con en XML.
     */
    public String toStringXml(){
        XMLOutputter xml = new XMLOutputter();
        ByteArrayOutputStream out=new ByteArrayOutputStream();

        xml.setFormat(Format.getPrettyFormat());
        try{
            xml.output(this.toXml(), out);
            out.flush();
            out.close();
        }
        catch(Exception ex){
            throw new RuntimeException(ex);
        }

        return out.toString();
    }

    /**
     * Recupera los atributos que funcionan como id's de la entidad.
     * @return Arreglo con el nombre de los atributos.
     */
    public String[] getIdName(){
        String[] n=new String[SimpleEntity.ids.get(this.getClass()).size()];

        for(int i=0; i<SimpleEntity.ids.get(this.getClass()).size(); i++){
            n[i]=SimpleEntity.ids.get(this.getClass()).get(i).getName();
        }

        return n;
    }

    /**
     * Comprueba su un objeto es una entidad.
     * @param obj Objeto que se desea comprobar.
     * @return true si es una entidad.
     */
    public static boolean isEntity(Object obj){
        return isEntity(obj.getClass());
    }

    /**
     * Comprueba su un objeto es una entidad.
     * @param obj Objeto que se desea comprobar.
     * @return true si es una entidad.
     */
    public static boolean isEntity(Class obj){
        if( obj==null || obj.getSuperclass()==Object.class ){
            return false;
        }
        else if( obj.getSuperclass()==SimpleEntity.class ){
            return true;
        }
        else{
            return isEntity( obj.getSuperclass() );
        }
    }

    //-------------------------------------------------------------- metodos protegidos  ----------------------------------------------------------------------------------

    protected Object parseStringValue(String value, Class tipo){
        Object res = null;

        //valida si es un null para asigna a un tipo de objeto
        if( (value==null || value.trim().isEmpty()) &&
                (tipo == Character.class || tipo == Boolean.class || tipo == Integer.class || 
                tipo == Long.class || tipo == Float.class || tipo == Double.class ||
                tipo == Date.class) ){
            res=null;
        }
        //si es cadena
        else if (tipo==String.class){
            DEBUGGER.debug("Es un String");
            try{
                res = value;
            }
            catch(Exception ex) { }
        }
        //si es caracter
        else if (tipo == Character.class || tipo==char.class){
            DEBUGGER.debug("Es un Char");
            try{
                res = value.charAt(0);
            }
            catch(Exception ex) { }
        }
        //si es boleano
        else if (tipo == Boolean.class || tipo==boolean.class){
            DEBUGGER.debug("Es un Boolean");
            try{
                res = Boolean.valueOf(value);
            }
            catch(Exception ex) { }
        }
        //si es entero
        else if (tipo == Integer.class || tipo==int.class){
            DEBUGGER.debug("Es un Int");
            try{
                res = Integer.parseInt(value);
            }
            catch(Exception ex){}
        }
        else if (tipo == Long.class || tipo==long.class){
            DEBUGGER.debug("Es un Long");
            try{
                res = Long.parseLong(value);
            }
            catch(Exception ex) { }
        }
        else if (tipo == Float.class || tipo==float.class){
            DEBUGGER.debug("Es un Float");
            try{
                res = Float.parseFloat(value);
            }
            catch(Exception ex) { }
        }
        //si es doble
        else if (tipo == Double.class || tipo==double.class){
            DEBUGGER.debug("Es un Double");
            try{
                res = Double.parseDouble(value);
            }
            catch(Exception ex) { }
        }
        //si es fecha
        else if (tipo == Date.class){
            DEBUGGER.debug("Es un Date");
            SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
            try{
                res = sdf.parse(value);
            }
            catch(Exception ex) { }
        }
        else
        {
            DEBUGGER.debug("Tipo no controlado: " + tipo);
        }
        DEBUGGER.debug("Valor recuperado "+res);

        return res;
    }
    
    /**
     * Genera la estructura de la entidad en un xml.
     * @param raiz Nodo a partir del cual se genera la estructura.
     */
    protected void defineEstructuraXml(Element raiz){
        Element nodo;

        //recorro los elementos
        for (List alTmp: SimpleEntity.cache.get(this.getClass())) {
            if( !Modifier.isPublic(((Field)alTmp.get(0)).getModifiers())&& !Modifier.isPrivate(((Field)alTmp.get(0)).getModifiers()) ){
                continue;
            }

            nodo=new Element("atributte");
            nodo.setAttribute("name", ((Field)alTmp.get(0)).getName());
            nodo.setAttribute("type", ((Field)alTmp.get(0)).getType().getName());
            if( Modifier.isPublic(((Field)alTmp.get(0)).getModifiers()) ){
                try{
                    nodo.setText( ""+((Field)alTmp.get(0)).get(this) );
                    nodo.setAttribute("scope", "public");
                }catch(Exception ex){
                    throw new RuntimeException("No se logró recuperar el valor de '"+((Field)alTmp.get(0)).getName()+"': "+ex);
                }
            }
            //es una entidad
            else if( SimpleEntity.isEntity(((Field)alTmp.get(0)).getType()) ){
                try{
                    ((SimpleEntity)((Method)alTmp.get(1)).invoke(this)).defineEstructuraXml(nodo);
                    nodo.setName("entity");
                }catch(Exception ex){
                    throw new RuntimeException("No se logró recuperar el valor de la entidad '"+((Method)alTmp.get(1)).getName()+"': "+ex);
                }
                nodo.setAttribute("scope", "private");
            }
            //es privado
            else{
                try{
                    nodo.setText( ""+((Method)alTmp.get(1)).invoke(this) );
                    nodo.setAttribute("scope", "private");
                    //System.out.println("+++ "+nodo+", "+alTmp.get(1));
                }catch(Exception ex){
                    throw new RuntimeException("No se logró recuperar el valor de '"+((Method)alTmp.get(1)).getName()+"': "+ex);
                }
            }

            //revisa si es llave
            for (Field id: SimpleEntity.ids.get(this.getClass())) {
                if( ((Field)alTmp.get(0)).equals(id) ){
                    nodo.setAttribute("id", "true");
                }
            }

            raiz.addContent(nodo);
        }
    }
}
