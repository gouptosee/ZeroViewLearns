package com.view.zero.learn.uis

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.view.zero.learn.R
import com.view.zero.learn.views.draw.fish.FishDrawable
import com.view.zero.learn.views.draw.fish.FishDrawable2
import com.view.zero.learn.views.draw.fish.FishLayout
import com.view.zero.learn.views.draw.fish.FishLayout2
import kotlinx.android.synthetic.main.activity_draw_fish.*
import kotlinx.android.synthetic.main.activity_draw_fish2.*

class DrawFishActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_draw_fish2)
        findViewById<FishLayout2>(R.id.fishlayout).initFish(FishDrawable2())

    }


}