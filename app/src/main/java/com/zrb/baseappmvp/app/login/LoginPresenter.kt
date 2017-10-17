package  com.zrb.baseappmvp.app.login

import android.text.TextUtils
import com.zrb.baseappmvp.base.BasePresenter
import com.zrb.baseappmvp.network.NetObver
import com.zrb.baseappmvp.network.XRequestBodyManager
import com.zrb.baseappmvp.network.URLManager
import com.zrb.baseappmvp.base.MyApp
import com.zrb.baseappmvp.bean.LoginBean
import com.zrb.baseappmvp.constant.Constants
import com.zrb.baseappmvp.tools.SharedPreferencesTools


/**
 * Created by zrb on 2017/6/6.
 */
class LoginPresenter : BasePresenter<LoginContract.View>(), LoginContract.Persenter {
    override fun autoLogin() {
        if (!TextUtils.isEmpty(SharedPreferencesTools.getInstance().getString(Constants.SESSIONID))
                && !TextUtils.isEmpty(SharedPreferencesTools.getInstance().getString(Constants.USERID))) {
            when (SharedPreferencesTools.getInstance().getString(Constants.USERROLE)) {
                "0" -> {
                    mView?.login(1)
                }
                "2" -> {
                    mView?.login(2)
                }
                "3" -> {
                    mView?.login(3)
                }
                "4" -> {
                    mView?.login(4)
                }
            }
        }
    }


    override fun login(username: String, password: String) {
        if (TextUtils.isEmpty(username)) {
            mView?.loginNUll("手机号码为空")
        } else if (TextUtils.isEmpty(password)) {
            mView?.loginNUll("密码为空")
        } else {
            doRequest(URLManager.doLogin, XRequestBodyManager.doLoginOrRegisterOrResetpwd(
                    username, password
            ), object : NetObver<LoginBean>() {
                override fun postError(code: Int, e: String) {
                    mView?.loginNUll(e)
                }

                override fun postResponse(t: LoginBean) {
                    SharedPreferencesTools.getInstance().putString(mActivity, Constants.USERID, t.user_id)
                    SharedPreferencesTools.getInstance().putString(mActivity, Constants.USERROLE, t.user_role)
                    SharedPreferencesTools.getInstance().putString(mActivity, Constants.SESSIONID, t.session_id)
                    SharedPreferencesTools.getInstance().putString(mActivity, Constants.USERHEADPIC, t.head_pic)
                    SharedPreferencesTools.getInstance().putString(mActivity, Constants.USERNAME, t.true_name)
                    SharedPreferencesTools.getInstance().putString(mActivity, Constants.HOSTVIEWIMG, t.host_view_img)
                    SharedPreferencesTools.getInstance().putString(mActivity, Constants.RONGYUNTOKEN, t.rongyun_token)
                    SharedPreferencesTools.getInstance().host_view_img = t.host_view_img

                    if (username != SharedPreferencesTools.getInstance().getString("user_name"))
                        clearData()
                    SharedPreferencesTools.getInstance().putString("user_name", username)
                    SharedPreferencesTools.getInstance().putString("password", password)
                    when (t.user_role) {
                        "0" -> {
                            mView?.login(1)
                        }
                        "2" -> {
                            mView?.login(2)
                        }
                        "3" -> {
                            mView?.login(3)
                        }
                        "4" -> {
                            mView?.login(4)
                        }
                    }

                }
            })
        }
    }
    fun clearData() {
        SharedPreferencesTools.getInstance().student_id = 0
        SharedPreferencesTools.getInstance().school_id = 0

        val app = mActivity?.application as MyApp
    }
}

