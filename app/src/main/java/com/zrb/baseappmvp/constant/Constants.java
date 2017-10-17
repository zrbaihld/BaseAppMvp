package com.zrb.baseappmvp.constant;

import android.os.Environment;

/**
 * Created by Anthony on 2016/4/28.
 * Class Note:
 * constants class in the project
 */
public class Constants {
    public static final String HTTP_PREFIX = "http://";
    public static final String HTTPS_PREFIX = "https://";

    /**
     * Activity和Fragment类型文件
     */
    public static final String BASE_TYPE_FRAGMENT_MAP_PATH = "raw://type_fragment_map";

    public static final String BASE_TYPE_ACTIVITY_MAP_PATH = "raw://type_activity_map";

    public static final String Remote_BASE_END_POINT = "http://www.baidu.com";//url for testing
    public static final String Remote_BASE_END_POINT_GITHUB = "https://api.github.com/";//url for testing
    public static final String LOCAL_FILE_BASE_END_POINT = "raw://";

    public static final String CURRENT_USER = "current_user";


    public static final int CURRENT_FONT_SIZE_SMALL = 1;
    public static final int CURRENT_FONT_SIZE_MEDIUM = 2;
    public static final int CURRENT_FONT_SIZE_LARGE = 3;


    public static final String TRS_FONT_MAP_PATH = "trs_font_map";

    //Baidu api
    public static final String REMOTE_BASE_END_POINT_WEATHER = "http://apis.baidu.com/";

    public static String DOWNLOAD_STORE_FOLDER = Environment.getExternalStorageDirectory().getAbsolutePath()
            + "/Download/";

    public static final String WEIXIN_KEY = "3d88f5fb6e5a1fbf2921dbb643f25894";


    public static final String SHARED_NAME = "jiaxiaoshu_shared";
    public static final String USERID = "user_id";
    public static final String USERROLE = "user_role";
    public static final String SESSIONID = "session_id";
    public static final String HOSTVIEWIMG = "host_view_img";
    public static final String SCHOOLID = "school_id";
    public static final String PARENTPAYUNREAD = "parent_pay_unread";
    public static final String STUDENTID = "stu_id";
    public static final String USERHEADPIC = "user_head_pic_change";
    public static final String USERNAME = "user_name_change";
    public static final String USERSWITCH = "user_switch";
    public static final String RONGYUNTOKEN = "user_rongyun_token";
    public static final String CHILDLISTLASTTIME = "child_list_last_time";
    public static final String USERFIRSTSCHOOLE = "USER_FIRST_SCHOOLE";

    public static final String OSSHEADPICUPPATH = "app/app_head/";
    public static final String OSSHBANNERPICUPPATH = "app/app_school/app_school_banner/";
    public static final String OSSHAUDITPICUPPATH = "app/app_school/app_school_audit/";
    public static final String OSSAPPSCHOOLE = "app/app_school/";
    public static final String OSSHGROUPPICUPPATH = "app/app_stu_groupby/";
    public static final String OSSHFOODPICUPPATH = "app/app_food/";
    public static final String OSSHSTUHEADPICUPPATH = "app/app_stu_head/";
    public static final String OSSHAPPSCHOOLEQRCODE = "app/app_school/app_school_qrcode/";


//    public static final String OSSHEADPICBASE = "http://sandboximg.jiaxiaoshu.cn/";


}
