package com.example.lock0134.mobileguifinalproject;

import android.app.Activity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class TranspoDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transpo_details);

        Bundle temp = getIntent().getExtras();

        FragmentManager manager = getSupportFragmentManager();
        searchDetailsFragment details = new searchDetailsFragment();
        details.setArguments(temp);
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.searchFrame, details);
        transaction.commit();

    }
}
