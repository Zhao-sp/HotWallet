package com.wallet.cold.app.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wallet.R;
import com.wallet.cold.app.main.IndexActivity;
import com.wallet.cold.utils.Data;
import com.wallet.cold.utils.LocalManageUtil;
import com.wallet.cold.utils.Utils;
import com.wallet.cold.utils.WeiboDialogUtils;

import static com.wallet.cold.utils.Utils.sendble;

public class Fragment5 extends Activity implements View.OnClickListener {
    private TextView resetpin;
    private TextView chushihua;
    private TextView chushihuaka,fhf5,languages,fingerprints;
    private ImageView fanhui;
    private Dialog mWeiboDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment5);
        Data.setsaoma("no");
        resetpin=(TextView) findViewById(R.id.chongzhi);
        resetpin.setOnClickListener(this);
        languages=(TextView) findViewById(R.id.languages);
        languages.setOnClickListener(this);
        fingerprints=(TextView) findViewById(R.id.fingerprints);
        fingerprints.setOnClickListener(this);
        fhf5=(TextView) findViewById(R.id.fhf5);
        fhf5.setOnClickListener(this);
        chushihua=(TextView) findViewById(R.id.gengxin);
        chushihua.setOnClickListener(this);chushihuaka=(TextView) findViewById(R.id.chushihuaka);
        chushihuaka.setOnClickListener(this);
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
        if(v.getId() == R.id.chushihuaka) {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(Fragment5.this);
            builder1.setCancelable(false)//设置点击对话框外部区域不关闭对话框
                    .setTitle(R.string.f511)
                    .setMessage(R.string.f512)
                    .setNegativeButton(R.string.f513, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            WeiboDialogUtils.closeDialog(mWeiboDialog);
                        }
                    })
                    .setPositiveButton(R.string.f514, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            mWeiboDialog = WeiboDialogUtils.createLoadingDialog(Fragment5.this, Data.getcontext().getResources().getString(R.string.f515));
                            Data.setdialog(mWeiboDialog);
                            Data.setbletype("chushihuazhiwen");
                            String a = "55aaf4000000f4aa55";
                            sendble(a,Data.getmService());
                        }
                    })
                    .show();
            //Toast.makeText(Data.getcontext(), Data.getcontext().getResources().getString(R.string.gx2), Toast.LENGTH_SHORT).show();
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
