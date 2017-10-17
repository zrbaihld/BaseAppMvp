package com.zrb.baseappmvp.network

import com.google.gson.Gson
import com.zrb.baseappmvp.tools.LogUtil
import com.zrb.baseappmvp.tools.SharedPreferencesTools
import okhttp3.RequestBody

/**
 * Created by zrb on 2017/6/14.
 */
object XRequestBodyManager {
    val gson = Gson()
    val MEDIATYPE = "application/json;charset=UTF-8"

    fun getRequestBody(strEntity: String): RequestBody {
        LogUtil.e("RequestBody= " + strEntity)
        return RequestBody.create(okhttp3.MediaType.parse(MEDIATYPE), strEntity)
    }

    fun getHashMap(): HashMap<String, Any> {
        val baseHm = HashMap<String, Any>()
        return baseHm
    }

    fun doLoginOrRegisterOrResetpwd(username: String, password: String): RequestBody {
        val body = getHashMap()
        body.put("username", username)
        body.put("password", password)
        return getRequestBody(gson.toJson(body))
    }
}