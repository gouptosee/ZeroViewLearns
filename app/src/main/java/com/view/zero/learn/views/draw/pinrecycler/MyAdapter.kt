package com.view.zero.learn.views.draw.pinrecycler

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.view.zero.learn.R
import kotlinx.android.synthetic.main.layout_item_group.view.*

class MyAdapter(var context: Context, var groupLists: List<GroupNameTest>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view =LayoutInflater.from(context).inflate(R.layout.layout_item_group,null)
        return MViewHolder(view)
    }

    override fun getItemCount(): Int {
        return groupLists.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var mHolder = holder as MViewHolder
        mHolder.tvName.text = groupLists[position].name
    }


    inner class MViewHolder(view:View) :RecyclerView.ViewHolder(view){
         var tvName = view.findViewById<TextView>(R.id.tv_name)
    }

}
