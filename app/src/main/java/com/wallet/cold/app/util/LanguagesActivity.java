package com.wallet.cold.app.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.wallet.R;
import com.wallet.cold.utils.Data;
import com.wallet.cold.utils.LocalManageUtil;
import com.wallet.cold.utils.Utils;

public class LanguagesActivity extends AppCompatActivity implements OnClickListener {
    private TextView fhgx;
    private TextView app,boot;
    private ImageView fanhui;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_languages);
        fhgx=(TextView) findViewById(R.id.fhla);
        app=(TextView) findViewById(R.id.china);
        boot=(TextView) findViewById(R.id.english);
        fanhui=(ImageView) findViewById(R.id.fanhui5);
        app.setOnClickListener(this);
        boot.setOnClickListener(this);
        fanhui.setOnClickListener(this);
        fhgx.setOnClickListener(this);
        Data.settype("languagesactivity");
        Data.setcontext(LanguagesActivity.this);
        new Utils().service_init(getApplicationContext());
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {//如果返回键按下
            Intent intent = new Intent(this, Fragment5.class);
            startActivity(intent);
        }
        return false;
    }
    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.fanhui5) {
            Intent intent = new Intent(this, Fragment5.class);
            startActivity(intent);
        } else if (i == R.id.fhla) {
            Intent intent1 = new Intent(this, Fragment5.class);
            startActivity(intent1);
        } else if (i == R.id.china) {
            LocalManageUtil.saveSelectLanguage(this, 1);
            Utils.reStart(this);
        } else if (i == R.id.english) {
            LocalManageUtil.saveSelectLanguage(this, 2);
            Utils.reStart(this);

        }
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocalManageUtil.setLocal(newBase));
    }
}
