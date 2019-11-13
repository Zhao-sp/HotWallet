package com.wallet.cold.app.index;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.wallet.R;
import com.wallet.cold.utils.Data;
import com.wallet.cold.utils.ImageAdapter;
import com.wallet.cold.utils.LocalManageUtil;
import com.wallet.cold.utils.LogCook;
import com.wallet.cold.utils.MyListView;
import com.wallet.cold.utils.Utils;
import com.wallet.cold.utils.WeiboDialogUtils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.wallet.cold.utils.Utils.getIndex;

public class ZXActivity extends AppCompatActivity {
    private Handler handler;
    private String result="";
    private Dialog mWeiboDialog;
    private MyListView lv1;
    private List<String> resultdata = new ArrayList<>();
    private String neirong;
    private String name;
    private String imageurl;
    private String time;
    private ImageAdapter adapter;
    private List<String> zxname=new ArrayList<>();
    private List<String> zximage=new ArrayList<>();
    private List<String> zxneirong=new ArrayList<>();
    private List<String> zxtime=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragmentzx);
        lv1=(MyListView)findViewById(R.id.list_zx);
        Data.settype("zxactivity");Data.setisshangla(false);
        Data.setcontext(ZXActivity.this);
        mWeiboDialog = WeiboDialogUtils.createLoadingDialog(ZXActivity.this, this.getResources().getString(R.string.f4));
        if(Utils.isNetworkConnected(Data.getcontext())) {
            send1();
        }else{
            Toast.makeText(Data.getcontext(), Data.getcontext().getResources().getString(R.string.cd2), Toast.LENGTH_SHORT).show();
            WeiboDialogUtils.closeDialog(mWeiboDialog);
        }
        lv1.setonRefreshListener(new MyListView.OnRefreshListener() {
            public void onRefresh() {
                new AsyncTask<Void, Void, Void>() {
                    protected Void doInBackground(Void... params) {
                        try {
                            Thread.sleep(1000);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                    @Override
                    protected void onPostExecute(Void result) {
                        adapter.notifyDataSetChanged();
                        Data.getzxname().clear();
                        Data.getzxtime().clear();
                        Data.getzxneirong().clear();
                        zxneirong.clear();
                        zxname.clear();
                        zxtime.clear();
                        zximage.clear();
                        resultdata.clear();
                        Data.setpage(1);
                        mWeiboDialog = WeiboDialogUtils.createLoadingDialog(ZXActivity.this, ZXActivity.this.getResources().getString(R.string.f4));
                        send1();
                        lv1.onRefreshComplete();
                    }
                }.execute();
            }
        });
        lv1.setOnLoadMoreListener(new MyListView.OnLoadMoreListener() {//上拉加载
            @Override
            public void onLoadMore() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Data.setisshangla(true);
                        adapter.notifyDataSetChanged();
                        resultdata.clear();
                        Data.setpage(Data.getpage()+1);
                        mWeiboDialog = WeiboDialogUtils.createLoadingDialog(ZXActivity.this, ZXActivity.this.getResources().getString(R.string.f4));
                        send1();
                        lv1.finishLoadMore();
                    }
                }, 1000);
            }
        });
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
                    for (int i=0;i<resultdata.size();i++){
                        int count=Utils.getSubCount_2(resultdata.get(i),"&");
                        if(count==3) {
                            int index1 = getIndex(resultdata.get(i), 1, "&");
                            int index2 = getIndex(resultdata.get(i), 2, "&");
                            int index3 = getIndex(resultdata.get(i), 3, "&");
                            neirong = resultdata.get(i).substring(0, index1);
                            name = resultdata.get(i).substring(index1 + 1, index2);
                            imageurl = resultdata.get(i).substring(index2 + 1, index3);
                            time = resultdata.get(i).substring(index3 + 1, resultdata.get(i).length());
                            zxneirong.add(neirong);
                            zxname.add(name);
                            zximage.add(imageurl);
                            zxtime.add(time);
                            Data.setzxname(zxname);
                            Data.setzxtime(zxtime);
                            Data.setzxneirong(zxneirong);
                        }
                    }
                    for(int i=0;i<zximage.size();i++){
                        WeiboDialogUtils.closeDialog(mWeiboDialog);
                        adapter = new ImageAdapter(ZXActivity.this, 0, zximage);
                        if(Data.getisshangla()){
                            adapter.notifyDataSetChanged();
                        }else {
                            lv1.setAdapter(adapter);
                        }
                    }
                }
            }
        };
    }
    public void send() {
        result="";
        HttpURLConnection conn = null;//声明连接对象
        String urlStr = "http://111.225.200.132:8023/cgi-bin/getnews?page="+Data.getpage();
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
                    resultdata.add(inputLine);
                }
                is.close();
            }
            conn.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
            WeiboDialogUtils.closeDialog(mWeiboDialog);
        }
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocalManageUtil.setLocal(newBase));
    }
}
