package com.wallet.cold.dfu.exception;


public class BlueToothNotEnableException extends BleException {
    public BlueToothNotEnableException() {
        super(ERROR_CODE_BLUETOOTH_NOT_ENABLE, "BlueTooth Not Enable Exception Occurred!");
    }
}
