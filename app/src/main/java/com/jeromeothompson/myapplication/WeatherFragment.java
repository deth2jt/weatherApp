package com.jeromeothompson.myapplication;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * A placeholder fragment containing a simple view.
 */
public class WeatherFragment extends Fragment {
    Typeface weatherFont;

    TextView cityField;
    TextView updatedField;
    TextView detailsField;
    TextView currentTemperatureField;
    TextView weatherIcon;

    Handler handler;
    public String tag = "WeatherFragment";

    public WeatherFragment(){
        handler = new Handler();
    }

    boolean raining = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        cityField = (TextView)rootView.findViewById(R.id.city_field);
        updatedField = (TextView)rootView.findViewById(R.id.updated_field);
        detailsField = (TextView)rootView.findViewById(R.id.details_field);
        currentTemperatureField = (TextView)rootView.findViewById(R.id.current_temperature_field);
        weatherIcon = (TextView)rootView.findViewById(R.id.weather_icon);

        weatherIcon.setTypeface(weatherFont);
        //cityField.setText("123");
        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(tag, "onCreate: " + new CityPreference(getActivity()).getCity());
        weatherFont = Typeface.createFromAsset(getActivity().getAssets(), "fonts/weather.ttf");
        //updateWeatherData(new CityPreference(getActivity()).getCity());

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                renderWeather(new CityPreference(getActivity()).getCity());
            }
        }, 5000);

        Log.d(tag, "onCreate WeatherFragement: " );
    }

    public void updateWeatherData(final String city){
        Log.d(tag, "updateWeatherData: " );
        new Thread(){
            public void run(){
                final ArrayList<String> list = RemoteFetch.getJSON(getActivity(), city);
                if(list == null){
                    handler.post(new Runnable(){
                        public void run(){
                            Toast.makeText(getActivity(),
                                    getActivity().getString(R.string.place_not_found),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    handler.post(new Runnable(){
                        public void run(){
                            //renderWeather(list);
                            renderWeather(city);
                        }
                    });
                }
            }
        }.start();
    }

    //public void renderWeather(ArrayList<String> list){
    public void renderWeather(String city){
        try {
            //boolean raining = false;
            Log.d(tag, "renderWeather: " );
            //RemoteFetch.getRaining(list);
            /*
            for (int i=0;i<list.size();i++)
            {
                raining = raining || RemoteFetch.isItGoingtoRain((list.get(i)));
            }
            Log.d(tag,  "raining: " + raining);

            */
            /*
            cityField.setText(json.getString("name").toUpperCase(Locale.US) +
                    ", " +
                    json.getJSONObject("sys").getString("country"));
            */
            Log.d(tag,  "cities: " + city);
            cityField
                    .setText
                            (city);
            //changeWallpaper2();
            //JSONObject details = json.getJSONArray("weather").getJSONObject(0);
            //JSONObject main = json.getJSONObject("main");
            //detailsField.setText("FOOFOFFO");
            /*
            detailsField.setText(
                    details.getString("description").toUpperCase(Locale.US) +
                            "\n" + "Humidity: " + main.getString("humidity") + "%" +
                            "\n" + "Pressure: " + main.getString("pressure") + " hPa");


            currentTemperatureField.setText(
                    String.format("%.2f", main.getDouble("temp"))+ " ℃");

            DateFormat df = DateFormat.getDateTimeInstance();
            String updatedOn = df.format(new Date(json.getLong("dt")*1000));
            */

            //updatedField.setText("Last update: " + updatedOn);

            /*
            setWeatherIcon(details.getInt("id"),
                    json.getJSONObject("sys").getLong("sunrise") * 1000,
                    json.getJSONObject("sys").getLong("sunset") * 1000);
            */
            //ArrayList<String> list = json.getJSONObject("sys");
            //setRecurringAlarm(getActivity());

        }catch(Exception e){
            e.printStackTrace();
            Log.e("SimpleWeather", "One or more fields not found in the JSON data");
        }
    }

    private void setRecurringAlarm(Context context) {

        Calendar updateTime = Calendar.getInstance();
        //updateTime.setTimeZone();
        updateTime.set(Calendar.HOUR_OF_DAY, 22);
        updateTime.set(Calendar.MINUTE, 37);

        //String cityString = new CityPreference(context).getCity();

        //Log.i("TAG", "new CityPreference(this).getCity(): " + );

        Intent intent = new Intent(context, OnAlarmReceive.class);
        //intent.putExtra("city", cityString);
        intent.putExtra("raining", raining);

        PendingIntent recurringDownload = PendingIntent.getBroadcast(context,0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarms = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarms.setInexactRepeating(AlarmManager.RTC_WAKEUP,updateTime.getTimeInMillis(),AlarmManager.INTERVAL_DAY, recurringDownload);
        Log.i("TAG", "ZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZz");
    }

    private void setWeatherIcon(int actualId, long sunrise, long sunset){
        int id = actualId / 100;
        String icon = "";
        if(actualId == 800){
            long currentTime = new Date().getTime();
            if(currentTime>=sunrise && currentTime<sunset) {
                icon = getActivity().getString(R.string.weather_sunny);
            } else {
                icon = getActivity().getString(R.string.weather_clear_night);
            }
        } else {
            switch(id) {
                case 2 : icon = getActivity().getString(R.string.weather_thunder);
                    break;
                case 3 : icon = getActivity().getString(R.string.weather_drizzle);
                    break;
                case 7 : icon = getActivity().getString(R.string.weather_foggy);
                    break;
                case 8 : icon = getActivity().getString(R.string.weather_cloudy);
                    break;
                case 6 : icon = getActivity().getString(R.string.weather_snowy);
                    break;
                case 5 : icon = getActivity().getString(R.string.weather_rainy);
                    break;
            }
        }
        weatherIcon.setText(icon);
    }

    public void changeCity(String city){
        updateWeatherData(city);
    }


}
