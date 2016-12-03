package com.example.oroles.hlin.Fragments.Items;


import java.io.Serializable;

public class BluetoothDeviceItem implements Serializable {

    private String mName;
    private String mAddress;

    public BluetoothDeviceItem(String name, String address)
    {
        mName = name;
        mAddress = address;
    }

    public String getName()
    {
        return mName;
    }

    public String getAddress()
    {
        return mAddress;
    }
}
