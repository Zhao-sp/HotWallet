package com.wallet.cold.app.index;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.wallet.R;
import com.wallet.cold.utils.LocalManageUtil;
import com.wallet.cold.utils.LogCook;
import com.wallet.cold.utils.Utils;
import com.wallet.cold.utils.WeiboDialogUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ZXNRActivity extends Activity {
    private TextView name;
    private Handler handler;
    private String result="";
    private Dialog mWeiboDialog;
    private String amsg1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zixun);
        name=(TextView)findViewById(R.id.name);
        Intent intent=getIntent();
        String amsg=intent.getStringExtra("amsg");
        amsg1=intent.getStringExtra("amsg1");
        //name.setText(amsg);
        WebView browser=(WebView)findViewById(R.id.Toweb);
        browser.loadUrl(amsg1);
        browser.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
//        mWeiboDialog = WeiboDialogUtils.createLoadingDialog(ZXNRActivity.this, this.getResources().getString(R.string.f4));
//        send1();
    }
    public void send1(){
        new Thread(new Runnable() {
            public void run() {
                send();
                Message m = handler.obtainMessage(); // 获取一个Message
                handler.sendMessage(m); // 发送消息
            }
        }).start();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (result != null) {
                    LogCook.d("资讯数据",String.valueOf(result));
                }
                WeiboDialogUtils.closeDialog(mWeiboDialog);
            }
        };
    }
    public void send() {
        HttpURLConnection conn = null;//声明连接对象
        String urlStr = amsg1;
        InputStream is = null;
        try {
            URL url = new URL(urlStr); //URL对象
            conn = (HttpURLConnection) url.openConnection(); //使用URL打开一个链接,下面设置这个连接
            conn.setRequestMethod("GET"); //使用get请求
            conn.setRequestProperty("Content-Type", "application/json");
            if (conn.getResponseCode() == 200) {//返回200表示连接成功
                is = conn.getInputStream(); //获取输入流
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader bufferReader = new BufferedReader(isr);
                String inputLine = "";
                while ((inputLine = bufferReader.readLine()) != null) {
                    result += inputLine + "\n";
                }
                is.close();
            }
            conn.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocalManageUtil.setLocal(newBase));
    }
}
