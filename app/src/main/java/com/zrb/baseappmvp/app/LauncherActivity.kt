package com.zrb.baseappmvp.app

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import  com.zrb.baseappmvp.app.guide.GuideActivity
import com.zhy.autolayout.AutoLayoutActivity
import com.zrb.baseappmvp.R
import com.zrb.baseappmvp.app.main.MainActivity
import com.zrb.baseappmvp.permiss.RxPermissions
import com.zrb.baseappmvp.tools.SharedPreferencesTools
import io.reactivex.Observable
import org.jetbrains.anko.toast


/**
 * Created by zrb on 2017/7/17.
 */
class LauncherActivity : AutoLayoutActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 不显示系统的标题栏，保证windowBackground和界面activity_main的大小一样，显示在屏幕不会有错位（去掉这一行试试就知道效果了）
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        //申请必要权限
        val pkgManager = packageManager
        val SDWritePermission = pkgManager.checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, packageName) == PackageManager.PERMISSION_GRANTED
        val ReadPhonePermission = pkgManager.checkPermission(Manifest.permission.READ_PHONE_STATE, packageName) == PackageManager.PERMISSION_GRANTED
        val rxPermissions = RxPermissions(this)
        if (Build.VERSION.SDK_INT >= 23 && !SDWritePermission && !ReadPhonePermission) {
            Observable
                    .just("")
                    .compose(rxPermissions.ensureEach(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE))
                    .subscribe({
                        permission ->
                        if (permission.granted) {
                        } else if (permission.shouldShowRequestPermissionRationale) {
                            toast("请打开权限")
                        } else {
                            toast("请打开权限")
                        }
                    })
        } else {
        }
        //判断是否是第一次登录,展示登录还是引导页
        setContentView(R.layout.activity_launcher)
        if (SharedPreferencesTools.getInstance().getBoolean(this, "APP_IS_FIRST_LAUNCHER")) {
            startActivity(Intent().setClass(this, MainActivity::class.java))
        } else {
            startActivity(Intent().setClass(this, GuideActivity::class.java))
        }
        finish()
    }

}