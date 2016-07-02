package com.hs.lxv3_1.login;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hs.lxv3_1.R;
import com.hs.lxv3_1.utils.Constant;
import com.hs.lxv3_1.utils.InputMethodSetter;
import com.hs.lxv3_1.utils.SharedPreHelper;
import com.hs.lxv3_1.utils.SystemBarDecoder;
import com.hs.lxv3_1.views.CustomProgressBar;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class LoginActivity extends Activity {

    private static int REF_TIME = 200;

    private Button mLoginBtn = null;

    private TextView mFrogetTextView = null;

    private TextView mRegistTextView = null;

    private ProgressBar mProgressBar = null;

    private CustomProgressBar mCustomBar = null;

    private boolean isStop = false;

    private EditText mAccountEdit = null;

    private EditText mPwdEdit = null;


    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case 0x123:

                    if (!isStop) {
                        mCustomBar.refreshView();
                        this.sendEmptyMessageDelayed(0x123, REF_TIME);
                    }
                    break;

                default:
                    break;
            }

        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_login);

        SystemBarDecoder.setSystemBartint(this, R.color.sys_bar_bg);


        mLoginBtn = (Button) this.findViewById(R.id.login_log_btn);
        mProgressBar = (ProgressBar) this.findViewById(R.id.login_loging_pbar);

        mAccountEdit = (EditText) findViewById(R.id.login_count_view);

        int[] resIds = new int[]{R.dimen.settings_left, R.dimen.settings_top,
                R.dimen.settings_right, R.dimen.settings_bottom};
        int[] dimensions = getDimensions(resIds);
        mAccountEdit.setCompoundDrawables(initDrawable(dimensions, R.drawable.account), null, null, null);
        mPwdEdit = (EditText) findViewById(R.id.login_pwd_view);
        mPwdEdit.setCompoundDrawables(initDrawable(dimensions, R.drawable.pwd), null, null, null);
        mLoginBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                System.out.println("-->>activity_login..");
                stopCustomProgress();
                mLoginBtn.setClickable(false);
                mLoginBtn
                        .setTextColor(getResources().getColor(R.color.text_bg));
                mProgressBar.setVisibility(View.VISIBLE);

                SharedPreHelper sph = new SharedPreHelper(LoginActivity.this, Constant.SP_NAME);

                if ("".equals(mAccountEdit.getText().toString().trim()) ||
                        "".equals(mPwdEdit.getText().toString().trim())) {
                    Toast.makeText(LoginActivity.this, "用户名和密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                sph.put(Constant.SP_ID, mAccountEdit.getText().toString());
                sph.put(Constant.SP_PWORD, mPwdEdit.getText().toString());
                sph.put(Constant.SP_LOGIN_STATE,true);

                finish();
            }
        });

        mFrogetTextView = (TextView) this.findViewById(R.id.login_forget_txt);
       /* 设置下划线*/
        // mFrogetTextView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        mFrogetTextView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                System.out.println("-->>forgot!");
            }
        });

        mRegistTextView = (TextView) this.findViewById(R.id.login_regist_txt);
       /* 设置下划线*/
        //mRegistTextView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        mRegistTextView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                System.out.println("-->>regist...");
            }
        });

        mCustomBar = (CustomProgressBar) this.findViewById(R.id.custom_bar);
        mHandler.sendEmptyMessageDelayed(0x123, REF_TIME);
    }


    private Drawable initDrawable(int[] dimensions, int resId) {
        Drawable d = this.getResources().getDrawable(resId);
        d.setBounds(dimensions[0], dimensions[1], dimensions[2], dimensions[3]);
        return d;
    }

    private int[] getDimensions(int[] resId) {
        int[] res = new int[resId.length];

        for (int i = 0; i < res.length; i++) {
            res[i] = this.getResources().getDimensionPixelOffset(resId[i]);
        }

        return res;
    }


    private void stopCustomProgress() {
        mCustomBar.setVisibility(View.GONE);
        isStop = true;
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View view = getCurrentFocus();
            InputMethodSetter.hideInputMethod(this, view, ev);
            return super.dispatchTouchEvent(ev);
        }
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);

    }


}
