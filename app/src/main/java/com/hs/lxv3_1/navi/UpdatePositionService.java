package com.hs.lxv3_1.navi;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.baidu.mapapi.model.LatLng;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Holy-Spirit on 2016/5/25.
 */
@SuppressWarnings("ALL")
public class UpdatePositionService extends Service {

    private MyBinder mBinder = null;

    private LatLng[] mLatLngs = new LatLng[4];

    @Override
    public void onCreate() {
        super.onCreate();
        mBinder = new MyBinder();

    }


    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }



    public class MyBinder extends Binder {
        Handler mHandler = null;

        public MyBinder(){

        };

        public Handler getHandler(){
            return mHandler;
        }

        public void bindHandler(Handler handler){
            this.mHandler = handler;
        }

        /**
         * @return
         */
        public UpdatePositionService getService() {
            return UpdatePositionService.this;
        }

        public void run() {

            new Thread() {
                @Override
                public void run() {

                    mLatLngs[0] = new LatLng(30.6485370000,104.1024710000);

                    mLatLngs[1] = new LatLng(30.6390910000,104.1197190000);

                    mLatLngs[2] = new LatLng(30.6338710000,104.1309300000);

                    mLatLngs[3] = new LatLng(30.6303910000,104.1401280000);

                    Timer t = new Timer();
                    t.schedule(new TimerTask() {
                        int i = 0;
                        @Override
                        public void run() {
                            System.out.println("-->>udpate");
                            LatLng ll = mLatLngs[i%4];
                            i++;
                            Message msg = new Message();
                            msg.what = DrivingNavigationActivity.MSG_RESET_NODE;
                            Bundle b = new Bundle();
                            b.putParcelable("latlng",ll);
                            msg.setData(b);
                            mHandler.sendMessage(msg);
                        }
                    }, 1000 * 30, 1000 * 30);

                }
            }.start();

        }


    }

}
