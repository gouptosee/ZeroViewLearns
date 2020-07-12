package com.view.zero.learn.views.nestscroll

import android.view.View
import android.widget.TextView

class MBehaviorImp2() : IBehavior<TextView> {


    override fun dependsOn(parent: View, selfView: TextView, denpendView: View): Boolean {
        if (denpendView is MAppTopChild) {
            selfView.layout(
                0,
                denpendView.measuredHeight,
                selfView.measuredWidth,
                selfView.measuredHeight + denpendView.measuredHeight
            )
            return true
        }
        return false
    }

    override fun onDependViewChanged(parent: View, selfView: TextView, dependView: View): Boolean {
        val scrollRange = (dependView as MAppLayoutInterface).getScrollRange()
        val scaleFinSize = 2
        val scrollY = parent.scrollY
        val space = parent.measuredWidth - selfView.measuredWidth * 2


        val rate = scrollY.toFloat() / scrollRange
        val offset = (space * rate)
        val scaleSize = 1 + rate
        selfView.translationX = offset


        selfView.pivotX = 0f
        selfView.pivotY = 0f

        selfView.scaleX = scaleSize
        selfView.scaleY = scaleSize

        return false
    }

    override fun fliping(target:TextView,velocityY: Float): Boolean {
        return false
    }

    override fun canScrollDown(target: TextView): Boolean {
        return true
    }

    override fun tryScrollTo(target: TextView, delx: Int, delY: Int) {

    }

}