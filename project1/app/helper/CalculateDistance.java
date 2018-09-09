package helper;

//this class is used to calculate the distance between two positions on the globe
//based on the longitude and latitude values.
//the code has been reffered from https://stackoverflow.com/questions/18861728/
// calculating-distance-between-two-points-represented-by-lat-long-upto-15-feet-acc
public  class CalculateDistance
{
    public static double  calculateDistance(double lat1,double lng1,double lat2,double lng2)
    {
        int r = 6371; // average radius of the earth in km
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                        * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = r * c;
        return d*1000;
    }



}