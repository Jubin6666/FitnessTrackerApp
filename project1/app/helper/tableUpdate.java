package helper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


import static helper.dataBaseConnection.connect;

public class tableUpdate{

    public static void updateTable (String name,double lat,double lon,double total_distance,
                             String timestamp,int currentID) throws SQLException
    {
        Connection c =connect();
        String sql = "INSERT INTO people"
                + "(name, lat, lon,total_distance,timestamp,currentID) VALUES"
                + "(?,?,?,?,?,?)";
        try
        {

            PreparedStatement pstmt = c.prepareStatement(sql);
            //the required values for the latitiude,lonvalue,total_distance,timestamp from post request .

            // set the corresponding param for the sql statements
            pstmt.setString(1, name);
            pstmt.setDouble(2,lat);
            pstmt.setDouble(3,lon);
            pstmt.setDouble(4,total_distance);
            pstmt.setString(5,timestamp);
            pstmt.setInt(6, currentID);

            // execute the sql statement
            pstmt.executeUpdate();

        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        finally{
            c.close();
        }

    }




}
