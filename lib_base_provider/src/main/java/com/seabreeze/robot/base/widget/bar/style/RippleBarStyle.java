package com.seabreeze.robot.base.widget.bar.style;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;

import com.seabreeze.robot.base.widget.bar.TitleBarSupport;

/**
 * <pre>
 * @user : milanxiaotiejiang
 * @email : 765151629@qq.com
 * @version : 1.0
 * @date : 2020/9/3
 * @description : 水波纹样式实现（对应布局属性：app:barStyle="ripple"）
 * </pre>
 */
public class RippleBarStyle extends TransparentBarStyle {

    @Override
    public Drawable getLeftTitleBackground(Context context) {
        Drawable drawable = getRippleDrawable(context);
        if (drawable != null) {
            return drawable;
        }
        return super.getLeftTitleBackground(context);
    }

    @Override
    public Drawable getRightTitleBackground(Context context) {
        Drawable drawable = getRippleDrawable(context);
        if (drawable != null) {
            return drawable;
        }
        return super.getRightTitleBackground(context);
    }

    /**
     * 获取水波纹的点击效果
     */
    public Drawable getRippleDrawable(Context context) {
        TypedValue typedValue = new TypedValue();
        if (context.getTheme().resolveAttribute(android.R.attr.selectableItemBackground, typedValue, true)) {
            return TitleBarSupport.getDrawable(context, typedValue.resourceId);
        }
        return null;
    }
}