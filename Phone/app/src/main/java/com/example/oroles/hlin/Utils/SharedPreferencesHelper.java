package com.example.oroles.hlin.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.oroles.hlin.Fragments.Items.BluetoothDeviceItem;
import com.example.oroles.hlin.Fragments.Items.BluetoothNameItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SharedPreferencesHelper {

    public static final String BLUETOOTH_NAMES = "bluetoothNames";
    public static final String BLUETOOTH_DEVICES = "bluetoothDevices";
    public static final String BLUETOOTH_STATUS = "bluetoothStatus";
    public static final String BLUETOOTH_LAST_TIME_USED = "bluetoothLastTimeUsed";
    public static final String BLUETOOTH_CONNECTED_NAME = "bluetoothConnectedName";
    public static final String BLUETOOTH_HASHES = "bluetoothHashes";
    public static final String BLUETOOTH_SELECTED_MAC = "bluetoothSelectedMac";

    public static void storeBluetoothNames(List<BluetoothNameItem> entries, Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();

        try {
            JSONArray jsonArray = new JSONArray();
            for(BluetoothNameItem entry : entries ) {
                JSONObject obj = new JSONObject();
                obj.put("ADDRESS", entry.getMac());
                obj.put("NAME", entry.getName());
                obj.put("DISPLAYNAME", entry.getDisplayName());
                obj.put("LASTUSETIME", entry.getLastUsedTime());
                obj.put("CREATEDTIME", entry.getCreatedTime());
                jsonArray.put(obj);
            }
            editor.putString(BLUETOOTH_NAMES, jsonArray.toString());
            editor.apply();
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    public static void storeBluetoothName(BluetoothNameItem entry, Context context) {
        List<BluetoothNameItem> entries = getBluetoothNames(context);
        entries.add(0, entry);
        storeBluetoothNames(entries, context);
    }

    public static void storeBluetoothDevices(List<BluetoothDeviceItem> bluetoothDevices, Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();

        try {
            JSONArray jsonArray = new JSONArray();
            for (BluetoothDeviceItem device : bluetoothDevices) {
                JSONObject obj = new JSONObject();
                obj.put("NAME", device.getName());
                obj.put("ADDRESS", device.getAddress());
                jsonArray.put(obj);
            }
            editor.putString(BLUETOOTH_DEVICES, jsonArray.toString());
            editor.apply();
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    public static void clearBluetoothDevices(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(BLUETOOTH_DEVICES, "");
        editor.apply();
    }

    public static ArrayList<BluetoothNameItem> getBluetoothNames(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String json = prefs.getString(BLUETOOTH_NAMES, "");
        ArrayList<BluetoothNameItem> array = new ArrayList<>();
        try
        {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); ++i) {
                JSONObject obj = (JSONObject)jsonArray.opt(i);
                BluetoothNameItem item = new BluetoothNameItem(obj.getString("DISPLAYNAME"),
                                                                obj.getString("NAME"),
                                                                obj.getString("LASTUSETIME"),
                                                                obj.getString("CREATEDTIME"),
                                                                obj.getString("ADDRESS"));
                array.add(item);
            }

        } catch (JSONException e) {
            //e.printStackTrace();
        }
        return array;
    }

    public static ArrayList<BluetoothDeviceItem> getBluetoothDevices(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String json = prefs.getString(BLUETOOTH_DEVICES, "");
        ArrayList<BluetoothDeviceItem> array = new ArrayList<>();
        try
        {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); ++i) {
                JSONObject obj = (JSONObject)jsonArray.opt(i);

                BluetoothDeviceItem d = new BluetoothDeviceItem(
                                                    obj.getString("NAME"),
                                                    obj.getString("ADDRESS"));
                array.add(d);

            }

        } catch (JSONException e) {
            //
        }
        return array;
    }

    public static void setBluetoothStatus(String status, Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(BLUETOOTH_STATUS, status);
        editor.apply();
    }

    public static String getBluetoothStatus(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(BLUETOOTH_STATUS, "Disconnected");
    }

    public static void setLastTimeUsed(String time, Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(BLUETOOTH_LAST_TIME_USED, time);
        editor.apply();
    }

    public static String getLastTimeUsed(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(BLUETOOTH_LAST_TIME_USED, "Unknown");
    }

    public static void setConnectedBluetooth(String name, Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(BLUETOOTH_CONNECTED_NAME, name);
        editor.apply();
    }

    public static String getConnectedBluetooth(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(BLUETOOTH_CONNECTED_NAME, "No Connection");
    }

    public static void setBluetoothHash(Context context, String mac, String value) {
        Map<String, String> hashes = getBluetoothHashes(context);
        hashes.put(mac, value);
        setBluetoothHashes(context, hashes);
    }

    public static String getBluetoothHash(Context context, String mac) {
        Map<String, String> hashes = getBluetoothHashes(context);
        if (hashes.containsKey(mac)) {
            return hashes.get(mac);
        } else {
            return "00000000000000000000000000000000";
        }
    }

    private static Map<String, String> getBluetoothHashes(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String json = prefs.getString(BLUETOOTH_HASHES, "");
        Map<String, String> result = new HashMap<>();
        try
        {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); ++i) {
                JSONObject obj = (JSONObject)jsonArray.opt(i);
                result.put(obj.getString("BLUETOOTH_MAC_KEY"), obj.getString("BLUETOOTH_HASH_VALUE"));
            }
        } catch (JSONException e) {
            //e.printStackTrace();
        }
        return result;
    }

    private static void setBluetoothHashes(Context context, Map<String, String> hashes) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();

        try {
            JSONArray jsonArray = new JSONArray();
            for (Map.Entry<String, String> value : hashes.entrySet()) {
                JSONObject obj = new JSONObject();
                obj.put("BLUETOOTH_MAC_KEY", value.getKey());
                obj.put("BLUETOOTH_HASH_VALUE", value.getValue());
                jsonArray.put(obj);
            }
            editor.putString(BLUETOOTH_HASHES, jsonArray.toString());
            editor.apply();
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    public static void setBluetoothSelectedMac(Context context, String value) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(BLUETOOTH_SELECTED_MAC, value);
        editor.apply();
    }

    public static String getBluetoothSelectedMac(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(BLUETOOTH_SELECTED_MAC, "");
    }
}
