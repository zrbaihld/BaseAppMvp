package com.zrb.baseappmvp.base

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.view.View
import com.zrb.baseappmvp.network.BaseNetObver
import com.zrb.baseappmvp.network.RxManager
import com.jakewharton.rxbinding2.view.RxView
import com.zrb.baseappmvp.dialog.LoadingDialog
import com.zrb.baseappmvp.network.BaseDataRepository
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.RequestBody
import java.util.concurrent.TimeUnit


/**
 * Created by zrb on 2017/6/6.
 */

abstract class BasePresenter<T : XView> : XPresenter<T> {
    var mActivity: Activity? = null
    var mView: T? = null
    var dataRepository: BaseDataRepository? = null
    var mRx = RxManager()


    override fun attachView(view: T, context: Activity) {
        this.mView = view
        this.mActivity = context
        dataRepository = BaseDataRepository(mActivity)
        this.onStart()

    }

    override fun detachView() {
        this.mView = null
        mRx.clear()
    }

    override fun back() {
        mActivity?.finish()
    }

    fun onStart() {}


    fun setClick(view: View, todo: () -> Unit) {
        RxView.clicks(view)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe({
                    todo()
                })
    }

    fun setLongClick(view: View, todo: () -> Unit) {
        RxView.longClicks(view)
                .subscribe({
                    todo()
                })
    }

    fun setNoClick(view: View, todo: () -> Unit) {
        RxView.clicks(view)
                .subscribe({
                    todo()
                })
    }

    fun doRequest(url: String, paramMap: RequestBody, netObver: BaseNetObver<*>) {
        doRequest(url,paramMap,netObver,{})
//        if (!isNetworkConnected(mActivity)) {
//            showToast("当前无网络")
//            return
//        }
//        LoadingDialog.showLoading(mActivity, {
//            showToast("网络请求超时")
//        })
//        dataRepository?.postString(url, paramMap)
//                ?.subscribeOn(Schedulers.newThread())               //在IO线程进行网络请求
//                ?.observeOn(AndroidSchedulers.mainThread())  //回到主线程去处理请求注册结果
//                ?.subscribe(netObver)  //回到主线程去处理请求注册结果
    }

    fun doRequest(url: String, paramMap: RequestBody, netObver: BaseNetObver<*>, dismiss: () -> Unit) {
        if (!isNetworkConnected(mActivity)) {
            showToast("当前无网络")
            return
        }
        LoadingDialog.showLoading(mActivity, dismiss)
        dataRepository?.postString(url, paramMap)
                ?.subscribeOn(Schedulers.newThread())               //在IO线程进行网络请求
                ?.observeOn(AndroidSchedulers.mainThread())  //回到主线程去处理请求注册结果
                ?.subscribe(netObver)  //回到主线程去处理请求注册结果
    }

    fun doNullDialogRequest(url: String, paramMap: RequestBody, netObver: BaseNetObver<*>) {
        if (!isNetworkConnected(mActivity)) {
            showToast("当前无网络")
            return
        }
        dataRepository?.postString(url, paramMap)
                ?.subscribeOn(Schedulers.newThread())               //在IO线程进行网络请求
                ?.observeOn(AndroidSchedulers.mainThread())  //回到主线程去处理请求注册结果
                ?.subscribe(netObver)  //回到主线程去处理请求注册结果
    }


    fun isNetworkConnected(context: Context?): Boolean {
        if (context != null) {
            val mConnectivityManager = context
                    .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val mNetworkInfo = mConnectivityManager.activeNetworkInfo
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable
            }
        }
        return false
    }

    fun showToast(Str: String) {
        Observable.just(Str)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { str ->
                    mView?.showtoast(str)
                }


    }
}