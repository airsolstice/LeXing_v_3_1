package com.hs.lxv3_1.login;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.hs.lxv3_1.R;
import com.hs.lxv3_1.utils.Constant;
import com.hs.lxv3_1.utils.SharedPreHelper;

public class UserInfoActivity extends Activity {

    private Button mUnlogin = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        mUnlogin = (Button) findViewById(R.id.unlogin);

        mUnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreHelper sph = new SharedPreHelper(UserInfoActivity.this, Constant.SP_NAME);
                sph.put(Constant.SP_LOGIN_STATE,false);
                finish();
            }
        });

    }


}
