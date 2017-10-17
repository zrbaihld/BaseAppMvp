package com.zrb.baseappmvp.app.main

import android.content.Intent
import android.os.Bundle
import  com.zrb.baseappmvp.app.login.LoginActivity
import com.zrb.baseappmvp.base.BaseActivity
import com.zrb.baseappmvp.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.top_bar_view.*

/**
 * Created by zrb on 2017/10/17.
 */
class MainActivity : BaseActivity<MainPresenter>(), MainContract.View {

    override fun getLayoutID(): Int = R.layout.activity_main

    override fun initEventAndData() {
        xPresenter?.initView()
    }

    override fun initUI(savedInstanceState: Bundle?) {
        setWhiteTitle("首页")
    }

    override fun initPresenter() {
        xPresenter = MainPresenter()
        xPresenter?.attachView(this, this)
    }

    override fun initClick() {
        setClick(to_recycleView, { })
        setClick(to_takephoto, {  })
        setClick(to_premiss, {  })
        setClick(to_greendao, {  })
        setClick(to_network, { })
        setClick(to_rxbus, {  })
        setClick(to_login, { startActivity(Intent(this, LoginActivity::class.java)) })
    }

}