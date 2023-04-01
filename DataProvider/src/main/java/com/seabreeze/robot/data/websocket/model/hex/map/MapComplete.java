package com.seabreeze.robot.data.websocket.model.hex.map;

import com.seabreeze.robot.data.websocket.model.hex.base.PDU;

import org.jetbrains.annotations.NotNull;

/**
 * User: milan
 * Time: 2021/9/4 9:11
 * Des: 获取完整地图
 */
public class MapComplete extends PDU {
    public MapComplete() {
        super(0xB2);
    }

    @NotNull
    @Override
    public byte[] getPayload() {
        return BYTE_01;
    }
}
