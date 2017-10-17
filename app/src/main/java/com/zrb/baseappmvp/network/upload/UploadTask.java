package com.zrb.baseappmvp.network.upload;

import android.app.Notification;
import android.util.Log;

import com.zrb.baseappmvp.network.RxManager;
import com.zrb.baseappmvp.network.HttpHelper;
import com.zrb.baseappmvp.network.event.UploadFinishEvent;
import com.zrb.baseappmvp.tools.FileTypeUtil;
import com.zrb.baseappmvp.tools.FileUtil;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * Created by Anthony on 2016/7/8.
 * Class Note:
 */
public class UploadTask {
    public int id;
    public String mUrl;
    public Disposable mSubscription;
    public Notification mNotification;
    public HashMap<String, File> mFileMap;
    public HashMap<String, String> mParamMap;
    public long total = 0;
    public long progress = 0;
    public int current_percent = 0;

    RxManager rxManager=new RxManager();

    HttpHelper httpHelper;

    public UploadTask(int id, String mUrl,
                      HashMap<String, File> map, HashMap<String, String> paramMap) {
        this.id = id;
        this.mUrl = mUrl;
        this.mFileMap = map;
        this.mParamMap = paramMap;
    }

    public void start() {
        Map<String, RequestBody> files = new HashMap<>();

        Iterator fileIterator = mFileMap.entrySet().iterator();
        while (fileIterator.hasNext()) {
            Map.Entry entry = (Map.Entry) fileIterator.next();
            String key = (String) entry.getKey();
            File file = (File) entry.getValue();

            RequestBody fileBody = RequestBody.create(MediaType.parse(getContentType(file)), file);
            files.put("" + key + "\"; filename=\"" + FileUtil.getUrlFileName(file.getAbsolutePath()) + "",
                    new UploadRequestBody(fileBody, this));
        }

        Iterator paramIterator = mParamMap.entrySet().iterator();
        while (paramIterator.hasNext()) {
            Map.Entry entry = (Map.Entry) paramIterator.next();
            String key = (String) entry.getKey();
            String val = (String) entry.getValue();

            files.put(key, RequestBody.create(MediaType.parse("text/plain"), val));
        }


        mSubscription = httpHelper.getApi(UploadApi.class)
                .uploadFile(mUrl, files)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Exception {
                            Log.v("FileUpload", responseBody.string());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                        rxManager.getMRxBus().post(new UploadFinishEvent(UploadTask.this, false));
                    }
                })
               ;
    }

    public void cancel() {
        if (mSubscription != null && !mSubscription.isDisposed()) {
            mSubscription.dispose();
        }
    }

    //获取文件的上传类型，图片格式为image/png,image/jpg等。非图片为application/octet-stream
    private String getContentType(File f) {
        String fileType = FileTypeUtil.getFileType(f.getAbsolutePath());

        if (fileType == null || fileType.equals("")) {
            return "application/octet-stream";
        } else {
            return "image/" + fileType;
        }
    }
}
