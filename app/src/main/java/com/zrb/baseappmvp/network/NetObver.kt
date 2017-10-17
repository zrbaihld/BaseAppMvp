package com.zrb.baseappmvp.network

import java.util.*

/**
 * Created by zrb on 2017/6/12.
 */

abstract class NetObver<T> : BaseNetObver<T>() {
    override fun postResponse(t: ArrayList< T>) {
    }
}
