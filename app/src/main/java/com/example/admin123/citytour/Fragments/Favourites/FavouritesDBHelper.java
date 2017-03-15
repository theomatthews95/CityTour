package com.example.admin123.citytour.Fragments.Favourites;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by theom on 15/03/2017.
 */

public class FavouritesDBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "recent_locations.db";
    public static final String TABLE_NAME = "recent_locations_table";
    public static final String COL_1 = "NAME";
    public static final String COL_2 = "LATITUDE";
    public static final String COL_3 = "LONGITUDE";
    public static final String COL_4 = "TIMESTAMP";

    public FavouritesDBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Create table with appropriate column headers
        db.execSQL("create table "+ TABLE_NAME + "(NAME TEXT PRIMARY KEY,LATITUDE REAL,LONGITUDE REAL, TIMESTAMP DATETIME DEFAULT CURRENT_TIMESTAMP)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Delete table if it already exists
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    //Function to insert data into the DB
    public boolean insertData(String name, Double latitude, Double longitude){
        SQLiteDatabase db = this.getWritableDatabase();

        try{
            //Take user paramas and insert into DB
            ContentValues contentValues = new ContentValues();
            contentValues.put(COL_1, name);
            contentValues.put(COL_2, latitude);
            contentValues.put(COL_3, longitude);

            //If insert failed, return false, else true
            long result = db.insertOrThrow(TABLE_NAME, null, contentValues);
            if (result == -1) {
                return false;
            }else {
                return true;
            }
        }catch (SQLiteConstraintException e){
            return false;
        }
    }

    //Return all data from the DB
    public Cursor getAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME, null);
        return res;
    }
}
