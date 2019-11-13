package com.wallet.cold.app.main;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.wallet.R;
import com.wallet.cold.utils.Data;
import com.wallet.cold.utils.LocalManageUtil;
import com.wallet.cold.utils.Utils;
import com.wallet.cold.utils.WeiboDialogUtils;

public class NumbersActivity extends AppCompatActivity implements View.OnClickListener {
//    private TextView fhnum;
//    private ImageView fanhui;
    private Dialog mWeiboDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_numbers);
//        fhnum=(TextView)findViewById(R.id.fhnum);
//        fanhui=(ImageView) findViewById(R.id.fanhuiadd);
//        fanhui.setOnClickListener(this);
//        fhnum.setOnClickListener(this);
        mWeiboDialog = WeiboDialogUtils.createLoadingDialog(NumbersActivity.this, this.getResources().getString(R.string.num5));
        Data.settype("numbers");
        Data.setcontext(NumbersActivity.this);
        new Utils().service_init(NumbersActivity.this);
    }

    @Override
    public void onClick(View v) {
//        if(v.getId()==R.id.fanhuiadd){
//            Intent intent = new Intent(NumbersActivity.this,CreateActivity.class);
//            startActivity(intent);
//        }else if(v.getId()==R.id.fhnum){
//            Intent intent = new Intent(NumbersActivity.this,CreateActivity.class);
//            startActivity(intent);
//        }
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocalManageUtil.setLocal(newBase));
    }
}
