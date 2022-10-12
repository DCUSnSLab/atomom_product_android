package com.example.roicamera;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class ResultActivity extends AppCompatActivity {
    TextView img_text;
    String details;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        img_text=(TextView)findViewById(R.id.img_details);

        Intent intent = getIntent();

        details = intent.getExtras().getString("text");

        img_text.setText(details);

    }

    @Override
    public void onBackPressed(){
        Button b2 = (Button) findViewById(R.id.send);
        b2.setEnabled(true);
    }

}