package com.view.zero.learn.views.nestscroll

import android.view.View

class MBehaviorImp() : IBehavior<MNestChild> {

    override fun dependsOn(parent: View, selfView: MNestChild, denpendView: View): Boolean {
        if (denpendView is MAppLayout) {
            selfView.layout(
                0,
                denpendView.measuredHeight,
                parent.measuredWidth,
                selfView.measuredHeight + denpendView.measuredHeight
            )
            return true
        }
        return false
    }

    override fun onDependViewChanged(
        parent: View,
        selfView: MNestChild,
        dependView: View
    ): Boolean {

        return false
    }

    override fun fliping(target: MNestChild, velocityY: Float): Boolean {
        target.fling(velocityY.toInt())
        return true

    }

    override fun canScrollDown(target: MNestChild): Boolean {
        if (target.scrollY != 0) return false
        return true
    }

    override fun tryScrollTo(target: MNestChild, delx: Int, delY: Int) {
        target.tryScrollTo(delx, delY)
    }

    override fun onStartScroll() {
    }


}