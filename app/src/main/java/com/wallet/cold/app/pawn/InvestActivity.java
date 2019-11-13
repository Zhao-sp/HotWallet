package com.wallet.cold.app.pawn;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
import com.wallet.cold.utils.WeiboDialogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.lang.String.valueOf;

public class InvestActivity extends AppCompatActivity implements View.OnClickListener{
    private Handler handler;
    private TextView fhgx,tztype;
    private String result="";
    private String shopname1="";
    private Dialog mWeiboDialog;
    private MyListView lv1;
    private MyAdapter adapter;
    Bundle bundle = new Bundle();
    List<String> name = new ArrayList<String>();
    List<String> balance = new ArrayList<String>();
    List<String> tokenamount = new ArrayList<String>();
    List<String> shopname = new ArrayList<String>();
    List<String> objectId = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invest);
        lv1=(MyListView)findViewById(R.id.list_zx);
        tztype=(TextView)findViewById(R.id.tztype);tztype.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        fhgx=(TextView) findViewById(R.id.fhgx);fhgx.setOnClickListener(this);tztype.setOnClickListener(this);
        mWeiboDialog = WeiboDialogUtils.createLoadingDialog(InvestActivity.this, this.getResources().getString(R.string.f4));
        Data.setdialog(mWeiboDialog);
        if(Data.gettype().equals("tzactivity")){
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if(Utils.isNetworkConnected(Data.getcontext())) {
            send1();
        }else{
            Toast.makeText(Data.getcontext(), Data.getcontext().getResources().getString(R.string.cd2), Toast.LENGTH_SHORT).show();
            WeiboDialogUtils.closeDialog(mWeiboDialog);
        }
        Data.settype("investactivity");
        Data.setcontext(InvestActivity.this);
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
                        balance.clear();
                        tokenamount.clear();
                        shopname.clear();
                        objectId.clear();
                        send1();
                        lv1.onRefreshComplete();
                    }
                }.execute();
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
                                } else if (valueOf(k).equals("balance")) {
                                    Object v = m.get(k);
                                    balance.add(valueOf(v));
                                } else if (valueOf(k).equals("tokenamount")) {
                                    Object v = m.get(k);
                                    tokenamount.add(valueOf(v));
                                } else if (valueOf(k).equals("shopname")) {
                                    Object v = m.get(k);
                                    shopname.add(valueOf(v));
                                } else if (valueOf(k).equals("id")) {
                                    Object v = m.get(k);
                                    objectId.add(valueOf(v));
                                }
                            }
                        }
                        LogCook.d("name", valueOf(name));
                        LogCook.d("balance", valueOf(balance));
                        LogCook.d("tokenamount", valueOf(tokenamount));
                        LogCook.d("shopname", valueOf(shopname));
                        LogCook.d("objectId", valueOf(objectId));
                        if(name.size()==0){
                            WeiboDialogUtils.closeDialog(mWeiboDialog);
                        }else {
                            adapter = new MyAdapter(InvestActivity.this, name);
                            lv1.setAdapter(adapter);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    };

    public void send() {
        HttpURLConnection conn = null;//声明连接对象
        String urlStr = Data.gethttp1()+"/hierstarQrCode/pawn/objectlist";
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
                LogCook.d("投资数据",result);
                is.close();
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
                viewHolder.dividend.setVisibility(View.GONE);
                viewHolder.img = (ImageView) view.findViewById(R.id.image);
                viewHolder.fxtoken = (TextView) view.findViewById(R.id.fxtoken);
                viewHolder.tztoken = (TextView) view.findViewById(R.id.tztoken);
                viewHolder.cdname = (TextView) view.findViewById(R.id.cdname);
                viewHolder.tz = (RelativeLayout) view.findViewById(R.id.tz);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            if(viewHolder!=null) {
                if (mList.get(i).contains("花瓶")) {
                    viewHolder.img.setImageResource(R.drawable.huaping);
                    viewHolder.name.setText(name.get(0));
                    viewHolder.fxtoken.setText(tokenamount.get(0));
                    viewHolder.tztoken.setText(balance.get(0));
                    viewHolder.cdname.setText(shopname.get(0));
                }else if (mList.get(i).contains("字画")) {
                    viewHolder.img.setImageResource(R.drawable.zihua);
                    if(mList.size()==1){
                        viewHolder.name.setText(name.get(0));
                        viewHolder.fxtoken.setText(tokenamount.get(0));
                        viewHolder.tztoken.setText(balance.get(0));
                        viewHolder.cdname.setText(shopname.get(0));
                    }else {
                        viewHolder.name.setText(name.get(1));
                        viewHolder.fxtoken.setText(tokenamount.get(1));
                        viewHolder.tztoken.setText(balance.get(1));
                        viewHolder.cdname.setText(shopname.get(1));
                    }
                }
                shopname1=shopname.get(0);
                WeiboDialogUtils.closeDialog(mWeiboDialog);
                ViewHolder finalViewHolder = viewHolder;
                viewHolder.tz.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mList.get(i).contains("花瓶")) {
                            bundle.putString("objectId", objectId.get(0));
                        }else if (mList.get(i).contains("字画")){
                            if(mList.size()==1){
                                bundle.putString("objectId", objectId.get(0));
                            }else {
                                bundle.putString("objectId", objectId.get(1));
                            }
                        }
                        bundle.putString("name", finalViewHolder.name.getText().toString());
                        bundle.putString("tokenamount", finalViewHolder.fxtoken.getText().toString());
                        bundle.putString("balance", finalViewHolder.tztoken.getText().toString());
                        bundle.putString("shopname", finalViewHolder.cdname.getText().toString());
                        startActivity(new Intent(InvestActivity.this, TzActivity.class).putExtras(bundle));
                    }
                });
            }
            return view;
        }
        class ViewHolder {
            ImageView img;
            TextView name,dividend,fxtoken,tztoken,cdname;
            RelativeLayout tz;
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
            Intent intent = new Intent(this, TztypeActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocalManageUtil.setLocal(newBase));
    }
}
