package  com.zrb.baseappmvp.app.guide

import android.content.Context
import android.content.Intent
import android.support.v4.view.ViewPager
import android.view.View
import android.widget.ImageView
import com.ToxicBakery.viewpager.transforms.AccordionTransformer
import com.zrb.baseappmvp.base.BasePresenter
import com.bigkoo.convenientbanner.ConvenientBanner
import com.bigkoo.convenientbanner.holder.Holder
import com.zrb.baseappmvp.R
import com.zrb.baseappmvp.app.main.MainActivity
import com.zrb.baseappmvp.tools.SharedPreferencesTools

/**
 * Created by zrb on 2017/7/17.
 */
class GuidePresenter : BasePresenter<GuideContract.View>(), GuideContract.Presenter {
    val image_path = arrayListOf(R.drawable.bg_guide_no1, R.drawable.bg_guide_no2, R.drawable.bg_guide_no3,
            R.drawable.bg_guide_no4)
    var iv_Banner: ConvenientBanner<Int>? = null
    var canNext = false
    override fun initIvBanner(iv_Banner: ConvenientBanner<Int>) {
        this.iv_Banner = iv_Banner

        iv_Banner.setPages(
                { LocalImageHolderView() }, image_path)
                //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
                .setPageIndicator(intArrayOf(R.drawable.ic_page_indicator, R.drawable.ic_page_indicator_focused))
                .onPageChangeListener = object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                mView?.showNext((position == 3))
                if (canNext && position == 0) {
                    SharedPreferencesTools.getInstance().putBoolean(mActivity, "APP_IS_FIRST_LAUNCHER", true)
                    mActivity?.startActivity(Intent().setClass(mActivity, MainActivity::class.java))
                    mActivity?.finish()
                }
                canNext = position == image_path.size - 1

            }

        }//监听翻页事件
//                .startTurning(2000)
        iv_Banner.setcurrentitem(0)
        iv_Banner.viewPager.setPageTransformer(true, AccordionTransformer())
//        iv_Banner.viewPager.setPageTransformer(true, FlipHorizontalTransformer())
    }

    private inner class LocalImageHolderView : Holder<Int> {
        override fun UpdateUI(p0: Context?, p1: Int, p2: Int?) {
            if (p2 != null) {
                imageView?.setImageResource(p2)
            }
        }

        var imageView: ImageView? = null
        override fun createView(context: Context): View {
            imageView = ImageView(context)
            imageView!!.scaleType = ImageView.ScaleType.FIT_XY
            return imageView as ImageView
        }


    }
}