package com.tb.learn

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.tb.learn.statusbar.TabAdapter
import com.tb.learn.viewpager.MainViewPagerAdapter
import com.tb.learn.viewpager.fragment.PageHelper

class MainActivity: FragmentActivity() {

    //StatusBar
    private lateinit var mAddBtn: ImageView
    private lateinit var mDelBtn: ImageView
    private lateinit var mRecyclerTab: RecyclerView
    private lateinit var mTabAdapter: TabAdapter

    //ViewPager
    private lateinit var mMainViewPager: ViewPager2
    private lateinit var mMainViewPagerAdapter: MainViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViewPager()
        initStatusBar()
    }

    /**
     * 初始化ViewPager，必须在initStatusBar之前调用
     */
    private fun initViewPager() {
        mMainViewPager = findViewById(R.id.viewpager_main)
        mMainViewPagerAdapter = MainViewPagerAdapter(this)
        mMainViewPager.adapter = mMainViewPagerAdapter

        //ViewPager滚动监听，标签页需要随着ViewPager滚动而改变选中状态
        mMainViewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                mTabAdapter.setPositionSelected(position)
            }
        })
    }

    /**
     * 初始化状态栏
     */
    private fun initStatusBar() {
        //添加页面按钮，在末尾添加一个页面
        mAddBtn = findViewById(R.id.status_bar_add)
        mAddBtn.setOnClickListener {
            mTabAdapter.addTab(PageHelper.Type.TEST)
            mRecyclerTab.smoothScrollToPosition(mTabAdapter.itemCount - 1)
        }

        //删除页面按钮，删除末尾的页面
        mDelBtn = findViewById(R.id.status_bar_delete)
        mDelBtn.setOnClickListener {
            mTabAdapter.delTab(mTabAdapter.itemCount - 1)
        }

        //标签页展示区
        mRecyclerTab = findViewById(R.id.status_bar_recyclerview)
        //Adapter实现，ViewPager需响应标签的添加、删除、选中事件
        mTabAdapter = TabAdapter(object: TabAdapter.OnTabChangeListener {
            override fun onTabAdded(type: String) {
                mMainViewPagerAdapter.addFragment(PageHelper.getPageFragment(type))
            }

            override fun onTabDeleted(position: Int) {
                mMainViewPagerAdapter.delFragment(position)
            }

            override fun onTabSelected(position: Int) {
                mRecyclerTab.smoothScrollToPosition(position)
                mMainViewPager.currentItem = position
            }
        })
        PageHelper.setAdapter(mTabAdapter)
        mRecyclerTab.adapter = mTabAdapter
        mRecyclerTab.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    }
}
