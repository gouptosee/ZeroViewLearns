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
import com.view.zero.learn.views.draw.fish.FishDrawable2
import kotlinx.android.synthetic.main.activity_draw_fish.*

class DrawFishActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_draw_fish)
        fishlayout.initFish(FishDrawable())
    }

}