package helper;
//the class is used to calculate the intital distance for the current entry


import java.sql.SQLException;
import java.util.HashMap;

public class CalculateTotalDistance
{
    public static double calculateInitialDistance(String name,int currentID,double lat,double lon) throws SQLException
    {
        //previousLon is used to get the lon value for the previous ID
        Double previousLon;

        //previousLat is used to get the previous lat value
        Double previousLat;

        //totalDistanceInitial is basically the totalDistance
        //which was travelled upto the last update
        Double totalDistanceInitial;

        Double tempDistance;
        HashMap<String,String> map = new HashMap<String,String>();
        //the if clause is to check that the entry is not the first entry
        if (currentID > 1) {
            map =getTableValues.getValues(name,currentID - 1);
        }
        previousLon = Double.valueOf(map.get("lon"));
        previousLat=Double.valueOf(map.get("lat"));
        totalDistanceInitial = Double.valueOf(map.get("total_distance"));

        tempDistance =CalculateDistance.calculateDistance
                (previousLat,previousLon,lat,lon);

        return tempDistance+totalDistanceInitial;



    }

}
