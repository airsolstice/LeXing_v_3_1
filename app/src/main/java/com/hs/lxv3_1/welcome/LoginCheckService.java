package com.hs.lxv3_1.welcome;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.hs.lxv3_1.utils.Constant;
import com.hs.lxv3_1.utils.SharedPreHelper;
@SuppressWarnings("ALL")
public class LoginCheckService extends Service {


    public MyBinder mBinder;

    @Override
    public void onCreate() {
        super.onCreate();
        mBinder = new MyBinder();

    }


    @Override
    public IBinder onBind(Intent intent) {

        return mBinder;
    }


    class MyBinder extends Binder {


        /**
         *
         * @return
         */
        public LoginCheckService getService() {
            return LoginCheckService.this;
        }

        public void run() {

            new Thread() {

                @Override
                public void run() {

                    SharedPreHelper helper =
                            new SharedPreHelper(LoginCheckService.this, Constant.SP_NAME);
                    helper.put(Constant.SP_PWORD,"123");
                    helper.put(Constant.SP_ID,"TripBySharing");
                    String id = helper.getString(Constant.SP_ID);
                    String pwd = helper.getString(Constant.SP_PWORD);

                    if ("TripBySharing".equals(id) && "123".equals(pwd)) {
                        helper.put(Constant.SP_LOGIN_STATE, true);
                    }else{
                        helper.put(Constant.SP_LOGIN_STATE, false);
                    }

                }
            }.start();
        }


    }


}
