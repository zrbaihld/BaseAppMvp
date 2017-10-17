package com.zrb.baseappmvp.base

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.Spanned
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.zrb.baseappmvp.tools.ImageLoadTools
import org.jetbrains.anko.find
import org.jetbrains.anko.textColor

/**
 * Created by zrb on 2017/10/6.
 */
abstract class BaseRecrcleView<T>(var context: Context, var layoutID: Int, var Datas: List<T>) : RecyclerView.Adapter<BaseRecrcleView.MyHoldView>() {


    override fun onBindViewHolder(holder: MyHoldView?, position: Int) {
        initData(holder, Datas[position], position)
    }

    override fun getItemCount(): Int = Datas.size

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MyHoldView = MyHoldView(View.inflate(context, layoutID, null))

    abstract fun initData(holder: MyHoldView?, Data: T, position: Int)

    @Suppress("UNCHECKED_CAST")
    class MyHoldView(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val views: SparseArray<View> = SparseArray()

        fun <T : View> getView(id: Int): T {
            if (views.get(id) == null) {
                views.put(id, itemView.findViewById(id))
            }
            return views[id] as T
        }

        fun setText(id: Int, value: String): MyHoldView {
            getView<TextView>(id).text = value
            return this
        }

        fun setText(id: Int, value: Spanned): MyHoldView {
            getView<TextView>(id).text = value
            return this
        }

        fun setBackgroundColor(id: Int, color: Int): MyHoldView {
            getView<View>(id).setBackgroundColor(color)
            return this
        }

        fun setTextColor(id: Int, textColor: Int): MyHoldView {
            getView<TextView>(id).textColor = textColor
            return this
        }

        fun setImageResource(id: Int, imageViewId: Int): MyHoldView {
            getView<ImageView>(id).setImageResource(imageViewId)
            return this
        }

        fun setBackgroundResource(id: Int, imageViewId: Int): MyHoldView {
            getView<ImageView>(id).setBackgroundResource(imageViewId)
            return this
        }

        fun setVisible(id: Int, visible: Boolean): MyHoldView {
            getView<View>(id).visibility = if (visible) View.VISIBLE else View.GONE
            return this
        }

        fun setOnClickListener(id: Int, listener: View.OnClickListener): MyHoldView {
            getView<View>(id).setOnClickListener(listener)
            return this
        }

        fun setOnTouchListener(id: Int, listener: View.OnTouchListener): MyHoldView {
            getView<View>(id).setOnTouchListener(listener)
            return this
        }

        fun setOnLongClickListener(id: Int, listener: View.OnLongClickListener): MyHoldView {
            getView<View>(id).setOnLongClickListener(listener)
            return this
        }

        fun setTag(id: Int, tag: Any): MyHoldView {
            getView<View>(id).tag = tag
            return this
        }

        fun setTag(id: Int, tagId: Int, tag: Any): MyHoldView {
            getView<View>(id).setTag(tagId, tag)
            return this
        }

        fun setSelect(id: Int, select: Boolean): MyHoldView {
            getView<View>(id).isSelected = select
            return this
        }

        fun setImg(id: Int, path: String): MyHoldView {
            ImageLoadTools.LoadImageNoRound(itemView.context, path, getView<ImageView>(id))
            return this
        }
        fun setRoundImg(id: Int, path: String): MyHoldView {
            ImageLoadTools.LoadCirclePicSmallImage(itemView.context, path, getView<ImageView>(id))
            return this
        }
    }
}

