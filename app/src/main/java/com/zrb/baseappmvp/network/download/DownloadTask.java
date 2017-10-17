package com.zrb.baseappmvp.network.download;

import android.app.Notification;
import android.content.Context;

import com.zrb.baseappmvp.network.RxManager;
import com.zrb.baseappmvp.constant.Constants;
import com.zrb.baseappmvp.network.HttpHelper;
import com.zrb.baseappmvp.tools.FileUtil;
import com.zrb.baseappmvp.tools.LogUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * Created by Anthony on 2016/6/12.
 * Class Note:
 */
public class DownloadTask implements Serializable {
    private Context mContext;
    public int id;
    public String mUrl;
    public String storePath;
    public Disposable mSubscription;
    public Notification mNotification;
    public int current_percent = 0;
    public boolean isUnknownLength = false;

    HttpHelper httpHelper;
    RxManager rxManager = new RxManager();

    public DownloadTask(int id, String mUrl, Context context) {
        this.id = id;
        this.mUrl = mUrl;
        this.mContext = context;
    }

    public void start() {
        httpHelper = new HttpHelper(mContext);
        LogUtil.e("DownloadApi  " + mUrl);
        mSubscription = httpHelper.getApi(DownloadApi.class)
                .downloadFile(mUrl)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Exception {
                        LogUtil.e("responseBody  " + responseBody.toString());
                        boolean result = writeResponseBodyToDisk(responseBody, FileUtil.getUrlFileName(mUrl));
                        LogUtil.e("result  " + result);
                        rxManager.getMRxBus().post(new DownloadFinishEvent(DownloadTask.this, result));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                        LogUtil.e("Throwable  ");
                        rxManager.getMRxBus().post(new DownloadFinishEvent(DownloadTask.this, false));
                    }
                });
    }

    public void cancel() {
        if (mSubscription != null && !mSubscription.isDisposed()) {
            mSubscription.dispose();
        }
    }

    private boolean writeResponseBodyToDisk(ResponseBody body, String fileName) {
        try {
            LogUtil.e("Constants.DOWNLOAD_STORE_FOLDER    "+ Constants.DOWNLOAD_STORE_FOLDER);
            String store_path = Constants.DOWNLOAD_STORE_FOLDER;
            storePath=store_path + fileName;
            File futureStudioIconFile = new File(storePath);

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                    rxManager.getMRxBus().post(new DownloadEvent(mUrl, fileSize, fileSizeDownloaded, this));
//                    LogUtil.e("file download: " + fileSizeDownloaded + " of " + fileSize);
                }

                outputStream.flush();
                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }
}
