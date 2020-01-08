package com.wallet.centralized;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wallet.MainActivity;
import com.wallet.R;
import com.wallet.cold.app.main.IndexActivity;
import com.wallet.cold.utils.Data;
import com.wallet.utils.language.LocalManageUtil;
import com.wallet.utils.LogCook;
import com.wallet.utils.WeiboDialogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import static com.wallet.cold.utils.Utils.getIndex;
import static com.wallet.cold.utils.Utils.hexStringToString;
import static com.wallet.cold.utils.Utils.sendble;

public class centerMainActivity extends AppCompatActivity implements OnClickListener {

    private EditText et_user,et_pwd;
    private Dialog mWeiboDialog;
    private Button verify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_centermain);
        Data.settype("centermain");
        Data.setcontext(centerMainActivity.this);
        et_user=(EditText) findViewById(R.id.username);
        et_pwd=(EditText) findViewById(R.id.passward);
        verify=(Button)findViewById(R.id.login);
        verify.setOnClickListener(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {//如果返回键按下
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fhreset) {
            Intent intent1 = new Intent(this, MainActivity.class);
            startActivity(intent1);
        }
        if (v.getId() == R.id.register) {
            Data.setauth0sign("register");
            if (et_user.getText().toString().equals("")) {
                Toast.makeText(Data.getcontext(), Data.getcontext().getResources().getString(R.string.m3), Toast.LENGTH_SHORT).show();
            } else if (et_pwd.getText().toString().equals("")) {
                Toast.makeText(Data.getcontext(), Data.getcontext().getResources().getString(R.string.m4), Toast.LENGTH_SHORT).show();
            } else {
                mWeiboDialog = WeiboDialogUtils.createLoadingDialog(centerMainActivity.this, Data.getcontext().getResources().getString(R.string.m1));
                Data.setdialog(mWeiboDialog);
                Data.setusername(et_user.getText().toString());
                Data.setpassword(et_pwd.getText().toString());
            }
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocalManageUtil.setLocal(newBase));
    }
}


