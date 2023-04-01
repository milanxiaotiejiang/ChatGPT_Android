package com.seabreeze.robot.data.websocket.model.hex.action;

import com.seabreeze.robot.data.websocket.model.hex.base.PDU;

import org.jetbrains.annotations.NotNull;

/**
 * User: milan
 * Time: 2021/9/3 17:42
 * Des: 底盘移动
 */
public class ActionMove extends PDU {

    private final DirectionType mDirectionType;
    private final int linear;
    private final int angular;

    public ActionMove(DirectionType directionType, int linear, int angular) {
        super(0xA1);
        this.mDirectionType = directionType;
        this.linear = linear;
        this.angular = angular;
    }

    @NotNull
    @Override
    public byte[] getPayload() {
        int sendLinear;
        int sendAngular = angular;
        if (mDirectionType == DirectionType.CLOCKWISE || mDirectionType == DirectionType.ANTICLOCKWISE) {
            sendLinear = linear / 3;
            sendAngular = angular / 3;
        } else {
            sendLinear = linear;
            sendAngular = angular;
        }
        return new byte[]{
                (byte) mDirectionType.mValue,
                (byte) sendLinear,
                (byte) sendAngular
        };
    }

    public enum DirectionType {

        FORWARD(0x00),
        BACKWARD(0x01),
        CLOCKWISE(0x02),
        ANTICLOCKWISE(0x03),
        STOP(0x04);

        private final int mValue;

        DirectionType(int value) {
            this.mValue = value;
        }
    }

}
