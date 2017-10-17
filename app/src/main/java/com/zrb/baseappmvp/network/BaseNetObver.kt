package com.zrb.baseappmvp.network

import android.app.LauncherActivity
import android.content.Intent
import android.text.TextUtils
import com.zrb.baseappmvp.tools.ExceptionUtil
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.google.gson.internal.`$Gson$Types`
import com.zrb.baseappmvp.base.AppManager
import com.zrb.baseappmvp.constant.Constants
import com.zrb.baseappmvp.dialog.LoadingDialog
import com.zrb.baseappmvp.tools.LogUtil
import com.zrb.baseappmvp.tools.SharedPreferencesTools
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type


/**
 * Created by zrb on 2017/6/22.
 */
abstract class BaseNetObver<T> : Observer<String> {
    val LOGINOUTTIMEEXCEPTION = 1
    val HTTPERROREXCEPTION = 2
    val RESPONSEBODYEXCEPTION = 3
    val DATANULL = 4
    val DATAISNOTJSON = 5
    val DATATRANSFORMERROR = 6
    val JXSAuthorityException = 7


    var gson = Gson()

    var rawType: Class<T>? = null
    var mType: Type? = null


    override fun onSubscribe(d: Disposable) {

        LogUtil.e("onSubscribe")

    }

    override fun onNext(date: String) {
//        LoadingDialog.disDialog()
        LogUtil.e("onNext=" + date)
        postResponseData(date)
        mType = getSuperclassTypeParameter(javaClass)

        rawType = `$Gson$Types`.getRawType(mType) as Class<T>

//        try {
        if (!TextUtils.isEmpty(date)) {
            if ("{" == (date.substring(0, 1))) {
                var bean = gson.fromJson<T>(date, rawType)
                LogUtil.e(" postResponse(bean)   $date")
                LogUtil.e(" postResponse(bean)   $bean")
                postResponse(bean)
            } else if ("[" == (date.substring(0, 1))) {
                postResponse(fromJsonList(date, rawType!!))
            } else {
                postResponse(rawType?.newInstance() as T)
            }
        } else {
            LogUtil.e("!TextUtils.isEmpty(date)")
            postResponse(rawType?.newInstance() as T)
        }
//        } catch (e: Exception) {
//            LogUtil.e("e.message    " + e.message)
//            postError(DATAISNOTJSON, "返回数据为空")
//        }
    }


    override fun onError(e: Throwable) {
        LoadingDialog.disDialog()
        if (e is ExceptionUtil.LoginOuttimeException) {
            postError(LOGINOUTTIMEEXCEPTION, e.meg)
            LogUtil.e("onError  LoginOuttimeException")
        } else if (e is ExceptionUtil.HttpErrorException) {
            LogUtil.e("onError  HttpErrorException")
            postError(HTTPERROREXCEPTION, "请求返回出错")
        } else if (e is ExceptionUtil.ResponseBodyException) {
            LogUtil.e("onError  ResponseBodyException")
            postError(RESPONSEBODYEXCEPTION, "解析错误")
        } else if (e is ExceptionUtil.JXSAuthorityException) {
            LogUtil.e("onError  JXSAuthorityException")
            postError(JXSAuthorityException, "无权限")
        } else if (e is ExceptionUtil.RemoteLoginException) {
            LogUtil.e("onError  RemoteLoginException")
            postError(RESPONSEBODYEXCEPTION, "异地登录")
            SharedPreferencesTools.getInstance().putString(Constants.USERID, "")
            SharedPreferencesTools.getInstance().putString(Constants.SESSIONID, "")
            AppManager.getAppManager().finishAllActivity()
//            AppManager.getApplication().startActivity(Intent().setClass(AppManager.getApplication(), LauncherActivity::class.java)
//                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        } else {
            LogUtil.e("onError  " + e.message)
        }
    }

    override fun onComplete() {
        LoadingDialog.disDialog()
        LogUtil.e("onComplete")
    }

    abstract fun postError(code: Int, e: String)
    abstract fun postResponse(t: ArrayList<T>)
    abstract fun postResponse(t: T)
    open fun postResponseData(data: String) {

    }

    fun getSuperclassTypeParameter(subclass: Class<*>): Type {
        val superclass = subclass.genericSuperclass
        if (superclass is Class<*>) {
            throw RuntimeException("Missing type parameter.")
        }
        val parameterized = superclass as ParameterizedType
        return `$Gson$Types`.canonicalize(parameterized.actualTypeArguments[0])
    }

    fun <T> fromJsonList(json: String, cls: Class<T>): ArrayList<T> {
        val array = JsonParser().parse(json).asJsonArray
        return array.mapTo(ArrayList<T>()) { gson.fromJson(it, cls) }
    }
}
