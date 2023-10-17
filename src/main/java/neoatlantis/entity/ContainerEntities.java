package neoatlantis.entity;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Conceptualizaci&oacute;n de un contenedor de entidades, con la que se busca
 * estandarizar el manejo de colecciones de entidades en los sistemas.
 * @author Hiryu (aslhiryu@gmail.com)
 */
public class ContainerEntities<E extends SimpleEntity> implements List<E>, Serializable {
    /**
     * Versi&oacute;n del Contenedor
     */
    public static final String version = "1.1";

    /**
     * Pagina en la que se encuentra posicionado el apuntador de paginas.
     */
    protected int paginaActual = 1;
    /**
     * Elementos que son considerados por una pagina.
     */
    protected int elementosPorPagina = 5;
    /**
     * Variable que controla la capacidad de duplicados.
     */
    protected boolean duplicados = false;
    /**
     * Arreglo donde almacena las entidades el contenedor.
     */
    protected ArrayList<E> data = new ArrayList<E>();

    /**
     * Constructor basico.
     */
    public ContainerEntities(){
    }

    /**
     * Comstructor que permite el manejo de duplicados en el contenedor.
     * @param duplicados true si se desean duplicados.
     */
    public ContainerEntities(boolean duplicados) {
        this.duplicados = duplicados;
    }

    /**
     * Obtiene el tama�o del contenedor.
     * @return Tama�o del contenedor.
     */
    public int size() {
        return this.data.size();
    }

    /**
     * Valida si esta vacio el contenedor.
     * @return true si esta vacio.
     */
    public boolean isEmpty() {
        return this.data.isEmpty();
    }

    /**
     * Valida si existe en el contenedor una determinada entidad.
     * @param e Entidad que se desea validar.
     * @return true si existe la entidad.
     */
    public boolean contains(Object e) {
        if( !(e instanceof SimpleEntity) ){
            return false;
        }

        //recorro todos los elementos
        for (E ent: this.data) {
            //System.out.println("compara: "+ent+" con "+e);
            //reviso si es el mismo elemento
            if (ent.equals(e)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Obtiene el iterator del contenedor.
     * @return Iterador del contenedor
     */
    public Iterator<E> iterator() {
        return this.data.iterator();
    }

    /**
     * Obtiene un arreglo con las entidades del contenedor.
     * @return Arreglo de entidades.
     */
    public Object[] toArray() {
        Object[] a=new Object[this.data.size()];

        for(int i=0; i<this.data.size(); i++){
            a[i]=this.data.get(i);
        }

        return a;
    }

    /**
     * Coloca las entidades en un arrglo dado.
     * @param <T> Tipo de componente del que se desea el arreglo.
     * @param a Arreglo en donde se desea cargar las entidades.
     * @return
     */
    public <T> T[] toArray(T[] a) {
        if (a.length != this.data.size()) {
            throw new RuntimeException("Tamaños incompatibles.");
        }

        for(int i=0; i<this.data.size(); i++){
            a[i]=(T)this.data.get(i);
        }

        return a;
    }

    /**
     * Agrega una entidad al contenedor.
     * @param e Entidad a agregar.
     * @return true si se logro agregar.
     */
    public boolean add(E e) {
        //System.out.println("D:"+this.duplicados+", "+this.contains(e));
        //valida si el objeto ya existe y si es valido agregarlo
        if (this.data.contains(e) && !this.duplicados) {
            return false;
        }

        this.data.add(e);
        return true;
    }

    /**
     * Elimina una entidad del contenedor.
     * @param arg0 Entidad a eliminar.
     * @return true si se logro eliminar.
     */
    public boolean remove(Object arg0) {
        return this.data.remove(arg0);
    }

    public boolean containsAll(Collection<?> arg0) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Agrega un contenedor al contenedor.
     * @param container Contenedor a agregar
     * @return true si se logro agregar el contenedor.
     */
    public boolean addAll(Collection<? extends E> container) {
        //valido que el contenedor sea valido
        if (container == null || !this.getClass().equals(container.getClass())) {
            return false;
        }

        //agrego los datos del contenedor
        //for (int i = 0; i < container.size(); i++) {
        for(E e: container){
            this.data.add(e);
        }

        return true;
    }

    public boolean addAll(int arg0, Collection<? extends E> arg1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean removeAll(Collection<?> arg0) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean retainAll(Collection<?> arg0) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Reinicia el contenedor.
     */
    public void clear() {
        this.data.clear();
    }

    /**
     * Obtienene una entidad
     * @param Posici&oacute;n de la entidad deseada.
     * @return Entidad obtenida.
     */
    public E get(int arg0) {
        return this.data.get(arg0);
    }

    public E getById(String id){
        //valido que tenga valores
        if( this.data.isEmpty() ){
            return null;
        }

        //recuperos los ids
        List ids=SimpleEntity.ids.get(this.data.get(0).getClass());
        List<List> mts=SimpleEntity.cache.get(this.data.get(0).getClass());
        Method m;

        if( ids==null || ids.isEmpty() ){
            throw new RuntimeException("El objeto '"+this.data.get(0).getClass()+"' no tiene definido alguna llave.");
        }
        else if(ids.size() == 1){
            for(int i=0; i<mts.size(); i++){
                //valido que sea el mismo campo
                if( mts.get(i).get(0).equals(ids.get(0)) ){
                    m=(Method)mts.get(i).get(1);

                    //recorro todos los objetos de la collecion
                    for(int j=0; j<this.data.size(); j++){
                        try{
                            if( m.invoke(this.data.get(j)).equals(id) ){
                                return this.data.get(j);
                            }
                        }catch(Exception ex){
                             throw new RuntimeException("Error al recuperar el valor por Id.");
                        }
                    }
                }
            }
        }

        throw new RuntimeException("No se puede recuperar el valor por Id.");
    }

    /**
     * Asigna una entidad en una posici&oacute;n deseada.
     * @param arg0 Posici&oacute;n  en donde se desea asignar la entidad.
     * @param arg1 Entidad a asignar
     * @return Entidad original que estaba en la posici&oacute;n.
     */
    public E set(int arg0, E arg1) {
        return this.data.set(arg0, arg1);
    }

    /**
     * Agrega una entidad en un posici&oacute;n especifica.
     * @param arg0 Posici&oacute;n en donde se desea agregar la entidad.
     * @param arg1 Entidad a agregar.
     */
    public void add(int arg0, E arg1) {
        //valida si el objeto ya existe y si es valido agregarlo
        if (this.contains(arg1) & !this.duplicados) {
            return;
        }

        this.data.add(arg0, arg1);
    }

    /**
     * Remueve una entidad de una posici&oacute;n dada.
     * @param arg0 Posic�&oacute;n de donde se desea remover la entidad.
     * @return Entidad removida.
     */
    public E remove(int arg0) {
        return this.data.remove(arg0);
    }

    /**
     * Obtiene la posici&oacute;n de la primera ocurrencia de un objeto.
     * @param arg0 Objeto que se busca en el Contenedor.
     * @return Posici&oacute;n en donde se encuentra el objeto o -1 si no se encuentra.
     */
    public int indexOf(Object arg0) {
        return this.data.indexOf(arg0);
    }

    /**
     * Obtiene la posici&oacute;n de la ultima ocurrencia de un objeto.
     * @param arg0 Objeto que se busca en el Contenedor.
     * @return Posici&oacute;n en donde se encuentra el objeto o -1 si no se encuentra.
     */
    public int lastIndexOf(Object arg0) {
        return this.data.lastIndexOf(arg0);
    }

    /**
     * Obtiene el listIterator del contenedor.
     * @return listIterator del contenedor.
     */
    public ListIterator<E> listIterator() {
        return this.data.listIterator();
    }

    /**
     * Obtiene el listIterator del contenedor.
     * @param arg0
     * @return listIterator del contenedor.
     */
    public ListIterator<E> listIterator(int arg0) {
        return this.data.listIterator(arg0);
    }

    /**
     * Obtiene un sub-contenedor a partir de una posici&oacute;n en especifico.
     * @param arg0 Posisi&oacute;n inicial de donde se tomar&aacute; el sub-contenedor.
     * @param arg1 numero de elementos que se desean en el sub-contenedor.
     * @return Contenedor con las entidades solicitadas.
     */
    public List<E> subList(int arg0, int arg1) {
        return this.data.subList(arg0, arg1);
    }

    /**
     * Intercanbia 2 entidades de posici&oacute;n.
     * @param data1 Posici&oacute;n de la primer entidad.
     * @param data2 Posici&oacute;n de la entidad por la que se desea intercanbiar.
     */
    public void switchEntities(int data1, int data2) {
        E obj1 = this.data.get(data1);
        E obj2 = this.data.get(data2);

        this.set(data1, obj2);
        this.set(data2, obj1);

        obj1 = null;
        obj2 = null;
    }

    /**
     * Revisa si permite duplicados en el contenedor.
     * @return true si los permite.
     */
    public boolean isDuplicate() {
         return this.duplicados;
     }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer("");
        Iterator<E> iter = this.iterator();

        //Concatena cada uno de las cadenas de los TO
        sb.append(">>>>>>>>>>>>>>>>>>>>>  ").append(this.getClass()).append("[").append(this.size()).append("]  <<<<<<<<<<<<<<<<<<<<<");
        sb.append(System.getProperty("line.separator"));
        while (iter.hasNext()) {
            sb.append(iter.next().obtieneInfo(0));
        }
        sb.append(">>>>>>>>>>>>>>>>>>>>>>>");
        for (int i = 0; i < ("" + this.getClass()).length(); i++) {
            if (i <= ("" + this.getClass()).length() / 2) {
                sb.append(">");
            } else {
                sb.append("<");
            }
        }
        sb.append("<<<<<<<<<<<<<<<<<<<<<<<");
        sb.append(System.getProperty("line.separator"));
        sb.append(System.getProperty("line.separator"));

        return sb.toString();
    }

    /**
     * Obtiene el numero de entidades que se tienen por pagina.
     * @return Numero de entidades.
     */
    public int getElementsByPage() {
        return this.elementosPorPagina;
    }

    /**
     * Asigna el numero de entidades que se manejaran por pagina.
     * @param numero Numero de entidades.
     */
    public void setElementsByPage(int numero) {
        this.elementosPorPagina = numero;
    }

    /**
     * Obtiene el numero de paginas por las que esta compuesto el actual contenedor.
     * @return Numero de paginas.
     */
    public int getPages() {
        int num = 0;

        //Si no hay datos decimos que estamos en la pagina 1
        if (this.size() == 0) {
            return 1;
        }

        //Calculamos las paginas mediante el tama?o del contenedor y y numero de objetos por pagina
        num = this.size() / this.elementosPorPagina;
        //Si la division no es exacta aumentamos una pagina mas
        if (this.size() % this.elementosPorPagina > 0) {
            num++;
        }

        return num;
    }

    /**
     * Obtiene una pagina del contenedor.
     * @param pagina Pagina que se desea obtener.
     * @return Contenedor con las entidades de la pagina.
     */
    public ContainerEntities<E> getPage(int pagina) {
        ContainerEntities con = new ContainerEntities();

        for (int i = (pagina - 1) * this.elementosPorPagina; i < pagina * this.elementosPorPagina && i < this.size(); i++) {
            con.add(this.get(i));
        }
        con.setElementsByPage(this.elementosPorPagina);

        return con;
    }

    /**
     * Obtiene la pagina actual del contenedor.
     * @return Contenedor con las entidades de la pagina.
     */
    public ContainerEntities<E> getPage() {
        return this.getPage(this.paginaActual);
    }

    /**
     * Avanza el apuntador de paginas a la siguiente posici&oacute;n.
     * @return true si se logro la operaci&oacute;n.
     */
    public boolean nextPage() {
        if (paginaActual < this.getPages()) {
            paginaActual++;
            return true;
        }

        return false;
    }

    /**
     * Avanza el apuntador de paginas a la anterior posici&oacute;n.
     * @return true si se logro la operaci&oacute;n.
     */
    public boolean previousPage() {
        if (paginaActual > 1) {
            paginaActual--;
            return true;
        }

        return false;
    }

    /**
     * Localiza el apuntador de paginas a la primera.
     */
    public void reward() {
        this.paginaActual = 1;
    }

    /**
     * Recustituye un contenedor serializada.
     * @param ois Flujo de donde se obtiene el contenedor.
     * @throws java.lang.ClassNotFoundException
     * @throws java.io.IOException
     */
    protected void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
        ois.defaultReadObject();
    }

    /**
     * Serializa en contenedor.
     * @param oos Flujo en donde se serializa el contenedor.
     * @throws java.io.IOException
     */
    protected void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
    }
}
