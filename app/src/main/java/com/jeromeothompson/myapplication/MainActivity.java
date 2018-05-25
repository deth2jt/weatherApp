package com.jeromeothompson.myapplication;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (savedInstanceState == null) {

            getSupportFragmentManager().beginTransaction().add(R.id.container, new WeatherFragment()).commit();
            Log.d("tag", "onCreateeeeeeeeeee: " );

        }

        /*
        Log.d("tag", "wwwwwwwwwwwwwwwwwwwwww: " );
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                setRecurringAlarm(this);
            }
        }, 5000);
        */
        setRecurringAlarm(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("TAG", "On Resume .....");
    }
    private void setRecurringAlarm(Context context) {

        Calendar updateTime = Calendar.getInstance();
        //updateTime.setTimeZone();
        updateTime.set(Calendar.HOUR_OF_DAY, 23);
        updateTime.set(Calendar.MINUTE, 48);

        String cityString = new CityPreference(this).getCity();

        Log.i("MAinActivity", "new CityPreference(this).getCity(): " + cityString);

        Intent intent = new Intent(context, OnAlarmReceive.class);
        intent.putExtra("city", cityString);

        PendingIntent recurringDownload = PendingIntent.getBroadcast(context,0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarms = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        alarms.setInexactRepeating(AlarmManager.RTC_WAKEUP,updateTime.getTimeInMillis(),AlarmManager.INTERVAL_DAY, recurringDownload);
        Log.i("TAG", "BroadcastReceiverrrr");
    }






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.change_city){
            showInputDialog();
        }
        return false;
    }

    private void showInputDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Change city");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setPositiveButton("Go", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String val = input.getText().toString();
                changeCity(val);
                Log.d("TAG", "valvalvalvalval: " + val);
            }
        });

        builder.show();
    }

    public void changeCity(String city){
        WeatherFragment wf = (WeatherFragment)getSupportFragmentManager()
                .findFragmentById(R.id.container);
        wf.changeCity(city);

        changeWallpaper(wf);


        new CityPreference(this).setCity(city);
    }



    public void changeWallpaper(WeatherFragment wf)
    {
        Log.d("TAG", "changeWallpaperchangeWallpaperchangeWallpaperchangeWallpaper");
        if(wf.raining)
        {
            try
            {
                //MainActivity mainActivity = new MainActivity();
                WallpaperManager myWallpaperManager = WallpaperManager.getInstance(this);

                myWallpaperManager.setResource(+R.mipmap.rain);
                Toast.makeText(this, "Wallpaper successfully changed", Toast.LENGTH_SHORT).show();
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
                WallpaperManager myWallpaperManager = WallpaperManager.getInstance(this);

                myWallpaperManager.setResource(+R.mipmap.him);
                Toast.makeText(this, "Wallpaper successfully changed", Toast.LENGTH_SHORT).show();
            }
            catch(Exception e)
            {
                e.printStackTrace();
                Log.e("SimpleWeather", "One or more fields not found in the JSON data");
            }
        }
    }
}
