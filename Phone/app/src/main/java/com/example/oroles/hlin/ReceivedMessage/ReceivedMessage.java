package com.example.oroles.hlin.ReceivedMessage;

import com.example.oroles.hlin.Controllers.ReceivedProcessorController;
import com.example.oroles.hlin.InterfacesControllers.IStore;

public abstract class ReceivedMessage implements IReceivedMessage {

    protected IStore mStore;
    protected ReceivedProcessorController mReceivedProcessorController;
    protected String mMessage;

    public ReceivedMessage(IStore store, ReceivedProcessorController receivedProcessorController, String message) {
        mStore = store;
        mReceivedProcessorController = receivedProcessorController;
        mMessage = message;
    }
}
