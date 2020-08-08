package com.view.zero.learn.views.draw.pinrecycler

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.view.zero.learn.LogTag
import kotlin.math.log
import kotlin.math.roundToInt

class RecDecord : RecyclerView.ItemDecoration {

    private var pinHeadHeight = 50;
    private var diviHeight = 1
    private var mPaint = Paint()
    private var fontMertics: Paint.FontMetrics
    private var txtHeight: Int

    constructor(context: Context) {
        pinHeadHeight = (context.resources.displayMetrics.density * pinHeadHeight).toInt()
        diviHeight = (context.resources.displayMetrics.density * diviHeight).toInt()
        mPaint.setColor(Color.RED)
        mPaint.isDither = true
        mPaint.isAntiAlias = true
        mPaint.textSize = 32f
        fontMertics = mPaint.fontMetrics
        txtHeight = (fontMertics.descent - fontMertics.ascent).toInt()
        Log.e(
            LogTag,
            "${fontMertics.ascent}  ${fontMertics.top}  ${fontMertics.descent}  ${fontMertics.bottom}"
        )
    }


    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)

        val paddingTop = parent.paddingTop
        val rect =
            Rect(parent.paddingLeft, paddingTop, parent.right - parent.paddingRight, parent.bottom)
        c.save()
        c.clipRect(rect)


        val count = parent.childCount
        val adapter = parent.adapter as MyAdapter
        for (i in 0 until count) {
            val view = parent.getChildAt(i)
            val position = parent.getChildLayoutPosition(view)

//            Log.e(LogTag, "view top ->>> ${view.top}")

            if (isGroupItem(parent, position)) {
                val groupName = getGroupName(parent, position)
//                val txLength = mPaint.measureText(groupName,0,groupName.length)

                val viewTop = view.top
                drawGroup(parent,groupName, viewTop, c)
            }
        }

        c.restore()

    }

    private fun drawGroup(parent: RecyclerView,groupName: String, viewTop: Int, c: Canvas) {
        mPaint.setColor(Color.parseColor("#eaeaee"))
        c.drawRect(
            parent.paddingLeft.toFloat(),
            (viewTop - pinHeadHeight).toFloat(),
            (parent.right - parent.paddingRight).toFloat(), viewTop.toFloat(), mPaint
        )
        mPaint.setColor(Color.RED)


        val baseLine = viewTop - pinHeadHeight / 2 + txtHeight / 2 - fontMertics.descent
        c.drawText(groupName, 40f, baseLine, mPaint)
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)

        val paddingTop = parent.paddingTop
        val rect =
            Rect(parent.paddingLeft, paddingTop, parent.right - parent.paddingRight, parent.bottom)
        c.save()
        c.clipRect(rect)

        val firstposition =
            (parent.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
        val itemView = parent.findViewHolderForAdapterPosition(firstposition)!!.itemView
        val groupName = getGroupName(parent, firstposition)
//        Log.e(LogTag,"onDrawover $firstposition $paddingTop  ${itemView.top}")

        if (isGroupItem(parent, firstposition + 1)) {
//            如果第二个可见的也有头部标签
//            最下面的绘制下边界为第一个可见view的底部和recyclerview上padding+标题的高度的 的 最小值
//            因为第一个view的底部变化是 (paddingTop + view.height -> paddingTop) 而标题底部最大不超过 padingTop+pinHeight
            val top = Math.min(itemView.bottom,paddingTop+pinHeadHeight)
            drawGroup(parent,groupName,top,c)

        } else {
            drawGroup(parent,groupName,paddingTop+pinHeadHeight,c)
        }



        c.restore()

    }


    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        val position = parent.getChildLayoutPosition(view)
        if (position == 0 || !getGroupName(parent, position - 1).equals(
                getGroupName(
                    parent,
                    position
                )
            )
        ) {
            outRect.set(0, pinHeadHeight, 0, 0)
        } else {
            outRect.set(0, diviHeight, 0, 0)
        }
    }

    fun getGroupName(parent: RecyclerView, position: Int): String {
        var adapter = parent.adapter as MyAdapter
        var groupItem = adapter.groupLists[position]
        return groupItem.groupName
    }

    fun isGroupItem(parent: RecyclerView, position: Int): Boolean {
        if (position == 0) return true
        return !getGroupName(parent, position - 1).equals(getGroupName(parent, position))

    }

//    fun findNextGroup(parent: RecyclerView,)
}