package com.view.zero.learn.views.nestscroll

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.view.ViewConfiguration
import android.widget.LinearLayout
import android.widget.Scroller
import androidx.core.view.NestedScrollingChild
import androidx.core.view.NestedScrollingChildHelper
import androidx.core.view.ViewCompat
import com.view.zero.learn.LogTag
import com.view.zero.learn.utils.LogUtils
import java.util.jar.Attributes
import kotlin.math.sign

//默认最后一个会被固定 随便写写
class MAppLayout @JvmOverloads constructor(
    context: Context,
    attributes: AttributeSet? = null,
    intStyle: Int = 0
) : LinearLayout(context, attributes, intStyle), MAppLayoutInterface, NestedScrollingChild {

    private lateinit var lastChildPined: View
    private var pinedHeight = 0

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        lastChildPined = getChildAt(childCount - 1)
        pinedHeight = lastChildPined.measuredHeight
    }

    override fun getPinedHeight(): Int {
        return pinedHeight
    }

    override fun getPinedView(): View {
        return lastChildPined
    }

    override fun getContentHeight(): Int {
        return measuredHeight
    }

    override fun getScrollRange(): Int {
        return measuredHeight - pinedHeight
    }

    private val mChildHelper: NestedScrollingChildHelper = NestedScrollingChildHelper(this)

    init {
        mChildHelper.isNestedScrollingEnabled = true
    }

    private var velocityTracker = VelocityTracker.obtain()
    private val maxVelocityY = ViewConfiguration.get(context).scaledMaximumFlingVelocity
    private val minVelocityY = ViewConfiguration.get(context).scaledMinimumFlingVelocity
    private val mScroller = Scroller(context)

    private val consumed = intArrayOf(0, 0)

    private var lastTouchedY = 0

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        val actionMask = event.actionMasked
        when (actionMask) {
            MotionEvent.ACTION_DOWN -> {
                if (!mScroller.isFinished) {
                    mScroller.abortAnimation()
                    mScroller.forceFinished(true)
                }

                mChildHelper.stopNestedScroll(ViewCompat.TYPE_TOUCH)
                mChildHelper.stopNestedScroll(ViewCompat.TYPE_NON_TOUCH)
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
                        invalidate()
                    }

                }
                mChildHelper.stopNestedScroll(ViewCompat.TYPE_TOUCH)

            }
        }

        return super.dispatchTouchEvent(event)
    }
}