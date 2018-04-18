package com.example.lock0134.mobileguifinalproject;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by William on 2018-04-03.
 */

public class QuestionStats extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_stats);

        TextView longest = findViewById(R.id.longestDisplay);
        TextView shortest = findViewById(R.id.shortestDisplay);
        TextView average = findViewById(R.id.averageDisplay);
        TextView total = findViewById(R.id.totalQuestionDisplay);

        longest.setText("Longest Question : " + getIntent().getExtras().getString("longest"));
        shortest.setText("Shortest Question : " + getIntent().getExtras().getString("shortest"));
        average.setText("Average Question Length : " + Double.toString(getIntent().getExtras().getDouble("average")));
        total.setText("Total Questions : " + Integer.toString(getIntent().getExtras().getInt("total")));
    }


}
