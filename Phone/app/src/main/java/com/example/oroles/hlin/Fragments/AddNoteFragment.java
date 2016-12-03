package com.example.oroles.hlin.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.oroles.hlin.Controllers.StoreController;
import com.example.oroles.hlin.R;

public class AddNoteFragment extends Fragment {

    private EditText mTitleEditText;
    private EditText mTextEditText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_note, container, false);

        mTitleEditText = (EditText)v.findViewById(R.id.noteTitleEditText);
        mTextEditText = (EditText)v.findViewById(R.id.noteTextEditText);

        Button addButton = (Button)v.findViewById(R.id.noteAddButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTextEditText.getText().toString().length() != 0 &&
                        mTitleEditText.getText().toString().length() != 0) {
                    if (!StoreController.getInstance(getActivity())
                            .getRepository()
                            .existsNote(mTitleEditText.getText().toString())) {
                        if (StoreController.getInstance(getActivity()).getBluetoothConnection().write(
                                StoreController.getInstance(getActivity()).getSenderProcessor().createAddNoteRequest(
                                        mTitleEditText.getText().toString(),
                                        mTextEditText.getText().toString()))) {
                            getActivity().finish();
                        } else {
                            Toast.makeText(getActivity(), "First connect to Bluetooth", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Note already exists", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Not all fields are filled", Toast.LENGTH_LONG).show();
                }
            }
        });

        return v;
    }
}
