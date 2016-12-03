package com.example.oroles.hlin.ReceivedMessage;

import android.preference.PreferenceManager;

import com.example.oroles.hlin.Controllers.ReceivedProcessorController;
import com.example.oroles.hlin.Fragments.SettingsFragment;
import com.example.oroles.hlin.InterfacesControllers.IStore;
import com.example.oroles.hlin.Utils.Utils;

public class ReceivedHashValue extends ReceivedMessage {

    public ReceivedHashValue(IStore store, ReceivedProcessorController receivedProcessorController, String message) {
        super(store, receivedProcessorController, message);
    }

    @Override
    public void execute() {
        String hash = Utils.getHash(mMessage);
        if (mStore.getEncryption().compareHash(hash)) {
            mStore.getBluetoothConnection().notifyConnectedBluetoothListeners("Connected-Correct Hash");

            mStore.getBluetoothConnection().write(
                    mStore.getSenderProcessor().createDecryptKey(
                        PreferenceManager.getDefaultSharedPreferences(mStore.getContext())
                                         .getString(SettingsFragment.SALT_FOR_KEY, "0")
            ));

            mStore.getBluetoothConnection().write(
                    mStore.getSenderProcessor().createLastTimeUsedRequest());

            if (PreferenceManager.getDefaultSharedPreferences(mStore.getContext())
                    .getBoolean(SettingsFragment.CHECK_BLUETOOTH_CONNECTION, true)) {
                mStore.getBluetoothIsAlive().startSending();
            }
        }
        else {
            String action = PreferenceManager.getDefaultSharedPreferences(mStore.getContext())
                    .getString(SettingsFragment.WRONG_DEVICE_ACTION, "Nothing");
            if (action.equals("CloseConnection")) {
                mStore.getBluetoothIsAlive().stopSending();
                mStore.getBluetoothConnection().close();
                mStore.getBluetoothConnection().notifyConnectedBluetoothListeners("Disconnected-Wrong Hash");
            } else {
                mStore.getBluetoothConnection().notifyConnectedBluetoothListeners("Wrong Hash");
            }
        }
    }
}
