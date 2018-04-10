package com.example.lock0134.mobileguifinalproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Alec on 2018-04-08.
 */

public class MovieDatabaseHelper extends SQLiteOpenHelper {
    private static String DATABASE_NAME = "movieDatabase.db";
    private static int VERSION_NUM = 1;
    public static String MOVIE_TABLE = "movieInfo";
    public static final String KEY_ID = "ID";
    public static final String KEY_DATA = "MOVIEDATA";
    public static final String KEY_ACTORS = "MOVIEACTORS";
    public static final String KEY_LENGTH = "MOVIELENGTH";
    public static final String KEY_DESC = "MOVIEDESC";
    public static final String KEY_RATE = "MOVIERATE";
    public static final String KEY_GENRE = "MOVIEGENRE";
    public static final String KEY_URL = "MOVIEURL";
    protected static final String ACTIVITY_NAME = "MovieDatabaseHelper";

    public MovieDatabaseHelper(Context ctx)
    {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + MOVIE_TABLE + " (" +KEY_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, " +KEY_DATA+ " TEXT, " +KEY_ACTORS+ " TEXT, " +KEY_LENGTH+ " TEXT, "
                +KEY_DESC+ " TEXT, " +KEY_RATE+ " TEXT, " +KEY_GENRE+ " TEXT, " +KEY_URL+ " TEXT " +");");
        Log.i(ACTIVITY_NAME, "Calling onCreate");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " +MOVIE_TABLE);
        onCreate(db);
    }
}
