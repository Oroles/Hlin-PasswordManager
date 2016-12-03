package com.example.oroles.hlin.Fragments;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.example.oroles.hlin.R;

public class SettingsFragment extends PreferenceFragment {

    public static final String CHECK_EXPIRED_PASSWORDS = "checkExpiredPasswords";
    public static final String DAYS_EXPIRED_PASSWORDS = "daysExpiredPasswords";
    public static final String EXPIRED_PASSWORDS_ACTION = "expirePasswordsAction";

    public static final String CHECK_BLUETOOTH_CONNECTION = "sendIsAlivePackages";
    public static final String FREQUENCY_BLUETOOTH_CONNECTION = "numberOfSecondsIsAlivePackages";
    public static final String BLUETOOTH_CONNECTION_ACTION = "isAliveAction";

    public static final String WRONG_DEVICE_ACTION = "wrongDeviceAction";

    public static final String SEARCH_ACTION = "searchAction";

    public static final String SALT_FOR_KEY = "saltForKey";

    public static final String ENFORCE_MINIMUM_LENGTH = "enforceMinimumLength";
    public static final String CHECK_FOR_COMMON_PASSWORDS = "checkForCommonPasswords";

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

        addPreferencesFromResource(R.xml.fragment_preference);
    }
}
