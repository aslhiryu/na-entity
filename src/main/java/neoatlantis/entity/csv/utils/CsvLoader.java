package neoatlantis.entity.csv.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import org.apache.log4j.Logger;

/**
 * Objeto que lee el contenido de un CSV
 * @author Hiryu (aslhiryu@gmail.com)
 */
public class CsvLoader {
    private Logger DEBUGGER=Logger.getLogger(CsvLoader.class);
    
    public static final char VACIO='\u0000';
    
    private BufferedReader file;
    private char deliminator=',';
    private char deliminatorString='"';
    private int rows;
    private int cols;
    
    public CsvLoader(String file, String encoding) throws FileNotFoundException, UnsupportedEncodingException{
        this(file, encoding, ',', VACIO);
    }
    
    public CsvLoader(String file, String encoding, char del, char delString) throws FileNotFoundException, UnsupportedEncodingException{
        this.file=new BufferedReader(new InputStreamReader(new FileInputStream(file), encoding));
        this.deliminator=del;
        this.deliminatorString=delString;
        this.rows=0;
        this.cols=0;
        
        DEBUGGER.debug("Carga al archivo: "+this.file);
        DEBUGGER.debug("Delimitador de columna: "+this.deliminator);
        DEBUGGER.debug("Delimitador de cadena: "+this.deliminatorString);
    }
    
    public String[] nextRow() throws IOException{
        String line;
        String[] data;
        String del="";
        
        if( this.deliminatorString!=VACIO ){
            del+=this.deliminatorString;
        }
        del+=this.deliminator;
        if( this.deliminatorString!=VACIO ){
            del+=this.deliminatorString;
        }
        
        if(this.file==null){
            throw new IOException("El archivo ha sido cerrado");
        }
        
        if((line = this.file.readLine()) != null) {
            data=line.split( del );
            this.rows++;
            this.cols=data.length;
            
            return data;
        }
        else{
            this.file.close();
            this.file=null;
            return null;
        }
    }
    
    public int getRows(){
        return this.rows;
    }
    
    public int getCols(){
        return this.cols;
    }
}
