package helper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
//the class does operations related to doing operations
//related to both distance and time.

import static helper.dataBaseConnection.connect;

public class TimeCalculator
{

    public static int timeCalculator(int id, double totalDistance,
                                     String time, String name)
            throws SQLException
    {
        Connection c =null;
        double distance1=0.00;
        String time1="";
        int timeDifference =0;
        int finalTime=0;

        String sql ="SELECT total_distance,timestamp from people WHERE "
                +"people.name=?"
                +"AND people.currentID=?";

        try {
            c=connect();
            PreparedStatement pstmt1 = c.prepareStatement(sql);
            // set the corresponding param for the sql statements
            pstmt1.setString(1, name);
            pstmt1.setInt(2, id-5);

            // execute the sql statement
            pstmt1.executeQuery();

            ResultSet rs = pstmt1.executeQuery( );
            while(rs.next())
            {
                distance1=rs.getDouble("total_distance");
                time1=rs.getString("timestamp");

            }
            int timeGap =CalculateTimeDifference.differenceInTime(time1,time);
            double speed =  ((totalDistance-distance1)/(timeGap));
            if(speed>=20){return 1;}
            else if(speed<=1){return 5;}

            //calculating the value using slope function.
            else{return (1+((int)speed-20)*(4/19));

            }

        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        finally{
            c.close();
        }


        return 0;
    }
    public static int timeCalculator(String timeStampCurrent,String name,double totalDistance) throws SQLException
    {
        Connection c=null;
        String sql ="SELECT timestamp from people WHERE "
                +"people.name=?"
                +"AND people.currentID=?";
        String previousTimeStamp="";

        try {
            c=connect();
            PreparedStatement pstmt1 = c.prepareStatement(sql);
            // set the corresponding param for the sql statements
            pstmt1.setString(1, name);
            pstmt1.setInt(2, 1);

            // execute the sql statement
            pstmt1.executeQuery();

            ResultSet rs = pstmt1.executeQuery( );
            while(rs.next())
            {
                previousTimeStamp=rs.getString("timeStamp");
            }
            int timeGap =CalculateTimeDifference.differenceInTime(previousTimeStamp,timeStampCurrent);


            double speed =  ((totalDistance)/(timeGap));

            if(speed>=20){return 1;}
            else if(speed<=1){return 5;}

            //calculating the value using slope function.
            else{return (1+((int)speed-20)*(4/19));

            }

        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        finally{
            c.close();
        }




        return 0;
    }


}
