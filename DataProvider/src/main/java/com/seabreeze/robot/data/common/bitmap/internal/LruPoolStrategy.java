package com.seabreeze.robot.data.common.bitmap.internal;

import android.graphics.Bitmap;

import androidx.annotation.Nullable;

/**
 * User: milan
 * Time: 2022/6/28 10:42
 * Des: glide : com.bumptech.glide.load.engine.bitmap_recycle
 * <p>
 */
interface LruPoolStrategy {
    void put(Bitmap bitmap);

    @Nullable
    Bitmap get(int width, int height, Bitmap.Config config);

    @Nullable
    Bitmap removeLast();

    String logBitmap(Bitmap bitmap);

    String logBitmap(int width, int height, Bitmap.Config config);

    int getSize(Bitmap bitmap);
}
