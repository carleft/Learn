package com.tb.learn.fileexplorer

import android.app.Application


class MainApplication: Application() {

    companion object {
        var instance: Application? = null
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}