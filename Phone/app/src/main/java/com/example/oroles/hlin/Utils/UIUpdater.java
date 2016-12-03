package com.example.oroles.hlin.Utils;


import android.os.Handler;

import com.example.oroles.hlin.Database.DatabaseEntry;
import com.example.oroles.hlin.Database.DatabaseNote;
import com.example.oroles.hlin.InterfacesControllers.IStore;
import com.example.oroles.hlin.Listeners.IConnectedBluetoothListener;
import com.example.oroles.hlin.Listeners.IExpireEntryListener;
import com.example.oroles.hlin.Listeners.IIsAliveBluetoothListener;
import com.example.oroles.hlin.Listeners.IReceivedAddMessageListener;
import com.example.oroles.hlin.Listeners.IReceivedAddNoteListener;
import com.example.oroles.hlin.Listeners.IReceivedStoredInBufferListener;

import java.util.concurrent.ConcurrentLinkedQueue;

public class UIUpdater
        implements IIsAliveBluetoothListener,
                   IConnectedBluetoothListener,
                   IReceivedStoredInBufferListener,
                   IReceivedAddMessageListener,
                   IReceivedAddNoteListener,
                   IExpireEntryListener{

    private ConcurrentLinkedQueue<String> mConnectionQueue;
    private ConcurrentLinkedQueue<String> mLastTimeQueue;
    private ConcurrentLinkedQueue<String> mStoreInBufferQueue;
    private ConcurrentLinkedQueue<DatabaseEntry> mAddMessagesQueue;
    private ConcurrentLinkedQueue<DatabaseNote> mAddNoteQueue;
    private ConcurrentLinkedQueue<Integer> mExpiredEntriesQueue;
    private ConcurrentLinkedQueue<String> mBluetoothNameQueue;
    private ConcurrentLinkedQueue<Handler> mHandlers;
    private boolean mStop;

    private Runnable runnable;
    private Thread thread;

    private static UIUpdater instance;

    static public UIUpdater getInstance() {
        if (instance == null) {
            instance = new UIUpdater();
        }
        return instance;
    }

    private UIUpdater() {
        mConnectionQueue = new ConcurrentLinkedQueue<>();
        mLastTimeQueue = new ConcurrentLinkedQueue<>();
        mStoreInBufferQueue = new ConcurrentLinkedQueue<>();
        mAddMessagesQueue = new ConcurrentLinkedQueue<>();
        mAddNoteQueue = new ConcurrentLinkedQueue<>();
        mExpiredEntriesQueue = new ConcurrentLinkedQueue<>();
        mBluetoothNameQueue = new ConcurrentLinkedQueue<>();
        mHandlers = new ConcurrentLinkedQueue<>();

        mStop = true;

        runnable = new Runnable() {
            @Override
            public void run() {
                runThread();
            }
        };

        thread = new Thread(runnable);
        thread.start();
    }

    public void startThread(IStore store) {
        if (mStop) {
            mStop = false;
            thread = new Thread(runnable);
            thread.start();

            store.getBluetoothIsAlive().addIsAliveBluetoothListener(this);
            store.getBluetoothConnection().addConnectedBluetoothListener(this);
            store.getReceivedProcessor().addReceivedStoredInBufferListener(this);
            store.getReceivedProcessor().addReceivedAddMessageListener(this);
            store.getExpireEntry().addExpireEntryListener(this);
            store.getReceivedProcessor().addReceivedAddNoteMessageListener(this);
        }
    }

    public void register(Handler handler) {
        mHandlers.add(handler);
    }

    public void deregister(Handler handler) {
        mHandlers.remove(handler);
    }

    public void addRequest(int request, Object value) {
        if (request == Requests.REQUEST_CONNECT_BLUETOOTH) {
            mBluetoothNameQueue.add((String)value);
            return;
        }
        if (request == Requests.REQUEST_UPDATE_STATUS) {
            mConnectionQueue.add((String)value);
            return;
        }
        if (request == Requests.REQUEST_UPDATE_TIME) {
            mLastTimeQueue.add((String)value);
            return;
        }
        if (request == Requests.REQUEST_STORE_IN_BUFFER) {
            mStoreInBufferQueue.add((String)value);
            return;
        }
        if (request == Requests.REQUEST_ADD_DATABASE_ENTRY) {
            mAddMessagesQueue.add((DatabaseEntry)value);
            return;
        }
        if (request == Requests.REQUEST_EXPIRED_ENTRIES) {
            mExpiredEntriesQueue.add(0);
            return;
        }
        if (request == Requests.REQUEST_ADD_DATABASE_NOTE) {
            mAddNoteQueue.add((DatabaseNote)value);
            return;
        }
    }

    private void runThread() {
        try {
            while (!mStop) {
                if (!mConnectionQueue.isEmpty()) {
                    String msg = mConnectionQueue.poll();
                    for (Handler handler : mHandlers) {
                        handler.obtainMessage(Requests.REQUEST_UPDATE_STATUS, msg).sendToTarget();
                    }
                }
                if (!mLastTimeQueue.isEmpty()) {
                    String msg = mLastTimeQueue.poll();
                    for (Handler handler : mHandlers) {
                        handler.obtainMessage(Requests.REQUEST_UPDATE_TIME, msg).sendToTarget();
                    }
                }
                if (!mStoreInBufferQueue.isEmpty()) {
                    String msg = mStoreInBufferQueue.poll();
                    for (Handler handler : mHandlers) {
                        handler.obtainMessage(Requests.REQUEST_STORE_IN_BUFFER, msg).sendToTarget();
                    }
                }
                if (!mAddMessagesQueue.isEmpty()) {
                    DatabaseEntry entry = mAddMessagesQueue.poll();
                    for (Handler handler : mHandlers) {
                        handler.obtainMessage(Requests.REQUEST_ADD_DATABASE_ENTRY, entry).sendToTarget();
                    }
                }
                if (!mExpiredEntriesQueue.isEmpty()) {
                    mExpiredEntriesQueue.poll();
                    for (Handler handler : mHandlers) {
                        handler.obtainMessage(Requests.REQUEST_EXPIRED_ENTRIES, null).sendToTarget();
                    }
                }
                if (!mBluetoothNameQueue.isEmpty()) {
                    String msg = mBluetoothNameQueue.poll();
                    for (Handler handler : mHandlers) {
                        handler.obtainMessage(Requests.REQUEST_CONNECT_BLUETOOTH, msg).sendToTarget();
                    }
                }
                if (!mAddNoteQueue.isEmpty()) {
                    DatabaseNote note = mAddNoteQueue.poll();
                    for (Handler handler : mHandlers) {
                        handler.obtainMessage(Requests.REQUEST_ADD_DATABASE_NOTE, note).sendToTarget();
                    }
                }
                Thread.sleep(100);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void stopThread(IStore store) {
        mStop = true;

        store.getBluetoothIsAlive().removeIsAliveBluetoothListener(this);
        store.getBluetoothConnection().removeConnectedBluetoothListener(this);
        store.getReceivedProcessor().removeReceivedStoredInBufferListener(this);
        store.getReceivedProcessor().removeReceivedAddMessageListener(this);
        store.getExpireEntry().removeExpireEntryListener(this);
        store.getReceivedProcessor().removeReceivedAddNoteMessageListener(this);
    }

    public void clear() {
        mConnectionQueue.clear();
        mLastTimeQueue.clear();
        mStoreInBufferQueue.clear();
        mAddMessagesQueue.clear();
        mAddNoteQueue.clear();
        mExpiredEntriesQueue.clear();
        mBluetoothNameQueue.clear();
    }

    @Override
    public void updateIsAliveBluetoothListener(String status) {
        mConnectionQueue.add(status);
    }

    @Override
    public void updateConnectedBluetooth(String connectionStatus) {
        mConnectionQueue.add(connectionStatus);
    }

    @Override
    public void updateLastTimeUsedBluetooth(String time) {
        mLastTimeQueue.add(time);
    }

    @Override
    public void updateReceivedStoredInBuffer(String message) {
        mStoreInBufferQueue.add(message);
    }

    @Override
    public void updateReceivedAddMessage(DatabaseEntry entry) {
        mAddMessagesQueue.add(entry);
    }

    @Override
    public void updateExpireEntry() {
        mExpiredEntriesQueue.add(0);
    }

    @Override
    public void updateReceiveAddNoteListener(DatabaseNote note) {
        mAddNoteQueue.add(note);
    }
}
