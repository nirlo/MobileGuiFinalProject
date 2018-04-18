package com.example.lock0134.mobileguifinalproject;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

/**
 * Created by Alec on 2018-04-06.
 */

public class movieDataFragment extends Fragment{
    private EditText title;
    private EditText actor;
    private EditText length;
    private EditText description;
    private EditText rating;
    private EditText genre;
    private EditText url;
    private Button submitB;
    Boolean editFlag = false;
    String position;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_movie_fragment, container, false);
        title = (EditText) view.findViewById(R.id.movieTitle);
        actor = (EditText) view.findViewById(R.id.movieActors);
        length = (EditText) view.findViewById(R.id.movieLength);
        description = (EditText) view.findViewById(R.id.movieDesc);
        rating= (EditText) view.findViewById(R.id.movieRate);
        genre = (EditText) view.findViewById(R.id.movieGenre);
        url = (EditText) view.findViewById(R.id.movieURL);
        submitB = (Button) view.findViewById(R.id.submitButton);
        if(!getActivity().getIntent().equals(null))
        {
            title.setText(getActivity().getIntent().getStringExtra("TITLE"));
            actor.setText(getActivity().getIntent().getStringExtra("ACTOR"));
            length.setText(getActivity().getIntent().getStringExtra("LENGTH"));
            description.setText(getActivity().getIntent().getStringExtra("DESC"));
            rating.setText(getActivity().getIntent().getStringExtra("RATING"));
            genre.setText(getActivity().getIntent().getStringExtra("GENRE"));
            url.setText(getActivity().getIntent().getStringExtra("URL"));
            position = getActivity().getIntent().getStringExtra("POSITION");
            //editFlag = true;
        }

        submitB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> list = new ArrayList();
                list.add(title.getEditableText().toString());
                list.add(actor.getEditableText().toString());
                list.add(length.getEditableText().toString());
                list.add(description.getEditableText().toString());
                list.add(rating.getEditableText().toString());
                list.add(genre.getEditableText().toString());
                list.add(url.getEditableText().toString());
                Intent resultIntent = new Intent();
                resultIntent.putStringArrayListExtra("Array", list);
                resultIntent.putExtra("Title", title.getText().toString());
                resultIntent.putExtra("Actor", actor.getText().toString());
                resultIntent.putExtra("Length", length.getText().toString());
                resultIntent.putExtra("Desc", description.getText().toString());
                resultIntent.putExtra("Rating", rating.getText().toString());
                resultIntent.putExtra("Genre", genre.getText().toString());
                resultIntent.putExtra("url", url.getText().toString());
                if(false) {
                    resultIntent.putExtra("pos", position);
                    getActivity().setResult(60, resultIntent);
                    editFlag = false;
                    getActivity().finish();
                }
                else
                {
                    getActivity().setResult(50, resultIntent);
                    getActivity().finish();
                }

            }
        });

        return view;
    }
}
