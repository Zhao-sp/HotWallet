package com.wallet.hot.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.wallet.MainActivity;
import com.wallet.R;
import com.wallet.cold.app.main.CreateActivity;
import com.wallet.cold.app.main.RecoverActivity;
import com.wallet.cold.utils.Data;
import com.wallet.utils.language.LocalManageUtil;

public class ByteActivity extends Activity implements OnClickListener {
    private TextView create;
    private TextView recover,fhselete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selete);
        create=(TextView) findViewById(R.id.create);
        recover=(TextView) findViewById(R.id.recover);
        fhselete=(TextView) findViewById(R.id.fhselete);
        create.setOnClickListener(this);
        recover.setOnClickListener(this);
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
            Intent intent1 = new Intent(this, RecoverActivity.class);
            startActivity(intent1);
        }
        if(v.getId() == R.id.fhselete) {
            Intent intent3 = new Intent(this, MainActivity.class);
            startActivity(intent3);
        }
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocalManageUtil.setLocal(newBase));
    }
}
