package com.tb.learn.viewpager

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.tb.learn.viewpager.fragment.BasePageFragment

class MainViewPagerAdapter(fragmentActivity: FragmentActivity):
    FragmentStateAdapter(fragmentActivity) {

    private val mFragmentList: MutableList<BasePageFragment> = ArrayList()

    fun addFragment(fragment: BasePageFragment) {
        mFragmentList.add(fragment)
        notifyItemInserted(itemCount - 1)
    }

    fun delFragment(index: Int) {
        if (index in 0 until itemCount) {
            mFragmentList.removeAt(index)
            notifyDataSetChanged()
        }

    }

    override fun getItemCount(): Int {
        return mFragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return mFragmentList[position]
    }


    //需覆写以下两个方法，才可以动态添加Fragment和删除Fragment

    override fun getItemId(position: Int): Long {
        return mFragmentList[position].hashCode().toLong()
    }

    override fun containsItem(itemId: Long): Boolean {
        val itemIds = mFragmentList.map{ it.hashCode().toLong()}
        return itemIds.contains(itemId)
    }
}