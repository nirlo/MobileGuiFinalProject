package com.example.lock0134.mobileguifinalproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {
    private Button ocTranspo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ocTranspo = findViewById(R.id.OCTranspoButton);

        ocTranspo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent  = new Intent(MainActivity.this, OCTranspo.class);
                startActivity(intent);
                Toast.makeText(MainActivity.this, "You have clicked the bus button!", Toast.LENGTH_LONG);
            }
        });

    }
}
