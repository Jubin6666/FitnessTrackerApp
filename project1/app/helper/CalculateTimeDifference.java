package helper;
//this is a helper method used to find the number of seconds between
// two datetime intervals


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CalculateTimeDifference
{


    public static int differenceInTime(String dateStart, String dateStop)
    {
        SimpleDateFormat format = new SimpleDateFormat("yy/MM/dd HH:mm:ss");

        Date d1 = null;
        Date d2 = null;
        try {
            d1 = format.parse(dateStart);
            d2 = format.parse(dateStop);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int diff = Math.toIntExact(d2.getTime() - d1.getTime());
        int diffSeconds = diff / 1000;

        return diffSeconds;

    }


}
