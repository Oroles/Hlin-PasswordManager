package com.example.oroles.hlin.ReceivedMessage;

import com.example.oroles.hlin.Controllers.ReceivedProcessorController;
import com.example.oroles.hlin.InterfacesControllers.IStore;

import java.util.Date;

public class ReceivedLastTimeUsedMessage extends ReceivedMessage {

    public ReceivedLastTimeUsedMessage(IStore store, ReceivedProcessorController receivedProcessorController, String message) {
        super(store, receivedProcessorController, message);
    }

    @Override
    public void execute() {
        long time = Long.valueOf(mMessage.substring(2, mMessage.indexOf('\t')));
        mStore.getBluetoothConnection().notifyLastTimeUsedListeners(
                android.text.format.DateFormat.format("yyyy-MM-dd kk:mm:ss", new Date(time)).toString());
    }
}
