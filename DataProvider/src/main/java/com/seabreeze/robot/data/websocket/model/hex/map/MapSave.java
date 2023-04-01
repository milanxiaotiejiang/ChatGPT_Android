package com.seabreeze.robot.data.websocket.model.hex.map;

import com.seabreeze.robot.data.websocket.model.hex.base.PDU;

import org.jetbrains.annotations.NotNull;

/**
 * User: milan
 * Time: 2021/9/4 9:09
 * Des: 保存地图
 */
public class MapSave extends PDU {
    public MapSave() {
        super(0xB1);
    }

    @NotNull
    @Override
    public byte[] getPayload() {
        return BYTE_01;
    }
}
