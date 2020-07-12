package com.view.zero.learn.views.pullrefresh

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import com.view.zero.learn.R

class DefaultRefreshHeader @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    intStyle: Int
) : RelativeLayout(context, attributeSet, intStyle), RefreshHeader {
    override fun getView(): View {
        return this
    }

    init {
        LayoutInflater.from(context)
            .inflate(R.layout.layout_pull_refresh_default_header, this, true)
    }





    override fun getMaxPullRange(): Int {
        return measuredHeight * 2
    }

    override fun getMeasureHeight(): Int {
        return measuredHeight
    }

}