package com.view.zero.learn.views.nestscroll

import android.view.View
import android.widget.TextView

class MBehaviorImp3() : IBehavior<TextView> {
    override fun dependsOn(parent: View, selfView: TextView, denpendView: View): Boolean {
        if (denpendView is MAppLayout) {
            selfView.layout(
                0,
                -selfView.measuredHeight,
                parent.measuredWidth,
                0
            )
            return true
        }
        return false
    }

    override fun onDependViewChanged(
        parent: View,
        selfView: TextView,
        dependView: View
    ): Boolean {
        val scrollRange = (dependView as MAppLayoutInterface).getScrollRange()
        val scrollY = parent.scrollY
        val rate = scrollY.toFloat() / scrollRange
        val translateY = selfView.measuredHeight * rate +parent.scrollY
        selfView.translationY = translateY
        return false
    }

    override fun fliping(target: TextView, velocityY: Float): Boolean {
        return false
    }

    override fun canScrollDown(target: TextView): Boolean {
        return true
    }

    override fun tryScrollTo(target: TextView, delx: Int, delY: Int) {
    }

}