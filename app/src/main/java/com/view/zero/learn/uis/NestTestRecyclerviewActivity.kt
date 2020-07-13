package com.view.zero.learn.uis

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.view.zero.learn.R
import com.view.zero.learn.utils.toastLong
import kotlinx.android.synthetic.main.activity_nest_test2.*

class NestTestRecyclerviewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nest_test2)
        initRecyclerView()
    }


    fun initRecyclerView() {
        recyclerview.layoutManager = LinearLayoutManager(this)
        recyclerview.adapter = MRecAdapter()
    }


    inner class MRecAdapter : RecyclerView.Adapter<MRecHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MRecHolder {
            val view = Button(this@NestTestRecyclerviewActivity)
            view.layoutParams = ViewGroup.LayoutParams(-1, -2)
            return MRecHolder(view)

        }

        override fun getItemCount(): Int {
            return 50
        }

        override fun onBindViewHolder(holder: MRecHolder, position: Int) {
            (holder.view as Button).text = "第${position}个"
        }


    }

    inner class MRecHolder(var view: View) : RecyclerView.ViewHolder(view)


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
