package com.example.oroles.hlin.ReceivedMessage;

import com.example.oroles.hlin.Controllers.ReceivedProcessorController;
import com.example.oroles.hlin.Database.DatabaseEntry;
import com.example.oroles.hlin.InterfacesControllers.IStore;
import com.example.oroles.hlin.Utils.Utils;

public class ReceivedAddMessage extends ReceivedMessage {

    public ReceivedAddMessage(IStore store, ReceivedProcessorController receivedProcessorController, String message) {
        super(store, receivedProcessorController, message);
    }

    @Override
    public void execute() {
        String website = Utils.getWebsite(mMessage);
        String username = Utils.getUsername(mMessage, Utils.SPLITTER);
        String password = Utils.getPassword(mMessage);
        DatabaseEntry entry = new DatabaseEntry(website, username, password);
        if ( mStore.getRepository().addEntry(entry) ) {
            mReceivedProcessorController.notifyReceivedAddMessageListeners(entry);
        }

        mStore.getEncryption().appendHash("Add");
    }
}
