package com.wallet.cold.app.index;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wallet.R;
import com.wallet.cold.utils.Data;
import com.wallet.cold.utils.LocalManageUtil;
import com.wallet.cold.utils.LogCook;
import com.wallet.cold.utils.MyListView;
import com.wallet.cold.utils.Utils;
import com.wallet.cold.utils.WeiboDialogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static com.wallet.cold.utils.Utils.getIndex;

public class HQActivity extends Activity implements View.OnClickListener {
    private MyListView lv1;
    private TextView quxiao,t1,ssname,noxx;
    private ImageView ss,ssxiao;
    private Handler handler;
    private String result="";
    private Dialog mWeiboDialog;
    private MyAdapter adapter;
    private List<String> list = new ArrayList<>();
    private List<String> fluctuationlist = new ArrayList<>();
    private List<String> current_pricelist = new ArrayList<>();
    private List<String> list1 = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragmenthq);
        Data.settype("hqactivity");
        Data.setcontext(HQActivity.this);
        ss=(ImageView) findViewById(R.id.ss);
        ss.setOnClickListener(this);
        quxiao=(TextView) findViewById(R.id.quxiao);
        t1=(TextView) findViewById(R.id.t1);
        ssname=(TextView) findViewById(R.id.ssname);
        noxx=(TextView) findViewById(R.id.noxx);
        ssxiao=(ImageView) findViewById(R.id.ssxiao);
        quxiao.setOnClickListener(this);
        ssxiao.setVisibility(View.INVISIBLE);
        t1.setVisibility(View.INVISIBLE);
        ssname.setVisibility(View.INVISIBLE);
        quxiao.setVisibility(View.INVISIBLE);
        noxx.setVisibility(View.INVISIBLE);
        list.add("ETH");list.add("BTC");
        lv1=(MyListView) findViewById(R.id.list);
        mWeiboDialog = WeiboDialogUtils.createLoadingDialog(HQActivity.this, HQActivity.this.getResources().getString(R.string.f4));
        if(Utils.isNetworkConnected(Data.getcontext())) {
            send2();
        }else{
            Toast.makeText(Data.getcontext(), Data.getcontext().getResources().getString(R.string.cd2), Toast.LENGTH_SHORT).show();
            WeiboDialogUtils.closeDialog(mWeiboDialog);
        }
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (result != null) {
                    LogCook.d("行情数据",String.valueOf(result));
                    int index = getIndex(result, 1, "[}]");
                    String btchq = result.substring(0,index+1);
                    LogCook.d("BTC行情数据",btchq);
                    String ethhq = result.substring(index+2,result.length()-1);
                    LogCook.d("ETH行情数据",ethhq);
                    try {
                        JSONObject jsonObject = new JSONObject(ethhq);
                        String fluctuation=jsonObject.getString("degree");
                        String current_price=jsonObject.getString("high");
                        fluctuationlist.add(fluctuation);
                        current_pricelist.add(current_price);
                        JSONObject jsonObject1 = new JSONObject(btchq);
                        String fluctuation1=jsonObject1.getString("degree");
                        String current_price1=jsonObject1.getString("high");
                        fluctuationlist.add(fluctuation1);
                        current_pricelist.add(current_price1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    adapter = new MyAdapter(HQActivity.this,list);
                    lv1.setAdapter(adapter);
                    WeiboDialogUtils.closeDialog(mWeiboDialog);
                }
            }
        };
        ssname.setOnEditorActionListener(new TextView.OnEditorActionListener(){
            @Override
            public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
                if(arg1 == EditorInfo.IME_ACTION_SEARCH) {
                    if(ssname.getText().toString().equals("")){
                        adapter = new MyAdapter(HQActivity.this,list);
                        lv1.setAdapter(adapter);
                        lv1.setVisibility(View.VISIBLE);
                    }else if(list.contains(ssname.getText().toString().toUpperCase())){
                        list1.clear();
                        list1.add(ssname.getText().toString().toUpperCase());
                        adapter = new MyAdapter(HQActivity.this,list1);
                        lv1.setAdapter(adapter);
                        lv1.setVisibility(View.VISIBLE);
                        noxx.setVisibility(View.INVISIBLE);
                        return true;
                    }else{
                        lv1.setVisibility(View.INVISIBLE);
                        noxx.setVisibility(View.VISIBLE);
                    }
                }
                return false;
            }
        });
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
                        mWeiboDialog = WeiboDialogUtils.createLoadingDialog(HQActivity.this, HQActivity.this.getResources().getString(R.string.f4));
                        send2();
                        lv1.onRefreshComplete();
                    }
                }.execute();
            }
        });
    }
    public void send2(){
        new Thread(new Runnable() {
            public void run() {
                send("http://111.225.200.132:8023/cgi-bin/getmarket");
                Message m = handler.obtainMessage(); // 获取一个Message
                handler.sendMessage(m); // 发送消息
            }
        }).start();
    }

    public void send(String urlStr) {
        HttpURLConnection conn = null;//声明连接对象
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
                if(result==null){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(Data.getcontext(), "获取行情失败", Toast.LENGTH_SHORT).show();
                            WeiboDialogUtils.closeDialog(Data.getdialog());
                        }
                    });
                }
                is.close();
            }
            conn.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
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
                view = LayoutInflater.from(mContext).inflate(R.layout.item_hangqing, null);
                viewHolder.name = (TextView) view.findViewById(R.id.name);
                viewHolder.je1 = (TextView) view.findViewById(R.id.je1);
                viewHolder.je2 = (TextView) view.findViewById(R.id.je2);
                viewHolder.hq = (TextView) view.findViewById(R.id.hq);
                viewHolder.image = (ImageView) view.findViewById(R.id.image);
                viewHolder.rrrr = (RelativeLayout) view.findViewById(R.id.rrrr);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            viewHolder.name.setText(mList.get(i));
            if(mList.get(i).equals("ETH")){
                viewHolder.image.setImageResource(R.drawable.eth);
            }else if(mList.get(i).equals("BTC")){
                viewHolder.image.setImageResource(R.drawable.btc);
            }
            DecimalFormat df = new DecimalFormat("0.00");
            Double d1= Double.parseDouble(current_pricelist.get(i));
            viewHolder.je2.setText("￥"+df.format(d1));
            Double d= Double.parseDouble(current_pricelist.get(i)) / 6.896;
            viewHolder.je1.setText("$ "+df.format(d));
            Double d2= Double.parseDouble(fluctuationlist.get(i));
            viewHolder.hq.setText(df.format(d2)+"%");
            if(!fluctuationlist.get(i).substring(0,1).equals("-")){
                viewHolder.hq.setBackgroundColor(0xffc25353);
            }
            viewHolder.rrrr.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                }
            });
            return view;
        }
        class ViewHolder {
            TextView name;
            TextView je1;
            TextView je2;
            TextView hq;
            ImageView image;
            RelativeLayout rrrr;
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.ss) {
            ss.setVisibility(View.INVISIBLE);
            ssxiao.setVisibility(View.VISIBLE);
            t1.setVisibility(View.VISIBLE);
            ssname.setVisibility(View.VISIBLE);
            quxiao.setVisibility(View.VISIBLE);
        }
        if(v.getId() == R.id.quxiao) {
            ss.setVisibility(View.VISIBLE);
            ssxiao.setVisibility(View.INVISIBLE);
            t1.setVisibility(View.INVISIBLE);
            ssname.setVisibility(View.INVISIBLE);
            quxiao.setVisibility(View.INVISIBLE);
        }
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocalManageUtil.setLocal(newBase));
    }
}

