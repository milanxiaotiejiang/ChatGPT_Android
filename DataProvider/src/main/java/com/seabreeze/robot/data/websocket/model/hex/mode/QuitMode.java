package com.seabreeze.robot.data.websocket.model.hex.mode;

import androidx.annotation.NonNull;

import com.seabreeze.robot.data.websocket.model.hex.base.PDU;

/**
 * User: milan
 * Time: 2021/10/8 17:10
 * Des: 工作模式
 */
public class QuitMode extends PDU {
    public QuitMode() {
        super(0xE3);
    }

    @NonNull
    @Override
    public byte[] getPayload() {
        return BYTE_01;
    }
}
