package com.example.deduce.deduceit;

import android.content.Intent;
import android.content.res.AssetManager;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.TreeSet;

public class MainActivity extends AppCompatActivity {

    public static ArrayList<String> wordList = new ArrayList<>();
    InputStream wordListStream;
    String s;
    int i;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_settings){
            Intent settingIntent =  new Intent(getApplicationContext(),SettingsActivity.class);
            startActivity(settingIntent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        FloatingActionButton settings_fab = (FloatingActionButton) findViewById(R.id.fab_one);
        settings_fab.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent settingIntent =  new Intent(getApplicationContext(),SettingsActivity.class);
                startActivity(settingIntent);
            }
        });
        FloatingActionButton description_fab = (FloatingActionButton) findViewById(R.id.fab_two);
        assert description_fab != null;
        description_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent descriptionintent = new Intent(getApplicationContext(),GameDescriptionActivity.class);
                startActivity(descriptionintent);
            }
        });
        EditText startWord = (EditText) findViewById(R.id.inputText);
        FloatingActionButton buttonOne = (FloatingActionButton) findViewById(R.id.hvsh1);
        Button goButton= (Button) findViewById(R.id.go_button);

        assert buttonOne != null;
        buttonOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FloatingActionButton buttonOne = (FloatingActionButton) findViewById(R.id.hvsh1);
                assert buttonOne != null;
                buttonOne.setVisibility(View.INVISIBLE);
                EditText startWord = (EditText) findViewById(R.id.inputText);
                startWord.setVisibility(View.VISIBLE);
                populateWordList();

                Button goButton= (Button) findViewById(R.id.go_button);
                goButton.setVisibility(View.VISIBLE);
            }
        });
        assert goButton != null;
        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText startWord = (EditText) findViewById(R.id.inputText);
                s = startWord.getText().toString().toLowerCase();
                if((s.length() == Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(MainActivity.this).getString("word_size","4")))&&MainActivity.wordList.contains(s)){
                    Intent initBluetoothIntent = new Intent(getApplicationContext(), InitBluetoothActivity.class);
                    initBluetoothIntent.putExtra("inputString",s.toString());
                    startActivity(initBluetoothIntent);
                    //System.out.println(s);
                }
                else {
                    Toast.makeText(v.getContext(),"Please enter a valid word",Toast.LENGTH_SHORT).show();
                }
            }
        });



    }

    private void populateWordList() {
        popluateDictionary mPopulateDictionary = new popluateDictionary(Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(this).getString("word_size","4")));
        mPopulateDictionary.start();
    }

    public class popluateDictionary extends Thread{

        private int wordLength;
        public popluateDictionary(int wordLength) {
            this.wordLength = wordLength;
        }

        @Override
        public void run() {
            AssetManager a = getAssets();
            wordList.clear();
            try{
                wordListStream = a.open("words.txt");
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }


            BufferedReader br = new BufferedReader(new InputStreamReader(wordListStream));
            try {
                while((s=br.readLine()) != null)
                {
                    if(s.length() == wordLength)
                    {
                        TreeSet ts = new TreeSet();
                        for(i=0;i< wordLength&& ts.add(s.charAt(i));i++);
                        if(i == wordLength)
                            wordList.add(s);
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
