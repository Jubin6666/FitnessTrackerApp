package helper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static helper.dataBaseConnection.connect;

/**
 * The SpeedCalculation is used to work on operations related to
 * **/
public class SpeedCalculation
{
    public static int calculateAverageSpeed(String name,String currentTimeStamp
            ,double totalDistanceFinal) throws SQLException
    {
        Connection c =connect();
        String sql ="SELECT timestamp from people WHERE "
                +" people.name=? "
                +"AND people.currentID=?";
        try
        {
            String startTime="";
            PreparedStatement pstmt = c.prepareStatement(sql);
            // set the corresponding param for the sql statements
            pstmt.setString(1, name);
            pstmt.setInt(2,1);
            // execute the sql statement
            ResultSet rs= pstmt.executeQuery();
            while(rs.next())
            {
                startTime=rs.getString("timestamp");
            }
            int timeDifference =CalculateTimeDifference.differenceInTime(startTime,currentTimeStamp);
        return (int)(totalDistanceFinal/(timeDifference));
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        finally{
            c.close();
        }

        return 0;



    }
}
