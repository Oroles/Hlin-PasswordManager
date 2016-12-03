package com.example.oroles.hlin.Fragments.Items;

import java.io.Serializable;

public class BluetoothNameItem implements Serializable {

    private String mName;
    private String mLastUsedTime;
    private String mCreatedTime;
    private String mMac;
    private String mDisplayName;

    public BluetoothNameItem(String displayName,
                             String name,
                             String lastUsedTime,
                             String createdTime,
                             String mac) {
        mDisplayName = displayName;
        mName = name;
        mLastUsedTime = lastUsedTime;
        mCreatedTime = createdTime;
        mMac = mac;
    }

    public String getDisplayName() {return mDisplayName;}

    public String getName() {
        return mName;
    }

    public String getLastUsedTime() {
        return mLastUsedTime;
    }

    public void setLastUsedTime(String lastUsedTime) {
        mLastUsedTime = lastUsedTime;
    }

    public String getMac() {return mMac;}

    public String getCreatedTime() { return mCreatedTime;}

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        BluetoothNameItem tmp = (BluetoothNameItem)obj;
        return tmp.getMac().equals(this.mMac);
    }
}
