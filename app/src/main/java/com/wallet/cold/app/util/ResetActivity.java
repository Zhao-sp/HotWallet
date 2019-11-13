package com.wallet.cold.app.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.wallet.R;
import com.wallet.cold.utils.Data;
import com.wallet.cold.utils.LocalManageUtil;
import com.wallet.cold.utils.Utils;

public class ResetActivity extends AppCompatActivity implements OnClickListener {
    private EditText pin,pin1,zjc;
    private Button chongzhi;
    private TextView fhreset;
    private ImageView fh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);
        pin=(EditText) findViewById(R.id.pin);
        pin1=(EditText) findViewById(R.id.pin1);
        zjc=(EditText) findViewById(R.id.zjc);
        chongzhi=(Button) findViewById(R.id.verify2);
        chongzhi.setOnClickListener(this);
        fhreset=(TextView) findViewById(R.id.fhreset);
        fhreset.setOnClickListener(this);
        fh=(ImageView) findViewById(R.id.fanhui5);
        fh.setOnClickListener(this);
        Data.settype("reseractivity");
        Data.setcontext(ResetActivity.this);
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
        int i1 = v.getId();
        if (i1 == R.id.fhreset) {
            Intent intent = new Intent(this, Fragment5.class);
            startActivity(intent);
        } else if (i1 == R.id.fanhui5) {
            Intent intent1 = new Intent(this, Fragment5.class);
            startActivity(intent1);
        } else if (i1 == R.id.verify2) {
            Utils.reset(pin.getText().toString(),pin1.getText().toString(),zjc.getText().toString());
        }
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocalManageUtil.setLocal(newBase));
    }
}
