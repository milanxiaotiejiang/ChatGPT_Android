package com.seabreeze.robot.data.websocket.model.hex.base;

import androidx.annotation.NonNull;

import com.seabreeze.robot.base.ext.utils.HexUtil;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;

/**
 * User: milan
 * Time: 2021/9/3 18:19
 * Des: 数据长度 + 指令数据 （指令ID + 指令数据）+ 同步标志 + 校验
 */
public abstract class PDU {

    private static final byte B_FF = (byte) 0xFF;
    private static final byte B_AA = (byte) 0xAA;
    private static final byte B_00 = (byte) 0x00;
    private static final byte B_01 = (byte) 0x01;
    private static final byte B_02 = (byte) 0x02;
    private static final byte B_03 = (byte) 0x03;

    protected static final byte[] HEAD_BYTES = new byte[]{B_FF, B_AA};
    protected static final byte[] BYTE_00 = new byte[]{B_00};
    protected static final byte[] BYTE_01 = new byte[]{B_01};
    protected static final byte[] BYTE_02 = new byte[]{B_02};
    protected static final byte[] BYTE_03 = new byte[]{B_03};

    public static final int MULTIPLE = 10000;

    //指令id
    private final int vendorId;

    public PDU(int vendorId) {
        this.vendorId = vendorId;
    }

    private byte[] mBytes;

    @NonNull
    public String pduMessage(long flag) {
        if (mBytes == null) {
            mBytes = buildBytes(flag);
        }
        return HexUtil.encodeHexStr(mBytes).toUpperCase(Locale.getDefault());
    }

    public static final int DATA_LENGTH = 1;
    public static final int VENDOR_LENGTH = 1;
    public static final int SYN_LENGTH = 1;
    public static final int CHECK_LENGTH = 1;

    private byte[] buildBytes(long flag) {

        byte[] payload = getPayload();

        int totalLength = HEAD_BYTES.length + DATA_LENGTH + VENDOR_LENGTH + payload.length + SYN_LENGTH + CHECK_LENGTH;
        byte[] result = new byte[totalLength];

        // 帧头
        System.arraycopy(HEAD_BYTES, 0, result, 0, HEAD_BYTES.length);
        // 数据长度
        result[HEAD_BYTES.length] = (byte) (totalLength - HEAD_BYTES.length - DATA_LENGTH);
        // 指令ID
        result[HEAD_BYTES.length + DATA_LENGTH] = (byte) vendorId;

        System.arraycopy(payload, 0, result, HEAD_BYTES.length + DATA_LENGTH + VENDOR_LENGTH, payload.length);

        result[totalLength - 2] = (byte) flag;

        byte check = 0;
        for (int i = 0; i < totalLength - 1; i++) {
            check ^= result[i];
        }
        result[totalLength - 1] = check;

        return result;
    }

    @NotNull
    public abstract byte[] getPayload();

}
