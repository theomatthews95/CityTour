package com.example.admin123.citytour.Fragments.Favourites;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by theom on 15/03/2017.
 */

public class FavouritesDBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "favourite_locations.db";
    public static final String TABLE_NAME = "favourite_locations_table";
    public static final String COL_1 = "NAME";
    public static final String COL_2 = "LATITUDE";
    public static final String COL_3 = "LONGITUDE";
    public static final String COL_4 = "TIMESTAMP";
    public static final String COL_5 = "REFERENCE";
    public static final String COL_6 = "USERNOTES";

    public FavouritesDBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Create table with appropriate column headers
        db.execSQL("create table "+ TABLE_NAME + "("+COL_1+" TEXT PRIMARY KEY,"+COL_2+" REAL,"+COL_3+" REAL,"+COL_4+" DATETIME DEFAULT CURRENT_TIMESTAMP,"+COL_5+" TEXT,"+COL_6+" TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Delete table if it already exists
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    //Function to insert data into the DB
    public boolean insertData(String name, Double latitude, Double longitude, String reference, String userNotes){
        SQLiteDatabase db = this.getWritableDatabase();

        try{
            //Take user paramas and insert into DB
            ContentValues contentValues = new ContentValues();
            contentValues.put(COL_1, name);
            contentValues.put(COL_2, latitude);
            contentValues.put(COL_3, longitude);
            contentValues.put(COL_5, reference);
            contentValues.put(COL_6, userNotes);

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

    //Return all data from the DB
    public Integer deleteValue(String locationTitle){
        SQLiteDatabase db = this.getWritableDatabase();
        Integer deletedNum = db.delete(TABLE_NAME, "NAME = ?", new String[]{locationTitle});
        return deletedNum;
    }

    //Update user notes
    public Integer updateUserNotes(String locationTitle, Double latitude, Double longitude, String reference, String userNotes){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_1, locationTitle);
        cv.put(COL_2, latitude);
        cv.put(COL_3, longitude);
        cv.put(COL_5, reference);
        cv.put(COL_6, userNotes);

        Log.i("DB_Helper", "Update query: "+locationTitle+ " notes "+userNotes);
        Integer result = db.update(TABLE_NAME, cv, "NAME = ?", new String[]{locationTitle});
        return result;
    }
}
