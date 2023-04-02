package com.tb.calculator.keyboard

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.tb.calculator.R

class CalculatorKeyBoardAdapter(private val mContext: Context):
    RecyclerView.Adapter<CalculatorKeyBoardAdapter.CalculatorButtonViewHolder>() {

    //数据列表
    private val mButtonList = listOf(
        CalculatorButtonBean("C"),
        CalculatorButtonBean("Del"),
        CalculatorButtonBean("%"),
        CalculatorButtonBean("/"),
        CalculatorButtonBean("7"),
        CalculatorButtonBean("8"),
        CalculatorButtonBean("9"),
        CalculatorButtonBean("*"),
        CalculatorButtonBean("4"),
        CalculatorButtonBean("5"),
        CalculatorButtonBean("6"),
        CalculatorButtonBean("-"),
        CalculatorButtonBean("1"),
        CalculatorButtonBean("2"),
        CalculatorButtonBean("3"),
        CalculatorButtonBean("+", R.drawable.plus),
        CalculatorButtonBean("null"),
        CalculatorButtonBean("0"),
        CalculatorButtonBean("."),
        CalculatorButtonBean("="),
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalculatorButtonViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.keyboard_area_button, parent, false)
        return CalculatorButtonViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mButtonList.size
    }

    override fun onBindViewHolder(holder: CalculatorButtonViewHolder, position: Int) {
        val bean = mButtonList[position]
        holder.bind(bean)
    }


    class CalculatorButtonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val button: Button = itemView.findViewById(R.id.keyboard_area_button)
        val img: ImageView = itemView.findViewById(R.id.keyboard_area_img)
        fun bind(bean: CalculatorButtonBean) {
            if (bean.imgRes != 0) {
                img.background = ResourcesCompat.getDrawable(itemView.resources, bean.imgRes, null)
                button.text = ""
            } else {
                img.background = null
                button.text = bean.text
            }
        }
    }
}