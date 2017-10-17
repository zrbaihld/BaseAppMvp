package com.zrb.baseappmvp.base

/**
 * Created by zrb on 2017/6/6.
 */
interface XView {
    fun requestError(type: Int, e: String)
    fun requestSuccess(type: Int, e: String)
    fun showtoast(e: String)
    fun finish(e: String)
    fun showNoData()
}