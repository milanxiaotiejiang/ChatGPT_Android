package com.seabreeze.robot.base.common.defenselib.hook;

public class SafeMode {
    private volatile static boolean isSafeMode;

    public static boolean isIsSafeMode() {
        return isSafeMode;
    }

    static synchronized void setIsSafeMode(boolean isSafeMode) {
        SafeMode.isSafeMode = isSafeMode;
    }
}
