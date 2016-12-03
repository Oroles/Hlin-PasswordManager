package com.example.oroles.hlin.InterfacesControllers;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import com.example.oroles.hlin.Listeners.IConnectedBluetoothListener;
import com.example.oroles.hlin.Listeners.IReceivedMessageListener;

public interface IBluetoothConnection {
    void connect(BluetoothAdapter adapter, BluetoothDevice device);
    boolean write(String message);
    boolean isConnected();
    void close();

    void addConnectedBluetoothListener(IConnectedBluetoothListener listener);
    void removeConnectedBluetoothListener(IConnectedBluetoothListener listener);
    void addReceivedMessageListener(IReceivedMessageListener listener);
    void removeReceivedMessageListener(IReceivedMessageListener listener);

    void notifyConnectedBluetoothListeners(String status);
    void notifyLastTimeUsedListeners(String time);
}
