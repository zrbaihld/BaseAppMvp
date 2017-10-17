package com.zrb.baseappmvp.app.main

import com.zrb.baseappmvp.base.XPresenter
import com.zrb.baseappmvp.base.XView

/**
 * Created by zrb on 2017/10/17.
 */
interface MainContract{
    interface View : XView {
    }

    interface Persenter: XPresenter<View> {
        fun initView()
    }
}