package com.seabreeze.robot.data.common.bitmap.internal;

/**
 * User: milan
 * Time: 2022/6/28 10:42
 * Des: glide : com.bumptech.glide.load.engine.bitmap_recycle
 * <p>
 * Interface for handling operations on a primitive array type.
 *
 * @param <T> Array type (e.g. byte[], int[])
 */
interface ArrayAdapterInterface<T> {

    /**
     * TAG for logging.
     */
    String getTag();

    /**
     * Return the length of the given array.
     */
    int getArrayLength(T array);

    /**
     * Allocate and return an array of the specified size.
     */
    T newArray(int length);

    /**
     * Return the size of an element in the array in bytes (e.g. for int return 4).
     */
    int getElementSizeInBytes();
}
