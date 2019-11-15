package com.wallet.cold.app.index;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
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

import com.wallet.R;
import com.wallet.cold.app.util.Fragment5;
import com.wallet.cold.utils.CaptureActivity;
import com.wallet.cold.utils.Data;
import com.wallet.cold.utils.LocalManageUtil;
import com.wallet.cold.utils.MyListView;
import com.wallet.cold.utils.PopWinShare;
import com.wallet.cold.utils.Utils;
import com.wallet.cold.utils.WeiboDialogUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Fragment1 extends AppCompatActivity implements View.OnClickListener {
    private TextView balance;
    private Dialog mWeiboDialog;
    private PopWinShare popWinShare;
    private ImageView kz,sz;
    private ImageView zhuanzhang,shoukuan;
    private MyListView lv1;
    private MyAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment1);
        Data.setsaoma("no");
        Data.setiseth("yes");
        lv1=(MyListView)findViewById(R.id.list_yue);
        balance =(TextView)findViewById(R.id.balance);Data.setcountamount(balance);
        kz = (ImageView)findViewById(R.id.kz);
        sz = (ImageView)findViewById(R.id.sz);
        zhuanzhang = (ImageView)findViewById(R.id.zhuanzhang);
        shoukuan = (ImageView)findViewById(R.id.shoukuan);
        kz.setOnClickListener(this);
        sz.setOnClickListener(this);
        int typeheight= Utils.getstatus(Fragment1.this);
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) kz.getLayoutParams();
        lp.topMargin = typeheight+20;
        kz.setLayoutParams(lp);
        RelativeLayout.LayoutParams lp1 = (RelativeLayout.LayoutParams) sz.getLayoutParams();
        lp1.topMargin = typeheight+20;
        sz.setLayoutParams(lp1);
        zhuanzhang.setOnClickListener(this);
        shoukuan.setOnClickListener(this);
        adapter = new MyAdapter(Fragment1.this, Data.getbledata());
        lv1.setAdapter(adapter);
        Data.settype("fragment1");
        Data.setcontext(Fragment1.this);
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
                        if(Utils.isNetworkConnected(Data.getcontext())) {
                            Dialog mWeiboDialog = WeiboDialogUtils.createLoadingDialog(Data.getcontext(), Data.getcontext().getResources().getString(R.string.type5));
                            Data.setdialog(mWeiboDialog);
                            adapter.notifyDataSetChanged();
                            lv1.onRefreshComplete();
                            Handler handler=new Handler();
                            Data.sethandler(handler);
                            new Utils().balancebtc();
                        }else{
                            Toast.makeText(Data.getcontext(), Data.getcontext().getResources().getString(R.string.cd2), Toast.LENGTH_SHORT).show();
                            WeiboDialogUtils.closeDialog(Data.getdialog());
                        }
                    }
                }.execute();
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
                    viewHolder.ethyue.setTextColor(0xffffffff);Data.setethtext(viewHolder.ethyue);
                    viewHolder.ethxianjin.setTextColor(0x66ffffff);Data.setethrmbtext(viewHolder.ethxianjin);
                } else if (mList.get(i).equals("BTC")) {
                    viewHolder.btcxianjin = (TextView) view.findViewById(R.id.xianjin);
                    viewHolder.btcyue = (TextView) view.findViewById(R.id.yue);
                    viewHolder.btcyue.setTextColor(0xffffffff);
                    Data.setbtctext(viewHolder.btcyue);
                    viewHolder.btcxianjin.setTextColor(0x66ffffff);
                    Data.setbtcrmbtext(viewHolder.btcxianjin);
                }else if (mList.get(i).equals("XRP")) {
                    viewHolder.xrpxianjin = (TextView) view.findViewById(R.id.xianjin);
                    viewHolder.xrpyue = (TextView) view.findViewById(R.id.yue);
                    viewHolder.xrpyue.setTextColor(0xffffffff);
                    Data.setxrptext(viewHolder.xrpyue);
                    viewHolder.xrpxianjin.setTextColor(0x66ffffff);
                    Data.setxrprmbtext(viewHolder.xrpxianjin);
                }else if (mList.get(i).equals("AED")) {
                    viewHolder.aedxianjin = (TextView) view.findViewById(R.id.xianjin);
                    viewHolder.aedyue = (TextView) view.findViewById(R.id.yue);
                    viewHolder.aedyue.setTextColor(0xffffffff);
                    Data.setaedtext(viewHolder.aedyue);
                    viewHolder.aedxianjin.setTextColor(0x66ffffff);
                    Data.setaedrmbtext(viewHolder.aedxianjin);
                } else {
                    viewHolder.hierxianjin = (TextView) view.findViewById(R.id.xianjin);
                    viewHolder.hieryue = (TextView) view.findViewById(R.id.yue);
                    viewHolder.hieryue.setTextColor(0xffffffff);
                    viewHolder.hierxianjin.setTextColor(0x66ffffff);
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
                        viewHolder.ethxianjin.setText("￥"+df.format(d));
                    }
                }
                if (mList.get(i).equals("BTC")) {
                    viewHolder.image.setImageResource(R.drawable.btc1);
                    if (!Data.getbtcbalance().equals("")) {
                        viewHolder.btcyue.setText(Data.getbtcbalance());
                        BigDecimal b1 = new BigDecimal(Double.parseDouble(Data.getbtcrmbbalance()));
                        BigDecimal b2 = new BigDecimal(Double.parseDouble(Data.getbtcbalance()));
                        Double d=  b1.multiply(b2).doubleValue();
                        viewHolder.btcxianjin.setText("￥"+df.format(d));
                    }
                }
                if (mList.get(i).equals("Hier")) {
                    viewHolder.image.setImageResource(R.drawable.hier);
                    if (!Data.gethieramount().equals("")) {
                        viewHolder.hieryue.setText(Data.gethieramount());
                        viewHolder.hierxianjin.setText("￥"+Data.gethieramount());
                    }
                }
                if (mList.get(i).equals("XRP")) {
                    viewHolder.image.setImageResource(R.drawable.xrp);
                    if (!Data.getxrpamount().equals("")) {
                        viewHolder.xrpyue.setText(Data.getxrpamount());
                        DecimalFormat df1 = new DecimalFormat("0.00");
                        viewHolder.xrpxianjin.setText("￥"+df1.format(Double.parseDouble(Data.getxrpamount())*1.9*7));
                    }
                }
                if (mList.get(i).equals("AED")) {
                    viewHolder.image.setImageResource(R.drawable.xrp);
                    if (!Data.getxrpamount().equals("")) {
                        viewHolder.aedyue.setText(Data.getxrpamount());
                        DecimalFormat df1 = new DecimalFormat("0.00");
                        viewHolder.aedxianjin.setText("￥"+df1.format(Double.parseDouble(Data.getxrpamount())*1.9*7));
                    }
                }
                Data.getcountamount().setText(Data.getamountrmb());
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
                        } else if (msg.equals("XRP")) {
                            msg1 = finalViewHolder.xrpyue.getText().toString();
                            msg2 = finalViewHolder.xrpxianjin.getText().toString();
                        } else if (msg.equals("AED")) {
                            msg1 = finalViewHolder.aedyue.getText().toString();
                            msg2 = finalViewHolder.aedxianjin.getText().toString();
                        } else {
                            msg1 = finalViewHolder.hieryue.getText().toString();
                            msg2 = finalViewHolder.hierxianjin.getText().toString();
                        }
                        Intent intent = new Intent(Fragment1.this, JYXXActivity.class);
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
            TextView mTextView,btcyue,ethyue,hieryue,btcxianjin,ethxianjin,hierxianjin,xrpyue,xrpxianjin,aedyue,aedxianjin;
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
                popWinShare = new PopWinShare(Fragment1.this, paramOnClickListener);
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
            Intent intent0 = new Intent(Fragment1.this, Fragment5.class);
            startActivity(intent0);
        }
        if(v.getId() == R.id.zhuanzhang) {
            Intent intent = new Intent(Fragment1.this, Transfer.class);
            startActivity(intent);
        }
        if(v.getId() == R.id.shoukuan) {
            Intent intent1 = new Intent(Fragment1.this, Receivables.class);
            startActivity(intent1);
        }
    }
    class OnClickLintener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.addbizhong) {
                Intent intent = new Intent(Fragment1.this, AddBiActivity.class);
                startActivity(intent);
            }
            if(v.getId() == R.id.sao) {
                Intent intent1 = new Intent(Fragment1.this, CaptureActivity.class);
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
