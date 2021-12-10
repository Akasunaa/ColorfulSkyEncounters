package com.example.project3;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.PictureDrawable;

import com.larvalabs.svgandroid.SVG;

import java.util.Random;
import java.util.concurrent.ExecutionException;

public class Player {
    Context context;
    Bitmap bitmapPlayer;
    int ox, oy;
    Random random;
    SVG svgPlayer;

    public Player(Context context) {
        //prend le svg
        this.context = context;
        AsyncPlayer asyncPlayer = new AsyncPlayer();
        asyncPlayer.execute();
        try {
            svgPlayer =  asyncPlayer.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        PictureDrawable pictureDrawable = svgPlayer.getDrawable();
        bitmapPlayer = Bitmap.createBitmap(pictureDrawable.getIntrinsicWidth(), pictureDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);



        random = new Random();
        ox = random.nextInt(Game.screenWidth);
        oy = Game.screenHeight -bitmapPlayer.getHeight();
    }

    public Bitmap getOurSpaceship(){
        return bitmapPlayer;
    }

    int getOurSpaceshipWidth(){
        return bitmapPlayer.getWidth();
    }
}