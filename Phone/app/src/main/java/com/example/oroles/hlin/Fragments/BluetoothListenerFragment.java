package com.example.oroles.hlin.Fragments;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.oroles.hlin.Fragments.Dialogs.DeleteBluetoothNameFragment;
import com.example.oroles.hlin.Fragments.Dialogs.SaveBluetoothDeviceFragment;
import com.example.oroles.hlin.Fragments.Items.BluetoothNameItem;
import com.example.oroles.hlin.Utils.Requests;
import com.example.oroles.hlin.Views.CCSpinner;
import com.example.oroles.hlin.Controllers.StoreController;
import com.example.oroles.hlin.Fragments.Adapters.BluetoothDeviceAdapter;
import com.example.oroles.hlin.Fragments.Items.BluetoothDeviceItem;
import com.example.oroles.hlin.Fragments.Adapters.BluetoothNameAdapter;
import com.example.oroles.hlin.Interfaces.IClickBluetoothNameListener;
import com.example.oroles.hlin.MainActivity;
import com.example.oroles.hlin.R;
import com.example.oroles.hlin.Utils.SharedPreferencesHelper;
import com.example.oroles.hlin.Utils.UIUpdater;
import com.example.oroles.hlin.Utils.Utils;

import java.util.List;

public class BluetoothListenerFragment extends Fragment
        implements IClickBluetoothNameListener
{
    private TextView mBluetoothConnectionTextView;
    private TextView mLastTimeUsedTextView;

    private BluetoothNameItem mSelectedBluetoothName;
    private Utils.BLUETOOTH_FOUND_OPERATION mBluetoothOperation;

    private CCSpinner mBluetoothSpinner;
    private BluetoothNameAdapter mBluetoothNameAdapter;
    private List<BluetoothNameItem> mBluetoothNameEntries;

    private BluetoothDeviceAdapter mBluetoothDeviceAdapter;
    private List<BluetoothDeviceItem> mBluetoothDeviceEntries;

    final Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == Requests.REQUEST_UPDATE_STATUS ){
                mBluetoothConnectionTextView.setText((String)msg.obj);
            }
            if(msg.what == Requests.REQUEST_UPDATE_TIME ) {
                mLastTimeUsedTextView.setText((String)msg.obj);
                if (mSelectedBluetoothName != null && !(msg.obj).equals("Unknown")) {
                    mSelectedBluetoothName.setLastUsedTime((String)msg.obj);
                }
            }
            if (msg.what == Requests.REQUEST_STORE_IN_BUFFER ) {
                Toast.makeText(getActivity(), (String)msg.obj, Toast.LENGTH_LONG).show();
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_bluetooth, container, false);

        mBluetoothConnectionTextView = (TextView)v.findViewById(R.id.connectionTextView);
        mBluetoothConnectionTextView.setText(SharedPreferencesHelper.getBluetoothStatus(getContext()));

        mLastTimeUsedTextView = (TextView)v.findViewById(R.id.bluetoothLastTimeUsedTextView);
        mLastTimeUsedTextView.setText(SharedPreferencesHelper.getLastTimeUsed(getContext()));

        mBluetoothSpinner = (CCSpinner)v.findViewById(R.id.bluetoothSpinner);

        mBluetoothNameEntries = SharedPreferencesHelper.getBluetoothNames(getContext());
        mBluetoothNameAdapter = new BluetoothNameAdapter(getActivity(), this, mBluetoothNameEntries);
        mBluetoothSpinner.setAdapter(mBluetoothNameAdapter);

        Button bluetoothConnect = (Button)v.findViewById(R.id.connectBluetoothButton);
        bluetoothConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disconnectFromBluetooth();
                mSelectedBluetoothName = (BluetoothNameItem)mBluetoothSpinner.getSelectedItem();
                mBluetoothOperation = Utils.BLUETOOTH_FOUND_OPERATION.CONNECT;
                if (mSelectedBluetoothName != null) {
                    BluetoothDevice device = ((MainActivity)getActivity()).getBluetoothAdapter().getRemoteDevice(mSelectedBluetoothName.getMac());
                    if (device != null) {
                        connectToBluetooth(device);
                    }
                }
            }
        });

        Button bluetoothDisconnect = (Button)v.findViewById(R.id.disconnectBluetoothButton);
        bluetoothDisconnect.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                disconnectFromBluetooth();
            }
        });

        Button discoverBluetooth = (Button)v.findViewById(R.id.discoverBluetoothButton);
        discoverBluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBluetoothOperation = Utils.BLUETOOTH_FOUND_OPERATION.ADD_TO_LIST;
                mBluetoothDeviceEntries.clear();
                mBluetoothDeviceAdapter.notifyDataSetChanged();
                startDiscovery();
            }
        });

        ListView bluetoothDeviceListView = (ListView) v.findViewById(R.id.bluetoothDevicesListView);
        mBluetoothDeviceEntries = SharedPreferencesHelper.getBluetoothDevices(getActivity());
        mBluetoothDeviceAdapter = new BluetoothDeviceAdapter(getActivity(), mBluetoothDeviceEntries);
        bluetoothDeviceListView.setAdapter(mBluetoothDeviceAdapter);

        bluetoothDeviceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mSelectedBluetoothName = null;
                BluetoothDeviceItem item = mBluetoothDeviceEntries.get(position);
                BluetoothDevice device = ((MainActivity) getActivity()).getBluetoothAdapter().getRemoteDevice(item.getAddress());
                disconnectFromBluetooth();
                connectToBluetooth(device);
            }
        });

        bluetoothDeviceListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                BluetoothDeviceItem item = mBluetoothDeviceAdapter.getItem(position);

                FragmentManager fm = getActivity().getSupportFragmentManager();
                SaveBluetoothDeviceFragment dialog = SaveBluetoothDeviceFragment.newInstance(item);
                dialog.setTargetFragment(
                        getActivity().getSupportFragmentManager().findFragmentByTag("Bluetooth"),
                        Requests.REQUEST_SAVE_BLUETOOTH_DEVICE
                );
                dialog.show(fm, SaveBluetoothDeviceFragment.TITLE);

                return true;
            }
        });

        UIUpdater.getInstance().register(handler);
        UIUpdater.getInstance().startThread(StoreController.getInstance(getActivity()));

        return v;
    }

    void startDiscovery() {
        BluetoothAdapter adapter = ((MainActivity) getActivity()).getBluetoothAdapter();
        if (adapter != null) {

            if (adapter.isDiscovering()) {
                adapter.cancelDiscovery();
            }
            final int REQUEST_BLUETOOTH_PERMISSION = 2;
            String[] PERMISSIONS_BLUETOOTH = { Manifest.permission.ACCESS_COARSE_LOCATION };
            int permission = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION);
            if (permission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        getActivity(),
                        PERMISSIONS_BLUETOOTH,
                        REQUEST_BLUETOOTH_PERMISSION
                );
            }

            adapter.startDiscovery();

            IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            getActivity().registerReceiver(mReceiver, filter);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        UIUpdater.getInstance().deregister(handler);

        SharedPreferencesHelper.storeBluetoothNames(mBluetoothNameEntries, getContext());
        SharedPreferencesHelper.storeBluetoothDevices(mBluetoothDeviceEntries, getContext());
        SharedPreferencesHelper.setLastTimeUsed(mLastTimeUsedTextView.getText().toString(), getContext());
        SharedPreferencesHelper.setBluetoothStatus(mBluetoothConnectionTextView.getText().toString(), getContext());

        try {
            getActivity().unregisterReceiver(mReceiver);
        } catch (IllegalArgumentException ex) {
            ///TODO replace receiver with a class so we can check if is register or not
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Requests.REQUEST_SAVE_BLUETOOTH_DEVICE) {
            if (resultCode == SaveBluetoothDeviceFragment.SAVE_BLUETOOTH_DEVICE_OK) {
                mBluetoothNameEntries = SharedPreferencesHelper.getBluetoothNames(getContext());
                mBluetoothNameAdapter = new BluetoothNameAdapter(getActivity(), this, mBluetoothNameEntries);
                mBluetoothSpinner.setAdapter(mBluetoothNameAdapter);
            }
        }
        if (requestCode == Requests.REQUEST_DELETE_BLUETOOTH_NAME) {
            if (resultCode == DeleteBluetoothNameFragment.DELETE_BLUETOOTH_NAME_OK) {
                mBluetoothNameEntries = SharedPreferencesHelper.getBluetoothNames(getContext());
                mBluetoothNameAdapter = new BluetoothNameAdapter(getActivity(), this, mBluetoothNameEntries);
                mBluetoothSpinner.setAdapter(mBluetoothNameAdapter);
            }
        }
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device == null || device.getName() == null) {
                    return;
                }
                if (mBluetoothOperation == Utils.BLUETOOTH_FOUND_OPERATION.CONNECT) {
                    if (device.getName().equals(mSelectedBluetoothName.getName())) {
                        connectToBluetooth(device);
                    }
                }
                else{
                    if (mBluetoothOperation == Utils.BLUETOOTH_FOUND_OPERATION.ADD_TO_LIST) {
                        String name = device.getName();
                        String address = device.getAddress();
                        if (address != null) {
                            mBluetoothDeviceEntries.add(new BluetoothDeviceItem(name, address));
                            mBluetoothDeviceAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }
        }
    };

    private void disconnectFromBluetooth() {
        //close the previous connection
        StoreController.getInstance(getActivity()).getBluetoothIsAlive().stopSending();

        StoreController.getInstance(getActivity()).getBluetoothConnection().write(
                StoreController.getInstance(getActivity()).getSenderProcessor().createCloseRequest(
                        StoreController.getInstance(getActivity()).getEncryption().getHash()
                )
        );

        StoreController.getInstance(getActivity()).getBluetoothConnection().write(
                StoreController.getInstance(getActivity()).getSenderProcessor().createSetLastTimeUsedRequest(
                        System.currentTimeMillis()
                )
        );
        StoreController.getInstance(getContext()).getBluetoothConnection().close();

        if (mSelectedBluetoothName != null) {
            SharedPreferencesHelper.setBluetoothSelectedMac(getActivity(), "");
            StoreController.getInstance(getActivity()).getEncryption().storeHash(mSelectedBluetoothName.getMac());
        }

        try { //workaround - wait to receive all the messages
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //remove all the previous messages
        UIUpdater.getInstance().clear();
        StoreController.getInstance(getContext()).getCommandManager().removeAll();


        UIUpdater.getInstance().addRequest(Requests.REQUEST_UPDATE_TIME, getResources().getString(R.string.unknown));
        UIUpdater.getInstance().addRequest(Requests.REQUEST_UPDATE_STATUS, getResources().getString(R.string.bluetoothDisconnect));
        UIUpdater.getInstance().addRequest(Requests.REQUEST_CONNECT_BLUETOOTH, getResources().getString(R.string.no_connection));
    }

    private void connectToBluetooth(BluetoothDevice device) {
        if (mSelectedBluetoothName != null) {
            SharedPreferencesHelper.setBluetoothSelectedMac(getActivity(), mSelectedBluetoothName.getMac());
            StoreController.getInstance(getActivity()).getEncryption().readHash(mSelectedBluetoothName.getMac());
        }

        //update the view for the new connection
        UIUpdater.getInstance().addRequest(Requests.REQUEST_UPDATE_STATUS, getResources().getString(R.string.connecting));
        UIUpdater.getInstance().addRequest(Requests.REQUEST_CONNECT_BLUETOOTH, obtainDisplayName(device));

        StoreController.getInstance(getActivity())
                .getBluetoothConnection()
                .connect(((MainActivity) getActivity()).getBluetoothAdapter(), device);
        try {
            getActivity().unregisterReceiver(mReceiver);
        } catch (IllegalArgumentException ex) {
            ///TODO replace receiver with a class so we can check if is register or not
        }
    }

    private String obtainDisplayName(BluetoothDevice device) {
        List<BluetoothNameItem> entries = SharedPreferencesHelper.getBluetoothNames(getContext());
        for (BluetoothNameItem entry : entries) {
            if (entry.getMac().equals(device.getAddress())) {
                return entry.getDisplayName();
            }
        }
        return device.getName();
    }

    @Override
    public void onItemLongClicked(View view) {
        mBluetoothSpinner.onDetachedFromWindow();

        final int position = (int) view.getTag(R.string.bluetooth_name_tag);

        BluetoothNameItem item = mBluetoothNameAdapter.getItem(position);

        FragmentManager fm = getActivity().getSupportFragmentManager();
        DeleteBluetoothNameFragment dialog = DeleteBluetoothNameFragment.newInstance(item);
        dialog.setTargetFragment(
                getActivity().getSupportFragmentManager().findFragmentByTag("Bluetooth"),
                Requests.REQUEST_DELETE_BLUETOOTH_NAME
        );
        dialog.show(fm, SaveBluetoothDeviceFragment.TITLE);
    }

    @Override
    public void onItemClicked(View view) {
        mBluetoothSpinner.onDetachedFromWindow();
        final int pos = (int) view.getTag(R.string.bluetooth_name_tag);

        mBluetoothNameEntries = SharedPreferencesHelper.getBluetoothNames(getContext());
        BluetoothNameItem item = mBluetoothNameEntries.get(pos);
        mBluetoothNameEntries.remove(item);
        mBluetoothNameEntries.add(0, item);

        mBluetoothNameAdapter = new BluetoothNameAdapter(getActivity(), this, mBluetoothNameEntries);
        mBluetoothSpinner.setAdapter(mBluetoothNameAdapter);

        mBluetoothSpinner.setSelection(0);
    }
}
