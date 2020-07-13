package com.view.zero.learn.uis.fras

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.view.zero.learn.R
import kotlinx.android.synthetic.main.activity_nest_test2.*

class NestVpFragment : Fragment() {

    private lateinit var rootView: View
    private var recyclerview: RecyclerView? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (!this::rootView.isInitialized){
            rootView = layoutInflater.inflate(R.layout.layout_recyclerview_common,null)
            recyclerview = rootView.findViewById(R.id.recyclerview)
        }
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
    }


    fun initRecyclerView() {
        recyclerview!!.isNestedScrollingEnabled  =true
        recyclerview!!.layoutManager = LinearLayoutManager(activity)
        recyclerview!!.adapter = MRecAdapter()
    }


    inner class MRecAdapter : RecyclerView.Adapter<MRecHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MRecHolder {
            val view = Button(activity)
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


}