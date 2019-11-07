
package com.wallet.cold.dfu.conn;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;

import com.wallet.cold.dfu.data.ScanResult;
import com.wallet.cold.dfu.exception.BleException;


public abstract class BleGattCallback extends BluetoothGattCallback {

    public void onFoundDevice(ScanResult scanResult) {
    }

    public void onConnecting(BluetoothGatt gatt, int status) {
    }

    public abstract void onConnectError(BleException exception);

    public abstract void onConnectSuccess(BluetoothGatt gatt, int status);

    public abstract void onDisConnected(BluetoothGatt gatt, int status, BleException exception);

}