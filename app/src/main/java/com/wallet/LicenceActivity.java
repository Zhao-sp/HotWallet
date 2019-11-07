package com.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

public class LicenceActivity extends AppCompatActivity implements
        View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private TextView mContinue;
    private CheckBox mCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.licence_layout);
        mContinue = findViewById(R.id.licence_continue);
        mContinue.setOnClickListener(this);
        mCheckBox = findViewById(R.id.licence_checkbox);
        mCheckBox.setOnCheckedChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mContinue&&mCheckBox.isChecked()) {
            Intent i = new Intent();
            i.setClass(this, CreateOrImportActivity.class);
            startActivity(i);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView.getId() == mCheckBox.getId()) {
            mContinue.setEnabled(isChecked ? true : false);
            mContinue.setBackgroundColor(isChecked ? getResources().getColor(R.color.sky_blue)
                    : getResources().getColor(R.color.dark_gray));
        }
    }
}
