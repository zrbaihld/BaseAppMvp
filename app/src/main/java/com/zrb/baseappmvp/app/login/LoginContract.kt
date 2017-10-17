package  com.zrb.baseappmvp.app.login

import com.zrb.baseappmvp.base.BasePresenter
import com.zrb.baseappmvp.base.XPresenter
import com.zrb.baseappmvp.base.XView

/**
 * Created by zrb on 2017/6/6.
 */
interface LoginContract{
    interface View :XView{
        fun login(type:Int)
        fun loginNUll(msg:String)
    }

    interface Persenter:XPresenter<View>{
        fun login(username: String, password: String)
        fun autoLogin()
    }
}