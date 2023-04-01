package com.seabreeze.robot.base.widget.bar;

import android.view.View;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * <pre>
 * @user : milanxiaotiejiang
 * @email : 765151629@qq.com
 * @version : 1.0
 * @date : 2020/9/3
 * @description : 标题栏点击监听接口
 * </pre>
 */
public interface OnTitleBarListener {

    /**
     * 左项被点击
     *
     * @param view 被点击的左项View
     */
    default void onLeftClick(@NotNull View view) {
    }

    /**
     * 标题被点击
     *
     * @param view 被点击的标题View
     */
    default void onTitleClick(@NotNull View view) {
    }

    /**
     * 右项被点击
     *
     * @param view 被点击的右项View
     */
    default void onRightClick(@NotNull View view) {
    }

    /**
     * 右项被长按
     *
     * @param view 被长按的右项View
     */
    default void onRightLongClick(@Nullable View view) {
    }
}