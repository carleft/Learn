package com.tb.learn.page

import androidx.annotation.StringDef
import com.tb.learn.statusbar.TabAdapter
import com.tb.learn.page.fragment.BasePageFragment
import com.tb.learn.page.fragment.HomePageFragment
import com.tb.learn.page.fragment.TestPageFragment

object PageHelper {

    @Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.CLASS, AnnotationTarget.TYPE)
    @StringDef (Type.HOME, Type.TEST)
    annotation class Type {
        companion object {
            const val HOME: String = "Home"
            const val TEST: String = "Test"
        }
    }

    private var adapter: TabAdapter? = null
    fun setAdapter(adapter: TabAdapter) {
        PageHelper.adapter = adapter
    }

    fun getPageFragment(@Type type: String): BasePageFragment =
        when(type) {
            Type.HOME -> HomePageFragment()
            Type.TEST -> TestPageFragment()
            else -> TestPageFragment()
        }

    fun addPage(@Type type: String) {
        adapter?.addTab(type)
    }
}