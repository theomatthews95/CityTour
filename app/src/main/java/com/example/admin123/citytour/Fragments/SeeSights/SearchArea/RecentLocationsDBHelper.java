package com.example.admin123.citytour.Fragments.SeeSights.SearchArea;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static com.google.android.gms.wearable.DataMap.TAG;

/**
 * Created by theom on 05/03/2017.
 */

public class RecentLocationsDBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "recent_locations.db";
    public static final String TABLE_NAME = "recent_locations_table";
    //public static final String COL_1 = "ID";
    public static final String COL_1 = "NAME";
    public static final String COL_2 = "LATITUDE";
    public static final String COL_3 = "LONGITUDE";
    public static final String COL_4 = "TIMESTAMP";


    public RecentLocationsDBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //db.execSQL("create table "+ TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT,"+"("+COL_2+")"+"("+COL_3+")");
        //db.execSQL("create table "+ TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT,NAME TEXT,LATITUDE REAL,LONGITUDE REAL)");
        db.execSQL("create table "+ TABLE_NAME + "(NAME TEXT PRIMARY KEY,LATITUDE REAL,LONGITUDE REAL, TIMESTAMP DATETIME DEFAULT CURRENT_TIMESTAMP)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean insertData(String name, Double latitude, Double longitude){
        SQLiteDatabase db = this.getWritableDatabase();

        try{
           /* boolean isRecordDuplicate = CheckIsDataAlreadyInDBorNot(TABLE_NAME, COL_1, name);
            if (isRecordDuplicate == true){
                deleteValue(name);
            }*/
            ContentValues contentValues = new ContentValues();
            contentValues.put(COL_1, name);
            contentValues.put(COL_2, latitude);
            contentValues.put(COL_3, longitude);

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

    public Cursor getAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME, null);
        return res;
    }

   /* public Integer deleteValue(String duplicate){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "NAME = ?", new String[]{duplicate});
    }*/

    public Cursor getRecent5(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from (select * from recent_locations_table order by timestamp DESC limit 10) order by timestamp DESC;", null);
        return res;
    }

   /* public boolean CheckIsDataAlreadyInDBorNot(String TableName, String dbfield, String fieldValue) {
        SQLiteDatabase db = this.getWritableDatabase();
        String Query = "Select * from " + TableName + " where " + dbfield + " =? ";
        Cursor cursor = db.rawQuery(Query, new String[]{fieldValue});
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }*/
}

