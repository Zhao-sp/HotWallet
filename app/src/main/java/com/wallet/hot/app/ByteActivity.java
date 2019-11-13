package com.wallet.hot.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.wallet.CreateOrImportActivity;
import com.wallet.R;
import com.wallet.cold.app.main.MainActivity;
import com.wallet.cold.utils.Data;
import com.wallet.cold.utils.LocalManageUtil;

public class ByteActivity extends Activity implements OnClickListener {
    private TextView create;
    private TextView recover,fhselete;
    private ImageView fanhui;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selete);
        create=(TextView) findViewById(R.id.create);
        recover=(TextView) findViewById(R.id.recover);
        fhselete=(TextView) findViewById(R.id.fhselete);
        fanhui=(ImageView) findViewById(R.id.fanhuiadd);
        create.setOnClickListener(this);
        recover.setOnClickListener(this);
        fanhui.setOnClickListener(this);
        fhselete.setOnClickListener(this);
        Data.settype("main");
        Data.setcontext(ByteActivity.this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {//如果返回键按下
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        return false;
    }
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.create) {
            Intent intent = new Intent(this, CreateActivity.class);
            startActivity(intent);
        }
        if(v.getId() == R.id.recover) {
            Intent intent1 = new Intent(this, HotRecoverActivity.class);
            startActivity(intent1);
        }
        if(v.getId() == R.id.fanhuiadd) {
            Intent intent2 = new Intent(this, CreateOrImportActivity.class);
            startActivity(intent2);
        }
        if(v.getId() == R.id.fhselete) {
            Intent intent3 = new Intent(this, CreateOrImportActivity.class);
            startActivity(intent3);
        }
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocalManageUtil.setLocal(newBase));
    }
}
