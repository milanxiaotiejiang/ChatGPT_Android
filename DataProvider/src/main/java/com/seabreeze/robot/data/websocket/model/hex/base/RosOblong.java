package com.seabreeze.robot.data.websocket.model.hex.base;

import android.graphics.RectF;

import com.seabreeze.robot.base.ext.tool.ConvertUtil;

import org.jetbrains.annotations.NotNull;

/**
 * User: milan
 * Time: 2021/9/3 19:28
 * Des: 长方形区域
 */
public class RosOblong extends PDU {

    private final RectF rectF;

    public RosOblong(int vendorId, RectF rectF) {
        super(vendorId);
        this.rectF = rectF;
    }

    @NotNull
    @Override
    public byte[] getPayload() {
        byte[] bytes = new byte[16];

        byte[] left = ConvertUtil.intToByteArray4(Math.round(rectF.left * MULTIPLE));
        System.arraycopy(left, 0, bytes, 0, left.length);

        byte[] top = ConvertUtil.intToByteArray4(Math.round(rectF.top * MULTIPLE));
        System.arraycopy(top, 0, bytes, 4, top.length);

        byte[] right = ConvertUtil.intToByteArray4(Math.round(rectF.right * MULTIPLE));
        System.arraycopy(right, 0, bytes, 8, right.length);

        byte[] bottom = ConvertUtil.intToByteArray4(Math.round(rectF.bottom * MULTIPLE));
        System.arraycopy(bottom, 0, bytes, 12, bottom.length);

        return bytes;
    }
}
