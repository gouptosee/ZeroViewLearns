package com.view.zero.learn.uis

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import com.view.zero.learn.R
import com.view.zero.learn.uis.fras.NestVpFragment
import kotlinx.android.synthetic.main.activity_nest_test3.*

class NestTestViewPagerActivity : AppCompatActivity() {

    private val list: MutableList<Fragment> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nest_test3)

        list.add(NestVpFragment())
        list.add(NestVpFragment())

        viewpager.adapter = VpAdapter()

    }

    inner class VpAdapter : FragmentPagerAdapter(supportFragmentManager) {
        override fun getItem(position: Int): Fragment {
            return list[position]
        }

        override fun getCount(): Int {
            return list.size
        }

    }
}




