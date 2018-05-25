package com.jeromeothompson.myapplication;

/**
 * Created by root on 4/17/18.
 */

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class RemoteFetch {

    //private static final String OPEN_WEATHER_MAP_API = "http://api.openweathermap.org/data/2.5/weather?q=%s&units=metric";
    private static final String OPEN_WEATHER_MAP_API = "http://api.openweathermap.org/data/2.5/forecast?q=%s";

    public static String tag = "RemoteFetchhhhhhhhhhhhhhhhhhhhhhhh";
    static boolean rain, endBool = false;

    public static String getValue(String val)
    {
        if(val.charAt(0) == '0')
            return val.substring(1,2);
        else
            return val;
    }

    public static boolean isItGoingtoRain( String list)
    {
        int index1 = list.indexOf("weather");
        int index2 = list.indexOf("clouds");
        //Log.d(tag, "index1111: " + index1 + " index2222: " + index2);
        int indexRain = list.substring(index1 + 8 + 3 , index2 ).indexOf("Rain");

        Log.d(tag, "indexRain: " + indexRain);
        if(indexRain == -1)
            return false;
        else
            return true;
    }

    public static boolean getRaining(ArrayList<String> list)
    {
        boolean raining = false;

        for (int i=0;i<list.size();i++)
        {
            raining = raining || RemoteFetch.isItGoingtoRain((list.get(i)));
        }
        Log.d(tag,  "getRaining: " + raining);
        rain = raining;
        endBool = true;
        return raining;
    }

    public static ArrayList<String> getJSON(Context context, String city){
        try {
            URL url = new URL(String.format(OPEN_WEATHER_MAP_API, city));
            HttpURLConnection connection =
                    (HttpURLConnection)url.openConnection();

            connection.addRequestProperty("x-api-key",
                    context.getString(R.string.open_weather_maps_app_id));







            Log.d(tag, "BufferedReader: " + OPEN_WEATHER_MAP_API);
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                Log.d(tag, "R.string.open_weather_maps_app_id: " + context.getString(R.string.open_weather_maps_app_id));
            StringBuffer json = new StringBuffer(2048);
            String tmp="";
            while((tmp=reader.readLine())!=null) {
                //Log.d(tag, "whilewhilewhile: " + tmp + "\n");
                json.append(tmp).append("\n");
            }
            reader.close();

            JSONObject data = new JSONObject(json.toString());
            //Log.d(tag, "data: " + data.getJSONArray("list"));
            // This value will be 404 if the request was not
            // successful



            ArrayList<String> list = new ArrayList<String>();
            JSONArray jsonArray = data.getJSONArray("list");
            int count = 0;
            if (jsonArray != null) {
                int len = jsonArray.length();
                for (int i=0;i<len;i++){
                    //list.add(jsonArray.get(i).toString());
                    //Log.d(tag, "in array: " + i + " :::: " + list.get(i) );

                    int indexLast = jsonArray.get(i).toString().lastIndexOf('"');
                    //Log.d(tag,  "INDEX1: " + indexLast);



                    int indexSecLast = jsonArray.get(i).toString().substring(0,indexLast).lastIndexOf('"');
                    Log.d(tag,  "INDEX2: " + jsonArray.get(i).toString().substring(indexSecLast+1, indexLast).split(" ")[0]  );

                    String timeString = jsonArray.get(i).toString().substring(indexSecLast+1, indexLast).split(" ")[1];
                    timeString = timeString.substring(0,2);
                    timeString = getValue(timeString);

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Calendar today = Calendar.getInstance();
                    today.add(Calendar.DAY_OF_YEAR, 2);
                    Date tomorrow = today.getTime();
                    String tomorrowString = sdf.format(  tomorrow );

                    String jsonDate = jsonArray.get(i).toString().substring(indexSecLast+1, indexLast).split(" ")[0];


                    if(tomorrowString.equals(jsonDate) && (Integer.parseInt(timeString) >= 3 ||  Integer.parseInt(timeString) <= 21 ))
                    {
                        Log.d(tag, "ADDED: " + i + " :::: " + jsonArray.get(i).toString() );
                        Log.d(tag, "timeString: " + i + " :::: " + timeString );

                        list.add(jsonArray.get(i).toString());
                    }

                    //Log.d(tag, "ADTE: " + i + " :::: " + sdf.format(  tomorrow ) );
                    count++;
                }

            }


            if(data.getInt("cod") != 200){
                return null;
            }
            Log.d(tag, "ENDDDDDDD " );
           // return data;
            return list;
        }catch(Exception e){
            Log.d(tag, "Exception " + e.getStackTrace() + "\ntoString"  + e);
            return null;
        }
    }
}
