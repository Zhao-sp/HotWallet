package com.wallet.cold.app.main;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wallet.R;
import com.wallet.cold.utils.Data;
import com.wallet.utils.language.LocalManageUtil;
import com.wallet.cold.utils.Utils;
import com.wallet.utils.WeiboDialogUtils;
import com.wallet.hot.app.ByteActivity;
import com.wallet.hot.utils.HotWalletUtils;

public class CreateActivity extends AppCompatActivity implements View.OnClickListener {

    private Button generate;
    private Button button15;
    private Button button21;
    private Button button18;
    private Button button24;
    private Dialog mWeiboDialog;
    private EditText pin;
    private EditText pin1;
    private TextView fhcreate,shezhi,queren;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        WeiboDialogUtils.closeDialog(mWeiboDialog);
        Data.settype("create");
        shezhi=(TextView) findViewById(R.id.shezhi);
        queren=(TextView) findViewById(R.id.queren);
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
        if(Data.getapptype().equals("hot")){
            shezhi.setText("设置密码:");
            queren.setText("确认密码:");
            pin.setHint("请输入8位以上密码");
            pin1.setHint("请再次输入8位以上密码");
        }
        Data.setcontext(CreateActivity.this);
        new Utils().service_init(getApplicationContext());
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.generate){//生成助记词逻辑代码
            if(Data.getapptype().equals("cold")) {
                if (pin.getText().toString().length() != 4) {
                    Toast.makeText(getApplicationContext(), this.getResources().getString(R.string.selete7), Toast.LENGTH_SHORT).show();
                } else if (!pin.getText().toString().equals(pin1.getText().toString())) {
                    Toast.makeText(getApplicationContext(), this.getResources().getString(R.string.selete8), Toast.LENGTH_SHORT).show();
                } else {
                    Data.setbletype("Initialize");
                    Data.setresulterror("no");
                    mWeiboDialog = WeiboDialogUtils.createLoadingDialog(CreateActivity.this, this.getResources().getString(R.string.selete9));
                    Utils.generate(pin.getText().toString(), Data.getbutton());
                    Intent intent = new Intent(Data.getcontext(), NumbersActivity.class);
                    Data.getcontext().startActivity(intent);
                }
//                if (pin.getText().toString().length() != 4) {
//                    Toast.makeText(getApplicationContext(), this.getResources().getString(R.string.selete7), Toast.LENGTH_SHORT).show();
//                } else if(Data.getisinitialize()) {
//                    if (pin1.getText().toString().equals("")) {
//                        Toast.makeText(getApplicationContext(), "请输入旧pin码", Toast.LENGTH_SHORT).show();
//                    } else {
//                        Data.setresult(pin1.getText().toString());
//                        Data.setresultdata(pin.getText().toString());
//                        Data.setbletype("getrandom");Data.setlimit(pin.getText().toString());
//                        new Transfer().chushihua();
//                    }
//                }else if(!Data.getisinitialize()){
//                    Data.setbletype("Initialize");
//                    mWeiboDialog = WeiboDialogUtils.createLoadingDialog(CreateActivity.this, CreateActivity.this.getResources().getString(R.string.selete9));
//                    Data.setdialog(mWeiboDialog);
//                    Utils.generate(pin.getText().toString(), Data.getbutton());
//                    Intent intent = new Intent(Data.getcontext(), NumbersActivity.class);
//                    Data.getcontext().startActivity(intent);
//                }
            }else{
                if (pin.getText().toString().length() < 8) {
                    Toast.makeText(getApplicationContext(), "请输入8位以上密码", Toast.LENGTH_SHORT).show();
                } else if (!pin.getText().toString().equals(pin1.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "两次输入的密码不一致", Toast.LENGTH_SHORT).show();
                }else {
                    Data.sethotpassword(pin.getText().toString());
                    HotWalletUtils.generateBip44Wallet();
                }
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
        }else if(v.getId() == R.id.fhcreate){
            if(Data.getapptype().equals("cold")) {
                Intent intent = new Intent(CreateActivity.this, BleActivity.class);
                startActivity(intent);
            }else{
                Intent intent = new Intent(CreateActivity.this, ByteActivity.class);
                startActivity(intent);
            }
        }
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocalManageUtil.setLocal(newBase));
    }
}

