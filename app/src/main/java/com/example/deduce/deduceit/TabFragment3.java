package com.example.deduce.deduceit;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;




/**
 * Created by mahesh on 11/06/16.
 */
public class TabFragment3 extends android.support.v4.app.Fragment {
    CharSequence cs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_fragment_3, container , false);
        final ListView wordsList = (ListView) rootView.findViewById(R.id.words);
        final EditText searchBar = (EditText) rootView.findViewById(R.id.searchWords);


        ArrayAdapter adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1 , android.R.id.text1, MainActivity.wordList);
        wordsList.setAdapter(adapter);

        searchBar.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                String s = searchBar.getText().toString().toLowerCase();
                ArrayList foundList = new ArrayList();
                ArrayAdapter foundArray = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1 , android.R.id.text1, foundList);
                ArrayAdapter nullArray = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1 , android.R.id.text1, Arrays.asList("No items match"));
                for (String string : MainActivity.wordList) {
                    if(string.contains(s)){
                        foundList.add(string);
                    }
                }
                if(foundList.isEmpty())
                    wordsList.setAdapter(nullArray);
                else
                    wordsList.setAdapter(foundArray);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });


        return rootView;
    }


}
