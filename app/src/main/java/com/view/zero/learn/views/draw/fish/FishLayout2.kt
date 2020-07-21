package com.view.zero.learn.views.draw.fish

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import android.widget.ImageView
import com.view.zero.learn.LogTag


interface FishInterface2 {
    fun accelSwim(acc: Boolean)

    fun getFishHeadPointF():PointF

    fun getFishAngle():Float

    fun getFishBaseLength():Float

    fun setFishAngle(angle: Float)
}

class FishLayout2 @JvmOverloads constructor(
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
    private var fishInterface: FishInterface2? = null
    private var mPath:Path = Path()
    private val duration = 3000L

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
        fishInterface = fish as FishInterface2
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

                val path = DrawUtils2.solveSwiming(mFish, centerX-fishSize/2, centerY-fishSize/2, curX-fishSize/2, curY-fishSize/2, fishInterface!!.getFishAngle(),200f,duration)
                mPath.reset()
                mPath = path

                centerX = curX
                centerY = curY
                animRipper(path)
            }
        }
        return true
    }


    fun animRipper(path:Path) {
        val ripperAnim = ObjectAnimator.ofFloat(this, "ripper", 0f, maxRipper)
        ripperAnim.setDuration(duration)
        ripperAnim.interpolator = DecelerateInterpolator()
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
        val pathMeasure = PathMeasure(path,false)
        val floatArray = FloatArray(2);
        ripperAnim.addUpdateListener(object :ValueAnimator.AnimatorUpdateListener{
            override fun onAnimationUpdate(animation: ValueAnimator?) {
                val factor = animation!!.animatedFraction
                 pathMeasure.getPosTan(pathMeasure.length*factor,null,floatArray)
                val angle = Math.toDegrees(Math.atan2(-floatArray[1].toDouble(),
                    floatArray[0].toDouble()
                )).toFloat()
                fishInterface!!.setFishAngle(angle)
                Log.e(LogTag,"angle->>>>$angle")
            }

        })

        ripperAnim.start()

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (animing) {
            canvas!!.drawCircle(centerX, centerY, ripper, mPaint)
        }
        canvas!!.drawPath(mPath,mPaint)
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