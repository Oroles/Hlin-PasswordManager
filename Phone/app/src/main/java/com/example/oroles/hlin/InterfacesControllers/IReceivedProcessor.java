package com.example.oroles.hlin.InterfacesControllers;

import com.example.oroles.hlin.Listeners.IReceivedAddMessageListener;
import com.example.oroles.hlin.Listeners.IReceivedAddNoteListener;
import com.example.oroles.hlin.Listeners.IReceivedStoredInBufferListener;

public interface IReceivedProcessor {
    void addReceivedAddMessageListener(IReceivedAddMessageListener listener);
    void removeReceivedAddMessageListener(IReceivedAddMessageListener listener);

    void addReceivedStoredInBufferListener(IReceivedStoredInBufferListener listener);
    void removeReceivedStoredInBufferListener(IReceivedStoredInBufferListener listener);

    void addReceivedAddNoteMessageListener(IReceivedAddNoteListener listener);
    void removeReceivedAddNoteMessageListener(IReceivedAddNoteListener listener);
}
