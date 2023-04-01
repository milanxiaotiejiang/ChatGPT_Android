package com.seabreeze.robot.data.websocket.model.hex.rub;

import android.graphics.PointF;

import com.seabreeze.robot.data.websocket.model.hex.base.RosRegion;

import java.util.ArrayList;
import java.util.List;

/**
 * User: milan
 * Time: 2021/9/4 9:23
 * Des: 设置障碍物
 */
public class RubObstacle extends RosRegion {

    private static final int OBSTACLE_PADDING = 3;

    public RubObstacle(float x, float y) {
        super(0xD3, valueToPointList(x, y));
    }

    private static List<PointF> valueToPointList(float x, float y) {
        List<PointF> pointFList = new ArrayList<>();
        pointFList.add(new PointF(x - OBSTACLE_PADDING, y - OBSTACLE_PADDING));
        pointFList.add(new PointF(x + OBSTACLE_PADDING, y - OBSTACLE_PADDING));
        pointFList.add(new PointF(x - OBSTACLE_PADDING, y + OBSTACLE_PADDING));
        pointFList.add(new PointF(x + OBSTACLE_PADDING, y + OBSTACLE_PADDING));
        return pointFList;
    }
}
