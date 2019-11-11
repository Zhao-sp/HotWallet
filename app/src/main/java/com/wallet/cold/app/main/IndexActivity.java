package com.wallet.cold.app.main;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TabHost;
import android.widget.Toast;

import com.wallet.R;
import com.wallet.cold.app.index.Fragment1;
import com.wallet.cold.app.index.HQActivity;
import com.wallet.cold.app.index.MeActivity;
import com.wallet.cold.app.index.ZXActivity;
import com.wallet.cold.app.pawn.PawnActivity;
import com.wallet.cold.utils.Data;
import com.wallet.cold.utils.LocalManageUtil;
import com.wallet.hot.app.HotIndexActivity;

public class IndexActivity extends TabActivity {
    private RadioButton guide_home, guide_pawn ,guide_store, guide_cart, guide_me;
    private Intent intent1,intentd,intent2,intent3,intent4,intent5;
    private String tab="tab0";
    private int currIndex = 0;
    private TabHost tabHost;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        //初始化四个Activity
        intent1 = new Intent(this, Fragment1.class);
        intentd = new Intent(this, PawnActivity.class);
        intent2 = new Intent(this, HQActivity.class);
        intent3 = new Intent(this, ZXActivity.class);
        intent4 = new Intent(this, MeActivity.class);
        intent5 = new Intent(this, HotIndexActivity.class);
        init();
        inittAB();
        Bundle extras = getIntent().getExtras();
        if (null != extras) {
            String type = extras.getString("type");
            if(type.equals("0")) {
                Toast.makeText(Data.getcontext(), "请注册后再进行登录操作", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 初始化组件
     */
    private void init() {
        guide_home = (RadioButton) findViewById(R.id.guide_home);
        //guide_pawn = (RadioButton) findViewById(R.id.guide_pawn);
        guide_cart = (RadioButton) findViewById(R.id.guide_cart);
        guide_store=(RadioButton) findViewById(R.id.guide_store);
        //guide_me=(RadioButton) findViewById(R.id.guide_me);
        //设置点击事件
        guide_home.setOnClickListener(new MyOnPageChangeListener());
        //guide_pawn.setOnClickListener(new MyOnPageChangeListener());
        guide_store.setOnClickListener(new MyOnPageChangeListener());
        guide_cart.setOnClickListener(new MyOnPageChangeListener());
        //guide_me.setOnClickListener(new MyOnPageChangeListener());
    }

    //填充TabHost
    private void inittAB() {
        tabHost = getTabHost();
        //这里tab0是第一个，tab1是第二个窗口，以此类推
        if(Data.getapptype().equals("cold")) {
            tabHost.addTab(tabHost.newTabSpec("tab0")
                    .setIndicator("tab0")
                    .setContent(intent1));
        }else if(Data.getapptype().equals("hot")) {
            tabHost.addTab(tabHost.newTabSpec("tab0")
                    .setIndicator("tab0")
                    .setContent(intent5));
        }
        tabHost.addTab(tabHost.newTabSpec("tabd")
                .setIndicator("tabd")
                .setContent(intentd));
        tabHost.addTab(tabHost.newTabSpec("tab1")
                .setIndicator("tab1")
                .setContent(intent2));
        tabHost.addTab(tabHost.newTabSpec("tab2")
                .setIndicator("tab2")
                .setContent(intent3));
        tabHost.addTab(tabHost.newTabSpec("tab3")
                .setIndicator("tab3")
                .setContent(intent4));
        if(Data.getresult().equals("1")) {
            Data.setresult("");
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    guide_me.performClick();
                }
            });
        }else if(Data.getresult().equals("2")){
            Data.setresult("");
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    guide_pawn.performClick();
                }
            });
        }else if(tab.equalsIgnoreCase("tab0")){
            tabHost.setCurrentTabByTag("tab0");
        }
    }

    /**
     * 点击事件类
     */
    private class MyOnPageChangeListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int i = v.getId();
            if (i == R.id.guide_home) {
                currIndex = 0;
                Drawable drawable = IndexActivity.this.getResources().getDrawable(R.drawable.qianbao_xz);
                guide_home.setCompoundDrawablesRelativeWithIntrinsicBounds(null, drawable, null, null);
//                Drawable drawabled = IndexActivity.this.getResources().getDrawable(R.drawable.pawn_xk);
//                guide_pawn.setCompoundDrawablesRelativeWithIntrinsicBounds(null, drawabled, null, null);
                Drawable drawable2 = IndexActivity.this.getResources().getDrawable(R.drawable.faxian_xk);
                guide_cart.setCompoundDrawablesRelativeWithIntrinsicBounds(null, drawable2, null, null);
                Drawable drawable3 = IndexActivity.this.getResources().getDrawable(R.drawable.hangqing_xk);
                guide_store.setCompoundDrawablesRelativeWithIntrinsicBounds(null, drawable3, null, null);
//                Drawable drawable12 = IndexActivity.this.getResources().getDrawable(R.drawable.my_xk);
//                guide_me.setCompoundDrawablesRelativeWithIntrinsicBounds(null, drawable12, null, null);
                tabHost.setCurrentTabByTag("tab0");
            } else if (i == R.id.guide_cart) {
                currIndex = 3;
                Drawable drawable4 = IndexActivity.this.getResources().getDrawable(R.drawable.qianbao_xk);
                guide_home.setCompoundDrawablesRelativeWithIntrinsicBounds(null, drawable4, null, null);
//                Drawable drawabled = IndexActivity.this.getResources().getDrawable(R.drawable.pawn_xk);
//                guide_pawn.setCompoundDrawablesRelativeWithIntrinsicBounds(null, drawabled, null, null);
                Drawable drawable5 = IndexActivity.this.getResources().getDrawable(R.drawable.faxian_xz);
                guide_cart.setCompoundDrawablesRelativeWithIntrinsicBounds(null, drawable5, null, null);
                Drawable drawable6 = IndexActivity.this.getResources().getDrawable(R.drawable.hangqing_xk);
                guide_store.setCompoundDrawablesRelativeWithIntrinsicBounds(null, drawable6, null, null);
//                Drawable drawable13 = IndexActivity.this.getResources().getDrawable(R.drawable.my_xk);
//                guide_me.setCompoundDrawablesRelativeWithIntrinsicBounds(null, drawable13, null, null);
                tabHost.setCurrentTabByTag("tab2");
            } else if (i == R.id.guide_store) {
                currIndex = 4;
                Drawable drawable1 = IndexActivity.this.getResources().getDrawable(R.drawable.hangqing_xz);
                guide_store.setCompoundDrawablesRelativeWithIntrinsicBounds(null, drawable1, null, null);
//                Drawable drawabled = IndexActivity.this.getResources().getDrawable(R.drawable.pawn_xk);
//                guide_pawn.setCompoundDrawablesRelativeWithIntrinsicBounds(null, drawabled, null, null);
                Drawable drawable7 = IndexActivity.this.getResources().getDrawable(R.drawable.faxian_xk);
                guide_cart.setCompoundDrawablesRelativeWithIntrinsicBounds(null, drawable7, null, null);
                Drawable drawable8 = IndexActivity.this.getResources().getDrawable(R.drawable.qianbao_xk);
                guide_home.setCompoundDrawablesRelativeWithIntrinsicBounds(null, drawable8, null, null);
//                Drawable drawable14 = IndexActivity.this.getResources().getDrawable(R.drawable.my_xk);
//                guide_me.setCompoundDrawablesRelativeWithIntrinsicBounds(null, drawable14, null, null);
                tabHost.setCurrentTabByTag("tab1");
//            } else if (i == R.id.guide_me) {
//                currIndex = 5;
//                Drawable drawable9 = IndexActivity.this.getResources().getDrawable(R.drawable.me_xz);
//                guide_me.setCompoundDrawablesRelativeWithIntrinsicBounds(null, drawable9, null, null);
//                Drawable drawabled = IndexActivity.this.getResources().getDrawable(R.drawable.pawn_xk);
//                guide_pawn.setCompoundDrawablesRelativeWithIntrinsicBounds(null, drawabled, null, null);
//                Drawable drawable10 = IndexActivity.this.getResources().getDrawable(R.drawable.faxian_xk);
//                guide_cart.setCompoundDrawablesRelativeWithIntrinsicBounds(null, drawable10, null, null);
//                Drawable drawable11 = IndexActivity.this.getResources().getDrawable(R.drawable.qianbao_xk);
//                guide_home.setCompoundDrawablesRelativeWithIntrinsicBounds(null, drawable11, null, null);
//                Drawable drawable15 = IndexActivity.this.getResources().getDrawable(R.drawable.hangqing_xk);
//                guide_store.setCompoundDrawablesRelativeWithIntrinsicBounds(null, drawable15, null, null);
//                tabHost.setCurrentTabByTag("tab3");
//            } else if (i == R.id.guide_pawn) {
//                currIndex = 5;
//                Drawable drawable9 = IndexActivity.this.getResources().getDrawable(R.drawable.my_xk);
//                guide_me.setCompoundDrawablesRelativeWithIntrinsicBounds(null, drawable9, null, null);
//                Drawable drawabled = IndexActivity.this.getResources().getDrawable(R.drawable.pawn_xz);
//                guide_pawn.setCompoundDrawablesRelativeWithIntrinsicBounds(null, drawabled, null, null);
//                Drawable drawable10 = IndexActivity.this.getResources().getDrawable(R.drawable.faxian_xk);
//                guide_cart.setCompoundDrawablesRelativeWithIntrinsicBounds(null, drawable10, null, null);
//                Drawable drawable11 = IndexActivity.this.getResources().getDrawable(R.drawable.qianbao_xk);
//                guide_home.setCompoundDrawablesRelativeWithIntrinsicBounds(null, drawable11, null, null);
//                Drawable drawable15 = IndexActivity.this.getResources().getDrawable(R.drawable.hangqing_xk);
//                guide_store.setCompoundDrawablesRelativeWithIntrinsicBounds(null, drawable15, null, null);
//                tabHost.setCurrentTabByTag("tabd");
            }
        }
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocalManageUtil.setLocal(newBase));
    }
}