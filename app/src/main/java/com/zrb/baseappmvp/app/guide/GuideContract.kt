package  com.zrb.baseappmvp.app.guide

import android.support.v7.widget.RecyclerView
import com.zrb.baseappmvp.base.XPresenter
import com.zrb.baseappmvp.base.XView
import com.bigkoo.convenientbanner.ConvenientBanner

/**
 * Created by zrb on 2017/7/17.
 */
interface GuideContract{
    interface View: XView {
        fun showNext(b:Boolean)
    }
    interface Presenter: XPresenter<View> {
        fun initIvBanner(iv_Banner: ConvenientBanner<Int>)
    }
}