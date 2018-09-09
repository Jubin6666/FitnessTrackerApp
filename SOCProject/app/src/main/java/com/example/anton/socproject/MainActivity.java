package com.example.anton.socproject;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity  {
    //the url to for the server.


     Button button1;
     EditText editText1;
     EditText editText2;
     TextView textView;
     //this queue is used for the volley,
     //singleton implementation is used to avoid 2 queues
     public RequestQueue mQueue;
     //mFusedLocationClient is basically used to get the location
     private FusedLocationProviderClient mFusedLocationClient;
     //Handler helps to do work between threads
     private Handler mainHandler = new Handler( );
      //COUNT is an index which is used to maintain the
     //number of times the user model has send data
      public  int COUNT;
     //boolean flag used to control threads
     public boolean check =false;
     public String url="";
     public String name="";

     protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button1 = (Button) findViewById(R.id.button1);
        editText1 = (EditText) findViewById(R.id.editText1);
        editText1.setText("10.0.2.2:9000");
        editText2 = (EditText) findViewById(R.id.editText2);
        textView = (TextView) findViewById(R.id.TextView1);
        //the setTag is used to toggle between the StartActivity and StopActivity
        //the tag is first set to 1 at that point the button is Start Activity Tracker
        //when the button is clicked the button has text Stop Activity Tracker and the tag is 0.
        button1.setTag(1);
        button1.setText("Start Activity Tracker");
        mQueue = Volley.newRequestQueue(this);
        mFusedLocationClient = new FusedLocationProviderClient(this);

        //the onClickListener is used for the button1 to toggle between
        //the Start and Stop Activity

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            int STATUS = (Integer) v.getTag();
                if (STATUS == 1) {
                    StringBuilder finalURL = new StringBuilder();
                    finalURL.append("http://");
                    finalURL.append(editText1.getText().toString());
                    finalURL.append("/locationupdate");
                    url=finalURL.toString();



                    name =(editText2.getText().toString());
                    COUNT=0;

                    button1.setText("Stop Activity Tracker");
                    v.setTag(0);
                    check=true;
                    MyThread myThread = new MyThread();
                    myThread.run();
                } else {
                    button1.setText("Start Activity Tracker");
                    check=false;
                    v.setTag(1);
                }
            }
        });
    }

    public void sendPostRequest(JSONObject LatNLon) throws JSONException {
        JSONObject json = new JSONObject();
        COUNT=COUNT+1;
        json.put("name", name.toString());
        json.put("lat", LatNLon.get("lat"));
        json.put("lon", LatNLon.get("lon"));
        json.put("currentID", COUNT);
        json.put("timestamp",getCurrentTimeStamp());
        json.put("indicator",0);
        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST,url, json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            final double  totalDistance = (double) response.get("totalDistance");
                            int time = (int) response.get("timeToWait");
                            System.out.println("totalDistance is: "+totalDistance);
                            System.out.println("wait time is: "+ time );
                            textView.setText("Total Distance"+ totalDistance);
                            delayMethod(time);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        System.out.println("Error on response");
                    }
                }
        );
        mQueue.add(postRequest);
    }
    //this method is used to get the time and date
    //in the
    //yy/MM/dd HH:mm:ss format so that there are no
    //errors in format at both client and server.
    public static String getCurrentTimeStamp(){
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(
                    "yy/MM/dd HH:mm:ss");
            String currentDateTime = dateFormat.format(new Date());

            return currentDateTime;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    //method to get current lat and lon values.
    public  void getLocation () {
        final JSONObject json = new JSONObject();
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            requestPermission();
        } else {
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener( new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                try {
                                    json.put("lat", location.getLatitude());
                                    json.put("lon", location.getLongitude());
                                    sendPostRequest(json);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
        }
    }
    public void requestPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                1);
    }
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    getLocation();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    requestPermission();
                }
                return;
            }

        }
    }

    //MyThread is used to maintain a different thread for Non UI operations.
    public class MyThread extends Thread{
        public void run(){
            getLocation();
        }
    }
    //delayMethod has an handler which helps the thread to
    //wait for an interval to send the next update.
     public void delayMethod(int time){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(check==true) {
                    getLocation();
                }
                else{
                    try {
                        sendFinalRequest();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return;
                }
            }
        },time);
     }
     //sendFinalRequest is used to send the last request to the server
    //this helps the server to delete the enteries when the runner has clicked stop.
    public void sendFinalRequest() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("indicator",1);
        json.put("name",name);
        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url, json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                    }
                }
        );
        mQueue.add(postRequest);
    }
}

