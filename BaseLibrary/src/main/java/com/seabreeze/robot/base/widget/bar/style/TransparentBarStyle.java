package com.seabreeze.robot.base.widget.bar.style;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;

import com.seabreeze.robot.base.R;
import com.seabreeze.robot.base.widget.bar.SelectorDrawable;
import com.seabreeze.robot.base.widget.bar.TitleBarSupport;

/**
 * <pre>
 * @user : milanxiaotiejiang
 * @email : 765151629@qq.com
 * @version : 1.0
 * @date : 2020/9/3
 * @description : 透明主题样式实现（对应布局属性：app:barStyle="transparent"）
 * </pre>
 */
public class TransparentBarStyle extends CommonBarStyle {

    @Override
    public Drawable getBackButtonDrawable(Context context) {
        return TitleBarSupport.getDrawable(context, R.drawable.bar_arrows_left_white);
    }

    @Override
    public Drawable getLeftTitleBackground(Context context) {
        return new SelectorDrawable.Builder()
                .setDefault(new ColorDrawable(0x00000000))
                .setFocused(new ColorDrawable(0x22000000))
                .setPressed(new ColorDrawable(0x22000000))
                .build();
    }

    @Override
    public Drawable getRightTitleBackground(Context context) {
        return new SelectorDrawable.Builder()
                .setDefault(new ColorDrawable(0x00000000))
                .setFocused(new ColorDrawable(0x22000000))
                .setPressed(new ColorDrawable(0x22000000))
                .build();
    }

    @Override
    public Drawable getTitleBarBackground(Context context) {
        return new ColorDrawable(0x00000000);
    }

    @Override
    public int getLeftTitleColor(Context context) {
        return 0xFFFFFFFF;
    }

    @Override
    public int getTitleTitleColor(Context context) {
        return 0xFFFFFFFF;
    }

    @Override
    public int getRightTitleColor(Context context) {
        return 0xFFFFFFFF;
    }

    @Override
    public Drawable getLineDrawable(Context context) {
        return new ColorDrawable(0x00000000);
    }
}