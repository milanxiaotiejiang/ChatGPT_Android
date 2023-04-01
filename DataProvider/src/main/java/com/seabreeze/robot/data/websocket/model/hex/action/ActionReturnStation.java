package com.seabreeze.robot.data.websocket.model.hex.action;

import com.seabreeze.robot.data.websocket.model.hex.base.PDU;

import org.jetbrains.annotations.NotNull;

/**
 * User: milan
 * Time: 2021/9/3 19:13
 * Des: 返回基站
 */
public class ActionReturnStation extends PDU {
    public ActionReturnStation() {
        super(0xA2);
    }

    @NotNull
    @Override
    public byte[] getPayload() {
        return BYTE_01;
    }
}
