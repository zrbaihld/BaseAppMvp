package com.zrb.baseappmvp.network.download;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.zrb.baseappmvp.network.RxManager;
import com.zrb.baseappmvp.R;
import com.zrb.baseappmvp.tools.FileUtil;
import com.zrb.baseappmvp.tools.LogUtil;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Anthony on 2016/6/12.
 * Class Note:
 * Download Service to load {@link NotificationManager}
 */
public class DownloadService extends Service {
    public static String DOWNLOAD_URL = "DOWNLOAD_URL";
    private static String ACTION = "DOWNLOAD_ACTION";
    private static int ACTION_CANCEL = 1;
    private NotificationManager mNotificationManager;
    private HashMap<String, DownloadTask> mTaskMap = new HashMap<>();
    RxManager rxManager = new RxManager();

    @Override
    public void onCreate() {
        super.onCreate();
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        rxManager.getMRxBus()
                .toObserverable(DownloadEvent.class)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<DownloadEvent>() {
                    @Override
                    public void accept(DownloadEvent downloadEvent) throws Exception {
                        if (downloadEvent.total != -1) {
                            float percent = (float) downloadEvent.progress / (float) downloadEvent.total * 100;
                            //整数值未变没就不更新通知栏，否则通知栏更新太频繁会导致卡顿
                            if (downloadEvent.task.current_percent != (int) percent) {
                                downloadEvent.task.current_percent = (int) percent;
                                downloadEvent.task.mNotification.contentView
                                        .setProgressBar(R.id.progressbar_download, 100, (int) percent, false);

                                if (mNotificationManager != null) {
                                    mNotificationManager.notify(downloadEvent.task.id, downloadEvent.task.mNotification);
                                }
                            }
                        } else {
                            if (!downloadEvent.task.isUnknownLength) {
                                downloadEvent.task.isUnknownLength = true;
                                downloadEvent.task.mNotification.contentView
                                        .setViewVisibility(R.id.progressbar_download, View.INVISIBLE);
                                downloadEvent.task.mNotification.contentView
                                        .setViewVisibility(R.id.progressbar_download_unknown, View.VISIBLE);
                                if (mNotificationManager != null) {
                                    mNotificationManager.notify(downloadEvent.task.id, downloadEvent.task.mNotification);
                                }
                            }
                        }
                    }

                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                });
        rxManager.getMRxBus()
                .toObserverable(DownloadFinishEvent.class)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<DownloadFinishEvent>() {
                    @Override
                    public void accept(DownloadFinishEvent downloadFinishEvent) throws Exception {
                        if (mNotificationManager != null) {
                            mNotificationManager.cancel(downloadFinishEvent.task.id);
                            mNotificationManager = null;
                        }
                        if (!downloadFinishEvent.isSuccess) {
                            Toast.makeText(getApplicationContext(), "下载失败", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "下载成功", Toast.LENGTH_SHORT).show();

                            Intent i = new Intent(Intent.ACTION_VIEW);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            i.setDataAndType(Uri.parse("file://" + downloadFinishEvent.task.storePath), "application/vnd.android.package-archive");
                            startActivity(i);


                        }
                        mTaskMap.remove(downloadFinishEvent.task.mUrl);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                        LogUtil.e("DownloadService  Throwable   " + throwable.getMessage());
                    }
                });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int action = intent.getIntExtra(ACTION, 0);

        if (action == ACTION_CANCEL) {
            String url = intent.getStringExtra(DOWNLOAD_URL);
            DownloadTask task = mTaskMap.get(url);

            if (mNotificationManager != null) {
                mNotificationManager.cancel(task.id);
            }
            task.cancel();
        } else {
            String url = intent.getStringExtra(DOWNLOAD_URL);

            if (!TextUtils.isEmpty(url)) {
//                if (mTaskMap.containsKey(url)) {
//                    Toast.makeText(getApplicationContext(), "正在下载中...", Toast.LENGTH_SHORT).show();
//                } else {
                DownloadTask task = new DownloadTask(mTaskMap.size(), url, getApplicationContext());
                mTaskMap.put(url, task);
                task.start();
                createNotification(task);
//                }
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
            DownloadTask task = (DownloadTask) entry.getValue();
            task.cancel();
        }
    }

    private void createNotification(DownloadTask task) {
        RemoteViews remoteViews = new RemoteViews(this.getPackageName(),
                R.layout.lib_layout_download_notification);
        remoteViews.setProgressBar(R.id.progressbar_download, 100, 0, false);
        remoteViews.setTextViewText(R.id.tv_title, "正在下载" + FileUtil.getUrlFileName(task.mUrl));

        Intent cancelIntent = new Intent(this, DownloadService.class);
        cancelIntent.putExtra(ACTION, ACTION_CANCEL);
        cancelIntent.putExtra(DOWNLOAD_URL, task.mUrl);
        PendingIntent intent = PendingIntent.getService(this, task.id, cancelIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.layout_btn_cancel, intent);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setContent(remoteViews)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(false) //将AutoCancel设为true后，当你点击通知栏的notification后，它会自动被取消消失
                .setPriority(NotificationCompat.PRIORITY_MAX) //从Android4.1开始，可以设置notification的优先级，优先级越高的，通知排的越靠前，优先级低的，不会在手机最顶部的状态栏显示图标
                .setOngoing(true) //将Ongoing设为true 那么notification将不能滑动删除
                .setTicker("下载中，请稍等...");

        task.mNotification = builder.build();
        mNotificationManager.notify(task.id, task.mNotification);
    }
}
