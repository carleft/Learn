package com.tb.learn.fileexplorer.page.fragment

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tb.learn.fileexplorer.R
import com.tb.learn.fileexplorer.adapter.ArrayObjectAdapter
import com.tb.learn.fileexplorer.adapter.Presenter
import com.tb.learn.fileexplorer.content.FileScanner
import com.tb.tools.TBLog
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.lang.ref.WeakReference

class ListPageFragment : BasePageFragment() {

    private lateinit var mRecyclerList: RecyclerView
    var adapter: ArrayObjectAdapter? = null

    companion object {
        private const val TAG = "ListPageFragment"
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_list_page
    }

    override fun initView(root: View) {
        mRecyclerList = root.findViewById(R.id.fragment_list_page_recyclerview)
        adapter = ArrayObjectAdapter(ListPresenter())
        mRecyclerList.adapter = adapter
        mRecyclerList.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    override fun onResume() {
        super.onResume()
//        FileScanTask(this).execute()
        //协程使用，待研究
        MainScope().launch {
            async {
                FileScanner.queryOther()
            }.let {
                launch(Dispatchers.Main) {
                    adapter?.let { adapter ->
                        adapter.addAll(it.await())
                    }
                }
            }
        }

//        MainScope().launch {
//            flow<ArrayList<FileScanner.FileScanResult>> {
//                FileScanner.queryOther()
//            }.flowOn(Dispatchers.Default).catch {
//
//            }.collect(){
//                adapter?.let { adapter ->
//                    adapter.addAll(it)
//                }
//            }
//        }
    }

    class ListPresenter: Presenter() {
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val itemView: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.viewholder_list_presenter, parent, false)
            return ListViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Any?) {
            val timeStart = System.currentTimeMillis()
            (holder as? ListViewHolder)?.let { vh ->
                (item as? FileScanner.FileScanResult)?.let { result ->
                    vh.title.text = result.title
                    //尽量不在onBindViewHolder中做耗时操作，此处不应该读取图片
                    vh.img.setImageBitmap(result.bitmap)
                }
            }
            TBLog.e(TAG, "onBindViewHolder end, elapsed time = ${System.currentTimeMillis() - timeStart}" )
        }
    }

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.viewholder_list_presenter_title)
        val img: ImageView = itemView.findViewById(R.id.viewholder_list_presenter_img)
    }


    class FileScanTask(fragment: ListPageFragment): AsyncTask<Int, Int, ArrayList<FileScanner.FileScanResult>>() {

        private var ref: WeakReference<ListPageFragment>

        init {
            ref = WeakReference(fragment)
        }

        override fun onPostExecute(result: ArrayList<FileScanner.FileScanResult>?) {
            ref.get()?.adapter?.let { adapter ->
                result?.let { result ->
                    adapter.addAll(result)
                }
            }
        }

        override fun doInBackground(vararg params: Int?): ArrayList<FileScanner.FileScanResult> {
            return FileScanner.queryOther()
        }
    }
}