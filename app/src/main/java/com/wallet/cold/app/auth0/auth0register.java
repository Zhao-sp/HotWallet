package com.wallet.cold.app.auth0;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wallet.R;
import com.wallet.cold.app.main.IndexActivity;
import com.wallet.cold.utils.Data;
import com.wallet.cold.utils.LocalManageUtil;
import com.wallet.cold.utils.LogCook;
import com.wallet.cold.utils.WeiboDialogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import static com.wallet.cold.utils.Utils.getIndex;
import static com.wallet.cold.utils.Utils.hexStringToString;
import static com.wallet.cold.utils.Utils.sendble;

public class auth0register extends AppCompatActivity implements OnClickListener {

    private ImageView fanhui5;
    private TextView fhreset;
    private EditText et_pin,et_user,et_pwd;
    private Dialog mWeiboDialog;
    private Button verify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth0register);
        Data.settype("auth0register");
        Data.setcontext(auth0register.this);
        et_user=(EditText) findViewById(R.id.username);
        et_pwd=(EditText) findViewById(R.id.passward);
        et_pin=(EditText) findViewById(R.id.pin);
        verify=(Button)findViewById(R.id.verify);
        verify.setOnClickListener(this);
        fanhui5=(ImageView)findViewById(R.id.fanhui5);
        fanhui5.setOnClickListener(this);
        fhreset=(TextView)findViewById(R.id.fhreset);
        fhreset.setOnClickListener(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {//如果返回键按下
            Intent intent = new Intent(this, IndexActivity.class);
            startActivity(intent);
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fanhui5) {
            Intent intent = new Intent(this, IndexActivity.class);
            startActivity(intent);
        }
        if (v.getId() == R.id.fhreset) {
            Intent intent1 = new Intent(this, IndexActivity.class);
            startActivity(intent1);
        }
        if (v.getId() == R.id.verify) {
            Data.setauth0sign("register");
            if (et_user.getText().toString().equals("")) {
                Toast.makeText(Data.getcontext(), Data.getcontext().getResources().getString(R.string.m3), Toast.LENGTH_SHORT).show();
            } else if (et_pwd.getText().toString().equals("")) {
                Toast.makeText(Data.getcontext(), Data.getcontext().getResources().getString(R.string.m4), Toast.LENGTH_SHORT).show();
            } else if ((et_pin.getText().toString().equals("") || et_pin.getText().toString().length() != 4)) {
                Toast.makeText(Data.getcontext(), Data.getcontext().getResources().getString(R.string.m2), Toast.LENGTH_SHORT).show();
            } else {
                mWeiboDialog = WeiboDialogUtils.createLoadingDialog(auth0register.this, Data.getcontext().getResources().getString(R.string.m1));
                Data.setdialog(mWeiboDialog);
                Data.setusername(et_user.getText().toString());
                Data.setpassword(et_pwd.getText().toString());
                Data.setlimit(et_pin.getText().toString());
                Data.setbizhong("ERC20");//币种分类
                Data.setsign("end0");//结束指令
                Data.setsaoma("yes");//是否进行签名
                String a1 = "55aa260110000002000035aa55";//结束签名
                sendble(a1, Data.getmService());
            }
        }
    }

    /**
     * auth0注册请求
     * @param sign
     */
    public void auth0register(String sign){//auth0注册请求
        new Thread(new Runnable() {
            public void run() {
                String address = hexStringToString(Data.getauth0address());
                address = address.substring(2,address.length()-1);
                String data="name="+Data.getusername()+"&pwd="+Data.getpassword()+"&address=0x"+address+"&publicKey="+Data.getauth0pubkey()+"&sign=0x"+sign+
                        "&uuid="+Data.getauth0uuid()+"&ethAddress=0x"+Data.getethaddress();
                String result = "";
                try {
                    //data = URLEncoder.encode(data, "UTF-8");
                    String urlName = "http://111.225.200.132:8181/hierstarQrCode/userinfo/register?"+data;
                    LogCook.d("发送参数", urlName);
                    URL U = new URL(urlName);
                    URLConnection connection = U.openConnection();
                    connection.connect();
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line;
                    while ((line = in.readLine()) != null) {
                        result += line;
                    }
                    LogCook.d("auth0注册请求返回数据", result);
                    in.close();
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("status_Sucess").equals("true")) {
                        Looper.prepare();
                        Toast.makeText(Data.getcontext(), Data.getcontext().getResources().getString(R.string.fff38), Toast.LENGTH_SHORT).show();
                        Data.getdb().execSQL("insert into Auth0AddressTb (blename,name,address) values " +
                                "('" + Data.getdevicename() + "','" + Data.getusername() + "','" + Data.getethaddress() + "')");//保存用戶名到本地
                        WeiboDialogUtils.closeDialog(Data.getdialog());
                        Data.getcontext().startActivity(new Intent(Data.getcontext(), IndexActivity.class));
                        Data.setauth0type(Data.getusername());
                        Looper.loop();
                    } else if (jsonObject.getString("status_Sucess").equals("false")) {//返回错误信息
                        Looper.prepare();
                        if(jsonObject.getString("status_Message").contains("Sorry")&&jsonObject.getString("status_Message").contains(Data.getethaddress())){
                            String result1=jsonObject.getString("status_Message");
                            int index = getIndex(result1, 4, " ");
                            String name= result1.substring(index+1,result1.length());
                            Cursor cursor = Data.getdb().rawQuery("select * from Auth0AddressTb where name=name", null);
                            if (cursor != null && cursor.getCount() > 0) {
                                cursor.close();
                            }else {
                                Data.getdb().execSQL("insert into Auth0AddressTb (blename,name,address) values " +
                                        "('" + Data.getdevicename() + "','" + name + "','" + Data.getethaddress() + "')");//保存用戶名到本地
                            }
                            Data.setauth0type(name);
                        }else{
                            Data.setusername("");
                        }
                        Toast.makeText(Data.getcontext(), Data.getcontext().getResources().getString(R.string.fff39) +
                                jsonObject.getString("status_Result") + ":" + jsonObject.getString("status_Message"), Toast.LENGTH_SHORT).show();
                        WeiboDialogUtils.closeDialog(Data.getdialog());
                        Looper.loop();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start(); // 开启线程
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocalManageUtil.setLocal(newBase));
    }
}

