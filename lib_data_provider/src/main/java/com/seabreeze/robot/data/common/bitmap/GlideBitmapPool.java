package com.seabreeze.robot.data.common.bitmap;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import com.seabreeze.robot.data.common.bitmap.internal.BitmapPool;
import com.seabreeze.robot.data.common.bitmap.internal.LruBitmapPool;

import java.util.Set;

/**
 * User: milan
 * Time: 2022/6/28 10:42
 * Des:
 */
public class GlideBitmapPool {

    private static final int DEFAULT_MAX_SIZE = 1024 * 1024 * 5 * 32;
    private final BitmapPool bitmapPool;
    private static GlideBitmapPool sInstance;

    private GlideBitmapPool(int maxSize) {
        bitmapPool = new LruBitmapPool(maxSize);
    }

    private GlideBitmapPool(int maxSize, Set<Bitmap.Config> allowedConfigs) {
        bitmapPool = new LruBitmapPool(maxSize, allowedConfigs);
    }

    private static GlideBitmapPool getInstance() {
        if (sInstance == null) {
            sInstance = new GlideBitmapPool(DEFAULT_MAX_SIZE);
        }
        return sInstance;
    }

    public static void initialize(int maxSize) {
        sInstance = new GlideBitmapPool(maxSize);
    }

    public static void initialize(int maxSize, Set<Bitmap.Config> allowedConfigs) {
        sInstance = new GlideBitmapPool(maxSize, allowedConfigs);
    }

    public static void putBitmap(Bitmap bitmap) {
        if (bitmap != null) {
            getInstance().bitmapPool.put(bitmap);
        }
    }

    @NonNull
    public static Bitmap getBitmap(int width, int height, Bitmap.Config config) {
        return getInstance().bitmapPool.get(width, height, config);
    }

    public static Bitmap getDirtyBitmap(int width, int height, Bitmap.Config config) {
        return getInstance().bitmapPool.getDirty(width, height, config);
    }

    public static void clearMemory() {
        getInstance().bitmapPool.clearMemory();
    }

    public static void trimMemory(int level) {
        getInstance().bitmapPool.trimMemory(level);
    }

    public static void shutDown() {
        if (sInstance != null) {
            sInstance.bitmapPool.clearMemory();
            sInstance = null;
        }
    }

}
