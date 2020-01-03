package com.wallet.cold.app.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.wallet.CreateOrImportActivity;
import com.wallet.R;
import com.wallet.cold.app.main.IndexActivity;
import com.wallet.cold.utils.Data;
import com.wallet.cold.utils.LocalManageUtil;
import com.wallet.cold.utils.Utils;

public class Fragment5 extends Activity implements View.OnClickListener {
    private RelativeLayout resetpin,chushihua,exit,languages,fingerprints,delete;
    private ImageView fanhui;
    private Dialog mWeiboDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment5);
        Data.setsaoma("no");
        resetpin=(RelativeLayout) findViewById(R.id.chongzhi);
        resetpin.setOnClickListener(this);
        languages=(RelativeLayout) findViewById(R.id.languages);
        languages.setOnClickListener(this);
        fingerprints=(RelativeLayout) findViewById(R.id.fingerprints);
        fingerprints.setOnClickListener(this);
        exit=(RelativeLayout) findViewById(R.id.exit);
        exit.setOnClickListener(this);
        chushihua=(RelativeLayout) findViewById(R.id.gengxin);
        chushihua.setOnClickListener(this);
        delete=(RelativeLayout) findViewById(R.id.delete);
        delete.setOnClickListener(this);
        if(Data.getapptype().equals("cold")){
            delete.setVisibility(View.GONE);
        }
        fanhui=(ImageView) findViewById(R.id.fanhui5);
        fanhui.setOnClickListener(this);
        Data.settype("fragment5");
        Data.setcontext(Fragment5.this);
        new Utils().service_init(getApplicationContext());
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.chongzhi) {
            Intent intent = new Intent(Data.getcontext(), ResetActivity.class);
            Data.getcontext().startActivity(intent);
        }
        if(v.getId() == R.id.fanhui5) {
            Data.getcontext().startActivity(new Intent(this, IndexActivity.class));
        }
        if(v.getId() == R.id.fhf5) {
            Data.getcontext().startActivity(new Intent(this, IndexActivity.class));
        }
        if(v.getId() == R.id.delete) {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(Fragment5.this);
            builder1.setCancelable(false)//设置点击对话框外部区域不关闭对话框
                    .setTitle(R.string.f54)
                    .setNegativeButton(R.string.f513, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setPositiveButton(R.string.f514, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Data.getdb().execSQL("DELETE FROM HotAddressTb");
                            Toast.makeText(Data.getcontext(), "删除成功", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Fragment5.this, CreateOrImportActivity.class);
                            startActivity(intent);
                        }
                    })
                    .show();
        }
        if(v.getId() == R.id.gengxin) {
            Intent intent2 = new Intent(Data.getcontext(), GengxinActivity.class);
            Data.getcontext().startActivity(intent2);
        }
        if(v.getId() == R.id.languages) {
            Intent intent3 = new Intent(Data.getcontext(), LanguagesActivity.class);
            Data.getcontext().startActivity(intent3);
        }
        if(v.getId() == R.id.fingerprints) {
            Intent intent = new Intent(Data.getcontext(), Fingerprints.class);
            Data.getcontext().startActivity(intent);
        }
        if(v.getId() == R.id.exit) {
            Data.getmService().disconnect();
            Data.getmService().close();
            Intent intent = new Intent(Data.getcontext(), CreateOrImportActivity.class);
            Data.getcontext().startActivity(intent);
        }
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocalManageUtil.setLocal(newBase));
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {//按返回键退出程序
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(Data.getcontext(),IndexActivity.class);
            Data.getcontext().startActivity(intent);
        }
        return super.onKeyDown(keyCode, event);
    }
}
