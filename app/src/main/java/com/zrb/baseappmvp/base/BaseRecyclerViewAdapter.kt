package com.zrb.baseappmvp.base

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.Spanned
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.TextView


/**
 * Created by zrb on 2017/6/8.
 */
abstract class BaseRecyclerViewAdapter<T, VH : BaseRecyclerViewAdapter.SparseArrayViewHolder>
/**
 * @param list the datas to attach the adapter
 */
(
        /**
         * data
         */
        protected var mList: List<T>) : RecyclerView.Adapter<VH>() {
    private var mContext: Context? = null
    /**
     * click listener
     */
    protected var mOnItemClickListener: OnItemClickListener<T>? = null
    /**
     * long click listener
     */
    protected var mOnItemLongClickListener: OnItemLongClickListener<T>? = null

    /**
     * get a item by index

     * @param position
     * *
     * @return
     */
    protected fun getItem(position: Int): T {
        return mList[position]
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    /**
     * set a long click listener

     * @param onItemLongClickListener
     */
    fun setOnItemLongClickListener(onItemLongClickListener: OnItemLongClickListener<T>) {
        mOnItemLongClickListener = onItemLongClickListener
    }

    /**
     * set a click listener

     * @param onItemClickListener
     */
    fun setOnItemClickListener(onItemClickListener: OnItemClickListener<T>) {
        mOnItemClickListener = onItemClickListener
    }

    /**
     * inflate a view by viewgroup ,id ,etc

     * @param viewGroup
     * *
     * @param layoutId
     * *
     * @param attach
     * *
     * @return
     */
    @JvmOverloads protected fun inflateItemView(viewGroup: ViewGroup, layoutId: Int, attach: Boolean = false): View {
        mContext = viewGroup.context
        return LayoutInflater.from(viewGroup.context).inflate(layoutId, viewGroup, attach)
    }


    /**
     * a final function to avoid you override
     * use template design pattern

     * @param vh
     * *
     * @param position
     */
    override fun onBindViewHolder(vh: VH, position: Int) {
        val item = getItem(position)
        bindDataToItemView(vh, item)
        bindDataToItemView(vh, item, position)
        bindItemViewClickListener(vh, item)
    }


    /**
     * bind data to itemview

     * @param vh   viewholder
     * *
     * @param item item
     */
    protected abstract fun bindDataToItemView(vh: VH, item: T)

    protected fun bindDataToItemView(vh: VH, item: T, position: Int) {

    }

    /**
     * bind click listner to itemview

     * @param vh   viewholder
     * *
     * @param item item
     */
    protected fun bindItemViewClickListener(vh: VH, item: T) {
        if (mOnItemClickListener != null) {
            vh.itemView.setOnClickListener({
                mOnItemClickListener!!.onClick(vh.itemView, item)
            })
        }
        if (mOnItemLongClickListener != null) {
            vh.itemView.setOnLongClickListener({
                onLong(vh, item)
            }

            )
        }
    }

    fun onLong(vh: VH, item: T): Boolean {
        mOnItemLongClickListener!!.onLongClick(vh.itemView, item)
        return true
    }

    /**
     * BaseViewHolder
     * using bindViewById(View view,int id) function to handle the relations between view and viewId
     */
    abstract class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        init {
            findView()
        }

        /**
         * you need to override this method to bind view in the viewholder
         * you'd better use bindViewById(View view,int id)
         */
        protected abstract fun findView()

        /**
         * generic function to findViewById

         * @param id viewId
         * *
         * @return the view found
         */
        protected fun <T : View> findViewById(id: Int): T {
            return itemView.findViewById<T>(id)
        }

    }

    open class SparseArrayViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val views: SparseArray<View> = SparseArray<View>()

        fun <T : View> getView(id: Int): T {
            var view: View? = views.get(id)
            if (view == null) {
                view = itemView.findViewById(id)
                views.put(id, view)
            }
            return view as T
        }

        fun setText(viewId: Int, value: String): SparseArrayViewHolder {
            val view = getView<TextView>(viewId)
            view.setText(value)
            return this@SparseArrayViewHolder
        }

        fun setText(viewId: Int, value: Spanned): SparseArrayViewHolder {
            val view = getView<TextView>(viewId)
            view.setText(value)
            return this@SparseArrayViewHolder
        }

        fun setTextColor(viewId: Int, textColor: Int): SparseArrayViewHolder {
            val view = getView<TextView>(viewId)
            view.setTextColor(textColor)
            return this@SparseArrayViewHolder
        }

        fun setImageResource(viewId: Int, imageResId: Int): SparseArrayViewHolder {
            val view = getView<ImageView>(viewId)
            view.setImageResource(imageResId)
            return this@SparseArrayViewHolder
        }

        fun setBackgroundColor(viewId: Int, color: Int): SparseArrayViewHolder {
            val view = getView<View>(viewId)
            view.setBackgroundColor(color)
            return this@SparseArrayViewHolder
        }

        fun setBackgroundResource(viewId: Int, backgroundRes: Int): SparseArrayViewHolder {
            val view = getView<View>(viewId)
            view.setBackgroundResource(backgroundRes)
            return this@SparseArrayViewHolder
        }

        fun setVisible(viewId: Int, visible: Boolean): SparseArrayViewHolder {
            val view = getView<View>(viewId)
            view.setVisibility(if (visible) View.VISIBLE else View.GONE)
            return this@SparseArrayViewHolder
        }

        fun setOnClickListener(viewId: Int, listener: View.OnClickListener): SparseArrayViewHolder {
            val view = getView<View>(viewId)
            view.setOnClickListener(listener)
            return this@SparseArrayViewHolder
        }

        fun setOnTouchListener(viewId: Int, listener: View.OnTouchListener): SparseArrayViewHolder {
            val view = getView<View>(viewId)
            view.setOnTouchListener(listener)
            return this@SparseArrayViewHolder
        }

        fun setOnLongClickListener(viewId: Int, listener: View.OnLongClickListener): SparseArrayViewHolder {
            val view = getView<View>(viewId)
            view.setOnLongClickListener(listener)
            return this@SparseArrayViewHolder
        }

        fun setTag(viewId: Int, tag: Any): SparseArrayViewHolder {
            val view = getView<View>(viewId)
            view.setTag(tag)
            return this@SparseArrayViewHolder
        }

        fun setTag(viewId: Int, id: Int, tag: Any): SparseArrayViewHolder {
            val view = getView<View>(viewId)
            view.setTag(id, tag)
            return this@SparseArrayViewHolder
        }

        fun setImage(viewId: Int, url: String): SparseArrayViewHolder {
            val view = getView<ImageView>(viewId)
//            MyImageTool.getImg(view, url)
            return this@SparseArrayViewHolder
        }

        fun setroundImg(viewId: Int, url: String): SparseArrayViewHolder {
            val view = getView<ImageView>(viewId)
//            MyImageTool.getroundImg(view, url)
            return this@SparseArrayViewHolder
        }

        fun setSelect(viewId: Int, select: Boolean): SparseArrayViewHolder {
            val view = getView<View>(viewId)
            view.setSelected(select)
            return this@SparseArrayViewHolder
        }

        fun setOnCheckedChangeListener(viewId: Int, select: CompoundButton.OnCheckedChangeListener): SparseArrayViewHolder {
            val view = getView<CheckBox>(viewId)
            view.setOnCheckedChangeListener(select)
            return this@SparseArrayViewHolder
        }

        fun setChecked(viewId: Int, check: Boolean): SparseArrayViewHolder {
            val view = getView<CheckBox>(viewId)
            view.setChecked(check)
            return this@SparseArrayViewHolder
        }
    }

    /**
     * 点击长按

     * @param <T>
    </T> */
    interface OnItemLongClickListener<T> {
        fun onLongClick(view: View, item: T)
    }

    /**
     * 点击

     * @param <T>
    </T> */
    interface OnItemClickListener<T> {
        fun onClick(view: View, item: T)
    }
}
/**
 * inflate a view by viewgroup ,id ,etc

 * @param viewGroup
 * *
 * @param layoutId
 * *
 * @return
 */