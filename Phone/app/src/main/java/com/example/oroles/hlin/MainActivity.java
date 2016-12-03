package com.example.oroles.hlin;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.oroles.hlin.Controllers.StoreController;
import com.example.oroles.hlin.Fragments.BluetoothListenerFragment;
import com.example.oroles.hlin.Fragments.EntriesFragment;
import com.example.oroles.hlin.Fragments.NotesFragment;
import com.example.oroles.hlin.Utils.Requests;
import com.example.oroles.hlin.Utils.SharedPreferencesHelper;
import com.example.oroles.hlin.Utils.UIUpdater;


public class MainActivity extends AppCompatActivity {

    private BluetoothAdapter mBluetoothAdapter;
    private ImageView mIconStatusImageView;
    private TextView mConnectedBluetoothTextView;

    private static final int REQUEST_ENABLE_BT = 1;
    private String mConnectionStatus;

    public static boolean SETTINGS_STARTED = false;

    final Handler handler = new Handler() {
        @Override
        public void  handleMessage(Message msg) {
            if(msg.what == Requests.REQUEST_UPDATE_STATUS){
                updateIconStatus((String)msg.obj);
                mConnectionStatus = (String)msg.obj;
            }
            if(msg.what == Requests.REQUEST_CONNECT_BLUETOOTH){
                mConnectedBluetoothTextView.setText((String)msg.obj);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StoreController.getInstance(getApplicationContext()).addContext(getApplicationContext());

        final int REQUEST_EXTERNAL_STORAGE_PERMISSION = 1; //request permission for reading the file
        String[] PERMISSIONS_STORAGE = { Manifest.permission.READ_EXTERNAL_STORAGE  };
        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE_PERMISSION
            );
        }

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), AddEntryActivity.class);
                if (StoreController.getInstance(getApplicationContext()).getBluetoothConnection().isConnected()) {
                    startActivity(i);
                }
                else {
                    Toast.makeText(getApplicationContext(), "First connect before adding entries", Toast.LENGTH_LONG).show();
                }
            }
        });*/

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Doesn't support Bluetooth", Toast.LENGTH_LONG).show();
        }

        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        mIconStatusImageView = (ImageView)findViewById(R.id.imageStatusImageView);
        mConnectionStatus = SharedPreferencesHelper.getBluetoothStatus(getApplicationContext());

        mConnectedBluetoothTextView = (TextView)findViewById(R.id.connectedBluetoothNameTextView);
        mConnectedBluetoothTextView.setText(SharedPreferencesHelper.getConnectedBluetooth(getApplicationContext()));

        updateIconStatus(mConnectionStatus);

        FragmentTabHost mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);

        mTabHost.addTab(mTabHost.newTabSpec("Entries").setIndicator("Entries"),
                EntriesFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("Notes").setIndicator("Notes"),
                NotesFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("Bluetooth").setIndicator("Bluetooth"),
                BluetoothListenerFragment.class, null);
        ((TextView)(mTabHost.getTabWidget().getChildAt(0).findViewById(android.R.id.title))).setTextColor(Color.RED);
        ((TextView)(mTabHost.getTabWidget().getChildAt(1).findViewById(android.R.id.title))).setTextColor(Color.RED);
        ((TextView)(mTabHost.getTabWidget().getChildAt(2).findViewById(android.R.id.title))).setTextColor(Color.RED);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        UIUpdater.getInstance().register(handler);
        UIUpdater.getInstance().startThread(StoreController.getInstance(getApplicationContext()));

        SETTINGS_STARTED = false;
    }

    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferencesHelper.setConnectedBluetooth(mConnectedBluetoothTextView.getText().toString(), getApplicationContext());
        SharedPreferencesHelper.setBluetoothStatus(mConnectionStatus, getApplicationContext());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (!SETTINGS_STARTED) {
            StoreController.getInstance(getApplicationContext()).getBluetoothIsAlive().stopSending();

            StoreController.getInstance(getApplicationContext()).getBluetoothConnection().write(
                    StoreController.getInstance(getApplicationContext()).getSenderProcessor().createCloseRequest(
                            StoreController.getInstance(getApplicationContext()).getEncryption().getHash()
                    )
            );
            StoreController.getInstance(getApplicationContext()).getBluetoothConnection().write(
                    StoreController.getInstance(getApplicationContext()).getSenderProcessor().createSetLastTimeUsedRequest(
                            System.currentTimeMillis()
                    )
            );
            String selectedMac = SharedPreferencesHelper.getBluetoothSelectedMac(getApplicationContext());
            if (!selectedMac.equals("")) {
                SharedPreferencesHelper.setBluetoothSelectedMac(getApplicationContext(), "");
                StoreController.getInstance(getApplicationContext()).getEncryption().storeHash(selectedMac);
            }
            StoreController.getInstance(getApplicationContext()).getBluetoothConnection().close();
            SharedPreferencesHelper.setLastTimeUsed("Unknown", getApplicationContext());
            SharedPreferencesHelper.setBluetoothStatus("Disconnected", getApplicationContext());
            SharedPreferencesHelper.setConnectedBluetooth("No Connection", getApplicationContext());
            SharedPreferencesHelper.clearBluetoothDevices(getApplicationContext());
            UIUpdater.getInstance().deregister(handler);
            UIUpdater.getInstance().stopThread(StoreController.getInstance(getApplicationContext()));
            EntriesFragment.sHasRunExpired = false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent i = new Intent(this, SettingsActivity.class);
            startActivity(i);
            return true;
        }
        if (id == R.id.action_add) {
            if (StoreController.getInstance(getApplicationContext()).getBluetoothConnection().isConnected()) {
                Intent i = new Intent(this, AddEntryActivity.class);
                startActivity(i);
                return true;
            }
            else {
                Toast.makeText(getApplicationContext(), "First connect before adding entries", Toast.LENGTH_LONG).show();
            }
        }
        if (id == R.id.action_add_note) {
            if (StoreController.getInstance(getApplicationContext()).getBluetoothConnection().isConnected()) {
                Intent i = new Intent(this, AddNoteActivity.class);
                startActivity(i);
                return true;
            }
            else {
                Toast.makeText(getApplicationContext(), "First connect before adding entries", Toast.LENGTH_LONG).show();
            }
        }
        if (id == R.id.action_backup) {
            if (StoreController.getInstance(getApplicationContext()).getRepository().createBackUp("MyBack")) {
                Toast.makeText(getApplicationContext(), "Backup was created", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "No backup", Toast.LENGTH_LONG).show();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateIconStatus(String status) {
        if ((status != null) &&
                        (status.equals("Connected - Is Alive") ||
                         status.equals("Connected-Press Button") ||
                         status.equals("Connected-Correct Hash"))) {
            mIconStatusImageView.setImageResource(R.drawable.circle_green);
        }
        else {
            mIconStatusImageView.setImageResource(R.drawable.circle_red);
            if (status != null) {
                mConnectedBluetoothTextView.setText(status);
            }
        }
    }

    public BluetoothAdapter getBluetoothAdapter() {
        return mBluetoothAdapter;
    }
}
