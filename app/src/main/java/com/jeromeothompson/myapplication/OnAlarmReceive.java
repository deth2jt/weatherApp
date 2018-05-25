package com.jeromeothompson.myapplication;

import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by root on 4/22/18.
 */

public class OnAlarmReceive extends BroadcastReceiver
{
    public static String TAG = "OnAlarmReceive";
    RemoteFetch rf = new RemoteFetch();
    @Override
    public void onReceive(final Context context, final Intent intent)
    {

        Log.d(TAG, "BroadcastReceiver, in onReceive:");

        /*
        //Toast.makeText(context, "I'm running", Toast.LENGTH_SHORT).show();

        String cityString = intent.getStringExtra("city");
        WeatherFragment wf = new WeatherFragment();
        //wf.updateWeatherData(cityString);
        //Boolean bool = wf.raining;
        Boolean raining = false;


        ArrayList<String> list = RemoteFetch.getJSON(context, cityString);



        try {
            //boolean raining = false;

            for (int i=0;i<list.size();i++)
            {
                raining = raining || RemoteFetch.isItGoingtoRain((list.get(i)));
            }

        }
        catch(Exception e){
            e.printStackTrace();
            Log.e("SimpleWeather", "Error getting raining data");
        }



        Log.d("TAG", "boolboolboolbool: " + raining + " :::: intent.getStringExtra(city): " + cityString);
        */

        //Boolean val = true;
        Log.d("TAG", "citycitycity: " + intent.getStringExtra("city"));
        boolean rain = false;
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                //boolean val = false;
                //while (!val) {
                    try {
                        //Your code goes here
                        rf.getRaining(rf.getJSON(context, intent.getStringExtra("city")));
                        Log.d("TAG", "In thread: " );
                        //val = val || rf.endBool;
                        //Thread.sleep(6000);
                        //if(val)
                        //Thread.currentThread().interrupt();
                        //return;
                        if(rf.rain)
                        {
                            try
                            {
                                Log.d("TAG", "it is raining raining: ");
                                //MainActivity mainActivity = new MainActivity();
                                WallpaperManager myWallpaperManager = WallpaperManager.getInstance(context);

                                myWallpaperManager.setResource(+R.mipmap.rain);
                                //Toast.makeText(context, "Wallpaper successfully changed", Toast.LENGTH_SHORT).show();
                                Log.d("TAG", "aWallpaper successfully changed");
                            }
                            catch(Exception e)
                            {
                                e.printStackTrace();
                                Log.e("SimpleWeather", "One or more fields not found in the JSON data");
                            }

                        }
                        else
                        {
                            try
                            {
                                //MainActivity mainActivity = new MainActivity();
                                WallpaperManager myWallpaperManager = WallpaperManager.getInstance(context);

                                myWallpaperManager.setResource(+R.mipmap.him);
                                //Toast.makeText(context, "Wallpaper successfully changed", Toast.LENGTH_SHORT).show();
                                Log.d("TAG", "bWallpaper successfully changed");
                            }
                            catch(Exception e)
                            {
                                e.printStackTrace();
                                Log.e("SimpleWeather", "One or more fields not found in the JSON data");
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
               // }
            }
        });

        thread.start();

        /*
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                thread.interrupt();
            }
        }, 6000);
        //thread.stop();
        */

        //boolean rain = RemoteFetch.getRaining(RemoteFetch.getJSON(context, intent.getStringExtra("city")));
        //Log.d("TAG", "rainrainrain: " + rain);
        Log.d("TAG", "rf.rainrf.rainrf.rain: " + rf.rain + " :::: intent.getStringExtra(city): " + intent.getStringExtra("city"));




    }





}
