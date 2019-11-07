package com.wallet.cold.app.pawn;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.wallet.cold.utils.Utils;
import com.wallet.cold.utils.WeiboDialogUtils;

import java.util.ArrayList;
import java.util.List;

import static com.wallet.cold.utils.Utils.sendble;
import static com.wallet.cold.utils.Utils.walletamount;

public class CardAmount extends AppCompatActivity implements View.OnClickListener {

    private MyListView lv1;
    private MyAdapter adapter;
    private TextView fhgx;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cardamount);
        lv1=(MyListView)findViewById(R.id.list);
        List<String> list = new ArrayList<>();list.add("cz");
        adapter = new MyAdapter(CardAmount.this, list);
        lv1.setAdapter(adapter);
        fhgx=(TextView) findViewById(R.id.fhgx);fhgx.setOnClickListener(this);
        Data.settype("cardamount");
        Data.setcontext(CardAmount.this);
        Dialog mWeiboDialog = WeiboDialogUtils.createLoadingDialog(this, this.getResources().getString(R.string.type4));
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
                walletamount();
            }
        });
        new Utils().service_init(getApplicationContext());
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
                view = LayoutInflater.from(mContext).inflate(R.layout.item_card, null);
                viewHolder.pt = (TextView) view.findViewById(R.id.ptmoney);Data.setpttext(viewHolder.pt);
                viewHolder.cardamount = (TextView) view.findViewById(R.id.money);
                viewHolder.et = (EditText) view.findViewById(R.id.input);
                viewHolder.qc = (Button) view.findViewById(R.id.qc);
                viewHolder.cz = (Button) view.findViewById(R.id.submit);
                ViewHolder finalViewHolder = viewHolder;
                viewHolder.et.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (finalViewHolder.et.getText().toString().matches("^0")) {//判断当前的输入第一个数是不是为0
                            finalViewHolder.et.setText("");
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
                Data.setcardmoney(viewHolder.cardamount);
                Data.setpttext(viewHolder.pt);
                ViewHolder finalViewHolder = viewHolder;
                viewHolder.cz.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String money1 = finalViewHolder.et.getText().toString();
                        if(finalViewHolder.cardamount.getText().toString().equals("0")){
                            Toast.makeText(getApplicationContext(), Data.getcontext().getResources().getString(R.string.card7), Toast.LENGTH_SHORT).show();
                        }else if (money1.length() < 1) {
                            Toast.makeText(getApplicationContext(), Data.getcontext().getResources().getString(R.string.card3), Toast.LENGTH_SHORT).show();
                        } else if (money1.equals("0")) {
                            Toast.makeText(getApplicationContext(), Data.getcontext().getResources().getString(R.string.fff21), Toast.LENGTH_SHORT).show();
                        } else {
                            Data.settxamount(money1);
                            int amount1 = Integer.valueOf(money1 + "00");
                            Dialog mWeiboDialog = WeiboDialogUtils.createLoadingDialog(CardAmount.this, Data.getcontext().getResources().getString(R.string.card8));
                            Data.setdialog(mWeiboDialog);
                            Data.setbletype("wallettx");
                            String amount = Integer.toHexString(amount1);
                            if (amount.length() == 2) {
                                amount = "000000" + amount;
                            }
                            if (amount.length() == 3) {
                                amount = "00000" + amount;
                            }
                            if (amount.length() == 4) {
                                amount = "0000" + amount;
                            }
                            if (amount.length() == 5) {
                                amount = "000" + amount;
                            }
                            if (amount.length() == 6) {
                                amount = "00" + amount;
                            }
                            if (amount.length() == 7) {
                                amount = "0" + amount;
                            }
                            String ret = Utils.strhex("8C01000000020004" + amount);
                            String a = "55aa8C01000000020004" + amount + ret + "aa55";
                            Data.setresulterror("no");
                            sendble(a, Data.getmService());
                        }
                    }
                });
                viewHolder.qc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String money1 = finalViewHolder.et.getText().toString();
                        if(finalViewHolder.pt.getText().toString().equals("0")){
                            Toast.makeText(getApplicationContext(), Data.getcontext().getResources().getString(R.string.card6), Toast.LENGTH_SHORT).show();
                        }else if (money1.length() < 1) {
                            Toast.makeText(getApplicationContext(), Data.getcontext().getResources().getString(R.string.card3), Toast.LENGTH_SHORT).show();
                        } else if (money1.equals("0")) {
                            Toast.makeText(getApplicationContext(), Data.getcontext().getResources().getString(R.string.fff21), Toast.LENGTH_SHORT).show();
                        } else {
                            Data.settxamount(money1);
                            int amount1 = Integer.valueOf(money1 + "00");
                            Dialog mWeiboDialog = WeiboDialogUtils.createLoadingDialog(CardAmount.this, Data.getcontext().getResources().getString(R.string.card9));
                            Data.setdialog(mWeiboDialog);
                            Data.setbletype("walletcz");
                            String amount = Integer.toHexString(amount1);
                            if (amount.length() == 2) {
                                amount = "000000" + amount;
                            }
                            if (amount.length() == 3) {
                                amount = "00000" + amount;
                            }
                            if (amount.length() == 4) {
                                amount = "0000" + amount;
                            }
                            if (amount.length() == 5) {
                                amount = "000" + amount;
                            }
                            if (amount.length() == 6) {
                                amount = "00" + amount;
                            }
                            if (amount.length() == 7) {
                                amount = "0" + amount;
                            }
                            String ret = Utils.strhex("8C01000000010004" + amount);
                            String a = "55aa8C01000000010004" + amount + ret + "aa55";
                            Data.setresulterror("no");
                            sendble(a, Data.getmService());
                        }
                    }
                });
            }
            return view;
        }
        class ViewHolder {
            TextView pt,cardamount;
            Button cz,qc;
            EditText et;
        }
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

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocalManageUtil.setLocal(newBase));
    }
}

