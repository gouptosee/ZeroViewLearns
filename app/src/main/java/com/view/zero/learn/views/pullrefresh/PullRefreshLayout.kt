package com.view.zero.learn.views.pullrefresh

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.core.view.NestedScrollingParent

open class PullRefreshLayout constructor(
    context: Context,
    attr: AttributeSet? = null,
    intStyle: Int
) : ViewGroup(context, attr, intStyle), NestedScrollingParent {


    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        return super.dispatchTouchEvent(ev)
    }


}