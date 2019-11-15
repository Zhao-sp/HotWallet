package com.wallet.cold.app.index;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.method.DigitsKeyListener;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wallet.R;
import com.wallet.cold.app.main.IndexActivity;
import com.wallet.cold.utils.CaptureActivity;
import com.wallet.cold.utils.Data;
import com.wallet.cold.utils.LocalManageUtil;
import com.wallet.cold.utils.Utils;
import com.wallet.cold.utils.WeiboDialogUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.wallet.cold.utils.Utils.sendble;


public class JYXXActivity extends AppCompatActivity implements OnClickListener {
    private TextView fhjy,name,jxye,zz,sk,noxx,balance;
    private ImageView sz,kz;
    private ListView lv1;
    private Map<String,Object> map = new HashMap<>();
    private Map<String,Object> map1 = new HashMap<>();
    private List<Map<String,Object>> list = new ArrayList<>();
    private MyAdapter adapter;
    private Handler handler;
    private String result="";
    private Dialog mWeiboDialog;
    private Button trustset;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jyxx);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {// 透明状态栏
            getWindow().addFlags( WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        balance=(TextView)findViewById(R.id.balance);fhjy=(TextView) findViewById(R.id.fhjy);
        lv1=(ListView) findViewById(R.id.list_jy); noxx=(TextView) findViewById(R.id.noxx);
        noxx.setVisibility(View.INVISIBLE); zz=(TextView) findViewById(R.id.zz);sk=(TextView) findViewById(R.id.sk);
        name=(TextView) findViewById(R.id.name);jxye=(TextView) findViewById(R.id.jxye);sz=(ImageView) findViewById(R.id.sz);kz=(ImageView) findViewById(R.id.kz);
        trustset=(Button) findViewById(R.id.trustset);trustset.setVisibility(View.GONE);noxx.setVisibility(View.VISIBLE);
        fhjy.setOnClickListener(this);sz.setOnClickListener(this);kz.setOnClickListener(this);sk.setOnClickListener(this);
        zz.setOnClickListener(this);trustset.setOnClickListener(this);
        Intent intent=getIntent();
        String amsg=intent.getStringExtra("amsg");
        String amsg1=intent.getStringExtra("amsg1");
        String amsg2=intent.getStringExtra("amsg2");
        if(amsg.equals("AED")){
            trustset.setVisibility(View.VISIBLE);
        }
        name.setText(amsg);
        jxye.setText(amsg1+" "+amsg);
        balance.setText("≈"+amsg2);
        int typeheight= Utils.getstatus(JYXXActivity.this);
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) sz.getLayoutParams();
        lp.topMargin = typeheight+10;
        sz.setLayoutParams(lp);
        RelativeLayout.LayoutParams lp1 = (RelativeLayout.LayoutParams) name.getLayoutParams();
        lp1.topMargin = typeheight+10;
        name.setLayoutParams(lp1);
        RelativeLayout.LayoutParams lp2 = (RelativeLayout.LayoutParams) kz.getLayoutParams();
        lp2.topMargin = typeheight+10;
        kz.setLayoutParams(lp2);
        RelativeLayout.LayoutParams lp3 = (RelativeLayout.LayoutParams) fhjy.getLayoutParams();
        lp3.topMargin = typeheight+5;
        fhjy.setLayoutParams(lp3);
        Data.settype("jyxxactivity");
        Data.setcontext(JYXXActivity.this);
        Cursor cursor=null;
        if(Data.getapptype().equals("cold")){
            cursor = Data.getdb().rawQuery("select * from JiaoyiTb where blename='"+ Data.getdevicename()+"'and bizhong ='"+name.getText().toString()+"'", null);
        }else if(Data.getapptype().equals("hot")){
            cursor = Data.getdb().rawQuery("select * from HotJiaoyiTb where blename='"+ Data.getdevicename()+"'and bizhong ='"+name.getText().toString()+"'", null);
        }
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {//游标是否继续向下移动
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String jine = cursor.getString(cursor.getColumnIndex("jine"));
                String riqi = cursor.getString(cursor.getColumnIndex("riqi"));
                String type = cursor.getString(cursor.getColumnIndex("type"));
                map.put("name",name);
                map.put("jine",jine);
                map.put("riqi",riqi);
                map.put("type",type);
                list.add(map);
                adapter = new MyAdapter(JYXXActivity.this, list);
                lv1.setAdapter(adapter);
            }
            //关闭cursor
            cursor.close();
        }else{
            noxx.setVisibility(View.VISIBLE);
        }
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
        int i = v.getId();
        if (i == R.id.fhjy) {
            startActivity(new Intent(this, IndexActivity.class));
        } else if (i == R.id.sz) {
            startActivity(new Intent(this, IndexActivity.class));
        } else if (i == R.id.kz) {
            Intent intent2 = new Intent(this, CaptureActivity.class);
            startActivity(intent2);
        } else if (i == R.id.zz) {
            lv1.setVisibility(View.VISIBLE);
        } else if (i == R.id.sk) {
            noxx.setVisibility(View.VISIBLE);
            lv1.setVisibility(View.INVISIBLE);
        } else if (i == R.id.trustset) {
            final EditText inputServer = new EditText(this);
            inputServer.setTransformationMethod(PasswordTransformationMethod.getInstance());
            inputServer.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.recover5).setView(inputServer).setNegativeButton(R.string.f513, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.setPositiveButton(R.string.f514, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    if(inputServer.getText().toString().equals("")){
                        Toast.makeText(Data.getcontext(),Data.getcontext().getResources().getString(R.string.m2), Toast.LENGTH_SHORT).show();
                    }else {
                        mWeiboDialog = WeiboDialogUtils.createLoadingDialog(JYXXActivity.this, Data.getcontext().getResources().getString(R.string.fff23));
                        Data.setdialog(mWeiboDialog);
                        Data.setlimit(inputServer.getText().toString());
                        Data.setbizhong("trustset");//币种分类
                        Data.setsign("end0");//结束指令
                        Data.setsaoma("yes");//是否进行签名
                        String a1 = "55aa260110000002000035aa55";//结束签名
                        sendble(a1, Data.getmService());
                    }
                }
            });
            builder.show();
        }
    }
    class MyAdapter extends BaseAdapter {
        private Context mContext;
        private List<Map<String,Object>> mList = new ArrayList<>();
        public MyAdapter(Context context, List<Map<String,Object>> list) {
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
                view = LayoutInflater.from(mContext).inflate(R.layout.item_jyxx, null);
                viewHolder.name = (TextView) view.findViewById(R.id.name);
                viewHolder.image = (ImageView) view.findViewById(R.id.image);
                viewHolder.jine = (TextView) view.findViewById(R.id.jine);
                viewHolder.riqi = (TextView) view.findViewById(R.id.riqi);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            map1=(HashMap)list.get(i);
            if(!map1.get("type").equals("1")){
                viewHolder.image.setImageResource(R.drawable.shouru);
                viewHolder.jine.setTextColor(0xff53c280);
            }
            viewHolder.name.setText((String)map.get("name"));
            viewHolder.jine.setText((String)map.get("jine")+name.getText().toString());
            viewHolder.riqi.setText((String)map.get("riqi"));
            return view;
        }
        class ViewHolder {
            TextView name;
            TextView jine;
            TextView riqi;
            ImageView image;
        }
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocalManageUtil.setLocal(newBase));
    }
}
