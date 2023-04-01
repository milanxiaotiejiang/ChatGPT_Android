package com.seabreeze.robot.base.widget

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import com.seabreeze.robot.base.ext.tool.dp2px

/**
 * User: milan
 * Time: 2022/6/27 10:29
 * Des:
 */
class CircularView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private lateinit var mPaint: Paint

    private var mWidth = 0f
    private var mEyeWidth = 0f

    private var mPadding = 0f
    private var startAngle = 0f
    private var isSmile = false
    private var rectF = RectF()

    private var mAnimatedValue = 0f

    init {
        initPaint()
    }

    private fun initPaint() {
        mPaint = Paint()
        mPaint.isAntiAlias = true
        mPaint.style = Paint.Style.STROKE
        mPaint.color = Color.WHITE
        mPaint.strokeWidth = dp2px(2f).toFloat()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mWidth = if (measuredWidth > height) measuredHeight.toFloat() else measuredWidth.toFloat()
        mPadding = dp2px(10f)
        mEyeWidth = dp2px(3f)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        rectF = RectF(mPadding, mPadding, mWidth - mPadding, mWidth - mPadding)
        mPaint.style = Paint.Style.STROKE
        canvas.drawArc(rectF, startAngle, 180f, false, mPaint) //第四个参数是否显示半径
        mPaint.style = Paint.Style.FILL
        if (isSmile) {
            canvas.drawCircle(mPadding + mEyeWidth + mEyeWidth / 2, mWidth / 3, mEyeWidth, mPaint)
            canvas.drawCircle(
                mWidth - mPadding - mEyeWidth - mEyeWidth / 2,
                mWidth / 3,
                mEyeWidth,
                mPaint
            )
        }
    }

    fun setViewColor(color: Int) {
        mPaint.color = color
        postInvalidate()
    }

    fun startAnim() {
        stopAnim()
        startViewAnim(0f, 1f, 500)
    }

    fun startAnim(time: Int) {
        stopAnim()
        startViewAnim(0f, 1f, time.toLong())
    }


    private var valueAnimator: ValueAnimator? = null

    private fun startViewAnim(startF: Float, endF: Float, time: Long): ValueAnimator? {
        valueAnimator = ValueAnimator.ofFloat(startF, endF)
        valueAnimator?.apply {
            duration = time
            interpolator = LinearInterpolator()
            repeatCount = ValueAnimator.INFINITE
            if (ValueAnimator.RESTART == setAnimRepeatMode()) {
                repeatMode = ValueAnimator.RESTART
            } else if (ValueAnimator.REVERSE == setAnimRepeatMode()) {
                repeatMode = ValueAnimator.REVERSE
            }
            addUpdateListener { valueAnimator ->
                OnAnimationUpdate(valueAnimator)
            }
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                }

                override fun onAnimationStart(animation: Animator) {
                    super.onAnimationStart(animation)
                }

                override fun onAnimationRepeat(animation: Animator) {
                    super.onAnimationRepeat(animation)
                }
            })
            if (!isRunning) {
                start()
            }
        }

        return valueAnimator
    }

    private fun stopAnim() {
        valueAnimator?.apply {
            clearAnimation()
            repeatCount = 0
            cancel()
            end()
            if (onStopAnim() == 0) {
                repeatCount = 0
                cancel()
                end()
            }
        }
    }

    private fun OnAnimationUpdate(valueAnimator: ValueAnimator) {
        mAnimatedValue = valueAnimator.animatedValue as Float
        if (mAnimatedValue < 0.5) {
            isSmile = false
            startAngle = 720 * mAnimatedValue
        } else {
            startAngle = 720f
            isSmile = true
        }
        invalidate()
    }

    private fun onStopAnim(): Int {
        isSmile = false
        mAnimatedValue = 0f
        startAngle = 0f
        return 0
    }

    private fun setAnimRepeatMode(): Int {
        return ValueAnimator.RESTART
    }

}