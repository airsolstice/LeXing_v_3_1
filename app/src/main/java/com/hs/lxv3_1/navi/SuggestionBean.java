package com.hs.lxv3_1.navi;

import com.baidu.mapapi.model.LatLng;

/**
 * Created by Holy-Spirit on 2016/3/2.
 */
public class SuggestionBean {

    private String headStr;
    private String detailStr;
    /*图标资源id*/
    private int resId ;
    private LatLng mLatLng;

    public void setLatLng(LatLng ll) {
        this.mLatLng = ll;
    }

    public LatLng getLatLng() {

        return mLatLng;
    }

    public void setHeadStr(String str){
        this.headStr = str;
    }

    public String getHeadStr(){
        return this.headStr;
    }

    public void setDetailStr(String str){
        this.detailStr = str;
    }

    public String getDetailStr(){
        return this.detailStr;
    }

    public void setResId(int resId){
        this.resId = resId;
    }

    public int getResId(){
        return resId;
    }

}
