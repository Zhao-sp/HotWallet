package com.wallet.cold.app.pawn;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.DigitsKeyListener;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.wallet.R;
import com.wallet.cold.utils.Data;
import com.wallet.cold.utils.LocalManageUtil;
import com.wallet.cold.utils.WeiboDialogUtils;

import static com.wallet.cold.utils.Utils.sendble;

public class login extends AppCompatActivity implements OnClickListener {

    private ImageView photo;
    private Dialog mWeiboDialog;
    private Button verify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Data.settype("login");
        Data.setcontext(login.this);
        photo= (ImageView) findViewById(R.id.img2);
        photo.setImageDrawable(getResources().getDrawable(R.drawable.login));
        verify=(Button)findViewById(R.id.verify);
        verify.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.verify) {
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
                            mWeiboDialog = WeiboDialogUtils.createLoadingDialog(login.this, Data.getcontext().getResources().getString(R.string.m11));
                            Data.setdialog(mWeiboDialog);
                            Data.setlimit(inputServer.getText().toString());
                            Data.setauth0sign("login_register");
                            Data.setbizhong("ERC20");//币种分类
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

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocalManageUtil.setLocal(newBase));
    }
}
