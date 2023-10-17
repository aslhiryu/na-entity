package neoatlantis.entity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import neoatlantis.entity.exceptions.ConfigurationDAOException;
import neoatlantis.entity.objects.ParameterAwarePreparedStatement;
import neoatlantis.utils.dataBase.ConfigurationDB;
import org.apache.log4j.Logger;

/**
 * Conceptualizaci&oacute;n de un Data Access Object, con la que se busca
 * estandarizar el manejo de DAO's
 *
 * @author Hiryu (aslhiryu@gmail.com)
 * @param <E> Tipo de Entidad
 */
public abstract class SimpleDAO<E extends  SimpleEntity> {

    private static final Logger DEBUGGER = Logger.getLogger(SimpleDAO.class);

    private int timeout=60;
    private int sizePage=10;
    private String user;
    private String pass;
    private String driver;
    private String jndi;
    private String url;

    public SimpleDAO(String jndi) {
        DEBUGGER.debug("Genera DAO con JNDI: " + jndi);
        this.jndi = jndi;
    }

    public SimpleDAO(String driver, String url, String user, String pass) {
        DEBUGGER.debug("Genera DAO con URL: " + url);
        this.driver = driver;
        this.url = url;
        this.user = user;
        this.pass = pass;
    }

    public SimpleDAO(Properties config) {
        try {
            ConfigurationDB.validateConfiguration(config);

            if (config.getProperty("jndi") != null && config.getProperty("jndi").length() > 0) {
                this.jndi = config.getProperty("jndi");
            } else {
                this.driver = config.getProperty("driver");
                this.url = config.getProperty("url");
                this.user = config.getProperty("user");
                this.pass = config.getProperty("pass");
            }
        } catch (Exception ex) {
            this.jndi = null;
            this.driver = null;
            this.url = null;
            this.user = null;
            this.pass = null;
            throw new ConfigurationDAOException(ex);
        }
    }

    
    
    
    public int getSizePage(){
        return this.sizePage;
    }
    
    public void setSizePage(int size){
        this.sizePage=size;
    }
    
    public int getTimeout(){
        return this.timeout;
    }
    
    public void setTimeout(int time){
        this.timeout=time;
    }
    
    
    
//  m etodos implementados  -   ---------------------------------------------------------------------------------------------------------------
    /**
     * Recupera una conexi�n a la BD
     *
     * @return Conexi�n abierta
     */
    protected Connection getConnection() {
        try {
            if (this.jndi != null && this.jndi.length() > 0) {
                DEBUGGER.info("Intenta generar conexión por jndi.");

                return ((DataSource) (new InitialContext()).lookup(this.jndi)).getConnection();
            } else {
                DEBUGGER.info("Intenta generar conexión por jdbc.");

                Properties properties = new Properties();
                properties.put("connectTimeout", (this.timeout * 1000));
                properties.put("user", this.user);
                properties.put("password", this.pass);
                Class.forName(this.driver);
                return DriverManager.getConnection(this.url, properties);
            }
        } catch (Exception ex) {
            DEBUGGER.error("Error al generar la conexión.", ex);

            throw new ConfigurationDAOException(ex);
        }
    }

    /**
     * Recupero todos los elementos a partir del filtro sin paginaci�n
     *
     * @param filtro Elemento que define la clausulas de busqueda
     * @return Coleccion de objetos cargados a partir del metodo
     * 'ParseMultipleEntities'
     * @throws java.sql.SQLException
     */
    public List<E> select(E filtro) throws SQLException {
        return this.select(filtro, 0, 0);
    }

    /**
     * Recupero todos los elementos a partir del filtro y con el tama�o de
     * pagina por default
     *
     * @param filtro Elemento que define la clausulas de busqueda
     * @param page P�gina que se desa obtener, 0 para recuperar todos los
     * registros
     * @return Coleccion de objetos cargados a partir del metodo
     * 'ParseMultipleEntities'
     * @throws java.sql.SQLException
     */
    public List<E> select(E filtro, int page) throws SQLException {
        return this.select(filtro, page, this.sizePage);
    }

    /**
     * Recupero todos los elementos a partir del filtro con paginaci�n
     *
     * @param filtro Elemento que define la clausulas de busqueda
     * @param page P�gina que se desa obtener, 0 para recuperar todos los
     * registros
     * @param pageSize Tama�o de la p�gina a obtener
     * @return Coleccion de objetos cargados a partir del metodo
     * 'ParseMultipleEntities'
     * @throws SQLException
     */
    public List<E> select(E filtro, int page, int pageSize) throws SQLException {
        ParameterAwarePreparedStatement query = null;
        ResultSet res = null;
        List<E> lTmp = new ArrayList();
        int pos = 0;
        String queryText = null;
        Connection con = null;

        try {
            queryText = this.getSelectQuery(filtro);
            con = this.getConnection();
            query = new ParameterAwarePreparedStatement(con.prepareStatement(queryText));
            this.asignSelectParameters(query, filtro);

            DEBUGGER.debug("Query para SELECT: " + queryText);
            this.printParameters(query);
            res = query.executeQuery();

            DEBUGGER.debug("Recupera " + (page <= 0 ? "todos los " : "") + "registros" + (page > 0 ? " de " + ((page - 1) * pageSize) + " a " + (page * pageSize) : ""));
            //genero los objetos
            while (res.next()) {
                pos++;
                //si solicitan pagina omito los registros anteriores a esta
                if (page > 0 && pos <= ((page - 1) * pageSize)) {
                    DEBUGGER.debug("Omito " + pos);
                    continue;
                }

                lTmp.add(this.parseMultipleEntities(res));
                DEBUGGER.debug("Considero registro " + pos);

                //si solicitan pagina omito los registros posteriores a esta
                if (page > 0 && pos >= (page * pageSize)) {
                    DEBUGGER.debug("Finalizo " + pos);
                    break;
                }
            }
            DEBUGGER.debug("Registros recuperados: " + lTmp.size());
        } catch (Exception ex) {
            DEBUGGER.error("No se logro recuperar los elementos: " + queryText, ex);
            throw new SQLException(ex);
        } finally {
            try {
                if (res != null) {
                    res.close();
                }
            } catch (Exception ex) {
            }
            try {
                if (query != null) {
                    query.close();
                }
            } catch (Exception ex) {
            }
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception ex) {
            }
        }

        return lTmp;
    }

    /**
     * Recupera la cantidad de registros de una consulta
     *
     * @param filtro Elemento que define la clausulas de busqueda
     * @return Numero de registros
     * @exception SQLException
     */
    public int getRowsCount(E filtro) throws SQLException {
        ParameterAwarePreparedStatement query = null;
        ResultSet res = null;
        int regs = 0;
        String queryText = null;
        Connection con = null;

        try {
            queryText = this.getContQuery(filtro);
            con = this.getConnection();
            query = new ParameterAwarePreparedStatement(con.prepareStatement(queryText));
            this.asignCountParameters(query, filtro);

            DEBUGGER.debug("Query para COUNT: " + queryText);
            this.printParameters(query);

            res = query.executeQuery();
            if (res.next()) {
                regs = res.getInt(1);
            }
            DEBUGGER.debug("Registros contados: " + regs);
        } catch (Exception ex) {
            DEBUGGER.error("No se logro recuperar los elementos: " + queryText, ex);
            throw new SQLException(ex);
        } finally {
            try {
                if (res != null) {
                    res.close();
                }
            } catch (Exception ex) {
            }
            try {
                if (query != null) {
                    query.close();
                }
            } catch (Exception ex) {
            }
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception ex) {
            }
        }

        return regs;
    }

    /**
     * Recupera un elemento por su id
     *
     * @param id Id del elemento a recuperar
     * @return Objeto cargado a partir del metodo 'ParseEntity'
     * @throws java.sql.SQLException
     */
    public E selectById(int id) throws SQLException {
        return this.selectById("" + id);
    }

    /**
     * Recupera un elemento por su id
     *
     * @param id Id del elemento a recuperar
     * @return Objeto cargado a partir del metodo 'ParseEntity'
     * @exception SQLException
     */
    public E selectById(String id) throws SQLException {
        ParameterAwarePreparedStatement query = null;
        ResultSet res = null;
        String queryText = null;
        Connection con = null;
        E to = null;

        try {
            queryText = this.getSelectByIdQuery();
            con = this.getConnection();
            query = new ParameterAwarePreparedStatement(con.prepareStatement(queryText));
            this.asignSelectByIdParameters(query, id);

            DEBUGGER.debug("Query para SELECT BY ID: " + queryText);
            DEBUGGER.debug("Con id: " + id);
            res = query.executeQuery();

            //valido que se tengan datos
            if (res.next()) {
                to = this.parseEntity(res);
            }
            DEBUGGER.debug("Elemento recuperado '" + to);
        } catch (Exception ex) {
            DEBUGGER.error("No se logro recuperar el elemento por id (" + id + "): " + queryText, ex);
            throw new SQLException(ex);
        } finally {
            try {
                if (res != null) {
                    res.close();
                }
            } catch (Exception ex) {
            }
            try {
                if (query != null) {
                    query.close();
                }
            } catch (Exception ex) {
            }
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception ex) {
            }
        }

        return to;
    }

    /**
     * Agrega un elemento
     *
     * @param to Elemento a agregar
     * @return true, si se logro agregar el elemento
     * @throws java.sql.SQLException
     */
    public boolean insert(E to) throws SQLException {
        ParameterAwarePreparedStatement query = null;
        String queryText = null;
        Connection con = null;
        boolean exito = false;
        int res;

        try {
            DEBUGGER.debug("Elemento a agregar: " + to);

            queryText = this.getInsertQuery(to);
            con = this.getConnection();
            query = new ParameterAwarePreparedStatement(con.prepareStatement(queryText));
            this.asignInsertParameters(query, to);

            DEBUGGER.debug("Query para INSERT: " + queryText);
            this.printParameters(query);

            res = query.executeUpdate();
            if (res > 0) {
                exito = true;
            }
            DEBUGGER.debug("Resultado: " + res);
        } catch (Exception ex) {
            DEBUGGER.error("No se logro agregar el elemento: " + queryText, ex);
            throw new SQLException(ex);
        } finally {
            try {
                if (query != null) {
                    query.close();
                }
            } catch (Exception ex) {
            }
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception ex) {
            }
        }

        return exito;
    }

    /**
     * Agrega un elemento en una tabla con un tipo autonumerico
     *
     * @param to Elemento a agregar
     * @return Valor autonumerico asignado en la operaci�n
     * @throws java.sql.SQLException
     */
    public int insertWithIdentity(E to) throws SQLException {
        ParameterAwarePreparedStatement query = null;
        String queryText = null;
        Connection con = null;
        ResultSet res;
        int id=0;

        try {
            DEBUGGER.debug("Elemento a agregar: " + to);

            queryText = this.getInsertWithAutonumericQuery(to);
            con = this.getConnection();
            query = new ParameterAwarePreparedStatement(con.prepareStatement(queryText));
            this.asignInsertParameters(query, to);

            DEBUGGER.debug("Query para INSERT: " + queryText);
            this.printParameters(query);

            res = query.executeQuery();
            if( res.next() ){
                id=res.getInt(1);
            }
            
            DEBUGGER.debug("Resultado: " + id);
        } catch (Exception ex) {
            DEBUGGER.error("No se logro agregar el elemento: " + queryText, ex);
            throw new SQLException(ex);
        } finally {
            try {
                if (query != null) {
                    query.close();
                }
            } catch (Exception ex) {
            }
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception ex) {
            }
        }

        return id;
    }

    /**
     * Actualiza un elemento
     *
     * @param to Elemento a actualizar
     * @return true, si se logro actualizar el elemento
     * @throws java.sql.SQLException
     */
    public boolean update(E to) throws SQLException {
        ParameterAwarePreparedStatement query = null;
        String queryText = null;
        Connection con = null;
        boolean exito = false;
        int res;

        try {
            DEBUGGER.debug("Elemento a actualizar: " + to);
            queryText = this.getUpdateQuery(to);
            con = this.getConnection();
            query = new ParameterAwarePreparedStatement(con.prepareStatement(queryText));
            this.asignUpdateParameters(query, to);

            DEBUGGER.debug("Query para UPDATE: " + queryText);
            this.printParameters(query);

            res = query.executeUpdate();
            if (res > 0) {
                exito = true;
            }
            DEBUGGER.debug("Resultado: " + res);
        } catch (Exception ex) {
            DEBUGGER.error("No se logro actualizar el elemento: " + queryText, ex);
            throw new SQLException(ex);
        } finally {
            try {
                if (query != null) {
                    query.close();
                }
            } catch (Exception ex) {
            }
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception ex) {
            }
        }

        return exito;
    }

    /**
     * Elimina un elemento
     *
     * @param to Elemento a eliminar
     * @return true, si se logro eliminar el elemento
     * @throws java.sql.SQLException
     */
    public boolean delete(E to) throws SQLException {
        ParameterAwarePreparedStatement query = null;
        String queryText = null;
        Connection con = null;
        boolean exito = false;
        int res;

        try {
            DEBUGGER.debug("Elemento a eliminar: " + to);
            queryText = this.getDeleteQuery(to);
            con = this.getConnection();
            query = new ParameterAwarePreparedStatement(con.prepareStatement(queryText));
            this.asignDeleteParameters(query, to);

            DEBUGGER.debug("Query para DELETE: " + queryText);
            this.printParameters(query);

            res = query.executeUpdate();
            if (res > 0) {
                exito = true;
            }
            DEBUGGER.debug("Resultado: " + res);
        } catch (Exception ex) {
            DEBUGGER.error("No se logro eliminar el elemento: " + queryText, ex);
            throw new SQLException(ex);
        } finally {
            try {
                if (query != null) {
                    query.close();
                }
            } catch (Exception ex) {
            }
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception ex) {
            }
        }

        return exito;
    }

    public boolean modifyStatus(int id, boolean enable) throws SQLException {
        return this.modifyStatus("" + id, enable);
    }

    /**
     * Habilita/inhabilita un elemento
     *
     * @param id Id del elemento
     * @param enable Nuevo estado
     * @return true, si se logro modificar el estado del elemento
     * @throws SQLException
     */
    public boolean modifyStatus(String id, boolean enable) throws SQLException {
        ParameterAwarePreparedStatement query = null;
        String queryText = null;
        Connection con = null;
        boolean exito = false;
        int res;

        try {
            queryText = this.getModifyStatusQuery();
            con = this.getConnection();
            query = new ParameterAwarePreparedStatement(con.prepareStatement(queryText));
            this.asignModifyStatusParameters(query, id, enable);

            DEBUGGER.debug("Query para MODIFY STATUS: " + queryText);
            this.printParameters(query);

            res = query.executeUpdate();
            if (res > 0) {
                exito = true;
            }
            DEBUGGER.debug("Resultado: " + res);
        } catch (Exception ex) {
            DEBUGGER.error("No se logro el cambio del estatus del elemento: " + queryText, ex);
            throw new SQLException(ex);
        } finally {
            try {
                if (query != null) {
                    query.close();
                }
            } catch (Exception ex) {
            }
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception ex) {
            }
        }

        return exito;
    }

    // metodos estaticos  ---------------------------------------------------------------------------------------------------------------------------
    public int calculatePageNumber(int registros) {
        return calculatePageNumber(registros, this.sizePage);
    }

    public static int calculatePageNumber(int registros, int tamano) {
        int paginas = 0;

        if (registros == 0) {
            paginas = 0;
        } else if (registros % tamano == 0) {
            paginas = registros / tamano;
        } else {
            return registros / tamano + 1;
        }

        return paginas;

    }
    
    private void printParameters(ParameterAwarePreparedStatement ps) throws SQLException {
        ParameterMetaData rs=ps.getParameterMetaData();
        
        DEBUGGER.debug("Con "+rs.getParameterCount()+" valores: ");
        for(int i=1; i<=rs.getParameterCount(); i++){
            DEBUGGER.debug(i+": ["+rs.getParameterClassName(i)+"] => "+ps.getParameters().get(i));
        }
    }
    

    // definicion de metodos ----------------------------------------------------------------------------------------------------------------------
    public E parseEntity(ResultSet res) {
        throw new UnsupportedOperationException("No tiene implementaci�n del metodo 'ParseEntity', en el Objeto: " + this.getClass());
    }

    public E parseMultipleEntities(ResultSet res) {
        return parseEntity(res);
    }

    public String getSelectByIdQuery() {
        return null;
    }

    public void asignSelectByIdParameters(PreparedStatement query, String id) throws SQLException {
        query.setString(1, id);
    }

    public String getSelectQuery(E filtro) {
        return null;
    }

    public void asignSelectParameters(PreparedStatement query, E filtro) throws SQLException {
        throw new UnsupportedOperationException("No tiene implementaci�n del metodo 'AsignSelectParameters', en el Objeto: " + this.getClass());
    }

    public String getInsertQuery(E objeto) {
        return null;
    }

    public String getInsertWithAutonumericQuery(E objeto) {
        return this.getInsertQuery(objeto) + "; SELECT CAST(SCOPE_IDENTITY() AS INT)";
    }

    public void asignInsertParameters(PreparedStatement query, E objeto) throws SQLException {
        throw new UnsupportedOperationException("No tiene implementaci�n del metodo 'AsignInsertParameters', en el Objeto: " + this.getClass());
    }

    public  String getUpdateQuery(E objeto) {
        return null;
    }

    public void asignUpdateParameters(PreparedStatement query, E objeto) throws SQLException {
        throw new UnsupportedOperationException("No tiene implementaci�n del metodo 'AsignUpdateParameters', en el Objeto: " + this.getClass());
    }

    public String getDeleteQuery(E objeto) {
        return null;
    }

    public void asignDeleteParameters(PreparedStatement query, E objeto) throws SQLException {
        throw new UnsupportedOperationException("No tiene implementaci�n del metodo 'AsignDeleteParameters', en el Objeto: " + this.getClass());
    }

    public String getModifyStatusQuery() {
        return null;
    }

    public void asignModifyStatusParameters(PreparedStatement query, String id, boolean enable) throws SQLException {
        query.setString(1, id);
        query.setBoolean(2, enable);
    }

    public String getContQuery(E filtro) {
        int posOrd = this.getSelectQuery(filtro).toLowerCase().indexOf(" order by ");
        int posFro = this.getSelectQuery(filtro).toLowerCase().indexOf(" from ");

        //calculo el tama�o
        int tam = this.getSelectQuery(filtro).length() - posFro - (posOrd > 0 ? (this.getSelectQuery(filtro).length() - posOrd) : 0);

        return "SELECT " + this.getCountSubquery() + this.getSelectQuery(filtro).substring(posFro, tam);
    }

    public void asignCountParameters(PreparedStatement query, E filtro) throws SQLException {
        this.asignSelectParameters(query, filtro);
    }

    public String getCountSubquery() {
        return " COUNT(*) ";
    }
}
