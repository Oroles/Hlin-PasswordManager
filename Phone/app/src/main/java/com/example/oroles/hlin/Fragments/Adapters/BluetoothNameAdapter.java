package com.example.oroles.hlin.Fragments.Adapters;

import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.oroles.hlin.Fragments.Items.BluetoothNameItem;
import com.example.oroles.hlin.Interfaces.IClickBluetoothNameListener;
import com.example.oroles.hlin.R;

import java.util.List;

public class BluetoothNameAdapter extends ArrayAdapter<BluetoothNameItem> {

    private FragmentActivity mActivity;
    private IClickBluetoothNameListener mClickListener;

    public BluetoothNameAdapter(FragmentActivity context, IClickBluetoothNameListener clickListener, List<BluetoothNameItem> entries) {
        super(context, 0, entries);
        mActivity = context;
        mClickListener = clickListener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mActivity.getLayoutInflater().inflate(R.layout.list_item_bluetooth_name, null);
        }

        BluetoothNameItem entry = getItem(position);
        ((TextView)convertView.findViewById(R.id.bluetoothNameTextView)).setText(entry.getDisplayName()
            + " (" + entry.getName() + ")");

        convertView.setTag(R.string.bluetooth_name_tag, position);
        convertView.setClickable(false);
        convertView.setLongClickable(false);

        return convertView;
    }


    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mActivity.getLayoutInflater().inflate(R.layout.list_item_bluetooth_name_drop_down, null);
        }

        BluetoothNameItem entry = getItem(position);
        ((TextView)convertView.findViewById(R.id.bluetoothNameTextView)).setText(entry.getDisplayName()
            + " (" + entry.getName() + ")");
        ((TextView)convertView.findViewById(R.id.bluetoothMacTextView)).setText(entry.getMac());

        convertView.setTag(R.string.bluetooth_name_tag, position);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mClickListener != null) {
                    mClickListener.onItemClicked(v);
                }
            }
        });

        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mClickListener != null) {
                    mClickListener.onItemLongClicked(v);
                }
                return true;
            }
        });

        return convertView;
    }
}
