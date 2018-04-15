package com.example.lock0134.mobileguifinalproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by William on 2018-04-03.
 */

public class QuestionDatabaseHelper extends SQLiteOpenHelper{

        final private static String DATABASE_NAME = "message.db";
        final private static int VERSION_NUM = 16;
        final static String TABLE_NAME= "Question";
        final static String KEY_ID = "ID";
        final static String KEY_TEXT = "Text";
        final static String KEY_RIGHT = "Right";
        final static String KEY_ANSWER = "Answer";
        final static String KEY_TYPE = "Type";
        final static String KEY_DIGIT = "Digit";

    public QuestionDatabaseHelper(Context ctx) {
            super(ctx, DATABASE_NAME, null, VERSION_NUM);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.i("DatabaseHelper", "Calling onCreate");
            db.execSQL( "CREATE TABLE " + TABLE_NAME  + " ( " + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_TEXT + " text, " + KEY_TYPE + " text, " +
                   KEY_ANSWER + " text, " + KEY_RIGHT + " text, " + KEY_DIGIT + " INTEGER); ");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int i, int i1) {
            Log.i("DatabaseHelper", "Calling onUpgrade, oldVersion=" + i + " newVersion=" + i1);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }


}
