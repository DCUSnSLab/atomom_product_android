package com.example.roicamera;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.Serializable;


public class ResultActivity extends AppCompatActivity {
    TextView img_text;
    String details;

    TextView product1;
    TextView product2;
    TextView product3;
    TextView product4;

    String details1;
    String details2;
    String details3;
    String details4;

//    public class Cosmetic implements Serializable {
//        private static final long serialVersionUID = 1L;
//        private String rank;
//        private String brand;
//        private String name;
//        private String ingredients;
//
//
//        public Cosmetic(String rank, String brand, String name, String ingredients){
//            this.rank = rank;
//            this.brand = brand;
//            this.name = name;
//            this.ingredients = ingredients;
//        }
//    }

    String p1_detail;
    String p2_detail;
    String p3_detail;
    String p4_detail;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        product1=(TextView)findViewById(R.id.p_details1);
        product2=(TextView)findViewById(R.id.p_details2);
        product3=(TextView)findViewById(R.id.p_details3);
        product4=(TextView)findViewById(R.id.p_details4);
        img_text=(TextView)findViewById(R.id.img_details);

        Intent intent = getIntent();

        details1 = intent.getExtras().getString("P1");
        details2 = intent.getExtras().getString("P2");
        details3 = intent.getExtras().getString("P3");
        details4 = intent.getExtras().getString("P4");
        details = intent.getExtras().getString("text");

        product1.setText(details1);
        product2.setText(details2);
        product3.setText(details3);
        product4.setText(details4);
        img_text.setText(details);

        p1_detail = intent.getExtras().getString("cosmetic1");
        p2_detail = intent.getExtras().getString("cosmetic2");
        p3_detail = intent.getExtras().getString("cosmetic3");
        p4_detail = intent.getExtras().getString("cosmetic4");

        product1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),IngredientActivity.class);
                intent.putExtra("Ingredients", p1_detail);
                startActivity(intent);
            }
        });
        product2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),IngredientActivity.class);
                intent.putExtra("Ingredients", p2_detail);
                startActivity(intent);
            }
        });
        product3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),IngredientActivity.class);
                intent.putExtra("Ingredients", p3_detail);
                startActivity(intent);
            }
        });
        product4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),IngredientActivity.class);
                intent.putExtra("Ingredients", p4_detail);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed(){
        Button b2 = (Button) findViewById(R.id.send);
        b2.setEnabled(true);
    }

}