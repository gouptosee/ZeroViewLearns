package com.view.zero.learn.utils

import android.util.Log

class LogUtils {
    companion object {

        const val LogTag = "1234"

        fun logE(msg: String) {
            Log.e(LogTag, "$msg")
        }

        fun logE(tag: String, msg: String) {
            Log.e(tag, "$msg")
        }

    }


}