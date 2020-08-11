package com.view.zero.learn.views.draw.photoview

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.widget.OverScroller
import com.view.zero.learn.LogTag
import kotlin.math.abs

class PhotoTestView1 @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    intStyle: Int = 0
) : View(context, attrs, intStyle) {

    private var mPaint = Paint()
    private var mBitmap: Bitmap? = null
    private var mOriginLeft: Float = 0f
    private var mOriginTop: Float = 0f

    private var overScroller: OverScroller


    private var mCurrentScale: Float = 1f
    private var mSmallScale: Float = 1f
    private var mLargetScale: Float = 1f
    private val LARGER_RATE = 3f

    private var mCurrentOffsetX: Float = 0f
    private var mCurrentOffsetY: Float = 0f

    private var mTouchGesture: GestureDetector
    private var mScaleGesture: ScaleGestureDetector

    private val Duration_Double_Tab = 300L

    private var mMinOffsetX = 0f
    private var mMaxOffsetX = 0f
    private var mMinOffsetY = 0f
    private var mMaxOffsetY = 0f

    private val overScrollRange = 100
    private var backAnimator: Animator? = null

    init {
        mPaint.isDither = true
        mPaint.isAntiAlias = true
        mTouchGesture = GestureDetector(context, PhotoGestureDetector())
        mScaleGesture = ScaleGestureDetector(context, PhotoScaleGestureDetector())
        overScroller = OverScroller(context)
    }


    private inner class FlipingRunner : Runnable {
        override fun run() {
            if (overScroller.computeScrollOffset()) {
                mCurrentOffsetX = overScroller.currX.toFloat()
                mCurrentOffsetY = overScroller.currY.toFloat()
                invalidate()
                postOnAnimation(this)
            }
        }
    }

    fun getScaleAnim(startScale: Float, endScale: Float): Animator {
        val anim = ObjectAnimator.ofFloat(this, "currentScale", startScale, endScale)
        anim.duration = Duration_Double_Tab
        return anim
    }

    fun getBackAnim(): Animator? {
        Log.e(
            LogTag,
            "$mCurrentOffsetX  $mMinOffsetX  $mMaxOffsetX  :  $mCurrentOffsetY  $mMinOffsetY $mMaxOffsetY"
        )
        if (mCurrentOffsetX < mMinOffsetX || mCurrentOffsetX > mMaxOffsetX || mCurrentOffsetY < mMinOffsetY || mCurrentOffsetY > mMaxOffsetY) {
            var afterX = if (mCurrentOffsetX > mMaxOffsetX) mMaxOffsetX else mCurrentOffsetX
            var afterY = if (mCurrentOffsetY > mMaxOffsetY) mMaxOffsetY else mCurrentOffsetY

            afterX = if (afterX < mMinOffsetX) mMinOffsetX else afterX
            afterY = if (afterY < mMinOffsetX) mMinOffsetX else afterY

            Log.e(LogTag, "after  $afterX $afterY")
            val proX = PropertyValuesHolder.ofFloat("currentOffsetX", mCurrentOffsetX, afterX)
            val proY = PropertyValuesHolder.ofFloat("currentOffsetY", mCurrentOffsetY, afterY)
            val anim = ObjectAnimator.ofPropertyValuesHolder(this, proX, proY)
            anim.duration = Duration_Double_Tab
            return anim
        }
        return null
    }

    private inner class PhotoScaleGestureDetector :
        ScaleGestureDetector.SimpleOnScaleGestureListener() {

        private var originScale: Float = 1f
        override fun onScale(detector: ScaleGestureDetector?): Boolean {
            val factor = detector!!.scaleFactor  //获取双指缩放比例大小
            var afterScale = originScale * factor  //计算最终要缩放的比例大小
            if (afterScale < mSmallScale) {
                afterScale = mSmallScale
            } else if (afterScale > mLargetScale) {
                afterScale = mLargetScale
            }
            mCurrentScale = afterScale
            if (mCurrentScale > mSmallScale) islarge = true
            invalidate()
            return super.onScale(detector)
        }

        override fun onScaleBegin(detector: ScaleGestureDetector?): Boolean {
            originScale = mCurrentScale //记录此时的初始缩放值，整个过程都以这个为基准
            return super.onScaleBegin(detector)
        }
    }

    private var islarge = false


    private inner class PhotoGestureDetector : GestureDetector.SimpleOnGestureListener() {


        override fun onSingleTapUp(e: MotionEvent?): Boolean {
            if (!overScroller.isFinished) {
                overScroller.forceFinished(true)
                overScroller.abortAnimation()
            }
            return super.onSingleTapUp(e)
        }

        override fun onDoubleTap(e: MotionEvent?): Boolean {
            Log.e(LogTag, "onDoubleTap ->>> ${e!!.action}")
            val startScale = mCurrentScale
            var endScale = 1f
            if (mCurrentScale == mSmallScale || !islarge) {
                //根据点击重心进行放大 算出最终的偏移距离
                mCurrentOffsetX = -(e!!.x - width / 2) * LARGER_RATE
                mCurrentOffsetY = -(e!!.y - height / 2) * LARGER_RATE

                endScale = mLargetScale
                islarge = true

                //放大需要判断偏移量边界，不能超过图片范围
                fixOffsets(endScale)
                fixCurrentOffsets()


            } else {
                endScale = mSmallScale
                islarge = false
            }


            Log.e(LogTag, "onDoubleTap  endScale ->>> ${endScale}")

            val anim = getScaleAnim(startScale, endScale)
            anim.start();

            return super.onDoubleTap(e)
        }

        override fun onScroll(
            e1: MotionEvent?,
            e2: MotionEvent?,
            distanceX: Float,
            distanceY: Float
        ): Boolean {
            Log.e(LogTag, "onScroll ->>>> $distanceX  $distanceY")
            mCurrentOffsetX -= distanceX
            mCurrentOffsetY -= distanceY
            fixOffsets(mCurrentScale)
            fixCurrentOffsets()
            invalidate()
            return super.onScroll(e1, e2, distanceX, distanceY)
        }

        override fun onDown(e: MotionEvent?): Boolean {
            return true
        }

        override fun onFling(
            e1: MotionEvent?,
            e2: MotionEvent?,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            fixOffsets(mCurrentScale)
            fixCurrentOffsets()
            overScroller.fling(
                mCurrentOffsetX.toInt(),
                mCurrentOffsetY.toInt(),
                velocityX.toInt(),
                velocityY.toInt(),
                mMinOffsetX.toInt(),
                mMaxOffsetX.toInt(),
                mMinOffsetY.toInt(),
                mMaxOffsetY.toInt()
                ,
                overScrollRange,
                overScrollRange
            )
            postOnAnimation(FlipingRunner())
            return super.onFling(e1, e2, velocityX, velocityY)
        }
    }


    fun fixOffsets(scaleSize: Float) {
        mMinOffsetX = -abs((width - scaleSize * mBitmap!!.width) / 2f)
        mMaxOffsetX = -mMinOffsetX

        mMinOffsetY = -abs((height - scaleSize * mBitmap!!.height) / 2f)
        mMaxOffsetY = -mMinOffsetY
    }

    //TODO
    fun fixOverCurrentOffsets() {


//        if (mCurrentOffsetX < mMinOffsetX - overScrollRange) {
//            mCurrentOffsetX = mMinOffsetX - overScrollRange
//        } else if (mCurrentOffsetX > mMaxOffsetX + overScrollRange) {
//            mCurrentOffsetX = mMaxOffsetX + overScrollRange
//        }
//
//        if (mCurrentOffsetY < mMinOffsetY - overScrollRange) {
//            mCurrentOffsetY = mMinOffsetY - overScrollRange
//        } else if (mCurrentOffsetY > mMaxOffsetY + overScrollRange) {
//            mCurrentOffsetY = mMaxOffsetY + overScrollRange
//        }
        fixCurrentOffsets()
    }


    fun fixCurrentOffsets() {
        if (mCurrentOffsetX < mMinOffsetX) {
            mCurrentOffsetX = mMinOffsetX
        } else if (mCurrentOffsetX > mMaxOffsetX) {
            mCurrentOffsetX = mMaxOffsetX
        }

        if (mCurrentOffsetY < mMinOffsetY) {
            mCurrentOffsetY = mMinOffsetY
        } else if (mCurrentOffsetY > mMaxOffsetY) {
            mCurrentOffsetY = mMaxOffsetY
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (mBitmap != null) {
            //算出当前缩放比例，等比例计算偏移量，因为需要根据点击点作为轴心进行缩放
            val scaleFaction: Float = (mCurrentScale - mSmallScale) / (mLargetScale - mSmallScale)
//            Log.e(LogTag, "offsets  ->>>> $mCurrentOffsetX  $mCurrentOffsetY  $scaleFaction")
//            fixOffsets()
//            fixCurrentOffsets()
            val transX = mCurrentOffsetX * scaleFaction
            val transY = mCurrentOffsetY * scaleFaction
            canvas!!.translate(transX, transY)
            canvas!!.scale(mCurrentScale, mCurrentScale, width / 2f, height / 2f)
            canvas!!.drawBitmap(mBitmap!!, mOriginLeft, mOriginTop, mPaint)
        }
    }

    fun setImageRes(resId: Int) {
        mBitmap = BitmapFactory.decodeResource(resources, resId)
        invalidate()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mOriginLeft = (width - mBitmap!!.width) / 2f
        mOriginTop = (height - mBitmap!!.height) / 2f
        mBitmap?.let {
            if (it!!.width < it!!.height) {
                mSmallScale = width / (mBitmap!!.width.toFloat())
            } else {
                mSmallScale = height / (mBitmap!!.height.toFloat())
            }
            mLargetScale = mSmallScale * LARGER_RATE
            mCurrentScale = mSmallScale
        }
    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        var result = mScaleGesture.onTouchEvent(event)
        if (!mScaleGesture.isInProgress) {
            result = mTouchGesture.onTouchEvent(event)
        }


        //TODO 滑动抬起回弹
//        when (event!!.action) {
//            MotionEvent.ACTION_UP -> {
//                if (!result) {
//                    if (backAnimator != null && backAnimator!!.isRunning)
//                        backAnimator!!.cancel()
//                    backAnimator = getBackAnim()
//                    if (backAnimator != null) backAnimator!!.start()
//                }
//            }
//        }
        Log.e(LogTag, "onTouchEvent result ->>>$result ")
        return result
    }

    fun setCurrentScale(currentScale: Float) {
        mCurrentScale = currentScale
        invalidate()
    }

    fun setCurrentOffsetX(offsetX: Float) {
        mCurrentOffsetX = offsetX
        invalidate()
    }

    fun setCurrentOffsetY(offsetY: Float) {
        mCurrentOffsetY = offsetY
        invalidate()
    }


}