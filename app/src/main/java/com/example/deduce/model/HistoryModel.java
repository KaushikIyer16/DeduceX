package com.example.deduce.model;

import java.util.ArrayList;

/**
 * Created by Bhargav on 15-06-2016.
 */
public class HistoryModel {

    public ArrayList<String> getHistoryList() {
        return historyList;
    }

    public void setHistoryList(ArrayList<String> historyList) {
        this.historyList = historyList;
    }

    private ArrayList<String> historyList = new ArrayList<>();
    
    public void add(String string,int count){

        historyList.add(string+"     "+count);
    }

    public static HistoryModel getInstance() {
        return new HistoryModel();
    }
}
