/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pruebas;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import neoAtlantis.utilidades.entity.objects.ParameterAwarePreparedStatement;

/**
 *
 * @author desarrollo.alberto
 */
public class PruebaPrepared {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        pruebaStatement();
    }
    
    public static void pruebaStatement() throws SQLException, ClassNotFoundException{
        Class.forName("net.sourceforge.jtds.jdbc.Driver");
        Connection con=DriverManager.getConnection("jdbc:jtds:sqlserver://SRDESBDCRM01/smd", "simodef", "simodef");
        
        ParameterAwarePreparedStatement ps=new ParameterAwarePreparedStatement(con.prepareStatement("select * from ctl_areas where idarea=?"));
        ps.setInt(1, 50);
        
        ParameterMetaData rs=ps.getParameterMetaData();
        
        System.out.println("Params: "+rs.getParameterCount());
        for(int i=1; i<=rs.getParameterCount(); i++){
            System.out.println("Tipo: "+rs.getParameterClassName(i));
            System.out.println("Valor: "+ps.getParameters().get(i));
        }
        
        
        ps.close();
        con.close();
    }
}
