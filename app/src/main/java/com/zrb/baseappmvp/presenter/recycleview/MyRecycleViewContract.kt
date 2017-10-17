package com.zrb.baseappmvp.presenter.recycleview

import android.support.v7.widget.RecyclerView
import com.zrb.baseappmvp.base.XPresenter
import com.zrb.baseappmvp.base.XView

/**
 * Created by zrb on 2017/10/17.
 */
interface MyRecycleViewContract {
    interface MyRecycleViewView : XView {
        fun initRecyclerView(adapter: RecyclerView.Adapter<*>)
        fun resetRecycleView()
    }

    interface MyRecycleViewPresenter {
        fun initView(view: MyRecycleViewView)
    }
}