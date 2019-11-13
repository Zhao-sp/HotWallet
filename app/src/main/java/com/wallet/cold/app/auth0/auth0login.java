package com.wallet.cold.app.auth0;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wallet.R;

import com.wallet.cold.app.index.Transfer;
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

public class auth0login extends AppCompatActivity implements OnClickListener {

    private ImageView photo;
    private TextView fhreset;
    private Dialog mWeiboDialog;
    private Button verify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth0login);
        Data.settype("auth0login");
        Data.setcontext(auth0login.this);
        photo= (ImageView) findViewById(R.id.img2);
        photo.setImageDrawable(getResources().getDrawable(R.drawable.haibo));
        verify=(Button)findViewById(R.id.verify);
        verify.setOnClickListener(this);
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
        if(v.getId() == R.id.fhreset) {
            Intent intent1 = new Intent(this, IndexActivity.class);
            startActivity(intent1);
        }
        if(v.getId() == R.id.verify) {
            Data.setauth0sign("login");
            auth0tzzf();
        }
    }

    /**
     * 获取挑战符
     */
    public void auth0tzzf() {
        new Thread(new Runnable() {
            public void run() {
                String result = "";
                try {
                    String urlName = "http://111.225.200.132:8181/hierstarQrCode/login/login?eth_address="+Data.getethaddress()+"&uuid="+Data.getauth0uuid();
                    LogCook.d("发送参数", urlName);
                    URL U = new URL(urlName);
                    URLConnection connection = U.openConnection();
                    connection.connect();
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line;
                    while ((line = in.readLine()) != null) {
                        result += line;
                    }
                    LogCook.d("auth0返回挑战字符", result);
                    in.close();
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("status_Sucess").equals("true")) {
                        Data.setplaint(jsonObject.getString("status_Message"));
                        Data.setdialog(mWeiboDialog);
                        Data.setbizhong("ERC20");//币种分类
                        Data.setsaoma("yes");//是否进行签名
                        new Transfer().sign(Data.getplaint());
                    } else if (jsonObject.getString("status_Sucess").equals("false")) {//返回错误信息
                        Looper.prepare();
                        Toast.makeText(Data.getcontext(), Data.getcontext().getResources().getString(R.string.uhttp1) +
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

    /**
     * auth0登录请求
     * @param sign
     */
    public void auth0login(String sign) {
        new Thread(new Runnable() {
            public void run() {
                String result = "";
                try {
                    String urlName = "http://111.225.200.132:8181/hierstarQrCode/login/verify?plaint="+Data.getplaint()+"&uuid="+Data.getauth0uuid()+"&SignResult="+sign+"&username=" + Data.getusername();
                    LogCook.d("发送参数", urlName);
                    URL U = new URL(urlName);
                    URLConnection connection = U.openConnection();
                    connection.connect();
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line;
                    while ((line = in.readLine()) != null) {
                        result += line;
                    }
                    LogCook.d("auth0登录请求返回数据", result);
                    in.close();
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("status_Sucess").equals("true")) {
                        Looper.prepare();
                        Toast.makeText(Data.getcontext(), Data.getcontext().getResources().getString(R.string.fff40), Toast.LENGTH_SHORT).show();
                        WeiboDialogUtils.closeDialog(Data.getdialog());
                        Data.getcontext().startActivity(new Intent(Data.getcontext(), IndexActivity.class));
                        Looper.loop();
                    } else if (jsonObject.getString("status_Sucess").equals("false")) {//返回错误信息
                        Looper.prepare();
                        Toast.makeText(Data.getcontext(), Data.getcontext().getResources().getString(R.string.fff41) +
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
