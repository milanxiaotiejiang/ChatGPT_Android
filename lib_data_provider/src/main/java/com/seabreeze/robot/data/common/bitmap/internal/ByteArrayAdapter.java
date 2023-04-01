package com.seabreeze.robot.data.common.bitmap.internal;

/**
 * User: milan
 * Time: 2022/6/28 10:42
 * Des: glide : com.bumptech.glide.load.engine.bitmap_recycle
 * <p>
 * Adapter for handling primitive byte arrays.
 */
@SuppressWarnings("PMD.UseVarargs")
public final class ByteArrayAdapter implements ArrayAdapterInterface<byte[]> {
    private static final String TAG = "ByteArrayPool";

    @Override
    public String getTag() {
        return TAG;
    }

    @Override
    public int getArrayLength(byte[] array) {
        return array.length;
    }

    @Override
    public byte[] newArray(int length) {
        return new byte[length];
    }

    @Override
    public int getElementSizeInBytes() {
        return 1;
    }
}
