package com.seabreeze.robot.data.websocket.model.hex.map;

import android.graphics.PointF;

import com.seabreeze.robot.data.websocket.model.hex.base.RosSingle;

/**
 * User: milan
 * Time: 2021/9/4 9:13
 * Des: 设置基站
 */
public class MapStation extends RosSingle {
    public MapStation(PointF pointF) {
        super(0xB4, pointF);
    }
}