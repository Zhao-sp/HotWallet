package com.wallet.cold.app.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wallet.R;
import com.wallet.cold.utils.Data;
import com.wallet.cold.utils.LocalManageUtil;
import com.wallet.cold.utils.Utils;
import com.wallet.cold.utils.WeiboDialogUtils;

import java.io.File;

public class GengxinActivity extends AppCompatActivity implements OnClickListener {
    private TextView fhgx;
    private TextView app,boot,dfu;
    private ImageView fanhui;
    private EditText et_path;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gengxin);
        fhgx=(TextView) findViewById(R.id.fhgx);
        app=(TextView) findViewById(R.id.app);
        boot=(TextView) findViewById(R.id.boot);
        dfu=(TextView) findViewById(R.id.dfu);
        fanhui=(ImageView) findViewById(R.id.fanhui5);
        app.setOnClickListener(this);
        boot.setOnClickListener(this);
        fanhui.setOnClickListener(this);
        fhgx.setOnClickListener(this);
        dfu.setOnClickListener(this);
        Data.settype("gengxinactivity");
        Data.setcontext(GengxinActivity.this);
        new Utils().service_init(getApplicationContext());
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {//如果返回键按下
            Intent intent = new Intent(this, Fragment5.class);
            startActivity(intent);
        }
        return false;
    }
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.fanhui5) {
            Intent intent = new Intent(this, Fragment5.class);
            startActivity(intent);
        }
        if(v.getId() == R.id.fhgx) {
            Intent intent1 = new Intent(this, Fragment5.class);
            startActivity(intent1);
        }
        if(v.getId() == R.id.app) {
            Toast.makeText(Data.getcontext(), Data.getcontext().getResources().getString(R.string.gx1), Toast.LENGTH_SHORT).show();
        }
        if(v.getId() == R.id.boot) {
            //Toast.makeText(Data.getcontext(), Data.getcontext().getResources().getString(R.string.gx2), Toast.LENGTH_SHORT).show();
            View myView1 = LayoutInflater.from(GengxinActivity.this).inflate(R.layout.dialog_view, null);
            et_path = (EditText) myView1.findViewById(R.id.et_path);
            et_path.setOnClickListener(new View.OnClickListener() {//选择文件路径
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("*/*");//设置类型，我这里是任意类型，任意后缀的可以这样写。
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    startActivityForResult(intent,1);
                }
            });
            AlertDialog.Builder builder3 = new AlertDialog.Builder(GengxinActivity.this);
            builder3.setCancelable(false)//设置点击对话框外部区域不关闭对话框
//          .setTitle("固件升级").setMessage("此操作会升级固件，确定要升级吗？")
            .setTitle("请选择升级文件").setView(myView1)
            .setNegativeButton(R.string.f513, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                }
            })
            .setPositiveButton(R.string.f514, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if(et_path.getText().toString().equals("")){
                        Toast.makeText(Data.getcontext(), "请选择文件目录", Toast.LENGTH_SHORT).show();
                    }else {
                        Dialog mWeiboDialog = WeiboDialogUtils.createLoadingDialog(GengxinActivity.this, "固件升级中...");
                        Data.setdialog(mWeiboDialog);
                        Utils.openboot();
                    }
                }
            })
            .show();
        }
        if(v.getId() == R.id.dfu) {
            AlertDialog.Builder builder2 = new AlertDialog.Builder(GengxinActivity.this);
            builder2.setCancelable(false)//设置点击对话框外部区域不关闭对话框
            .setTitle(R.string.dfu).setMessage(R.string.dfu1)
            .setNegativeButton(R.string.f513, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                }
            })
            .setPositiveButton(R.string.f514, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Dialog mWeiboDialog = WeiboDialogUtils.createLoadingDialog(GengxinActivity.this, Data.getcontext().getResources().getString(R.string.gx4));
                    Data.setdialog(mWeiboDialog);
                    Data.setbletype("dfureadapp");
                    Utils.dfu();
                    //Toast.makeText(Data.getcontext(), Data.getcontext().getResources().getString(R.string.gx1), Toast.LENGTH_SHORT).show();
                }
            })
            .show();
            //Toast.makeText(Data.getcontext(), Data.getcontext().getResources().getString(R.string.gx2), Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {//是否选择，没选择就不会继续
            Uri uri = data.getData();//得到uri，后面就是将uri转化成file的过程。
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor actualimagecursor = managedQuery(uri, proj, null, null, null);
            int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            actualimagecursor.moveToFirst();
            String img_path = actualimagecursor.getString(actual_image_column_index);
            File file = new File(img_path);
            Data.setpath(file.toString());
            et_path.setText(file.toString());
        }
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocalManageUtil.setLocal(newBase));
    }
}
