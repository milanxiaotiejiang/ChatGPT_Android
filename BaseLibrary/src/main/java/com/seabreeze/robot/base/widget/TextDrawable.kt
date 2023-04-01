package com.seabreeze.robot.base.widget

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import com.seabreeze.robot.base.R
import com.seabreeze.robot.base.widget.round.RoundTextView
import me.jessyan.autosize.utils.AutoSizeUtils


class TextDrawable : RoundTextView {
    private var drawableLeft: Drawable? = null
    private var drawableRight: Drawable? = null
    private var drawableTop: Drawable? = null
    private var drawableBottom: Drawable? = null
    private var leftWidth = 0
    private var rightWidth = 0
    private var topWidth = 0
    private var bottomWidth = 0
    private var leftHeight = 0
    private var rightHeight = 0
    private var topHeight = 0
    private var bottomHeight = 0
    private var mContext: Context

    constructor(context: Context) : super(context) {
        mContext = context
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        mContext = context
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        mContext = context
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        val typedArray: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.TextDrawable)
        drawableLeft = typedArray.getDrawable(R.styleable.TextDrawable_leftDrawable)
        drawableRight = typedArray.getDrawable(R.styleable.TextDrawable_rightDrawable)
        drawableTop = typedArray.getDrawable(R.styleable.TextDrawable_topDrawable)
        drawableBottom = typedArray.getDrawable(R.styleable.TextDrawable_bottomDrawable)
        if (drawableLeft != null) {
            leftWidth = typedArray.getDimensionPixelOffset(
                R.styleable.TextDrawable_leftDrawableWidth,
                AutoSizeUtils.dp2px(context, 20f)
            )
            leftHeight = typedArray.getDimensionPixelOffset(
                R.styleable.TextDrawable_leftDrawableHeight,
                AutoSizeUtils.dp2px(context, 20f)
            )
        }
        if (drawableRight != null) {
            rightWidth = typedArray.getDimensionPixelOffset(
                R.styleable.TextDrawable_rightDrawableWidth,
                AutoSizeUtils.dp2px(context, 20f)
            )
            rightHeight = typedArray.getDimensionPixelOffset(
                R.styleable.TextDrawable_rightDrawableHeight,
                AutoSizeUtils.dp2px(context, 20f)
            )
        }
        if (drawableTop != null) {
            topWidth = typedArray.getDimensionPixelOffset(
                R.styleable.TextDrawable_topDrawableWidth,
                AutoSizeUtils.dp2px(context, 20f)
            )
            topHeight = typedArray.getDimensionPixelOffset(
                R.styleable.TextDrawable_topDrawableHeight,
                AutoSizeUtils.dp2px(context, 20f)
            )
        }
        if (drawableBottom != null) {
            bottomWidth = typedArray.getDimensionPixelOffset(
                R.styleable.TextDrawable_bottomDrawableWidth,
                AutoSizeUtils.dp2px(context, 20f)
            )
            bottomHeight = typedArray.getDimensionPixelOffset(
                R.styleable.TextDrawable_bottomDrawableHeight,
                AutoSizeUtils.dp2px(context, 20f)
            )
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        drawableLeft?.setBounds(0, 0, leftWidth, leftHeight)
        drawableRight?.setBounds(0, 0, rightWidth, rightHeight)
        drawableTop?.setBounds(0, 0, topWidth, topHeight)
        drawableBottom?.setBounds(0, 0, bottomWidth, bottomHeight)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        this.setCompoundDrawables(drawableLeft, drawableTop, drawableRight, drawableBottom)
    }

    /**
     * 设置左侧图片并重绘
     */
    fun setDrawableLeft(drawableLeft: Drawable?) {
        this.drawableLeft = drawableLeft
        invalidate()
    }

    /**
     * 设置左侧图片并重绘
     */
    fun setDrawableLeft(drawableLeftRes: Int) {
        drawableLeft = mContext.getResources().getDrawable(drawableLeftRes)
        invalidate()
    }

    /**
     * 设置右侧图片并重绘
     */
    fun setDrawableRight(drawableRight: Drawable?) {
        this.drawableRight = drawableLeft
        invalidate()
    }

    /**
     * 设置右侧图片并重绘
     */
    fun setDrawableRight(drawableRightRes: Int) {
        drawableRight = mContext.getResources().getDrawable(drawableRightRes)
        invalidate()
    }

    /**
     * 设置上部图片并重绘
     */
    fun setDrawable(drawableTop: Drawable?) {
        this.drawableTop = drawableTop
        invalidate()
    }

    /**
     * 设置上部图片并重绘
     */
    fun setDrawableTop(drawableTopRes: Int) {
        drawableTop = mContext.getResources().getDrawable(drawableTopRes)
        invalidate()
    }

    /**
     * 设置底部图片并重绘
     */
    fun setDrawableBottom(drawableBottom: Drawable?) {
        this.drawableBottom = drawableBottom
        invalidate()
    }

    /**
     * 设置底侧图片并重绘
     */
    fun setDrawableBottom(drawableBottomRes: Int) {
        drawableBottom = mContext.getResources().getDrawable(drawableBottomRes)
        invalidate()
    }
}