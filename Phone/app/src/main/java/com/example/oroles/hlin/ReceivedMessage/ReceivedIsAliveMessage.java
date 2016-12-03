package com.example.oroles.hlin.ReceivedMessage;

import com.example.oroles.hlin.Controllers.ReceivedProcessorController;
import com.example.oroles.hlin.InterfacesControllers.IStore;

public class ReceivedIsAliveMessage extends ReceivedMessage {
    public ReceivedIsAliveMessage(IStore store, ReceivedProcessorController receivedProcessorController, String message) {
        super(store, receivedProcessorController, message);
    }

    @Override
    public void execute() {
        mStore.getBluetoothIsAlive().receivedMessage();
        mStore.getEncryption().appendHash("IsAlive");
    }
}
