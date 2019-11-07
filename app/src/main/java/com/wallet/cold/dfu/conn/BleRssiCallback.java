package com.wallet.cold.dfu.conn;


public abstract class BleRssiCallback extends BleCallback {
    public abstract void onSuccess(int rssi);
}