package com.tb.learn.fileexplorer.page.fragment

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tb.learn.fileexplorer.R
import com.tb.learn.fileexplorer.activity.TestActivity
import com.tb.learn.fileexplorer.adapter.ArrayObjectAdapter
import com.tb.learn.fileexplorer.adapter.Presenter
import com.tb.learn.fileexplorer.content.FileScanResult
import com.tb.learn.fileexplorer.content.FileScanner
import com.tb.customview.ScaleGestureImageView
import com.tb.tools.TBLog
import com.tb.tools.getBitmapByFactory
import com.tb.tools.whenAllNotNull
import kotlinx.coroutines.*

class ListPageFragment : BasePageFragment() {

    private lateinit var mRecyclerList: RecyclerView
    private lateinit var mImgViewer: ScaleGestureImageView
    var adapter: ArrayObjectAdapter? = null

    companion object {
        private const val TAG = "ListPageFragment"
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_list_page
    }

    override fun initView(root: View) {
        //列表初始化
        mRecyclerList = root.findViewById(R.id.fragment_list_page_recyclerview)
        adapter = ArrayObjectAdapter(ListPresenter())
        mRecyclerList.adapter = adapter
        mRecyclerList.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        //添加分割线
        mRecyclerList.addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL))

        //图片查看器
        mImgViewer = root.findViewById(R.id.fragment_list_page_img_viewer)
        mImgViewer.visibility = View.INVISIBLE
        mImgViewer.setOnClickListener {
            it.visibility = View.GONE
        }
    }

    override fun onResume() {
        super.onResume()

        MainScope().launch(Dispatchers.Main) {
            FileScanner.scanForImg { result ->
                adapter?.add(result)
            }
        }
    }

    inner class ListPresenter: Presenter() {
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val itemView: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.viewholder_list_presenter, parent, false)
            return ListViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Any?) {
            //尽量不在onBindViewHolder中做耗时操作，否则会影响滑动流畅度
            val timeStart = System.currentTimeMillis()

            //自己写的判空API
            whenAllNotNull(holder as? ListViewHolder, item as? FileScanResult) {
                vh, result ->
                vh.title.text = result.title
                vh.path.text = result.path
                vh.itemView.setOnClickListener {
                    onItemClick(result)
                }

                vh.img.post {
                    //子线程加载图片
                    MainScope().launch(Dispatchers.Main) {
                        getBitmapByFactory(result.path, vh.img.width, vh.img.height).let { bitmap ->
                            vh.img.setImageBitmap(bitmap)
                        }
                    }
                }
            }

            TBLog.e(TAG, "onBindViewHolder end, elapsed time = ${System.currentTimeMillis() - timeStart}" )
        }

        private fun onItemClick(item: FileScanResult) {
//            mImgViewer.post {
//                val width = mImgViewer.width
//                val height = mImgViewer.height
//                TBLog.d(TAG, "width = $width, height = $height")
//
//                MainScope().launch(Dispatchers.Main) {
//                    getBitmapByFactory(item.path, width, height)?.let {
//                        mImgViewer.setImageBitmap(it)
//                        mImgViewer.visibility = View.VISIBLE
//                    }
//                }
//            }
            val intent: Intent = Intent(activity, TestActivity::class.java)
            intent.putExtra("path", item.path)
            activity?.startActivity(intent)
        }
    }

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.viewholder_list_presenter_title)
        val img: ImageView = itemView.findViewById(R.id.viewholder_list_presenter_img)
        val path: TextView = itemView.findViewById(R.id.viewholder_list_presenter_path)
    }
}