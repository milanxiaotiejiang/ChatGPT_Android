package com.seabreeze.robot.data.websocket.model.hex.nff;

import com.seabreeze.robot.data.websocket.model.hex.base.RosState;

/**
 * User: milan
 * Time: 2021/9/4 9:35
 * Des: 清洁开关
 */
public class NffDrag extends RosState {
    public NffDrag(boolean isState) {
        super(0xC2, isState);
    }
}
