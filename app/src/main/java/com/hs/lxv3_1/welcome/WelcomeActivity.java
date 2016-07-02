package com.hs.lxv3_1.welcome;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.hs.lxv3_1.R;
import com.hs.lxv3_1.main.MainActivity;
import com.hs.lxv3_1.utils.BitmapDecoder;
import com.hs.lxv3_1.utils.Constant;
import com.hs.lxv3_1.utils.SystemBarDecoder;


public class WelcomeActivity extends Activity {


    /* LOGO引用*/
    private ImageView mLogoImg = null;
    private int  sWidth = 0;
    private int  sHeight = 0;


    private ServiceConnection  mConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LoginCheckService.MyBinder binder = (LoginCheckService.MyBinder) service;
            binder.run();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

            System.out.println("-->>run error!");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* 无标题栏设置*/
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_welcome);
       /* 设置系统栏和app色调自适应*/
        SystemBarDecoder.setSystemBartint(this, R.color.sys_bar_bg);
        autoLogin();

        DisplayMetrics dm = getResources().getDisplayMetrics();

        sWidth = dm.widthPixels;
        sHeight = dm.heightPixels;

        initAnima();

    }


    private void autoLogin() {
        Intent intent = new Intent(WelcomeActivity.this, LoginCheckService.class);
        bindService(intent, mConn, Context.BIND_AUTO_CREATE);
    }

    private void initAnima() {
        mLogoImg = (ImageView) this.findViewById(R.id.welcome_logo_img);
        /* 设置透明度动画*/
        AlphaAnimation anima = new AlphaAnimation(Constant.ALPHA_START, Constant.ALPHA_END);
        /* 设置动画时间*/
        anima.setDuration(Constant.ANIM_DURATION);
        /* 开启动画*/
        mLogoImg.startAnimation(anima);
        /*动画完成时的接口监听事件*/
        anima.setAnimationListener(new LogoAnimation());

    }


    /**
     * 透明度动画监听接口
     */
    private class LogoAnimation implements Animation.AnimationListener {

        private Bitmap bm;

        @Override
        public void onAnimationStart(Animation animation) {

            if(sWidth == 0 || sHeight == 0){
                return;
            }

            bm = BitmapDecoder.decodeBitmapFromRaw(WelcomeActivity.this,R.raw.welcome,sWidth,sHeight);
            mLogoImg.setImageBitmap(bm);
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            unbindService(mConn);
            leapToOtherActivity(bm);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }

    }

    /**
     * 启动下一个Activity
     * 关闭这个Activity
     */

    private void leapToOtherActivity(Bitmap bm) {

        if(bm != null){
            bm.recycle();
        }

        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}

