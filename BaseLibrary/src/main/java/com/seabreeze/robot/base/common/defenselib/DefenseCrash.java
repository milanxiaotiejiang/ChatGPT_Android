package com.seabreeze.robot.base.common.defenselib;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.android.hidden.pass.HiddenApiBypass;
import com.seabreeze.robot.base.common.defenselib.handler.ExceptionDispatcher;
import com.seabreeze.robot.base.common.defenselib.handler.IExceptionHandler;
import com.seabreeze.robot.base.common.defenselib.hook.HookHandler;
import com.seabreeze.robot.base.common.defenselib.hook.HookInstrumentation;
import com.seabreeze.robot.base.common.defenselib.hook.HookThreadLoop;
import com.seabreeze.robot.base.common.defenselib.hook.IHook;
import com.seabreeze.robot.base.common.defenselib.hook.SafeMode;

public final class DefenseCrash {
    public static final String TAG = "DefenseCrash";

    private static ExceptionDispatcher mExceptionDispatcher;

    private volatile static boolean installed = false;

    private volatile static boolean initialized = false;

    private static IHook hookThread;

    private static IHook hookInstrumentation;

    private static IHook hookHandler;

    private DefenseCrash() {
    }

    /**
     * initialize the defense lib
     *
     * @param context
     */
    public static void initialize(Context context) {
        if (initialized) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            HiddenApiBypass.addHiddenApiExemptions("L");
        }
        initialized = true;
        mExceptionDispatcher = new ExceptionDispatcher();
        hookThread = new HookThreadLoop(mExceptionDispatcher);
        hookInstrumentation = new HookInstrumentation(mExceptionDispatcher, context);
        hookHandler = new HookHandler(mExceptionDispatcher);
    }

    /**
     * install the defense fire wall
     *
     * @param handler
     */
    public static boolean install(IExceptionHandler handler) {
        return install(handler, true, true, true);
    }

    /**
     * install the defense fire wall
     *
     * @param handler
     */
    public static boolean install(IExceptionHandler handler, boolean isHookIns, boolean isHookThread, boolean isHookHandler) {
        if (!initialized) {
            Log.e(TAG, "need call DefenseCrash.initialize() first");
            return false;
        }
        if (installed) {
            return true;
        }
        installed = true;
        mExceptionDispatcher.setIExceptionHandler(handler);
        if (isHookIns) {
            hookInstrumentation.hook();
        }
        if (isHookThread) {
            hookThread.hook();
        }
        if (isHookHandler) {
            hookHandler.hook();
        }
        return true;
    }

    /**
     * uninstall the defense fire wall
     */
    public static boolean unInstall() {
        if (!initialized) {
            Log.e(TAG, "need call DefenseCrash.initialize() first");
            return false;
        }
        if (!installed) {
            return false;
        }
        installed = false;
        hookInstrumentation.unHook();
        hookThread.unHook();
        hookHandler.unHook();
        return true;
    }

    public static boolean isIsSafeMode() {
        return SafeMode.isIsSafeMode();
    }

}
