package com.example.oroles.hlin.Fragments;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.oroles.hlin.Controllers.StoreController;
import com.example.oroles.hlin.MainActivity;
import com.example.oroles.hlin.R;
import com.example.oroles.hlin.Utils.Utils;

public class AddEntryFragment extends Fragment {

    private EditText mWebsiteEditText;
    private EditText mUsernameEditText;
    private EditText mPasswordEditText;

    private CheckBox mSymbolsCheckBox;
    private CheckBox mNumbersCheckBox;
    private CheckBox mLettersCheckBox;

    private TextView mPasswordLengthTextView;
    private SeekBar mPasswordLengthSeekBar;

    private Button mShowHideAddPanelButton;
    private Button mShowHideGeneratePanelButton;
    private LinearLayout mPanelAddLinearLayout;
    private RelativeLayout mPanelGenerateRelativeLayout;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        setHasOptionsMenu(true);
        setMenuVisibility(true);

        MainActivity.SETTINGS_STARTED = true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_add_entry, container, false);

        mPasswordLengthTextView = (TextView)v.findViewById(R.id.passwordLengthValueTextView);
        mPasswordLengthSeekBar = (SeekBar)v.findViewById(R.id.passwordLengthSeekBar);
        mPasswordLengthSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mPasswordLengthTextView.setText(String.valueOf(progress + 8));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mWebsiteEditText = (EditText)v.findViewById(R.id.websiteNameEditText);
        mUsernameEditText = (EditText)v.findViewById(R.id.usernameNameEditText);
        mPasswordEditText = (EditText)v.findViewById(R.id.passwordNameEditText);

        mSymbolsCheckBox = (CheckBox)v.findViewById(R.id.checkSymbols);
        mNumbersCheckBox = (CheckBox)v.findViewById(R.id.checkNumbers);
        mLettersCheckBox = (CheckBox)v.findViewById(R.id.checkLetters);

        ImageButton showPassword = (ImageButton)v.findViewById(R.id.showPasswordButton);
        showPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    mPasswordEditText.setInputType(InputType.TYPE_CLASS_TEXT);
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    mPasswordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                return true;
            }
        });

        mPanelAddLinearLayout = (LinearLayout) v.findViewById(R.id.panelAddLinearLayout);
        mPanelGenerateRelativeLayout = (RelativeLayout)v.findViewById(R.id.panelGenerateLinearLayout);

        mShowHideAddPanelButton = (Button)v.findViewById(R.id.panelAddButton);
        mShowHideAddPanelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPanelAddLinearLayout.getVisibility() == View.VISIBLE) {
                    mPanelAddLinearLayout.setVisibility(View.GONE);
                    mShowHideAddPanelButton.setText(R.string.showAddPanel);
                    mShowHideAddPanelButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_down, 0);
                } else {
                    mPanelAddLinearLayout.setVisibility(View.VISIBLE);
                    mShowHideAddPanelButton.setText(R.string.hideAddPanel);
                    mShowHideAddPanelButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_up, 0);
                }
            }
        });

        mShowHideGeneratePanelButton = (Button)v.findViewById(R.id.panelGenerateButton);
        mShowHideGeneratePanelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPanelGenerateRelativeLayout.getVisibility() == View.VISIBLE) {
                    mPanelGenerateRelativeLayout.setVisibility(View.GONE);
                    mShowHideGeneratePanelButton.setText(R.string.showGeneratePanel);
                    mShowHideGeneratePanelButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_down, 0);
                } else {
                    mPanelGenerateRelativeLayout.setVisibility(View.VISIBLE);
                    mShowHideGeneratePanelButton.setText(R.string.hideGeneratePanel);
                    mShowHideGeneratePanelButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_up, 0);
                }
            }
        });


        Button bAdd = (Button)v.findViewById(R.id.addEntryButton);
        bAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((mWebsiteEditText.getText().length() != 0) &&
                        (mUsernameEditText.getText().length() != 0) &&
                        (mPasswordEditText.getText().length() != 0) ) {
                    if (!StoreController.getInstance(getContext())
                            .getRepository()
                            .existsPasswordEntry(mWebsiteEditText.getText().toString(),mUsernameEditText.getText().toString())) {
                        if (mPasswordEditText.getText().length() < 16) {
                            if (((!PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean(SettingsFragment.ENFORCE_MINIMUM_LENGTH, true)) ||
                                    ((mPasswordEditText.getText().length() > 7)))) {
                                if ((!PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean(SettingsFragment.CHECK_FOR_COMMON_PASSWORDS, true)) ||
                                        (!StoreController.getInstance(getActivity()).getRepository().commonPassword(mPasswordEditText.getText().toString()))) {
                                    StoreController.getInstance(getActivity()).
                                            getBluetoothConnection().
                                            write(StoreController.getInstance(getActivity())
                                                    .getSenderProcessor().createAddEntryRequest(
                                                            mWebsiteEditText.getText().toString(),
                                                            mUsernameEditText.getText().toString(),
                                                            mPasswordEditText.getText().toString()));
                                    getActivity().finish();
                                } else {
                                    Toast.makeText(getActivity(), "It is a common password", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(getActivity(), "It doesn't have a minimum length of 8", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(getActivity(), "Password too long, it can't be longer then 16", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Entry already exists", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Not all the fields are filled", Toast.LENGTH_LONG).show();
                }
            }
        });

        Button bGenerate = (Button)v.findViewById(R.id.addEntryAndGenerateButton);
        bGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((mWebsiteEditText.getText().length() != 0) &&
                        (mUsernameEditText.getText().length() != 0) &&
                        (mSymbolsCheckBox.isChecked() || mNumbersCheckBox.isChecked() || mLettersCheckBox.isChecked()) &&
                        (Integer.parseInt(mPasswordLengthTextView.getText().toString()) != 0) ) {
                    if (!StoreController.getInstance(getContext())
                            .getRepository()
                            .existsPasswordEntry(mWebsiteEditText.getText().toString(), mUsernameEditText.getText().toString())) {
                        StoreController.getInstance(getActivity())
                                .getBluetoothConnection()
                                .write(StoreController.getInstance(getActivity())
                                        .getSenderProcessor().createAddAndGenerateEntryRequest(
                                                mWebsiteEditText.getText().toString(),
                                                mUsernameEditText.getText().toString(),
                                                Utils.generateAllowTypes(
                                                        mSymbolsCheckBox.isChecked(),
                                                        mNumbersCheckBox.isChecked(),
                                                        mLettersCheckBox.isChecked()
                                                ),
                                                Integer.parseInt(mPasswordLengthTextView.getText().toString())
                                        ));
                        getActivity().finish();
                    } else {
                        Toast.makeText(getActivity(), "Entry already exists", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Not all the fields are filled", Toast.LENGTH_LONG).show();
                }
            }
        });

        return v;
    }
}
