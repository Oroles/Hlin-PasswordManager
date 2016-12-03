package com.example.oroles.hlin.Listeners;

import com.example.oroles.hlin.Database.DatabaseEntry;

public interface IReceivedAddMessageListener {
    void updateReceivedAddMessage(DatabaseEntry entry);
}
