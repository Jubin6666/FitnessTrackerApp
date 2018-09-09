package helper;

import com.fasterxml.jackson.databind.util.JSONPObject;
import play.libs.Json;
import scala.util.parsing.json.JSONObject$;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;


import static helper.dataBaseConnection.connect;

public class getTableValues
{
    public static HashMap<String,String> getValues (String name, int currentID) throws SQLException
    {
        HashMap<String,String> map = new HashMap<String,String>();
        Connection c =connect();
        String sql ="SELECT lat,lon,total_distance,timestamp from people WHERE "
                +" people.name=? "
                +"AND people.currentID=?";
        try
        {

            PreparedStatement pstmt = c.prepareStatement(sql);
            // set the corresponding param for the sql statements
            pstmt.setString(1, name);
            pstmt.setInt(2,currentID);
            // execute the sql statement
            ResultSet rs= pstmt.executeQuery();
            while(rs.next())
            {
                map.put("lat", String.valueOf(rs.getDouble("lat")));
                map.put("lon", String.valueOf(rs.getDouble("lon")));
                map.put("total_distance", String.valueOf(rs.getDouble("total_distance")));
                map.put("timestamp", String.valueOf(rs.getDouble("timestamp")));
            }

        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        finally{
            c.close();
        }
return map;
    }
}
