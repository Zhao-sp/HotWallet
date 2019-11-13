package com.wallet.cold.app.main;

import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wallet.CreateOrImportActivity;
import com.wallet.R;
import com.wallet.cold.dfu.DfuUpdateActivity;
import com.wallet.cold.utils.Data;
import com.wallet.cold.utils.LocalManageUtil;
import com.wallet.cold.utils.UartService;
import com.wallet.cold.utils.Utils;
import com.wallet.cold.utils.WeiboDialogUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends Activity {
    private static final int REQUEST_SELECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    public static final String TAG = "nRFUART";
    private static final int UART_PROFILE_DISCONNECTED = 21;
    private int mState = UART_PROFILE_DISCONNECTED;
    private UartService mService = null;
    private BluetoothDevice mDevice = null;
    private BluetoothAdapter mBtAdapter = null;
    private Button btnConnectDisconnect;
    private BluetoothAdapter mBluetoothAdapter;
    private Dialog mWeiboDialog;
    List<BluetoothDevice> deviceList= new ArrayList<BluetoothDevice>();
    private DeviceAdapter deviceAdapter;
    private ServiceConnection onService = null;
    Map<String, Integer> devRssiValues =new HashMap<String, Integer>();
    private static final long SCAN_PERIOD = 10000; //10 seconds
    private Handler mHandler;
    private boolean mScanning;
    private BluetoothDevice device;
    private ListView newDevicesListView;
    private TextView bletype;
    private List<String> List = new ArrayList<>();
    private Map<String,Object> map = new HashMap<>();
    public static final String MAC_ADDRESS = "mac address";
    public static final String DEVICE_NAME = "device name";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if((getIntent().getFlags() &Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0){//防止部分手机点击home键后退出程序
            finish();
            return;
        }
        //创建表
        Data.getdb().execSQL("create table if not exists JiaoyiTb (_id integer primary key,blename text not null,name text not null,bizhong text not null,jine integer not null,riqi text not null,type integer not null)");
        Data.getdb().execSQL("create table if not exists Auth0AddressTb (_id integer primary key,blename text not null,name text not null,address text not null)");
        Data.settype("type");Data.setrestart(0);Data.setscan("1");Data.setisapp("yes");Data.setauth0type("未登录");
        Data.setbtcbalance("");Data.setethbalance("");Data.setblename("");Data.setsign("0");Data.setusername("");Data.setauth0sign("");
        Data.setsaoma("no");Data.setbletype("");Data.setisdfu(false);Data.setsigncount1(1);Data.setresult("");Data.setresulterror("");
        Data.setreturnbledata("yes");Data.setdfuupdate(false);
        Data.setbledata(List);Data.setpage(1);
        Data.setcontext(MainActivity.this);
        new Utils().service_init(getApplicationContext());
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBtAdapter == null) {
            mService.close();
            mService.disconnect();
            finish();
            return;
        }
        newDevicesListView = findViewById(R.id.list_search_devices);
        bletype=findViewById(R.id.bletype);
        btnConnectDisconnect = findViewById(R.id.button);
        mHandler = new Handler();
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            finish();
        }
        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        if (mBluetoothAdapter == null) {
            finish();
            return;
        }
        Data.setBluetoothAdapter(mBluetoothAdapter);
        populateList();
        btnConnectDisconnect.setOnClickListener(new View.OnClickListener() {//重新扫描按钮
            @Override
            public void onClick(View v) {
                if(Data.getscan().equals("1")) {
                    deviceList.clear();
                    device = null;
                    deviceAdapter = new DeviceAdapter(MainActivity.this, deviceList);
                    newDevicesListView.setAdapter(deviceAdapter);
                    newDevicesListView.setOnItemClickListener(mDeviceClickListener);
                    scanLeDevice(true);
                    bletype.setText(R.string.main3);
                    btnConnectDisconnect.setVisibility(View.INVISIBLE);
                }
            }
        });
        if (!mBtAdapter.isDiscovering()) {
            mBtAdapter.cancelDiscovery();
        }
        // 开始搜索蓝牙设备,搜索到的蓝牙设备通过广播返回
        mBtAdapter.startDiscovery();
    }

    private long mExitTime;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {//按返回键退出程序
        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            if ((System.currentTimeMillis() - mExitTime) > 2000) {
//                Toast.makeText(this, R.string.main4, Toast.LENGTH_SHORT).show();
//                mExitTime = System.currentTimeMillis();
//            } else {
//                mService.close();
//                mService.disconnect();
//                System.exit(0);
//            }
//            return true;
            Intent intent = new Intent(this, CreateOrImportActivity.class);
            startActivity(intent);
        }
        return super.onKeyDown(keyCode, event);
    }

    private void populateList() {
        deviceAdapter = new DeviceAdapter(this, deviceList);
        newDevicesListView.setAdapter(deviceAdapter);
        newDevicesListView.setOnItemClickListener(mDeviceClickListener);
        scanLeDevice(true);
    }

    public void scanLeDevice(final boolean enable) {//搜索蓝牙
        if (enable) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    if(Data.getblename().equals("")) {
                        bletype.setText(R.string.main5);
                    }else{
                        bletype.setText("");
                    }
                    btnConnectDisconnect.setVisibility(View.VISIBLE);
                }
            }, SCAN_PERIOD);
            mScanning = true;
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
    }

    public BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, final int rssi, byte[] scanRecord) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            addDevice(device, rssi);
                        }
                    });
                }
            });
        }
    };

    public void addDevice(BluetoothDevice device, int rssi) {//添加蓝牙信息
        boolean deviceFound = false;
        for (BluetoothDevice listDev : deviceList) {
            if (listDev.getAddress().equals(device.getAddress())) {
                deviceFound = true;
                break;
            }
        }
        devRssiValues.put(device.getAddress(), rssi);
        if (!deviceFound) {
            if(device.getName()==null){
            }else {
                deviceList.add(device);
                deviceAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onStart() {//按home退出后在进入程序的逻辑
        super.onStart();
        if(device==null) {
            IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
            filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
            filter.addAction(BluetoothDevice.ACTION_FOUND);
            filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
            filter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
            registerReceiver(receiver, filter);
            WeiboDialogUtils.closeDialog(Data.getdialog());
            if (Data.getscan().equals("1")) {
                deviceList.clear();
                device = null;
                deviceAdapter = new DeviceAdapter(MainActivity.this, deviceList);
                newDevicesListView.setAdapter(deviceAdapter);
                newDevicesListView.setOnItemClickListener(mDeviceClickListener);
                scanLeDevice(true);
                btnConnectDisconnect.setVisibility(View.INVISIBLE);
                bletype.setText(R.string.main3);
            }
        }else{
            if(Utils.isNetworkConnected(Data.getcontext())) {
                mWeiboDialog = WeiboDialogUtils.createLoadingDialog(MainActivity.this, this.getResources().getString(R.string.type));
                Data.setdialog(mWeiboDialog);
                Utils.gainboot();
            }else{
                Toast.makeText(Data.getcontext(), Data.getcontext().getResources().getString(R.string.cd2), Toast.LENGTH_SHORT).show();
                WeiboDialogUtils.closeDialog(Data.getdialog());
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        mBluetoothAdapter.stopLeScan(mLeScanCallback);
    }

    private AdapterView.OnItemClickListener mDeviceClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            device = deviceList.get(position);
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
    };

    protected void onPause() {
        super.onPause();
        scanLeDevice(false);
    }

    static class DeviceAdapter extends BaseAdapter {//蓝牙信息显示界面逻辑类
        Context context;
        List<BluetoothDevice> devices;
        LayoutInflater inflater;

        public DeviceAdapter(Context context, List<BluetoothDevice> devices) {
            this.context = context;
            inflater = LayoutInflater.from(context);
            this.devices = devices;
        }

        @Override
        public int getCount() {
            return devices.size();
        }

        @Override
        public Object getItem(int position) {
            return devices.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder vg;
            if (convertView != null) {
                vg = (ViewHolder) convertView.getTag();
            } else {
                vg = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.item_ble, parent, false);
                vg.name = convertView.findViewById(R.id.name);
                vg.image = convertView.findViewById(R.id.jimage);
                vg.rrrr = convertView.findViewById(R.id.rrrr);
                convertView.setTag(vg);
            }
            BluetoothDevice device = devices.get(position);
            vg.name.setText(device.getName());
            vg.name.setTextColor(0xffffffff);
            Data.setblename("yes");
            vg.rrrr.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(Utils.isNetworkConnected(Data.getcontext())) {
                        Dialog mWeiboDialog = WeiboDialogUtils.createLoadingDialog(Data.getcontext(), context.getResources().getString(R.string.type));
                        Data.setdialog(mWeiboDialog);
                        Data.setdevicename(vg.name.getText().toString());
                        Data.setDeviceaddress(device.getAddress());
                        if(device.getName().equals("DfuTarg")){
                            Intent intent = new Intent(Data.getcontext(),DfuUpdateActivity.class);
                            intent.putExtra(MAC_ADDRESS,device.getAddress());
                            intent.putExtra(DEVICE_NAME,device.getName());
                            Data.getcontext().startActivity(intent);
                        }else {
                            Data.getmService().connect(device.getAddress());
                        }
                    }else{
                        Toast.makeText(Data.getcontext(), Data.getcontext().getResources().getString(R.string.cd2), Toast.LENGTH_SHORT).show();
                    }
                }
            });
            return convertView;
        }
        class ViewHolder {
            TextView name;
            ImageView image;
            RelativeLayout rrrr;
        }
    }


    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // 获得已经搜索到的蓝牙设备
            if (action.equals(BluetoothDevice.ACTION_FOUND)) {

            } else if (action.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)) {

            }else if (action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)) {
                // 状态改变的广播
                device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String name = device.getName();
                if (device.getName().equalsIgnoreCase(name)) {
                    int connectState = device.getBondState();
                    switch (connectState) {
                        case BluetoothDevice.BOND_NONE:  //10
                            Toast.makeText(getApplicationContext(), context.getResources().getString(R.string.type1)+device.getName(), Toast.LENGTH_SHORT).show();
                            Data.setscan("1");
                            Data.getmService().disconnect();
                            Data.getmService().close();
                            WeiboDialogUtils.closeDialog(Data.getdialog());
                            break;
                        case BluetoothDevice.BOND_BONDING:  //11
                            Toast.makeText(getApplicationContext(), context.getResources().getString(R.string.type2)+device.getName(), Toast.LENGTH_SHORT).show();
                            Data.setscan("2");
                            break;
                        case BluetoothDevice.BOND_BONDED:   //12
                            Toast.makeText(getApplicationContext(), context.getResources().getString(R.string.type3)+device.getName(), Toast.LENGTH_SHORT).show();
                            mWeiboDialog = WeiboDialogUtils.createLoadingDialog(MainActivity.this, context.getResources().getString(R.string.type));
                            Data.setdialog(mWeiboDialog);
                            try {
                                Data.setdevicename(device.getName());
                                Data.setDeviceaddress(device.getAddress());
                                Data.getmService().connect(device.getAddress());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                    }
                }
            }
        }
    };
    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        //mService.stopSelf();
        mService= null;
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        if (!mBtAdapter.isEnabled()) {
            Log.i(TAG, "onResume - BT not enabled yet");
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_SELECT_DEVICE:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    String deviceAddress = data.getStringExtra(BluetoothDevice.EXTRA_DEVICE);
                    mDevice = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(deviceAddress);
                    mService.connect(deviceAddress);
                }
                break;
            case REQUEST_ENABLE_BT:
                if (resultCode == Activity.RESULT_OK) {
                } else {
                    finish();
                }
                break;
            default:
                break;
        }
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocalManageUtil.setLocal(newBase));
    }
}
