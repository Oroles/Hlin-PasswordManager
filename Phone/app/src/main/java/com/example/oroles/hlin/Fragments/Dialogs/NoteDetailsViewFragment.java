package com.example.oroles.hlin.Fragments.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.TextView;

import com.example.oroles.hlin.Controllers.StoreController;
import com.example.oroles.hlin.Database.DatabaseNote;
import com.example.oroles.hlin.R;

import java.util.Date;

public class NoteDetailsViewFragment extends DialogFragment {

    public static final String TITLE = "Details view";
    public static final String ARGUMENTS = "arguments";

    public static final int DELETED_OK = 1;
    public static final int DELETED_CANCEL = 2;

    public static NoteDetailsViewFragment newInstance(DatabaseNote entry) {
        Bundle args = new Bundle();
        args.putSerializable(ARGUMENTS, entry);

        NoteDetailsViewFragment fragment = new NoteDetailsViewFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstance) {
        View v = getActivity().getLayoutInflater()
                .inflate(R.layout.fragment_note_details_view, null);

        final DatabaseNote note = (DatabaseNote)getArguments().getSerializable(ARGUMENTS);

        ((TextView)v.findViewById(R.id.detailsTitleNote)).setText(note.getTitle());
        ((TextView)v.findViewById(R.id.detailsDateNote)).setText(android.text.format.DateFormat.format("yyyy-MM-dd kk:mm:ss", new Date(note.getCreateTime())));

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(TITLE)
                //.setPositiveButton(android.R.string.ok, null)
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (StoreController.getInstance(getActivity())
                                .getRepository()
                                .deleteNote(note.getTitle())) {
                            getTargetFragment().onActivityResult(getTargetRequestCode(), DELETED_OK, getActivity().getIntent());
                        }
                        else {
                            getTargetFragment().onActivityResult(getTargetRequestCode(), DELETED_CANCEL, getActivity().getIntent());
                        }
                    }
                })
                .create();
    }

}
