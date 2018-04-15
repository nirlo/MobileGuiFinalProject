package com.example.lock0134.mobileguifinalproject;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.Serializable;

/**
 * Created by William on 2018-04-06.
 */

public class QuestionFragment extends Fragment implements Serializable {
    boolean isTablet;
    Question q;
     Button deleteButton;
     Button addButton;
     String choice;
     QuizStart qs;

    EditText question;
     EditText answer1;
     EditText answer2;
     EditText answer3;
     EditText answer4;
     EditText correctAnswer;
     EditText digits;
     View view;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        final Boolean exist = getArguments().getBoolean("exist");

        deleteButton = (Button) view.findViewById(R.id.deleteQuestion);
        addButton = (Button) view.findViewById(R.id.addQuestion);

        if(exist) {
            addButton.setText("Save");
        }

        deleteButton.setOnClickListener(new View.OnClickListener()  {

            @Override
            public void onClick(View v) {
                if(isTablet) {
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.remove(QuestionFragment.this).commit();
                    qs.deleteRow(q.getID());
                }
                else {
                    Intent intent = new Intent();
                    intent.putExtra("delete", true);
                    intent.putExtra("question", q);

                    getActivity().setResult(50, intent);
                    getActivity().finish();
                }
            }


        });

        addButton.setOnClickListener(new View.OnClickListener()  {

            @Override
            public void onClick(View v) {

                question = view.findViewById(R.id.question);
                answer1 = view.findViewById(R.id.answer1);
                answer2 = view.findViewById(R.id.answer2);
                answer3 = view.findViewById(R.id.answer3);
                answer4 = view.findViewById(R.id.answer4);
                digits = view.findViewById(R.id.digits);
        if(exist){
            saveFrag();
        }
        else {
            q.setQuestion(question.getText().toString());
            try {
                q.setRightAnswer(((EditText) view.findViewById(R.id.answerRight)).getText().toString());
            } catch (NullPointerException e) {

            }

            try {
                q.getAnswers().add(((EditText) view.findViewById(R.id.answer1)).getText().toString());
                q.getAnswers().add(((EditText) view.findViewById(R.id.answer2)).getText().toString());
                q.getAnswers().add(((EditText) view.findViewById(R.id.answer3)).getText().toString());
                q.getAnswers().add(((EditText) view.findViewById(R.id.answer4)).getText().toString());
            } catch (NullPointerException e) {

            }
            try {
                q.setDigits(Integer.parseInt(digits.getText().toString()));
            } catch (NullPointerException e) {

            }


            if (isTablet) {
                qs.addRow(q);
            } else {
                Intent intent = new Intent();
                intent.putExtra("add", true);
                intent.putExtra("question", q);
                getActivity().setResult(51, intent);
                getActivity().finish();
            }


        }
            }


        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        q = (Question) (getArguments().getSerializable("question"));
        switch(choice){
            case "numeric":view = inflater.inflate(R.layout.numeric_fragment, container, false);
            q.setType("numeric"); break;
            case "multipleChoice": view = inflater.inflate(R.layout.multiple_choice_fragment, container, false);
                q.setType("multipleChoice"); break;
            case "trueFalse": view = inflater.inflate(R.layout.true_false_fragment, container, false);
                q.setType("trueFalse");
        }
        question = view.findViewById(R.id.question);
        answer1 = view.findViewById(R.id.answer1);
        answer2 = view.findViewById(R.id.answer2);
        answer3 = view.findViewById(R.id.answer3);
        answer4 = view.findViewById(R.id.answer4);
        correctAnswer = view.findViewById(R.id.answerRight);
        digits = view.findViewById(R.id.digits);



        try {
        correctAnswer.setText(q.getRightAnswer());
        }catch(NullPointerException e){

        }
        try{
        question.setText(q.getQuestion());
    }catch(NullPointerException e){

    }
        try {
            answer1.setText(q.getAnswers().get(0));
            answer2.setText(q.getAnswers().get(1));
            answer3.setText(q.getAnswers().get(2));
            answer4.setText(q.getAnswers().get(3));
        }catch(NullPointerException e){


        }catch(IndexOutOfBoundsException e2){

    }
        try{
        digits.setText(Integer.toString(q.getDigits()));
    }catch(NullPointerException e){

    }
        return view;
    }


    public void setIsTablet(boolean isTablet){
        this.isTablet = isTablet;
    }

    public void setChoice(String choice){
        this.choice = choice;
    }

    public void setQuizStart(QuizStart qs){
        this.qs = qs;
    }

    public void saveFrag(){

        q.setQuestion(question.getText().toString());
        try{
            q.setRightAnswer(((EditText)view.findViewById(R.id.answerRight)).getText().toString());
        }catch(NullPointerException e){

        }

        try {
            q.getAnswers().add(((EditText)view.findViewById(R.id.answer1)).getText().toString());
            q.getAnswers().add(((EditText)view.findViewById(R.id.answer2)).getText().toString());
            q.getAnswers().add(((EditText)view.findViewById(R.id.answer3)).getText().toString());
            q.getAnswers().add(((EditText)view.findViewById(R.id.answer4)).getText().toString());
        }catch(NullPointerException e){

        }
        try{
            q.setDigits(Integer.parseInt(digits.getText().toString()));
        }catch(NullPointerException e){

        }



        if(isTablet) {
            qs.modifyRow(q);
        }
        else {
            Intent intent = new Intent();
            intent.putExtra("modify", true);
            intent.putExtra("question", q);
            getActivity().setResult(50, intent);
            getActivity().finish();
        }

    }


}


