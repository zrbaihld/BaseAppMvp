package com.zrb.baseappmvp.tools;

import android.content.Context;
import android.content.SharedPreferences;

import com.zrb.baseappmvp.base.AppManager;
import com.zrb.baseappmvp.constant.Constants;


/**
 * Created by zrb on 2017/6/20.
 */

public class SharedPreferencesTools {
    private SharedPreferences sharedPreferences;

    private static SharedPreferencesTools sharedPreferencesTools;
    private static int school = -1;
    private static int student_id = -1;
    private static String host_view_img = "";

    public static SharedPreferencesTools getInstance() {
        if (sharedPreferencesTools == null) {
            synchronized (SharedPreferencesTools.class) {
                if (sharedPreferencesTools == null) {
                    sharedPreferencesTools = new SharedPreferencesTools();
                }
            }
        }
        return sharedPreferencesTools;
    }


    private SharedPreferences getSharePreferences(Context context) {
        if (sharedPreferences == null)
            sharedPreferences = context.getSharedPreferences(Constants.SHARED_NAME, Context.MODE_PRIVATE);
        return sharedPreferences;
    }

    public void putString(Context context, String name, String value) {
        getSharePreferences(context).edit().putString(name, value).apply();
    }

    public void putBoolean(Context context, String name, Boolean value) {
        getSharePreferences(context).edit().putBoolean(name, value).apply();
    }

    public boolean getBoolean(Context context, String name, Boolean value) {
        return getSharePreferences(context).getBoolean(name, value);
    }

    public boolean getBoolean(Context context, String name) {
        return getSharePreferences(context).getBoolean(name, false);
    }

    public void putString(String name, String value) {
        getSharePreferences(AppManager.getApplication()).edit().putString(name, value).apply();
    }


    public String getString(Context context, String name) {
        return getSharePreferences(context).getString(name, "");
    }

    public void putLong(String name, Long value) {
        getSharePreferences(AppManager.getApplication()).edit().putLong(name, value).apply();
    }


    public Long getLong(Context context, String name) {
        return getSharePreferences(context).getLong(name, 0);
    }

    public String getString(String name) {
        return getSharePreferences(AppManager.getApplication()).getString(name, "");
    }


    public void putInt(Context context, String name, int value) {
        getSharePreferences(context).edit().putInt(name, value).apply();
    }

    public void putInt(String name, int value) {
        getSharePreferences(AppManager.getApplication()).edit().putInt(name, value).apply();
    }


    public int getInt(Context context, String name) {
        return getSharePreferences(context).getInt(name, 0);
    }

    public int getInt(String name) {
        return getSharePreferences(AppManager.getApplication()).getInt(name, 0);
    }


    public int getSchool_id() {
        if (school == -1) {
            school = getSharePreferences(AppManager.getApplication()).getInt(Constants.SCHOOLID, -1);
        }
        return school;
    }

    public void setSchool_id(int School_id) {
        school = School_id;
        putInt(Constants.SCHOOLID, school);
    }

    public int getStudent_id() {
        if (student_id == -1) {
            student_id = getSharePreferences(AppManager.getApplication()).getInt(Constants.STUDENTID, -1);
        }
        return student_id;
    }

    public void setStudent_id(int stu_id) {
        student_id = stu_id;
        putInt(Constants.STUDENTID, student_id);
    }

    public String getHost_view_img() {
        if (host_view_img.isEmpty()) {
            host_view_img = getSharePreferences(AppManager.getApplication()).getString(Constants.HOSTVIEWIMG, "");
        }
        return host_view_img;
    }

    public void setHost_view_img(String img) {
        putString(Constants.HOSTVIEWIMG, img);
        host_view_img = img;
    }

}
