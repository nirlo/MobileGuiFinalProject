package com.example.lock0134.mobileguifinalproject;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class QuizStart extends AppCompatActivity {
    ListView listView;
    List<String> questions = new ArrayList<String>();
    protected static final String ACTIVITY_NAME = "QuizStart";
    SQLiteDatabase db;
    boolean frameLayoutLoaded = false;
    QuizAdapter quizAdapter;
    Cursor c;

    private class QuizAdapter extends ArrayAdapter<String> {
        public QuizAdapter(Context ctx) {
            super(ctx, 0);
        }

        public int getCount(){
            return questions.size();
        }

        public String getItem(int position) {
            return questions.get(position);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = QuizStart.this.getLayoutInflater();


            return inflater.inflate(R.layout.question_row, null);


        }

        public long getId(int position){
            return position;
        }

        public long getItemId(int position){
            /**c = db.rawQuery("select * from question", null);
            c.moveToFirst();
            if(!c.isAfterLast())
                c.moveToPosition(position);


            return c.getLong(c.getColumnIndex("ID"));**/
            return position;
        }


    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_start);
        questions.add("bram");
        questions.add("bram");
        questions.add("bram");
        quizAdapter = new QuizAdapter( this );
        QuestionDatabaseHelper dbHelper = new QuestionDatabaseHelper(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        db = dbHelper.getWritableDatabase();
        listView = (ListView) findViewById(R.id.questionView);
        listView.setAdapter(quizAdapter);
    }

    public boolean onCreateOptionsMenu (Menu m) {
        getMenuInflater().inflate(R.menu.quiz_toolbar, m );
        return true;

    }
    public boolean onOptionsItemSelected(MenuItem mi) {
        int id = mi.getItemId();
        switch(id){
            case R.id.action_one:
                Log.d("Toolbar", "Option 1 selected");


                break;
            case R.id.action_two:
                Log.d("Toolbar", "Option 2 selected");

                break;
            case R.id.action_three:
                Log.d("Toolbar", "Option 3 selected");





                break;
            case R.id.about:
                String textAbout = "Version 1.0, by William";

                Toast toast = Toast.makeText(this, textAbout, Toast.LENGTH_SHORT);
                toast.show();

        }
        return true;
    }
}
