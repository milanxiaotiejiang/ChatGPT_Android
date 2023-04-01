package com.seabreeze.robot.data.websocket.model.hex.map;

import android.graphics.PointF;

import com.seabreeze.robot.data.websocket.model.hex.base.RosRegion;

import java.util.List;

/**
 * User: milan
 * Time: 2021/9/3 19:48
 * Des: 擦除地图上的点位
 */
public class MapErase extends RosRegion {
    public MapErase(List<PointF> pointFS) {
        super(0xB3, pointFS);
    }
}
