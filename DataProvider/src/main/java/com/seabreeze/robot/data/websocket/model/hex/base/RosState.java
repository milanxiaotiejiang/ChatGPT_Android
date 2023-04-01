package com.seabreeze.robot.data.websocket.model.hex.base;

import org.jetbrains.annotations.NotNull;

/**
 * User: milan
 * Time: 2021/9/4 9:26
 * Des: true、false
 */
public class RosState extends PDU {

    private final boolean isState;

    public RosState(int vendorId, boolean isState) {
        super(vendorId);
        this.isState = isState;
    }

    @NotNull
    @Override
    public byte[] getPayload() {
        return isState ? BYTE_01 : BYTE_00;
    }
}
