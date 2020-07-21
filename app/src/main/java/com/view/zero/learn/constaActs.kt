package com.view.zero.learn

import com.view.zero.learn.uis.DrawFishActivity

const val LogTag = "1234"

enum class Acts constructor(val actName: String, val actClazz: Class<*>) {
    NestTestActivity("Nest测试Activity", com.view.zero.learn.uis.NestTestActivity::class.java),
    NestRecyclerViewTestActivity("NestRecycleriew测试Activity", com.view.zero.learn.uis.NestTestRecyclerviewActivity::class.java),
    NestViewPagerTestActivity("NestViewPager测试Activity", com.view.zero.learn.uis.NestTestViewPagerActivity::class.java),
    DrawFishActivity("drawable画鱼练习", com.view.zero.learn.uis.DrawFishActivity::class.java),
    DrawFishActivity2("drawable画鱼练习2", com.view.zero.learn.uis.DrawFishActivity2::class.java),

    PullRefreshActivity("pull测试Activity", com.view.zero.learn.uis.PullRefreshActivity::class.java),
}