package com.example.suyash.hexatime;

import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    public TextView textView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        textView= (TextView)findViewById(R.id.textView);
        textView.setVisibility(View.VISIBLE);


        Thread thread = null;
        Runnable runnable = new CountDownRunner();
        thread= new Thread(runnable);
        thread.start();

        Button button = (Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Log.d("Button Clicked: ", "Starting new Activity");
                    Intent intent = new Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
                    intent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT, new ComponentName(MainActivity.this, LiveWallpaper.class));
                    startActivity(intent);
                }
                catch (Exception e){
                    Log.d("Button Clicked: " , "Exception: " + e);
                }
            }
        });

    }

    public void time() {
        runOnUiThread(new Runnable() {
            public void run() {
                try{
                    textView= (TextView)findViewById(R.id.textView);
                    Date date = new Date();
                    int hours = date.getHours();
                    int minutes = date.getMinutes();
                    int seconds = date.getSeconds();
                    String h = hours + "";
                    if (hours/10 == 0) h = "0" + hours;
                    String m = minutes + "";
                    if (minutes/10 == 0) m = "0" + minutes;
                    String s = seconds + "";
                    if (seconds/10 == 0) s = "0" + seconds;
                    String curTime;
                    curTime = h + ":" + m + ":" + s;
                    textView.setText(curTime);
                    View view = getWindow().getDecorView();
                    view.setBackgroundColor(Color.parseColor("#" + h + m + s));
                }catch (Exception e) {
                    Log.d("Exception: ", "" + e);
                }
            }
        });
    }


    class CountDownRunner implements Runnable{
        public void run() {
            while(!Thread.currentThread().isInterrupted()){
                try {
                    time();
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }catch(Exception e){
                    Log.d("Exception: ", "" + e);
                }
            }
        }
    }

}
