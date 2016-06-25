package com.example.deduce.deduceit;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class GameDescriptionActivity extends AppCompatActivity {
    InputStream helpText;
    String descriptionTextFromFile="";
    String finalText="";
    private TextView text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_description);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        text = (TextView) findViewById(R.id.game_description);
        AssetManager a = getAssets();


        try{
            helpText = a.open("help_text.txt");
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(helpText));
        try {
            while ((descriptionTextFromFile=br.readLine()) != null)
            {
                finalText+=descriptionTextFromFile;
            }
            text.setText(finalText);
        } catch (IOException e) {
            e.printStackTrace();
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
