package com.example.oroles.hlin.Controllers;

import android.preference.PreferenceManager;

import com.example.oroles.hlin.Fragments.SettingsFragment;
import com.example.oroles.hlin.InterfacesControllers.IBluetoothIsAlive;
import com.example.oroles.hlin.InterfacesControllers.IStore;
import com.example.oroles.hlin.Listeners.IIsAliveBluetoothListener;
import com.example.oroles.hlin.R;

import java.util.ArrayList;
import java.util.List;


public class BluetoothIsAliveController implements IBluetoothIsAlive, Runnable {

    private IStore mStore;
    private boolean mStop;
    private boolean mReceived;

    List<IIsAliveBluetoothListener> mIsAliveBluetoothListeners;

    public BluetoothIsAliveController(IStore store) {
        mStore = store;
        mStop = false;
        mReceived = false;
        mIsAliveBluetoothListeners = new ArrayList<>();
    }

    void notifyIsAliveBluetoothListeners(String status) {
        for(IIsAliveBluetoothListener listener : mIsAliveBluetoothListeners) {
            listener.updateIsAliveBluetoothListener(status);
        }
    }

    @Override
    public void startSending() {
        mStop = false;
        Thread t = new Thread(this);
        t.start();
    }

    @Override
    public void stopSending() {
        mStop = true;
    }

    @Override
    public void receivedMessage() {
        mReceived = true;
    }

    @Override
    public void addIsAliveBluetoothListener(IIsAliveBluetoothListener listener) {
        mIsAliveBluetoothListeners.add(listener);
    }

    @Override
    public void removeIsAliveBluetoothListener(IIsAliveBluetoothListener listener) {
        mIsAliveBluetoothListeners.remove(listener);
    }

    @Override
    public void run() {
        try {
            Thread.sleep(1000);//add a sleep so will not send directly the message

            int sleepTime = Integer.valueOf(PreferenceManager
                    .getDefaultSharedPreferences(mStore.getContext())
                    .getString(SettingsFragment.FREQUENCY_BLUETOOTH_CONNECTION, "1"));

            if (sleepTime < 1) {
                sleepTime = 1;
            }
            else if (sleepTime > 60) {
                sleepTime = 60;
            }

            while (!mStop) {
                mStore.getBluetoothConnection().write(
                        mStore.getSenderProcessor().createIsAliveRequest());

                Thread.sleep(sleepTime * 1000);

                String action = PreferenceManager.getDefaultSharedPreferences(mStore.getContext())
                        .getString(SettingsFragment.BLUETOOTH_CONNECTION_ACTION, "Nothing");

                String status;
                if (action.equals("CloseConnection")) {
                    if (!mReceived) {
                        StoreController.getInstance(mStore.getContext()).getBluetoothIsAlive().stopSending();
                        StoreController.getInstance(mStore.getContext()).getBluetoothConnection().close();
                        status = mStore.getContext().getResources().getString(R.string.ConnectedNotAlive);
                    }
                    else {
                        status = mStore.getContext().getResources().getString(R.string.ConnectedAlive);
                    }
                }
                else {
                    if (!mReceived) {
                        status = mStore.getContext().getResources().getString(R.string.ConnectedNotAlive);
                    } else {
                        status = mStore.getContext().getResources().getString(R.string.ConnectedAlive);
                    }
                }
                //SharedPreferencesHelper.setBluetoothStatus(mStore.getContext(), status);
                notifyIsAliveBluetoothListeners(status);
                //UIUpdater.getInstance().addRequest(Requests.REQUEST_UPDATE_STATUS, status);
                mReceived = false;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
