package com.seabreeze.robot.data.websocket.model.hex.action;

import com.seabreeze.robot.data.websocket.model.hex.base.RosState;

/**
 * User: milan
 * Time: 2021/10/13 13:21
 * Des:
 */
public class ActionStartOver extends RosState {
    public ActionStartOver(boolean isOver) {
        super(0xA4, isOver);
    }
}

