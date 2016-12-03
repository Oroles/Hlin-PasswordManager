package com.example.oroles.hlin.ReceivedMessage;

import com.example.oroles.hlin.Controllers.ReceivedProcessorController;
import com.example.oroles.hlin.InterfacesControllers.IStore;
import com.example.oroles.hlin.Utils.Utils;

public class ReceivedStoredInBuffer extends ReceivedMessage {

    public ReceivedStoredInBuffer(IStore store, ReceivedProcessorController receivedProcessorController, String message) {
        super(store, receivedProcessorController, message);
    }

    @Override
    public void execute() {
        mReceivedProcessorController.notifyReceivedStoredInBufferListeners(Utils.getFirstMessage(mMessage));
    }
}
