package com.view.zero.learn.views.nestscroll

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.ViewConfiguration
import android.widget.LinearLayout
import android.widget.Scroller
import androidx.core.view.NestedScrollingChild
import androidx.core.view.NestedScrollingChildHelper
import androidx.core.view.ViewCompat
import com.view.zero.learn.LogTag
import com.view.zero.learn.utils.LogUtils
import kotlin.math.sign

class MNestChild @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    intStyle: Int = 0
) : LinearLayout(context, attributeSet, intStyle), NestedScrollingChild {

    private val mChildHelper: NestedScrollingChildHelper = NestedScrollingChildHelper(this)

    init {
        mChildHelper.isNestedScrollingEnabled = true
    }

    private val minTouchSlop = ViewConfiguration.get(context).scaledTouchSlop
    private var totalHeight: Int = 0
    private var velocityTracker = VelocityTracker.obtain()
    private val maxVelocityY = ViewConfiguration.get(context).scaledMaximumFlingVelocity
    private val minVelocityY = ViewConfiguration.get(context).scaledMinimumFlingVelocity

    private val mScroller = Scroller(context)

    private val consumed = intArrayOf(0, 0)

    override fun dispatchNestedFling(
        velocityX: Float,
        velocityY: Float,
        consumed: Boolean
    ): Boolean {
        return super.dispatchNestedFling(velocityX, velocityY, consumed)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        totalHeight = 0

        for (i in 0 until childCount) {
            val childView = getChildAt(i)
            totalHeight += childView.measuredHeight
        }


    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        velocityTracker?.recycle()
    }


    private var lastTouchedY = 0
    private var lastFlingY = 0


    override fun onTouchEvent(event: MotionEvent): Boolean {
        val actionMask = event.actionMasked
        when (actionMask) {
            MotionEvent.ACTION_DOWN -> {
                if (!mScroller.isFinished) {
                    mScroller.abortAnimation()
                    mScroller.forceFinished(true)
                }

                mChildHelper.stopNestedScroll(ViewCompat.TYPE_TOUCH)
                mChildHelper.stopNestedScroll(ViewCompat.TYPE_NON_TOUCH)

                //其实就是设置setNestedScrollingParentForType，如果父亲有处理的意愿就设置，否则父亲就不处理
                mChildHelper.startNestedScroll(
                    ViewCompat.SCROLL_AXIS_VERTICAL,
                    ViewCompat.TYPE_TOUCH
                )
                lastTouchedY = event.rawY.toInt()
            }
            MotionEvent.ACTION_MOVE -> {
                velocityTracker.addMovement(event)

                val curEventY = event.rawY.toInt()
                var delY = (lastTouchedY - curEventY).toInt()

                if (mChildHelper.dispatchNestedPreScroll(0, delY, consumed, null)) {
                    delY -= consumed[1]
                }

                if (delY != 0) {
                    val curScrollY = scrollY
                    tryScrollTo(0, delY)
                    val resScrollY = scrollY

                    val unconsumeY = delY - (resScrollY - curScrollY)

                    mChildHelper.dispatchNestedScroll(
                        0,
                        0,
                        delY,
                        unconsumeY,
                        null,
                        ViewCompat.TYPE_TOUCH
                    )
                }
                consumed[1] = 0
                lastTouchedY = curEventY
            }
            MotionEvent.ACTION_UP -> {
                velocityTracker.computeCurrentVelocity(1000)
                var velocityY = velocityTracker.yVelocity
                if (Math.abs(velocityY) > minVelocityY) {
                    if (Math.abs(velocityY) > maxVelocityY) {
                        velocityY = velocityY.sign * maxVelocityY
                    }
                    if (!mChildHelper.dispatchNestedPreFling(0f, -velocityY)) {
//                        fling(-velocityY.toInt())
                    }
                }

                mChildHelper.stopNestedScroll(ViewCompat.TYPE_TOUCH)
            }
        }
        return true
    }


    fun tryScrollTo(delX: Int, dexY: Int) {
        if (dexY == 0) return
        var currentScrollY = scrollY
        var targetScrollY = currentScrollY + dexY
        if (targetScrollY < 0) targetScrollY = 0
        if (targetScrollY > totalHeight - measuredHeight) targetScrollY =
            totalHeight - measuredHeight
        scrollTo(0, targetScrollY)

    }



    fun fling(velocityY: Int) {
        if (childCount > 0) {
            mChildHelper.startNestedScroll(
                ViewCompat.SCROLL_AXIS_VERTICAL,
                ViewCompat.TYPE_NON_TOUCH
            )
            lastFlingY = scrollY
            mScroller.fling(
                scrollX, scrollY,
                0, -velocityY,
                0, 0, Int.MIN_VALUE, Int.MAX_VALUE
            )
            invalidate()
        }
    }

    override fun computeScroll() {

        if (mScroller.isFinished) return

        mScroller.computeScrollOffset()

        var curY = mScroller.currY
        var delY = lastFlingY - curY  //往上滑是负的
        if (mChildHelper.dispatchNestedPreScroll(
                0,
                delY,
                consumed,
                null,
                ViewCompat.TYPE_NON_TOUCH
            )
        ) {
            delY -= consumed[1]
        }

        tryScrollTo(0, delY)

        invalidate()

        lastFlingY = curY

    }


}

