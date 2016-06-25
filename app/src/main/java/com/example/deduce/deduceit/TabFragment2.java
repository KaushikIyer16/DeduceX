package com.example.deduce.deduceit;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.deduce.model.HistoryModel;

/**
 * Created by mahesh on 11/06/16.
 */
public class TabFragment2 extends android.support.v4.app.Fragment {
    private static View subContext;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_fragment_2, container , false);
        subContext=rootView;
        int width = getResources().getDisplayMetrics().widthPixels/2;
        EditText mEditText = (EditText) rootView.findViewById(R.id.edit_Text);
        mEditText.setMaxWidth(width);
        TextView turnName = (TextView) rootView.findViewById(R.id.turnStatus);
        if(GameInfoActivity.turn){
            turnName.setText("Your Turn");
        }
        else {
            turnName.setText("Opponent's Turn");
        }
        //Button submitButton = (Button) rootView.findViewById(R.id.button_main);

        /*submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText mEditText = (EditText) v.findViewById(R.id.edit_Text);
                Toast.makeText(v.getContext(),mEditText.getText().toString(),Toast.LENGTH_SHORT).show();
                TabFragment1.refreshList(mEditText.getText().toString(),2);
            }
        });*/

        return rootView;
    }

}
