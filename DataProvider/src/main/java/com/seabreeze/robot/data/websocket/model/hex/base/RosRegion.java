package com.seabreeze.robot.data.websocket.model.hex.base;

import android.graphics.PointF;

import com.seabreeze.robot.base.ext.tool.ConvertUtil;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * User: milan
 * Time: 2021/9/3 19:28
 * Des: 区域
 */
public class RosRegion extends PDU {

    private final List<PointF> pointFS;

    public RosRegion(int vendorId, List<PointF> pointFS) {
        super(vendorId);
        this.pointFS = pointFS;
    }

    @NotNull
    @Override
    public byte[] getPayload() {
        byte[] bytes = new byte[32];

        for (int i = 0; i < pointFS.size(); i++) {
            PointF f = pointFS.get(i);
            byte[] bytesX = ConvertUtil.intToByteArray4(Math.round(f.x * MULTIPLE));
            System.arraycopy(bytesX, 0, bytes, i * 2 * 4, bytesX.length);

            byte[] bytesY = ConvertUtil.intToByteArray4(Math.round(f.y * MULTIPLE));
            System.arraycopy(bytesY, 0, bytes, i * 2 * 4 + bytesX.length, bytesY.length);
        }
        return bytes;
    }
}
