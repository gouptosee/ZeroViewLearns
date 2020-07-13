package com.view.zero.learn.views.nestscroll

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.view.zero.learn.LogTag
import com.view.zero.learn.R
import kotlinx.android.synthetic.main.activity_nest_test2.view.*

class MViewPagerBehavior() : IBehavior<ViewPager> {
    override fun dependsOn(parent: View, selfView: ViewPager, denpendView: View): Boolean {
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
        selfView: ViewPager,
        dependView: View
    ): Boolean {
        return false
    }

    private var fliping = false

    override fun fliping(target: ViewPager, velocityY: Float): Boolean {
        if (fliping) return true
        fliping = true

        findCurRecyclerView(target).fling(0, velocityY.toInt())
        return true
    }

    fun findCurRecyclerView(target: ViewPager): RecyclerView {
        val recyclerView =
            target.getChildAt(target.currentItem).findViewById<RecyclerView>(R.id.recyclerview)
        return recyclerView
    }

    override fun canScrollDown(target: ViewPager): Boolean {
        val can = findCurRecyclerView(target).canScrollVertically(-1)
        return !can
    }

    override fun tryScrollTo(target: ViewPager, delx: Int, delY: Int) {

        findCurRecyclerView(target).scrollBy(delx, delY)
    }

    override fun onStartScroll() {
        fliping = false
    }


}