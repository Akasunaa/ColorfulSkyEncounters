package com.example.project3;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Shot {
    //pour gerer les tirs
    Bitmap shot;
    Context context;
    int shx, shy;
    String color;

    public Shot(Context context, int shx, int shy,String color) {
        this.context = context;
        this.color=color;
        if (color=="blue"){
            shot = BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.shotblue);
        }else if (color=="yellow"){
            shot = BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.shotyellow);
        }else if(color=="green"){
            shot = BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.shotgreen);
        }else{
            shot = BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.shotred);
        }

        shot = shot.createScaledBitmap(shot, 50, 100,true);
        this.shx = shx;
        this.shy = shy;
    }



    public Bitmap getShot(){
        return shot;
    }
    public int getShotWidth() {
        return shot.getWidth();
    }
    public int getShotHeight() {
        return shot.getHeight();
    }
}