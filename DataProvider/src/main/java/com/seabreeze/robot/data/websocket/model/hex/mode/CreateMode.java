package com.seabreeze.robot.data.websocket.model.hex.mode;

import androidx.annotation.NonNull;

import com.seabreeze.robot.data.websocket.model.hex.base.PDU;

/**
 * User: milan
 * Time: 2021/10/8 17:10
 * Des: 建图模式
 */
public class CreateMode extends PDU {
    public CreateMode() {
        super(0xE1);
    }

    @NonNull
    @Override
    public byte[] getPayload() {
        return BYTE_01;
    }
}
