package com.zrb.baseappmvp.network

import com.zrb.baseappmvp.network.RxBus
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer


/**
 * 用于管理RxBus的事件和Rxjava相关代码的生命周期处理
 * not used now
 */

class RxManager {

    var mRxBus = RxBus.`$`()
    private val mObservables = HashMap<String, Observable<*>>()// 管理观察源
    private val mCompositeSubscription = CompositeDisposable()// 管理订阅者


    fun on(eventName: String, action1: Consumer<Any>) {
        val mObservable = mRxBus.register<Any>(eventName)
        mObservables.put(eventName, mObservable)
        mCompositeSubscription.add(mObservable.observeOn(AndroidSchedulers.mainThread())
                .subscribe(action1, Consumer<Throwable> { }))
    }

    fun add(m: Disposable) {
        mCompositeSubscription.add(m)
    }

    fun clear() {
        mCompositeSubscription.dispose()// 取消订阅
        for ((key, value) in mObservables)
            mRxBus.unregister(key, value)// 移除观察
    }

    fun post(tag: Any, content: Any) {
        mRxBus.post(tag, content)
    }
    fun post(content: Any) {
        mRxBus.post(content)
    }
}