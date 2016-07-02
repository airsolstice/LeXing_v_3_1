package com.hs.lxv3_1.main;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Button;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hs.lxv3_1.R;
import com.hs.lxv3_1.utils.Constant;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;

/**
 * Created by Holy-Spirit on 2016/4/16.
 */
public class LoadingGroupInfoAsync extends AsyncTask<Integer, Integer, GroupBean> {


    private Context mContext;
    private Button mButton;
    private Handler mCallBack;

    public LoadingGroupInfoAsync(Context context, Button btn, Handler callBack) {
        this.mContext = context;
        this.mButton = btn;
        this.mCallBack = callBack;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mButton.setClickable(false);

    }


    @Override
    protected GroupBean doInBackground(Integer... params) {

        int srcId;

        if (params[0] == 0) {
            srcId = R.raw.json_group_one;
        } else if(params[0] == 1){

            srcId = R.raw.json_group_two;
        }else {
            return null;
        }

        InputStream inputStream = mContext.getResources().openRawResource(srcId);
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        StringBuffer content = new StringBuffer();
        String len;
        GroupBean group = null;

        try {
            while ((len = br.readLine()) != null) {
                content.append(len);
            }
            String jsonData = content.toString();
            Type type = new TypeToken<GroupBean>() {
            }.getType();
            Gson gson = new Gson();
            group = gson.fromJson(jsonData, type);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return group;
    }


    @Override
    protected void onPostExecute(GroupBean group) {
        super.onPostExecute(group);

        mButton.setClickable(true);
        Message msg = new Message();
        msg.what = Constant.MSG_GROUP_INFO;
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.GROUP_INFO_KEY, group);
        msg.setData(bundle);
        mCallBack.sendMessage(msg);
    }
}
