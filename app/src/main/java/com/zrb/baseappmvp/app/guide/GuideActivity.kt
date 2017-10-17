package  com.zrb.baseappmvp.app.guide

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import com.zrb.baseappmvp.base.BaseActivity
import com.bigkoo.convenientbanner.ConvenientBanner
import com.zrb.baseappmvp.R
import com.zrb.baseappmvp.app.main.MainActivity
import com.zrb.baseappmvp.tools.SharedPreferencesTools
import kotlinx.android.synthetic.main.activity_guide.*
import org.jetbrains.anko.find

/**
 * Created by zrb on 2017/7/17.
 */
class GuideActivity : BaseActivity<GuidePresenter>(), GuideContract.View {
    override fun showNext(b: Boolean) {
        if (b)
            activity_guide_next.visibility = View.VISIBLE
        else
            activity_guide_next.visibility = View.GONE
    }

    override fun getLayoutID(): Int = R.layout.activity_guide

    override fun initEventAndData() {
        xPresenter?.initIvBanner(find<ConvenientBanner<Int>>(R.id.banner_iv))
    }

    override fun initUI(savedInstanceState: Bundle?) {
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    override fun initPresenter() {
        xPresenter = GuidePresenter()
        xPresenter?.attachView(this, this)
    }

    override fun initClick() {
        setClick(activity_guide_next, {
            SharedPreferencesTools.getInstance().putBoolean(this, "APP_IS_FIRST_LAUNCHER", true)
            startActivity(Intent().setClass(this, MainActivity::class.java))
            finish()
        })
    }

}