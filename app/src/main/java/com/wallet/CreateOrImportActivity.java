package com.wallet;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.WriterException;
import com.wallet.utils.JniUtils;
import com.wallet.utils.SharedPrefsStrListUtil;
import com.wallet.cold.app.main.MainActivity;
import com.wallet.cold.utils.Data;
import com.wallet.utils.language.LocalManageUtil;
import com.wallet.utils.LogCook;
import com.wallet.cold.utils.Utils;
import com.wallet.utils.WeiboDialogUtils;
import com.wallet.hot.app.ByteActivity;

import java.io.File;

public class CreateOrImportActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView mTitleView;
    private Button mCreateBtn,mImportBtn,mCenterBtn;
    private ObjectAnimator mTitleViewAnimator,mCreateBtnAnimator,mImportBtnAnimator,mCenterBtnAnimator;
    private int mScreenHeight = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_import_layout);
        mTitleView = findViewById(R.id.create_import_title);
        mCreateBtn = findViewById(R.id.create_wallet);
        mCreateBtn.setOnClickListener(this);
        mImportBtn = findViewById(R.id.hot_wallet);
        mImportBtn.setOnClickListener(this);
        mCenterBtn = findViewById(R.id.center_wallet);
        mCenterBtn.setOnClickListener(this);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        mScreenHeight = dm.heightPixels;
        initAnimation();
        startAnimation();
        //SharedPrefsStrListUtil.clear(getApplicationContext());
        new JniUtils().getbbCourseKeyFromC(this);
        SQLiteDatabase db = openOrCreateDatabase("HotWallet.db", MODE_PRIVATE, null);//创建数据库
        Data.setdb(db);Data.setresult("");Data.setisblecomment("0");Data.setcontext(CreateOrImportActivity.this);Data.settype("createOrimport");Data.setbizhong("");
        Data.sethttp1("http://111.225.200.132:8181");//8181开发环境 8282演示环境
        //Data.getdb().execSQL("drop table HotAddressTb");
        //Data.getdb().execSQL("drop table Auth0AddressTb");
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(CreateOrImportActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(CreateOrImportActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            } else {
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    LogCook.DeleteOverdueLogFile();
                    File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/com.hotwallet/log");
                    LogCook.getInstance().setLogPath(Environment.getExternalStorageDirectory().getAbsolutePath() + "/com.hotwallet/log")
                            .setLogName(LogCook.getNowDay() +"log.txt")
                            .isOpen(true)
                            .isSave(true)
                            .initialize();
                    if (!file.exists()) {
                        LogCook.d("jim", "path1 create:" + file.mkdirs());
                    }
                }
            }
        }
    }

    /**
     * 权限回调处理
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Toast.makeText(MainActivity.this, "位置权限打开", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(CreateOrImportActivity.this, "位置权限被禁止", Toast.LENGTH_LONG).show();
                }
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},0);
                }
            }
            break;
            case 0: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Toast.makeText(MainActivity.this, "相机权限打开", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(CreateOrImportActivity.this, "相机权限被禁止", Toast.LENGTH_LONG).show();
                }
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},2);
                }
            }
            break;
            case 2: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    Toast.makeText(CreateOrImportActivity.this, "读权限被禁止", Toast.LENGTH_LONG).show();
                }
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 3);
                }
            }
            break;
            case 3: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //创建文件夹
                    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/com.hotwallet/log");
                        LogCook.getInstance().setLogPath(Environment.getExternalStorageDirectory().getAbsolutePath() + "/com.hotwallet/log")
                                .setLogName(LogCook.getNowDay() +"log.txt")
                                .isOpen(true)
                                .isSave(true)
                                .initialize();
                        if (!file.exists()) {
                            LogCook.d("jim", "path1 create:" + file.mkdirs());
                        }
                    }
                    break;
                } else {
                    Toast.makeText(CreateOrImportActivity.this, "写权限被禁止", Toast.LENGTH_LONG).show();
                }
            }
            break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.hot_wallet:
                Data.setapptype("hot");
                java.util.List<String> list= SharedPrefsStrListUtil.getStrListValue(getApplicationContext(),"hotcurrency");
                Data.setbledata(list);
                Intent Intent1 = new Intent();
                Data.getdb().execSQL("create table if not exists HotAddressTb (_id integer primary key,password text not null,btcaddress text not null,ethaddress text not null,ethprv text not null,ethpub text not null," +
                        "btcprv text not null,btcpub text not null,xrpaddress text not null,xrppub text not null,xrpprv text not null,mnemonic text not null)");
                Cursor cursor = Data.getdb().rawQuery("select * from HotAddressTb", null);
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        String password = cursor.getString(cursor.getColumnIndex("password"));
                        String btcaddress = cursor.getString(cursor.getColumnIndex("btcaddress"));
                        String ethaddress = cursor.getString(cursor.getColumnIndex("ethaddress"));
                        String ethprv = cursor.getString(cursor.getColumnIndex("ethprv"));
                        String ethpub = cursor.getString(cursor.getColumnIndex("ethpub"));
                        String btcprv = cursor.getString(cursor.getColumnIndex("btcprv"));
                        String btcpub = cursor.getString(cursor.getColumnIndex("btcpub"));
                        String xrpaddress = cursor.getString(cursor.getColumnIndex("xrpaddress"));
                        String xrppub = cursor.getString(cursor.getColumnIndex("xrppub"));
                        String xrpprv = cursor.getString(cursor.getColumnIndex("xrpprv"));
                        String mnemonic = cursor.getString(cursor.getColumnIndex("mnemonic"));
                        Data.sethotpassword(password);Data.setbtcaddress(btcaddress);Data.setethaddress(ethaddress);
                        Data.sethotethprv(ethprv);Data.sethotbtcprv(btcprv);Data.sethotbtcpub(btcpub);Data.sethotethpub(ethpub);
                        Data.setxrpaddress(xrpaddress);Data.setxrppub(xrppub);Data.setxrpprv(xrpprv);Data.sethotzjc(mnemonic);
                    }
                    cursor.close();
                    try {
                        if(!Data.getethaddress().equals("")) {
                            Bitmap codeBitmap = Utils.createCode(Data.getethaddress());
                            Data.setethimgCode(codeBitmap);
                        }
                        if(!Data.getbtcaddress().equals("")) {
                            Bitmap codeBitmap1 = Utils.createCode(Data.getbtcaddress());
                            Data.setimgCode(codeBitmap1);
                        }
                        if(!Data.getxrpaddress().equals("")) {
                            Bitmap codeBitmap2 = Utils.createCode(Data.getxrpaddress());
                            Data.setxrpimgCode(codeBitmap2);
                        }
                    } catch (WriterException e) {
                        e.printStackTrace();
                    }
                    Dialog mWeiboDialog = WeiboDialogUtils.createLoadingDialog(this, this.getResources().getString(R.string.type4));
                    Data.setdialog(mWeiboDialog);
                    new Utils().balancebtc();
                }else {
                    Intent1.setClass(this, ByteActivity.class);
                    startActivity(Intent1);
                }
                break;
            case R.id.create_wallet:
                Data.setapptype("cold");
                Intent Intent = new Intent();
                Intent.setClass(this, MainActivity.class);
                startActivity(Intent);
                break;
            case R.id.center_wallet:
                Data.setapptype("center");
                Intent intent = new Intent();
                intent.setClass(this, MainActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void initAnimation() {
        titleAnimation();
        createAnimation();
        importAnimation();
        centerAnimation();
    }

    private void startAnimation() {
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(1000);
        animatorSet.play(mCreateBtnAnimator).with(mImportBtnAnimator).with(mCenterBtnAnimator).after(mTitleViewAnimator);
        animatorSet.start();
    }

    private void titleAnimation() {
        mTitleViewAnimator = ObjectAnimator.ofFloat(mTitleView, "translationY",
                mScreenHeight, 0);
        mTitleViewAnimator.setDuration(500);
        mTitleViewAnimator.setInterpolator(new DecelerateInterpolator());
        mTitleViewAnimator.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {
                // TODO Auto-generated method stub
                mTitleView.setTranslationY(mScreenHeight);
                mTitleView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                // TODO Auto-generated method stub
                mTitleView.setTranslationY(0);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                // TODO Auto-generated method stub

            }
        });
    }

    private void createAnimation() {
        mCreateBtnAnimator = ObjectAnimator.ofFloat(mCreateBtn, "translationY",
                mScreenHeight, 0);
        mCreateBtnAnimator.setDuration(800);
        mCreateBtnAnimator.setInterpolator(new DecelerateInterpolator());
        mCreateBtnAnimator.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {
                // TODO Auto-generated method stub
                mCreateBtn.setTranslationY(mScreenHeight);
                mCreateBtn.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                // TODO Auto-generated method stub
                mCreateBtn.setTranslationY(0);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                // TODO Auto-generated method stub

            }
        });
    }

    private void importAnimation() {
        mImportBtnAnimator = ObjectAnimator.ofFloat(mImportBtn, "translationY",
                mScreenHeight, 0);
        mImportBtnAnimator.setDuration(800);
        mImportBtnAnimator.setInterpolator(new DecelerateInterpolator());
        mImportBtnAnimator.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {
                // TODO Auto-generated method stub
                mImportBtn.setTranslationY(mScreenHeight);
                mImportBtn.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                // TODO Auto-generated method stub
                mImportBtn.setTranslationY(0);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                // TODO Auto-generated method stub

            }
        });
    }

    private void centerAnimation() {
        mCenterBtnAnimator = ObjectAnimator.ofFloat(mCenterBtn, "translationY",
                mScreenHeight, 0);
        mCenterBtnAnimator.setDuration(800);
        mCenterBtnAnimator.setInterpolator(new DecelerateInterpolator());
        mCenterBtnAnimator.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {
                // TODO Auto-generated method stub
                mCenterBtn.setTranslationY(mScreenHeight);
                mCenterBtn.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                // TODO Auto-generated method stub
                mCenterBtn.setTranslationY(0);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                // TODO Auto-generated method stub

            }
        });
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocalManageUtil.setLocal(newBase));
    }
}
