package com.example.oroles.hlin;

import android.support.v4.app.Fragment;

import com.example.oroles.hlin.Fragments.AddNoteFragment;


public class AddNoteActivity extends SingleFragmentMenuActivity {
    @Override
    protected Fragment createFragment() {
        return new AddNoteFragment();
    }
}
