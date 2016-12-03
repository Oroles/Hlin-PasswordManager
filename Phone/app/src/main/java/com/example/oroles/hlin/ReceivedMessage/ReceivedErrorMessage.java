package com.example.oroles.hlin.ReceivedMessage;

import com.example.oroles.hlin.Controllers.ReceivedProcessorController;
import com.example.oroles.hlin.InterfacesControllers.IStore;


public class ReceivedErrorMessage extends ReceivedMessage {

    public ReceivedErrorMessage(IStore store, ReceivedProcessorController receivedProcessorController, String message) {
        super(store, receivedProcessorController, message);
    }

    @Override
    public void execute() {
        mReceivedProcessorController.notifyReceivedStoredInBufferListeners(mMessage);
    }
}
