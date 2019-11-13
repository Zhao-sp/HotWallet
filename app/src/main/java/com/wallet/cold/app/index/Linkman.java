package com.wallet.cold.app.index;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.wallet.R;
import com.wallet.cold.app.main.IndexActivity;
import com.wallet.cold.utils.CaptureActivity;
import com.wallet.cold.utils.Data;
import com.wallet.cold.utils.LocalManageUtil;

public class Linkman extends AppCompatActivity implements View.OnClickListener {

    private TextView addaddress,fhreset,tianjia,t3,dzmc,t4,t5,address;
    private ImageView fanhui5,jia,jian,saoyisao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linkman);
        Data.settype("Linkman");
        Data.setcontext(Linkman.this);
        addaddress=(TextView)findViewById(R.id.addaddress);
        addaddress.setOnClickListener(this);
        fhreset=(TextView)findViewById(R.id.fhreset);
        fhreset.setOnClickListener(this);
        fanhui5=(ImageView) findViewById(R.id.fanhui5);
        fanhui5.setOnClickListener(this);
        jia=(ImageView) findViewById(R.id.jia);
        jian=(ImageView) findViewById(R.id.jian);
        saoyisao=(ImageView) findViewById(R.id.saoyisao);
        saoyisao.setOnClickListener(this);
        tianjia=(TextView)findViewById(R.id.tianjia);
        t3=(TextView)findViewById(R.id.t3);
        t4=(TextView)findViewById(R.id.t4);
        t5=(TextView)findViewById(R.id.t5);
        dzmc=(TextView)findViewById(R.id.dzmc);
        address=(TextView)findViewById(R.id.address);
        Bundle extras = getIntent().getExtras();
        if (null != extras) {
            String result = extras.getString("result");
            address.setText(result);
            jia.setVisibility(View.INVISIBLE);
            tianjia.setVisibility(View.INVISIBLE);
            t3.setVisibility(View.INVISIBLE);
        }else {
            jian.setVisibility(View.INVISIBLE);
            saoyisao.setVisibility(View.INVISIBLE);
            t4.setVisibility(View.INVISIBLE);
            t5.setVisibility(View.INVISIBLE);
            address.setVisibility(View.INVISIBLE);
            dzmc.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fanhui5) {
            Data.setresult("1");
            Intent intent = new Intent(this, IndexActivity.class);
            startActivity(intent);
        }
        if (v.getId() == R.id.fhreset) {
            Data.setresult("1");
            Intent intent = new Intent(this, IndexActivity.class);
            startActivity(intent);
        }
        if (v.getId() == R.id.saoyisao) {
            Intent intent = new Intent(this, CaptureActivity.class);
            startActivity(intent);
        }
        if (v.getId() == R.id.addaddress) {
            View view = LayoutInflater.from(Linkman.this).inflate(R.layout.bottom_address, null);
            final Dialog dialog = new Dialog(this, R.style.common_dialog);
            dialog.setContentView(view);
            dialog.setCancelable(true);
            dialog.show();
            // 监听
            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    t3.setVisibility(View.INVISIBLE);tianjia.setVisibility(View.INVISIBLE);
                    jia.setVisibility(View.INVISIBLE);jian.setVisibility(View.VISIBLE);
                    saoyisao.setVisibility(View.VISIBLE);t4.setVisibility(View.VISIBLE);
                    t5.setVisibility(View.VISIBLE);address.setVisibility(View.VISIBLE);
                    dzmc.setVisibility(View.VISIBLE);
                    if(v.getId() == R.id.addbtc) {

                    }
                    if(v.getId() == R.id.addeth) {
                        dzmc.setText("ETH:");
                    }
                    if(v.getId() == R.id.addeos) {
                        dzmc.setText("EOS:");
                    }
                    dialog.dismiss();
                }
            };
            TextView addbtc = (TextView) view.findViewById(R.id.addbtc);
            TextView addeth = (TextView) view.findViewById(R.id.addeth);
            TextView addeos = (TextView) view.findViewById(R.id.addeos);
            TextView mBtnCancel = (TextView) view.findViewById(R.id.cancel);
            addbtc.setOnClickListener(listener);
            addeth.setOnClickListener(listener);
            addeos.setOnClickListener(listener);
            mBtnCancel.setOnClickListener(listener);
            // 设置相关位置，一定要在 show()之后
            Window window = dialog.getWindow();
            window.getDecorView().setPadding(0, 0, 0, 0);
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.gravity = Gravity.BOTTOM;
            window.setAttributes(params);
        }
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocalManageUtil.setLocal(newBase));
    }
}

