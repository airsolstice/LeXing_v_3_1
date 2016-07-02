package com.hs.lxv3_1.main;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Holy-Spirit on 2016/4/16.
 */
public class GroupBean implements Serializable{

    private int no;
    private int sum;
    private List<MemberBean> list;

    public GroupBean() {
        list = new ArrayList<>();
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public List<MemberBean> getList() {
        return list;
    }

    public void setList(List<MemberBean> list) {
        this.list = list;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }


    public static class MemberBean {

        private String id;
        private String name;
        private double lat;
        private double lng;
        private String img_url;

        public String getImg_url() {
            return img_url;
        }

        public void setImg_url(String img_url) {
            this.img_url = img_url;
        }

        public double getLng() {
            return lng;
        }

        public void setLng(double lng) {
            this.lng = lng;
        }

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        public String getName() {

            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {

            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}
