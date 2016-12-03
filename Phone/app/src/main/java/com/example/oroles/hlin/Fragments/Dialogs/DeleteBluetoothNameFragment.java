package com.example.oroles.hlin.Fragments.Dialogs;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.TextView;

import com.example.oroles.hlin.R;

import com.example.oroles.hlin.Fragments.Items.BluetoothNameItem;
import com.example.oroles.hlin.Utils.SharedPreferencesHelper;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class DeleteBluetoothNameFragment extends DialogFragment{

    public static final String TITLE = "Delete Bluetooth Name";
    public static final String ARGUMENTS = "arguments";

    public static final int DELETE_BLUETOOTH_NAME_OK = 1;
    public static final int DELETE_BLUETOOTH_NAME_CANCEL = 2;

    public static DeleteBluetoothNameFragment newInstance(BluetoothNameItem item) {
        Bundle args = new Bundle();
        args.putSerializable(ARGUMENTS, item);

        DeleteBluetoothNameFragment fragment = new DeleteBluetoothNameFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstance) {
        View v = getActivity().getLayoutInflater()
                .inflate(R.layout.fragment_delete_bluetooth_name, null);

        final BluetoothNameItem item = (BluetoothNameItem)getArguments().getSerializable(ARGUMENTS);

        ((TextView)v.findViewById(R.id.bluetoothDisplayNameTextView)).setText(item.getDisplayName());
        ((TextView)v.findViewById(R.id.bluetoothNameTextView)).setText(item.getName());
        ((TextView)v.findViewById(R.id.bluetoothMacTextView)).setText(item.getMac());
        ((TextView)v.findViewById(R.id.bluetoothLastTimeUsedTextView)).setText(item.getLastUsedTime());
        ((TextView)v.findViewById(R.id.bluetoothCreatedTextView)).setText(item.getCreatedTime());

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(TITLE)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        List<BluetoothNameItem> entries = SharedPreferencesHelper.getBluetoothNames(getContext());
                        entries.remove(item);
                        SharedPreferencesHelper.storeBluetoothNames(entries, getContext());

                        getTargetFragment()
                                .onActivityResult(getTargetRequestCode(), DELETE_BLUETOOTH_NAME_OK, getActivity()
                                        .getIntent());
                    }
                })
                .create();
    }
}
