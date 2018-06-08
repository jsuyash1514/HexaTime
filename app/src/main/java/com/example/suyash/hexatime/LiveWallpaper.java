package com.example.suyash.hexatime;


import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.provider.AlarmClock;
import android.service.wallpaper.WallpaperService;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.os.Handler;
import java.util.Date;

/**
 * Created by suyash on 6/7/18.
 */

public class LiveWallpaper extends WallpaperService{
    static float x,y;
    String curTime;
    @Override
    public Engine onCreateEngine() {
        return new WallpaperEngine();
    }

    private class WallpaperEngine extends Engine{
        private final Handler handler = new Handler();
        private final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                draw();
            }
        };

        private Paint paint = new Paint();
        int width, height;
        boolean visible = true;

        public WallpaperEngine(){

            paint.setAntiAlias(true);
            paint.setColor(Color.parseColor("#ffffff"));
            paint.setStyle(Paint.Style.FILL);
            paint.setTextSize(120);

            handler.post(runnable);
        }

        @Override
        public void onVisibilityChanged(boolean visible){
            this.visible = visible;
            if(visible){
                handler.post(runnable);
            }else {
                handler.removeCallbacks(runnable);
            }
        }

        @Override
        public  void onSurfaceDestroyed(SurfaceHolder holder){
            super.onSurfaceDestroyed(holder);
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder,int format,int width,int height){
            this.width = width;
            this.height = height;
            super.onSurfaceChanged(holder,format,width,height);
        }

        @Override
        public void onTouchEvent(MotionEvent event){
            float X = event.getX();
            float Y = event.getY();
            Rect rect = new Rect();
            paint.getTextBounds(curTime,0,curTime.length(),rect);
            if (rect.contains((int)(X-x),(int)(Y-y))) {
                Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
            super.onTouchEvent(event);
        }

        private void draw(){
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

            curTime = h + ":" + m + ":" + s;
            int color = Color.parseColor("#" + h + m + s);

            SurfaceHolder holder = getSurfaceHolder();
            Canvas canvas = holder.lockCanvas();
            if (canvas != null){
                canvas.drawColor(color);
                x = (canvas.getWidth() / 2) - (paint.measureText(curTime) / 2);
                y = (canvas.getHeight() / 2) - ((paint.ascent() + paint.descent()) / 2);
                canvas.drawText(curTime,x,y,paint);
                holder.unlockCanvasAndPost(canvas);
            }
            handler.removeCallbacks(runnable);
            if (visible){
                handler.postDelayed(runnable,1000);
            }
        }
    }
}
