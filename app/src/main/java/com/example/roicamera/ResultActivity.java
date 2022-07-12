package com.example.roicamera;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;


public class ResultActivity extends AppCompatActivity {
    TextView img_text;
    String details;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        img_text=(TextView)findViewById(R.id.img_details);
        img_text.setMovementMethod(new ScrollingMovementMethod());

        Intent intent = getIntent();

        details = intent.getExtras().getString("text");
        img_text.setText(details);
    }


}