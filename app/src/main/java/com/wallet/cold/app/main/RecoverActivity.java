package com.wallet.cold.app.main;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wallet.R;
import com.wallet.cold.utils.Data;
import com.wallet.cold.utils.LocalManageUtil;
import com.wallet.cold.utils.Utils;
import com.wallet.cold.utils.WeiboDialogUtils;
import com.wallet.hot.app.ByteActivity;
import com.wallet.hot.utils.HotWalletUtils;

public class RecoverActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText zhujici;
    private Dialog mWeiboDialog;
    private EditText pin;
    private EditText pin1;
    private TextView fhrecover,shezhi,queren;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover);
        findViewById(R.id.verify1).setOnClickListener(this);
        zhujici=(EditText)findViewById(R.id.zhujici1);
        pin=(EditText) findViewById(R.id.pin);
        pin1=(EditText) findViewById(R.id.pin1);
        fhrecover=(TextView) findViewById(R.id.fhrecover);
        fhrecover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Data.getapptype().equals("cold")) {
                    Intent intent = new Intent(RecoverActivity.this, BleActivity.class);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(RecoverActivity.this, ByteActivity.class);
                    startActivity(intent);
                }
            }
        });
        shezhi=(TextView) findViewById(R.id.shezhi);
        queren=(TextView) findViewById(R.id.queren);
        if(Data.getapptype().equals("hot")){
            shezhi.setText("设置密码:");
            queren.setText("确认密码:");
            pin.setHint("请输入8位以上密码");
            pin1.setHint("请再次输入8位以上密码");
            zhujici.setText("amount filter say bless mountain industry suffer goddess awkward earth upper spell cliff swarm ketchup");
            pin.setText("12345678");
            pin1.setText("12345678");
        }
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
                if(Data.getapptype().equals("cold")) {
                    if (pin.getText().toString().length() != 4) {
                        Toast.makeText(getApplicationContext(), this.getResources().getString(R.string.selete7), Toast.LENGTH_SHORT).show();
                    } else if (!pin.getText().toString().equals(pin1.getText().toString())) {
                        Toast.makeText(getApplicationContext(), this.getResources().getString(R.string.selete8), Toast.LENGTH_SHORT).show();
                    } else {
                        Data.setbletype("recover");
                        Data.setresulterror("no");
                        Data.setbizhong("1");
                        mWeiboDialog = WeiboDialogUtils.createLoadingDialog(this, this.getResources().getString(R.string.recover10));
                        Data.setdialog(mWeiboDialog);
                        Utils.recover(pin.getText().toString(), zhujici.getText().toString());
                    }
                }else{
                    if (pin.getText().toString().length() < 8) {
                        Toast.makeText(getApplicationContext(), "请输入8位以上密码", Toast.LENGTH_SHORT).show();
                    } else if (!pin.getText().toString().equals(pin1.getText().toString())) {
                        Toast.makeText(getApplicationContext(), "两次输入的密码不一致", Toast.LENGTH_SHORT).show();
                    }else {
                        Data.sethotpassword(pin.getText().toString());
                        HotWalletUtils.RecoverBip44Wallet(zhujici.getText().toString());
                    }
                }
            }
        }
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocalManageUtil.setLocal(newBase));
    }
}
