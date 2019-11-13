package com.wallet.cold.app.index;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wallet.R;
import com.wallet.cold.app.pawn.CardAmount;
import com.wallet.cold.app.pawn.CzActivity;
import com.wallet.cold.app.pawn.TxActivity;
import com.wallet.cold.utils.Data;
import com.wallet.cold.utils.LocalManageUtil;

import java.io.File;

public class MeActivity extends AppCompatActivity implements OnClickListener {

    private static final int PICK = 1;// 拍照上传
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;// 结果
    private ImageView photo2;
    private File tempFile;
    private TextView type;
    private RelativeLayout cz,tx,kntx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me);
        Data.settype("meactivity");
        Data.setcontext(MeActivity.this);
        photo2= (ImageView) findViewById(R.id.img2);
        type= (TextView) findViewById(R.id.type);
        cz= (RelativeLayout) findViewById(R.id.view_cz);
        tx= (RelativeLayout) findViewById(R.id.view_tx);
        kntx= (RelativeLayout) findViewById(R.id.kntx);
        type.setText(Data.getauth0type());
        cz.setOnClickListener(this);
        tx.setOnClickListener(this);
        kntx.setOnClickListener(this);
        photo2.setImageDrawable(getResources().getDrawable(R.drawable.head));
        photo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!type.getText().toString().equals("未登录")) {
                    View view1 = LayoutInflater.from(MeActivity.this).inflate(R.layout.head_select, null);
                    final Dialog dialog = new Dialog(MeActivity.this, R.style.common_dialog);
                    dialog.setContentView(view1);
                    dialog.setCancelable(false);
                    dialog.show();
                    // 监听
                    View.OnClickListener select_img = new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (v.getId() == R.id.head_xiangji) {
                                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(intent, PICK);
                            }
                            if (v.getId() == R.id.head_tuku) {
                                // 激活系统图库，选择一张图片
                                Intent intent = new Intent(Intent.ACTION_PICK);
                                intent.setType("image/*");
                                // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_GALLERY
                                startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
                            }
                            if (v.getId() == R.id.head_cancel_btn) {

                            }
                            dialog.dismiss();
                        }
                    };
                    TextView xiangji = (TextView) view1.findViewById(R.id.head_xiangji);
                    TextView tuku = (TextView) view1.findViewById(R.id.head_tuku);
                    TextView mBtnCancel = (TextView) view1.findViewById(R.id.head_cancel_btn);
                    xiangji.setOnClickListener(select_img);
                    tuku.setOnClickListener(select_img);
                    mBtnCancel.setOnClickListener(select_img);
                    // 设置相关位置，一定要在 show()之后
                    Window window = dialog.getWindow();
                    window.getDecorView().setPadding(0, 0, 0, 0);
                    WindowManager.LayoutParams params = window.getAttributes();
                    params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                    params.gravity = Gravity.BOTTOM;
                    window.setAttributes(params);
                }else{
                    Toast.makeText(Data.getcontext(), Data.getcontext().getResources().getString(R.string.m15), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /*
     * 剪切图片
     */
    private void crop(Uri uri) {
        //SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
        //String address = sDateFormat.format(new java.util.Date());
        //imagePath = address + ".JPEG";
        //　Uri imageUri = Uri.parse("保存的文件夹的名称/" + address
        // + ".JPEG");
        // 裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // 裁剪框的比例，1：1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 250);
        //intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);//输出路径
        intent.putExtra("outputFormat", "JPEG");// 图片格式
        intent.putExtra("noFaceDetection", true);// 取消人脸识别
        intent.putExtra("return-data", true);
        //Cursor cursor = LocationApplication.getContext().getContentResolver().query(uri, null, null, null, null);
        //if (cursor != null && cursor.moveToFirst()) {
        //photopath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
        //Log.e("photopath", "photopath:------------" + photopath);
        //}
        //filePath=photopath;
        //定义一个全局变量，接受photopath
        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CUT
        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK) {
            Bundle bundle = data.getExtras();
            Bitmap bitmap = (Bitmap) bundle.get("data");
            Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, null,null));
            crop(uri);
        }
        if (requestCode == PHOTO_REQUEST_GALLERY) {
            // 从相册返回的数据
            if (data != null) {
                // 得到图片的全路径
                Uri uri = data.getData();
                crop(uri);
            }
        } else if (requestCode == PHOTO_REQUEST_CUT) {
            // 从剪切图片返回的数据
            if (data != null) {
                Bitmap bitmap = data.getParcelableExtra("data");
                photo2.setImageBitmap(bitmap);
            }
            try {
                // 将临时文件删除
                tempFile.delete();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.address){
            Intent intent = new Intent(this, Linkman.class);
            startActivity(intent);
        }
        if(v.getId() == R.id.view_cz){
            Intent intent = new Intent(this, CzActivity.class);
            startActivity(intent);
        }
        if(v.getId() == R.id.view_tx){
            Intent intent = new Intent(this, TxActivity.class);
            startActivity(intent);
        }
        if(v.getId() == R.id.kntx){
            Intent intent = new Intent(this, CardAmount.class);
            startActivity(intent);
        }
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocalManageUtil.setLocal(newBase));
    }
}
