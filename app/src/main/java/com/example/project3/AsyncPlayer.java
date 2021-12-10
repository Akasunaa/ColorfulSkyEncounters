package com.example.project3;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGBuilder;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class AsyncPlayer extends AsyncTask<Void, Void, SVG> {

    //Me retourne un svg du player
    SVG svg;



    public AsyncPlayer() {

    }



    protected void onPreExecute() {

    }

    @Override
    protected SVG doInBackground(Void... voids) {
        Bitmap bm = null;
        //recherche du svg
        String urlString = "https://app.pixelencounter.com/api/basic/svgmonsters?primaryColor=orange" ;
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
