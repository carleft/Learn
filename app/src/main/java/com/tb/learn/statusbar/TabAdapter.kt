package com.tb.learn.statusbar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tb.learn.R
import com.tb.learn.page.PageHelper

class TabAdapter(var onTabChangeListener: OnTabChangeListener):
    RecyclerView.Adapter<TabAdapter.TabViewHolder>() {

    private val mTypeList: MutableList<@PageHelper.Type String> = ArrayList()
    private var mSelectedPosition: Int = 0

    init {
        //主页锁定放在首位，且不可删除
        addTab(PageHelper.Type.HOME)
    }

    interface OnTabChangeListener {
        fun onTabAdded(@PageHelper.Type type: String)
        fun onTabDeleted(position: Int)
        fun onTabSelected(position: Int)
    }

    fun addTab(@PageHelper.Type type: String) {
        onTabChangeListener.onTabAdded(type)
        mTypeList.add(type)
        notifyItemInserted(itemCount - 1)
        setPositionSelected(itemCount - 1)
    }

    fun delTab(position: Int) {
        if (position in 1 until itemCount) {
            //重新设定选中的item
            if (mSelectedPosition == position &&
                mSelectedPosition - 1 in 0 until itemCount) {
                //如果删除的Tab是当前选中的
                mSelectedPosition -= 1
                setPositionSelected(mSelectedPosition)
            }


//            java.lang.positionOutOfBoundsException: Inconsistency detected. Invalid item position 1(offset:-1).state:3 androidx.viewpager2.widget.ViewPager2$RecyclerViewImpl
//            //移除指定item
//            mTypeList.removeAt(position)
//
//            notifyItemRemoved(position)
//            if (position != itemCount - 1) {
//                notifyItemRangeChanged(position, itemCount - position - 1)
//            }

            mTypeList.removeAt(position)
            notifyDataSetChanged()

            onTabChangeListener.onTabDeleted(position)
        }
    }

    fun setPositionSelected(position: Int) {
        if (position in 0 until itemCount) {
            val lastSelectedPosition: Int = mSelectedPosition
            mSelectedPosition = position
            notifyItemChanged(mSelectedPosition, 0) //payload局部刷新
            if (lastSelectedPosition != mSelectedPosition) {
                notifyItemChanged(lastSelectedPosition, 0)
            }
            onTabChangeListener.onTabSelected(position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TabViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.viewholder_tab, parent, false)
        return TabViewHolder(view)
    }

    override fun onBindViewHolder(holder: TabViewHolder, position: Int) {

    }

    override fun onBindViewHolder(holder: TabViewHolder, position: Int, payloads: MutableList<Any>) {
        //payloads局部刷新
        holder.root.isSelected = mSelectedPosition == position
        holder.closeBtn.visibility = if(mSelectedPosition == position && position != 0) View.VISIBLE else View.GONE

        //全局刷新
        if (payloads.isEmpty()) {
            holder.title.text = mTypeList[holder.adapterPosition]
            holder.closeBtn.setOnClickListener {
                delTab(holder.adapterPosition)
            }
            holder.root.setOnClickListener {
                setPositionSelected(holder.adapterPosition)
            }
        }
    }

    override fun getItemCount(): Int {
        return mTypeList.size
    }

    class TabViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var root: View = itemView
        var title: TextView = itemView.findViewById(R.id.viewholder_tab_title)
        var closeBtn: ImageView = itemView.findViewById(R.id.viewholder_tab_close)
    }
}