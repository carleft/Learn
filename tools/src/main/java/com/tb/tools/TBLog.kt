package com.tb.tools

import android.util.Log

/**
 * 简单的日志工具类
 */
object TBLog {
    //@JvmOverloads表示java文件可以调用kotlin的默认参数方法，相当于重载，不加则必须填所有参数
    //@JvmStatic表示转为真正的静态方法，否则在java文件中只能通过TBLog.INSTANCE.v()才能调用

    @JvmOverloads
    @JvmStatic
    fun v(tag: String, msg: String, tr: Throwable? = null) {
        Log.v("TB0232", "@$tag: $msg", tr)
    }

    @JvmOverloads
    @JvmStatic
    fun d(tag: String, msg: String, tr: Throwable? = null) {
        Log.d("TB0232", "@$tag: $msg", tr)
    }

    @JvmOverloads
    @JvmStatic
    fun i(tag: String, msg: String, tr: Throwable? = null) {
        Log.i("TB0232", "@$tag: $msg", tr)
    }

    @JvmOverloads
    @JvmStatic
    fun w(tag: String, msg: String, tr: Throwable? = null) {
        Log.w("TB0232", "@$tag: $msg", tr)
    }

    @JvmOverloads
    @JvmStatic
    fun e(tag: String, msg: String, tr: Throwable? = null) {
        Log.e("TB0232", "@$tag: $msg", tr)
    }

}