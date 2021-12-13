package com.example.project3;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class MyDatabase extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    //les noms de con bonjour
    private static final String DATABASE_FILE_NAME = "mydatabase";
    private static final String DATABASE_TABLE_NAME = "mydatabase";
    private static final String PKEY = "pkey";
    private static final String COL1 = "col1";
    private static final String COL2 = "col2";

    MyDatabase(Context context) {
        super(context, DATABASE_FILE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String DATABASE_TABLE_CREATE = "CREATE TABLE " + DATABASE_TABLE_NAME + " (" +
                PKEY + " INTEGER PRIMARY KEY," +
                COL1 + " PSEUDO," +
                COL2 + " SCORE" +")";
        db.execSQL(DATABASE_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertData(String pseudo,int score)
    {
        Log.i("JFL"," Insert in database");
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        ContentValues values = new ContentValues();
        values.put(COL1,pseudo);
        values.put(COL2,score);
        db.insert(DATABASE_TABLE_NAME,null, values);
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    @SuppressLint("Range")
    public List<Pair<String,Integer>> readData()
    {
        List<Pair<String,Integer>> list = new ArrayList<Pair<String, Integer>>();
        String select = new String("SELECT * from " + DATABASE_TABLE_NAME);
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(select, null);
        Log.i("JFL", "Number of entries: " + cursor.getCount());
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                Log.i("JFL", "Reading: " + cursor.getString((cursor).getColumnIndex(PKEY)) + cursor.getString(cursor.getColumnIndex(COL1))+cursor.getString(cursor.getColumnIndex(COL2)));
                Pair pair = Pair.create(cursor.getString(cursor.getColumnIndex(COL1)),cursor.getString(cursor.getColumnIndex(COL2)));
                list.add(pair);
            } while (cursor.moveToNext());
        }
        return list;
    }

    public boolean update(String id, int score) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("UPDATE "+DATABASE_TABLE_NAME+" SET col2 = "+"'"+score+"' "+ "WHERE pkey = "+"'"+id+"'");
        return true;
    }

    @SuppressLint("Range")
    public String getId(String pseudo)
    {
        String select = new String("SELECT PKEY FROM " + DATABASE_TABLE_NAME + " WHERE COL1 = " + "'" + pseudo+ "'");

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(select, null);
        Log.i("JFL", "Number of entries: " + cursor.getCount());
        cursor.moveToLast();
        return cursor.getString((cursor).getColumnIndex(PKEY));

    }


}