package neoatlantis.entity.csv.utils;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;
import neoatlantis.entity.csv.exceptions.CsvImportException;
import neoatlantis.entity.csv.objects.CellDefinition;
import neoatlantis.entity.csv.objects.CsvDAO;
import neoatlantis.entity.csv.objects.DataModel;
import neoatlantis.entity.csv.objects.ResultData;
import neoatlantis.entity.csv.objects.ResultImport;
import org.apache.log4j.Logger;

/**
 * Clase que realiza la importación d elos datos de un archivo CSV a una base de datos
 * @author Hiryu (aslhiryu@gmail.com)
 */
public class CsvImporter {
    private static final Logger DEBUGGER=Logger.getLogger(CsvImporter.class);
    
    private DataModel model;
    private Properties configDB;
    private String fileName;
    private String fileEncoding;
    
    public CsvImporter(Properties configDB, String fileName, String encoding, DataModel model){
        this.model=model;
        this.configDB=configDB;
        this.fileName=fileName;
        this.fileEncoding=encoding;
    }
    
    public ResultImport executeImport() throws CsvImportException{
        ResultImport result=new ResultImport();
        boolean resDao;

        try{
            CsvDAO dao=new CsvDAO(this.configDB, this.model);
            CsvLoader loader=new CsvLoader(this.fileName, this.fileEncoding, this.model.getColumnDelimiter(), this.model.getStringDelimiter());
            String[] data;
            int keyPosition=this.model.getColumnKey();
            CellDefinition existe;
            ResultData res;
            
            if( this.fileName.indexOf(File.separator)>0 ){
                result.setFileName(this.fileName.substring(this.fileName.lastIndexOf(File.separatorChar)+1));
            }
            else{
                result.setFileName(this.fileName);
            }
            DEBUGGER.debug("Inicia la importacion del archivo: "+this.fileName);
            DEBUGGER.debug("Con llave en la posicion: "+keyPosition);

            //recorro los datos del archivo
            while( (data=loader.nextRow())!=null ){
                //valida si la primera liena son titulos
                if( loader.getRows()==1 && this.model.isWithTitles() ){
                    continue;
                }
                
                res=new ResultData();
                try{
                    existe=dao.selectById(data[keyPosition]);
                    //si se necesita actualizar
                    if(existe!=null){
                        DEBUGGER.debug("La llave '"+data[keyPosition]+"' ya existe");
                        resDao=dao.update(data);
                        
                        if(resDao){
                            result.setUpdated(result.getUpdated()+1);
                            res.setDetails("Registro actualizado");
                            res.setRight(true);
                        }
                        else{
                            result.setUpdated(result.getWithError()+1);
                            res.setDetails("No se logro actualizar el registro");
                            res.setRight(false);
                        }
                    }
                    //si se necesita agregar
                    else{
                        DEBUGGER.debug("La llave '"+data[keyPosition]+"' no existe");
                        resDao=dao.insert(data);
                        
                        if(resDao){
                            result.setInserted(result.getInserted()+1);
                            res.setDetails("Registro agregado");
                            res.setRight(true);
                        }
                        else{
                            result.setUpdated(result.getWithError()+1);
                            res.setDetails("No se logro agregar el registro");
                            res.setRight(false);
                        }
                    }
                }
                catch(SQLException ex){
                    result.setWithError(result.getWithError()+1);
                    res.setDetails(ex.getMessage());
                    res.setRight(false);
                }
                
                res.setData(data);
                res.setRow(loader.getRows());
                result.getData().add(res);
            }
        }
        catch(CsvImportException ex){
            throw ex;
        }
        catch(IOException ex){
            throw new CsvImportException("Existio un problema leer la inrformación", ex);
        }
        
        return result;
    }
}
