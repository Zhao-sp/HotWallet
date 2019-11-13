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

public class TztypeActivity extends Activity implements View.OnClickListener{
    private Handler handler;
    private TextView fhgx,tztype;
    private String result="";
    private Dialog mWeiboDialog;
    private MyListView lv1;
    private MyAdapter adapter;
    List<String> name = new ArrayList<String>();
    List<String> balance = new ArrayList<String>();
    List<String> dividend = new ArrayList<String>();
    List<String> isredeem = new ArrayList<String>();
    List<String> objectId = new ArrayList<String>();
    List<String> shopname = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invest);
        Data.settype("tztypeactivity");
        Data.setcontext(TztypeActivity.this);
        lv1=(MyListView)findViewById(R.id.list_zx);
        tztype=(TextView)findViewById(R.id.tztype);tztype.setVisibility(View.GONE);
        fhgx=(TextView) findViewById(R.id.fhgx);fhgx.setOnClickListener(this);
        mWeiboDialog = WeiboDialogUtils.createLoadingDialog(TztypeActivity.this, this.getResources().getString(R.string.f4));
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
                        name.clear();
                        balance.clear();
                        isredeem.clear();
                        objectId.clear();
                        shopname.clear();
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
            }
        }).start();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                try {
                    if (result != null&&result!="") {
                        JSONObject jsonObject =new JSONObject(result);
                        LogCook.d("获取投资列表返回数据",String.valueOf(result));
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
                                } else if (valueOf(k).equals("investAmount")) {
                                    Object v = m.get(k);
                                    balance.add(valueOf(v));
                                } else if (valueOf(k).equals("isredeem")) {
                                    Object v = m.get(k);
                                    isredeem.add(valueOf(v));
                                } else if (valueOf(k).equals("objectId")) {
                                    Object v = m.get(k);
                                    objectId.add(valueOf(v));
                                } else if (valueOf(k).equals("shopname")) {
                                    Object v = m.get(k);
                                    shopname.add(valueOf(v));
                                }else if (valueOf(k).equals("dividend")) {
                                    Object v = m.get(k);
                                    dividend.add(valueOf(v));
                                }
                            }
                        }
                        LogCook.d("name", valueOf(name));
                        LogCook.d("balance", valueOf(balance));
                        LogCook.d("isredeem", valueOf(isredeem));
                        LogCook.d("objectId", valueOf(objectId));
                        LogCook.d("shopname", valueOf(shopname));
                        LogCook.d("dividend", valueOf(dividend));
                        if(name.size()==0){
                            WeiboDialogUtils.closeDialog(mWeiboDialog);
                        }else {
                            adapter = new MyAdapter(TztypeActivity.this, name);
                            lv1.setAdapter(adapter);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    };

    /**
     * 用户投资列表（用户投资过的商品）
     */
    public void send() {
        new Thread(new Runnable() {
            public void run() {
                result = "";
                try {
                    String data = "{\"user\":\"" + Data.getauth0type() + "\"}";
                    data = URLEncoder.encode(data, "UTF-8");
                    String urlName = Data.gethttp1()+"/hierstarQrCode/pawnshop/userinvest?jsonParams="+data;
                    LogCook.d("发送参数", urlName);
                    URL U = new URL(urlName);
                    URLConnection connection = U.openConnection();
                    connection.connect();
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line;
                    while ((line = in.readLine()) != null) {
                        result += line;
                    }
                    in.close();
                    Message m = handler.obtainMessage(); // 获取一个Message
                    handler.sendMessage(m); // 发送消息
                } catch (Exception e) {
                    Looper.prepare();
                    e.printStackTrace();
                    WeiboDialogUtils.closeDialog(Data.getdialog());
                    Toast.makeText(getApplicationContext(),Data.getcontext().getResources().getString(R.string.tz9), Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
            }
        }).start(); // 开启线程
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
                viewHolder.img = (ImageView) view.findViewById(R.id.image);
                viewHolder.fxtoken = (TextView) view.findViewById(R.id.fxtoken);
                viewHolder.fxtoken1 = (TextView) view.findViewById(R.id.fxtoken1);
                viewHolder.tztoken = (TextView) view.findViewById(R.id.tztoken);
                viewHolder.tztoken1 = (TextView) view.findViewById(R.id.tztoken1);
                viewHolder.cdname = (TextView) view.findViewById(R.id.cdname);
                viewHolder.tztoken.setVisibility(View.INVISIBLE);
                viewHolder.tztoken1.setVisibility(View.INVISIBLE);
                viewHolder.fxtoken1.setText(R.string.tz10);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            if(viewHolder!=null) {
                if (mList.get(i).contains("花瓶")) {
                    viewHolder.img.setImageResource(R.drawable.huaping);
                }else if (mList.get(i).contains("字画")) {
                    viewHolder.img.setImageResource(R.drawable.zihua);
                }
                viewHolder.name.setText(name.get(i));
                viewHolder.fxtoken.setText(balance.get(i));
                viewHolder.cdname.setText(R.string.tz11);
                if(isredeem.get(i).equals("是")&&dividend.get(i).equals(0)){
                    viewHolder.dividend.setText(R.string.tz12);
                    viewHolder.dividend.setBackgroundResource(R.drawable.lanyabg);
                }else{
                    viewHolder.dividend.setVisibility(View.INVISIBLE);
                    viewHolder.tztoken.setVisibility(View.VISIBLE);
                    viewHolder.tztoken1.setVisibility(View.VISIBLE);
                    viewHolder.tztoken1.setText(R.string.tz13);
                    viewHolder.tztoken.setText(dividend.get(i));
                }
                WeiboDialogUtils.closeDialog(mWeiboDialog);
                ViewHolder finalViewHolder = viewHolder;
                viewHolder.dividend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (finalViewHolder.dividend.getText().toString().equals(R.string.tz12)) {
                            Data.setobjectId(objectId.get(i));
                            AlertDialog.Builder builder = new AlertDialog.Builder(TztypeActivity.this);
                            builder.setTitle(R.string.tz14).setNegativeButton(R.string.f513, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            builder.setPositiveButton(R.string.f514, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    final EditText inputServer = new EditText(TztypeActivity.this);
                                    inputServer.setTransformationMethod(PasswordTransformationMethod.getInstance());
                                    inputServer.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
                                    AlertDialog.Builder builder = new AlertDialog.Builder(TztypeActivity.this);
                                    builder.setTitle(R.string.recover5).setView(inputServer).setNegativeButton(R.string.f513, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    });
                                    builder.setPositiveButton(R.string.f514, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (inputServer.getText().toString().equals("")) {
                                                Toast.makeText(Data.getcontext(), Data.getcontext().getResources().getString(R.string.m2), Toast.LENGTH_SHORT).show();
                                            } else {
                                                String data1 = "10fd6099";
                                                String data2 = "0000000000000000000000000000000000000000000000000000000000000001";
                                                String data3 = Integer.toHexString(Integer.parseInt(finalViewHolder.fxtoken.getText().toString()));
                                                for (int length = data3.length(); length < 64; length++) {
                                                    data3 = "0" + data3;
                                                }
                                                String data4 = "0000000000000000000000000000000000000000000000000000000000000001";
                                                Data.sethiersign(data1 + data3 + data2 + data4);
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
            TextView name,dividend,fxtoken,fxtoken1,tztoken,tztoken1,cdname;
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
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocalManageUtil.setLocal(newBase));
    }
}
