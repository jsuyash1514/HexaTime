package com.example.suyash.hexatime;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;
import android.os.Handler;
import java.util.Date;

/**
 * Created by suyash on 6/7/18.
 */

public class LiveWallpaper extends WallpaperService{
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
            paint.setTextSize(80);
            paint.setDither(true);


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
            String curTime;
            curTime = h + ":" + m + ":" + s;
            int color = Color.parseColor("#" + h + m + s);

            SurfaceHolder holder = getSurfaceHolder();
            Canvas canvas = null;

            try {
                canvas = holder.lockCanvas();
                if (canvas != null){
                    canvas.drawColor(color);
                }
                float x = (canvas.getWidth() / 2) - (paint.measureText(curTime) / 2);
                float y = (canvas.getHeight() / 2) - ((paint.ascent() + paint.descent()) / 2);
                canvas.drawText(curTime,x,y,paint);
            }finally {
                if (canvas != null){
                    holder.unlockCanvasAndPost(canvas);
                }
            }
            handler.removeCallbacks(runnable);
            if (visible){
                handler.postDelayed(runnable,1000);
            }
        }
    }
}
