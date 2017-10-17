package com.zrb.baseappmvp.base

import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View
import com.zrb.baseappmvp.network.RxManager
import com.jakewharton.rxbinding2.view.RxView
import com.zhy.autolayout.AutoLayoutActivity
import com.zrb.baseappmvp.R
import com.zrb.baseappmvp.base.AppManager
import com.zrb.baseappmvp.dialog.LoadingDialog
import com.zrb.baseappmvp.network.BaseDataRepository
import com.zrb.baseappmvp.tools.StatusBarCompat
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.top_bar_view.*
import org.jetbrains.anko.textColor
import org.jetbrains.anko.toast
import java.util.concurrent.TimeUnit


/**
 * Created by zrb on 2017/6/3.
 */

abstract class BaseActivity<T : XPresenter<*>> : AutoLayoutActivity(), XView {
    var xPresenter: T? = null
    var rxManager: RxManager? = null
    protected var mDataRepository: BaseDataRepository? = null
    var mContext: Context? = null
    var isActivityShow = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDataRepository = BaseDataRepository(this)
        mContext = this
        setContentView(getLayoutID())
        setBaseConfig()
        initPresenter()
        initUI(savedInstanceState)
//        xPresenter?.attachView(this, this)
        initEventAndData()
        initClick()

    }


    abstract fun getLayoutID(): Int
    abstract fun initEventAndData()
    abstract fun initUI(savedInstanceState: Bundle?)
    abstract fun initPresenter()
    abstract fun initClick()

    fun onNewThread(todo: () -> Unit, refrashUI: () -> Unit) {
        Observable.just("")
                .observeOn(Schedulers.newThread())
                .map { todo() }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { refrashUI() }
    }

    fun setClick(view: View, todo: () -> Unit) {
        RxView.clicks(view)
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe({ todo() })
    }

    fun setLongClick(view: View, todo: () -> Unit) {
        RxView.longClicks(view)
                .subscribe({ todo() })
    }

    fun setNoClick(view: View, todo: () -> Unit) {
        RxView.clicks(view)
                .subscribe({ todo() })
    }


    private fun setBaseConfig() {
        initTheme()
        AppManager.getAppManager().addActivity(this)
        //        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        rxManager = RxManager()
        SetStatusBarColor()
        if (iv_back != null)
            setClick((iv_back), { finish() })
    }

    fun setWhiteTitle(title: String) {
        if (top_bar_view == null)
            return
        top_bar_view.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white))
        tv_title.text = title
        tv_title.textColor = ContextCompat.getColor(this, R.color.TextTitleColor)
    }

    fun setRightText(text: String) {
        if (top_bar_view == null)
            return
        tv_right.visibility = View.VISIBLE
        ll_right.visibility = View.GONE
        tv_right.text = text
    }


    private fun initTheme() {}

    /**
     * 着色状态栏（4.4以上系统有效）
     */
    protected fun SetStatusBarColor() {
        StatusBarCompat.setStatusBarColor(this, ContextCompat.getColor(this, R.color.mainColor))
    }

    /**
     * 着色状态栏（4.4以上系统有效）
     */
    protected fun SetStatusBarColor(color: Int) {
        StatusBarCompat.setStatusBarColor(this, color)
    }

    /**
     * 沉浸状态栏（4.4以上系统有效）
     */
    protected fun SetTranslanteBar() {
        StatusBarCompat.translucentStatusBar(this)
    }

    fun showLoaing() {
        LoadingDialog.showLoading(this)
    }

    fun showLoaing(msg: String) {
        LoadingDialog.showLoading(this, msg, true)
    }

    fun dismissLoading() {
        LoadingDialog.disDialog()
    }

    override fun onDestroy() {
        LoadingDialog.disDialog()
        super.onDestroy()
        xPresenter?.detachView()
        rxManager?.clear()
        AppManager.getAppManager().finishActivity(this)
    }

    override fun requestError(type: Int, e: String) {
        toast(e)
    }

    override fun requestSuccess(type: Int, e: String) {
        toast(e)
    }

    override fun showtoast(e: String) {
        toast(e)
    }

    override fun showNoData() {
//        toast(getString(R.string.has_no_data))
    }

    override fun finish(e: String) {
        toast(e)
        finish()
    }

    override fun onResume() {
        super.onResume()
        isActivityShow = true
    }

    override fun onPause() {
        super.onPause()
        isActivityShow = false
    }

    //禁止旋转
    override fun setRequestedOrientation(requestedOrientation: Int) {
        return
    }
}
