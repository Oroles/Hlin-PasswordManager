package com.example.oroles.hlin.Controllers;

import com.example.oroles.hlin.Database.DatabaseEntry;
import com.example.oroles.hlin.Database.DatabaseNote;
import com.example.oroles.hlin.InterfacesControllers.IReceivedProcessor;
import com.example.oroles.hlin.InterfacesControllers.IStore;
import com.example.oroles.hlin.Listeners.IReceivedAddMessageListener;
import com.example.oroles.hlin.Listeners.IReceivedAddNoteListener;
import com.example.oroles.hlin.Listeners.IReceivedMessageListener;
import com.example.oroles.hlin.Listeners.IReceivedStoredInBufferListener;
import com.example.oroles.hlin.ReceivedMessage.IReceivedMessage;
import com.example.oroles.hlin.ReceivedMessage.ReceivedAddGeneratedMessage;
import com.example.oroles.hlin.ReceivedMessage.ReceivedAddMessage;
import com.example.oroles.hlin.ReceivedMessage.ReceivedAddNote;
import com.example.oroles.hlin.ReceivedMessage.ReceivedErrorMessage;
import com.example.oroles.hlin.ReceivedMessage.ReceivedHashValue;
import com.example.oroles.hlin.ReceivedMessage.ReceivedIsAliveMessage;
import com.example.oroles.hlin.ReceivedMessage.ReceivedLastTimeUsedMessage;
import com.example.oroles.hlin.ReceivedMessage.ReceivedStoredInBuffer;
import com.example.oroles.hlin.Utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class ReceivedProcessorController implements IReceivedProcessor, IReceivedMessageListener {

    private IStore mStore;
    private List<IReceivedAddMessageListener> mReceivedAddMessageListener;
    private List<IReceivedStoredInBufferListener> mReceivedStoredInBufferListener;
    private List<IReceivedAddNoteListener> mReceivedAddNoteMessageListener;

    public ReceivedProcessorController(IStore store) {
        mStore = store;
        mReceivedAddMessageListener = new ArrayList<>();
        mReceivedStoredInBufferListener = new ArrayList<>();
        mReceivedAddNoteMessageListener = new ArrayList<>();
        mStore.getBluetoothConnection().addReceivedMessageListener(this);
    }

    @Override
    public void updateReceivedMessage(String message) {
        //move them on another thread
        IReceivedMessage receivedMessage;
        switch (Utils.getMessageType(message)) {
            case Utils.ADD_ENTRY_TYPE: {
                if (mStore.getCommandManager().existCommand(Utils.ADD_ENTRY_TYPE)) {
                    mStore.getCommandManager().removeAll(Utils.ADD_ENTRY_TYPE);
                    receivedMessage = new ReceivedAddMessage(mStore, this, message);
                }
                else {
                    receivedMessage = new ReceivedErrorMessage(mStore, this, "Unexpected Add entry message");
                }
                break;

            }
            case Utils.ADD_NOTE_TYPE: {
                if (mStore.getCommandManager().existCommand(Utils.ADD_NOTE_TYPE)) {
                    mStore.getCommandManager().removeAll(Utils.ADD_NOTE_TYPE);
                    receivedMessage = new ReceivedAddNote(mStore, this, message);
                }
                else {
                    receivedMessage = new ReceivedErrorMessage(mStore, this, "Unexpected Add note message");
                }
                break;
            }
            case Utils.RETRIEVE_HASH_MESSAGE_TYPE: {
                if (mStore.getCommandManager().existCommand(Utils.RETRIEVE_HASH_MESSAGE_TYPE)) {
                    mStore.getCommandManager().removeAll(Utils.RETRIEVE_HASH_MESSAGE_TYPE);
                    receivedMessage = new ReceivedHashValue(mStore, this, message);
                }
                else {
                    receivedMessage = new ReceivedErrorMessage(mStore, this, "Unexpected Hash message");
                }
                break;
            }
            case Utils.ADD_GENERATE_ENTRY_TYPE: {
                if (mStore.getCommandManager().existCommand(Utils.ADD_GENERATE_ENTRY_TYPE)) {
                    mStore.getCommandManager().removeAll(Utils.ADD_GENERATE_ENTRY_TYPE);
                    receivedMessage = new ReceivedAddGeneratedMessage(mStore, this, message);
                }
                else {
                    receivedMessage = new ReceivedErrorMessage(mStore, this, "Unexpected Add and generate entry message");
                }
                break;
            }
            case Utils.IS_ALIVE_MESSAGE_TYPE: {
                if (mStore.getCommandManager().existCommand(Utils.IS_ALIVE_MESSAGE_TYPE)) {
                    receivedMessage = new ReceivedIsAliveMessage(mStore, this, message);
                }
                else {
                    receivedMessage = new ReceivedErrorMessage(mStore, this, "Unexpected Is alive message");
                }
                break;
            }
            case Utils.GET_STORED_IN_BUFFER: {
                if (mStore.getCommandManager().existCommand(Utils.ADD_GENERATE_ENTRY_TYPE) ||
                        mStore.getCommandManager().existCommand(Utils.ADD_ENTRY_TYPE) ||
                        mStore.getCommandManager().existCommand(Utils.ADD_NOTE_TYPE) ||
                        mStore.getCommandManager().existCommand(Utils.REQUEST_NOTE_PASSWORD_TYPE) ||
                        mStore.getCommandManager().existCommand(Utils.REQUEST_PASSWORD_MESSAGE_TYPE)){
                    receivedMessage = new ReceivedStoredInBuffer(mStore, this, message);
                }
                else {
                    receivedMessage = new ReceivedErrorMessage(mStore, this, "Unexpected Store in buffer message");
                }
                break;
            }
            case Utils.LAST_TIME_USED: {
                if (mStore.getCommandManager().existCommand(Utils.LAST_TIME_USED)) {
                    receivedMessage = new ReceivedLastTimeUsedMessage(mStore, this, message);
                }
                else {
                    receivedMessage = new ReceivedErrorMessage(mStore, this, "Unexpected Last time used message");
                }
                break;
            }
            default: {
                receivedMessage = new ReceivedErrorMessage(mStore, this, "Unknown message: " + message);
                break;
            }

        }
        receivedMessage.execute();
    }

    @Override
    public void addReceivedAddMessageListener(IReceivedAddMessageListener listener) {
        mReceivedAddMessageListener.add(listener);
    }

    @Override
    public void removeReceivedAddMessageListener(IReceivedAddMessageListener listener) {
        mReceivedAddMessageListener.remove(listener);
    }

    @Override
    public void addReceivedStoredInBufferListener(IReceivedStoredInBufferListener listener) {
        mReceivedStoredInBufferListener.add(listener);
    }

    @Override
    public void removeReceivedStoredInBufferListener(IReceivedStoredInBufferListener listener) {
        mReceivedStoredInBufferListener.remove(listener);
    }

    @Override
    public void addReceivedAddNoteMessageListener(IReceivedAddNoteListener listener) {
        mReceivedAddNoteMessageListener.add(listener);
    }

    @Override
    public void removeReceivedAddNoteMessageListener(IReceivedAddNoteListener listener) {
        mReceivedAddNoteMessageListener.remove(listener);
    }

    public void notifyReceivedAddMessageListeners(DatabaseEntry entry) {
        for (IReceivedAddMessageListener listener : mReceivedAddMessageListener) {
            listener.updateReceivedAddMessage(entry);
        }
    }

    public void notifyReceivedStoredInBufferListeners(String message) {
        for (IReceivedStoredInBufferListener listener : mReceivedStoredInBufferListener) {
            listener.updateReceivedStoredInBuffer(message);
        }
    }

    public void notifyReceivedAddNoteMessageListeners(DatabaseNote note) {
        for (IReceivedAddNoteListener listener : mReceivedAddNoteMessageListener) {
            listener.updateReceiveAddNoteListener(note);
        }
    }
}
