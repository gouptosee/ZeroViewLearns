package com.view.zero.learn.uis

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.view.zero.learn.R
import kotlinx.android.synthetic.main.activity_photo_test.*

class PhotoViewTestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_test)
        photo_view.setImageRes(R.mipmap.p_t)
    }
}