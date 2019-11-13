package com.wallet.cold.app.pawn;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.method.DigitsKeyListener;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.wallet.R;
import com.wallet.cold.app.main.IndexActivity;
import com.wallet.cold.utils.Data;
import com.wallet.cold.utils.LocalManageUtil;
import com.wallet.cold.utils.LogCook;
import com.wallet.cold.utils.MyListView;
import com.wallet.cold.utils.Utils;
import com.wallet.cold.utils.Utilshttp;
import com.wallet.cold.utils.WeiboDialogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.wallet.cold.utils.Utils.sendble;
import static java.lang.String.valueOf;

public class CdActivity extends AppCompatActivity implements View.OnClickListener{
    private Handler handler;
    private TextView fhgx,tztype,pt,ptamount;
    private String result="";
    private Dialog mWeiboDialog;
    private MyListView lv1;
    private MyAdapter adapter;
    Bundle bundle = new Bundle();
    List<String> name = new ArrayList<String>();
    List<String> redeemPrice = new ArrayList<String>();
    List<String> shopname = new ArrayList<String>();
    List<String> objectId = new ArrayList<String>();
    List<Integer> isredeem = new ArrayList<Integer>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cd);
        lv1=(MyListView)findViewById(R.id.list_zx);
        pt=findViewById(R.id.pt);
        ptamount=findViewById(R.id.ptamount);
        tztype=(TextView)findViewById(R.id.tztype);tztype.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        fhgx=(TextView) findViewById(R.id.fhgx);fhgx.setOnClickListener(this);tztype.setOnClickListener(this);
        mWeiboDialog = WeiboDialogUtils.createLoadingDialog(CdActivity.this, this.getResources().getString(R.string.f4));
        Data.setdialog(mWeiboDialog);
        if(Utils.isNetworkConnected(Data.getcontext())) {
            send1();
            getptamount();
        }else{
            Toast.makeText(Data.getcontext(), Data.getcontext().getResources().getString(R.string.cd2), Toast.LENGTH_SHORT).show();
            WeiboDialogUtils.closeDialog(mWeiboDialog);
        }
        Data.settype("cdactivity");
        Data.setcontext(CdActivity.this);
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
                        name.clear();
                        redeemPrice.clear();
                        shopname.clear();
                        objectId.clear();
                        isredeem.clear();
                        send1();
                        lv1.onRefreshComplete();
                    }
                }.execute();
            }
        });
    }
    /**
     * 查询平台余额
     */
    public void getptamount() {
        new Thread(new Runnable() {
            public void run() {
                String result = "";
                try {
                    String data = "{\"user\":\"" + Data.getauth0type() + "\",\"currencyId\":1}";
                    data = URLEncoder.encode(data, "UTF-8");
                    String urlName = Data.gethttp1()+"/hierstarQrCode/pawn/getBalance?jsonParams="+data;
                    LogCook.d("发送参数", urlName);
                    URL U = new URL(urlName);
                    URLConnection connection = U.openConnection();
                    connection.connect();
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line;
                    while ((line = in.readLine()) != null) {
                        result += line;
                    }
                    LogCook.d("查询平台余额返回数据", result);
                    in.close();
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("status_Sucess").equals("true")) {
                        String message= jsonObject.getString("status_Message");
                        if(message!="") {
                            LogCook.d("平台余额", message);
                            Thread.sleep(2000);
                            ptamount.setText(message);
                        }
                        WeiboDialogUtils.closeDialog(Data.getdialog());
                    } else if (jsonObject.getString("status_Sucess").equals("false")) {//返回错误信息
                        Looper.prepare();
                        Toast.makeText(Data.getcontext(), Data.getcontext().getResources().getString(R.string.uhttp6) +
                                jsonObject.getString("status_Result") + ":" + jsonObject.getString("status_Message"), Toast.LENGTH_SHORT).show();
                        WeiboDialogUtils.closeDialog(Data.getdialog());
                        Looper.loop();
                    }
                } catch (Exception e) {
                    Looper.prepare();
                    Toast.makeText(Data.getcontext(), Data.getcontext().getResources().getString(R.string.uhttp6), Toast.LENGTH_SHORT).show();
                    WeiboDialogUtils.closeDialog(Data.getdialog());
                    Looper.loop();
                    e.printStackTrace();
                }
            }
        }).start(); // 开启线程
    }
    public void send1(){
        new Thread(new Runnable() {
            public void run() {
                send();
            }
        }).start();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                try {
                    if (result != null&&result!="") {
                        JSONObject jsonObject =new JSONObject(result);
                        String result1 = jsonObject.getString("data");
                        List<Object> list1 = JSON.parseArray(result1);
                        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
                        for (Object object : list1) {
                            Map<String, Object> ageMap = new HashMap<String, Object>();
                            Map<String, Object> ret = (Map<String, Object>) object;//取出list里面的值转为map
                            list.add(ret);
                        }
                        Map<String, Object> m = new HashMap<String, Object>();
                        for (int q = 0; q < list.size(); q++) {
                            LogCook.d("遍历返回信息", valueOf(list.get(q)));
                            m = (Map<String, Object>) list.get(q); //通过索引方式进行转换类型的强转
                            Set keySet = m.keySet(); // 读取map中的文件
                            Iterator<String> it = keySet.iterator();
                            while (it.hasNext()) {  //挨个遍历
                                Object k = it.next(); // key
                                if (valueOf(k).equals("name")) {
                                    Object v = m.get(k);
                                    name.add(valueOf(v));
                                } else if (valueOf(k).equals("redeemPrice")) {
                                    Object v = m.get(k);
                                    redeemPrice.add(valueOf(v));
                                } else if (valueOf(k).equals("shopname")) {
                                    Object v = m.get(k);
                                    shopname.add(valueOf(v));
                                } else if (valueOf(k).equals("id")) {
                                    Object v = m.get(k);
                                    objectId.add(valueOf(v));
                                } else if (valueOf(k).equals("isredeem")) {
                                    Object v = m.get(k);
                                    isredeem.add((Integer) v);
                                }
                            }
                        }
                        LogCook.d("name", valueOf(name));
                        LogCook.d("redeemPrice", valueOf(redeemPrice));
                        LogCook.d("shopname", valueOf(shopname));
                        LogCook.d("objectId", valueOf(objectId));
                        LogCook.d("isredeem", valueOf(isredeem));
                        if(name.size()==0){
                            WeiboDialogUtils.closeDialog(mWeiboDialog);
                        }else {
                            adapter = new MyAdapter(CdActivity.this, name);
                            lv1.setAdapter(adapter);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    };

    public void send() {//我出当的商品（用于查看我出当的商品，赎回）
        InputStream is = null;
        try {
            String data = "{\"user\":\"" + Data.getauth0type() + "\"}";
            data = URLEncoder.encode(data, "UTF-8");
            HttpURLConnection conn = null;//声明连接对象
            String urlStr = Data.gethttp1()+"/hierstarQrCode/pawnshop/userobject?jsonParams="+data;
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
                LogCook.d("出当数据",result);
                is.close();
                Message m = handler.obtainMessage(); // 获取一个Message
                handler.sendMessage(m);
            }
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),Data.getcontext().getResources().getString(R.string.invest1) , Toast.LENGTH_SHORT).show();
            WeiboDialogUtils.closeDialog(mWeiboDialog);
        }
    }

    class MyAdapter extends BaseAdapter {
        private Context mContext;
        private List<String> mList = new ArrayList<>();
        public MyAdapter(Context context, List<String> list) {
            mContext = context;
            mList = list;
        }
        @Override
        public int getCount() {
            return mList.size();
        }
        @Override
        public Object getItem(int i) {
            return mList.get(i);
        }
        @Override
        public long getItemId(int i) {
            return i;
        }
        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder = null;
            if (view == null) {
                viewHolder = new ViewHolder();
                view = LayoutInflater.from(mContext).inflate(R.layout.item_touzi, null);
                viewHolder.name = (TextView) view.findViewById(R.id.name);
                viewHolder.dividend = (TextView) view.findViewById(R.id.dividend);
                viewHolder.dividend.setText(R.string.tz5);
                viewHolder.dividend.setBackgroundResource(R.drawable.lanyabg);
                viewHolder.img = (ImageView) view.findViewById(R.id.image);
                viewHolder.fxtoken = (TextView) view.findViewById(R.id.fxtoken);
                viewHolder.fxtoken.setText("0");
                viewHolder.tztoken = (TextView) view.findViewById(R.id.tztoken);
                viewHolder.tztoken1 = (TextView) view.findViewById(R.id.tztoken1);
                viewHolder.tztoken1.setText(R.string.tz8);
                viewHolder.cdname = (TextView) view.findViewById(R.id.cdname);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            if(viewHolder!=null) {
                if (mList.get(i).contains("花瓶")) {
                    viewHolder.img.setImageResource(R.drawable.huaping);
                    viewHolder.name.setText(name.get(0));
                    viewHolder.tztoken.setText(redeemPrice.get(0));
                    viewHolder.cdname.setText(shopname.get(0));
                }else if (mList.get(i).contains("字画")) {
                    viewHolder.img.setImageResource(R.drawable.zihua);
                    if(mList.size()==1){
                        viewHolder.name.setText(name.get(0));
                        viewHolder.tztoken.setText(redeemPrice.get(0));
                        viewHolder.cdname.setText(shopname.get(0));
                    }else {
                        viewHolder.name.setText(name.get(1));
                        viewHolder.tztoken.setText(redeemPrice.get(1));
                        viewHolder.cdname.setText(shopname.get(1));
                    }
                }
                if(isredeem.get(i)==1){
                    viewHolder.dividend.setText(R.string.tz5);
                    viewHolder.dividend.setBackgroundResource(R.color.black);
                }
                ViewHolder finalViewHolder = viewHolder;
                viewHolder.dividend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(finalViewHolder.dividend.getText().toString().equals(R.string.tz6)) {
                            Data.setobjectId(objectId.get(i));
                            AlertDialog.Builder builder = new AlertDialog.Builder(CdActivity.this);
                            builder.setTitle(R.string.tz7).setNegativeButton(R.string.f513, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            builder.setPositiveButton(R.string.f514, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    final EditText inputServer = new EditText(CdActivity.this);
                                    inputServer.setTransformationMethod(PasswordTransformationMethod.getInstance());
                                    inputServer.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
                                    AlertDialog.Builder builder = new AlertDialog.Builder(CdActivity.this);
                                    builder.setTitle(Data.getcontext().getResources().getString(R.string.recover5)).setView(inputServer).setNegativeButton(R.string.f513, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    });
                                    builder.setPositiveButton(R.string.f514, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (inputServer.getText().toString().equals("")) {
                                                Toast.makeText(Data.getcontext(), Data.getcontext().getResources().getString(R.string.m2), Toast.LENGTH_SHORT).show();
                                            } else {
                                                String data1 = "32745565";
                                                String data2 = "0000000000000000000000000000000000000000000000000000000000000001";
                                                String data3 = Integer.toHexString(Integer.parseInt(finalViewHolder.tztoken.getText().toString()));
                                                for (int length = data3.length(); length < 64; length++) {
                                                    data3 = "0" + data3;
                                                }
                                                String data4 = "0000000000000000000000000000000000000000000000000000000000000001";
                                                String data5 = "0000000000000000000000000000000000000000000000000000000000000001";
                                                String data6 = "0000000000000000000000000000000000000000000000000000000000000001";
                                                String data7 = "0000000000000000000000000000000000000000000000000000000000000001";
                                                String data8 = "0000000000000000000000000000000000000000000000000000000000000001";
                                                String data9 = "0000000000000000000000000000000000000000000000000000000000000001";
                                                String data10 = "0000000000000000000000000000000000000000000000000000000000000001";
                                                String data11 = "0000000000000000000000000000000000000000000000000000000000000001";
                                                Data.sethiersign(data1 + data3 + data2 + data4 + data5 + data6 + data7 + data8 + data9 + data10 + data11);
                                                LogCook.d("data", Data.gethiersign());
                                                Data.setlimit(inputServer.getText().toString());
                                                Data.setbizhong("Pawn");//币种分类
                                                Data.setsign("end0");//结束指令
                                                Data.setsaoma("yes");//是否进行签名
                                                String a1 = "55aa260110000002000035aa55";//结束签名
                                                sendble(a1, Data.getmService());
                                            }
                                        }
                                    });
                                    builder.show();
                                }
                            });
                            builder.show();
                        }
                    }
                });
            }
            return view;
        }
        class ViewHolder {
            ImageView img;
            TextView name,dividend,fxtoken,tztoken1,tztoken,cdname;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {//如果返回键按下
            Data.setresult("2");
            Intent intent = new Intent(this, IndexActivity.class);
            startActivity(intent);
        }
        return false;
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fhgx) {
            Data.setresult("2");
            Intent intent = new Intent(this, IndexActivity.class);
            startActivity(intent);
        }
        if (v.getId() == R.id.tztype) {

        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocalManageUtil.setLocal(newBase));
    }
}
