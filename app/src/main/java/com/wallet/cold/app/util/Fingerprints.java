package com.wallet.cold.app.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wallet.R;
import com.wallet.cold.utils.Data;
import com.wallet.cold.utils.LocalManageUtil;
import com.wallet.cold.utils.Utils;
import com.wallet.cold.utils.WeiboDialogUtils;

import static com.wallet.cold.utils.Utils.sendble;

public class Fingerprints extends AppCompatActivity implements View.OnClickListener {
    private TextView fhgx,name1,name2,name3;
    private RelativeLayout l2,l3,l4,l5;
    private ImageView fanhui;
    private static String fingerprints1="no";
    private static String fingerprints2="no";
    private static String fingerprints3="no";
    private Dialog mWeiboDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fingerprints);
        name1=(TextView) findViewById(R.id.name1);Data.setresulterror("no");
        name2=(TextView) findViewById(R.id.name2);
        name3=(TextView) findViewById(R.id.name3);
        l2=(RelativeLayout) findViewById(R.id.l2);
        l2.setOnClickListener(this);
        l3=(RelativeLayout) findViewById(R.id.l3);
        l3.setOnClickListener(this);
        l4=(RelativeLayout) findViewById(R.id.l4);
        l4.setOnClickListener(this);
        fhgx=(TextView) findViewById(R.id.fhgx);
        fhgx.setOnClickListener(this);
        fanhui=(ImageView) findViewById(R.id.fanhui5);
        fanhui.setOnClickListener(this);
        Data.settype("fingerprints");
        Data.setcontext(Fingerprints.this);
        new Utils().service_init(getApplicationContext());
    }

    public void bleresult(String data){
        data=data.substring(6,data.length());
        String a=hexString2binaryString(data);
        if(a.substring(0,1).equals("1")){
            fingerprints1="yes";
        }else{
            fingerprints1="no";
        }
        if(a.substring(1,2).equals("1")){
            fingerprints2="yes";
        }else{
            fingerprints2="no";
        }
        if(a.substring(2,3).equals("1")){
            fingerprints3="yes";
        }else{
            fingerprints3="no";
        }
    }

    /**
     * 16进制字符串转2进制字符串
     * @param hexString
     * @return
     */
    public static String hexString2binaryString(String hexString) {
        if (hexString == null || hexString.length() % 2 != 0)
            return null;
        String bString = "", tmp;
        for (int i = 0; i < hexString.length(); i++) {
            tmp = "0000"
                    + Integer.toBinaryString(Integer.parseInt(hexString
                    .substring(i, i + 1), 16));
            bString += tmp.substring(tmp.length() - 4);
        }
        return bString;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.fanhui5) {
            Data.getcontext().startActivity(new Intent(this, Fragment5.class));
        }
        if(v.getId() == R.id.fhgx) {
            Data.getcontext().startActivity(new Intent(this, Fragment5.class));
        }
        if(v.getId() == R.id.l2) {
            Data.setfingerprints("fingerprints1");
            Data.setfingerprintsname(name1.getText().toString());
            if(fingerprints1.equals("yes")) {
                Intent intent = new Intent(Data.getcontext(), FingerprintsXQ.class);
                Data.getcontext().startActivity(intent);
            }else{
                AlertDialog.Builder builder = new AlertDialog.Builder(Fingerprints.this);
                builder.setCancelable(false)//设置点击对话框外部区域不关闭对话框
                        .setTitle(R.string.zw4)
                        .setMessage(R.string.zw5)
                        .setNegativeButton(R.string.f513, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setPositiveButton(R.string.f514, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mWeiboDialog = WeiboDialogUtils.createLoadingDialog(Fingerprints.this, Data.getcontext().getResources().getString(R.string.zw6));
                                Data.setdialog(mWeiboDialog);
                                Data.setbletype("Recording");
                                String ret=Utils.strhex("f201018000000000");
                                String a = "55aaf201018000000000"+ret+"aa55";
                                sendble(a,Data.getmService());
                            }
                        })
                        .show();
            }
        }
        if(v.getId() == R.id.l3) {
            Data.setfingerprints("fingerprints2");
            Data.setfingerprintsname(name2.getText().toString());
            if(fingerprints2.equals("yes")) {
                Intent intent = new Intent(Data.getcontext(), FingerprintsXQ.class);
                Data.getcontext().startActivity(intent);
            }else{
                AlertDialog.Builder builder = new AlertDialog.Builder(Fingerprints.this);
                builder.setCancelable(false)//设置点击对话框外部区域不关闭对话框
                        .setTitle(R.string.zw4)
                        .setMessage(R.string.zw5)
                        .setNegativeButton(R.string.f513, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setPositiveButton(R.string.f514, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mWeiboDialog = WeiboDialogUtils.createLoadingDialog(Fingerprints.this, Data.getcontext().getResources().getString(R.string.zw6));
                                Data.setdialog(mWeiboDialog);
                                Data.setbletype("Recording");
                                String ret=Utils.strhex("f201010080000000");
                                String a = "55aaf201010080000000"+ret+"aa55";
                                sendble(a,Data.getmService());
                            }
                        })
                        .show();
            }
        }
        if(v.getId() == R.id.l4) {
            Data.setfingerprints("fingerprints3");
            Data.setfingerprintsname(name3.getText().toString());
            if(fingerprints3.equals("yes")) {
                Intent intent = new Intent(Data.getcontext(), FingerprintsXQ.class);
                Data.getcontext().startActivity(intent);
            }else{
                AlertDialog.Builder builder = new AlertDialog.Builder(Fingerprints.this);
                builder.setCancelable(false)//设置点击对话框外部区域不关闭对话框
                        .setTitle(R.string.zw4)
                        .setMessage(R.string.zw5)
                        .setNegativeButton(R.string.f513, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setPositiveButton(R.string.f514, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mWeiboDialog = WeiboDialogUtils.createLoadingDialog(Fingerprints.this, Data.getcontext().getResources().getString(R.string.zw6));
                                Data.setdialog(mWeiboDialog);
                                Data.setbletype("Recording");
                                String ret=Utils.strhex("f201010000800000");
                                String a = "55aaf201010000800000"+ret+"aa55";
                                sendble(a,Data.getmService());
                            }
                        })
                        .show();
            }
        }
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocalManageUtil.setLocal(newBase));
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {//按返回键退出程序
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(Data.getcontext(),Fragment5.class);
            Data.getcontext().startActivity(intent);
        }
        return super.onKeyDown(keyCode, event);
    }
}
