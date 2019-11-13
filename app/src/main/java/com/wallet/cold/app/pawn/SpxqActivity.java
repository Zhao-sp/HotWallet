package com.wallet.cold.app.pawn;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wallet.R;
import com.wallet.cold.utils.Data;
import com.wallet.cold.utils.LocalManageUtil;
import com.wallet.cold.utils.PayFragment;
import com.wallet.cold.utils.PayPwdView;
import com.wallet.cold.utils.Utils;
import com.wallet.cold.utils.WeiboDialogUtils;

import static com.wallet.cold.utils.Utils.sendble;

public class SpxqActivity extends AppCompatActivity implements PayPwdView.InputCallBack,View.OnClickListener{
    private TextView fhgx,name,cdamount,tztoken;
    private Dialog mWeiboDialog;
    private ImageView image;
    private Button submit;
    private PayFragment fragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        Data.settype("spxqactivity");
        Data.setcontext(SpxqActivity.this);
        fhgx=(TextView) findViewById(R.id.fhgx);fhgx.setOnClickListener(this);
        image=(ImageView) findViewById(R.id.image);
        name=findViewById(R.id.name);
        cdamount=findViewById(R.id.cdamount);
        tztoken=findViewById(R.id.tztoken);
        submit=findViewById(R.id.submit);submit.setOnClickListener(this);
        mWeiboDialog = WeiboDialogUtils.createLoadingDialog(SpxqActivity.this, this.getResources().getString(R.string.type4));
        Data.setdialog(mWeiboDialog);
        Bundle extras = getIntent().getExtras();
        if (null != extras) {
            String name1 = extras.getString("name");
            String balance = extras.getString("balance");
            if (name1.contains("电视机")) {
                image.setImageResource(R.drawable.tv);
            }else if (name1.contains("冰箱")) {
                image.setImageResource(R.drawable.bx);
            }else if (name1.contains("电饭锅")) {
                image.setImageResource(R.drawable.dg);
            }
            name.setText(name1);
            tztoken.setText(balance);
        }
        Data.setcardmoney(cdamount);
        new Utils().service_init(getApplicationContext());
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {//如果返回键按下
            Intent intent = new Intent(this, ShopActivity.class);
            startActivity(intent);
        }
        return false;
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fhgx) {
            Intent intent = new Intent(this, ShopActivity.class);
            startActivity(intent);
        }
        if (v.getId() == R.id.submit) {
            Bundle bundle = new Bundle();
            bundle.putString(PayFragment.EXTRA_CONTENT, tztoken.getText().toString());
            fragment = new PayFragment();
            fragment.setArguments(bundle);
            fragment.setPaySuccessCallBack(this);
            fragment.show(getSupportFragmentManager(), "Pay");
        }
    }

    @Override
    public void onInputFinish(String result) {
        fragment.dismiss();
        //    	dismissDialog(id);
        //    	finish();
        if(result.equals("1234")) {
            if(Data.getresult().equals("pt")) {
                double d=Double.parseDouble(Data.getptamount());
                if (Math.round(d) < Long.parseLong(tztoken.getText().toString())) {
                    Toast.makeText(getApplicationContext(), Data.getcontext().getResources().getString(R.string.sc7), Toast.LENGTH_SHORT).show();
                }else {
                    Data.setpaytype("pt");
                    Dialog mWeiboDialog = WeiboDialogUtils.createLoadingDialog(SpxqActivity.this, Data.getcontext().getResources().getString(R.string.sc9));
                    Data.setdialog(mWeiboDialog);
                    String data1 = "a9059cbb0000000000000000000000004080a2b89739f67b56c5bf843a347a6cda72eb92";
                    Data.settxamount(tztoken.getText().toString());
                    String data3 = Integer.toHexString(Integer.parseInt(tztoken.getText().toString()));
                    for (int length = data3.length(); length < 64; length++) {
                        data3 = "0" + data3;
                    }
                    Data.sethiersign(data1 + data3);
                    Data.setlimit(result);
                    Data.setsign("end0");
                    Data.setbizhong("Hier");
                    Data.setsaoma("yes");
                    String a = "55aa260110000002000035aa55";//结束签名
                    sendble(a, Data.getmService());
                }
            }else if(Data.getresult().equals("card")) {
                if(Long.parseLong(cdamount.getText().toString())<Long.parseLong(tztoken.getText().toString())) {
                    Toast.makeText(getApplicationContext(), Data.getcontext().getResources().getString(R.string.sc8), Toast.LENGTH_SHORT).show();
                }else {
                    int amount1 = Integer.valueOf(tztoken.getText().toString() + "00");
                    Dialog mWeiboDialog = WeiboDialogUtils.createLoadingDialog(SpxqActivity.this, Data.getcontext().getResources().getString(R.string.sc9));
                    Data.setdialog(mWeiboDialog);
                    Data.setbletype("wallettx");Data.setpaytype("card");
                    String amount = Integer.toHexString(amount1);
                    if (amount.length() == 2) {
                        amount = "000000" + amount;
                    }
                    if (amount.length() == 3) {
                        amount = "00000" + amount;
                    }
                    if (amount.length() == 4) {
                        amount = "0000" + amount;
                    }
                    if (amount.length() == 5) {
                        amount = "000" + amount;
                    }
                    if (amount.length() == 6) {
                        amount = "00" + amount;
                    }
                    if (amount.length() == 7) {
                        amount = "0" + amount;
                    }
                    String ret = Utils.strhex("8C01000000020004" + amount);
                    String a = "55aa8C01000000020004" + amount + ret + "aa55";
                    Data.setresulterror("no");
                    sendble(a, Data.getmService());
                }
            }
        } else {
            Toast.makeText(getApplicationContext(), Data.getcontext().getResources().getString(R.string.sc10), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocalManageUtil.setLocal(newBase));
    }
}
