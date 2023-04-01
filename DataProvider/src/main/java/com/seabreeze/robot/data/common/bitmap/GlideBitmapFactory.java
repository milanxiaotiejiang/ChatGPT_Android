package com.seabreeze.robot.data.common.bitmap;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.util.TypedValue;

import com.seabreeze.robot.data.common.bitmap.internal.Util;

import java.io.FileDescriptor;
import java.io.InputStream;

/**
 * User: milan
 * Time: 2022/6/28 10:42
 * Des:
 */
public class GlideBitmapFactory {

    public static Bitmap decodeFile(String pathName) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathName, options);
        options.inSampleSize = 1;
        options.inMutable = true;
        Bitmap inBitmap = GlideBitmapPool.getBitmap(options.outWidth, options.outHeight, options.inPreferredConfig);
        if (Util.canUseForInBitmap(inBitmap, options)) {
            options.inBitmap = inBitmap;
        }
        options.inJustDecodeBounds = false;
        try {
            return BitmapFactory.decodeFile(pathName, options);
        } catch (Exception e) {
            options.inBitmap = null;
            return BitmapFactory.decodeFile(pathName, options);
        }
    }

    public static Bitmap decodeFile(String pathName, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathName, options);
        options.inSampleSize = Util.calculateInSampleSize(options, reqWidth, reqHeight);
        options.inMutable = true;
        Bitmap inBitmap = GlideBitmapPool.getBitmap(options.outWidth, options.outHeight, options.inPreferredConfig);
        if (Util.canUseForInBitmap(inBitmap, options)) {
            options.inBitmap = inBitmap;
        }
        options.inJustDecodeBounds = false;
        try {
            return BitmapFactory.decodeFile(pathName, options);
        } catch (Exception e) {
            options.inBitmap = null;
            return BitmapFactory.decodeFile(pathName, options);
        }
    }

    public static Bitmap decodeResource(Resources res, int id) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, id, options);
        options.inSampleSize = 1;
        options.inMutable = true;
        Bitmap inBitmap = GlideBitmapPool.getBitmap(options.outWidth, options.outHeight, options.inPreferredConfig);
        if (Util.canUseForInBitmap(inBitmap, options)) {
            options.inBitmap = inBitmap;
        }
        options.inJustDecodeBounds = false;
        try {
            return BitmapFactory.decodeResource(res, id, options);
        } catch (Exception e) {
            options.inBitmap = null;
            return BitmapFactory.decodeResource(res, id, options);
        }
    }

    public static Bitmap decodeResource(Resources res, int id, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, id, options);
        options.inSampleSize = Util.calculateInSampleSize(options, reqWidth, reqHeight);
        options.inMutable = true;
        Bitmap inBitmap = GlideBitmapPool.getBitmap(options.outWidth, options.outHeight, options.inPreferredConfig);
        if (Util.canUseForInBitmap(inBitmap, options)) {
            options.inBitmap = inBitmap;
        }
        options.inJustDecodeBounds = false;
        try {
            return BitmapFactory.decodeResource(res, id, options);
        } catch (Exception e) {
            options.inBitmap = null;
            return BitmapFactory.decodeResource(res, id, options);
        }
    }

    public static Bitmap decodeByteArray(byte[] data, int offset, int length) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(data, offset, length, options);
        options.inSampleSize = 1;
        options.inMutable = true;
        Bitmap inBitmap = GlideBitmapPool.getBitmap(options.outWidth, options.outHeight, options.inPreferredConfig);
        if (Util.canUseForInBitmap(inBitmap, options)) {
            options.inBitmap = inBitmap;
        }
        options.inJustDecodeBounds = false;
        try {
            return BitmapFactory.decodeByteArray(data, offset, length, options);
        } catch (Exception e) {
            options.inBitmap = null;
            return BitmapFactory.decodeByteArray(data, offset, length, options);
        }
    }

    public static Bitmap decodeByteArray(byte[] data, int offset, int length, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(data, offset, length, options);
        options.inSampleSize = Util.calculateInSampleSize(options, reqWidth, reqHeight);
        options.inMutable = true;
        Bitmap inBitmap = GlideBitmapPool.getBitmap(options.outWidth, options.outHeight, options.inPreferredConfig);
        if (Util.canUseForInBitmap(inBitmap, options)) {
            options.inBitmap = inBitmap;
        }
        options.inJustDecodeBounds = false;
        try {
            return BitmapFactory.decodeByteArray(data, offset, length, options);
        } catch (Exception e) {
            options.inBitmap = null;
            return BitmapFactory.decodeByteArray(data, offset, length, options);
        }
    }

    public static Bitmap decodeResourceStream(Resources res, TypedValue value, InputStream is, Rect pad) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResourceStream(res, value, is, pad, options);
        options.inSampleSize = 1;
        options.inMutable = true;
        Bitmap inBitmap = GlideBitmapPool.getBitmap(options.outWidth, options.outHeight, options.inPreferredConfig);
        if (Util.canUseForInBitmap(inBitmap, options)) {
            options.inBitmap = inBitmap;
        }
        options.inJustDecodeBounds = false;
        try {
            return BitmapFactory.decodeResourceStream(res, value, is, pad, options);
        } catch (Exception e) {
            options.inBitmap = null;
            return BitmapFactory.decodeResourceStream(res, value, is, pad, options);
        }
    }

    public static Bitmap decodeResourceStream(Resources res, TypedValue value, InputStream is, Rect pad, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResourceStream(res, value, is, pad, options);
        options.inSampleSize = Util.calculateInSampleSize(options, reqWidth, reqHeight);
        options.inMutable = true;
        Bitmap inBitmap = GlideBitmapPool.getBitmap(options.outWidth, options.outHeight, options.inPreferredConfig);
        if (Util.canUseForInBitmap(inBitmap, options)) {
            options.inBitmap = inBitmap;
        }
        options.inJustDecodeBounds = false;
        try {
            return BitmapFactory.decodeResourceStream(res, value, is, pad, options);
        } catch (Exception e) {
            options.inBitmap = null;
            return BitmapFactory.decodeResourceStream(res, value, is, pad, options);
        }
    }

    public static Bitmap decodeStream(InputStream is) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is, null, options);
        options.inSampleSize = 1;
        options.inMutable = true;
        Bitmap inBitmap = GlideBitmapPool.getBitmap(options.outWidth, options.outHeight, options.inPreferredConfig);
        if (Util.canUseForInBitmap(inBitmap, options)) {
            options.inBitmap = inBitmap;
        }
        options.inJustDecodeBounds = false;
        try {
            return BitmapFactory.decodeStream(is, null, options);
        } catch (Exception e) {
            options.inBitmap = null;
            return BitmapFactory.decodeStream(is, null, options);
        }
    }

    public static Bitmap decodeStream(InputStream is, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is, null, options);
        options.inSampleSize = Util.calculateInSampleSize(options, reqWidth, reqHeight);
        options.inMutable = true;
        Bitmap inBitmap = GlideBitmapPool.getBitmap(options.outWidth, options.outHeight, options.inPreferredConfig);
        if (Util.canUseForInBitmap(inBitmap, options)) {
            options.inBitmap = inBitmap;
        }
        options.inJustDecodeBounds = false;
        try {
            return BitmapFactory.decodeStream(is, null, options);
        } catch (Exception e) {
            options.inBitmap = null;
            return BitmapFactory.decodeStream(is, null, options);
        }
    }

    public static Bitmap decodeStream(InputStream is, Rect outPadding) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is, outPadding, options);
        options.inSampleSize = 1;
        options.inMutable = true;
        Bitmap inBitmap = GlideBitmapPool.getBitmap(options.outWidth, options.outHeight, options.inPreferredConfig);
        if (Util.canUseForInBitmap(inBitmap, options)) {
            options.inBitmap = inBitmap;
        }
        options.inJustDecodeBounds = false;
        try {
            return BitmapFactory.decodeStream(is, outPadding, options);
        } catch (Exception e) {
            options.inBitmap = null;
            return BitmapFactory.decodeStream(is, outPadding, options);
        }
    }

    public static Bitmap decodeStream(InputStream is, Rect outPadding, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is, outPadding, options);
        options.inSampleSize = Util.calculateInSampleSize(options, reqWidth, reqHeight);
        options.inMutable = true;
        Bitmap inBitmap = GlideBitmapPool.getBitmap(options.outWidth, options.outHeight, options.inPreferredConfig);
        if (Util.canUseForInBitmap(inBitmap, options)) {
            options.inBitmap = inBitmap;
        }
        options.inJustDecodeBounds = false;
        try {
            return BitmapFactory.decodeStream(is, outPadding, options);
        } catch (Exception e) {
            options.inBitmap = null;
            return BitmapFactory.decodeStream(is, outPadding, options);
        }
    }

    public static Bitmap decodeFileDescriptor(FileDescriptor fd) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFileDescriptor(fd, null, options);
        options.inSampleSize = 1;
        options.inMutable = true;
        Bitmap inBitmap = GlideBitmapPool.getBitmap(options.outWidth, options.outHeight, options.inPreferredConfig);
        if (Util.canUseForInBitmap(inBitmap, options)) {
            options.inBitmap = inBitmap;
        }
        options.inJustDecodeBounds = false;
        try {
            return BitmapFactory.decodeFileDescriptor(fd, null, options);
        } catch (Exception e) {
            options.inBitmap = null;
            return BitmapFactory.decodeFileDescriptor(fd, null, options);
        }
    }

    public static Bitmap decodeFileDescriptor(FileDescriptor fd, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFileDescriptor(fd, null, options);
        options.inSampleSize = Util.calculateInSampleSize(options, reqWidth, reqHeight);
        options.inMutable = true;
        Bitmap inBitmap = GlideBitmapPool.getBitmap(options.outWidth, options.outHeight, options.inPreferredConfig);
        if (Util.canUseForInBitmap(inBitmap, options)) {
            options.inBitmap = inBitmap;
        }
        options.inJustDecodeBounds = false;
        try {
            return BitmapFactory.decodeFileDescriptor(fd, null, options);
        } catch (Exception e) {
            options.inBitmap = null;
            return BitmapFactory.decodeFileDescriptor(fd, null, options);
        }
    }

    public static Bitmap decodeFileDescriptor(FileDescriptor fd, Rect outPadding) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFileDescriptor(fd, outPadding, options);
        options.inSampleSize = 1;
        options.inMutable = true;
        Bitmap inBitmap = GlideBitmapPool.getBitmap(options.outWidth, options.outHeight, options.inPreferredConfig);
        if (Util.canUseForInBitmap(inBitmap, options)) {
            options.inBitmap = inBitmap;
        }
        options.inJustDecodeBounds = false;
        try {
            return BitmapFactory.decodeFileDescriptor(fd, outPadding, options);
        } catch (Exception e) {
            options.inBitmap = null;
            return BitmapFactory.decodeFileDescriptor(fd, outPadding, options);
        }
    }

    public static Bitmap decodeFileDescriptor(FileDescriptor fd, Rect outPadding, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFileDescriptor(fd, outPadding, options);
        options.inSampleSize = Util.calculateInSampleSize(options, reqWidth, reqHeight);
        options.inMutable = true;
        Bitmap inBitmap = GlideBitmapPool.getBitmap(options.outWidth, options.outHeight, options.inPreferredConfig);
        if (Util.canUseForInBitmap(inBitmap, options)) {
            options.inBitmap = inBitmap;
        }
        options.inJustDecodeBounds = false;
        try {
            return BitmapFactory.decodeFileDescriptor(fd, outPadding, options);
        } catch (Exception e) {
            options.inBitmap = null;
            return BitmapFactory.decodeFileDescriptor(fd, outPadding, options);
        }
    }


}
