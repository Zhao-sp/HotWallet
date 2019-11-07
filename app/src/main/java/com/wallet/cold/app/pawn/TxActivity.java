package com.wallet.cold.app.pawn;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wallet.R;
import com.wallet.cold.app.main.IndexActivity;
import com.wallet.cold.utils.Data;
import com.wallet.cold.utils.LocalManageUtil;
import com.wallet.cold.utils.MyListView;
import com.wallet.cold.utils.Utilshttp;
import com.wallet.cold.utils.WeiboDialogUtils;

import java.util.ArrayList;
import java.util.List;

import static com.wallet.cold.utils.Utils.sendble;

public class TxActivity extends AppCompatActivity implements View.OnClickListener {

    private MyListView lv1;
    private MyAdapter adapter;
    private TextView textView,fhgx;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cz);
        findViewById(R.id.back).setOnClickListener(this);
        textView=(TextView)findViewById(R.id.textView4);
        textView.setText(R.string.m17);
        lv1=(MyListView)findViewById(R.id.list);
        fhgx=(TextView) findViewById(R.id.fhgx);fhgx.setOnClickListener(this);
        List<String> list = new ArrayList<>();list.add("tx");
        adapter = new MyAdapter(TxActivity.this, list);
        lv1.setAdapter(adapter);
        Data.settype("txactivity");
        Data.setcontext(TxActivity.this);
        Dialog mWeiboDialog = WeiboDialogUtils.createLoadingDialog(this, this.getResources().getString(R.string.f4));
        Data.setdialog(mWeiboDialog);
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
                new Utilshttp().getptamount();
            }
        });
        new Utilshttp().getptamount();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {//如果返回键按下
            Data.setresult("1");
            Intent intent = new Intent(this, IndexActivity.class);
            startActivity(intent);
        }
        return false;
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fhgx) {
            Data.setresult("1");
            Intent intent = new Intent(this, IndexActivity.class);
            startActivity(intent);
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
                view = LayoutInflater.from(mContext).inflate(R.layout.activity_cz_1, null);
                viewHolder.pt = (TextView) view.findViewById(R.id.pt);
                viewHolder.hier = (TextView) view.findViewById(R.id.hier);
                viewHolder.money = (EditText) view.findViewById(R.id.money);
                viewHolder.submit = (Button) view.findViewById(R.id.submit);
                ViewHolder finalViewHolder = viewHolder;
                viewHolder.money.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (finalViewHolder.money.getText().toString().matches("^0")) {//判断当前的输入第一个数是不是为0
                            finalViewHolder.money.setText("");
                        }
                    }
                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                });
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            if(viewHolder!=null) {
                Data.setpttext(viewHolder.pt);
                Data.sethiertext(viewHolder.hier);
                ViewHolder finalViewHolder = viewHolder;
                viewHolder.submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String money1 = finalViewHolder.money.getText().toString();
                        if (money1.length() < 1) {
                            Toast.makeText(getApplicationContext(), Data.getcontext().getResources().getString(R.string.card3), Toast.LENGTH_SHORT).show();
                        } else if (money1.equals("0")) {
                            Toast.makeText(getApplicationContext(), Data.getcontext().getResources().getString(R.string.fff21), Toast.LENGTH_SHORT).show();
                        } else {
                            final EditText inputServer = new EditText(TxActivity.this);
                            inputServer.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            inputServer.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
                            AlertDialog.Builder builder = new AlertDialog.Builder(TxActivity.this);
                            builder.setTitle(R.string.recover5).setView(inputServer).setNegativeButton(R.string.f513, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            builder.setPositiveButton(R.string.f514, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    if(inputServer.getText().toString().equals("")){
                                        Toast.makeText(Data.getcontext(), Data.getcontext().getResources().getString(R.string.m2), Toast.LENGTH_SHORT).show();
                                    }else {
                                        String data1 = "a9059cbb0000000000000000000000004080a2b89739f67b56c5bf843a347a6cda72eb92";
                                        Data.settxamount(money1);
                                        String data3 = Integer.toHexString(Integer.parseInt(money1));
                                        for (int length = data3.length(); length < 64; length++) {
                                            data3 = "0" + data3;
                                        }
                                        Data.sethiersign(data1 + data3);
                                        Data.setlimit(inputServer.getText().toString());
                                        Data.setsign("end0");
                                        Data.setbizhong("Hier");
                                        Data.setsaoma("yes");
                                        String a = "55aa260110000002000035aa55";//结束签名
                                        sendble(a, Data.getmService());
                                    }
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
            TextView pt,hier;
            EditText money;
            Button submit;
        }
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocalManageUtil.setLocal(newBase));
    }
}
