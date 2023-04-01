package com.seabreeze.robot.data.websocket.model.hex.nff;

import com.seabreeze.robot.data.websocket.model.hex.base.PDU;

import org.jetbrains.annotations.NotNull;

/**
 * User: milan
 * Time: 2021/9/4 9:35
 * Des: 扫拖开关
 * 00 全关
 * 01 扫
 * 02 拖
 * 03全开
 */
public class NffAll extends PDU {
    private final boolean isClean;
    private final boolean isDrag;

    public NffAll(boolean isClean, boolean isDrag) {
        super(0xC3);
        this.isClean = isClean;
        this.isDrag = isDrag;
    }

    @NotNull
    @Override
    public byte[] getPayload() {
        if (isClean) {
            if (isDrag) {
                return BYTE_03;
            } else {
                return BYTE_01;
            }
        } else {
            if (isDrag) {
                return BYTE_02;
            } else {
                return BYTE_00;
            }
        }
    }
}
