package com.wallet.cold.app.index;

import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.method.NumberKeyListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.WriterException;
import com.wallet.R;
import com.wallet.cold.app.main.IndexActivity;
import com.wallet.cold.utils.Data;
import com.wallet.cold.utils.LocalManageUtil;
import com.wallet.cold.utils.PopWinShare1;
import com.wallet.cold.utils.Utils;
import com.wallet.cold.utils.WeiboDialogUtils;

import java.util.HashMap;
import java.util.Map;

public class Receivables extends AppCompatActivity implements View.OnClickListener {
    private PopWinShare1 popWinShare;
    private TextView popadd,popadd1;
    private TextView key;
    private TextView copy,fhf2;
    private ImageView share,fanhui,xiala;
    private Dialog mWeiboDialog;
    private ImageView imgCode;
    private EditText skje;
    public static final String APP_ID = "wx5e85422500011114";
    public static final String APP_SECRET = "2cf4715572d0eee8410cde8f4775aaf8";
    public final String webUrl="www.haibocloud.com";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment2);
        Data.setsaoma("no");
        popadd = (TextView) findViewById(R.id.popadd);
        popadd.setOnClickListener(this);popadd1 = (TextView) findViewById(R.id.popadd1);
        popadd1.setOnClickListener(this);
        key = (TextView) findViewById(R.id.key);
        skje = (EditText) findViewById(R.id.skje);
        skje.setKeyListener(new NumberKeyListener() {
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
        copy=(TextView)findViewById(R.id.copy);
        copy.setOnClickListener(this);
        share=(ImageView)findViewById(R.id.share);
        share.setOnClickListener(this);
        fanhui=(ImageView)findViewById(R.id.fanhui2);
        fanhui.setOnClickListener(this);
        fhf2=(TextView) findViewById(R.id.fhf2);
        fhf2.setOnClickListener(this);
        xiala=(ImageView)findViewById(R.id.xiala2);
        xiala.setOnClickListener(this);
        if(Data.getapptype().equals("cold")){
            key.setText(Data.getbtcaddress());
        }else{
            key.setText(Data.gethotbtcaddress());
        }
        imgCode = (ImageView) findViewById(R.id.img_code);
        if(Data.getbizhong().equals("BTC")) {
            popadd.setText("BTC");
            popadd1.setText("BTC");
            if(Data.getapptype().equals("cold")){
                key.setText(Data.getbtcaddress());
            }else{
                key.setText(Data.gethotbtcaddress());
            }
            imgCode.setImageBitmap(Data.getimgCode());
        }else if(Data.getbizhong().equals("ETH")) {
            popadd.setText("ETH");
            popadd1.setText("ETH");
            if(Data.getapptype().equals("cold")){
                key.setText("0x"+Data.getethaddress());
            }else{
                key.setText("0x"+Data.gethotethaddress());
            }
            imgCode.setImageBitmap(Data.getethimgCode());
        }else if(Data.getbizhong().equals("XRP")) {
            popadd.setText("XRP");
            popadd1.setText("XRP");
            key.setText(Data.getxrpaddress());
            imgCode.setImageBitmap(Data.getxrpimgCode());
        }
        Data.setcontext(Receivables.this);
        Data.settype("fragment2");
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.popadd) {
            if (popWinShare == null) {
                //自定义的单击事件
                OnClickLintener paramOnClickListener = new OnClickLintener();
                popWinShare = new PopWinShare1(Receivables.this, paramOnClickListener);
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
        if(v.getId() == R.id.xiala2) {
            if (popWinShare == null) {
                //自定义的单击事件
                OnClickLintener paramOnClickListener = new OnClickLintener();
                popWinShare = new PopWinShare1(Receivables.this, paramOnClickListener);
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
        if(v.getId() == R.id.copy) {
            ClipboardManager clip = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            //clip.getText(); // 粘贴
            if (key.getText().toString() == "" || key.getText().toString() == null) {
                Toast.makeText(getApplicationContext(), this.getResources().getString(R.string.ff7), Toast.LENGTH_SHORT).show();
            } else {
                clip.setText(key.getText().toString()); // 复制
                Toast.makeText(getApplicationContext(), this.getResources().getString(R.string.ff8), Toast.LENGTH_SHORT).show();
            }
        }
        if(v.getId() == R.id.share) {
            Map<String,String> map=new HashMap<>();
            String address="";
            if(skje.getText().toString().equals("")){
                if (popadd.getText().toString().equals("ETH")) {
                    if(Data.getapptype().equals("cold")){
                        address = Data.getethaddress();
                    }else{
                        address = Data.gethotethaddress();
                    }
                } else if (popadd.getText().toString().equals("BTC")) {
                    if(Data.getapptype().equals("cold")){
                        address = Data.getbtcaddress();
                    }else{
                        address = Data.gethotbtcaddress();
                    }
                } else if (popadd.getText().toString().equals("XRP")) {
                    address = Data.getxrpaddress();
                }
                map.put("address",address);
            }else {
                if (popadd.getText().toString().equals("ETH")) {
                    if(Data.getapptype().equals("cold")){
                        map.put("address", Data.getethaddress());
                    }else{
                        map.put("address", Data.gethotethaddress());
                    }

                } else if (popadd.getText().toString().equals("BTC")) {
                    if(Data.getapptype().equals("cold")){
                        map.put("address", Data.getbtcaddress());
                    }else{
                        map.put("address", Data.gethotbtcaddress());
                    }
                } else if (popadd.getText().toString().equals("XRP")) {
                    map.put("address", Data.getxrpaddress());
                }
                map.put("amount", skje.getText().toString());
                address = String.valueOf(map).substring(1, String.valueOf(map).length() - 1);
            }
            Bitmap codeBitmap = null;
            if (!TextUtils.isEmpty(address)) {
                try {
                    codeBitmap = Utils.createCode(address);
                    Data.setshareimgCode(codeBitmap);
                } catch (WriterException e) {
                    e.printStackTrace();
                }
            }
            View view = LayoutInflater.from(Receivables.this).inflate(R.layout.bottom_share, null);
            final Dialog dialog = new Dialog(this, R.style.common_dialog);
            dialog.setContentView(view);
            dialog.setCancelable(true);
            dialog.show();
            // 监听
            View.OnClickListener listener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(v.getId() == R.id.view_share_weixin) {
                            // 分享到微信
                            Utils.shareWeb(Data.getcontext(),APP_ID,webUrl,"HotWallet",map.get("address"), Data.getshareimgCode(),0);
                        }
                        if(v.getId() == R.id.view_share_pengyou) {
                            // 分享到朋友圈
                            Utils.shareWeb(Data.getcontext(),APP_ID,webUrl,"HotWallet",map.get("address"), Data.getshareimgCode(),1);
                        }
                        if(v.getId() == R.id.share_cancel_btn) {

                        }
                        dialog.dismiss();
                    }
                };
                ViewGroup mViewWeixin = (ViewGroup) view.findViewById(R.id.view_share_weixin);
                ViewGroup mViewPengyou = (ViewGroup) view.findViewById(R.id.view_share_pengyou);
                ViewGroup mViewQQ = (ViewGroup) view.findViewById(R.id.view_share_qq);
                TextView mBtnCancel = (TextView) view.findViewById(R.id.share_cancel_btn);
                ImageView share_erweima = (ImageView) view.findViewById(R.id.share_erweima);
                share_erweima.setImageBitmap(codeBitmap);
                TextView shareaddress = (TextView) view.findViewById(R.id.shareaddress);
                shareaddress.setText(key.getText().toString());
                mViewWeixin.setOnClickListener(listener);
                mViewPengyou.setOnClickListener(listener);
                mViewQQ.setOnClickListener(listener);
                mBtnCancel.setOnClickListener(listener);
                // 设置相关位置，一定要在 show()之后
                Window window = dialog.getWindow();
                window.getDecorView().setPadding(0, 0, 0, 0);
                WindowManager.LayoutParams params = window.getAttributes();
                params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                params.gravity = Gravity.BOTTOM;
                window.setAttributes(params);
        }
        if(v.getId() == R.id.fhf2) {
            Intent intent = new Intent(Data.getcontext(), IndexActivity.class);
            Data.getcontext().startActivity(intent);
        }
        if(v.getId() == R.id.fanhui2) {
            Intent intent = new Intent(Data.getcontext(), IndexActivity.class);
            Data.getcontext().startActivity(intent);
        }
    }
    class OnClickLintener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Data.settype("fragment2");
            mWeiboDialog = WeiboDialogUtils.createLoadingDialog(Receivables.this, Data.getcontext().getResources().getString(R.string.ff9));
            if(v.getId() == R.id.layout_btc) {
                popadd.setText("BTC");
                popadd1.setText("BTC");
                Data.setbizhong("BTC");
                popWinShare.dismiss();
                if(Data.getapptype().equals("cold")){
                    key.setText(Data.getbtcaddress());
                }else{
                    key.setText(Data.gethotbtcaddress());
                }
                imgCode.setImageBitmap(Data.getimgCode());
                WeiboDialogUtils.closeDialog(mWeiboDialog);
            }
            if(v.getId() == R.id.layout_eth) {
                popadd.setText("ETH");
                popadd1.setText("ETH");
                Data.setbizhong("ETH");
                popWinShare.dismiss();
                if(Data.getapptype().equals("cold")){
                    key.setText("0x"+Data.getethaddress());
                }else{
                    key.setText("0x"+Data.gethotethaddress());
                }
                imgCode.setImageBitmap(Data.getethimgCode());
                WeiboDialogUtils.closeDialog(mWeiboDialog);
            }
            if(v.getId() == R.id.layout_xrp) {
                popadd.setText("XRP");
                popadd1.setText("drops");
                Data.setbizhong("XRP");
                popWinShare.dismiss();
                key.setText(Data.getxrpaddress());
                imgCode.setImageBitmap(Data.getxrpimgCode());
                WeiboDialogUtils.closeDialog(mWeiboDialog);
            }
        }
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocalManageUtil.setLocal(newBase));
    }
}
