package com.example.project3;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project3.R;

public class StartUp extends AppCompatActivity {
    //ecran d'acceuil

    MyDatabase mydb;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startup);
        mydb = new MyDatabase(this.getApplicationContext());
    }

    public void startGame(View view) {
        EditText tp = (EditText) findViewById (R.id.pseudo);
        String pseudo = tp.getText().toString();
        Log.i("Pseudo",pseudo);
        if (pseudo.equals("")){
            pseudo = "unknown";
        }
        mydb.insertData(pseudo,0);
        String id=mydb.getId(pseudo);
        Log.i("idyes",id);
        //mydb.readData();
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("id",id);
        this.startActivity(intent);
        finish();

    }
}