package com.hs.lxv3_1.app;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;

/**
 * 使用自定义的的Application之前必须在
 * manifest.xml文件中更改默认的Application引用过
 */

public class MyApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        /*初始话百度地图SDK*/
        SDKInitializer.initialize(getApplicationContext());
    }

}