package com.example.oroles.hlin.Fragments.Adapters;

import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.oroles.hlin.Fragments.Items.BluetoothDeviceItem;
import com.example.oroles.hlin.R;

import java.util.List;

public class BluetoothDeviceAdapter extends ArrayAdapter<BluetoothDeviceItem> {

    private FragmentActivity mActivity;

    public BluetoothDeviceAdapter(FragmentActivity context, List<BluetoothDeviceItem> entries) {
        super(context, 0, entries);
        mActivity = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mActivity.getLayoutInflater().inflate(R.layout.list_item_bluetooth, null);
        }

        BluetoothDeviceItem device = getItem(position);

        ((TextView)convertView.findViewById(R.id.bluetoothDeviceNameEntry)).setText(device.getName());
        ((TextView)convertView.findViewById(R.id.bluetoothDeviceMacEntry)).setText(device.getAddress());

        return convertView;
    }
}
