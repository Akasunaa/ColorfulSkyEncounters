package com.example.project3;

import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

class SortPair implements Comparator<Pair<String, Integer>>{
    public int compare(Pair<String, Integer> a, Pair<String, Integer> b){
        return Integer.parseInt(String.valueOf(b.second)) - Integer.parseInt(String.valueOf(a.second));
    }
}

public class GameOver extends AppCompatActivity {

    //les points affiche
    //TextView tvPoints;
    MyDatabase mydb;
    List<Pair<String,Integer>> listScore;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_over);
        mydb=new MyDatabase(this.getApplicationContext());
        listScore=mydb.readData();
        Collections.sort(listScore,new SortPair());

        //int points = getIntent().getExtras().getInt("points");
        //tvPoints = findViewById(R.id.tvPoints);
        //tvPoints.setText("" + points);

        ListView list = (ListView)findViewById(R.id.listView);
        ArrayAdapter<String> tableau = new ArrayAdapter<String>(list.getContext(), R.layout.text_highscore);
        int limit = Math.min(listScore.size(),20);
        for (int i=0; i<limit; i++) {
            String ligne = listScore.get(i).first + " : " + listScore.get(i).second + " "+ "Point(s)";
            tableau.add(ligne); }
        list.setAdapter(tableau);

    }

    public void restart(View view) {
        Intent intent = new Intent(GameOver.this, StartUp.class);
        startActivity(intent);
        finish();
    }

    public void exit(View view) {
        finish();
    }
}