package com.zrb.baseappmvp.base;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import com.zhy.autolayout.config.AutoLayoutConifg;
import com.zrb.baseappmvp.db.DaoMaster;
import com.zrb.baseappmvp.db.DaoSession;

import org.greenrobot.greendao.database.Database;


/**
 * Created by zrb on 2017/6/19.
 */

public class MyApp extends Application {
    boolean ENCRYPTED = false;
    //
    private DaoSession daoSession;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        AutoLayoutConifg.getInstance().useDeviceSize();
        AppManager.setApplication(this);
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, ENCRYPTED ? "users-db-encrypted" : "users-db");
        Database db = helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();

    }
    public DaoSession getDaoSession() {
        return daoSession;
    }

    public static String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();
        @SuppressLint("WrongConstant") ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }
}
