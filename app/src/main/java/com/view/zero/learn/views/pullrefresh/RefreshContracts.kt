package com.view.zero.learn.views.pullrefresh

import android.view.View

interface RefreshHeader {

    fun getView(): View

    fun getMaxPullRange(): Int

    fun getMeasureHeight(): Int

}

interface RefreshContent {

    fun canRefresh(): Boolean

    fun getView(): View

}







