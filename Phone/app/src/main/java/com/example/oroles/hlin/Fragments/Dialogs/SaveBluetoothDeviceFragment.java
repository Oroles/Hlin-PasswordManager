package com.example.oroles.hlin.Fragments.Dialogs;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.oroles.hlin.Fragments.Items.BluetoothDeviceItem;
import com.example.oroles.hlin.Fragments.Items.BluetoothNameItem;
import com.example.oroles.hlin.R;
import com.example.oroles.hlin.Utils.SharedPreferencesHelper;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

public class SaveBluetoothDeviceFragment extends DialogFragment{
    public static final String TITLE = "Save Bluetooth";
    public static final String ARGUMENTS = "arguments";

    public static final int SAVE_BLUETOOTH_DEVICE_OK = 1;
    public static final int SAVE_BLUETOOTH_DEVICE_CANCEL = 2;

    private EditText mDisplayNameEditText;

    public static SaveBluetoothDeviceFragment newInstance(BluetoothDeviceItem item) {
        Bundle args = new Bundle();
        args.putSerializable(ARGUMENTS, item);

        SaveBluetoothDeviceFragment fragment = new SaveBluetoothDeviceFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstance) {
        View v = getActivity().getLayoutInflater()
                .inflate(R.layout.fragment_save_bluetooth_device, null);

        final BluetoothDeviceItem item = (BluetoothDeviceItem)getArguments().getSerializable(ARGUMENTS);

        ((TextView)v.findViewById(R.id.displayNameEditText)).setText(item.getName());
        ((TextView)v.findViewById(R.id.macTextView)).setText(item.getAddress());
        ((TextView)v.findViewById(R.id.nameTextView)).setText(item.getName());

        mDisplayNameEditText = (EditText)v.findViewById(R.id.displayNameEditText);

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(TITLE)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String displayName = mDisplayNameEditText.getText().toString();

                        BluetoothNameItem entry = new BluetoothNameItem(displayName,
                                item.getName(),
                                "Unknown",
                                DateFormat.getDateTimeInstance().format(new Date()),
                                item.getAddress());
                        if (!existingName(mDisplayNameEditText.getText().toString())) {
                            if (!existingMac(item.getAddress())) {
                                SharedPreferencesHelper.storeBluetoothName(entry, getContext());
                            } else {
                                Toast.makeText(getContext(), "Already the mac exist", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(getContext(), "Already the name exist", Toast.LENGTH_LONG).show();
                        }

                        getTargetFragment()
                                .onActivityResult(getTargetRequestCode(), SAVE_BLUETOOTH_DEVICE_OK, getActivity()
                                        .getIntent());
                    }
                })
                .create();
    }

    private boolean existingName(String bluetoothName) {
        List<BluetoothNameItem> entries = SharedPreferencesHelper.getBluetoothNames(getContext());
        for(BluetoothNameItem entry : entries) {
            if (entry.getName().equals(bluetoothName)) {
                return true;
            }
        }
        return false;
    }

    private boolean existingMac(String mac) {
        List<BluetoothNameItem> entries = SharedPreferencesHelper.getBluetoothNames(getContext());
        for(BluetoothNameItem entry : entries) {
            if (entry.getMac().equals(mac)) {
                return true;
            }
        }
        return false;
    }
}
