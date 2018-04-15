package com.example.lock0134.mobileguifinalproject;

import android.util.Property;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by William on 2018-04-14.
 */

public class QuestionCalc {
    public static Question getLongest(List<Question> questions) {
        Collections.sort(questions, new SortByLength());
        return questions.get(questions.size()-1);
    }

    public static Question getShortest(List<Question> questions) {
        Collections.sort(questions, new SortByLength());
        return questions.get(0);
    }

    public static int getTotal(List<Question> questions) {
        return questions.size();
    }

    public static double getAverage(List<Question> questions) {
        int total = 0;
        for(Question q : questions){
            total += q.getQuestion().length();
        }
        return total / questions.size();
    }

    public static class SortByLength implements Comparator<Question> {


    @Override
    public int compare(Question o, Question t1) {
        return (o).getQuestion().length() - (t1).getQuestion().length();
    }
}

}
