package com.example.android.displaymyjokes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DisplayMyJokesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_my_jokes);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            String displayJoke = bundle.getString("display");
            TextView jokeTextView = (TextView) findViewById(R.id.joke_textView);
            jokeTextView.setText(displayJoke);
        }


    }


}
