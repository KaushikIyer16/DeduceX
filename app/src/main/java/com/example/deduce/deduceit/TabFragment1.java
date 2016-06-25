package com.example.deduce.deduceit;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.deduce.model.HistoryModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created by mahesh on 11/06/16.
 */
public class TabFragment1 extends android.support.v4.app.Fragment {

    private List<String> mArray;
    private static ArrayAdapter<String> adapter;
    private View mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //HistoryModel historyModel=GameInfoActivity.historyModel;
        View rootView = inflater.inflate(R.layout.tab_fragment_1, container , false);
        mContext = rootView;



        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1,android.R.id.text1,GameInfoActivity.historyModel.getHistoryList());
        ListView wordsList = (ListView) rootView.findViewById(R.id.history);
        wordsList.setAdapter(adapter);
        wordsList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();



    }

}
