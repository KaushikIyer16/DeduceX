package com.example.deduce.deduceit;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class InitBluetoothActivity extends AppCompatActivity {

    public static final int REQUEST_ENABLE_BT = 1;
    public static final int DISCOVERABLE_DURATION = 90;
    public static BluetoothSocket mConnectedSocket;
    public BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    public List<BluetoothDevice> discoveredDevices = new ArrayList<>();
    public ArrayAdapter mArrayAdapter;
    public String inputWord;
    public ProgressBar mProgressBar;
    private Set<BluetoothDevice> pairedDevices;

    private final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (!discoveredDevices.contains(device) && !mBluetoothAdapter.getBondedDevices().contains(device)) {
                    mArrayAdapter.add(device.getName() + "    " + device.getAddress());
                    discoveredDevices.add(device);
                    redrawListView();
                }

            }
        }
    };
    public BluetoothSocket mBluetoothSocket;

    @Override
    public void onBackPressed() {
        // do nothing
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init_bluetooth);

        Intent intent  = getIntent();
        inputWord = intent.getExtras().getString("inputString");

        mArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mBroadcastReceiver, filter);
        initBluetooth();
        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                initBluetooth();
            }
        });

        */
        ListView mBluetoothDeviceList = (ListView) findViewById(R.id.BluetoothListView);
        assert mBluetoothDeviceList != null;
        mBluetoothDeviceList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                /*if(mBluetoothSocket == null){
                    initBluetooth();
                }*/
                Thread clientThread = new Thread(new ConnectThread(discoveredDevices.get(i)));
                clientThread.start();


            }
        });



    }

    /*This function is used to initiate the bluetooth pairing process by first enabling bluetooth if
      its not ON and if it is then it will ask for enabling this device's discoverability for DISCOVERABLE_DURATION
      time which is a static final variable in MainActivity.java*/
    private void initBluetooth() {
        if (mBluetoothAdapter == null) {
            System.out.println("This Device does not have bluetooth");
        }
        if (!mBluetoothAdapter.isEnabled()) {
            Intent initBlutoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(initBlutoothIntent, REQUEST_ENABLE_BT);
        } else {

            /*Starting the server thread to listen for any pairing requests*/
            Thread serverThread = new Thread(new AcceptThread());
            serverThread.start();
            initPairing();
        }
    }

    /*The Below function will present the user with a list of already paired devices and will further continue
    * to scan for more devices*/

    @Override
    protected void onResume() {
        super.onResume();
        //initBluetooth();
    }

    private void initPairing() {

        mArrayAdapter.clear();
        discoveredDevices.clear();
        if (mBluetoothSocket != null) {
            mBluetoothSocket = null;

        }
        mProgressBar = (ProgressBar) findViewById(R.id.mProgressBar);
        mProgressBar.setVisibility(View.VISIBLE);
        mProgressBar.postDelayed(new Runnable() {
            @Override
            public void run() {

                mProgressBar.setVisibility(View.GONE);
                pairedDevices = mBluetoothAdapter.getBondedDevices();
                for (BluetoothDevice bluetoothDevice : pairedDevices) {
                    discoveredDevices.add(bluetoothDevice);
                    mArrayAdapter.add(bluetoothDevice.getName() + "     " + bluetoothDevice.getAddress());

                }
            }
        }, 10000);


        redrawListView();

        // now we will start to discover any phones that are active nearby
        mBluetoothAdapter.startDiscovery();

        // we have make sure that other devices can discover this one

        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, DISCOVERABLE_DURATION);
        startActivity(discoverableIntent);
    }


    /* this function will redraw the list view whenever the bluetooth discovery process is being
        refreshed or being called for the first time*/
    private void redrawListView() {
        ListView mListView = (ListView) findViewById(R.id.BluetoothListView);

        mListView.setAdapter(mArrayAdapter);
    }

    private void sendIntentAfterConnection(boolean turn) {
        Intent intent = new Intent(getApplicationContext(), GameInfoActivity.class);
        mConnectedSocket = mBluetoothSocket;
        intent.putExtra("turn",turn);
        intent.putExtra("input", inputWord);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_ENABLE_BT && resultCode == RESULT_OK) {
            Thread serverThread = new Thread(new AcceptThread());
            serverThread.start();
            initPairing();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent settingIntent = new Intent(getApplicationContext(),SettingsActivity.class);
            startActivity(settingIntent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(mBroadcastReceiver);
    }


    /*The Below Class is a thread used to model the server thread
    * It has the follow functions:
    *   1. run() - this is the standard function to be overriden and simply accepts a bluetooth socket
    *              when the connection is successful and launches an intent to the next screen.
    *
    * It uses the following variables:
    *   1. mBluetoothServerSocket - this is the variable that emulates the server socket by running
    *      a blocking while loop within the run function. But before this it is initialzed to listen
    *      to a RFCOMM channel in the constructor using the listeningUsingRfcommWithServiceRecord()
    *   2. mBluetoothSocket - this is used to represent the socket obtained for communication. this
    *      is a global variable initialized in the enclosing class.
    *   3. myUUID - is a variable representing the UUID of the application.*/

    private class AcceptThread extends Thread {

        public BluetoothServerSocket mBluetoothServerSocket;

        UUID myUUID = new UUID(980, 632);

        public AcceptThread() {

            try {
                mBluetoothServerSocket = mBluetoothAdapter.listenUsingRfcommWithServiceRecord("DeduceIt", myUUID);

            } catch (IOException ee) {
                ee.printStackTrace();
            }
        }

        @Override
        public void run() {
            BluetoothSocket socket;
            while (true) {
                try {
                    socket = mBluetoothServerSocket.accept();
                } catch (IOException ee) {
                    break;
                }

                if (socket != null) {
                    // manage the socket and do some work
                    mBluetoothSocket = socket;

                    // once connection socket has been obtained. then do a intent to GameInfoActivity.java
                    sendIntentAfterConnection(false);
                    try {

                        mBluetoothServerSocket.close();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }


    }

    /*The Below Class is a thread used to model the client thread
    * It has the follow functions:
    *   1. run() - this is the standard function to be overriden which requests for
    *
    * It uses the following variables:
    *   1. mBluetoothServerSocket - this is the variable that emulates the server socket by running
    *      a blocking while loop within the run function. But before this it is initialzed to listen
    *      to a RFCOMM channel in the constructor using the listeningUsingRfcommWithServiceRecord()
    *   2. mBluetoothSocket - this is used to represent the socket that is usedto connect to remote
    *      hosts, is a global variable initialized in the enclosing class.
    *   3. myUUID - is a variable representing the UUID of the application.
    *   4. mBluetoothDevice - is a socket that will be used for communication*/
    private class ConnectThread extends Thread {


        public BluetoothDevice mBluetoothDevice;
        UUID myUUID = new UUID(980, 632);

        public ConnectThread(BluetoothDevice device) {

            try {
                mBluetoothDevice = device;

                mBluetoothSocket = mBluetoothDevice.createRfcommSocketToServiceRecord(myUUID);
            } catch (IOException ee) {
                ee.printStackTrace();
            }
        }

        @Override
        public void run() {
            mBluetoothAdapter.cancelDiscovery();
            try {
                mBluetoothSocket.connect();
                sendIntentAfterConnection(true);
            } catch (IOException ee) {
                try {
                    mBluetoothSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
