package com.hs.lxv3_1.navi;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;

import com.baidu.mapapi.model.LatLng;
import com.baidu.navisdk.adapter.BNRouteGuideManager;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.hs.lxv3_1.R;
import com.hs.lxv3_1.utils.Constant;

import java.util.ArrayList;
import java.util.List;

public class DrivingNavigationActivity extends Activity {

    private BNRoutePlanNode mBNRoutePlanNode = null;

    private ServiceConnection mConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            UpdatePositionService.MyBinder binder = (UpdatePositionService.MyBinder) service;

            if (binder.getHandler() == null) {
                binder.bindHandler(mHandler);
            }
            binder.run();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            System.out.println("-->>update service is error!");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        NavigationOptActivity.activityList.add(this);
        createHandler();
        View view = BNRouteGuideManager
                .getInstance().onCreate(this, new BNRouteGuideManager.OnNavigationListener() {

                    @Override
                    public void onNaviGuideEnd() {
                        finish();
                    }

                    @Override
                    public void notifyOtherAction(int actionType, int arg1, int arg2, Object obj) {
                        Log.e("BNDemoGuideActivity_notifyOtherAction",
                                "actionType:" + actionType + "arg1:" + arg1 + "arg2:" + arg2 +
                                        "obj:" +
                                        obj.toString());
                    }

                });


        if (view != null) {
            setContentView(view);
        }

        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                mBNRoutePlanNode = (BNRoutePlanNode) bundle
                        .getSerializable(Constant.ROUTE_PLAN_NODE);

            }
        }
        Intent intent1 = new Intent(DrivingNavigationActivity.this, UpdatePositionService.class);
        bindService(intent1, mConn, Context.BIND_AUTO_CREATE);


    }

    @Override
    protected void onResume() {
        BNRouteGuideManager.getInstance().onResume();
        super.onResume();
        if (mHandler != null) {
            mHandler.sendEmptyMessageAtTime(MSG_SHOW, 2000);
        }
    }

    protected void onPause() {
        super.onPause();
        BNRouteGuideManager.getInstance().onPause();
    }

    @Override
    protected void onDestroy() {
        BNRouteGuideManager.getInstance().onDestroy();
        NavigationOptActivity.activityList.remove(this);
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        BNRouteGuideManager.getInstance().onStop();
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        BNRouteGuideManager.getInstance().onBackPressed(false);
    }

    public void onConfigurationChanged(android.content.res.Configuration newConfig) {
        BNRouteGuideManager.getInstance().onConfigurationChanged(newConfig);
        super.onConfigurationChanged(newConfig);
    }

    ;

    private void addCustomizedLayerItems() {
        List<BNRouteGuideManager.CustomizedLayerItem>
                items = new ArrayList<>();
        BNRouteGuideManager.CustomizedLayerItem item1 = null;
        if (mBNRoutePlanNode != null) {
            item1 = new BNRouteGuideManager.CustomizedLayerItem(mBNRoutePlanNode.getLongitude(),
                    mBNRoutePlanNode.getLatitude(),
                    mBNRoutePlanNode.getCoordinateType(),
                    getResources().getDrawable(R.drawable.loc_mark),
                    BNRouteGuideManager.CustomizedLayerItem.ALIGN_CENTER);
            items.add(item1);

            BNRouteGuideManager.getInstance().setCustomizedLayerItems(items);
        }
        BNRouteGuideManager.getInstance().showCustomizedLayer(true);
    }

    public static final int MSG_SHOW = 1;
    public static final int MSG_HIDE = 2;
    public static final int MSG_RESET_NODE = 3;
    private Handler mHandler = null;

    private void createHandler() {
        if (mHandler == null) {
            mHandler = new Handler(getMainLooper()) {
                public void handleMessage(android.os.Message msg) {
                    if (msg.what == MSG_SHOW) {
                        addCustomizedLayerItems();
                    } else if (msg.what == MSG_HIDE) {
                        BNRouteGuideManager.getInstance().showCustomizedLayer(false);
                    } else if (msg.what == MSG_RESET_NODE) {

                        Bundle b = msg.getData();
                        LatLng ll = b.getParcelable("latlng");
                        BNRouteGuideManager.getInstance().resetEndNodeInNavi(
                                new BNRoutePlanNode(ll.longitude, ll.latitude, "end", null,
                                        BNRoutePlanNode.CoordinateType.BD09LL));
                    }
                }


            };
        }
    }
}
