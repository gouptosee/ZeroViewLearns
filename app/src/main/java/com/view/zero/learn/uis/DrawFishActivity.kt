package com.view.zero.learn.uis

import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.ViewTreeObserver
import androidx.appcompat.app.AppCompatActivity
import com.view.zero.learn.LogTag
import com.view.zero.learn.R
import com.view.zero.learn.views.draw.fish.DrawUtils
import com.view.zero.learn.views.draw.fish.FishDrawable
import kotlinx.android.synthetic.main.activity_draw_fish.*

class DrawFishActivity : AppCompatActivity() {

    private var centerX = 0f
    private var centerY = 0f
    private var size = 0
    private var checked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_draw_fish)

        image.setImageDrawable(FishDrawable())
        image.viewTreeObserver.addOnPreDrawListener {
            if (checked) return@addOnPreDrawListener true
            checked = true
            size = image.measuredWidth
            centerY = (image.top + size / 2).toFloat()
            centerX = (image.left + size / 2).toFloat()
            return@addOnPreDrawListener true
        }

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event!!.action) {
            MotionEvent.ACTION_UP -> {
                if (image.animation != null) {
                    image.clearAnimation()
                }
                val curX = event.x
                var curY = event.y

                DrawUtils.solveSwiming(image, centerX, centerY, curX, curY, 1000)
                Log.e(LogTag, "action up ->> $centerX : $centerY || $curX  $curY")

                centerX = curX
                centerY = curY
            }
        }



        return super.onTouchEvent(event)
    }

}