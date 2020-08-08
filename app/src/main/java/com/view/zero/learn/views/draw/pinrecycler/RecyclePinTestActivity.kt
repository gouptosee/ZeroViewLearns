package com.view.zero.learn.views.draw.pinrecycler

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper
import com.view.zero.learn.LogTag
import com.view.zero.learn.R
import kotlinx.android.synthetic.main.layout_recyclerview_common.*
import java.lang.Exception

class RecyclePinTestActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_recyclerview_common)
        init()
    }

    fun init() {
        val list = ArrayList<GroupNameTest>()
        addListNewItems(list,"张三","第一个",10)
        addListNewItems(list,"李四","第二个",5)
        addListNewItems(list,"王五","第三个",15)
        addListNewItems(list,"赵六","第四个",8)



        val layoutManger = LinearLayoutManager(this)
        recyclerview.addItemDecoration(RecDecord(this))
        recyclerview.layoutManager = layoutManger
        recyclerview.adapter = MyAdapter(this,list)

        Handler().postDelayed({
            Log.e(LogTag,"layoutManger ->>>${recyclerview.childCount}   ${layoutManger.height}   ${layoutManger.paddingBottom}")
            invokeLogs(layoutManger)
        },2000)


    }

    fun addListNewItems(list: ArrayList<GroupNameTest>, name: String, groupName: String,count:Int) {
        for (i in 0 until count){
            var group = GroupNameTest("$name $i",groupName)
            list.add(group)
        }
    }



    fun invokeLogs(layoutManager:LinearLayoutManager){
        try {
            val field = LinearLayoutManager::class.java.getDeclaredField("mOrientationHelper")
            field.isAccessible = true
            val orientationHelper = field.get(layoutManager) as OrientationHelper

            for (i in 0 until recyclerview.childCount){
                Log.e(LogTag,"recycler item ->>> ${orientationHelper.getDecoratedEnd(recyclerview.getChildAt(i))}   ${orientationHelper.endAfterPadding}")
            }

            Log.e(LogTag,"recycler ori helper ->>> ${orientationHelper.end}  ${orientationHelper.mode}")

            val method = LinearLayoutManager::class.java.getDeclaredMethod("getChildClosestToEnd")
            method.isAccessible = true
            val view = method.invoke(layoutManager) as View

            val method_getPosition = LinearLayoutManager::class.java.superclass!!.getDeclaredMethod("getPosition",View::class.java)
            method.isAccessible = true
            val position = method_getPosition.invoke(layoutManager,view) as Int
            Log.e(LogTag,"closest position ->>> $position")




        }catch (e:Exception){
            e.printStackTrace()
        }
    }
}