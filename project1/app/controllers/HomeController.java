package controllers;

import helper.*;
import play.mvc.*;

import com.google.inject.Inject;
import play.data.DynamicForm;
import play.data.FormFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;



import play.libs.Json;

import java.sql.*;
import java.util.HashMap;


import static helper.CalculateTotalDistance.calculateInitialDistance;
import static helper.TimeCalculator.timeCalculator;


/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {



    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    public Result index() {
        return ok(views.html.index.render());

    }

    /**
     * handleupdates() returns an json which has two values
     * timeToWait and totalDistance as the keys.
     * */
    @Inject
    FormFactory formFactory;
    public Result handleupdates() throws SQLException
    {


        //totalDistanceFinal is used to calculate the total distance
        // covered upto the given point.
        Double totalDistanceFinal=0.00;
        DynamicForm dynamicForm = formFactory.form().bindFromRequest();

        //indicator is used to check if the user has pressed the stop button
        //when the user presses the stop button his corresponding enteries is
        //deleted from database table.
        //this is a design decision to make the app and server have less data storage
        if(Integer.parseInt(dynamicForm.get("indicator"))==1){
            DeleteRecord.deleteRecordFromTable(dynamicForm.get("name"));
            return ok();
        }

        //variables from the dynamic form
        int currentID = Integer.parseInt(dynamicForm.get("currentID"));
        System.out.println(currentID);
        String name =dynamicForm.get("name");
        System.out.println("User Name is " +name);
        double lat = Double.parseDouble(dynamicForm.get("lat"));
        System.out.println("Latitude received is" + lat);
        double lon =Double.parseDouble(dynamicForm.get("lon"));
        System.out.println("Longitude received is"+ lon);
        String timestamp =dynamicForm.get("timestamp");
        System.out.println("Time at which message is send" +timestamp);

    // calculate the total distance upto the last entry
         if (currentID > 1) {
            totalDistanceFinal = calculateInitialDistance(name,
              currentID, lat, lon);
                int averageSpeed =SpeedCalculation.calculateAverageSpeed(
                        name,timestamp,totalDistanceFinal
                );
                System.out.println("Average Speed is: " +averageSpeed+ " m/s");
            }

        //update the sql table with the most recent entry.
        tableUpdate.updateTable(name,lat,lon,totalDistanceFinal,
                timestamp,currentID);




        ObjectNode result = Json.newObject();
        //the case to calculate when the total number of enteries is less than 5.
        if(Integer.parseInt(dynamicForm.get("currentID")) > 5){
            int time =timeCalculator(Integer.parseInt
                            (dynamicForm.get("currentID")),
                    totalDistanceFinal,dynamicForm.get("timestamp"),
                    dynamicForm.get("name"));
            result.put("timeToWait",time);
        }
        else if(Integer.parseInt(dynamicForm.get("currentID"))>1
                && Integer.parseInt(dynamicForm.get("currentID"))<=5)
            {
                int timeDifference =TimeCalculator.
                        timeCalculator(dynamicForm.get("timestamp")
                        ,dynamicForm.get("name"),totalDistanceFinal);
            int time = (int) (totalDistanceFinal/timeDifference);
            result.put("timeToWait",time);
            }
            //if it is just the first entry we use the waitTime as 5secs
            else if(Integer.parseInt(dynamicForm.get("currentID"))==1)
        {
            result.put("timeToWait",5);
        }
        result.put("totalDistance", totalDistanceFinal);
        System.out.println(result);
        return ok(result);


    }



}
