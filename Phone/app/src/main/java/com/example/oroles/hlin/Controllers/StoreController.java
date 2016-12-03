package com.example.oroles.hlin.Controllers;

import android.content.Context;

import com.example.oroles.hlin.InterfacesControllers.IBluetoothConnection;
import com.example.oroles.hlin.InterfacesControllers.IBluetoothIsAlive;
import com.example.oroles.hlin.InterfacesControllers.ICommandManager;
import com.example.oroles.hlin.InterfacesControllers.IEncryption;
import com.example.oroles.hlin.InterfacesControllers.IExpireEntry;
import com.example.oroles.hlin.InterfacesControllers.IReceivedProcessor;
import com.example.oroles.hlin.InterfacesControllers.IRepository;
import com.example.oroles.hlin.InterfacesControllers.ISenderProcessor;
import com.example.oroles.hlin.InterfacesControllers.IStore;


public class StoreController implements IStore {

    private IRepository mRepository;
    private IBluetoothConnection mBluetoothConnection;
    private IReceivedProcessor mReceiveProcessor;
    private ISenderProcessor mSenderProcessor;
    private IEncryption mEncryption;
    private IBluetoothIsAlive mBluetoothIsAlive;
    private IExpireEntry mExpireEntry;
    private ICommandManager mCommandManager;
    private Context mContext;

    private static IStore mInstance;
    public static IStore getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new StoreController(context);
        }
        return mInstance;
    }

    private StoreController(Context context) {
        mContext = context;
        mRepository = new RepositoryController(this);
        mBluetoothConnection = new BluetoothConnectionController(this);
        mReceiveProcessor = new ReceivedProcessorController(this);
        mSenderProcessor = new SenderProcessorController(this);
        mEncryption = new EncryptionController(this);
        mBluetoothIsAlive = new BluetoothIsAliveController(this);
        mExpireEntry = new ExpireEntryController(this);
        mCommandManager = new CommandManagerController(this);
    }

    @Override
    public IBluetoothConnection getBluetoothConnection() {
        return mBluetoothConnection;
    }

    @Override
    public IRepository getRepository() {
        return mRepository;
    }

    @Override
    public IReceivedProcessor getReceivedProcessor() {
        return mReceiveProcessor;
    }

    @Override
    public ISenderProcessor getSenderProcessor() {
        return mSenderProcessor;
    }

    @Override
    public IEncryption getEncryption() {
        return mEncryption;
    }

    @Override
    public IBluetoothIsAlive getBluetoothIsAlive() {
        return mBluetoothIsAlive;
    }

    @Override
    public IExpireEntry getExpireEntry() {
        return mExpireEntry;
    }

    @Override
    public ICommandManager getCommandManager() { return mCommandManager;}

    @Override
    public void addContext(Context context) {
        mContext = context;
    }

    @Override
    public Context getContext() {
        return mContext;
    }
}
