package com.example.lock0134.mobileguifinalproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by lock0134 on 2018-04-13.
 */

public class BusStopsDatabaseHelpder extends SQLiteOpenHelper {
    public final static String TABLE_NAME = "Stops";
    public final static String DATABASE_NAME = "STOPS";
    public final static int VERSION_NUM = 1;
    public final static String KEY_STOP = "stop";
    public final static String KEY_ID = "_ID";
    public final static String ACTIVITY_NAME = "OCTranspo";
    public final static String BUS_NUMBER = "bus number";

    public BusStopsDatabaseHelpder(Context ctx) {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "("+KEY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+KEY_STOP+ " TEXT, "+BUS_NUMBER+"INTEGER);");

        Log.i("BusStopsDatabaseHelpder", "Calling onCreate");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
        Log.i("BusStopDatabaseHelper", "Calling onUpgrade, oldVersion = "+ i + " newVersion = "+ i1);
    }
}
