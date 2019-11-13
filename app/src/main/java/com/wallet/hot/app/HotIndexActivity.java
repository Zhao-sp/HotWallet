package com.wallet.hot.app;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.WriterException;
import com.wallet.CreateOrImportActivity;
import com.wallet.R;
import com.wallet.cold.app.index.AddBiActivity;
import com.wallet.cold.app.index.JYXXActivity;
import com.wallet.cold.app.index.Receivables;
import com.wallet.cold.app.util.Fragment5;
import com.wallet.cold.utils.CaptureActivity;
import com.wallet.cold.utils.Data;
import com.wallet.cold.utils.LocalManageUtil;
import com.wallet.cold.utils.MyListView;
import com.wallet.cold.utils.PopWinShare;
import com.wallet.cold.utils.Utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class HotIndexActivity extends Activity implements View.OnClickListener {
    private TextView balance,delete;
    private Dialog mWeiboDialog;
    private PopWinShare popWinShare;
    private ImageView kz,sz;
    private ImageView zhuanzhang,shoukuan;
    private MyListView lv1;
    private MyAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotindex);
        Data.settype("hotindex");
        Data.setcontext(HotIndexActivity.this);
        Data.getdb().execSQL("create table if not exists HotJiaoyiTb (_id integer primary key,blename text not null,name text not null,bizhong text not null,jine integer not null,riqi text not null,type integer not null)");
        Data.getdb().execSQL("insert into HotAddressTb (password,btcaddress,ethaddress,ethprv,btcprv,btcpub) values " +
                "('" + Data.gethotpassword() + "','" + Data.gethotbtcaddress() + "','" + Data.gethotethaddress() + "','" + Data.gethotethprv() + "','" + Data.gethotbtcprv() + "','" + Data.gethotbtcpub() + "')");
        lv1=(MyListView)findViewById(R.id.list_yue);
        balance =(TextView)findViewById(R.id.balance);
        delete =(TextView)findViewById(R.id.delete);
        kz = (ImageView)findViewById(R.id.kz);
        sz = (ImageView)findViewById(R.id.sz);
        zhuanzhang = (ImageView)findViewById(R.id.zhuanzhang);
        shoukuan = (ImageView)findViewById(R.id.shoukuan);
        kz.setOnClickListener(this);
        sz.setOnClickListener(this);
        delete.setOnClickListener(this);
        if (!TextUtils.isEmpty(Data.gethotbtcaddress())) {
            Bitmap codeBitmap = null;
            try {
                codeBitmap = Utils.createCode(Data.gethotbtcaddress());
                Data.setimgCode(codeBitmap);
            } catch (WriterException e) {
                e.printStackTrace();
            }
        }
        if (!TextUtils.isEmpty(Data.gethotethaddress())) {
            Bitmap codeBitmap = null;
            try {
                codeBitmap = Utils.createCode(Data.gethotethaddress());
                Data.setethimgCode(codeBitmap);
            } catch (WriterException e) {
                e.printStackTrace();
            }
        }
        int typeheight=Utils.getstatus(HotIndexActivity.this);
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) kz.getLayoutParams();
        lp.topMargin = typeheight+20;
        kz.setLayoutParams(lp);
        RelativeLayout.LayoutParams lp1 = (RelativeLayout.LayoutParams) sz.getLayoutParams();
        lp1.topMargin = typeheight+20;
        sz.setLayoutParams(lp1);
        zhuanzhang.setOnClickListener(this);
        shoukuan.setOnClickListener(this);
        List<String> list = new ArrayList<>();list.add("ETH");list.add("BTC");
        adapter = new MyAdapter(HotIndexActivity.this, list);
        lv1.setAdapter(adapter);
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
                        lv1.onRefreshComplete();
                    }
                }.execute();
                new Utils().balancebtc();
            }
        });
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
            ViewHolder viewHolder1 = new ViewHolder();
            viewHolder1=null;
            if (view == null) {
                viewHolder = new ViewHolder();
                view = LayoutInflater.from(mContext).inflate(R.layout.item_amount, null);
                viewHolder.mTextView = (TextView) view.findViewById(R.id.name);
                viewHolder.image = (ImageView) view.findViewById(R.id.jimage);
                if (mList.get(i).equals("ETH")) {
                    viewHolder.ethxianjin = (TextView) view.findViewById(R.id.xianjin);
                    viewHolder.ethyue = (TextView) view.findViewById(R.id.yue);
                    viewHolder.ethyue.setTextColor(0xffffffff);
                    viewHolder.ethxianjin.setTextColor(0x66ffffff);
                } else if (mList.get(i).equals("BTC")) {
                    viewHolder.btcxianjin = (TextView) view.findViewById(R.id.xianjin);
                    viewHolder.btcyue = (TextView) view.findViewById(R.id.yue);
                    viewHolder.btcyue.setTextColor(0xffffffff);
                    viewHolder.btcxianjin.setTextColor(0x66ffffff);
                    Data.setiseth("no");
                } else {
                    viewHolder.yue = (TextView) view.findViewById(R.id.yue);
                    viewHolder.yue.setTextColor(0xffffffff);
                    Data.setiseth("no");
                }
                viewHolder.rrrr = view.findViewById(R.id.rrrr);
                view.setTag(viewHolder);
            } else {
                viewHolder1 = (ViewHolder) view.getTag();
            }
            if(viewHolder!=null) {
                viewHolder.mTextView.setText(mList.get(i));
                DecimalFormat df = new DecimalFormat("0.00");
                if (mList.get(i).equals("ETH")) {
                    viewHolder.image.setImageResource(R.drawable.eth);
                    if (!Data.getethbalance().equals("")) {
                        viewHolder.ethyue.setText(Data.getethbalance());
                        BigDecimal b1 = new BigDecimal(Double.parseDouble(Data.getethrmbbalance()));
                        BigDecimal b2 = new BigDecimal(Double.parseDouble(Data.getethbalance()));
                        Double d=  b1.multiply(b2).doubleValue();
                        viewHolder.ethxianjin.setText(df.format(d));
                    }
                }
                if (mList.get(i).equals("BTC")) {
                    viewHolder.image.setImageResource(R.drawable.btc1);
                    if (!Data.getbtcbalance().equals("")) {
                        viewHolder.btcyue.setText(Data.getbtcbalance());
                        BigDecimal b1 = new BigDecimal(Double.parseDouble(Data.getbtcrmbbalance()));
                        BigDecimal b2 = new BigDecimal(Double.parseDouble(Data.getbtcbalance()));
                        Double d=  b1.multiply(b2).doubleValue();
                        viewHolder.btcxianjin.setText(df.format(d));
                    }
                }
                if (mList.get(i).equals("EOS")) {
                    viewHolder.image.setImageResource(R.drawable.eos);
                }
                if (mList.get(i).equals("DASH")) {
                    viewHolder.image.setImageResource(R.drawable.dash);
                }
                if (mList.get(i).equals("LBTC")) {
                    viewHolder.image.setImageResource(R.drawable.lbtc);
                }
                ViewHolder finalViewHolder = viewHolder;
                viewHolder.rrrr.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String msg = finalViewHolder.mTextView.getText().toString();
                        String msg1 = "";
                        String msg2 = "";
                        if (msg.equals("ETH")) {
                            msg1 = finalViewHolder.ethyue.getText().toString();
                            msg2 = finalViewHolder.ethxianjin.getText().toString();
                        } else if (msg.equals("BTC")) {
                            msg1 = finalViewHolder.btcyue.getText().toString();
                            msg2 = finalViewHolder.btcxianjin.getText().toString();
                        } else {
                            msg1 = finalViewHolder.yue.getText().toString();
                        }
                        Intent intent = new Intent(HotIndexActivity.this, JYXXActivity.class);
                        intent.putExtra("amsg", msg);
                        intent.putExtra("amsg1", msg1);
                        intent.putExtra("amsg2", msg2);
                        startActivity(intent);
                    }
                });
            }
            return view;
        }
        class ViewHolder {
            TextView mTextView;
            TextView btcyue;
            TextView ethyue;
            TextView yue;
            TextView btcxianjin;
            TextView ethxianjin;
            ImageView image;
            RelativeLayout rrrr;
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.kz) {
            if (popWinShare == null) {
                //自定义的单击事件
                OnClickLintener paramOnClickListener = new OnClickLintener();
                popWinShare = new PopWinShare(HotIndexActivity.this, paramOnClickListener);
                popWinShare.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
                popWinShare.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
                //监听窗口的焦点事件，点击窗口外面则取消显示
                popWinShare.getContentView().setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (!hasFocus) {
                            popWinShare.dismiss();
                        }
                    }
                });
            }
            //设置默认获取焦点
            popWinShare.setFocusable(true);
            //以某个控件的x和y的偏移量位置开始显示窗口
            popWinShare.showAsDropDown(kz, 0, 0);
            //如果窗口存在，则更新
            popWinShare.update();
        }
        if(v.getId() == R.id.sz) {
            Intent intent0 = new Intent(HotIndexActivity.this, Fragment5.class);
            startActivity(intent0);
        }
        if(v.getId() == R.id.zhuanzhang) {
            Intent intent = new Intent(HotIndexActivity.this, HotTransfer.class);
            startActivity(intent);
        }
        if(v.getId() == R.id.shoukuan) {
            Intent intent = new Intent(HotIndexActivity.this, Receivables.class);
            startActivity(intent);
        }
        if(v.getId() == R.id.delete) {
            Data.getdb().execSQL("DELETE FROM HotAddressTb");
            Toast.makeText(Data.getcontext(), "删除成功", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(HotIndexActivity.this, CreateOrImportActivity.class);
            startActivity(intent);
        }
    }
    class OnClickLintener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.addbizhong) {
                Intent intent = new Intent(HotIndexActivity.this, AddBiActivity.class);
                startActivity(intent);
            }
            if(v.getId() == R.id.sao) {
                Intent intent1 = new Intent(HotIndexActivity.this,CaptureActivity.class);
                startActivity(intent1);
            }
        }
    }
    @Override
    public boolean onKeyDown(int keyCode,KeyEvent event){//禁止返回键
        if(keyCode== KeyEvent.KEYCODE_BACK)
            return true;//不执行父类点击事件
        return super.onKeyDown(keyCode, event);//继续执行父类其他点击事件
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocalManageUtil.setLocal(newBase));
    }
}
