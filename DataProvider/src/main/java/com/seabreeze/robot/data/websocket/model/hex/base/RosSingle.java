package com.seabreeze.robot.data.websocket.model.hex.base;

import android.graphics.PointF;

import com.seabreeze.robot.base.ext.tool.ConvertUtil;

import org.jetbrains.annotations.NotNull;

/**
 * User: milan
 * Time: 2021/9/4 9:15
 * Des: 单点
 */
public class RosSingle extends PDU {

    private final PointF pointF;

    public RosSingle(int vendorId, PointF pointF) {
        super(vendorId);
        this.pointF = pointF;
    }

    @NotNull
    @Override
    public byte[] getPayload() {
        byte[] bytes = new byte[8];

        byte[] bytesX = ConvertUtil.intToByteArray4(Math.round(pointF.x * MULTIPLE));
        System.arraycopy(bytesX, 0, bytes, 0, bytesX.length);

        byte[] bytesY = ConvertUtil.intToByteArray4(Math.round(pointF.y * MULTIPLE));
        System.arraycopy(bytesY, 0, bytes, bytesX.length, bytesY.length);
        return bytes;
    }
}
