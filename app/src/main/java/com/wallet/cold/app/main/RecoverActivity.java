package com.wallet.cold.app.main;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wallet.R;
import com.wallet.cold.utils.Data;
import com.wallet.cold.utils.LocalManageUtil;
import com.wallet.cold.utils.Utils;
import com.wallet.cold.utils.WeiboDialogUtils;

public class RecoverActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText zhujici;
    private Dialog mWeiboDialog;
    private EditText pin;
    private EditText pin1;
    private ImageView fanhui;
    private TextView fhrecover;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover);
        findViewById(R.id.verify1).setOnClickListener(this);
        zhujici=(EditText)findViewById(R.id.zhujici1);
        pin=(EditText) findViewById(R.id.pin);
        pin1=(EditText) findViewById(R.id.pin1);
        fanhui=(ImageView) findViewById(R.id.fanhuiadd);
        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecoverActivity.this, BleActivity.class);
                startActivity(intent);
            }
        });
        fhrecover=(TextView) findViewById(R.id.fhrecover);
        fhrecover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecoverActivity.this,BleActivity.class);
                startActivity(intent);
            }
        });
        Data.settype("recover");
        Data.setcontext(RecoverActivity.this);
        new Utils().service_init(getApplicationContext());
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.verify1){//恢复助记词
            if(zhujici.getText().toString().equals("")) {
                Toast.makeText(getApplicationContext(),this.getResources().getString(R.string.recover9),Toast.LENGTH_SHORT).show();
            }else{
                if (pin.getText().toString().length() != 4) {
                    Toast.makeText(getApplicationContext(), this.getResources().getString(R.string.selete7), Toast.LENGTH_SHORT).show();
                } else if (!pin.getText().toString().equals(pin1.getText().toString())) {
                    Toast.makeText(getApplicationContext(), this.getResources().getString(R.string.selete8), Toast.LENGTH_SHORT).show();
                } else {
                    Data.setbletype("recover");Data.setresulterror("no");
                    Data.setbizhong("1");
                    mWeiboDialog = WeiboDialogUtils.createLoadingDialog(this, this.getResources().getString(R.string.recover10));
                    Data.setdialog(mWeiboDialog);
                    Utils.recover(pin.getText().toString(),zhujici.getText().toString());
                }
            }
        }
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocalManageUtil.setLocal(newBase));
    }
}
