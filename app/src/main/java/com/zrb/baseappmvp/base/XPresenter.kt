package com.zrb.baseappmvp.base

import android.app.Activity

/**
 * Created by zrb on 2017/6/6.
 */
interface XPresenter<T:XView>{
    fun attachView(view:T,context: Activity)

    fun detachView()

    fun back()


}

