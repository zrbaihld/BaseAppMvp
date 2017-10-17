package com.zrb.baseappmvp.presenter.recycleview

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.zrb.baseappmvp.base.BasePresenter
import com.zrb.baseappmvp.network.BaseNetObver
import com.zrb.baseappmvp.presenter.recycleview.MyRecycleViewContract
import okhttp3.RequestBody
import java.util.ArrayList

/**
 * Created by zrb on 2017/9/26.
 */
abstract class MyRecycleViewPresenter<T> : BasePresenter<MyRecycleViewContract.MyRecycleViewView>() {
    var index = 0
    val adapterList = arrayListOf<T>()
     fun onRefresh() {
        index = 0
        getlist()
    }

     fun onLoadMore() {
        getlist()
    }

    private var myadapter = MyAdapter()
     fun initView() {
        setAdapter()
        getlist()

    }

    fun setAdapter() {
        mView?.initRecyclerView(myadapter)
    }


    open fun getlist() {
        doRequest(getListUrl(), getListRequestBody(),
                getNetObver())
    }

    fun initDate(t: ArrayList<T>) {
        mView?.resetRecycleView()
        if (index == 0) {
            adapterList.clear()
        }
        if (t.size == 0) {
            mView?.showNoData()
            mView?.resetRecycleView()
        }
        adapterList.addAll(t)
        index = adapterList.size
        myadapter.notifyDataSetChanged()
    }

    fun initError() {
        mView?.resetRecycleView()
        adapterList.clear()
        index = adapterList.size
        myadapter.notifyDataSetChanged()
    }

    fun notifyDataSetChanged() {
        myadapter.notifyDataSetChanged()
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v)

    private inner class MyAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
            myBindViewHolder(holder, position)
        }

        override fun getItemCount(): Int = getRecycleViewCount()

        override fun onCreateViewHolder(p0: ViewGroup?, p1: Int): RecyclerView.ViewHolder = getViewHolder(p0)

    }
  open  fun getRecycleViewCount(): Int=adapterList.size
    abstract fun getListUrl(): String
    abstract fun getNetObver(): BaseNetObver<*>
    abstract fun getListRequestBody(): RequestBody
    abstract fun getAdapterId(): Int
    abstract fun myBindViewHolder(p0: RecyclerView.ViewHolder?, p1: Int)
    open fun getViewHolder(p0: ViewGroup?): RecyclerView.ViewHolder {
        return ViewHolder(
                View.inflate(mActivity, getAdapterId(), null))
    }

}