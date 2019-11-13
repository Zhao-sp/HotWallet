package com.wallet.cold.app.main;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wallet.R;
import com.wallet.cold.utils.Data;
import com.wallet.cold.utils.LocalManageUtil;
import com.wallet.cold.utils.Utils;
import com.wallet.cold.utils.WeiboDialogUtils;

public class CreateActivity extends AppCompatActivity implements View.OnClickListener {

    private Button generate;
    private Button button15;
    private Button button21;
    private Button button18;
    private Button button24;
    private Dialog mWeiboDialog;
    private EditText pin;
    private EditText pin1;
    private ImageView fanhui;
    private TextView fhcreate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        WeiboDialogUtils.closeDialog(mWeiboDialog);
        Data.settype("create");
        fanhui=(ImageView) findViewById(R.id.fanhuiadd);
        fanhui.setOnClickListener(this);
        fhcreate=(TextView) findViewById(R.id.fhcreate);
        fhcreate.setOnClickListener(this);
        generate=(Button)findViewById(R.id.generate);
        generate.setOnClickListener(this);
//        button12=(Button)findViewById(R.id.button12);
//        button12.setOnClickListener(this);
        button15=(Button)findViewById(R.id.button15);
        button15.setOnClickListener(this);
        button18=(Button)findViewById(R.id.button18);
        button18.setOnClickListener(this);
        button21=(Button)findViewById(R.id.button21);
        button21.setOnClickListener(this);
        button24=(Button)findViewById(R.id.button24);
        button24.setOnClickListener(this);
        pin=(EditText) findViewById(R.id.pin);
        pin1=(EditText) findViewById(R.id.pin1);
        button15.setEnabled(false);
        Data.setbutton(15);
        Data.setcontext(CreateActivity.this);
        new Utils().service_init(getApplicationContext());
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.generate){//生成助记词逻辑代码
            if (pin.getText().toString().length() != 4) {
                Toast.makeText(getApplicationContext(), this.getResources().getString(R.string.selete7), Toast.LENGTH_SHORT).show();
            } else if (!pin.getText().toString().equals(pin1.getText().toString())) {
                Toast.makeText(getApplicationContext(), this.getResources().getString(R.string.selete8), Toast.LENGTH_SHORT).show();
            } else {
                Data.setbletype("Initialize");Data.setresulterror("no");
                mWeiboDialog = WeiboDialogUtils.createLoadingDialog(CreateActivity.this, this.getResources().getString(R.string.selete9));
                Utils.generate(pin.getText().toString(),Data.getbutton());
                Intent intent = new Intent(Data.getcontext(), NumbersActivity.class);
                Data.getcontext().startActivity(intent);
            }
//        }else if(v.getId() == R.id.button12){
//            button24.setEnabled(true);button21.setEnabled(true);button15.setEnabled(true);
//            button18.setEnabled(true);button12.setEnabled(false);Data.setbutton(12);
        }else if(v.getId() == R.id.button15){
            button21.setEnabled(true);button15.setEnabled(false);button24.setEnabled(true);
            button18.setEnabled(true);Data.setbutton(15);
        }else if(v.getId() == R.id.button18){
            button21.setEnabled(true);button15.setEnabled(true);button24.setEnabled(true);
            button18.setEnabled(false);Data.setbutton(18);
        }else if(v.getId() == R.id.button21){
            button21.setEnabled(false);button15.setEnabled(true);button24.setEnabled(true);
            button18.setEnabled(true);Data.setbutton(21);
        }else if(v.getId() == R.id.button24){
            button21.setEnabled(true);button15.setEnabled(true);
            button18.setEnabled(true);button24.setEnabled(false);Data.setbutton(24);
        }else if(v.getId() == R.id.fanhuiadd){
            Intent intent = new Intent(CreateActivity.this, BleActivity.class);
            startActivity(intent);
        }else if(v.getId() == R.id.fhcreate){
            Intent intent = new Intent(CreateActivity.this,BleActivity.class);
            startActivity(intent);
        }
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocalManageUtil.setLocal(newBase));
    }
}

