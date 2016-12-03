package com.example.oroles.hlin.Controllers;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import com.example.oroles.hlin.Bluetooth.ConnectThread;
import com.example.oroles.hlin.Listeners.IConnectedBluetoothListener;
import com.example.oroles.hlin.Listeners.IReceivedMessageListener;
import com.example.oroles.hlin.InterfacesControllers.IBluetoothConnection;
import com.example.oroles.hlin.InterfacesControllers.IStore;

import java.util.ArrayList;
import java.util.List;


public class BluetoothConnectionController implements IBluetoothConnection {

    private IStore mStore;

    private ConnectThread mConnectThread;

    private List<IConnectedBluetoothListener> mBluetoothConnectionStatusListeners;
    private List<IReceivedMessageListener> mReceiveMessageListeners;

    public BluetoothConnectionController(IStore store) {
        mStore = store;
        mBluetoothConnectionStatusListeners = new ArrayList<>();
        mReceiveMessageListeners = new ArrayList<>();
    }

    @Override
    public void connect(BluetoothAdapter adapter, BluetoothDevice device) {
        mConnectThread = new ConnectThread(this, mStore, adapter, device);
        mConnectThread.start();
    }

    @Override
    public boolean write(String message) {
        if (mConnectThread != null) {
            mConnectThread.write(message);
            return true;
        }
        return false;
    }

    @Override
    public boolean isConnected() {
        if (mConnectThread != null) {
            return mConnectThread.isConnected();
        }
        return false;
    }

    @Override
    public void close() {
        if (mConnectThread != null) {
            mConnectThread.close();
        }
    }

    @Override
    public void addConnectedBluetoothListener(IConnectedBluetoothListener listener) {
        mBluetoothConnectionStatusListeners.add(listener);
    }

    @Override
    public void removeConnectedBluetoothListener(IConnectedBluetoothListener listener) {
        mBluetoothConnectionStatusListeners.remove(listener);
    }

    @Override
    public void addReceivedMessageListener(IReceivedMessageListener listener) {
        mReceiveMessageListeners.add(listener);
    }

    @Override
    public void removeReceivedMessageListener(IReceivedMessageListener listener) {
        mReceiveMessageListeners.remove(listener);
    }

    @Override
    public void notifyConnectedBluetoothListeners(String status) {
        for (IConnectedBluetoothListener listener : mBluetoothConnectionStatusListeners ) {
            listener.updateConnectedBluetooth(status);
        }
    }

    @Override
    public void notifyLastTimeUsedListeners(String time) {
        for (IConnectedBluetoothListener listener : mBluetoothConnectionStatusListeners) {
            listener.updateLastTimeUsedBluetooth(time);
        }
    }

    public void notifyReceiveMessageListeners(String message) {
        for (IReceivedMessageListener listener : mReceiveMessageListeners) {
            listener.updateReceivedMessage(message);
        }
    }
}
