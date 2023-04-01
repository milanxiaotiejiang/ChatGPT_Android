package com.seabreeze.robot.data.websocket.model.hex.action;

import android.graphics.PointF;
import android.graphics.RectF;

import com.seabreeze.robot.data.websocket.model.hex.base.RosRegion;

import java.util.ArrayList;
import java.util.List;

/**
 * User: milan
 * Time: 2021/9/3 19:48
 * Des:
 */
public class Cleaning extends RosRegion {

    public Cleaning(RectF rectF) {
        super(0xA3, rectToPointList(rectF));
    }

    private static List<PointF> rectToPointList(RectF rectF) {
        List<PointF> pointFList = new ArrayList<>();
        pointFList.add(new PointF(rectF.left, rectF.top));
        pointFList.add(new PointF(rectF.left, rectF.bottom));
        pointFList.add(new PointF(rectF.right, rectF.top));
        pointFList.add(new PointF(rectF.right, rectF.bottom));
        return pointFList;
    }
}
