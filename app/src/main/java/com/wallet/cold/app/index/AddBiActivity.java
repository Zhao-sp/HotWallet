package com.wallet.cold.app.index;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.wallet.R;
import com.wallet.SharedPrefsStrListUtil;
import com.wallet.cold.app.main.IndexActivity;
import com.wallet.cold.utils.Data;
import com.wallet.cold.utils.LocalManageUtil;
import com.wallet.cold.utils.Utils;
import com.wallet.cold.utils.WeiboDialogUtils;
import com.wallet.hot.utils.HotWalletUtils;

import java.util.ArrayList;
import java.util.List;

public class AddBiActivity extends AppCompatActivity implements OnClickListener {
    private TextView addbi,name,quxiao,t1,ssname,noxx;
    private ListView lv1;
    private ImageView fanhuiadd,ss,ssxiao;
    private MyAdapter adapter;
    private List<String> list = new ArrayList<>();
    private List<String> list1 = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addbi);
        addbi=(TextView) findViewById(R.id.fhaddbi);
        name=(TextView) findViewById(R.id.name);
        quxiao=(TextView) findViewById(R.id.quxiao);
        t1=(TextView) findViewById(R.id.t1);
        ssname=(TextView) findViewById(R.id.ssname);
        noxx=(TextView) findViewById(R.id.noxx);
        ss=(ImageView) findViewById(R.id.ss);
        lv1=(ListView) findViewById(R.id.list);
        fanhuiadd=(ImageView) findViewById(R.id.fanhuiadd);
        ssxiao=(ImageView) findViewById(R.id.ssxiao);
        addbi.setOnClickListener(this);
        ss.setOnClickListener(this);
        fanhuiadd.setOnClickListener(this);
        quxiao.setOnClickListener(this);
        ssxiao.setVisibility(View.INVISIBLE);
        t1.setVisibility(View.INVISIBLE);
        ssname.setVisibility(View.INVISIBLE);
        quxiao.setVisibility(View.INVISIBLE);
        noxx.setVisibility(View.INVISIBLE);
        list.add("ETH");list.add("BTC");list.add("XRP");list.add("AED");
        adapter = new MyAdapter(AddBiActivity.this,list);
        lv1.setAdapter(adapter);
        Data.settype("addbiactivity");
        Data.setcontext(AddBiActivity.this);
        ssname.setOnEditorActionListener(new TextView.OnEditorActionListener(){
        @Override
            public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
                if(arg1 == EditorInfo.IME_ACTION_SEARCH) {
                    if(list.contains(ssname.getText().toString().toUpperCase())){
                        list1.clear();
                        list1.add(ssname.getText().toString().toUpperCase());
                        adapter = new MyAdapter(AddBiActivity.this,list1);
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
        if(v.getId() == R.id.fhaddbi) {
            startActivity(new Intent(this, IndexActivity.class));
        }
        if(v.getId() == R.id.fanhuiadd) {
            startActivity(new Intent(this, IndexActivity.class));
        }
        if(v.getId() == R.id.ss) {
            ss.setVisibility(View.INVISIBLE);
            name.setVisibility(View.INVISIBLE);
            fanhuiadd.setVisibility(View.INVISIBLE);
            ssxiao.setVisibility(View.VISIBLE);
            t1.setVisibility(View.VISIBLE);
            ssname.setVisibility(View.VISIBLE);
            quxiao.setVisibility(View.VISIBLE);
        }
        if(v.getId() == R.id.quxiao) {
            ss.setVisibility(View.VISIBLE);
            name.setVisibility(View.VISIBLE);
            fanhuiadd.setVisibility(View.VISIBLE);
            ssxiao.setVisibility(View.INVISIBLE);
            t1.setVisibility(View.INVISIBLE);
            ssname.setVisibility(View.INVISIBLE);
            quxiao.setVisibility(View.INVISIBLE);
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
                view = LayoutInflater.from(mContext).inflate(R.layout.item_bizhong, null);
                viewHolder.name = (TextView) view.findViewById(R.id.name);
                viewHolder.image = (ImageView) view.findViewById(R.id.image);
                viewHolder.type = (ImageView) view.findViewById(R.id.type);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            viewHolder.name.setText(mList.get(i));
            if (mList.get(i).equals("ETH")) {
                viewHolder.image.setImageResource(R.drawable.eth);
                if(Data.getbledata().contains("ETH")) {
                    viewHolder.type.setImageResource(R.drawable.yixuan);
                }else{
                    viewHolder.type.setImageResource(R.drawable.weixian);
                }
            }
            if (mList.get(i).equals("XRP")) {
                viewHolder.image.setImageResource(R.drawable.xrp);
                if(Data.getbledata().contains("XRP")) {
                    viewHolder.type.setImageResource(R.drawable.yixuan);
                }else{
                    viewHolder.type.setImageResource(R.drawable.weixian);
                }
            }
            if (mList.get(i).equals("AED")) {
                viewHolder.image.setImageResource(R.drawable.aed);
                if(Data.getbledata().contains("AED")) {
                    viewHolder.type.setImageResource(R.drawable.yixuan);
                }else{
                    viewHolder.type.setImageResource(R.drawable.weixian);
                }
            }
            if (mList.get(i).equals("BTC")) {
                viewHolder.image.setImageResource(R.drawable.btc1);
                if(Data.getbledata().contains("BTC")) {
                    viewHolder.type.setImageResource(R.drawable.yixuan);
                }else{
                    viewHolder.type.setImageResource(R.drawable.weixian);
                }
            }
            if (mList.get(i).equals("EOS-EOS")) {
                viewHolder.image.setImageResource(R.drawable.eos);
                if(Data.getbledata().contains("EOS")){
                    viewHolder.type.setImageResource(R.drawable.yixuan);
                }else{
                    viewHolder.type.setImageResource(R.drawable.weixian);
                }
            }
            if (mList.get(i).equals("DASH")) {
                viewHolder.image.setImageResource(R.drawable.dash);
                if(Data.getbledata().contains("DASH")){
                    viewHolder.type.setImageResource(R.drawable.yixuan);
                }else{
                    viewHolder.type.setImageResource(R.drawable.weixian);
                }
            }
            if (mList.get(i).equals("LBTC")) {
                viewHolder.image.setImageResource(R.drawable.lbtc);
                if(Data.getbledata().contains("LBTC")){
                    viewHolder.type.setImageResource(R.drawable.yixuan);
                }else{
                    viewHolder.type.setImageResource(R.drawable.weixian);
                }
            }
            ViewHolder finalViewHolder = viewHolder;
            viewHolder.type.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(Data.getbledata().contains(finalViewHolder.name.getText().toString())){
                        //Toast.makeText(Data.getcontext(), Data.getcontext().getResources().getString(R.string.add5), Toast.LENGTH_SHORT).show();
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(AddBiActivity.this);
                        builder1.setCancelable(false)//设置点击对话框外部区域不关闭对话框
                                .setTitle("币种已添加，是否删除？")
                                .setNegativeButton(R.string.f513, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                                .setPositiveButton(R.string.f514, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        if(Data.getapptype().equals("cold")) {
                                            SharedPrefsStrListUtil.removeStrListItem(AddBiActivity.this, "coldcurrency" + Data.getDeviceaddress(), finalViewHolder.name.getText().toString());
                                        }else{
                                            SharedPrefsStrListUtil.removeStrListItem(AddBiActivity.this, "hotcurrency", finalViewHolder.name.getText().toString());
                                        }
                                        Data.getbledata().remove(finalViewHolder.name.getText().toString());
                                        finalViewHolder.type.setImageResource(R.drawable.weixian);
                                        Toast.makeText(Data.getcontext(), "删除成功", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .show();
                    }else {
                        if(finalViewHolder.name.getText().toString().equals("AED")&&!Data.getbledata().contains("XRP")){
                            Toast.makeText(Data.getcontext(), "请先添加瑞波币", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Dialog mWeiboDialog = WeiboDialogUtils.createLoadingDialog(AddBiActivity.this, Data.getcontext().getResources().getString(R.string.type4));
                        Data.setdialog(mWeiboDialog);
                        finalViewHolder.type.setImageResource(R.drawable.yixuan);
                        Data.getbledata().add(finalViewHolder.name.getText().toString());
                        if(Data.getapptype().equals("cold")) {
                            SharedPrefsStrListUtil.putStrListValue(getApplicationContext(), "coldcurrency"+Data.getDeviceaddress(), Data.getbledata());
                            Data.setbletype("address");
                            if(finalViewHolder.name.getText().toString().equals("BTC")){
                                Utils.btc();
                            }else if(finalViewHolder.name.getText().toString().equals("ETH")){
                                Utils.eth();
                            }else if(finalViewHolder.name.getText().toString().equals("XRP")){
                                Utils.xrp();
                            }else if(finalViewHolder.name.getText().toString().equals("AED")){
                                Toast.makeText(Data.getcontext(), Data.getcontext().getResources().getString(R.string.add6), Toast.LENGTH_SHORT).show();
                                WeiboDialogUtils.closeDialog(Data.getdialog());
                            }
                        }else{
                            SharedPrefsStrListUtil.putStrListValue(getApplicationContext(), "hotcurrency", Data.getbledata());
                            if(finalViewHolder.name.getText().toString().equals("BTC")){
                                HotWalletUtils.TestBip44BTC(Data.gethotzjc());
                            }else if(finalViewHolder.name.getText().toString().equals("ETH")){
                                HotWalletUtils.TestBip44ETH(Data.gethotzjc());
                            }else if(finalViewHolder.name.getText().toString().equals("XRP")){
                                HotWalletUtils.TestBip44XRP(Data.gethotzjc());
                            }else if(finalViewHolder.name.getText().toString().equals("AED")){
                                Toast.makeText(Data.getcontext(), Data.getcontext().getResources().getString(R.string.add6), Toast.LENGTH_SHORT).show();
                                WeiboDialogUtils.closeDialog(Data.getdialog());
                            }
                        }
                    }
                }
            });
            return view;
        }
        class ViewHolder {
            TextView name;
            ImageView type;
            ImageView image;
        }
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocalManageUtil.setLocal(newBase));
    }
}
