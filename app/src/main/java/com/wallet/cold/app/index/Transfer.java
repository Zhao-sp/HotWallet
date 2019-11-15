package com.wallet.cold.app.index;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.text.method.NumberKeyListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wallet.R;
import com.wallet.cold.app.auth0.auth0register;
import com.wallet.cold.app.main.IndexActivity;
import com.wallet.cold.rlp.RLP;
import com.wallet.cold.utils.CaptureActivity;
import com.wallet.cold.utils.Data;
import com.wallet.cold.utils.LocalManageUtil;
import com.wallet.cold.utils.LogCook;
import com.wallet.cold.utils.PopWinShare1;
import com.wallet.cold.utils.Utils;
import com.wallet.cold.utils.Utilshttp;
import com.wallet.cold.utils.WeiboDialogUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.wallet.cold.utils.Utils.ETHAddressValidate;
import static com.wallet.cold.utils.Utils.Encrypt;
import static com.wallet.cold.utils.Utils.bitCoinAddressValidate;
import static com.wallet.cold.utils.Utils.bytes2Hex;
import static com.wallet.cold.utils.Utils.bytetostring;
import static com.wallet.cold.utils.Utils.end;
import static com.wallet.cold.utils.Utils.getIndex;
import static com.wallet.cold.utils.Utils.getSubCount_2;
import static com.wallet.cold.utils.Utils.hexStringToString;
import static com.wallet.cold.utils.Utils.sendble;
import static com.wallet.cold.utils.Utils.strhex;
import static com.wallet.cold.utils.Utils.strlength;

public class Transfer extends AppCompatActivity implements View.OnClickListener {
    private PopWinShare1 popWinShare;
    private TextView popadd,popadd1,popadd2,popadd3,balance,fee1,xl,fhf3;
    private EditText to,fee,limit1,amountyue;
    private ImageView saoma,fanhui;
    private Dialog mWeiboDialog;
    private Button commit;
    private String scriptPubKey,strhex1,strhex2;
    private boolean uxto=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment3);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        Data.setn(0);
        Data.setcount(0);
        Data.setsaoma("yes");
        popadd = (TextView) findViewById(R.id.popadd2);
        popadd.setOnClickListener(this);
        popadd1 = (TextView) findViewById(R.id.popadd);
        popadd1.setOnClickListener(this);
        popadd2 = (TextView) findViewById(R.id.popadd1);
        popadd2.setOnClickListener(this);
        popadd3 = (TextView) findViewById(R.id.popadd3);
        popadd3.setOnClickListener(this);
        saoma = (ImageView) findViewById(R.id.saoma);
        saoma.setOnClickListener(this);
        fanhui = (ImageView) findViewById(R.id.fanhui3);
        fanhui.setOnClickListener(this);
        xl = (TextView) findViewById(R.id.xl);
        xl.setOnClickListener(this);
        balance =(TextView)findViewById(R.id.USD);
        fhf3 =(TextView)findViewById(R.id.fhf3);
        fhf3.setOnClickListener(this);
        Data.setbalance(balance);
        commit = (Button) findViewById(R.id.verify2);
        commit.setOnClickListener(this);
        to = (EditText) findViewById(R.id.to);
        limit1 = (EditText) findViewById(R.id.limit1);
        fee = (EditText) findViewById(R.id.fee);
        fee1 = (TextView) findViewById(R.id.fee1);
        fee.setKeyListener(new NumberKeyListener() {
            @Override
            protected char[] getAcceptedChars() {
                char[] numberChars={'1','2','3','4','5','6','7','8','9','0','.'};
                return numberChars;
            }
            @Override
            public int getInputType() {
                return android.text.InputType.TYPE_CLASS_PHONE;
            }
        });
        amountyue = (EditText) findViewById(R.id.btcyue);
        amountyue.setKeyListener(new NumberKeyListener() {
            @Override
            protected char[] getAcceptedChars() {
                char[] numberChars={'1','2','3','4','5','6','7','8','9','0','.'};
                return numberChars;
            }
            @Override
            public int getInputType() {
                return android.text.InputType.TYPE_CLASS_PHONE;
            }
        });
        Bundle extras = getIntent().getExtras();
        if (null != extras) {
            String result = extras.getString("result");
            if(result.contains("=")&&getSubCount_2(result, "=")==2){
                List<String> list=Arrays.asList(result.split(","));
                List<String> list1=Arrays.asList(list.get(0).split("="));
                List<String> list2=Arrays.asList(list.get(1).split("="));
                if(list1.contains("address")){
                    to.setText(list1.get(1));
                    amountyue.setText(list2.get(1));
                }else {
                    to.setText(list2.get(1));
                    amountyue.setText(list1.get(1));
                }
            }else {
                to.setText(result);
            }
        }
        if(to.getText().toString().length()==34){
            Data.setbizhong("XRP");
            popadd.setText("XRP");
            popadd1.setText("XRP");
            popadd2.setText("drops");
            popadd3.setVisibility(View.GONE);
            fee.setVisibility(View.GONE);
            fee1.setVisibility(View.GONE);
            balance.setText(Data.getxrpamount());
        }else if(to.getText().toString().length()==40) {
            popadd.setText("ETH");
            popadd1.setText("ETH");
            popadd2.setText("ETH");
            popadd3.setText("Wei");
            popadd3.setVisibility(View.VISIBLE);
            fee.setVisibility(View.VISIBLE);
            fee1.setVisibility(View.VISIBLE);
            balance.setText(Data.getethbalance());
        }else{
            popadd.setText("BTC");
            popadd1.setText("BTC");
            popadd2.setText("BTC");
            popadd3.setText("BTC");
            popadd3.setVisibility(View.VISIBLE);
            fee.setVisibility(View.VISIBLE);
            fee1.setVisibility(View.VISIBLE);
            balance.setText(Data.getbtcbalance());
        }
        Data.settype("fragment3");
        Data.setcontext(Transfer.this);
        new Utils().service_init(getApplicationContext());
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.popadd2) {
            if (popWinShare == null) {
                //自定义的单击事件
                Transfer.OnClickLintener paramOnClickListener = new Transfer.OnClickLintener();
                popWinShare = new PopWinShare1(Transfer.this, paramOnClickListener);
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
            popWinShare.showAsDropDown(popadd, 0, 0);
            //如果窗口存在，则更新
            popWinShare.update();
        }
        if(v.getId() == R.id.xl) {
            if (popWinShare == null) {
                //自定义的单击事件
                Transfer.OnClickLintener paramOnClickListener = new Transfer.OnClickLintener();
                popWinShare = new PopWinShare1(Transfer.this, paramOnClickListener);
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
            popWinShare.showAsDropDown(popadd, 0, 0);
            //如果窗口存在，则更新
            popWinShare.update();
        }
        if(v.getId() == R.id.saoma) {
            startActivity(new Intent(Transfer.this, CaptureActivity.class));
        }
        if(v.getId() == R.id.fanhui3) {
            startActivity(new Intent(Transfer.this, IndexActivity.class));
        }
        if(v.getId() == R.id.fhf3) {
            startActivity(new Intent(Transfer.this, IndexActivity.class));
        }
        if(v.getId() == R.id.verify2) {
            if(Utils.isNetworkConnected(Data.getcontext())) {
                Data.settype("fragment3");
                if(popadd.getText().toString().equals("BTC")){
                    trade(popadd.getText().toString(),limit1.getText().toString(),to.getText().toString(),amountyue.getText().toString(),fee.getText().toString()
                            ,balance.getText().toString(), Data.getbtcaddress());
                }else if(popadd.getText().toString().equals("ETH")){
                    trade(popadd.getText().toString(),limit1.getText().toString(),to.getText().toString(),amountyue.getText().toString(),fee.getText().toString()
                            ,balance.getText().toString(), Data.getethaddress());
                }else if(popadd.getText().toString().equals("XRP")||popadd.getText().toString().equals("AED")){
                    trade(popadd.getText().toString(), limit1.getText().toString(), to.getText().toString(), amountyue.getText().toString(), ""
                            , balance.getText().toString(), Data.getxrpaddress());
                }
            }else{
                Toast.makeText(Data.getcontext(), Data.getcontext().getResources().getString(R.string.cd2), Toast.LENGTH_SHORT).show();
                WeiboDialogUtils.closeDialog(Data.getdialog());
            }
        }
    }

    public void trade(String type,String pin,String to,String amountyue,String fee,String balance,String address) {
        if (pin.length() != 4) {
            Toast.makeText(getApplicationContext(), this.getResources().getString(R.string.fff13), Toast.LENGTH_SHORT).show();
        } else {
            if (to.equals("")) {
                Toast.makeText(getApplicationContext(), this.getResources().getString(R.string.fff14), Toast.LENGTH_SHORT).show();
                return;
            } else if (amountyue.equals("")) {
                Toast.makeText(getApplicationContext(), this.getResources().getString(R.string.fff15), Toast.LENGTH_SHORT).show();
                return;
            }else if (address.equals(to)) {
                    Toast.makeText(getApplicationContext(), this.getResources().getString(R.string.fff17), Toast.LENGTH_SHORT).show();
                    return;
            } else {
                if (type.equals("BTC")) {
                    if (fee.compareTo("0.0001") < 0) {
                        Toast.makeText(getApplicationContext(), this.getResources().getString(R.string.fff16), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Double a2 = Double.valueOf(fee);
                    Double a1 = Double.valueOf(amountyue);
                    Double a3 = a1 + a2;
                    BigDecimal bd3 = new BigDecimal(a3).setScale(8, BigDecimal.ROUND_DOWN);
                    if (balance.substring(0, 10).compareTo(String.valueOf(bd3)) < 0) {
                        Toast.makeText(getApplicationContext(), this.getResources().getString(R.string.fff18), Toast.LENGTH_SHORT).show();
                    } else {
                        boolean amount1 = false;
                        int index = amountyue.indexOf(".");
                        String subStr = amountyue.substring(index + 1, amountyue.length());
                        for (int i = 0; i < subStr.length(); i++) {
                            if (!subStr.substring(i, i + 1).equals("0")) {
                                amount1 = true;
                            }
                        }
                        if (fee.contains(".") && fee.length() - 1 - fee.indexOf(".") > 8) {
                            Toast.makeText(getApplicationContext(), this.getResources().getString(R.string.fff19), Toast.LENGTH_SHORT).show();
                        } else if (amountyue.contains(".") && amountyue.length() - 1 - amountyue.indexOf(".") > 8) {
                            Toast.makeText(getApplicationContext(), this.getResources().getString(R.string.fff20), Toast.LENGTH_SHORT).show();
                        } else if (!amount1) {
                            Toast.makeText(getApplicationContext(), this.getResources().getString(R.string.fff21), Toast.LENGTH_SHORT).show();
                        } else if (!bitCoinAddressValidate(to)) {
                            Toast.makeText(getApplicationContext(), this.getResources().getString(R.string.fff22), Toast.LENGTH_SHORT).show();
                        } else {
                            mWeiboDialog = WeiboDialogUtils.createLoadingDialog(Transfer.this, this.getResources().getString(R.string.fff23));
                            Data.setdialog(mWeiboDialog);
                            Data.setfee(fee);
                            Data.setyue(amountyue);
                            Data.setto(to);
                            Data.setlimit(pin);
                            Data.setsign("end0");
                            Data.setbizhong("BTC");
                            Data.setsaoma("yes");
                            String a = "55aa260110000002000035aa55";//结束签名
                            sendble(a, Data.getmService());
                        }
                    }
                } else if (type.equals("ETH")) {
                    Double a = Double.parseDouble(balance);
                    Double b = Double.parseDouble(amountyue);
                    if (to.contains("0x")) {
                        Toast.makeText(getApplicationContext(), this.getResources().getString(R.string.fff24), Toast.LENGTH_SHORT).show();
                    } else if (!ETHAddressValidate(to)) {
                        Toast.makeText(getApplicationContext(), this.getResources().getString(R.string.fff25), Toast.LENGTH_SHORT).show();
                    } else if (fee.equals("")) {
                        Toast.makeText(getApplicationContext(), this.getResources().getString(R.string.fff26), Toast.LENGTH_SHORT).show();
                    } else if (limit1.getText().toString().equals("")) {
                        Toast.makeText(getApplicationContext(), this.getResources().getString(R.string.fff27), Toast.LENGTH_SHORT).show();
                    } else if (fee.compareTo("9000") >= 0) {
                        Toast.makeText(getApplicationContext(), this.getResources().getString(R.string.fff28), Toast.LENGTH_SHORT).show();
                    } else if (a.compareTo(b) < 0) {
                        Toast.makeText(getApplicationContext(), this.getResources().getString(R.string.fff18), Toast.LENGTH_SHORT).show();
                    } else {
                        mWeiboDialog = WeiboDialogUtils.createLoadingDialog(Transfer.this, this.getResources().getString(R.string.fff23));
                        Data.setdialog(mWeiboDialog);
                        Data.setfee(fee);
                        Data.setyue(amountyue);
                        Data.setto(to);
                        Data.setlimit(pin);
                        Data.setsign("end0");
                        Data.setbizhong("ETH");
                        Data.setsaoma("yes");
                        String a1 = "55aa260110000002000035aa55";//结束签名
                        sendble(a1, Data.getmService());
                    }
                } else if (type.equals("XRP")||type.equals("AED")) {
                    Double a = Double.parseDouble(balance)*1000000;
                    Double b = Double.parseDouble(amountyue);
                    if (a.compareTo(b) < 0) {
                        Toast.makeText(getApplicationContext(), this.getResources().getString(R.string.fff18), Toast.LENGTH_SHORT).show();
                    } else {
                        mWeiboDialog = WeiboDialogUtils.createLoadingDialog(Transfer.this, this.getResources().getString(R.string.fff23));
                        Data.setdialog(mWeiboDialog);
                        Data.setyue(amountyue);
                        Data.setto(to);
                        Data.setlimit(pin);
                        Data.setsign("end0");
                        if(type.equals("XRP")) {
                            Data.setbizhong("XRP");
                        }else if(type.equals("AED")) {
                            Data.setbizhong("AED");
                        }
                        Data.setsaoma("yes");
                        String a1 = "55aa260110000002000035aa55";//结束签名
                        sendble(a1, Data.getmService());
                    }
                }
            }
        }
    }

    /**
     * 签名获取随机数
     */
    public void chushihua(){
        Data.setsign("number");
        Data.setsaoma("yes");
        String password3=Encrypt(Data.getlimit());
        LogCook.d("哈希值", password3);
        Data.sethash(password3);//pin码哈希值
        String data1 = "8401000000100000";
        String ret=strhex(data1);
        String a = "55aa8401000000100000"+ ret + "aa55";
        sendble(a, Data.getmService());
    }

    /**
     * 签名初始化
     * @param number
     */
    public void initialize(String number){
        Data.setsign("chushihua");
        Data.setsaoma("yes");
        String data = Data.gethash()+number;
        byte[] bytes1 = new byte[data.length() / 2];
        for (int i = 0; i < data.length() / 2; i++) {//16进制字符串转byte[]
            String subStr = data.substring(i * 2, i * 2 + 2);
            bytes1[i] = (byte) Integer.parseInt(subStr, 16);
        }
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(bytes1);
            number = bytes2Hex(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        String a="";
        if(Data.getbizhong().equals("ERC20")||Data.getbizhong().equals("Hier")) {
            String strlength = strlength(number + "20202000");
            String data1 = "26011001000100" + strlength + number + "20202000";
            String ret = strhex(data1);
            a = "55aa" + data1 + ret + "aa55";
        }else if(Data.getbizhong().equals("Pawn")) {
            String strlength = strlength(number + "5041574E");
            String data1 = "26011001000100" + strlength + number + "5041574E";
            String ret = strhex(data1);
            a = "55aa" + data1 + ret + "aa55";
        }else{
            String data1 = "2601100000010020" + number;
            String ret=strhex(data1);
            a = "55aa2601100000010020" + number + ret + "aa55";
        }
        sendble(a, Data.getmService());
    }

    /**
     * 比特币交易逻辑
     */
    public void btcchushihua() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (Data.getbizhong().equals("BTC")) {//比特币构造原始交易
                    try {
                        Map<String, Object> map = new LinkedHashMap<>();
                        List<String> list = new ArrayList<String>();
                        String map2 = "";
                        boolean a = false;
                        boolean b = false;
                        for (int i = 0; i < Data.getamount().size(); i++) {//遍历金额是否有相等的
                            Double a1 = Double.valueOf(Data.getamount().get(i)) - Double.valueOf(Data.getyue());
                            BigDecimal bd = new BigDecimal(a1).setScale(8, BigDecimal.ROUND_DOWN);
                            Double a2 = Double.valueOf(Data.getfee());
                            BigDecimal bd2 = new BigDecimal(a2).setScale(8, BigDecimal.ROUND_DOWN);
                            if (String.valueOf(bd).compareTo(String.valueOf(bd2)) == 0) {
                                a = true;
                                map.put("\"txid\"", "\"" + Data.gettxid().get(i) + "\"");
                                map.put("\"vout\"", Data.getvout().get(i));
                                scriptPubKey = Data.getscriptPubKey().get(i);
                                Data.setscriptPubKey1(scriptPubKey);
                                map2 = "{\"address\":\"" + Data.getbtcaddress() + "\",\"v\":0},{\"address\":\"" + Data.getto() + "\",\"v\":" + Data.getyue() + "}";
                                break;
                            }
                        }
                        if (!a) {
                            for (int i = 0; i < Data.getamount().size(); i++) {//遍历金额是否有大于的
                                Double a1 = Double.valueOf(Data.getamount().get(i)) - Double.valueOf(Data.getyue());
                                BigDecimal bd = new BigDecimal(a1).setScale(8, BigDecimal.ROUND_DOWN);//余额减去转账
                                Double a2 = Double.valueOf(Data.getfee());
                                BigDecimal bd2 = new BigDecimal(a2).setScale(8, BigDecimal.ROUND_DOWN);//服务费
                                if (String.format("%.8f", bd).compareTo(String.valueOf(bd2)) > 0) {
                                    Double a4 = a1 - a2;//找零
                                    BigDecimal bd4 = new BigDecimal(a4).setScale(8, BigDecimal.ROUND_DOWN);
                                    a = true;
                                    map.put("\"txid\"", "\"" + Data.gettxid().get(i) + "\"");
                                    map.put("\"vout\"", Data.getvout().get(i));
                                    scriptPubKey = Data.getscriptPubKey().get(i);
                                    Data.setscriptPubKey1(scriptPubKey);
                                    map2 = "{\"address\":\"" + Data.getbtcaddress() + "\",\"v\":" + String.valueOf(bd4) + "},{\"address\":\"" + Data.getto() + "\",\"v\":" + Data.getyue() + "}";
                                    break;
                                }
                            }
                        }
                        List<Object> map1 = new ArrayList<Object>();
                        List<String> list1 = new ArrayList<String>();
                        if (!a) {
                            Double sum = 0d;
                            b = true;
                            for (int i = 0; i < Data.getamount().size(); i++) {//遍历余额小于转账金额
                                Double a1 = Double.valueOf(Data.getamount().get(i)) - Double.valueOf(Data.getyue());
                                BigDecimal bd = new BigDecimal(a1).setScale(8, BigDecimal.ROUND_DOWN);//余额减去转账
                                Double a2 = Double.valueOf(Data.getfee());
                                BigDecimal bd2 = new BigDecimal(a2).setScale(8, BigDecimal.ROUND_DOWN);//服务费
                                Double a3 = Double.valueOf(Data.getyue()) + Double.valueOf(Data.getfee());
                                BigDecimal bd3 = new BigDecimal(a3).setScale(8, BigDecimal.ROUND_DOWN);//转账加服务费
                                if ((String.valueOf(bd).compareTo(String.valueOf(bd2)) < 0 || bd.toString().equals("0E-8")) && !Data.getamount().get(i).equals("0.0")) {
                                    list1.add(String.valueOf(i));
                                    sum += Double.parseDouble(Data.getamount().get(i));
                                    if (new BigDecimal(sum).setScale(8, BigDecimal.ROUND_DOWN).compareTo(bd3) > 0) {//最终转账和大于(转账加服务费)
                                        Double a4 = sum - a3;//找零
                                        BigDecimal bd4 = new BigDecimal(a4).setScale(8, BigDecimal.ROUND_DOWN);
                                        for (int j = 0; j < i + 1; j++) {
                                            if (list1.contains(String.valueOf(j))) {
                                                map = new HashMap<>();
                                                map.put("\"txid\"", "\"" + Data.gettxid().get(j) + "\"");
                                                map.put("\"vout\"", Data.getvout().get(j));
                                                map1.add(map);
                                            }
                                        }
                                        map2 = "{\"address\":\"" + Data.getbtcaddress() + "\",\"v\":" + String.valueOf(bd4) + "},{\"address\":\"" + Data.getto() + "\",\"v\":" + Data.getyue() + "}";
                                        break;
                                    }
                                }
                            }
                        }
                        String address = "";
                        if (b) {
                            uxto = true;
                            Data.setuxto(uxto);
                            address = "{\"txids\":[" + map1.toString().substring(1, map1.toString().length() - 1) + "],\"adds\":[" + map2 + "]}";
                        } else {
                            uxto = false;
                            Data.setuxto(uxto);
                            address = "{\"txids\":[" + map.toString() + "],\"adds\":[" + map2 + "]}";
                        }
                        Data.setbtctype("gouzaojiaoyi");
                        String btcerror = Utils.getbtchttp(address);
                        if (btcerror.contains("success")) {
                            btcerror = btcerror.substring(0, btcerror.length() - 7);
                            LogCook.d("返回构造原始交易数据", btcerror);
                            result1 = btcerror;
                            Data.setresult(result1);
                            sign(result1);//进行签名
                        } else {
                            String error = btcerror;
                            Looper.prepare();
                            Toast.makeText(Data.getcontext(), Data.getcontext().getResources().getString(R.string.fff29) + error, Toast.LENGTH_SHORT).show();
                            WeiboDialogUtils.closeDialog(Data.getdialog());
                            Data.setbletype("1");
                            end();
                            Looper.loop();
                        }
                    } catch (Throwable e) {
                        Looper.prepare();
                        Toast.makeText(Data.getcontext(), Data.getcontext().getResources().getString(R.string.fff30), Toast.LENGTH_SHORT).show();
                        WeiboDialogUtils.closeDialog(Data.getdialog());
                        Data.setbletype("1");
                        end();
                        Looper.loop();
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
    private List<String> sign = new ArrayList<>();
    /**
     * 比特币发送交易请求
     */
    public void  btc() {
        new Thread(new Runnable() {//
            @Override
            public void run() {
                if (Data.getuxto()) {
                    String data = "";
                    result1 = Data.getresult();
                    int count = getSubCount_2(result1, "ffffffff");
                    for (int q = 0; q < count; q++) {
                        int index = getIndex(result1, q + 1, "ffffffff");
                        if (q == 0) {
                            strhex1 = result1.substring(0, index - 2);
                            strhex2 = result1.substring(index, result1.length());
                        } else {
                            int index1 = getIndex(result1, q, "ffffffff");
                            strhex1 = result1.substring(index1, index - 2);
                            strhex2 = result1.substring(index, result1.length());
                        }
                        sign = Data.getsigndata();
                        String a = sign.get(q) + Data.getpubkey();
                        byte[] result = new byte[a.length() / 2];
                        char[] achar = a.toCharArray();
                        for (int i = 0; i < a.length() / 2; i++) {
                            int pos = i * 2;
                            result[i] = (byte) ((byte) "0123456789ABCDEF".indexOf(achar[pos]) << 4 | (byte) "0123456789ABCDEF".indexOf(achar[pos + 1]));
                        }
                        String len = Integer.toHexString(Integer.parseInt(String.valueOf(result.length + 3)));//签名数据和公钥总长度+3
                        byte[] bytes1 = new byte[sign.get(q).length() / 2];
                        for (int i = 0; i < sign.get(q).length() / 2; i++) {//16进制字符串转byte[]
                            String subStr = sign.get(q).substring(i * 2, i * 2 + 2);
                            bytes1[i] = (byte) Integer.parseInt(subStr, 16);
                        }
                        String len1 = Integer.toHexString(Integer.parseInt(String.valueOf(bytes1.length + 1)));//签名数据长度+1
                        byte[] bytes2 = new byte[Data.getpubkey().length() / 2];
                        for (int i = 0; i < Data.getpubkey().length() / 2; i++) {//16进制字符串转byte[]
                            String subStr = Data.getpubkey().substring(i * 2, i * 2 + 2);
                            bytes2[i] = (byte) Integer.parseInt(subStr, 16);
                        }
                        String len2 = Integer.toHexString(Integer.parseInt(String.valueOf(bytes2.length)));//公钥长度
                        if (q == 0) {
                            data = strhex1 + len + len1 + sign.get(q) + "01" + len2 + Data.getpubkey();
                        } else if (q == count - 1) {
                            data = data + strhex1 + len + len1 + sign.get(q) + "01" + len2 + Data.getpubkey() + strhex2;

                        } else {
                            data = data + strhex1 + len + len1 + sign.get(q) + "01" + len2 + Data.getpubkey();
                        }
                    }
                    LogCook.d("ble返回btc多个txid签名结果", sign.toString());
                    LogCook.d("发送交易数据", data);
                    Data.setbtctype("sendrawtransaction");
                    String address = "{\"address\":\"" + data + "\"}";
                    String btcerror = Utils.getbtchttp(address);
                    if (!btcerror.equals("")) {
                        if (!btcerror.contains("success")) {
                            Looper.prepare();
                            Toast.makeText(Data.getcontext(), Data.getcontext().getResources().getString(R.string.fff30) + btcerror, Toast.LENGTH_SHORT).show();
                            WeiboDialogUtils.closeDialog(Data.getdialog());
                            Looper.loop();
                        } else {
                            (new Thread(runnable1)).start();
                        }
                    }
                } else {
                    String data2 = Data.getresultdata();
                    String a = data2 + Data.getpubkey();
                    byte[] bytes = new byte[a.length() / 2];
                    for (int i = 0; i < a.length() / 2; i++) {//16进制字符串转byte[]
                        String subStr = a.substring(i * 2, i * 2 + 2);
                        bytes[i] = (byte) Integer.parseInt(subStr, 16);
                    }
                    String len = Integer.toHexString(Integer.parseInt(String.valueOf(bytes.length + 3)));//签名数据和公钥总长度+3
                    byte[] bytes1 = new byte[data2.length() / 2];
                    for (int i = 0; i < data2.length() / 2; i++) {//16进制字符串转byte[]
                        String subStr = data2.substring(i * 2, i * 2 + 2);
                        bytes1[i] = (byte) Integer.parseInt(subStr, 16);
                    }
                    String len1 = Integer.toHexString(Integer.parseInt(String.valueOf(bytes1.length + 1)));//签名数据长度+1
                    byte[] bytes2 = new byte[Data.getpubkey().length() / 2];
                    for (int i = 0; i < Data.getpubkey().length() / 2; i++) {//16进制字符串转byte[]
                        String subStr = Data.getpubkey().substring(i * 2, i * 2 + 2);
                        bytes2[i] = (byte) Integer.parseInt(subStr, 16);
                    }
                    String len2 = Integer.toHexString(Integer.parseInt(String.valueOf(bytes2.length)));//公钥长度
                    String data = Data.getstrhex1() + len + len1 + data2 + "01" + len2 + Data.getpubkey() + "f" + Data.getstrhex2();
                    LogCook.d("发送交易数据", data);
                    Data.setbtctype("sendrawtransaction");
                    String address = "{\"address\":\"" + data + "\"}";
                    String btcerror = Utils.getbtchttp(address);
                    if (btcerror.equals("")) {
                        (new Thread(runnable1)).start();
                    }else{
                        Looper.prepare();
                        Toast.makeText(Data.getcontext(), Data.getcontext().getResources().getString(R.string.fff30) + btcerror, Toast.LENGTH_SHORT).show();
                        WeiboDialogUtils.closeDialog(Data.getdialog());
                        Looper.loop();
                    }
                }
            }
        }).start();
    }
    Runnable runnable1 = new Runnable() {//刷新比特币余额
        @Override
        public void run() {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date curDate = new Date(System.currentTimeMillis());
            String str = formatter.format(curDate);
            Data.getdb().execSQL("insert into JiaoyiTb (blename,name,bizhong,jine,riqi,type) values " +
                    "('" + Data.getdevicename() + "','" + Data.getbtcaddress() + "','BTC'," + Data.getyue() + ",'" + str + "',1)");
            Data.setbtctype("balance");
            String btcJson = "{\"min\":0,\"max\":9999999,\"address\":\""+ Data.getbtcaddress()+"\"}";
            String btcerror = Utils.getbtchttp(btcJson);
            if (btcerror.contains("success")) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Data.getbalance().setText(Data.getbtcbalance());
                    }
                });
                Looper.prepare();
                Toast.makeText(Data.getcontext(), Data.getcontext().getResources().getString(R.string.fff32), Toast.LENGTH_SHORT).show();
                WeiboDialogUtils.closeDialog(Data.getdialog());
                Looper.loop();
            }else {
                Looper.prepare();
                Toast.makeText(Data.getcontext(), Data.getcontext().getResources().getString(R.string.fff33) + btcerror, Toast.LENGTH_SHORT).show();
                WeiboDialogUtils.closeDialog(Data.getdialog());
                Looper.loop();
            }
        }
    };
    /**
     * 以太坊交易逻辑
     */
    public void eth(){//以太坊网络请求
        new Thread(new Runnable() {
            public void run() {
                if (Data.getethtype().equals("dealid")) {//获取交易序号
                    String address = "{\"address\":\"0x" + Data.getethaddress() + "\"}";
                    String dealid = Utils.getethhttp(address);
                    if (dealid.contains("success")) {
                        dealid = dealid.substring(0, dealid.length() - 7);
                        ethcreatetransaction(dealid);
                    } else {
                        String error = dealid;
                        Looper.prepare();
                        Toast.makeText(getApplicationContext(), Data.getcontext().getResources().getString(R.string.fff34) + error, Toast.LENGTH_SHORT).show();
                        WeiboDialogUtils.closeDialog(Data.getdialog());
                        Looper.loop();
                    }
                } else if (Data.getethtype().equals("sendtransaction")) {//发送交易
                    if(!Data.getrlpdata().equals("")) {
                        String data1 = Data.getrlpdata().substring(0, 2);
                        String data3 = Data.getrlpdata().substring(2, 66);
                        String data4 = Data.getrlpdata().substring(66, Data.getrlpdata().length());
                        listdata = Data.getrlplist();
                        listdata.add(data1);
                        listdata.add(data3);
                        listdata.add(data4);
                        LogCook.d("待编码数据", listdata.toString());
                        List<byte[]> list = new ArrayList<>();
                        for (int j = 0; j < listdata.size(); j++) {
                            byte[] bytes = new byte[listdata.get(j).length() / 2];
                            for (int i = 0; i < listdata.get(j).length() / 2; i++) {//16进制字符串转byte[]
                                String subStr = listdata.get(j).substring(i * 2, i * 2 + 2);
                                bytes[i] = (byte) Integer.parseInt(subStr, 16);
                            }
                            byte[] data = RLP.encode(bytes);
                            list.add(data);
                        }
                        byte[] data = RLP.encodeList(list.get(0), list.get(1), list.get(2), list.get(3), list.get(4), list.get(5), list.get(6), list.get(7), list.get(8));
                        String encodedata = bytetostring(data);
                        LogCook.d("发送交易rlp编码", encodedata);
                        if(Data.getbizhong().equals("ERC20")) {
                            if (Data.getauth0sign().equals("register")) {
                                Looper.prepare();
                                new auth0register().auth0register(encodedata);
                                Looper.loop();
                                return;
                            }
                        }
                        if(Data.getbizhong().equals("Hier")) {
                            Looper.prepare();
                            if(Data.gettype().equals("czactivity")) {
                                new Utilshttp().czhier(encodedata);
                            }else if(Data.gettype().equals("txactivity")||Data.gettype().equals("spxqactivity")) {
                                new Utilshttp().txhier(encodedata);
                            }
                            Looper.loop();
                            return;
                        }
                        if(Data.getbizhong().equals("Pawn")) {
                            Looper.prepare();
                            if(Data.gettype().equals("tzactivity")) {
                                new Utilshttp().tzpawn(encodedata);
                            }else if(Data.gettype().equals("cdactivity")){
                                new Utilshttp().shpawn(encodedata);
                            }else if(Data.gettype().equals("tztypeactivity")){
                                new Utilshttp().fhpawn(encodedata);
                            }
                            Looper.loop();
                            return;
                        }
                        String address = "{\"address\":\"0x" + encodedata + "\"}";
                        Looper.prepare();
                        String dealid = Utils.getethhttp(address);
                        Data.setresult(dealid);
                        Message msg = Handler.obtainMessage();
                        msg.arg1 = R.string.msg_not_network;
                        Handler.sendMessage(msg);
                    }
                }
            }
        }).start(); // 开启线程
    }

    private Handler Handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.arg1) {
                case R.string.msg_not_network:
                    String dealid=Data.getresult();
                    Data.setresult("");
                    if (dealid == "") {
                        if(Data.getbizhong().equals("ETH")) {
                            Data.setethtype("balance");
                            String address1 = "{\"address\":\"0x" + Data.getethaddress() + "\"}";
                            String etherror = Utils.getethhttp(address1);
                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date curDate = new Date(System.currentTimeMillis());
                            String str = formatter.format(curDate);
                            Data.getdb().execSQL("insert into JiaoyiTb (blename,name,bizhong,jine,riqi,type) values " +
                                    "('" + Data.getdevicename() + "','" + Data.getethaddress() + "','ETH'," + Data.getyue() + ",'" + str + "',1)");
                            if (etherror.contains("success")) {
                                Data.getbalance().setText(Data.getethbalance());
                                Toast.makeText(Data.getcontext(), Data.getcontext().getResources().getString(R.string.fff32), Toast.LENGTH_SHORT).show();
                                WeiboDialogUtils.closeDialog(Data.getdialog());
                            } else {
                                Toast.makeText(Data.getcontext(), Data.getcontext().getResources().getString(R.string.fff36) + etherror, Toast.LENGTH_SHORT).show();
                                WeiboDialogUtils.closeDialog(Data.getdialog());
                            }
                        }else if(Data.getbizhong().equals("ERC20")){
                            if(Data.getauth0sign().equals("register")) {
                                Toast.makeText(Data.getcontext(), Data.getcontext().getResources().getString(R.string.fff38), Toast.LENGTH_SHORT).show();
                                WeiboDialogUtils.closeDialog(Data.getdialog());
                            }else if(Data.getauth0sign().equals("login")) {
                                Toast.makeText(Data.getcontext(), Data.getcontext().getResources().getString(R.string.fff40), Toast.LENGTH_SHORT).show();
                                WeiboDialogUtils.closeDialog(Data.getdialog());
                            }
                        }
                    } else {
                        if (Data.getbizhong().equals("ETH")){
                            Toast.makeText(Data.getcontext(), Data.getcontext().getResources().getString(R.string.fff30) + dealid, Toast.LENGTH_SHORT).show();
                            LogCook.d("ETH交易失败", dealid);
                            WeiboDialogUtils.closeDialog(Data.getdialog());
                        }else if(Data.getbizhong().equals("ERC20")) {
                            if(Data.getauth0sign().equals("register")) {
                                Toast.makeText(Data.getcontext(), Data.getcontext().getResources().getString(R.string.fff39) + dealid, Toast.LENGTH_SHORT).show();
                                WeiboDialogUtils.closeDialog(Data.getdialog());
                            }else if(Data.getauth0sign().equals("login")) {
                                Toast.makeText(Data.getcontext(), Data.getcontext().getResources().getString(R.string.fff41), Toast.LENGTH_SHORT).show();
                                WeiboDialogUtils.closeDialog(Data.getdialog());
                            }
                        }
                    }
                    Looper.loop();
                    break;
                default:
                    break;
            }
        }
    };

    private List<String> listdata = new ArrayList<>();//原始交易数据
    private String result1;
    private void ethcreatetransaction(String dealid){//以太坊构造交易并与卡交互
        Data.setsaoma("yes");
        listdata.clear();
        if(dealid.equals("00")) {//交易序号
            listdata.add("");
        }else{
            listdata.add(dealid);
        }
        if(Data.getbizhong().equals("ERC20")){
            listdata.add("46");//交易手续费单价wei
            listdata.add("015f90");//交易总手续费上限,单位wei  2个币 1bc16d674ec80000
            //listdata.add("ac73828d4e29ba3de3959c2ec816ed9373369b85");//auth0开发环境合约地址
            listdata.add("ea661abe47930ec61a6f41d0a734ee2bbcbd714a");//auth0演示环境合约地址
            listdata.add("");
            if(Data.getauth0sign().equals("register")||Data.getauth0sign().equals("login_register")) {
                String address = hexStringToString(Data.getauth0address());
                address = address.substring(2, address.length() - 1);
                listdata.add("4a270f47000000000000000000000000" + address);
            }else if(Data.getauth0sign().equals("login")) {
                listdata.add(Data.getplaint());
            }
        }else if(Data.getbizhong().equals("Pawn")){
            listdata.add("46");//交易手续费单价wei
            listdata.add("015f90");//交易总手续费上限,单位wei  2个币 1bc16d674ec80000
            //listdata.add("82792364c2df3b3c655066f15e4d9fb7b549ebb1");//典当开发环境合约地址
            listdata.add("127bb81098cd890ec231199f91434385802ec459");//典当演示环境合约地址
            listdata.add("");
            listdata.add(Data.gethiersign());
        }else if(Data.getbizhong().equals("Hier")){
            listdata.add("46");//交易手续费单价wei
            listdata.add("015f90");//交易总手续费上限,单位wei  2个币 1bc16d674ec80000
            //listdata.add("f53b60446c39d112da910c956d8523205d050f44");//海博币开发环境合约地址
            listdata.add("f53b60446c39d112da910c956d8523205d050f44");//海博币演示环境合约地址
            listdata.add("");
            listdata.add(Data.gethiersign());
        }else if(Data.getbizhong().equals("ETH")){
            listdata.add(Data.getfee());//交易手续费单价wei
            listdata.add("7530");//交易总手续费上限,单位wei  2个币 1bc16d674ec80000
            listdata.add(Data.getto());//转账地址
            String amount1="";//转账金额
            if(Data.getyue().contains(".")) {
                Double gasPrice = Double.parseDouble(Data.getyue());//0.00000000000000036
                gasPrice = gasPrice * 1000000000000000000L;
                String a=new java.text.DecimalFormat("0").format(gasPrice);
                long l = Long.valueOf(a).longValue();
                amount1 = Long.toHexString(l);
            }else{
                amount1=new BigInteger(Data.getyue()+"000000000000000000", 10).toString(16);
            }
            if(amount1.length()%2==1){
                amount1="0"+amount1;
            }
            listdata.add(amount1);//amount,单位wei，1以太币=1000000000000000000wei
            listdata.add("");//data,固定值为00
        }
        LogCook.d("list",listdata.toString());
        Data.setrlplist(listdata);
        List<byte[]> list = new ArrayList<>();
        for(int j=0;j<listdata.size();j++) {
            byte[] bytes = new byte[listdata.get(j).length() / 2];
            for (int i = 0; i < listdata.get(j).length() / 2; i++) {//16进制字符串转byte[]
                String subStr = listdata.get(j).substring(i * 2, i * 2 + 2);
                bytes[i] = (byte) Integer.parseInt(subStr, 16);
            }
            byte[] data = RLP.encode(bytes);//rlp编码
            list.add(data);
        }
        byte[] data = RLP.encodeList(list.get(0),list.get(1),list.get(2),list.get(3),list.get(4),list.get(5));
        result1=bytetostring(data);
        LogCook.d("eth待签名rlp编码", result1);
        Data.setresult(result1);
        sign(Data.getresult());//进行签名
    }

    /**
     *xrp构造待签名数据
     */
    public void xrpcreatetransaction(){
        String data1="53545800120000228000000024";
        String data2=Integer.toHexString(Integer.parseInt(Data.getxrpserialnumber()));
        for(int i=data2.length();i<8;i++){
            data2="0"+data2;
        }
        String data3="6140000000";
        String data4=Integer.toHexString(Integer.parseInt(Data.getyue()));
        for(int i=data4.length();i<8;i++){
            data4="0"+data4;
        }
        String data5="68400000000000000C7321";
        String data6=Data.getxrppub();
//        String data7="8114";
//        String data8="A36974B21AFB3F3BB46E8B26382C015528E60B9F";
//        String data9="8314";
//        String data10="E0249ACE24AFCC2339F55FF9C4119CAAFC9825CB";
        String data7="8114";
        String data8="DAC0052492C9E9610BD1E5F860D1E026EA47DA90";
        String data9="8314";
        String data10="BCE2C71D73612D1F37B5A3E1947AB3227A76CD84";
        sign(data1+data2+data3+data4+data5+data6+data7+data8+data9+data10);//进行签名
    }

    /**
     *xrp发送交易数据
     */
    public void xrpsendtransaction(String sign){
        String data1="120000228000000024";
        String data2=Integer.toHexString(Integer.parseInt(Data.getxrpserialnumber()));
        for(int i=data2.length();i<8;i++){
            data2="0"+data2;
        }
        String data3="6140000000";
        String data4=Integer.toHexString(Integer.parseInt(Data.getyue()));
        for(int i=data4.length();i<8;i++){
            data4="0"+data4;
        }
        String data5="68400000000000000C7321";
        String data6=Data.getxrppub();
        String data7="74";
        String data8=strlength(sign);
        String data9="8114";
        String data10="DAC0052492C9E9610BD1E5F860D1E026EA47DA90";
        String data11="8314";
        String data12="BCE2C71D73612D1F37B5A3E1947AB3227A76CD84";
//        String data9="8114";
//        String data10="A36974B21AFB3F3BB46E8B26382C015528E60B9F";
//        String data11="8314";
//        String data12="E0249ACE24AFCC2339F55FF9C4119CAAFC9825CB";
        new Utilshttp().getxrpsendtransaction(data1+data2+data3+data4+data5+data6+data7+data8+sign+data9+data10+data11+data12);
    }

    /**
     *瑞波币代币构造待签名数据
     */
    public void aedcreatetransaction(){
        String data1="53545800120000228000000024";
        String data2=Integer.toHexString(Integer.parseInt(Data.getxrpserialnumber()));
        for(int i=data2.length();i<8;i++){
            data2="0"+data2;
        }
        String data3="61";
        String data4="D4838D7EA4C68000";
        String data5="0000000000000000000000004145440000000000";
        String data51="C8865AF270B553E2B2DB2371B703EC4D253D9F42";
        String data52="68400000000000000C7321";
        String data6=Data.getxrppub();
        String data7="8114";
        String data8="DAC0052492C9E9610BD1E5F860D1E026EA47DA90";
        String data9="8314";
        String data10="BCE2C71D73612D1F37B5A3E1947AB3227A76CD84";
        sign(data1+data2+data3+data4+data5+data51+data52+data6+data7+data8+data9+data10);//进行签名
    }

    /**
     *aed发送交易数据
     */
    public void aedsendtransaction(String sign){
        String data1="120000228000000024";
        String data2=Integer.toHexString(Integer.parseInt(Data.getxrpserialnumber()));
        for(int i=data2.length();i<8;i++){
            data2="0"+data2;
        }
        String data3="61";
        String data4="D4838D7EA4C68000";
        String data5="0000000000000000000000004145440000000000";
        String data51="C8865AF270B553E2B2DB2371B703EC4D253D9F42";
        String data52="68400000000000000C7321";
        String data6=Data.getxrppub();
        String data7="74";
        String data8=strlength(sign);
        String data9="8114";
        String data10="DAC0052492C9E9610BD1E5F860D1E026EA47DA90";
        String data11="8314";
        String data12="BCE2C71D73612D1F37B5A3E1947AB3227A76CD84";
        new Utilshttp().getxrpsendtransaction(data1+data2+data3+data4+data5+data51+data52+data6+data7+data8+sign+data9+data10+data11+data12);
    }

    /**
     *trustset构造待签名数据
     */
    public void trustsetcreatetransaction(){
        String data1="53545800120014228000000024";
        String data2="00000023201B001E9C7A";
        String data3="63";
        String data4="D5038D7EA4C68000";
        String data5="0000000000000000000000004145440000000000";
        String data51="C8865AF270B553E2B2DB2371B703EC4D253D9F42";
        String data52="68400000000000000C7321";
        String data6=Data.getxrppub();
        String data7="8114";
        String data8="DAC0052492C9E9610BD1E5F860D1E026EA47DA90";
        sign(data1+data2+data3+data4+data5+data51+data52+data6+data7+data8);//进行签名
    }

    /**
     *trustset发送交易数据
     */
    public void trustsetsendtransaction(String sign){
        String data1="120014228000000024";
        String data2="00000023201B001E9C7A";
        String data3="63";
        String data4="D5038D7EA4C68000";
        String data5="0000000000000000000000004145440000000000";
        String data51="C8865AF270B553E2B2DB2371B703EC4D253D9F42";
        String data52="68400000000000000C7321";
        String data6=Data.getxrppub();
        String data7="74";
        String data8=strlength(sign);
        String data9="8114";
        String data10="DAC0052492C9E9610BD1E5F860D1E026EA47DA90";
        new Utilshttp().getxrpsendtransaction(data1+data2+data3+data4+data5+data51+data52+data6+data7+data8+sign+data9+data10);
    }

    /**
     * 数据签名
     * @param s
     */
    public void sign(String s){
        Data.setsign("signing");
        Data.setreceivedata("no");Data.setresulterror("no");
        Data.setsaoma("yes");
        Data.setbtcsign(false);
        if(Data.getbizhong().equals("BTC")) {//比特币数据签名
            if (Data.getuxto()) {
                int ffcount=getSubCount_2(s, "ffffffff");
                Data.setcount(getSubCount_2(s, "ffffffff"));
                int q= Data.getn();
                int w=q+1;
                LogCook.d("第"+ w +"个txid","共"+String.valueOf(ffcount)+"个txid");
                int index1 = getIndex(s,ffcount,"ffffffff");
                strhex1 = s.substring(0, index1+8);
                strhex2 = s.substring(index1+8, s.length());
                scriptPubKey = Data.getscriptPubKey().get(q);
                byte[] bytes3 = new byte[scriptPubKey.length() / 2];
                for (int i = 0; i < scriptPubKey.length() / 2; i++) {//16进制字符串转byte[]
                    String subStr = scriptPubKey.substring(i * 2, i * 2 + 2);
                    bytes3[i] = (byte) Integer.parseInt(subStr, 16);
                }
                String len = Integer.toHexString(Integer.parseInt(String.valueOf(bytes3.length)));
                List<String> list = new ArrayList<>();
                int c=0;
                if(w==1) {
                    if (strhex1.length() % 1024 != 0) {
                        c = strhex1.length() / 1024 + 1;
                    } else {
                        c = strhex1.length() / 1024;
                    }
                    for (int i = 0; i < c; i++) {
                        if (i == c - 1) {
                            String strlength = strlength(strhex1.substring(i * 1024, strhex1.length()));
                            if (strlength.length() == 1) {
                                String data = "0501" + "00000080000" + strlength + strhex1.substring(i * 1024, strhex1.length());
                                String ret = strhex(data);
                                String a = "55aa050100000080000" + strlength + strhex1.substring(i * 1024, strhex1.length()) + ret + "aa55";
                                list.add(a);
                            } else if (strlength.length() == 2) {
                                String data = "0501" + "0000008000" + strlength + strhex1.substring(i * 1024, strhex1.length());
                                String ret = strhex(data);
                                String a = "55aa05010000008000" + strlength + strhex1.substring(i * 1024, strhex1.length()) + ret + "aa55";
                                list.add(a);
                            } else if (strlength.length() == 3) {
                                String data = "0501" + "000000800" + strlength + strhex1.substring(i * 1024, strhex1.length());
                                String ret = strhex(data);
                                String a = "55aa0501000000800" + strlength + strhex1.substring(i * 1024, strhex1.length()) + ret + "aa55";
                                list.add(a);
                            }
                        } else {
                            String strlength = strlength(strhex1.substring(i * 1024, 1024 * (i + 1)));
                            if (strlength.length() == 2) {
                                String data = "0501" + "0000008000" + strlength + strhex1.substring(i * 1024, 1024 * (i + 1));
                                String ret = strhex(data);
                                String a = "55aa05010000008000" + strlength + strhex1.substring(i * 1024, 1024 * (i + 1)) + ret + "aa55";
                                list.add(a);
                            } else if (strlength.length() == 3) {
                                String data = "0501" + "000000800" + strlength + strhex1.substring(i * 1024, 1024 * (i + 1));
                                String ret = strhex(data);
                                String a = "55aa0501000000800" + strlength + strhex1.substring(i * 1024, 1024 * (i + 1)) + ret + "aa55";
                                list.add(a);
                            }
                        }
                    }
                    String strlength4 = strlength(strhex2);
                    String data4 = "0501" + "0000000100" + strlength4 + strhex2;
                    String ret4 = strhex(data4);
                    String a4 = "55aa05010000000100" + strlength4 + strhex2 + ret4 + "aa55";
                    list.add(a4);
                }
                String strlength3 = Utils.strlength(len +scriptPubKey);
                if(String.valueOf(ffcount).equals(String.valueOf(w))) {
                    String data3 = "0501" + "0000008200" + strlength3 + len + scriptPubKey;
                    String ret3 = strhex(data3);
                    String scriptPubKey1 = "55aa05010000008200" + strlength3 + len + scriptPubKey + ret3 + "aa55";
                    list.add(scriptPubKey1);
                }else{
                    String data3 = "0501" + "0000000200" + strlength3 + len + scriptPubKey;
                    String ret3 = strhex(data3);
                    String scriptPubKey1 = "55aa05010000000200" + strlength3 + len + scriptPubKey + ret3 + "aa55";
                    list.add(scriptPubKey1);
                }
                Data.setbtcsignerror("no");
                int i = 0;
                for (int j=0;j<j+1;j++){
                    if(Data.getbtcsign()||i==0|| Data.getreturnbledata().equals("yes")) {
                        if(list.size()!=0) {
                            for (; i < list.size(); ) {
                                Data.setbtcsign(false);
                                i++;
                                if (i == list.size()) {
                                    Data.setreturnbledata("yes");
                                } else {
                                    Data.setreturnbledata("no");
                                }
                                sendble(list.get(i-1), Data.getmService());
                                break;
                            }
                        }
                    }
                    if(i==list.size()){
                        Data.setbtcsign(true);
                        break;
                    }
                }
            } else {
                strhex1 = s.substring(0, s.indexOf("ffffffff"));
                String strhex3 = strhex1.substring(0, strhex1.length() - 2);
                Data.setstrhex1(strhex3);
                strhex2 = s.substring(s.indexOf("ffffffff") + 1);
                Data.setstrhex2(strhex2);
                scriptPubKey= Data.getscriptPubKey1();
                byte[] bytes3 = new byte[scriptPubKey.length() / 2];
                for (int i = 0; i < scriptPubKey.length() / 2; i++) {//16进制字符串转byte[]
                    String subStr = scriptPubKey.substring(i * 2, i * 2 + 2);
                    bytes3[i] = (byte) Integer.parseInt(subStr, 16);
                }
                String len = Integer.toHexString(Integer.parseInt(String.valueOf(bytes3.length)));
                List<String> list = new ArrayList<>();
                String strlength = Utils.strlength(strhex1 +"ffffffff");
                String data1 = "0501" + "0000008000"+strlength+ strhex1 +"ffffffff";
                String ret1=strhex(data1);
                String txin = "55aa05010000008000"+strlength + strhex1 +"ffffffff" + ret1 + "aa55";
                list.add(txin);
                String strlength2 = Utils.strlength(strhex2.substring(7,strhex2.length()));
                String data2 = "0501" + "0000000100"+strlength2+ strhex2.substring(7,strhex2.length());
                String ret2=strhex(data2);
                String txout = "55aa05010000000100"+strlength2 + strhex2.substring(7,strhex2.length()) + ret2 + "aa55";
                list.add(txout);
                String strlength3 = Utils.strlength(len +scriptPubKey);
                String data3 = "0501" + "0000008200"+strlength3+ len +scriptPubKey;
                String ret3=strhex(data3);
                String scriptPubKey1 = "55aa05010000008200" + strlength3+ len + scriptPubKey + ret3 + "aa55";
                list.add(scriptPubKey1);
                Data.setbtcsignerror("no");
                int i = 0;
                for(int j=0;j<j+1;j++) {
                    if(Data.getbtcsign()||i==0) {
                        for (;i < list.size(); ) {
                            sendble(list.get(i), Data.getmService());
                            Data.setbtcsign(false);
                            i++;
                            break;
                        }
                    }
                    if(i==3){
                        Data.setbtcsign(true);
                        break;
                    }
                    if(Data.getbtcsignerror().equals("yes")){
                        break;
                    }
                }
            }
        }else if(Data.getbizhong().equals("ETH")){//以太坊数据签名
            String strlength = strlength(s);
            String data1 = "0501" + "0100000000" + strlength + s;
            String ret=strhex(data1);
            String a = "55aa05010100000000" + strlength + s + ret + "aa55";
            sendble(a, Data.getmService());
        }else if(Data.getbizhong().equals("ERC20")) {
            String a = "";
            if (Data.getauth0sign().equals("login")) {
                byte[] sb = s.getBytes();
                final StringBuilder stringBuilder = new StringBuilder();
                for (byte byteChar : sb)
                    stringBuilder.append(String.format("%02X", byteChar));
                LogCook.d("", stringBuilder.toString());
                String strlength = strlength(stringBuilder.toString());
                String data1 = "8a0100000000000" + strlength + stringBuilder.toString();
                String ret = strhex(data1);
                a = "55aa8a0100000000000" + strlength + stringBuilder.toString() + ret + "aa55";
            } else if (Data.getauth0sign().equals("register") || Data.getauth0sign().equals("login_register")) {
                String strlength = strlength(s);
                String data1 = "0501" + "0101000000" + strlength + s;
                String ret = strhex(data1);
                a = "55aa05010101000000" + strlength + s + ret + "aa55";
            }
            sendble(a, Data.getmService());
        }else if(Data.getbizhong().equals("Hier")){
            String strlength = strlength(s);
            String data1 = "0501" + "0101000000" + strlength + s;
            String ret = strhex(data1);
            String a = "55aa05010101000000" + strlength + s + ret + "aa55";
            sendble(a, Data.getmService());
        }else if(Data.getbizhong().equals("Pawn")){
            String strlength = strlength(s);
            if(Data.gettype().equals("tzactivity")) {
                String data1 = "0501" + "010100000" + strlength + s;
                String ret = strhex(data1);
                String a = "55aa0501010100000" + strlength + s + ret + "aa55";
                sendble(a, Data.getmService());
            }else if(Data.gettype().equals("cdactivity")) {
                String data1 = "0501" + "010200000" + strlength + s;
                String ret = strhex(data1);
                String a = "55aa0501010200000" + strlength + s + ret + "aa55";
                sendble(a, Data.getmService());
            }else if(Data.gettype().equals("tztypeactivity")) {
                String data1 = "0501" + "0103000000" + strlength + s;
                String ret = strhex(data1);
                String a = "55aa05010103000000" + strlength + s + ret + "aa55";
                sendble(a, Data.getmService());
            }
        }else if(Data.getbizhong().equals("XRP")||Data.getbizhong().equals("AED")||Data.getbizhong().equals("trustset")){
            String strlength = strlength(s);
            String data1 = "0501" + "0300000000" + strlength + s;
            String ret = strhex(data1);
            String a = "55aa05010300000000" + strlength + s + ret + "aa55";
            sendble(a, Data.getmService());
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

    class OnClickLintener implements View.OnClickListener {//切换币种
        @Override
        public void onClick(View v) {
            int i = v.getId();
            if (i == R.id.layout_btc) {
                Data.setbizhong("BTC");
                popadd.setText("BTC");
                popadd1.setText("BTC");
                popadd2.setText("BTC");
                popadd3.setText("BTC");
                popadd3.setVisibility(View.VISIBLE);
                fee.setVisibility(View.VISIBLE);
                fee1.setVisibility(View.VISIBLE);
                popWinShare.dismiss();
                balance.setText(Data.getbtcbalance());
            } else if (i == R.id.layout_eth) {
                Data.setbizhong("ETH");
                popadd.setText("ETH");
                popadd1.setText("ETH");
                popadd2.setText("ETH");
                popadd3.setText("Wei");
                popadd3.setVisibility(View.VISIBLE);
                fee.setVisibility(View.VISIBLE);
                fee1.setVisibility(View.VISIBLE);
                popWinShare.dismiss();
                if (Data.getethbalance() == null) {
                    balance.setText("0.00000000");
                } else {
                    balance.setText(Data.getethbalance());
                }
            } else if (i == R.id.layout_xrp) {
                Data.setbizhong("XRP");
                popadd.setText("XRP");
                popadd1.setText("XRP");
                popadd2.setText("drops");
                popadd3.setVisibility(View.GONE);
                fee.setVisibility(View.GONE);
                fee1.setVisibility(View.GONE);
                popWinShare.dismiss();
                balance.setText(Data.getxrpamount());
            } else if (i == R.id.layout_aed) {
                Data.setbizhong("AED");
                popadd.setText("AED");
                popadd1.setText("AED");
                popadd2.setText("AED");
                popadd3.setVisibility(View.GONE);
                fee.setVisibility(View.GONE);
                fee1.setVisibility(View.GONE);
                popWinShare.dismiss();
                balance.setText(Data.getxrpamount());
            }
        }
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocalManageUtil.setLocal(newBase));
    }
}
