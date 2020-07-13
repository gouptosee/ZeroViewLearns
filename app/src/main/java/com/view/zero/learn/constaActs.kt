package com.view.zero.learn

const val LogTag = "1234"

enum class Acts constructor(val actName: String, val actClazz: Class<*>) {
    NestTestActivity("Nest测试Activity", com.view.zero.learn.uis.NestTestActivity::class.java),
    NestRecyclerViewTestActivity("NestRecycleriew测试Activity", com.view.zero.learn.uis.NestTestRecyclerviewActivity::class.java),
    NestViewPagerTestActivity("NestViewPager测试Activity", com.view.zero.learn.uis.NestTestViewPagerActivity::class.java),

    PullRefreshActivity("pull测试Activity", com.view.zero.learn.uis.PullRefreshActivity::class.java),
}