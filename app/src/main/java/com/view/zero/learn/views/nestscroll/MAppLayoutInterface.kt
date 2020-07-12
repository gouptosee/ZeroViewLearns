package com.view.zero.learn.views.nestscroll

import android.view.View

interface MAppLayoutInterface {

    fun getPinedHeight(): Int

    fun getPinedView(): View

    fun getContentHeight():Int

    fun getScrollRange():Int

}