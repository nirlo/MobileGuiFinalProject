package com.example.lock0134.mobileguifinalproject;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;

/**
 * Created by William on 2018-04-06.
 */

public class QuestionDetails extends Activity {
    boolean isTablet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        QuestionFragment frag = (QuestionFragment)getIntent().getExtras().getSerializable("frag");
        String choice = getIntent().getExtras().getString("type");
        frag.setArguments(getIntent().getExtras());


try {
    switch (choice) {
        case "numeric":
            setContentView(R.layout.numeric_fragment);
            break;
        case "multipleChoice":
            setContentView(R.layout.multiple_choice_fragment);
            break;
        case "trueFalse":
            setContentView(R.layout.true_false_fragment);
    }
}catch(NullPointerException e){
    finish();
}

        FragmentTransaction ft = getFragmentManager().beginTransaction();
try{
        switch(choice){
            case "numeric":ft.add(R.id.nFrameLayout, frag).commit(); break;
            case "multipleChoice":ft.add(R.id.mcFrameLayout, frag).commit();break;
            case "trueFalse":ft.add(R.id.tffFrameLayout, frag).commit();;
        }
}catch(NullPointerException e){
    finish();
}
    }
}
