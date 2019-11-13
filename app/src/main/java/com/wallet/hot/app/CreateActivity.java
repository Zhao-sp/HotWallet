package com.wallet.hot.app;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wallet.R;
import com.wallet.cold.utils.Data;
import com.wallet.cold.utils.LocalManageUtil;
import com.wallet.cold.utils.WeiboDialogUtils;
import com.wallet.hot.utils.BaiBeiWalletUtils;
import com.wallet.hot.utils.BaibeiWallet;

import org.web3j.crypto.CipherException;

import java.io.IOException;


public class CreateActivity extends Activity implements View.OnClickListener {

    private Button generate;
    private Dialog mWeiboDialog;
    private EditText uname;
    private EditText pwd;
    private ImageView fanhui;
    private TextView fhcreate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotcreate);
        fanhui=(ImageView) findViewById(R.id.fanhuiadd);
        fanhui.setOnClickListener(this);
        fhcreate=(TextView) findViewById(R.id.fhcreate);
        fhcreate.setOnClickListener(this);
        generate=(Button)findViewById(R.id.generate);
        generate.setOnClickListener(this);
        uname=(EditText) findViewById(R.id.uname);
        pwd=(EditText) findViewById(R.id.pwd);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.generate){//生成助记词逻辑代码
            if (TextUtils.isEmpty(uname.getText().toString())) {
                Toast.makeText(this, "请输入用户名", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(pwd.getText().toString())||pwd.getText().toString().length()<8) {
                Toast.makeText(this, "请输入8位数以上密码", Toast.LENGTH_SHORT).show();
                return;
            }
            Data.sethotpassword(pwd.getText().toString());
            BaibeiWallet mWallet = null;
            try {
                mWallet = BaiBeiWalletUtils.generateBip44Wallet(pwd.getText().toString());
            } catch (CipherException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
//            WalletManager.getInstance().addWallet(mWallet);
//            String address = mWallet.getWalletFile().getAddress();
//            Data.sethotethaddress(address);
//            log("TAG", "onClickCreateWallet: 钱包地址 = " + address);
            Intent intent = new Intent(CreateActivity.this, BackUpActivity.class);
            startActivity(intent);
        }else if(v.getId() == R.id.fanhuiadd){
            Intent intent = new Intent(CreateActivity.this, ByteActivity.class);
            startActivity(intent);
        }else if(v.getId() == R.id.fhcreate){
            Intent intent = new Intent(CreateActivity.this,ByteActivity.class);
            startActivity(intent);
        }
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocalManageUtil.setLocal(newBase));
    }
}

