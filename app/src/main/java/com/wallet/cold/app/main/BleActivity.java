package com.wallet.cold.app.main;

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
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.wallet.R;
import com.wallet.cold.utils.Data;
import com.wallet.cold.utils.LocalManageUtil;
import com.wallet.cold.utils.Utils;
import com.wallet.cold.utils.WeiboDialogUtils;

import static com.wallet.cold.utils.Utils.sendble;

public class BleActivity extends AppCompatActivity implements OnClickListener {
    private TextView create;
    private TextView recover,fhselete;
    private ImageView fanhui;
    private Dialog mWeiboDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selete);
        create=(TextView) findViewById(R.id.create);Data.setresulterror("no");
        recover=(TextView) findViewById(R.id.recover);
        fhselete=(TextView) findViewById(R.id.fhselete);
        fanhui=(ImageView) findViewById(R.id.fanhuiadd);
        create.setOnClickListener(this);
        recover.setOnClickListener(this);
        fanhui.setOnClickListener(this);
        fhselete.setOnClickListener(this);
        Data.settype("main");
        Data.setcontext(BleActivity.this);
        new Utils().service_init(getApplicationContext());
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
            Data.settype("main+c");
            Data.setbletype("resetselect");
            String data = "55aaf1000000f1aa55";
            sendble(data, Data.getmService());
//            Intent intent2 = new Intent(this, CreateActivity.class);
//            startActivity(intent2);
        }
        if(v.getId() == R.id.recover) {
            Data.settype("main+r");
            Data.setbletype("resetselect");
            String data = "55aaf1000000f1aa55";
            sendble(data, Data.getmService());
        }
        if(v.getId() == R.id.fanhuiadd) {
            Intent intent2 = new Intent(this, MainActivity.class);
            startActivity(intent2);
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

    public void fingerprint(){
        AlertDialog.Builder builder = new AlertDialog.Builder(Data.getcontext());
        builder.setCancelable(false)//设置点击对话框外部区域不关闭对话框
                .setTitle("录入指纹")
                .setMessage("是否录入指纹？确认（3个），取消（1个）")
                .setNegativeButton(R.string.f513, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        mWeiboDialog = WeiboDialogUtils.createLoadingDialog(Data.getcontext(), "请在设备上录入您的指纹信息");
                        Data.setdialog(mWeiboDialog);
                        Data.setbletype("Recording fingerprints");Data.setresulterror("no");
                        String ret=Utils.strhex("f201018000000000");
                        String a = "55aaf201018000000000"+ret+"aa55";
                        sendble(a,Data.getmService());
                    }
                })
                .setPositiveButton(R.string.f514, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mWeiboDialog = WeiboDialogUtils.createLoadingDialog(Data.getcontext(), "请在设备上录入您的指纹信息");
                        Data.setdialog(mWeiboDialog);
                        Data.setbletype("Recording fingerprints3");Data.setresulterror("no");
                        String a = "55aaf20103808080000070aa55";
                        sendble(a,Data.getmService());
//                        String ret=Utils.strhex("f301010080000000");
//                        String a = "55aaf301010080000000"+ret+"aa55";
                        sendble(a,Data.getmService());
                    }
                })
                .show();
    }
}
