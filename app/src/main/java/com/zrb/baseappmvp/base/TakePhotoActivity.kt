package com.zrb.baseappmvp.base

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import com.jph.takephoto.app.TakePhoto
import com.jph.takephoto.app.TakePhotoImpl
import com.jph.takephoto.compress.CompressConfig
import com.jph.takephoto.model.*
import com.jph.takephoto.permission.InvokeListener
import com.jph.takephoto.permission.PermissionManager
import com.jph.takephoto.permission.TakePhotoInvocationHandler
import com.zrb.baseappmvp.R
import com.zrb.baseappmvp.tools.LogUtil
import io.reactivex.Observable
import java.io.File

/**
 * Created by zrb on 2017/6/20.
 */
abstract class TakePhotoActivity<T : XPresenter<*>> : BaseActivity<T>(), TakePhoto.TakeResultListener, InvokeListener {

    private var takePhoto: TakePhoto? = null
    private var invokeParam: InvokeParam? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        getTakePhoto()?.onCreate(savedInstanceState)
        super.onCreate(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        getTakePhoto()?.onSaveInstanceState(outState)
        super.onSaveInstanceState(outState)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        getTakePhoto()?.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val type = PermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults)
        PermissionManager.handlePermissionsResult(this, type, invokeParam, this)
    }

    /**
     * 获取TakePhoto实例
     * @return
     */
    fun getTakePhoto(): TakePhoto? {
        if (takePhoto == null) {
            takePhoto = TakePhotoInvocationHandler.of(this).bind(TakePhotoImpl(this, this)) as TakePhoto
        }
        return takePhoto
    }

    override fun takeSuccess(result: TResult) {
    }

    override fun takeFail(result: TResult, msg: String) {
        LogUtil.e("takeFail:" + msg)
    }

    override fun takeCancel() {
        LogUtil.e(resources.getString(R.string.msg_operation_canceled))
    }

    override fun invoke(invokeParam: InvokeParam): PermissionManager.TPermissionType {
        val type = PermissionManager.checkPermission(TContextWrap.of(this), invokeParam.method)
        if (PermissionManager.TPermissionType.WAIT == type) {
            this.invokeParam = invokeParam
        }
        return type
    }

    fun btnClickSelect(type: Int) {
        getCammerPermission(type, 0,0)
    }

    fun btnClickMultipleSelect(type: Int,limit: Int) {
        getCammerPermission(type, 3,limit)
    }

    fun btnClickOblongSelect(type: Int) {
        getCammerPermission(type, 1,0)
    }

    fun selectPic(type: Int, height: Int, width: Int, iscrop: Boolean, limit: Int) {
        var file = File(Environment.getExternalStorageDirectory(), "/temp/" + System.currentTimeMillis() + ".jpg")
        if (!file.parentFile.exists()) file.parentFile.mkdirs()
        var imageUri = Uri.fromFile(file)
        //裁切配置
        val withWonCrop = true
        val crop_builder = CropOptions.Builder()
        crop_builder.setAspectX(width).setAspectY(height)
        crop_builder.setWithOwnCrop(withWonCrop)
        var Crop = crop_builder.create()
        //压缩配置
        val config = CompressConfig.Builder()
                .setMaxSize(1024000)
                .setMaxPixel(800)
                .enableReserveRaw(false)
                .create()
        getTakePhoto()?.onEnableCompress(config, true)
        //选择照片配置

        var builder = TakePhotoOptions.Builder()
        builder.setWithOwnGallery(true)
        getTakePhoto()?.setTakePhotoOptions(builder.create())
        LogUtil.e("getTakePhoto()?.onPickMultiple(10)   $limit")

        when (type) {
            0 -> {
                if (iscrop)
                    getTakePhoto()?.onPickFromCaptureWithCrop(imageUri, Crop)
                else
                    getTakePhoto()?.onPickFromCapture(imageUri)
            }
            1 -> {
                if (iscrop)
                    getTakePhoto()?.onPickFromGalleryWithCrop(imageUri, Crop)
                else {
                    if (limit == 0) {
                        getTakePhoto()?.onPickFromGallery()
                    } else {
                        getTakePhoto()?.onPickMultiple(limit)
                    }
                }

            }
        }
    }

    fun toSelectPic(type: Int, type2: Int,limit: Int) {
        when (type2) {
            0 -> selectPic(type, 800, 800, false, 0)
            1 -> selectPic(type, 600, 900, true, 0)
            3 -> selectPic(type, 600, 900, false, limit)
        }
    }

    fun getCammerPermission(type: Int, type2: Int,limit: Int) {
        val pkgManager = packageManager
        // 读写 sd card 权限非常重要, android6.0默认禁止的, 建议初始化之前就弹窗让用户赋予该权限
        when (type) {
            0 -> {
                toSelectPic(type, type2,limit)
            }
            1 -> {
                toSelectPic(type, type2,limit)
            }
        }
    }

}