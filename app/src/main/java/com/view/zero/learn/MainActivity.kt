package com.view.zero.learn

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var actList: MutableList<Acts> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        createUi()
    }


    private fun createUi() {

        for (i in Acts.values()) {
            actList.add(i)
        }

        recylerview_main.layoutManager = LinearLayoutManager(this)
        recylerview_main.adapter = MainAdapter()


    }


    inner class MainAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val button = Button(parent.context)
            button.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            val vh = MViewHolder(button)
            return vh
        }

        override fun getItemCount(): Int {
            return actList.size
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val vh = holder as MViewHolder
            vh.button.text = actList[position].actName
            vh.button.setOnClickListener() {
                startActivity(Intent(this@MainActivity, actList[position].actClazz))
            }
        }

    }

    inner class MViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var button: Button = view as Button
    }


}
