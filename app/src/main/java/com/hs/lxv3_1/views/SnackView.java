package com.hs.lxv3_1.views;

import android.support.design.widget.Snackbar;
import android.view.View;

import com.hs.lxv3_1.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Holy-Spirit on 2016/4/21.
 */
@SuppressWarnings("ALL")
public class SnackView {

    public Snackbar mSnack = null;

    public Timer mTimer = null;


    public SnackView(View v) {

        if (mSnack == null) {
            System.out.println("-->null");
            mSnack = Snackbar.make(v, "", Snackbar.LENGTH_LONG);
        } else {
            System.out.println("-->not null");
        }

    }


    public SnackView setText(CharSequence cs) {
        if (mSnack == null) {
            return this;
        }
        mSnack.setText(cs);
        return this;
    }


    public void showDelay(final int duration) {

        if (mSnack == null) {
            return;
        }
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {

                System.out.println("-->>duration=" + duration);
                if (duration <= 0) {
                    return;
                }

                mSnack.show();
                showDelay(duration - 1);
            }
        }, 2000);


    }


    public void show(int duration) {

        if (mSnack == null) {
            return;
        }
        /*先显示一次*/
        mSnack.show();
        if (duration > 0) {
            showDelay(duration);
        }

    }


    public void dismiss() {

        if (mSnack == null || mTimer == null) {
            return;
        }

        mTimer.cancel();
    }

    public SnackView setViewBackGroundColor(int color) {

        if (mSnack == null) {
            return this;
        }
        mSnack.getView().setBackgroundColor(color);

        return this;
    }


    public SnackView setViewBackGround(int resId) {

        if (mSnack == null) {
            return this;
        }
        mSnack.getView().setBackgroundResource(R.drawable.rect_wbg_shadow_top);

        return this;
    }


    public SnackView setActionTextColor(int color) {

        if (mSnack == null) {
            return this;
        }
        mSnack.setActionTextColor(color);
        return this;
    }


    public SnackView setAction(String str, View.OnClickListener listener) {
        if (mSnack == null) {
            return this;
        }
        mSnack.setAction(str, listener);
        return this;
    }


}
