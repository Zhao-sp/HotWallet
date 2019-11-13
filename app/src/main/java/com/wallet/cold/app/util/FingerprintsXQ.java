package com.wallet.cold.app.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wallet.R;
import com.wallet.cold.utils.Data;
import com.wallet.cold.utils.LocalManageUtil;
import com.wallet.cold.utils.Utils;

import static com.wallet.cold.utils.Utils.sendble;

public class FingerprintsXQ extends AppCompatActivity implements View.OnClickListener {
    private TextView fhf2,name,deletezw;
    private ImageView fanhui2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zwxq);
        name=(TextView) findViewById(R.id.name1);Data.setresulterror("no");
        name.setText(Data.getfingerprintsname());
        deletezw=(TextView) findViewById(R.id.deletezw);
        deletezw.setOnClickListener(this);
        fhf2=(TextView) findViewById(R.id.fhf2);
        fhf2.setOnClickListener(this);
        fanhui2=(ImageView) findViewById(R.id.fanhui2);
        fanhui2.setOnClickListener(this);
        Data.settype("fingerprintsXQ");
        Data.setcontext(FingerprintsXQ.this);
        new Utils().service_init(getApplicationContext());
    }


    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.fanhui2) {
            Data.getcontext().startActivity(new Intent(this, Fingerprints.class));
        }
        if(v.getId() == R.id.fhf2) {
            Data.getcontext().startActivity(new Intent(this, Fingerprints.class));
        }
        if(v.getId() == R.id.deletezw) {
            Data.setbletype("Delete");
            if(Data.getfingerprints().equals("fingerprints1")){
                String ret=Utils.strhex("f301018000000000");
                String a = "55aaf301018000000000"+ret+"aa55";
                sendble(a,Data.getmService());
            }else if(Data.getfingerprints().equals("fingerprints2")){
                String ret=Utils.strhex("f301010080000000");
                String a = "55aaf301010080000000"+ret+"aa55";
                sendble(a,Data.getmService());
            }else if(Data.getfingerprints().equals("fingerprints3")){
                String ret=Utils.strhex("f301010000800000");
                String a = "55aaf301010000800000"+ret+"aa55";
                sendble(a,Data.getmService());
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
            Intent intent = new Intent(Data.getcontext(),Fingerprints.class);
            Data.getcontext().startActivity(intent);
        }
        return super.onKeyDown(keyCode, event);
    }
}
