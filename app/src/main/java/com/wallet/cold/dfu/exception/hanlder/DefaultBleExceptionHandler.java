package com.wallet.cold.dfu.exception.hanlder;

import com.wallet.cold.dfu.exception.BlueToothNotEnableException;
import com.wallet.cold.dfu.exception.ConnectException;
import com.wallet.cold.dfu.exception.GattException;
import com.wallet.cold.dfu.exception.NotFoundDeviceException;
import com.wallet.cold.dfu.exception.OtherException;
import com.wallet.cold.dfu.exception.ScanFailedException;
import com.wallet.cold.dfu.exception.TimeoutException;
import com.wallet.cold.dfu.utils.BleLog;

public class DefaultBleExceptionHandler extends BleExceptionHandler {

    private static final String TAG = "BleExceptionHandler";

    public DefaultBleExceptionHandler() {

    }

    @Override
    protected void onConnectException(ConnectException e) {
        BleLog.e(TAG, e.getDescription());
    }

    @Override
    protected void onGattException(GattException e) {
        BleLog.e(TAG, e.getDescription());
    }

    @Override
    protected void onTimeoutException(TimeoutException e) {
        BleLog.e(TAG, e.getDescription());
    }

    @Override
    protected void onNotFoundDeviceException(NotFoundDeviceException e) {
        BleLog.e(TAG, e.getDescription());
    }

    @Override
    protected void onBlueToothNotEnableException(BlueToothNotEnableException e) {
        BleLog.e(TAG, e.getDescription());
    }

    @Override
    protected void onScanFailedException(ScanFailedException e) {
        BleLog.e(TAG, e.getDescription());
    }

    @Override
    protected void onOtherException(OtherException e) {
        BleLog.e(TAG, e.getDescription());
    }
}
