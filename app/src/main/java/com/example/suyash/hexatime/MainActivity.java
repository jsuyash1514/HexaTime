package com.example.suyash.hexatime;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Thread thread = null;
        Runnable runnable = new CountDownRunner();
        thread= new Thread(runnable);
        thread.start();

    }

    public void time() {
        runOnUiThread(new Runnable() {
            public void run() {
                try{
                    TextView textView= (TextView)findViewById(R.id.textView);
                    Date date = new Date();
                    int hours = date.getHours();
                    int minutes = date.getMinutes();
                    int seconds = date.getSeconds();
                    String curTime;
                    if (hours/10 == 0) curTime = "0" + hours + ":" + minutes + ":" + seconds;
                    else if (minutes/10 == 0) curTime = hours + ":" + "0" + minutes + ":" + seconds;
                    else if (seconds/10 == 0) curTime = hours + ":" + minutes + ":" + "0" + seconds;
                    else curTime = hours + ":" + minutes + ":" + seconds;
                    textView.setText(curTime);
                    View view = getWindow().getDecorView();
                    view.setBackgroundColor(Color.parseColor("#" + hours + minutes + seconds));
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
