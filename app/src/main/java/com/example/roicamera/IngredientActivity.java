package com.example.roicamera;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class IngredientActivity extends AppCompatActivity {

    TextView ingredients;
    String detail;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient);

        ingredients=(TextView)findViewById(R.id.cosmetic_details);
        ingredients.setMovementMethod(new ScrollingMovementMethod());

        Intent intent = getIntent();

        detail = intent.getExtras().getString("Ingredients");

        ingredients.setText(detail);

    }
}
