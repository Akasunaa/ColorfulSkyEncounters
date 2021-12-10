package com.example.project3;

import com.larvalabs.svgandroid.*;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;


import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class AsyncEnemy extends AsyncTask<Void, Void, SVG> {

    //Me retourne un svg d'enemy
    SVG svg;
    String color;


    public AsyncEnemy(String color) {
        this.color=color;
    }



    protected void onPreExecute() {

    }

    @Override
    protected SVG doInBackground(Void... voids) {
        //recherche du svg
        Bitmap bm = null;
        String string = "https://app.pixelencounter.com/api/basic/svgmonsters?primaryColor="+color;

        String urlString = string;
        URL url = null;
        try {
            url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            svg = new SVGBuilder().readFromInputStream(in).build();


        } catch (IOException e) {
            e.printStackTrace();
        }
        return svg;
    }

    protected void onPostExecute(){

    }
}