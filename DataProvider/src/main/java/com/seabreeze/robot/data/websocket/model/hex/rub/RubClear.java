package com.seabreeze.robot.data.websocket.model.hex.rub;

import androidx.annotation.NonNull;

import com.seabreeze.robot.data.websocket.model.hex.base.PDU;

/**
 * User: milan
 * Time: 2021/9/16 18:47
 * Des: 重置障碍物
 */
public class RubClear extends PDU {
    public RubClear() {
        super(0xD4);
    }

    @NonNull
    @Override
    public byte[] getPayload() {
        return BYTE_01;
    }
}

