package com.example.oroles.hlin.Bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import com.example.oroles.hlin.Controllers.BluetoothConnectionController;
import com.example.oroles.hlin.InterfacesControllers.IStore;

import java.io.IOException;
import java.util.UUID;

public class ConnectThread extends Thread {
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private final BluetoothSocket mmSocket;
    private ConnectedThread mConnectedThread;

    private final BluetoothAdapter mBluetoothAdapter;
    private BluetoothConnectionController mBluetoothConnectionController;
    private IStore mStore;

    public ConnectThread(BluetoothConnectionController bluetoothConnectionController, IStore store,
                          BluetoothAdapter adapter, BluetoothDevice device) {
        mBluetoothAdapter = adapter;
        mBluetoothConnectionController = bluetoothConnectionController;
        mStore = store;

        // create temp object, because mmSocket is final
        BluetoothSocket tmp = null;

        // Get a BluetoothSocket to connect with the given BluetoothDevice
        try {
            // MY_UUID is the app's UUID string, also used by the server code
            tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mmSocket = tmp;
    }

    public void run() {
        // Cancel discovery because it will slow down the connection
        mBluetoothAdapter.cancelDiscovery();

        try {
            // Connect the device through the socket. This will block
            // until it succeeds or throws an exception
            mmSocket.connect();
        } catch (IOException connectException) {
            // Unable to connect; close the socket and get out
            mBluetoothConnectionController.notifyConnectedBluetoothListeners("Not connected");

            try {
                mmSocket.close();
            } catch (IOException closeException) {
                closeException.printStackTrace();
            }
            return;
        }

        // Do work to manage the connection (in a separate thread)
        mConnectedThread = new ConnectedThread(mBluetoothConnectionController, mmSocket);
        mConnectedThread.start();

        // let know that it managed to connect
        mBluetoothConnectionController.notifyConnectedBluetoothListeners("Connected-Press Button");
        // send the request to get the hash value
        this.write(mStore.getSenderProcessor().createGetHashRequest());
    }

    public void write(String reply) {
        if (!reply.equals("\n")) {
            if (mConnectedThread != null) {
                mConnectedThread.write(reply.getBytes());
            }
        }
    }

    public void close() {
        try {
            if (mConnectedThread != null) {
                mConnectedThread.close();
            }
            mmSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isConnected() {
        return mmSocket.isConnected();
    }
}
