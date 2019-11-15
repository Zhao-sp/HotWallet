package com.wallet.cold.utils;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.wallet.R;

public class PopWinShare1 extends PopupWindow {
    private View mainView;
    private TextView layoutbtc, layouteth,layoutxrp,layoutaed;

    public PopWinShare1(Activity paramActivity, View.OnClickListener paramOnClickListener){
        super(paramActivity);
        //窗口布局
        mainView = LayoutInflater.from(paramActivity).inflate(R.layout.popwin_share1, null);
        //分享布局
        layoutbtc = ((TextView)mainView.findViewById(R.id.layout_btc));
        //复制布局
        layouteth = (TextView)mainView.findViewById(R.id.layout_eth);
        layoutxrp = (TextView)mainView.findViewById(R.id.layout_xrp);
        layoutaed = (TextView)mainView.findViewById(R.id.layout_aed);
        //设置每个子布局的事件监听器
        if (paramOnClickListener != null){
            layoutbtc.setOnClickListener(paramOnClickListener);
            layouteth.setOnClickListener(paramOnClickListener);
            layoutxrp.setOnClickListener(paramOnClickListener);
            layoutaed.setOnClickListener(paramOnClickListener);
        }
        setContentView(mainView);
        //设置显示隐藏动画
        setAnimationStyle(R.style.AnimTools);
        //设置背景透明
        setBackgroundDrawable(new ColorDrawable(0));
    }
}

