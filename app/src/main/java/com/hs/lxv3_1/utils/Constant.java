package com.hs.lxv3_1.utils;

/**
 * Created by Holy-Spirit on 2016/2/26.
 */
public class Constant {

   /* 定位数据库属性*/

    public static final int VERSION = 1;

    public static final String DB_NAME = "LeXing_DB";

    public static final String POS_TAB = "pos_tab";

    public static final String POS_ID = "id";

    public static final String POS_ADDR = "addr";

    public static final String POS_TIME = "time";

    public static final String POS_LAT = "latitude";

    public static final String POS_LNG = "longitude";


    /*欢迎动画的播放时间*/
    public static final int ANIM_DURATION = 6000;
    /*AlphaAnimation动画的开始透明度参数*/
    public static final float ALPHA_START = 0.0f;
    /*AlphaAnimation动画的结束透明度参数*/
    public static final float ALPHA_END = 1.0f;

    /*百度坐标系类型*/
    public static final String COORTYPE_BD09LL = "bd09ll";
    public static final String COORTYPE_GCJ02 = "bd09ll";
    public static final String COORTYPE_WGS84 = "bd09ll";
    public static final String COORTYPE_BD09_MC = "bd09ll";
    /*百度定位更新频率*/
    public static final int MAP_SCAN_SPAN = 1 * 60 * 1000;
    /*百度导航缓存文件名*/
    public static final String APP_FOLDER_NAME = "LeXing_Navigation";
    /*百度导航偏好模式*/
    public static final String ROUTE_PLAN_NODE = "routePlanNode";
    public static final String SHOW_CUSTOM_ITEM = "showCustomItem";
    public static final String RESET_END_NODE = "resetEndNode";
    public static final String VOID_MODE = "voidMode";
    public static final String TAG_MAIN = "MainActivity";


    public static final String SP_NAME = "LeXing_SP";
    public static final String SP_ID = "id";
    public static final String SP_PWORD = "pword";
    public static final String SP_LOGIN_STATE = "state";


    public static final int MSG_GROUP_INFO = 1;
    public static final int MSG_SEARCH_RESULT = 2;
    public static final int MSG_CONV_LL_TO_DISPLAY = 3;
    public static final int ROUTE_PLAN_RESULT = 4;
    public static final int MSG_CONV_LL_TO_INFOWIN = 5;

    public static final String GROUP_INFO_KEY = "group_info";
    public static final String SUG_SEARCH_KEY = "suggestion_info_result";


}
