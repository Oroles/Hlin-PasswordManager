package com.example.oroles.hlin.Controllers;

import android.os.AsyncTask;
import android.preference.PreferenceManager;

import com.example.oroles.hlin.Database.DatabaseEntry;
import com.example.oroles.hlin.Fragments.SettingsFragment;
import com.example.oroles.hlin.InterfacesControllers.IExpireEntry;
import com.example.oroles.hlin.InterfacesControllers.IStore;
import com.example.oroles.hlin.Listeners.IExpireEntryListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ExpireEntryController implements IExpireEntry {

    private IStore mStore;
    private List<IExpireEntryListener> mExpireEntryListeners;

    private CheckDateTask task;

    public ExpireEntryController(IStore store) {
        mStore = store;
        mExpireEntryListeners = new ArrayList<>();
    }

    @Override
    public void start() {
        task = new CheckDateTask();
        task.execute();
    }

    @Override
    public void stop() {
        if (task != null) {
            task.cancel(false);
        }
    }

    @Override
    public void addExpireEntryListener(IExpireEntryListener listener) {
        mExpireEntryListeners.add(listener);
    }

    @Override
    public void removeExpireEntryListener(IExpireEntryListener listener) {
        mExpireEntryListeners.remove(listener);
    }

    void notifyExpireEntryListeners() {
        for (IExpireEntryListener listener : mExpireEntryListeners) {
            listener.updateExpireEntry();
        }
    }

    private class CheckDateTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            List<DatabaseEntry> entries = mStore.getRepository().getEntries();
            float nrOfDays = (Float.valueOf(PreferenceManager
                    .getDefaultSharedPreferences(mStore.getContext())
                    .getString(SettingsFragment.DAYS_EXPIRED_PASSWORDS, "0.0")));

            if (nrOfDays > 30) {
                nrOfDays = 30;
            }

            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH,-((int) nrOfDays));

            for (DatabaseEntry entry : entries) {

                if (calendar.getTime().after(new Date(entry.getTime()))) {
                    mStore.getRepository().setExpireStatus(entry, true);
                }
                else {
                    mStore.getRepository().setExpireStatus(entry, false);
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void items) {
            notifyExpireEntryListeners();
        }

    }
}
