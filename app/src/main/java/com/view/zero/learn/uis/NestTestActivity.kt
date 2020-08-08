package com.view.zero.learn.uis

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.view.zero.learn.LogTag
import com.view.zero.learn.R
import com.view.zero.learn.utils.toastLong
import kotlinx.android.synthetic.main.activity_nest_test.*

class NestTestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nest_test)
        addTextViews()

//        mNestChild.viewTreeObserver.addOnPreDrawListener {
//            Log.e(LogTag,"height ->>> ${mNestChild.measuredHeight}  ${mNestChild.measuredWidth}")
//            return@addOnPreDrawListener true }
//        mNestChild.post(){
//            Log.e(LogTag,"post height ->>> ${mNestChild.measuredHeight}  ${mNestChild.measuredWidth}")
//        }
        Log.e(LogTag,"Nest ->>> ${LayoutInflater.from(this)}")

    }

    private fun addTextViews() {
        for (i in 0 until 10) {
            val tv = TextView(this)
            val height = resources.displayMetrics.density * 300
            val lp = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height.toInt())
            tv.layoutParams = lp

            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30f)
            tv.text = "第${i}个元素"
            tv.gravity = Gravity.CENTER

            mNestChild.addView(tv)

            val lineView = View(this)
            val linep = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 2)
            lineView.layoutParams = linep
            lineView.setBackgroundColor(Color.parseColor("#00aaaa"))
            mNestChild.addView(lineView)
        }
        mNestParent.requestLayout()

    }


    fun onClickViews(view: View) {
        when (view.id) {
            R.id.header_1 ->{
                toastLong("Header 1 click")
            }
            R.id.header_2 ->{
                toastLong("Header 2 click")

            }
            R.id.pin_1 ->{
                toastLong("pin click")

            }
            R.id.float_1 ->{
                toastLong("float click")

            }
        }
    }

}