package  com.zrb.baseappmvp.app.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.zrb.baseappmvp.base.BaseActivity
import com.zrb.baseappmvp.R
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.top_bar_view.*
import org.jetbrains.anko.toast


/**
 * Created by zrb on 2017/6/6.
 */
class LoginActivity : BaseActivity<LoginPresenter>(), LoginContract.View {
    override fun getLayoutID(): Int = R.layout.activity_login


    override fun login(type: Int) {
        when (type) {


        }
        finish()
    }

    override fun loginNUll(e: String) {
        toast(e)
    }

    override fun initPresenter() {
        xPresenter = LoginPresenter()
        xPresenter!!.attachView(this, this)
    }

    override fun initClick() {
        setClick(activity_login_login, {
            xPresenter!!.login(et_mobile.text.toString(),
                    et_password.text.toString())
        })

//        setClick(activity_login_forgetpassword, { startActivity(Intent().setClass(this, ForgetPasswordActivity::class.java)) })
//        setClick(activity_login_register, { startActivity(Intent().setClass(this, RegisterActivity::class.java)) })
        setClick(activity_login_otherlogin, { })


    }

    override fun initUI(savedInstanceState: Bundle?) {
        tv_title.text = "登录"
        iv_back.visibility = View.GONE
//        activity_login_phone.setText(SharedPreferencesTools.getInstance().getString("user_name"))
//        activity_login_password.setText(SharedPreferencesTools.getInstance().getString("password"))


    }

    override fun initEventAndData() {
        xPresenter?.autoLogin()
    }


}