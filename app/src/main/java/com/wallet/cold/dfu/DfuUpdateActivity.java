package com.wallet.cold.dfu;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.wallet.cold.app.main.MainActivity;
import com.wallet.R;
import com.wallet.cold.dfu.data.ScanResult;
import com.wallet.cold.dfu.dfu_service.DfuService;
import com.wallet.cold.utils.Data;

import no.nordicsemi.android.dfu.DfuProgressListener;
import no.nordicsemi.android.dfu.DfuServiceController;
import no.nordicsemi.android.dfu.DfuServiceInitiator;
import no.nordicsemi.android.dfu.DfuServiceListenerHelper;

import static com.wallet.cold.app.main.MainActivity.DEVICE_NAME;
import static com.wallet.cold.app.main.MainActivity.MAC_ADDRESS;

public class DfuUpdateActivity extends BaseActivity implements View.OnClickListener {
    private BluetoothService mBluetoothService;
    private TextView tv_show,fanhui;
    private ImageView fhdfu;
    private String dfu_macAddress;
    private String dfu_device_name;
    private String path;
    private ProgressBar progressBar;
    private boolean xiancheng=true;
    private ProgressBar mProgressBarTest;
    private Runnable mRunnable;
    private Handler mHandler;
    private int m_iStep=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dfu_update);
        Intent intent = getIntent();
        dfu_macAddress = intent.getStringExtra(MAC_ADDRESS);
        dfu_device_name = intent.getStringExtra(DEVICE_NAME);
        bindService();
        tv_show = (TextView) findViewById(R.id.tv_result);
        Button startBtn = (Button) findViewById(R.id.startDfu);
        startBtn.setOnClickListener(this);
        Data.setstartBtn(startBtn);
        fhdfu=(ImageView) findViewById(R.id.fanhuidfu);
        fhdfu.setOnClickListener(this);
        fanhui=(TextView) findViewById(R.id.fhdfu);
        fanhui.setOnClickListener(this);
        progressBar = (ProgressBar) findViewById(R.id.dfu_progress);
        mProgressBarTest=(ProgressBar)findViewById(R.id.ProgressBarTest);
        mHandler=new Handler(){
            public void handleMessage(Message message){
                mProgressBarTest.setProgress(m_iStep);
            }
        };
        mRunnable=new Runnable() {
            @Override
            public void run() {
                try {
                    while (xiancheng) {
                        Thread.sleep(100);
                        m_iStep = Data.getpercent();
                        Message message = new Message();
                        mHandler.sendMessage(message);
                        xiancheng = false;
                    }
                } catch (Exception e) {
                    Message message = new Message();
                    message.obj = e;
                    mHandler.sendMessage(message);
                }
            }
        };
    }

    public String getRealPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (null != cursor && cursor.moveToFirst()) {
            ;
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
            cursor.close();
        }
        return res;
    }

    /**
     * 专为Android4.4设计的从Uri获取文件绝对路径，以前的方法已不好使
     */
    @SuppressLint("NewApi")
    public String getPath(final Context context, final Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Data.getstartBtn().setEnabled(false);
        Data.setdfuupdate(true);
        if (resultCode == RESULT_OK) {
            Uri uri = data.getData();
            if (uri == null)
                return;
            if ("file".equalsIgnoreCase(uri.getScheme())) {//使用第三方应用打开
                path = uri.getPath();
            } else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {//4.4以后
                path = getPath(this, uri);
            } else {//4.4以下下系统调用方法
                path = getRealPathFromURI(uri);
            }
            tv_show.setText(path);
            if (requestCode == 1) {
                progressBar.setVisibility(View.VISIBLE);
                final DfuServiceInitiator starter = new DfuServiceInitiator(dfu_macAddress)
                        .setDeviceName(mBluetoothService.getName())
                        .setKeepBond(true);
// If you want to have experimental buttonless DFU feature supported call additionally:
                starter.setUnsafeExperimentalButtonlessServiceInSecureDfuEnabled(true);
// but be aware of this: https://devzone.nordicsemi.com/question/100609/sdk-12-bootloader-erased-after-programming/
// and other issues related to this experimental service.

// Init packet is required by Bootloader/DFU from SDK 7.0+ if HEX or BIN file is given above.
// In case of a ZIP file, the init packet (a DAT file) must be included inside the ZIP file.
//            if (mFileType == DfuService.TYPE_AUTO)
                starter.setZip(uri, path);
//            else {
//                starter.setBinOrHex(mFileType, mFileStreamUri, mFilePath).setInitFile(mInitFileStreamUri, mInitFilePath);
//            }
                final DfuServiceController controller = starter.start(this, DfuService.class);
// You may use the controller to pause, resume or abort the DFU process.
//            DfuServiceInitiator.createDfuNotificationChannel(this);
            }
        }
    }

    private final DfuProgressListener dfuProgressListener = new DfuProgressListener() {
        @Override
        public void onDeviceConnecting(String deviceAddress) {
//          progressBar.setIndeterminate(true);
//          mTextPercentage.setText(R.string.dfu_status_connecting);
            Log.i("TEST", "onDeviceConnecting: " + deviceAddress);
        }

        @Override
        public void onDeviceConnected(String deviceAddress) {
            Log.i("TEST", "onDeviceConnected: " + deviceAddress);
        }

        @Override
        public void onDfuProcessStarting(String deviceAddress) {
//          progressBar.setIndeterminate(true);
//          mTextPercentage.setText(R.string.dfu_status_starting);
            Log.i("TEST", "onDfuProcessStarting: " + deviceAddress);
        }

        @Override
        public void onDfuProcessStarted(String deviceAddress) {
            Log.i("TEST", "onDfuProcessStarted: " + deviceAddress);
        }

        @Override
        public void onEnablingDfuMode(String deviceAddress) {
            Log.i("TEST", "onEnablingDfuMode: " + deviceAddress);
        }

        @Override
        public void onProgressChanged(String deviceAddress, int percent, float speed, float avgSpeed, int currentPart, int partsTotal) {
            Log.i("TEST", "onProgressChanged: " + deviceAddress + "百分比" + percent + ",speed "
                    + speed + ",avgSpeed " + avgSpeed + ",currentPart " + currentPart
                    + ",partTotal " + partsTotal);
            tv_show.setText(Data.getcontext().getResources().getString(R.string.dfu2) + percent + "%");
            Data.setpercent(percent);
            xiancheng = true;
            new Thread(mRunnable).start();
        }

        @Override
        public void onFirmwareValidating(String deviceAddress) {
            Log.i("TEST", "onFirmwareValidating: " + deviceAddress);
        }

        @Override
        public void onDeviceDisconnecting(String deviceAddress) {
            Log.i("TEST", "onDeviceDisconnecting: " + deviceAddress);
        }

        @Override
        public void onDeviceDisconnected(String deviceAddress) {
            Data.getstartBtn().setEnabled(true);
            Data.setdfuupdate(false);
            tv_show.setText(R.string.dfu3);
            Log.i("TEST", "onDeviceDisconnected: " + deviceAddress);
            Intent intent2 = new Intent(DfuUpdateActivity.this, MainActivity.class);
            startActivity(intent2);
        }

        @Override
        public void onDfuCompleted(String deviceAddress) {
            Log.i("TEST", "onDfuCompleted: " + deviceAddress);
//          progressBar.setIndeterminate(true);
            progressBar.setVisibility(View.GONE);
        }

        @Override
        public void onDfuAborted(String deviceAddress) {
            Log.i("TEST", "onDfuAborted: " + deviceAddress);
            progressBar.setVisibility(View.GONE);
        }

        @Override
        public void onError(String deviceAddress, int error, int errorType, String message) {
            Log.i("TEST", "onError: " + deviceAddress + ",message:" + message);
            progressBar.setVisibility(View.GONE);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        DfuServiceListenerHelper.registerProgressListener(this, dfuProgressListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        DfuServiceListenerHelper.unregisterProgressListener(this, dfuProgressListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBluetoothService != null) {
            unbindService();
            //  mBluetoothService = null;
        }
    }


    private void bindService() {
        Intent bindIntent = new Intent(this, BluetoothService.class);
        this.bindService(bindIntent, mFhrSCon, Context.BIND_AUTO_CREATE);
    }


    private void unbindService() {
        this.unbindService(mFhrSCon);
    }

    private BluetoothService.Callback callback = new BluetoothService.Callback() {
        @Override
        public void onStartScan() {

        }

        @Override
        public void onScanning(ScanResult scanResult) {

        }

        @Override
        public void onScanComplete() {

        }

        @Override
        public void onConnecting() {

        }

        @Override
        public void onConnectFail() {

        }

        @Override
        public void onDisConnected() {
            tv_show.setText("Dfu蓝牙断开连接");
        }

        @Override
        public void onServicesDiscovered() {
            tv_show.setText("Dfu蓝牙连接成功");
        }
    };


    private ServiceConnection mFhrSCon = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBluetoothService = ((BluetoothService.BluetoothBinder) service).getService();
            mBluetoothService.setScanCallback(callback);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.startDfu) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            //intent.setType(“image/*”);//选择图片
            //intent.setType(“audio/*”); //选择音频
            //intent.setType(“video/*”); //选择视频 （mp4 3gp 是android支持的视频格式）
            //intent.setType(“video/*;image/*”);//同时选择视频和图片
            intent.setType("*/*");//无类型限制
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(intent, 1);
        } else if (i == R.id.fhdfu) {
            if (Data.getdfuupdate()) {
                Toast.makeText(getApplicationContext(), "请等待升级完成后再操作", Toast.LENGTH_LONG).show();
            } else {
                Intent intent2 = new Intent(this, MainActivity.class);
                startActivity(intent2);
            }

        } else if (i == R.id.fanhuidfu) {
            if (Data.getdfuupdate()) {
                Toast.makeText(getApplicationContext(), "请等待升级完成后再操作", Toast.LENGTH_LONG).show();
            } else {
                Intent intent2 = new Intent(this, MainActivity.class);
                startActivity(intent2);
            }

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            if(Data.getdfuupdate()) {
                Toast.makeText(getApplicationContext(), "请等待升级完成后再操作", Toast.LENGTH_LONG).show();
                return true;
            }else{
                Intent intent2 = new Intent(this, MainActivity.class);
                startActivity(intent2);
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
