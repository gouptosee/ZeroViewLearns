package com.view.zero.learn.views.draw.photoview

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
import com.view.zero.learn.LogTag

class PhotoView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    intStyle: Int = 0
) : View(context, attrs, intStyle) {

    private var mPaint = Paint()
    private var mBitmap: Bitmap? = null
    private var mOriginLeft: Float = 0f
    private var mOriginTop: Float = 0f


    private var mCurrentScale: Float = 1f
    private var mSmallScale: Float = 1f
    private var mLargetScale: Float = 1f
    private val LARGER_RATE = 3f

    private var mCurrentOffsetX: Float = 0f
    private var mCurrentOffsetY: Float = 0f

    private var mTouchGesture: GestureDetector
    private var mScaleGesture: ScaleGestureDetector

    init {
        mPaint.isDither = true
        mPaint.isAntiAlias = true
        mTouchGesture = GestureDetector(context, PhotoGestureDetector())
        mScaleGesture = ScaleGestureDetector(context, PhotoScaleGestureDetector())
    }

    private inner class PhotoScaleGestureDetector :
        ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector?): Boolean {
            return super.onScale(detector)
        }
    }

    private inner class PhotoGestureDetector : GestureDetector.SimpleOnGestureListener() {

        override fun onDoubleTap(e: MotionEvent?): Boolean {
            Log.e(LogTag,"onDoubleTap ->>> ")
            if (mCurrentScale == mSmallScale) {
                mCurrentScale = mLargetScale
            }else{
                mCurrentScale = mSmallScale
            }
            invalidate()
            return super.onDoubleTap(e)
        }

        override fun onScroll(
            e1: MotionEvent?,
            e2: MotionEvent?,
            distanceX: Float,
            distanceY: Float
        ): Boolean {
            return super.onScroll(e1, e2, distanceX, distanceY)
        }
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (mBitmap != null) {

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
        return result
    }

}