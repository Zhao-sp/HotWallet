package com.wallet.hot.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.text.method.NumberKeyListener;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.wallet.R;
import com.wallet.cold.app.main.IndexActivity;
import com.wallet.hot.utils.eth.ETHCredentials;
import com.wallet.hot.utils.eth.ETHTransactionEncoder;
import com.wallet.utils.Sweepcode.CaptureActivity;
import com.wallet.cold.utils.Data;
import com.wallet.utils.language.LocalManageUtil;
import com.wallet.utils.LogCook;
import com.wallet.cold.utils.PopWinShare1;
import com.wallet.cold.utils.Utils;
import com.wallet.cold.utils.Utilshttp;
import com.wallet.utils.WeiboDialogUtils;

import org.bitcoinj.core.DumpedPrivateKey;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.Sha256Hash;
import org.bitcoinj.params.TestNet3Params;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.SignatureException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.wallet.cold.utils.Utils.ETHAddressValidate;
import static com.wallet.cold.utils.Utils.getIndex;
import static com.wallet.cold.utils.Utils.getSubCount_2;
import static com.wallet.cold.utils.Utils.isBTCValidAddress;

public class HotTransfer extends Activity implements View.OnClickListener {
    private PopWinShare1 popWinShare;
    private TextView popadd, popadd1, popadd2, popadd3,fee1,balance,fee;
    private EditText to,limit1,amountyue;
    private ImageView saoma, fanhui,tuzi,gui;
    private TextView xl, fhf3;
    private Dialog mWeiboDialog;
    private Button commit;
    private String scriptPubKey,strhex1,strhex2;
    private boolean uxto = false;
    private String result1;
    private SeekBar mSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transfer);
        Data.setn(0);
        Data.setcount(0);
        Data.setsaoma("yes");
        mSeekBar = (SeekBar) findViewById(R.id.seekbar);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //滑动中的监听
                if(popadd.getText().toString().equals("BTC")) {
                    float value = progress / 10000f;
                    if (progress == 0) {
                        fee.setText("0.0001");
                    }else {
                        fee.setText(String.valueOf(value));
                    }
                }else if(popadd.getText().toString().equals("ETH")) {
                    fee.setText(String.valueOf(progress));
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //滑动后的事件
            }
        });
        tuzi = (ImageView) findViewById(R.id.tuzi);
        gui = (ImageView) findViewById(R.id.gui);
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
        balance = (TextView) findViewById(R.id.USD);
        fhf3 = (TextView) findViewById(R.id.fhf3);
        fee1 = (TextView) findViewById(R.id.fee1);
        fhf3.setOnClickListener(this);
        Data.setbalance(balance);
        commit = (Button) findViewById(R.id.verify2);
        commit.setOnClickListener(this);
        to = (EditText) findViewById(R.id.to);
        limit1 = (EditText) findViewById(R.id.limit1);
        fee = (TextView) findViewById(R.id.fee);
        amountyue = (EditText) findViewById(R.id.btcyue);
        amountyue.setKeyListener(new NumberKeyListener() {
            @Override
            protected char[] getAcceptedChars() {
                char[] numberChars = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '.'};
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
            if (result.contains("=") && getSubCount_2(result, "=") == 2) {
                List<String> list = Arrays.asList(result.split(","));
                List<String> list1 = Arrays.asList(list.get(0).split("="));
                List<String> list2 = Arrays.asList(list.get(1).split("="));
                if (list1.contains("address")) {
                    to.setText(list1.get(1));
                    amountyue.setText(list2.get(1));
                } else {
                    to.setText(list2.get(1));
                    amountyue.setText(list1.get(1));
                }
            } else {
                to.setText(result);
            }
        }
        if(to.getText().toString().length()==34) {
            Data.setbizhong("XRP");
            popadd.setText("XRP");
            popadd1.setText("XRP");
            popadd2.setText("drops");
            popadd3.setVisibility(View.GONE);
            fee.setVisibility(View.GONE);
            fee1.setVisibility(View.GONE);
            tuzi.setVisibility(View.GONE);
            gui.setVisibility(View.GONE);
            mSeekBar.setVisibility(View.GONE);
            balance.setText(Data.getxrpamount());
        }else if (to.getText().toString().length() == 40) {
            popadd.setText("ETH");
            popadd1.setText("ETH");
            popadd2.setText("ETH");
            popadd3.setText("Wei");
            mSeekBar.setMax(9000);
            fee.setText("1");
            balance.setText(Data.getethbalance());
        } else {
            popadd.setText("BTC");
            popadd1.setText("BTC");
            popadd2.setText("BTC");
            popadd3.setText("BTC");
            fee.setText("0.0001");
            balance.setText(Data.getbtcbalance());
        }
        Data.settype("hottransfer");
        Data.setcontext(HotTransfer.this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.popadd2) {
            if (popWinShare == null) {
                //自定义的单击事件
                HotTransfer.OnClickLintener paramOnClickListener = new HotTransfer.OnClickLintener();
                popWinShare = new PopWinShare1(HotTransfer.this, paramOnClickListener);
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
        if (v.getId() == R.id.xl) {
            if (popWinShare == null) {
                //自定义的单击事件
                HotTransfer.OnClickLintener paramOnClickListener = new HotTransfer.OnClickLintener();
                popWinShare = new PopWinShare1(HotTransfer.this, paramOnClickListener);
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
        if (v.getId() == R.id.saoma) {
            startActivity(new Intent(HotTransfer.this, CaptureActivity.class));
        }
        if (v.getId() == R.id.fanhui3) {
            startActivity(new Intent(HotTransfer.this, IndexActivity.class));
        }
        if (v.getId() == R.id.fhf3) {
            startActivity(new Intent(HotTransfer.this, IndexActivity.class));
        }
        if (v.getId() == R.id.verify2) {
            if (popadd.getText().toString().equals("BTC")) {
                trade(popadd.getText().toString(), limit1.getText().toString(), to.getText().toString(), amountyue.getText().toString(), fee.getText().toString()
                        , balance.getText().toString(), Data.getbtcaddress());
            } else if (popadd.getText().toString().equals("ETH")) {
                trade(popadd.getText().toString(), limit1.getText().toString(), to.getText().toString(), amountyue.getText().toString(), fee.getText().toString()
                        , balance.getText().toString(), Data.getethaddress());
            } else if (popadd.getText().toString().equals("XRP")||popadd.getText().toString().equals("AED")) {
                trade(popadd.getText().toString(), limit1.getText().toString(), to.getText().toString(), amountyue.getText().toString(), ""
                        , balance.getText().toString(), Data.getxrpaddress());
            }
        }
    }

    public void trade(String type, String pin, String to, String amountyue, String fee, String balance, String address) {
        if (pin.length() < 8 || !pin.equals(Data.gethotpassword())) {
            Toast.makeText(getApplicationContext(), "请输入正确的钱包密码", Toast.LENGTH_SHORT).show();
        } else {
            Double a=Double.parseDouble(balance);
            Double b=Double.parseDouble(amountyue);
            if (type.equals("BTC")) {
                if (to.equals("")) {
                    Toast.makeText(getApplicationContext(), this.getResources().getString(R.string.fff14), Toast.LENGTH_SHORT).show();
                    return;
                } else if (amountyue.equals("")) {
                    Toast.makeText(getApplicationContext(), this.getResources().getString(R.string.fff15), Toast.LENGTH_SHORT).show();
                    return;
                } else if (fee.compareTo("0.0001") < 0) {
                    Toast.makeText(getApplicationContext(), this.getResources().getString(R.string.fff16), Toast.LENGTH_SHORT).show();
                    return;
                } else if (address.equals(to)) {
                    Toast.makeText(getApplicationContext(), this.getResources().getString(R.string.fff17), Toast.LENGTH_SHORT).show();
                    return;
                }
                Double a2 = Double.valueOf(fee);
                Double a1 = Double.valueOf(amountyue);;
                Double a3 = a1 + a2;
                BigDecimal bd3 = new BigDecimal(a3).setScale(8, BigDecimal.ROUND_DOWN);
                if (a.compareTo(Double.parseDouble(String.valueOf(bd3))) < 0) {
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
                    } else if (!isBTCValidAddress(to)) {
                        Toast.makeText(getApplicationContext(), this.getResources().getString(R.string.fff22), Toast.LENGTH_SHORT).show();
                    } else {
                        mWeiboDialog = WeiboDialogUtils.createLoadingDialog(HotTransfer.this, this.getResources().getString(R.string.fff231));
                        Data.setdialog(mWeiboDialog);
                        Data.setfee(fee);
                        Data.setyue(amountyue);
                        Data.setto(to);
                        Data.setbizhong("BTC");
                        btcchushihua();
                    }
                }
            } else if (type.equals("ETH")) {
                if (!ETHAddressValidate(to)) {
                    Toast.makeText(getApplicationContext(), this.getResources().getString(R.string.fff25), Toast.LENGTH_SHORT).show();
                } else if (amountyue.equals("")) {
                    Toast.makeText(getApplicationContext(), this.getResources().getString(R.string.fff15), Toast.LENGTH_SHORT).show();
                } else if (fee.equals("")) {
                    Toast.makeText(getApplicationContext(), this.getResources().getString(R.string.fff26), Toast.LENGTH_SHORT).show();
                } else if (limit1.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), this.getResources().getString(R.string.fff27), Toast.LENGTH_SHORT).show();
                } else if (address.equals(to)) {
                    Toast.makeText(getApplicationContext(), this.getResources().getString(R.string.fff17), Toast.LENGTH_SHORT).show();
                } else if (fee.compareTo("9000") >= 0) {
                    Toast.makeText(getApplicationContext(), this.getResources().getString(R.string.fff28), Toast.LENGTH_SHORT).show();
                } else if (a.compareTo(b) < 0) {
                    Toast.makeText(getApplicationContext(), this.getResources().getString(R.string.fff18), Toast.LENGTH_SHORT).show();
                } else {
                    mWeiboDialog = WeiboDialogUtils.createLoadingDialog(this, this.getResources().getString(R.string.fff231));
                    Data.setdialog(mWeiboDialog);
                    Data.setfee(fee);
                    Data.setyue(amountyue);
                    Data.setto(to);
                    Data.setlimit(pin);
                    Data.setbizhong("ETH");
                    Data.setethtype("dealid");
                    eth();
                }
            } else if (type.equals("XRP")||type.equals("AED")) {
                if (!to.substring(0,1).equals("r")) {
                    Toast.makeText(getApplicationContext(), this.getResources().getString(R.string.fff25), Toast.LENGTH_SHORT).show();
                } else if (amountyue.equals("")) {
                    Toast.makeText(getApplicationContext(), this.getResources().getString(R.string.fff15), Toast.LENGTH_SHORT).show();
                } else if (address.equals(to)) {
                    Toast.makeText(getApplicationContext(), this.getResources().getString(R.string.fff17), Toast.LENGTH_SHORT).show();
                } else if (a.compareTo(b) < 0) {
                    Toast.makeText(getApplicationContext(), this.getResources().getString(R.string.fff18), Toast.LENGTH_SHORT).show();
                } else {
                    mWeiboDialog = WeiboDialogUtils.createLoadingDialog(this, this.getResources().getString(R.string.fff231));
                    Data.setdialog(mWeiboDialog);
                    Data.setyue(amountyue);
                    Data.setto(to);
                    Data.setlimit(pin);
                    if(type.equals("XRP")) {
                        Data.setbizhong("XRP");
                    }else{
                        Data.setbizhong("AED");
                    }
                    new Utilshttp().getxrpamount();
                }
            }
        }
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
                                DecimalFormat df = new DecimalFormat("0.00000000");
                                String b1=df.format(new BigDecimal(Double.parseDouble(Data.getamount().get(i))));
                                String b2=df.format(new BigDecimal(Double.parseDouble(Data.getyue())));
                                BigDecimal bd = new BigDecimal(b1).subtract(new BigDecimal(b2));//余额减去转账
                                Double a2 = Double.valueOf(Data.getfee());
                                BigDecimal bd2 = new BigDecimal(a2).setScale(8, BigDecimal.ROUND_DOWN);//服务费
                                BigDecimal bd1=bd.subtract(bd2);//余额-转账-服务费
                                Double a3 = Double.valueOf(Data.getyue()) + Double.valueOf(Data.getfee());
                                BigDecimal bd3 = new BigDecimal(a3).setScale(8, BigDecimal.ROUND_DOWN);//转账加服务费
                                if (((bd1.compareTo(new BigDecimal(0)))<0 || bd.toString().equals("0E-8")) && !Data.getamount().get(i).equals("0.0")) {
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
                            sign(result1);
                        } else {
                            String error = btcerror;
                            Looper.prepare();
                            Toast.makeText(Data.getcontext(), Data.getcontext().getResources().getString(R.string.fff29) + error, Toast.LENGTH_SHORT).show();
                            WeiboDialogUtils.closeDialog(Data.getdialog());
                            Data.setbletype("1");
                            Looper.loop();
                        }
                    } catch (Throwable e) {
                        Looper.prepare();
                        Toast.makeText(Data.getcontext(), Data.getcontext().getResources().getString(R.string.fff30), Toast.LENGTH_SHORT).show();
                        WeiboDialogUtils.closeDialog(Data.getdialog());
                        Data.setbletype("1");
                        Looper.loop();
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    /**
     * 数据签名
     * @param s
     */
    @SuppressLint("NewApi")
    public void sign(String s){
        Data.setsign("signing");
        if (Data.getuxto()) {
            int count = getSubCount_2(s, "ffffffff");
            for (int q = 0; q < count; q++) {
                int j=q+1;
                LogCook.d("第"+ j +"个txid","共"+count+"个txid");
                int index = getIndex(s, q + 1, "ffffffff");
                strhex1 = s.substring(0, index - 2);
                strhex2 = s.substring(index, s.length());
                scriptPubKey = Data.getscriptPubKey().get(q);
                byte[] bytes3 = new byte[scriptPubKey.length() / 2];
                for (int i = 0; i < scriptPubKey.length() / 2; i++) {//16进制字符串转byte[]
                    String subStr = scriptPubKey.substring(i * 2, i * 2 + 2);
                    bytes3[i] = (byte) Integer.parseInt(subStr, 16);
                }
                String len = Integer.toHexString(Integer.parseInt(String.valueOf(bytes3.length)));
                String data=strhex1+len+scriptPubKey+strhex2+"01000000";
                LogCook.d("待签名信息",data);
                byte[] bytes = new byte[data.length() / 2];
                for (int i = 0; i < data.length() / 2; i++) {//16进制字符串转byte[]
                    String subStr = data.substring(i * 2, i * 2 + 2);
                    bytes[i] = (byte) Integer.parseInt(subStr, 16);
                }
                String aaa=Signingtrasaction(Data.gethotbtcprv(),bytes);
                sign.add(aaa);
            }
            btc();
        } else {
            strhex1 = s.substring(0, s.indexOf("ffffffff"));
            String strhex3 = strhex1.substring(0, strhex1.length() - 2);
            Data.setstrhex1(strhex3);
            strhex2 = s.substring(s.indexOf("ffffffff") + 1);
            Data.setstrhex2(strhex2);
            scriptPubKey=Data.getscriptPubKey1();
            byte[] bytes3 = new byte[scriptPubKey.length() / 2];
            for (int i = 0; i < scriptPubKey.length() / 2; i++) {//16进制字符串转byte[]
                String subStr = scriptPubKey.substring(i * 2, i * 2 + 2);
                bytes3[i] = (byte) Integer.parseInt(subStr, 16);
            }
            String len = Integer.toHexString(Integer.parseInt(String.valueOf(bytes3.length)));
            String data=Data.getstrhex1() + len + scriptPubKey + "f" + Data.getstrhex2()+"01000000";
            LogCook.d("待签名信息",data);
            byte[] bytes = new byte[data.length() / 2];
            for (int i = 0; i < data.length() / 2; i++) {//16进制字符串转byte[]
                String subStr = data.substring(i * 2, i * 2 + 2);
                bytes[i] = (byte) Integer.parseInt(subStr, 16);
            }
            String aaa=Signingtrasaction(Data.gethotbtcprv(),bytes);
            Data.setresultdata(aaa);
            btc();
        }
    }

    /**
     * btc公钥验签
     * @param msg
     * @param signatureMsg
     * @param pubkey
     * @return
     */
    public static boolean verifyMessage(String msg, String signatureMsg, String pubkey) {
        boolean result = false;
        ECKey ecKey = ECKey.fromPublicOnly(org.bitcoinj.core.Utils.HEX.decode(pubkey));
        try {
            ecKey.verifyMessage(msg, signatureMsg);
            result = true;
        } catch (SignatureException e) {
            result = false;
            e.printStackTrace();
        } finally {
            return result;
        }

    }

    /**
     * btc压缩私钥签名
     * @param wif
     * @param msg
     * @return
     */
    public String Signingtrasaction(String wif, byte[] msg) {
        String hex="";
        try {
            // creating a key object from WiF
            DumpedPrivateKey dpk = DumpedPrivateKey.fromBase58(TestNet3Params.get(), wif);
            ECKey key = dpk.getKey();
            // checking our key object
            // NetworkParameters main = MainNetParams.get();
//            String check = key.getPrivateKeyAsWiF(TestNet3Params.get());
//            System.out.println(wif.equals(check));  // true
//            Log.e("wif check", String.valueOf(wif.equals(check)));
            // creating Sha object from string
            Sha256Hash hash = Sha256Hash.twiceOf(msg);
            // creating signature
            ECKey.ECDSASignature sig = key.sign(hash);
            com.wallet.hot.utils.btc.DumpedPrivateKey dumpedPrivateKey= com.wallet.hot.utils.btc.DumpedPrivateKey.fromBase58(TestNet3Params.get(),wif);
            com.wallet.hot.utils.btc.ECKey key1=dumpedPrivateKey.getKey();
            com.wallet.hot.utils.btc.ECKey.ECDSASignature ecdsaSignature=key1.sign(hash);
            // encoding
            byte[] res = ecdsaSignature.encodeToDER();
            // converting to hex
            hex = Base64.encodeToString(res, 16);
//            Log.e("sigendTransiction", hex);
            LogCook.d("签名结果:",Utils.bytesToHexString(res));
            return Utils.bytesToHexString(res);
            //Log.e("decrypttx",""+ Hex.decode(sig.encodeToDER()));
        } catch (Exception e) {
            Log.e("signing exception", e.getMessage().toString());
        }
        return hex;
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
                    LogCook.d("ble返回btc多个txid签名结果", sign.toString());
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
                        String a = sign.get(q) + Data.gethotbtcpub();
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
                        byte[] bytes2 = new byte[Data.gethotbtcpub().length() / 2];
                        for (int i = 0; i < Data.gethotbtcpub().length() / 2; i++) {//16进制字符串转byte[]
                            String subStr = Data.gethotbtcpub().substring(i * 2, i * 2 + 2);
                            bytes2[i] = (byte) Integer.parseInt(subStr, 16);
                        }
                        String len2 = Integer.toHexString(Integer.parseInt(String.valueOf(bytes2.length)));//公钥长度
                        if (q == 0) {
                            data = strhex1 + len + len1 + sign.get(q) + "01" + len2 + Data.gethotbtcpub();
                        } else if (q == count - 1) {
                            data = data + strhex1 + len + len1 + sign.get(q) + "01" + len2 + Data.gethotbtcpub() + strhex2;
                        } else {
                            data = data + strhex1 + len + len1 + sign.get(q) + "01" + len2 + Data.gethotbtcpub();
                        }
                    }
                    LogCook.d("发送交易数据", data);
                    Data.setbtctype("sendrawtransaction");sign.clear();
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
                    String a = data2 + Data.gethotbtcpub();
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
                    byte[] bytes2 = new byte[Data.gethotbtcpub().length() / 2];
                    for (int i = 0; i < Data.gethotbtcpub().length() / 2; i++) {//16进制字符串转byte[]
                        String subStr = Data.gethotbtcpub().substring(i * 2, i * 2 + 2);
                        bytes2[i] = (byte) Integer.parseInt(subStr, 16);
                    }
                    String len2 = Integer.toHexString(Integer.parseInt(String.valueOf(bytes2.length)));//公钥长度
                    String data = Data.getstrhex1() + len + len1 + data2 + "01" + len2 + Data.gethotbtcpub() + "f" + Data.getstrhex2();
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
            String btcJson = "{\"min\":0,\"max\":9999999,\"address\":\""+Data.getbtcaddress()+"\"}";
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
                        BigInteger bi = new BigInteger(dealid,16);
                        BigInteger fee1 = new BigInteger(fee.getText().toString(),16);
                        BigInteger limit1 = new BigInteger("7530",16);
                        try {
                            String sign=signedEthTransactionData(Data.gethotethprv(),to.getText().toString(),bi,fee1,limit1,amountyue.getText().toString());
                            Data.setethtype("sendtransaction");
                            Data.setrlpdata(sign);
                            eth();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        String error = dealid;
                        Looper.prepare();
                        Toast.makeText(getApplicationContext(), Data.getcontext().getResources().getString(R.string.fff34) + error, Toast.LENGTH_SHORT).show();
                        WeiboDialogUtils.closeDialog(Data.getdialog());
                        Looper.loop();
                    }
                } else if (Data.getethtype().equals("sendtransaction")) {//发送交易
                    if(!Data.getrlpdata().equals("")) {
                        String address = "{\"address\":\"" + Data.getrlpdata() + "\"}";
                        String dealid = Utils.getethhttp(address);
                        if (dealid == "") {
                            if(Data.getbizhong().equals("ETH")) {
                                Data.setethtype("balance");
                                String address1 = "{\"address\":\"0x" + Data.getethaddress() + "\"}";
                                String etherror = Utils.getethhttp(address1);
                                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                Date curDate = new Date(System.currentTimeMillis());
                                String str = formatter.format(curDate);
                                if (etherror.contains("success")) {
                                    Looper.prepare();
                                    Data.getbalance().setText(Data.getethbalance());
                                    Toast.makeText(Data.getcontext(), Data.getcontext().getResources().getString(R.string.fff32), Toast.LENGTH_SHORT).show();
                                    WeiboDialogUtils.closeDialog(Data.getdialog());
                                    Looper.loop();
                                } else {
                                    Looper.prepare();
                                    Toast.makeText(Data.getcontext(), Data.getcontext().getResources().getString(R.string.fff36) + etherror, Toast.LENGTH_SHORT).show();
                                    WeiboDialogUtils.closeDialog(Data.getdialog());
                                    Looper.loop();
                                }
                                Data.getdb().execSQL("insert into JiaoyiTb (blename,name,bizhong,jine,riqi,type) values " +
                                        "('" + Data.getdevicename() + "','" + Data.getethaddress() + "','ETH'," + Data.getyue() + ",'" + str + "',1)");
                            }else if(Data.getbizhong().equals("ERC20")){
                                if(Data.getauth0sign().equals("register")) {
                                    Looper.prepare();
                                    Toast.makeText(Data.getcontext(), Data.getcontext().getResources().getString(R.string.fff38), Toast.LENGTH_SHORT).show();
                                    WeiboDialogUtils.closeDialog(Data.getdialog());
                                    Looper.loop();
                                }else if(Data.getauth0sign().equals("login")) {
                                    Looper.prepare();
                                    Toast.makeText(Data.getcontext(), Data.getcontext().getResources().getString(R.string.fff40), Toast.LENGTH_SHORT).show();
                                    WeiboDialogUtils.closeDialog(Data.getdialog());
                                    Looper.loop();
                                }
                            }
                        } else {
                            if (Data.getbizhong().equals("ETH")){
                                Looper.prepare();
                                Toast.makeText(Data.getcontext(), Data.getcontext().getResources().getString(R.string.fff30) + dealid, Toast.LENGTH_SHORT).show();
                                LogCook.d("ETH交易失败", dealid);
                                WeiboDialogUtils.closeDialog(Data.getdialog());
                                Looper.loop();
                            }else if(Data.getbizhong().equals("ERC20")) {
                                if(Data.getauth0sign().equals("register")) {
                                    Looper.prepare();
                                    Toast.makeText(Data.getcontext(), Data.getcontext().getResources().getString(R.string.fff39) + dealid, Toast.LENGTH_SHORT).show();
                                    WeiboDialogUtils.closeDialog(Data.getdialog());
                                    Looper.loop();
                                }else if(Data.getauth0sign().equals("login")) {
                                    Looper.prepare();
                                    Toast.makeText(Data.getcontext(), Data.getcontext().getResources().getString(R.string.fff41), Toast.LENGTH_SHORT).show();
                                    WeiboDialogUtils.closeDialog(Data.getdialog());
                                    Looper.loop();
                                }
                            }
                        }
                    }
                }
            }
        }).start(); // 开启线程
    }

    /**
     * 离线签名eth
     * @param privateKey
     * @param to//转账的钱包地址
     * @param nonce//获取到的交易次数
     * @param gasPrice
     * @param gasLimit
     * @param value  //转账的值
     * @return
     */
    public static String signedEthTransactionData(String privateKey, String to, BigInteger nonce, BigInteger gasPrice, BigInteger gasLimit, String value) throws Exception {
        //把十进制的转换成ETH的Wei, 1ETH = 10^18 Wei
        BigDecimal realValue = Convert.toWei(value, Convert.Unit.ETHER);
        RawTransaction rawTransaction = RawTransaction.createEtherTransaction(nonce, gasPrice, gasLimit, to, realValue.toBigIntegerExact());
        //手续费= (gasPrice * gasLimit ) / 10^18 ether

        Credentials credentials = Credentials.create(privateKey);
        //使用TransactionEncoder对RawTransaction进行签名操作
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
        ETHCredentials credentials1 = ETHCredentials.create(privateKey);
        byte[] signedMessage1 = ETHTransactionEncoder.signMessage(rawTransaction, credentials1);
        //        //转换成0x开头的字符串
        return Numeric.toHexString(signedMessage);
    }

    /**
     * eth代币转账离线签名
     * @param privateKey
     * @param contractAddress//合约地址
     * @param to//转账的钱包地址
     * @param nonce//获取到的交易次数
     * @param gasPrice
     * @param gasLimit
     * @param value   //转账的值
     * @return
     */
    public static String signedEthContractTransactionData(String privateKey, String contractAddress, String to, BigInteger nonce, BigInteger gasPrice, BigInteger gasLimit, Double value, Double decimal) throws Exception {
        //因为每个代币可以规定自己的小数位, 所以实际的转账值=数值 * 10^小数位
        BigDecimal realValue = BigDecimal.valueOf(value * Math.pow(10.0, decimal));

        //0xa9059cbb代表某个代币的转账方法hex(transfer) + 对方的转账地址hex + 转账的值的hex
        String data = "0xa9059cbb" + Numeric.toHexStringNoPrefixZeroPadded(Numeric.toBigInt(to), 64) + Numeric.toHexStringNoPrefixZeroPadded(realValue.toBigInteger(), 64);
        RawTransaction rawTransaction = RawTransaction.createTransaction(nonce, gasPrice, gasLimit, contractAddress, data);
        //手续费= (gasPrice * gasLimit ) / 10^18 ether

        Credentials credentials = Credentials.create(privateKey);
        //使用TransactionEncoder对RawTransaction进行签名操作
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
        //转换成0x开头的字符串
        return Numeric.toHexString(signedMessage);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {//如果返回键按下
            Intent intent = new Intent(this,IndexActivity.class);
            startActivity(intent);
        }
        return false;
    }

    class OnClickLintener implements View.OnClickListener {//切换币种
        @Override
        public void onClick(View v) {
            int i = v.getId();
            if (i == R.id.layout_btc) {
                if(Data.getbledata().contains("BTC")) {
                    Data.setbizhong("BTC");
                    popadd3.setVisibility(View.VISIBLE);
                    fee.setVisibility(View.VISIBLE);
                    fee1.setVisibility(View.VISIBLE);
                    tuzi.setVisibility(View.VISIBLE);
                    gui.setVisibility(View.VISIBLE);
                    mSeekBar.setVisibility(View.VISIBLE);
                    popadd.setText("BTC");
                    popadd1.setText("BTC");
                    popadd2.setText("BTC");
                    popadd3.setText("BTC");
                    fee.setText("0.0001");
                    popWinShare.dismiss();
                    balance.setText(Data.getbtcbalance());
                }else{
                    Toast.makeText(Data.getcontext(), Data.getcontext().getResources().getString(R.string.f511), Toast.LENGTH_SHORT).show();
                }
            } else if (i == R.id.layout_eth) {
                if(Data.getbledata().contains("ETH")) {
                    Data.setbizhong("ETH");
                    popadd3.setVisibility(View.VISIBLE);
                    fee.setVisibility(View.VISIBLE);
                    fee1.setVisibility(View.VISIBLE);
                    tuzi.setVisibility(View.VISIBLE);
                    gui.setVisibility(View.VISIBLE);
                    mSeekBar.setVisibility(View.VISIBLE);
                    popadd.setText("ETH");
                    popadd1.setText("ETH");
                    popadd2.setText("ETH");
                    popadd3.setText("Wei");
                    mSeekBar.setMax(9000);
                    fee.setText("1");
                    popWinShare.dismiss();
                    if (Data.getethbalance() == null) {
                        balance.setText("0.00000000");
                    } else {
                        balance.setText(Data.getethbalance());
                    }
                }else{
                    Toast.makeText(Data.getcontext(), Data.getcontext().getResources().getString(R.string.f511), Toast.LENGTH_SHORT).show();
                }
            } else if (i == R.id.layout_xrp) {
                if(Data.getbledata().contains("XRP")) {
                    Data.setbizhong("XRP");
                    popadd.setText("XRP");
                    popadd1.setText("XRP");
                    popadd2.setText("drops");
                    popadd3.setVisibility(View.GONE);
                    fee.setVisibility(View.GONE);
                    fee1.setVisibility(View.GONE);
                    tuzi.setVisibility(View.GONE);
                    gui.setVisibility(View.GONE);
                    mSeekBar.setVisibility(View.GONE);
                    popWinShare.dismiss();
                    balance.setText(Data.getxrpamount());
                }else{
                    Toast.makeText(Data.getcontext(), Data.getcontext().getResources().getString(R.string.f511), Toast.LENGTH_SHORT).show();
                }
            } else if (i == R.id.layout_aed) {
                if(Data.getbledata().contains("AED")) {
                    Data.setbizhong("AED");
                    popadd.setText("AED");
                    popadd1.setText("AED");
                    popadd2.setText("AED");
                    popadd3.setVisibility(View.GONE);
                    fee.setVisibility(View.GONE);
                    fee1.setVisibility(View.GONE);
                    tuzi.setVisibility(View.GONE);
                    gui.setVisibility(View.GONE);
                    mSeekBar.setVisibility(View.GONE);
                    popWinShare.dismiss();
                    balance.setText(Data.getaedamount());
                }else{
                    Toast.makeText(Data.getcontext(), Data.getcontext().getResources().getString(R.string.f511), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocalManageUtil.setLocal(newBase));
    }
}
