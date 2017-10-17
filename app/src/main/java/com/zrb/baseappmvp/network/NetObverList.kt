package com.zrb.baseappmvp.network

/**
 * Created by zrb on 2017/6/12.
 */

abstract class NetObverList<T> : BaseNetObver<T>() {
    override fun postResponse(t: T) {
       postResponse(arrayListOf<T>())
    }
}
