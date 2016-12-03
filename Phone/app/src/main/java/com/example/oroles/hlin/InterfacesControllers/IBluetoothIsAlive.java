package com.example.oroles.hlin.InterfacesControllers;

import com.example.oroles.hlin.Listeners.IIsAliveBluetoothListener;

public interface IBluetoothIsAlive {

    void startSending();
    void stopSending();

    void receivedMessage();

    void addIsAliveBluetoothListener(IIsAliveBluetoothListener listener);
    void removeIsAliveBluetoothListener(IIsAliveBluetoothListener listener);
}
