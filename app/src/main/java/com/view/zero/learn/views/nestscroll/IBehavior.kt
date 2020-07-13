package com.view.zero.learn.views.nestscroll

import android.view.View

interface IBehavior<T : View> {

    fun dependsOn(parent: View, selfView: T, denpendView: View): Boolean

    fun onDependViewChanged(parent: View, selfView: T, dependView: View): Boolean

    fun fliping(target: T, velocityY: Float): Boolean

    fun canScrollDown(target: T): Boolean

    fun tryScrollTo(target: T, delx: Int, delY: Int)

    fun onStartScroll()

}