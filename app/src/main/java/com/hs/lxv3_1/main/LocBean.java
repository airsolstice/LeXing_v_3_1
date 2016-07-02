package com.hs.lxv3_1.main;

/**
 * Created by Holy-Spirit on 2016/2/27.
 */
public class LocBean {

    private int id;
    private String addr;
    private String time;
    private double lat;
    private double lng;

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLng() {

        return lng;
    }

    public void setId(int id){
        this.id = id;
    }

    public int getId(){
        return id;
    }

    public void setAddr(String addr){
        this.addr = addr;
    }

    public String getAddr(){
        return addr;
    }


    public void setTime(String time ){
        this.time = time;
    }

    public String getTime(){
        return  time;
    }
}
