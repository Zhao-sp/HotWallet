package com.wallet.cold.app.pawn;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.DigitsKeyListener;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wallet.R;
import com.wallet.cold.utils.Data;
import com.wallet.cold.utils.LocalManageUtil;
import com.wallet.cold.utils.LogCook;

import static com.wallet.cold.utils.Utils.sendble;

public class TzActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView fhgx,name,fxtoken,tztoken,cdname;
    private Dialog mWeiboDialog;
    private ImageView image;
    private EditText et;
    private Button submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tz);
        Data.settype("tzactivity");
        Data.setcontext(TzActivity.this);
        fhgx=(TextView) findViewById(R.id.fhgx);fhgx.setOnClickListener(this);
        image=(ImageView) findViewById(R.id.image);
        et=findViewById(R.id.input);
        name=findViewById(R.id.name);
        fxtoken=findViewById(R.id.fxtoken);
        tztoken=findViewById(R.id.tztoken);
        cdname=findViewById(R.id.cdname);
        submit=findViewById(R.id.submit);submit.setOnClickListener(this);
        Bundle extras = getIntent().getExtras();
        if (null != extras) {
            String name1 = extras.getString("name");
            String tokenamount = extras.getString("tokenamount");
            String balance = extras.getString("balance");
            String shopname = extras.getString("shopname");
            String objectId = extras.getString("objectId");
            Data.setobjectId(objectId);
            if(name1.equals("花瓶")){
                image.setImageResource(R.drawable.huaping);
            }else if(name1.equals("字画")){
                image.setImageResource(R.drawable.zihua);
            }
            name.setText(name1);
            fxtoken.setText(tokenamount);
            tztoken.setText(balance);
            cdname.setText(shopname);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {//如果返回键按下
            Intent intent = new Intent(this, InvestActivity.class);
            startActivity(intent);
        }
        return false;
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fhgx) {
            Intent intent = new Intent(this, InvestActivity.class);
            startActivity(intent);
        }
        if (v.getId() == R.id.submit) {
            if (et.getText().toString().length() < 1) {
                Toast.makeText(getApplicationContext(), "请输入投资金额", Toast.LENGTH_SHORT).show();
            } else if (et.getText().toString().equals("0")) {
                Toast.makeText(getApplicationContext(), "充值金额不能为零", Toast.LENGTH_SHORT).show();
            } else if (Long.parseLong(et.getText().toString())>Long.parseLong(tztoken.getText().toString())) {
                Toast.makeText(getApplicationContext(), "投资金额超过上限", Toast.LENGTH_SHORT).show();
            } else {
                final EditText inputServer = new EditText(TzActivity.this);
                inputServer.setTransformationMethod(PasswordTransformationMethod.getInstance());
                inputServer.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
                AlertDialog.Builder builder = new AlertDialog.Builder(TzActivity.this);
                builder.setTitle("请输入PIN码").setView(inputServer).setNegativeButton(R.string.f513, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.setPositiveButton(R.string.f514, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if(inputServer.getText().toString().equals("")){
                            Toast.makeText(Data.getcontext(), "PIN码不能为空", Toast.LENGTH_SHORT).show();
                        }else {
                            Data.settxamount(et.getText().toString());
                            String data1 = "770c07e5";
                            String data2 = Integer.toHexString(Integer.parseInt(et.getText().toString()));
                            for (int length = data2.length(); length < 64; length++) {
                                data2 = "0" + data2;
                            }
                            String data3 = "0000000000000000000000000000000000000000000000000000000000000001";
                            String data4 = "0000000000000000000000000000000000000000000000000000000000000001";
                            String data5 = Data.getethaddress();
                            for (int length = data5.length(); length < 64; length++) {
                                data5 = "0" + data5;
                            }
                            String data6 = "000000000000000000000000de94cd3a0424860c7ea0ba875d66dbcc9af284c5";
                            String data7 = "0000000000000000000000000000000000000000000000000000000000000001";
                            String data8 = "0000000000000000000000000000000000000000000000000000000000000001";
                            String data9 = "0000000000000000000000000000000000000000000000000000000000000001";
                            Data.sethiersign(data1+ data2+data3+data4+data5+data6+data7+data8+data9);
                            LogCook.d("data", Data.gethiersign());
                            Data.setlimit(inputServer.getText().toString());
                            Data.setsign("end0");
                            Data.setbizhong("Pawn");
                            Data.setsaoma("yes");
                            String a = "55aa260110000002000035aa55";//结束签名
                            sendble(a, Data.getmService());
                        }
                    }
                });
                builder.show();
            }
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocalManageUtil.setLocal(newBase));
    }
}
