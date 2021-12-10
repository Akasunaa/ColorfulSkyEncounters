package com.example.project3;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.graphics.drawable.VectorDrawable;
import android.os.Build;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.larvalabs.svgandroid.SVG;

import java.util.Random;
import java.util.concurrent.ExecutionException;

public class EnemySpaceship {
    Context context;
    Bitmap bitmapEnemy;
    int ex, ey;
    int enemyVelocity;
    Random random;
    SVG svgEnemy;
    String color;


    public EnemySpaceship(Context context,String color) {
        this.color=color;
        //prend le svg
        this.context = context;
        //look for svg yes
        AsyncEnemy asyncEnemy = new AsyncEnemy(color);
        asyncEnemy.execute();
        try {
            svgEnemy =  asyncEnemy.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        PictureDrawable pictureDrawable = svgEnemy.getDrawable();
        bitmapEnemy = Bitmap.createBitmap(pictureDrawable.getIntrinsicWidth(), pictureDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

        random = new Random();
        int[] spawn = new int[]{250,450,650,850};
        ex = spawn[random.nextInt(4)];
        ey = 0;
        enemyVelocity = 15+random.nextInt(5);
    }

    public SVG getSvgEnemy(){
        return svgEnemy;
    }

    public void reset(){
        this.ey=0;

    }

    public Bitmap getBitmapEnemy(){
        return bitmapEnemy;
    }

    int getEnemySpaceshipWidth(){
        return bitmapEnemy.getWidth();
    }

    int getEnemySpaceshipHeight(){
        return bitmapEnemy.getHeight();
    }
}