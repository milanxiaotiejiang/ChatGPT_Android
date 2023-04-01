package com.seabreeze.robot.data.websocket.model.hex.rub;

import android.graphics.PointF;

import com.seabreeze.robot.data.websocket.model.hex.base.RosLine;

import java.util.ArrayList;
import java.util.List;

/**
 * User: milan
 * Time: 2021/9/4 9:22
 * Des: 设置虚拟墙
 */
public class RubWall extends RosLine {
    public RubWall(float x1, float y1, float x2, float y2) {
        super(0xD1, valueToPointList(x1, y1, x2, y2));
    }

    private static List<PointF> valueToPointList(float x1, float y1, float x2, float y2) {
        List<PointF> pointFList = new ArrayList<>();
        pointFList.add(new PointF(x1, y1));
        pointFList.add(new PointF(x2, y2));
        return pointFList;
    }
}
