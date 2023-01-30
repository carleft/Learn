package com.tb.learn.fileexplorer.page.fragment

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tb.learn.fileexplorer.R
import com.tb.learn.fileexplorer.adapter.ArrayObjectAdapter
import com.tb.learn.fileexplorer.adapter.Presenter
import com.tb.learn.fileexplorer.content.FileScanResult
import com.tb.learn.fileexplorer.content.FileScanner
import com.tb.tools.TBLog
import com.tb.tools.whenAllNotNull
import kotlinx.coroutines.*

class ListPageFragment : BasePageFragment() {

    private lateinit var mRecyclerList: RecyclerView
    private lateinit var mImgViewer: ImageView
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

//            (holder as? ListViewHolder)?.let { vh ->
//                (item as? FileScanResult)?.let { result ->
//                    vh.title.text = result.title
//                    vh.img.setImageBitmap(result.bitmap)
//                    vh.path.text = result.path
//                }
//            }

            //自己写的判空API
            whenAllNotNull(holder as? ListViewHolder, item as? FileScanResult) {
                vh, result ->
                vh.title.text = result.title
                vh.path.text = result.path
                vh.itemView.setOnClickListener {
                    onItemClick(result)
                }
                //子线程加载图片
                MainScope().launch(Dispatchers.Default) {
                    //读取图片
                    val option = BitmapFactory.Options()
                    //缩小图片读取大小，避免Canvas: trying to draw too large bitmap
                    option.inPreferredConfig = Bitmap.Config.ALPHA_8
                    option.inJustDecodeBounds = false
                    option.inSampleSize = 100

                    BitmapFactory.decodeFile(result.path, option)?.let { bitmap ->
                        //加载完成后主线程更新UI
                        withContext(Dispatchers.Main) {
                            vh.img.setImageBitmap(bitmap)
                        }
                    }
                }
            }

            TBLog.e(TAG, "onBindViewHolder end, elapsed time = ${System.currentTimeMillis() - timeStart}" )
        }

        private fun onItemClick(item: FileScanResult) {
            MainScope().launch(Dispatchers.Default) {
                //读取图片
                val option = BitmapFactory.Options()
                //缩小图片读取大小，避免Canvas: trying to draw too large bitmap
                option.inPreferredConfig = Bitmap.Config.ARGB_8888
                option.inJustDecodeBounds = false
                option.inSampleSize = 2
                BitmapFactory.decodeFile(item.path, option)?.let {
                    withContext(Dispatchers.Main) {
                        mImgViewer.setImageBitmap(it)
                        mImgViewer.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.viewholder_list_presenter_title)
        val img: ImageView = itemView.findViewById(R.id.viewholder_list_presenter_img)
        val path: TextView = itemView.findViewById(R.id.viewholder_list_presenter_path)
    }
}