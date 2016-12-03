package com.example.oroles.hlin.InterfacesControllers;

import android.content.Context;

public interface IStore {

    IBluetoothConnection getBluetoothConnection();
    IRepository getRepository();
    IReceivedProcessor getReceivedProcessor();
    ISenderProcessor getSenderProcessor();
    IEncryption getEncryption();
    IBluetoothIsAlive getBluetoothIsAlive();
    IExpireEntry getExpireEntry();
    ICommandManager getCommandManager();


    void addContext(Context context);
    Context getContext();
}
