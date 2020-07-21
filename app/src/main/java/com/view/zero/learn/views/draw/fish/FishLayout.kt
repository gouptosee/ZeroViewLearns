package com.view.zero.learn.views.draw.fish

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.animation.AccelerateInterpolator
import android.widget.FrameLayout
import android.widget.ImageView


interface FishInterface {
    fun accelSwim(acc: Boolean)

    fun getFishHeadPointF():PointF

    fun getFishAngle():Float

    fun getFishBaseLength():Float
}


class FishLayout @JvmOverloads constructor(
    context: Context, attributeSet: AttributeSet? = null, intstyle: Int = 0
) : FrameLayout(context, attributeSet, intstyle) {
    private lateinit var mFish: ImageView
    private var fishSize = 0
    private var centerX = 0f
    private var centerY = 0f

    private var mPaint = Paint()
    private var ripper = 0f
    private var maxRipper = 100f
    private var mAlpha = 1f
    private var animing = false
    private var fishInterface: FishInterface? = null

    init {
        setWillNotDraw(false)
        mPaint.isAntiAlias = true
        mPaint.isDither = true
        mPaint.style = Paint.Style.STROKE;
        mPaint.strokeWidth = 5f
        mPaint.setColor(Color.parseColor("#00aaaa"))

    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        val fishLeft = (centerX - fishSize / 2).toInt()
        val fishTop = (centerY - fishSize / 2).toInt()
        mFish.layout(fishLeft, fishTop, fishLeft + fishSize, fishTop + fishSize)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        mFish = getChildAt(0) as ImageView
    }


    fun initFish(fish: Drawable) {
        mFish.setImageDrawable(fish)
        fishInterface = fish as FishInterface
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (fishSize == 0) fishSize = mFish.measuredWidth
        centerX = (measuredWidth / 2).toFloat()
        centerY = (measuredHeight / 2).toFloat()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event!!.action) {
            MotionEvent.ACTION_UP -> {
                if (mFish.animation != null) {
                    mFish.clearAnimation()
                }
                val curX = event.x
                var curY = event.y

                DrawUtils.solveSwiming(mFish, centerX, centerY, curX, curY, 1000)

                centerX = curX
                centerY = curY
                animRipper()
            }
        }
        return true
    }


    fun animRipper() {
        val ripperAnim = ObjectAnimator.ofFloat(this, "ripper", 0f, maxRipper)
        ripperAnim.setDuration(1000)
        ripperAnim.interpolator = AccelerateInterpolator()
        ripperAnim.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                fishInterface?.accelSwim(false)

            }

            override fun onAnimationStart(animation: Animator?) {
                super.onAnimationStart(animation)
                fishInterface?.accelSwim(true)
            }
        })
        ripperAnim.start()

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (animing) {
            canvas!!.drawCircle(centerX, centerY, ripper, mPaint)
        }
    }

    fun setRipper(ripper: Float) {
        animing = true
        if (ripper == maxRipper) animing = false
        this.ripper = ripper
        mAlpha = 1 - ripper / maxRipper
        mPaint.alpha = (mAlpha * 255).toInt()
        invalidate()
    }


}