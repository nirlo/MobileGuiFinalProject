package com.example.lock0134.mobileguifinalproject;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class QuizStart extends AppCompatActivity {
    ListView listView;
    List<Question> questions = new ArrayList<Question>();
    protected static final String ACTIVITY_NAME = "QuizStart";
    SQLiteDatabase db;
    boolean frameLayoutLoaded = false;
    QuizAdapter quizAdapter;
    boolean isTablet;
    AlertDialog dialog;
    Cursor c;
    Button importButton;
    QuestionFragment lastFrag;

    private class QuizAdapter extends ArrayAdapter<String> {
        public QuizAdapter(Context ctx) {
            super(ctx, 0);
        }

        public int getCount(){
            return questions.size();
        }

        public String getItem(int position) {
            return questions.get(position).getQuestion();
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = QuizStart.this.getLayoutInflater();

            View result = inflater.inflate(R.layout.question_row, parent ,false);
            TextView message = (TextView)result.findViewById(R.id.questionText);
            message.setText(getItem(position));
            return result;


        }

        public long getId(int position){
            return position;
        }

        public long getItemId(int position){
            c = db.rawQuery("select * from question", null);
            if(c.getCount() == 0){
                return 0;
            }
            c.moveToFirst();
            if(!c.isAfterLast())
                c.moveToPosition(position);

            return c.getLong(c.getColumnIndex("ID"));
        }


    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_start);

        final FrameLayout fl = (FrameLayout) findViewById(R.id.frameLayout);

        if((fl != null)){
            frameLayoutLoaded = true;
        }

        importButton = (Button) findViewById(R.id.importQuestion);

        importButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(QuizStart.this);
                builder.setTitle("Import Question");
                LayoutInflater inflater = getLayoutInflater();
                final View dView = inflater.inflate(R.layout.import_question, null);
                builder.setView(dView);

                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        URL url = null;
                        try {
                            url = new URL("http://torunski.ca/CST2335/QuizInstance.xml");
                        }catch(MalformedURLException e){

                        }
                        XMLImporter importQuestion = new XMLImporter(url);
                        ProgressBar pb = (ProgressBar) dView.findViewById(R.id.progressBar);
                        importQuestion.setpBar(pb);
                        importQuestion.execute();
                        while(pb.getProgress() != 100){
                            try {
                                Thread.sleep(50);
                            }catch(InterruptedException e){

                            }
                        }
                        Snackbar sb = Snackbar.make(getWindow().getDecorView(), "Imported Questions", Snackbar.LENGTH_SHORT);
                        sb.show();
                        questions.addAll(importQuestion.getQuestions());
                        addRow();

                    }
                });

                builder.setNegativeButton("Back", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

                dialog = builder.create();
                dialog.show();

            }
        });

        quizAdapter = new QuizAdapter( this );
        QuestionDatabaseHelper dbHelper = new QuestionDatabaseHelper(this);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        db = dbHelper.getWritableDatabase();


        c = db.rawQuery("select * from question", null);
        if (c.moveToFirst()) {


            while (!c.isAfterLast()) {

                questions.add(new Question(c.getInt(c.getColumnIndex(QuestionDatabaseHelper.KEY_ID)),
                        c.getString(c.getColumnIndex(QuestionDatabaseHelper.KEY_TEXT)),
                        new ArrayList(),
                        c.getString(c.getColumnIndex(QuestionDatabaseHelper.KEY_RIGHT)),
                        c.getString(c.getColumnIndex(QuestionDatabaseHelper.KEY_TYPE)) ,
                        c.getInt((c.getColumnIndex(QuestionDatabaseHelper.KEY_DIGIT)))));
                String ans = c.getString(c.getColumnIndex(QuestionDatabaseHelper.KEY_ANSWER));
                try {
                    String[] a = ans.split(",");
                    for(String one : a){
                        questions.get(questions.size()-1).getAnswers().add(one);
                    }
                }catch(NullPointerException e){

                }


                c.moveToNext();
            }
        }
        listView = (ListView) findViewById(R.id.questionView);
        listView.setAdapter(quizAdapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                QuestionFragment frag = new QuestionFragment();
                String type = questions.get(position).getType();
                Bundle bundle = new Bundle();
                switch(type) {
                    case "numeric":
                        bundle.putString("type", "numeric");
                        frag.setChoice("numeric");
                        break;
                    case "multipleChoice":
                        bundle.putString("type", "multipleChoice");
                        frag.setChoice("multipleChoice");
                        break;
                    case "trueFalse":
                        bundle.putString("type", "trueFalse");
                        frag.setChoice("trueFalse");
                }
                bundle.putBoolean("exist", true);
                bundle.putSerializable("frag", frag);
                bundle.putSerializable("question" , questions.get(position));

                if (frameLayoutLoaded) {
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    if(lastFrag!= null){
                        lastFrag.saveFrag();
                        ft.remove(lastFrag);
                        addRow();
                    }

                    while (fm.getBackStackEntryCount() > 0) {
                        fm.popBackStackImmediate();
                    }
                    frag.setArguments(bundle);
                    frag.setIsTablet(true);
                    frag.setQuizStart(QuizStart.this);
                    lastFrag = frag;
                    ft.add(R.id.frameLayout, frag).commit();



                } else {

                    Intent intent = new Intent(getApplicationContext(), QuestionDetails.class);
                    intent.putExtras(bundle);
                    frag.setIsTablet(false);




                    QuizStart.this.startActivityForResult(intent, 50);


                }

            }
    });

    }

    public boolean onCreateOptionsMenu (Menu m) {
        getMenuInflater().inflate(R.menu.quiz_toolbar, m );
        return true;

    }
    public boolean onOptionsItemSelected(MenuItem mi) {
        int id = mi.getItemId();
        switch(id){
            case R.id.action_add:

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("What type of question do you want to create?");
                LayoutInflater inflater = this.getLayoutInflater();
                final View dView = inflater.inflate(R.layout.dialogQuestion, null);
                builder.setView(dView);

                builder.setNegativeButton("Back", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

                dialog = builder.create();
                dialog.show();


                final Button numeric = (Button) dView.findViewById(R.id.numeric);
                final Button multipleChoice = (Button) dView.findViewById(R.id.multipleChoice);
                final Button trueFalse = (Button) dView.findViewById(R.id.trueFalse);

                setQuestionButtonListener(numeric);
                setQuestionButtonListener(multipleChoice);
                setQuestionButtonListener(trueFalse);


                break;
            case R.id.action_remove:
                SparseBooleanArray checkedItemPositions = listView.getCheckedItemPositions();
                int itemCount = listView.getCount();

                for(int i=itemCount-1; i >= 0; i--){
                    if(checkedItemPositions.get(i)){
                        quizAdapter.remove(questions.get(i).getQuestion());
                    }
                }
                checkedItemPositions.clear();
                quizAdapter.notifyDataSetChanged();

                break;
            case R.id.stats:
                Intent intent = new Intent(getApplicationContext(), QuestionStats.class);
                Bundle b = new Bundle();
                b.putString("longest" , QuestionCalc.getLongest(questions).getQuestion());
                b.putString("shortest" , QuestionCalc.getShortest(questions).getQuestion());
                b.putInt("total" , QuestionCalc.getTotal(questions));
                b.putDouble("average" , QuestionCalc.getAverage(questions));
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                if (lastFrag != null) {
                    ft.remove(lastFrag);
                }
                intent.putExtras(b);
                startActivity(intent);



                break;
            case R.id.about:
                AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
                builder2.setTitle("About");
                LayoutInflater inflater2 = this.getLayoutInflater();
                final View dView2 = inflater2.inflate(R.layout.question_about, null);
                builder2.setView(dView2);

                builder2.setNegativeButton("Back", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

                dialog = builder2.create();
                dialog.show();

        }
        return true;
    }

    private void setQuestionButtonListener(final Button b){
         b.setOnClickListener(new View.OnClickListener()  {

            @Override
            public void onClick(View v) {
                int id = b.getId();


                QuestionFragment frag = new QuestionFragment();
                Question q = new Question(-1,
                        "", new ArrayList(),
                        "",
                        "", 0);
                Bundle bundle = new Bundle();
                switch (id) {
                    case R.id.numeric:
                        bundle.putString("type", "numeric");
                        frag.setChoice("numeric");
                        break;
                    case R.id.multipleChoice:
                        bundle.putString("type", "multipleChoice");
                        frag.setChoice("multipleChoice");
                        break;
                    case R.id.trueFalse:
                        bundle.putString("type", "trueFalse");
                        frag.setChoice("trueFalse");
                }




                    if (frameLayoutLoaded) {
                        bundle.putBoolean("exist", false);
                        bundle.putSerializable("frag", frag);
                        bundle.putSerializable("question", q);
                        FragmentManager fm = getFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        if (lastFrag != null) {
                            ft.remove(lastFrag);
                        }

                        while (fm.getBackStackEntryCount() > 0) {
                            fm.popBackStackImmediate();
                        }
                        frag.setArguments(bundle);
                        frag.setIsTablet(true);
                        frag.setQuizStart(QuizStart.this);
                        lastFrag = frag;
                        ft.add(R.id.frameLayout, frag).commit();


                    } else {
                        Intent intent = new Intent(QuizStart.this, QuestionDetails.class);
                        frag.setIsTablet(false);



                        bundle.putSerializable("question", q);
                        bundle.putSerializable("frag", frag);
                        intent.putExtras(bundle);



                        QuizStart.this.startActivityForResult(intent, 50);

                    }
                    dialog.dismiss();


            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int responseCode, Intent data){

        if(requestCode == 50 && responseCode == 50 && data.getBooleanExtra("delete" , false)){
            deleteRow(((Question)data.getSerializableExtra("question")).getID());
        }
        if(requestCode == 50 && responseCode == 51 && data.getBooleanExtra("add" , false)){
            questions.add((Question)(data.getSerializableExtra("question")));
            addRow();
        }
        if(requestCode == 50 && responseCode == 50 && data.getBooleanExtra("modify" , false)){
            modifyRow((Question)(data.getSerializableExtra("question")));
        }
    }

    public void deleteRow(long id){
        Question deleteQuestion = null;

        for(Question q : questions){
            if(q.getID() == -1){
                return;
            }
            if(q.getID() == id){
                deleteQuestion = q;
                break;
            }
        }

        questions.clear();
        db.execSQL("DELETE FROM " + QuestionDatabaseHelper.TABLE_NAME + " WHERE " + "id" + "= '" + id + "'");

        c = db.rawQuery("select * from question", null);
        if (c.moveToFirst()) {


            while (!c.isAfterLast()) {

                questions.add(new Question(c.getInt(c.getColumnIndex(QuestionDatabaseHelper.KEY_ID)),
                        c.getString(c.getColumnIndex(QuestionDatabaseHelper.KEY_TEXT)),
                        new ArrayList(),
                        c.getString(c.getColumnIndex(QuestionDatabaseHelper.KEY_RIGHT)),
                        c.getString(c.getColumnIndex(QuestionDatabaseHelper.KEY_TYPE)),
                        c.getInt((c.getColumnIndex(QuestionDatabaseHelper.KEY_DIGIT)))));
                String ans = c.getString(c.getColumnIndex(QuestionDatabaseHelper.KEY_ANSWER));
                try {
                    String[] a = ans.split(",");
                    for (String one : a) {
                        questions.get(questions.size()-1).getAnswers().add(one);
                    }
                }catch(NullPointerException e){

                }

                c.moveToNext();
            }
        }
        questions.remove(deleteQuestion);
        listView.setAdapter(quizAdapter);
        quizAdapter.notifyDataSetChanged();
        Toast toast = Toast.makeText(this, "Question Deleted", Toast.LENGTH_LONG);
        toast.show();


        quizAdapter.notifyDataSetChanged();
    }

    public String convertListToCommaString(List<String> list){
        String ss = "";
        if(list.size() == 1){
            return list.get(0);
        }
        for(String s : list){
            if(!(list.lastIndexOf(s) == list.size() - 1)){
                ss += s + ",";
            }
            else{
                ss += s;

            }
        }

        return ss;
    }

    public void addRow(Question q){
        questions.add(q);
        addRow();
    }

    public void addRow(){

        List<ContentValues> cvList = new ArrayList<>();
        List<Question> qNew = new ArrayList<>();
        int questionCount = 0;
        for(Question q : questions){
            if(q.getID() == -1){
                ContentValues cv = new ContentValues();
                cv.put(QuestionDatabaseHelper.KEY_TEXT , q.getQuestion() );
                cv.put(QuestionDatabaseHelper.KEY_RIGHT , q.getRightAnswer() );
                cv.put(QuestionDatabaseHelper.KEY_TYPE , q.getType() );
                cv.put(QuestionDatabaseHelper.KEY_DIGIT , q.getDigits() );
                cv.put(QuestionDatabaseHelper.KEY_ANSWER ,convertListToCommaString(q.getAnswers()));
                cvList.add(cv);
                qNew.add(q);
                questionCount++;
            }

        }

        for(ContentValues cv: cvList)
        db.insertOrThrow(QuestionDatabaseHelper.TABLE_NAME , QuestionDatabaseHelper.KEY_TEXT , cv);

        Cursor c = db.rawQuery("select * from question", null);

        c.moveToPosition(c.getCount() - questionCount);
        int h = 0;
        while (!c.isAfterLast()) {

            try {
                qNew.get(h).setID((int) c.getLong(c.getColumnIndex(QuestionDatabaseHelper.KEY_ID)));
            } catch (NullPointerException e) {

            }
            h++;
            c.moveToNext();
        }

    questions.clear();

        c = db.rawQuery("select * from question", null);
        if (c.moveToFirst()) {


            while (!c.isAfterLast()) {

                questions.add(new Question(c.getInt(c.getColumnIndex(QuestionDatabaseHelper.KEY_ID)),
                        c.getString(c.getColumnIndex(QuestionDatabaseHelper.KEY_TEXT)),
                        new ArrayList(),
                        c.getString(c.getColumnIndex(QuestionDatabaseHelper.KEY_RIGHT)),
                        c.getString(c.getColumnIndex(QuestionDatabaseHelper.KEY_TYPE)),
                        c.getInt((c.getColumnIndex(QuestionDatabaseHelper.KEY_DIGIT)))));
                String ans = c.getString(c.getColumnIndex(QuestionDatabaseHelper.KEY_ANSWER));
                try {
                    String[] a = ans.split(",");
                    for (String one : a) {
                        questions.get(questions.size()-1).getAnswers().add(one);
                    }
                }catch(NullPointerException e){

                }

                c.moveToNext();
            }
        }
        listView.setAdapter(quizAdapter);
        quizAdapter.notifyDataSetChanged();
        Toast toast = Toast.makeText(this, "Question Added", Toast.LENGTH_LONG);
        toast.show();
    }

    public void modifyRow(Question q){
        ContentValues cv = new ContentValues();
        cv.put(QuestionDatabaseHelper.KEY_TEXT , q.getQuestion() );
        cv.put(QuestionDatabaseHelper.KEY_RIGHT , q.getRightAnswer() );
        cv.put(QuestionDatabaseHelper.KEY_TYPE , q.getType() );
        cv.put(QuestionDatabaseHelper.KEY_DIGIT , q.getDigits() );
        cv.put(QuestionDatabaseHelper.KEY_ANSWER ,convertListToCommaString(q.getAnswers()));
        db.update(QuestionDatabaseHelper.TABLE_NAME , cv , "id="+q.getID() ,null);
        Toast toast = Toast.makeText(this, "Question Saved", Toast.LENGTH_LONG);
        toast.show();
        listView.setAdapter(quizAdapter);
        quizAdapter.notifyDataSetChanged();
    }
}
