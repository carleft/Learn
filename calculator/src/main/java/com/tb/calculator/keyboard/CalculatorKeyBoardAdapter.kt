package com.tb.calculator.keyboard

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.tb.calculator.R

class CalculatorKeyBoardAdapter(private val mContext: Context):
    RecyclerView.Adapter<CalculatorKeyBoardAdapter.CalculatorButtonViewHolder>() {

    var onItemClickListener: OnItemClickListener? = null

    //数据列表
    private val mButtonList = listOf(
        CalculatorButtonBean(CalculatorButtonType.CLEAR),
        CalculatorButtonBean(CalculatorButtonType.DELETE),
        CalculatorButtonBean(CalculatorButtonType.PERCENT),
        CalculatorButtonBean(CalculatorButtonType.DIVIDE),
        CalculatorButtonBean(CalculatorButtonType.SEVEN),
        CalculatorButtonBean(CalculatorButtonType.EIGHT),
        CalculatorButtonBean(CalculatorButtonType.NINE),
        CalculatorButtonBean(CalculatorButtonType.MULTIPLY),
        CalculatorButtonBean(CalculatorButtonType.FOUR),
        CalculatorButtonBean(CalculatorButtonType.FIVE),
        CalculatorButtonBean(CalculatorButtonType.SIX),
        CalculatorButtonBean(CalculatorButtonType.SUBTRACT),
        CalculatorButtonBean(CalculatorButtonType.ONE),
        CalculatorButtonBean(CalculatorButtonType.TWO),
        CalculatorButtonBean(CalculatorButtonType.THREE),
        CalculatorButtonBean(CalculatorButtonType.PLUS, R.drawable.plus),
        CalculatorButtonBean("null"),
        CalculatorButtonBean(CalculatorButtonType.ZERO),
        CalculatorButtonBean(CalculatorButtonType.POINT),
        CalculatorButtonBean(CalculatorButtonType.EQUAL),
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
        holder.bind(bean, onItemClickListener)
    }


    class CalculatorButtonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val mTextView: TextView = itemView.findViewById(R.id.keyboard_area_text)
        private val mImgView: ImageView = itemView.findViewById(R.id.keyboard_area_img)
        private var mType: @CalculatorButtonType String = CalculatorButtonType.UNKNOWN

        fun bind(bean: CalculatorButtonBean, mOnItemClickListener: OnItemClickListener?) {
            mType = bean.type
            if (bean.imgRes != 0) {
                mImgView.background = ResourcesCompat.getDrawable(itemView.resources, bean.imgRes, null)
                mTextView.text = ""
            } else {
                mImgView.background = null
                mTextView.text = mType
            }
            itemView.setOnClickListener {
                mOnItemClickListener?.onClick(mType)
            }
        }
    }

    interface OnItemClickListener {
        fun onClick(type: @CalculatorButtonType String)
    }
}