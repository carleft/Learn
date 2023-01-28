package com.tb.tools

import android.util.Log

/**
 * 简单的日志工具类
 */
object TBLog {

    fun v(tag: String, msg: String, tr: Throwable? = null) {
        Log.v("TB0232", "@$tag: $msg", tr)
    }

    fun d(tag: String, msg: String, tr: Throwable? = null) {
        Log.d("TB0232", "@$tag: $msg", tr)
    }

    fun i(tag: String, msg: String, tr: Throwable? = null) {
        Log.i("TB0232", "@$tag: $msg", tr)
    }

    fun w(tag: String, msg: String, tr: Throwable? = null) {
        Log.w("TB0232", "@$tag: $msg", tr)
    }

    fun e(tag: String, msg: String, tr: Throwable? = null) {
        Log.e("TB0232", "@$tag: $msg", tr)
    }

}