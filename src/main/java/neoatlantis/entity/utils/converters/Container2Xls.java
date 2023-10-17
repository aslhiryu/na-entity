package neoatlantis.entity.utils.converters;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Properties;
import neoatlantis.entity.ContainerEntities;
import neoatlantis.entity.SimpleEntity;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import neoatlantis.entity.utils.UtilsEntities;

/**
 *
 * @author Hiryu (aslhiryu@gmail.com)
 */
public class Container2Xls {
    public static void parse(ContainerEntities cto, OutputStream out) throws IOException {
        parse(cto, out, null, null, BorderStyle.NONE);
    }

    public static void parse(ContainerEntities cto, OutputStream out, Properties nombres) throws IOException {
        parse(cto, out, nombres, null, BorderStyle.NONE);
    }

    public static void parse(ContainerEntities cto, OutputStream out, Properties nombres, Properties visibles) throws IOException {
        parse(cto, out, nombres, visibles, BorderStyle.NONE);
    }

    public static void parse(ContainerEntities cto, OutputStream out, Properties nombres, Properties visibles, BorderStyle borde) throws IOException {
        generaLibro(cto, nombres, visibles, borde).write(out);
    }

    public static byte[] parseBytes(ContainerEntities cto) throws IOException {
        return parseBytes(cto, null, null, BorderStyle.NONE);
    }

    public static byte[] parseBytes(ContainerEntities cto, Properties nombres) throws IOException {
        return parseBytes(cto, nombres, null, BorderStyle.NONE);
    }

    public static byte[] parseBytes(ContainerEntities cto, Properties nombres, Properties visibles) throws IOException {
        return parseBytes(cto, nombres, visibles, BorderStyle.NONE);
    }

    public static byte[] parseBytes(ContainerEntities cto, Properties nombres, Properties visibles, BorderStyle borde) throws IOException {
        HSSFWorkbook libro = generaLibro(cto, nombres, visibles, borde);

        return libro.getBytes();
    }

    private static HSSFWorkbook generaLibro(ContainerEntities cto, Properties nombres, Properties visibles, BorderStyle borde) throws IOException {
        //si el objeto no existe
        if (cto == null || cto.size() == 0) {
            return null;
        }System.out.println("Entro ***");

        //para un libro
        HSSFWorkbook libro = new HSSFWorkbook();
        HSSFSheet hoja = libro.createSheet();
        HSSFRow renglon = null;
        HSSFCell celda = null;
        int i=0;

        //preparo el estilo
        HSSFCellStyle estilo, estilo2;
        HSSFFont fuente;

        estilo = libro.createCellStyle();
        estilo.setBorderBottom(borde);
        estilo.setBorderLeft(borde);
        estilo.setBorderTop(borde);
        fuente = libro.createFont();
        fuente.setBold(true);
        estilo.setFont(fuente);
        estilo.setAlignment(HorizontalAlignment.CENTER);

        estilo2 = libro.createCellStyle();
        estilo2.setBorderBottom(borde);
        estilo2.setBorderLeft(borde);
        estilo2.setBorderRight(borde);
        estilo2.setBorderTop(borde);

        //genero los titulos
        renglon = hoja.createRow(0);
        for(List alTmp: cto.get(0).getCache()) {
            //omite las propiedades que no son ni publicas ni privadas
            if( (!Modifier.isPrivate(((Field)alTmp.get(0)).getModifiers()) && !Modifier.isPublic(((Field)alTmp.get(0)).getModifiers()))
                    || !UtilsEntities.esVisible((Field)alTmp.get(0), visibles)){
                 continue;
            }

            celda = renglon.createCell((short) i);
            celda.setCellStyle(estilo);
            celda.setCellValue(UtilsEntities.obtieneNombre((Field)alTmp.get(0), nombres));
            i++;
        }

        //genero los valores
        for(int j=0; j<cto.size(); j++){
            renglon = hoja.createRow(j + 1);
            i=0;

            for(List alTmp: cto.get(0).getCache()) {
                //omite las propiedades que no son ni publicas ni privadas
                if( (!Modifier.isPrivate(((Field)alTmp.get(0)).getModifiers()) && !Modifier.isPublic(((Field)alTmp.get(0)).getModifiers()))
                        || !UtilsEntities.esVisible((Field)alTmp.get(0), visibles)){
                     continue;
                }

                celda = renglon.createCell((short) i);
                celda.setCellStyle(estilo2);
//System.out.println("=> "+((Field)alTmp.get(0)).getType().getName()+", ");
                if( Modifier.isPublic(((Field)alTmp.get(0)).getModifiers()) ){
                    try{
                        if( ((Field)alTmp.get(0)).getType().getName().equals("int") || ((Field)alTmp.get(0)).getType().getName().equals("long") ||
                                ((Field)alTmp.get(0)).getType().getName().equals("short") || ((Field)alTmp.get(0)).getType().getName().equals("float") ||
                                ((Field)alTmp.get(0)).getType().getName().equals("double") ){
                                celda.setCellValue( Double.parseDouble(""+((Field)alTmp.get(0)).get(cto.get(j))) );
                        }
                        else{
                            celda.setCellValue( ""+((Field)alTmp.get(0)).get(cto.get(j)) );
                        }
                    }catch(Exception ex){
                        throw new RuntimeException("No se logro recuperar el valor de '"+((Field)alTmp.get(0)).getName()+"': "+ex);
                    }
                }
                //es una entidad
                else if( SimpleEntity.isEntity(((Field)alTmp.get(0)).getType()) ){
                    try{
                        //data[i]=parse( (SimpleEntity)((Method)alTmp.get(1)).invoke(to) );
                         celda.setCellValue("[Entity]");
                    }catch(Exception ex){
                        throw new RuntimeException("No se logro recuperar el valor de la entidad '"+((Method)alTmp.get(1)).getName()+"': "+ex);
                    }
                }
                //es privado
                else{
                    try{
                        if( ((Field)alTmp.get(0)).getType().getName().equals("int") || ((Field)alTmp.get(0)).getType().getName().equals("long") ||
                                ((Field)alTmp.get(0)).getType().getName().equals("short") || ((Field)alTmp.get(0)).getType().getName().equals("float") ||
                                ((Field)alTmp.get(0)).getType().getName().equals("double") ){
                                celda.setCellValue( Double.parseDouble(""+((Method)alTmp.get(1)).invoke(cto.get(j))) );
                        }
                        else{
                            celda.setCellValue( ""+((Method)alTmp.get(1)).invoke(cto.get(j)) );
                        }
                    }catch(Exception ex){
                        throw new RuntimeException("No se logro recuperar el valor de '"+((Method)alTmp.get(1)).getName()+"': "+ex);
                    }
                }

                i++;
            }
        }

        return libro;
    }
}
