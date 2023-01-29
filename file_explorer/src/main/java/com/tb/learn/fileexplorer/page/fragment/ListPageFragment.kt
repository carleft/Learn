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
import java.lang.ref.WeakReference

class ListPageFragment: BasePageFragment() {

    private lateinit var mRecyclerList: RecyclerView
    var adapter: ArrayObjectAdapter? = null

    override fun getLayoutId(): Int {
        return R.layout.fragment_list_page
    }

    override fun initView(root: View) {
        mRecyclerList = root.findViewById(R.id.fragment_list_page_recyclerview)
        adapter = ArrayObjectAdapter(ListPresenter())
        mRecyclerList.adapter = adapter
        mRecyclerList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    override fun onResume() {
        super.onResume()
        FileScanTask(this).execute()
    }

    class ListPresenter: Presenter() {
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val itemView: View = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_list_presenter, parent, false)
            return object: RecyclerView.ViewHolder(itemView) {}
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Any?) {
            holder.itemView.let { itemView ->
                val title: TextView = itemView.findViewById(R.id.viewholder_list_presenter_title)
                val img: ImageView = itemView.findViewById(R.id.viewholder_list_presenter_img)
                (item as? FileScanner.FileScanResult)?.let { result ->
                    title.text = result.title
                    val option = BitmapFactory.Options()
                    option.inPreferredConfig = Bitmap.Config.ALPHA_8
                    option.inJustDecodeBounds = false
                    option.inSampleSize = 100
                    BitmapFactory.decodeFile(result.path, option)?.let { bitmap ->
                        img.setImageBitmap(bitmap)
                    }
                }
            }
        }
    }


    class FileScanTask(fragment: ListPageFragment): AsyncTask<Int, Int, ArrayList<FileScanner.FileScanResult>>() {

        private var ref: WeakReference<ListPageFragment>

        init {
            ref = WeakReference(fragment)
        }

        override fun onPostExecute(result: ArrayList<FileScanner.FileScanResult>?) {
            ref.get()?.adapter?.let {adapter ->
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