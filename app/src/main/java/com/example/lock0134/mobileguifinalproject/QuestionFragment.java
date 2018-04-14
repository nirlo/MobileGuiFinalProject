package com.example.lock0134.mobileguifinalproject;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
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
    public void onViewCreated(View view, Bundle savedInstanceState) {
        deleteButton = (Button) view.findViewById(R.id.deleteQuestion);
        addButton = (Button) view.findViewById(R.id.addQuestion);

        question = view.findViewById(R.id.question);
        answer1 = view.findViewById(R.id.answer1);
        answer2 = view.findViewById(R.id.answer2);
        answer3 = view.findViewById(R.id.answer3);
        answer4 = view.findViewById(R.id.answer4);
        correctAnswer = view.findViewById(R.id.answerRight);
        digits = view.findViewById(R.id.digits);

        deleteButton.setOnClickListener(new View.OnClickListener()  {

            @Override
            public void onClick(View v) {
                if(isTablet) {
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.remove(QuestionFragment.this).commit();
                    // quizStart.deleteRow(id);
                }
                else {
                    Intent intent = new Intent();
                    intent.putExtra("delete", true);

                    getActivity().setResult(50, intent);
                    getActivity().finish();
                }
            }


        });

        addButton.setOnClickListener(new View.OnClickListener()  {

            @Override
            public void onClick(View v) {

                q.setQuestion(question.getText().toString());
                try{
                    q.setRightAnswer(correctAnswer.getText().toString());
                }catch(NullPointerException e){

                }

                Log.i("thhh" , q.getQuestion() + "u");
                Log.i("thhh" , q.getRightAnswer() + "u");

                try {
                    q.getAnswers().add(answer1.getText().toString());
                    q.getAnswers().add(answer2.getText().toString());
                    q.getAnswers().add(answer3.getText().toString());
                    q.getAnswers().add(answer4.getText().toString());
                }catch(NullPointerException e){

                }
                try{
                    q.getAnswers().add(digits.getText().toString());
                }catch(NullPointerException e){

                }



                if(isTablet) {
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.remove(QuestionFragment.this).commit();
                    // quizStart.addRow(id);
                }
                else {
                    Intent intent = new Intent();
                    intent.putExtra("add", true);
                    getActivity().setResult(50, intent);
                    getActivity().finish();
                }



            }


        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        switch(choice){
            case "numeric":view = inflater.inflate(R.layout.numeric_fragment, container, false); break;
            case "multipleChoice": view = inflater.inflate(R.layout.multiple_choice_fragment, container, false); break;
            case "trueFalse": view = inflater.inflate(R.layout.true_false_fragment, container, false);
        }


        q = (Question) (getActivity().getIntent().getExtras().getSerializable("question"));

       /** correctAnswer.setText(getArguments().getString("cAnswer"));
        question.setText(getArguments().getString("Question"));**/
        try {
            answer1.setText(getArguments().getString("Answer1") + "e");
            answer2.setText(getArguments().getString("Answer2"));
            answer3.setText(getArguments().getString("Answer3"));
            answer4.setText(getArguments().getString("Answer4"));
        }catch(NullPointerException e){

        }
        try{
        digits.setText(getArguments().getString("Digits"));
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


}


