package com.seabreeze.robot.data.common.bitmap.internal;

/**
 * User: milan
 * Time: 2022/6/28 10:42
 * Des: glide : com.bumptech.glide.load.engine.bitmap_recycle
 * <p>
 * Adapter for handling primitive int arrays.
 */
@SuppressWarnings("PMD.UseVarargs")
public final class IntegerArrayAdapter implements ArrayAdapterInterface<int[]> {
    private static final String TAG = "IntegerArrayPool";

    @Override
    public String getTag() {
        return TAG;
    }

    @Override
    public int getArrayLength(int[] array) {
        return array.length;
    }

    @Override
    public int[] newArray(int length) {
        return new int[length];
    }

    @Override
    public int getElementSizeInBytes() {
        return 4;
    }
}
