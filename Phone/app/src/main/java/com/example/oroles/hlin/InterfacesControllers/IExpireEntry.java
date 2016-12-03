package com.example.oroles.hlin.InterfacesControllers;

import com.example.oroles.hlin.Listeners.IExpireEntryListener;

public interface IExpireEntry {

    void start();
    void stop();

    void addExpireEntryListener(IExpireEntryListener listener);
    void removeExpireEntryListener(IExpireEntryListener listener);
}
