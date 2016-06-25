package com.example.deduce.deduceit;

import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.deduce.model.HistoryModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.TreeSet;

public class GameInfoActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    public static HistoryModel historyModel = HistoryModel.getInstance();
    private ArrayList<String> hList = new ArrayList<>();
    public static String inputString;
    public static boolean playerTurn;
    public BluetoothSocket mBluetoothSocket;
    public static final int MESSAGE_RECEIVED=1;
    public ConnectedThread mConnectThread;
    public static boolean turn;
    public String theWord;
    public static boolean lost =false;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case MESSAGE_RECEIVED:
                    //System.out.println("this is from the other phone:"+msg.obj);
                    if(msg.obj.toString().equalsIgnoreCase("3")){
                        Toast.makeText(GameInfoActivity.this,"You Lose :P",Toast.LENGTH_SHORT).show();
                        quitGame();
                    }
                    else if(!(msg.obj.toString().equalsIgnoreCase("1")||msg.obj.toString().equalsIgnoreCase("0"))){
                        theWord = msg.obj.toString();
                        //System.out.println("inside the if in handler: "+theWord);
                    }
                    else if(msg.obj.toString().equalsIgnoreCase("1")){
                        TextView turnTextView = (TextView) findViewById(R.id.turnStatus);
                        turnTextView.setText("Your Turn");
                        switchTurn();
                    }
                    else if (msg.obj.toString().equalsIgnoreCase("0"))
                        turn = false;


                    break;
            }
        }
    };


    @Override
    protected void onStop() {
        super.onStop();

        historyModel.getHistoryList().clear();
    }
    @Override
    public void onBackPressed() {
        // do nothing
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        historyModel.getHistoryList().clear();
    }

    public void getTurn(boolean turn){
        TextView turnName = (TextView) findViewById(R.id.turnStatus);
        if(turn){
            turnName.setText("Your Turn");
        }
        else{
            turnName.setText("Opponent's Turn");
        }
    }
    public void switchTurn(){
        turn=!turn;
    }
    public void btnClick(View view){
        EditText mEditText = (EditText)findViewById(R.id.edit_Text);


        final TextView mTextView = (TextView) findViewById(R.id.hit_count_label);
        final ImageView numberImage = (ImageView) findViewById(R.id.hit_count);
        TextView turnName = (TextView) findViewById(R.id.turnStatus);
        if(!turn)
        {
            Toast.makeText(this,"Please wait for your turn",Toast.LENGTH_SHORT).show();
            mEditText.setText("");
        }
        else if(!mEditText.getText().toString().equals("") && mEditText.getText().toString().length()==Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(GameInfoActivity.this).getString("word_size","4"))&& !(historyModel.getHistoryList().toString().contains(mEditText.getText().toString())) &&MainActivity.wordList.contains(mEditText.getText().toString())){
            String userInput = mEditText.getText().toString();
            int hc = getHitCount(theWord,userInput);

            /*This portion is to determine what happens when one player wins the game*/
            if(userInput.equalsIgnoreCase(theWord))
            {
                Toast.makeText(this,"You Win!!!",Toast.LENGTH_SHORT).show();

                mConnectThread.write(new String("3").getBytes());

                quitGame();

            }
            historyModel.add(mEditText.getText().toString(),hc);
            if (view != null) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
            switch(hc){
                case 0:numberImage.setImageResource(R.drawable.number_0);
                    break;
                case 1:numberImage.setImageResource(R.drawable.number_1);
                    break;
                case 2:numberImage.setImageResource(R.drawable.number_2);
                    break;
                case 3:numberImage.setImageResource(R.drawable.number_3);
                    break;
                case 4:numberImage.setImageResource(R.drawable.number_4);
                    break;
                case 5:numberImage.setImageResource(R.drawable.number_5);

            }
            mTextView.setVisibility(View.VISIBLE);
            numberImage.setVisibility(View.VISIBLE);

            mTextView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mTextView.setVisibility(View.INVISIBLE);
                    numberImage.setVisibility(View.INVISIBLE);
                }
            },5000);

            switchTurn();
            getTurn(turn);

            mConnectThread.write(new String("1").getBytes());
        }
        else
            Toast.makeText(this,"Please enter a valid string or unused word",Toast.LENGTH_SHORT).show();
        mEditText.setText("");
        //View view = this.getCurrentFocus();
    }

    private void quitGame() {
        mConnectThread.cancel();
        Intent callMainActivity = new Intent(getApplicationContext(), MainActivity.class);
        callMainActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(callMainActivity);
    }

    private int getHitCount(String src, String compare) {
        int hitCount=0;

        for(int i=0;i<compare.length();i++){
            if(src.contains(Character.toString(compare.charAt(i)))){
                hitCount++;
            }
        }
        return hitCount;
    }

    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        Intent mIntent = getIntent();
        inputString = mIntent.getExtras().getString("input");
        playerTurn = mIntent.getExtras().getBoolean("turn");
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        mBluetoothSocket = InitBluetoothActivity.mConnectedSocket;
        mConnectThread = new ConnectedThread();
        mConnectThread.start();
        synchronized (this){
            if(playerTurn)
                mConnectThread.write(inputString.getBytes());
            else {
                mConnectThread.write(inputString.getBytes());
            }
        }




        turn=playerTurn;
        mViewPager.setCurrentItem(1);
    }
    private class ConnectedThread extends Thread{

        private InputStream mInputStream;
        private OutputStream mOutputStream;

        public ConnectedThread() {
            try {
                mInputStream = mBluetoothSocket.getInputStream();
                mOutputStream = mBluetoothSocket.getOutputStream();
            }catch (IOException ee){
                ee.printStackTrace();
                Toast.makeText(GameInfoActivity.this,"Bluetooth Unable to communicate",Toast.LENGTH_SHORT).show();
                mInputStream = null;
                mOutputStream = null;

            }
        }

        @Override
        public void run() {
            byte data[] = new byte[1024];
            int bytes;
            while (true){

                try{
                    bytes = mInputStream.read(data);
                    String message = new String(data,0,bytes);
                    mHandler.obtainMessage(MESSAGE_RECEIVED,0,0,message).sendToTarget();

                }catch (IOException ee){
                    // this means that we have lost the connection

                }
            }
        }

        public void write(byte data[]){
            try {
                mOutputStream.write(data);
            } catch (IOException e) {
                e.printStackTrace();

            }
        }

        public void cancel(){
            try {
                mBluetoothSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_game_info, container, false);

           /* TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));*/
            return rootView;
        }


    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            //return PlaceholderFragment.newInstance(position + 1);
            switch (position) {
                case 0:
                    TabFragment1 history = new TabFragment1();
                    return history;
                case 1:
                    TabFragment2 status = new TabFragment2();
                    return status;
                case 2:
                    TabFragment3 dictionary = new TabFragment3();
                    return dictionary;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "HISTORY";
                case 1:
                    return "STATUS";
                case 2:
                    return "DICTIONARY";
            }
            return null;
        }
    }
}
