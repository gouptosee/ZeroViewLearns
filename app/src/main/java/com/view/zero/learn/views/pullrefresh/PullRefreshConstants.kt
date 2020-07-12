package com.view.zero.learn.views.pullrefresh

class PullRefreshConstants {
    companion object {
        const val STATE_NONE = 0
        const val STATE_PULL_TO_REFRESH = 1
        const val STATE_RELEASE_TO_REFRESH = 2
        const val STATE_REFRESHING = 3
        const val STATE_REFRESH_FINISH = 4

        fun getStateDescByType(type: Int): String {
            when (type) {
                STATE_PULL_TO_REFRESH -> "下拉刷新"
                STATE_RELEASE_TO_REFRESH -> "释放刷新"
                STATE_REFRESHING -> "正在刷新"
                STATE_REFRESH_FINISH -> "刷新完成"
            }
            return "下拉刷新"
        }
    }
}