package com.view.zero.learn.views.draw.photoview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class PhotoView @JvmOverloads constructor(context:Context,attrs:AttributeSet?=null,intStyle:Int=0): View(context,attrs,intStyle) {

    private var mPaint = Paint()
    init {
        mPaint.isDither = true
        mPaint.isAntiAlias = true
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
    }



}