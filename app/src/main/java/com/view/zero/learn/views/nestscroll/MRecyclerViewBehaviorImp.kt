package com.view.zero.learn.views.nestscroll

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.view.zero.learn.LogTag

class MRecyclerViewBehaviorImp() : IBehavior<RecyclerView> {
    override fun dependsOn(parent: View, selfView: RecyclerView, denpendView: View): Boolean {
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
        selfView: RecyclerView,
        dependView: View
    ): Boolean {
        return false
    }

    private var fliping = false

    override fun fliping(target: RecyclerView, velocityY: Float): Boolean {
        if (fliping) return true
        fliping = true
        target.fling(0, velocityY.toInt())
        return true
    }

    override fun canScrollDown(target: RecyclerView): Boolean {
        val can = target.canScrollVertically(-1)
        return !can
    }

    override fun tryScrollTo(target: RecyclerView, delx: Int, delY: Int) {
        target.scrollBy(delx, delY)
    }

    override fun onStartScroll() {
        fliping = false
    }


}