package com.zrb.baseappmvp.network.upload;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RemoteViews;

import com.zrb.baseappmvp.network.RxManager;
import com.zrb.baseappmvp.R;
import com.zrb.baseappmvp.network.event.UploadEvent;
import com.zrb.baseappmvp.network.event.UploadEventInternal;
import com.zrb.baseappmvp.network.event.UploadFinishEvent;
import com.zrb.baseappmvp.tools.ToastUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Anthony on 2016/7/8.
 * Class Note:
 */
public class UploadService extends Service {
    public static String UPLOAD_URL = "UPLOAD_URL";
    public static String UPLOAD_FILES = "UPLOAD_FILES";
    public static String UPLOAD_PARAMS = "UPLOAD_PARAMS";
    private static String ACTION = "UPLOAD_ACTION";
    private static String UPLOAD_CANCEL_ID = "UPLOAD_CANCEL_ID";
    private static int ACTION_CANCEL = 1;
    private NotificationManager mNotificationManager;
    private HashMap<Integer, UploadTask> mTaskMap = new HashMap<>();
    RxManager rxManager=new RxManager();

    ToastUtils toastUtils;

    @Override
    public void onCreate() {
        super.onCreate();
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        rxManager.getMRxBus().toObserverable(UploadEventInternal.class)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<UploadEventInternal>() {
                    @Override
                    public void accept(UploadEventInternal uploadEvent) throws Exception {
                        uploadEvent.task.progress += uploadEvent.byteCount;
                        float percent = (float) uploadEvent.task.progress / (float) uploadEvent.task.total * 100;
                        if (uploadEvent.task.current_percent != (int) percent) {
                            uploadEvent.task.current_percent = (int) percent;
                            uploadEvent.task.mNotification.contentView
                                    .setProgressBar(R.id.progressbar_upload, 100, (int) percent, false);

                            if (mNotificationManager != null) {
                                mNotificationManager.notify(uploadEvent.task.id, uploadEvent.task.mNotification);
                            }
                            Log.v("FileUpload", "Process: " + String.valueOf((int) percent));
                             rxManager.getMRxBus().post(new UploadEvent(uploadEvent.task.total,
                                    uploadEvent.task.progress, uploadEvent.task));

                            if ((int) percent == 100) {
                                 rxManager.getMRxBus().post(new UploadFinishEvent(uploadEvent.task, true));
                            }
                    }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                });

         Disposable 上传失败 =  rxManager.getMRxBus().toObserverable(UploadFinishEvent.class)
                .subscribeOn(Schedulers.io())
                 .observeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
//                .observeOn(AndroidSchedulers.mainThread(),false,100)
                .subscribe(new Consumer<UploadFinishEvent>() {
                    @Override
                    public void accept(UploadFinishEvent uploadFinishEvent) throws Exception {
                        if (mNotificationManager != null) {
                            mNotificationManager.cancel(uploadFinishEvent.task.id);
                        }

                        if (!uploadFinishEvent.isSuccess) {
                            toastUtils.showToast("上传失败");
                        }

                        mTaskMap.remove(uploadFinishEvent.task.id);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int action = intent.getIntExtra(ACTION, 0);

        if (action == ACTION_CANCEL) {
            int id = intent.getIntExtra(UPLOAD_CANCEL_ID, -1);
            UploadTask task = mTaskMap.get(id);
            if (task != null) {
                if (mNotificationManager != null) {
                    mNotificationManager.cancel(task.id);
                }
                task.cancel();
            }
        } else {
            String url = intent.getStringExtra(UPLOAD_URL);
            List<UploadParam> fileList = intent.getParcelableArrayListExtra(UPLOAD_FILES);
            List<UploadParam> paramList = intent.getParcelableArrayListExtra(UPLOAD_PARAMS);

            if (!TextUtils.isEmpty(url) && fileList != null && fileList.size() > 0) {
                long total = 0;

                HashMap<String, File> fileMap = new HashMap<>();
                for (UploadParam param : fileList) {
                    File file = new File(param.value);
                    total += file.length();
                    fileMap.put(param.key, file);
                }

                HashMap<String, String> paramMap = new HashMap<>();
                if (paramList != null) {
                    for (UploadParam param : paramList) {
                        paramMap.put(param.key, param.value);
                    }
                }

                UploadTask task = new UploadTask(mTaskMap.size(), url, fileMap, paramMap);
                task.total = total;
                mTaskMap.put(mTaskMap.size(), task);
                task.start();
                createNotification(task);
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        mNotificationManager = null;
        cancelAll();
        super.onDestroy();
    }

    private void cancelAll() {
        Iterator iterator = mTaskMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            UploadTask task = (UploadTask) entry.getValue();
            task.cancel();
        }
    }

    private void createNotification(UploadTask task) {
        RemoteViews remoteViews = new RemoteViews(this.getPackageName(),
                R.layout.lib_layout_upload_notification);
        remoteViews.setProgressBar(R.id.progressbar_upload, 100, 0, false);
        remoteViews.setTextViewText(R.id.tv_title, "正在上传" + task.mFileMap.size() + "个文件");

        Intent cancelIntent = new Intent(this, UploadService.class);
        cancelIntent.putExtra(ACTION, ACTION_CANCEL);
        cancelIntent.putExtra(UPLOAD_CANCEL_ID, task.id);
        PendingIntent intent = PendingIntent.getService(this, task.id, cancelIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.layout_btn_cancel, intent);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setContent(remoteViews)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(false) //将AutoCancel设为true后，当你点击通知栏的notification后，它会自动被取消消失
                .setPriority(NotificationCompat.PRIORITY_MAX) //从Android4.1开始，可以设置notification的优先级，优先级越高的，通知排的越靠前，优先级低的，不会在手机最顶部的状态栏显示图标
                .setOngoing(true) //将Ongoing设为true 那么notification将不能滑动删除
                .setTicker("上传中，请稍等...");

        task.mNotification = builder.build();
        mNotificationManager.notify(task.id, task.mNotification);
    }
}
