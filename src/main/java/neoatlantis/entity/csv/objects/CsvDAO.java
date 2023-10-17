package neoatlantis.entity.csv.objects;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import neoatlantis.entity.SimpleDAO;

/**
 * Objeto que define el funcionamiento de un DAO para importar datos de un CVS a una base de datos
 * @author Hiryu (aslhiryu@gmail.com)
 */
public class CsvDAO extends SimpleDAO<CellDefinition> {
    private DataModel model;
    private String[] data;
    private SimpleDateFormat sdf;
    
    public CsvDAO(Properties config, DataModel model) {
        super(config);
        this.model=model;
        this.sdf=new SimpleDateFormat(model.getDateFormat());
    }
    
    
    @Override
    public CellDefinition parseEntity(ResultSet rs){
        return new CellDefinition("demo", "", true);
    }
    
    @Override
    public String getSelectByIdQuery(){
        return "SELECT * FROM "+this.model.getTable()+" WHERE "+this.model.getKey()+"=?";
    }
    
    @Override
    public String getUpdateQuery(CellDefinition d){
        StringBuilder sb=new StringBuilder("");
        boolean coma=false;
        
        sb.append("UPDATE ").append(this.model.getTable())
                .append(" SET ");
        
        for(CellDefinition c: this.model.getCells()){
            //valida que no sea la llave
            if( c.getColumnDB().equalsIgnoreCase(this.model.getKey()) ){
                continue;
            }
            
            if( c.getColumnPosition()>=0 ){
                if(coma){
                    sb.append(", ");
                }
                
                sb.append(c.getColumnDB()).append("=?");
                coma=true;
            }
        }
        sb.append(" WHERE ").append(this.model.getKey()).append("=?");
        
        return sb.toString();
    }
    
    @Override
    public void asignUpdateParameters(PreparedStatement ps, CellDefinition d) throws SQLException{
        int count=1;
        Date dat;
        
        for(CellDefinition c: this.model.getCells()){
            //valida que no sea la llave
            if( c.getColumnDB().equalsIgnoreCase(this.model.getKey()) ){
                continue;
            }

            if( c.getColumnPosition()>=0 ){
                //valida si no es una fecha
                if( c.isDate() && this.data[c.getColumnPosition()]!=null && !this.data[c.getColumnPosition()].isEmpty() ){
                    try{
                        dat=this.sdf.parse(this.data[c.getColumnPosition()]);
                    }
                    catch(Exception ex){
                        throw new SQLException("La fecha no coincide con el formato asignado '"+this.model.getDateFormat()+"'", ex);
                    }
                    ps.setTimestamp(count, new Timestamp(dat.getTime()));
                }
                else{
                    //aplica el trim
                    if( this.data[c.getColumnPosition()]!=null ){
                        ps.setString(count, this.data[c.getColumnPosition()].trim());
                    }
                    else{
                        ps.setString(count, null);
                    }
                }
                count++;
            }
        }
        
        try{
            ps.setString(count, this.data[this.model.getColumnKey()]);
        }
        catch(Exception ex){
            throw new SQLException("No se logro definir el valor para la llave", ex);
        }
    }
    
    public boolean update(String[] data) throws SQLException{
        this.data=data;
        return this.update(new CellDefinition("", "", true));
    }

    
    @Override
    public String getInsertQuery(CellDefinition d){
        StringBuilder sb=new StringBuilder("");
        boolean coma=false;
        int count=0;
        
        sb.append("INSERT INTO ").append(this.model.getTable())
                .append(" (");
        
        for(CellDefinition c: this.model.getCells()){
            if( c.getColumnPosition()>=0 ){
                if(coma){
                    sb.append(", ");
                }
                
                sb.append(c.getColumnDB());
                coma=true;
                count++;
            }
        }
        
        sb.append(") VALUES (");
        for(int i=0; i<count; i++){
            if( i>0 ){
                sb.append(", ");
            }
            sb.append('?');
        }
        sb.append(')');
        
        return sb.toString();
    }
    
    @Override
    public void asignInsertParameters(PreparedStatement ps, CellDefinition d) throws SQLException{
        int count=1;
        Date dat;
        
        for(CellDefinition c: this.model.getCells()){
            if( c.getColumnPosition()>=0 ){
                //valida si no es una fecha
                if( c.isDate() && this.data[c.getColumnPosition()]!=null && !this.data[c.getColumnPosition()].isEmpty() ){
                    try{
                        dat=this.sdf.parse(this.data[c.getColumnPosition()]);
                    }
                    catch(Exception ex){
                        throw new SQLException("La fecha no coincide con el formato asignado '"+this.model.getDateFormat()+"'", ex);
                    }
                    ps.setTimestamp(count, new Timestamp(dat.getTime()));
                }
                else{
                    //aplica el trim
                    if( this.data[c.getColumnPosition()]!=null ){
                        ps.setString(count, this.data[c.getColumnPosition()].trim());
                    }
                    else{
                        ps.setString(count, null);
                    }
                }
                count++;
            }
        }
    }
    
    public boolean insert(String[] data) throws SQLException{
        this.data=data;
        return this.insert(new CellDefinition("", "", true));
    }
}
