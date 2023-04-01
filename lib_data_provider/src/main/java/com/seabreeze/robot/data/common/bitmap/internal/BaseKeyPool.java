package com.seabreeze.robot.data.common.bitmap.internal;

import com.bumptech.glide.util.Util;

import java.util.Queue;

/**
 * User: milan
 * Time: 2022/6/28 10:42
 * Des: glide : com.bumptech.glide.load.engine.bitmap_recycle
 * <p>
 */
abstract class BaseKeyPool<T extends Poolable> {
    private static final int MAX_SIZE = 20;
    private final Queue<T> keyPool = Util.createQueue(MAX_SIZE);

    T get() {
        T result = keyPool.poll();
        if (result == null) {
            result = create();
        }
        return result;
    }

    public void offer(T key) {
        if (keyPool.size() < MAX_SIZE) {
            keyPool.offer(key);
        }
    }

    abstract T create();
}
