package com.example.oroles.hlin.ReceivedMessage;

import com.example.oroles.hlin.Controllers.ReceivedProcessorController;
import com.example.oroles.hlin.Database.DatabaseNote;
import com.example.oroles.hlin.InterfacesControllers.IStore;
import com.example.oroles.hlin.Utils.Utils;


public class ReceivedAddNote extends ReceivedMessage {

    public ReceivedAddNote(IStore store, ReceivedProcessorController receivedProcessorController, String message) {
        super(store, receivedProcessorController, message);
    }

    @Override
    public void execute() {
        String title = Utils.getTitle(mMessage);
        String text = Utils.getNote(mMessage);
        DatabaseNote note = new DatabaseNote(title, text);
        if (mStore.getRepository().addNote(note)) {
            mReceivedProcessorController.notifyReceivedAddNoteMessageListeners(note);
        }
        mStore.getEncryption().appendHash("AddNote");
    }
}
