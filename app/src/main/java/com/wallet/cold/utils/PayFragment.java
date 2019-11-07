package com.wallet.cold.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.wallet.R;


public class PayFragment extends DialogFragment implements View.OnClickListener {

    public static final String EXTRA_CONTENT = "extra_content";    //提示框内容

    private PayPwdView psw_input;
    private PayPwdView.InputCallBack inputCallBack;
    private TextView tv_zfname,tv_zfname1;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), R.style.BottomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置Content前设定
        dialog.setContentView(R.layout.fragment_pay);
        dialog.setCanceledOnTouchOutside(false); // 外部点击取消
        // 设置宽度为屏宽, 靠近屏幕底部。
        final Window window = dialog.getWindow();
        window.setWindowAnimations(R.style.AnimBottom);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        final WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
        lp.gravity = Gravity.TOP;
        window.setAttributes(lp);
        initView(dialog);
        return dialog;
    }

    private void initView(Dialog dialog) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            TextView tv_content = (TextView) dialog.findViewById(R.id.tv_content);
            tv_content.setText(bundle.getString(EXTRA_CONTENT));
        }
        Data.setresult("card");
        tv_zfname = (TextView) dialog.findViewById(R.id.zfname);tv_zfname.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        tv_zfname1 = (TextView) dialog.findViewById(R.id.zfname1);tv_zfname1.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        psw_input = (PayPwdView) dialog.findViewById(R.id.payPwdView);
        PwdInputMethodView inputMethodView = (PwdInputMethodView) dialog.findViewById(R.id.inputMethodView);
        psw_input.setInputMethodView(inputMethodView);
        psw_input.setInputCallBack(inputCallBack);
        dialog.findViewById(R.id.iv_close).setOnClickListener(this);
        dialog.findViewById(R.id.zfname).setOnClickListener(this);
        dialog.findViewById(R.id.zfname1).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_close:
                dismiss();
                break;
            case R.id.zfname:
                LayoutInflater inflater = LayoutInflater.from(Data.getcontext());
                final View textEntryView = inflater.inflate(R.layout.dlglayout, null);
                final TextView pt=(TextView)textEntryView.findViewById(R.id.pt);
                final TextView card=(TextView)textEntryView.findViewById(R.id.card);
                AlertDialog.Builder builder = new AlertDialog.Builder(Data.getcontext());
                builder.setView(textEntryView);
                final AlertDialog dialog = builder.show();
                pt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Data.setresult("pt");
                        tv_zfname.setText(R.string.pf1);
                        dialog.dismiss();
                    }
                });
                card.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Data.setresult("card");
                        tv_zfname.setText(R.string.m18);
                        dialog.dismiss();
                    }
                });
                break;
            case R.id.zfname1:
                LayoutInflater inflater1 = LayoutInflater.from(Data.getcontext());
                final View textEntryView1 = inflater1.inflate(R.layout.dlglayout, null);
                final TextView pt1=(TextView)textEntryView1.findViewById(R.id.pt);
                final TextView card1=(TextView)textEntryView1.findViewById(R.id.card);
                AlertDialog.Builder builder1 = new AlertDialog.Builder(Data.getcontext());
                builder1.setView(textEntryView1);
                final AlertDialog dialog1 = builder1.show();
                pt1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Data.setresult("pt");
                        tv_zfname.setText(R.string.pf1);
                        dialog1.dismiss();
                    }
                });
                card1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Data.setresult("card");
                        tv_zfname.setText(R.string.m18);
                        dialog1.dismiss();
                    }
                });
                break;

        }
    }

    /**
     * 设置输入回调
     *
     * @param inputCallBack
     */
    public void setPaySuccessCallBack(PayPwdView.InputCallBack inputCallBack) {
        this.inputCallBack = inputCallBack;
    }
}
