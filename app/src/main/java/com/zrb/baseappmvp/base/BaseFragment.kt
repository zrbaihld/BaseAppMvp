package com.zrb.baseappmvp.base

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.zrb.baseappmvp.network.RxManager
import com.jakewharton.rxbinding2.view.RxView
import com.zrb.baseappmvp.R
import com.zrb.baseappmvp.network.BaseDataRepository
import kotlinx.android.synthetic.main.top_bar_view.*
import org.jetbrains.anko.support.v4.find
import org.jetbrains.anko.textColor
import org.jetbrains.anko.toast
import java.util.concurrent.TimeUnit

/**
 * Created by zrb on 2017/6/6.
 */
abstract class BaseFragment<T : XPresenter<*>> : Fragment(), XView {
    private val STATE_SAVE_IS_HIDDEN = "STATE_SAVE_IS_HIDDEN"

    var xPresenter: T? = null
    var rxManager: RxManager? = null

    protected var mDataRepository: BaseDataRepository? = null
    var mContext: Context? = null
    var mActivity: Activity? = null

    //是否可见状态
    var XisVisible: Boolean = false
    //View已经初始化完成
    var isPrepared: Boolean = false
    //是否第一次加载完
    var isFirstLoad = true

    var mRootView: View? = null

    override fun onAttach(context: Context?) {
        mContext = context
        mActivity = activity
        super.onAttach(context)

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (mRootView != null) {
            isPrepared = true
            return mRootView
        }
        isFirstLoad = true
        //绑定View
        mRootView = inflater?.inflate(getLayoutID(), container, false)
        isPrepared = true
        return mRootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        rxManager = RxManager()
        mDataRepository = BaseDataRepository(mContext)
        initPresenter()
        initUI()
//初始化事件和获取数据, 在此方法中获取数据不是懒加载模式
        initEventAndData()
        //在此方法中获取数据为懒加载模式,如不需要懒加载,请在initEventAndData获取数据,GankFragment有使用实例
        lazyLoad()

        initClick()
    }

    abstract fun getLayoutID(): Int
    abstract fun initUI()
    abstract fun initPresenter()
    abstract fun initEventAndData()
    abstract fun initClick()
    protected abstract fun lazyLoadData()

    fun setClick(view: View, todo: () -> Unit) {
        RxView.clicks(view)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe({ todo() })
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (userVisibleHint) {
            XisVisible = true
            onVisible()
        } else {
            XisVisible = false
            onInvisible()
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            XisVisible = true
            onVisible()
        } else {
            XisVisible = false
            onInvisible()
        }
    }

    protected fun onVisible() {
        lazyLoad()
    }

    protected fun onInvisible() {}

    protected fun lazyLoad() {
        if (!isPrepared || !XisVisible || !isFirstLoad) return
        isFirstLoad = false
        lazyLoadData()
    }

    protected fun setWhiteTitle(title: String) {
        find<View>(R.id.top_bar_view).setBackgroundColor(ContextCompat.getColor(mContext, R.color.white))
        val tv_title = find<TextView>(R.id.tv_title)
        tv_title.text = title
        tv_title.textColor = ContextCompat.getColor(mContext, R.color.TextTitleColor)
    }
    fun setRightText(text: String) {
        if (top_bar_view == null)
            return
        tv_right.visibility = View.VISIBLE
        ll_right.visibility = View.GONE
        tv_right.text = text
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState!!.putBoolean(STATE_SAVE_IS_HIDDEN, isHidden)
    }

    override fun onDetach() {
        super.onDetach()
        try {
            val childFragmentManager = android.support.v4.app.Fragment::class.java.getDeclaredField("mChildFragmentManager")
            childFragmentManager.isAccessible = true
            childFragmentManager.set(this, null)
        } catch (e: NoSuchFieldException) {
            throw RuntimeException(e)
        } catch (e: IllegalAccessException) {
            throw RuntimeException(e)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        xPresenter?.detachView()
        rxManager?.clear()
    }

    override fun requestError(type: Int, e: String) {
        mActivity?.toast(e)

    }

    override fun requestSuccess(type: Int, e: String) {
        mActivity?.toast(e)
    }

    override fun showtoast(e: String) {
        mActivity?.toast(e)
    }

    override fun finish(e: String) {
        mActivity?.toast(e)
    }

    override fun showNoData() {
        if (mActivity != null)
            mActivity?.toast(mActivity!!.getString(R.string.has_no_data))
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }
}