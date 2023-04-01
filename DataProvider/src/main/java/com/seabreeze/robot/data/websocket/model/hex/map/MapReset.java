package com.seabreeze.robot.data.websocket.model.hex.map;

import com.seabreeze.robot.data.websocket.model.hex.base.PDU;

import org.jetbrains.annotations.NotNull;

/**
 * User: milan
 * Time: 2021/9/4 9:17
 * Des: 重置地图
 */
public class MapReset extends PDU {
    public MapReset() {
        super(0xB5);
    }

    @NotNull
    @Override
    public byte[] getPayload() {
        return BYTE_01;
    }
}