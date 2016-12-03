package com.example.oroles.hlin;

import android.support.v4.app.Fragment;
import com.example.oroles.hlin.Fragments.AddEntryFragment;


public class AddEntryActivity extends SingleFragmentMenuActivity {
    @Override
    protected Fragment createFragment() {
        return new AddEntryFragment();
    }
}
