package com.wallet.cold.utils;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.wallet.R;

public class PopWinShare extends PopupWindow {
    private View mainView;
    private TextView addbizhong, sao;

    public PopWinShare(Activity paramActivity, View.OnClickListener paramOnClickListener){
        super(paramActivity);
        //窗口布局
        mainView = LayoutInflater.from(paramActivity).inflate(R.layout.popwin_share, null);
        //分享布局
        addbizhong = ((TextView)mainView.findViewById(R.id.addbizhong));
        //复制布局
        sao = (TextView)mainView.findViewById(R.id.sao);
        //设置每个子布局的事件监听器
        if (paramOnClickListener != null){
            addbizhong.setOnClickListener(paramOnClickListener);
            sao.setOnClickListener(paramOnClickListener);
        }
        setContentView(mainView);
        //设置显示隐藏动画
        setAnimationStyle(R.style.AnimTools);
        //设置背景透明
        setBackgroundDrawable(new ColorDrawable(0));
    }
}
