package com.hs.lxv3_1.main;

import android.os.Handler;

import com.baidu.mapapi.search.route.BikingRoutePlanOption;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRoutePlanOption;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.hs.lxv3_1.utils.Constant;

/**
 * Created by Holy-Spirit on 2016/5/17.
 */
public class LxRouteServer {

    private RoutePlanSearch mRouteSearcher = null;

    private String mCurrentCity = null;

    private Handler mHandler = null;

    private WalkingRouteResult mWalkRet = null;

    private TransitRouteResult mTransitRet = null;

    private DrivingRouteResult mDrivingRet = null;

    private BikingRouteResult mBikingRet = null;

    public LxRouteServer(Handler handler) {
        this.mHandler = handler;
    }

    public interface PlanTypeReference {
        int DRIVING = 1;
        int WALKING = 2;
        int TRANSIT = 3;
        int BIKING = 4;
    }



    public void calculatePlan(int planType, PlanNode sNode, PlanNode eNode) {

        if (mRouteSearcher == null) {
            mRouteSearcher = RoutePlanSearch.newInstance();
            mRouteSearcher.setOnGetRoutePlanResultListener(mRouteListener);
        }

        switch (planType) {
            case PlanTypeReference.DRIVING:
                System.out.println("-->>d");
                mRouteSearcher.drivingSearch((new DrivingRoutePlanOption()).from(sNode).to(eNode));
                break;

            case PlanTypeReference.WALKING:
                System.out.println("-->>w");
                mRouteSearcher.walkingSearch((new WalkingRoutePlanOption()).from(sNode).to(eNode));
                break;

            case PlanTypeReference.TRANSIT:
                if (mCurrentCity == null||mCurrentCity.isEmpty()) {
                    System.out.println("-->>null");
                 return;
                }
                System.out.println("-->>t="+mCurrentCity);
                mRouteSearcher.transitSearch((new TransitRoutePlanOption())
                        .from(sNode).city(mCurrentCity).to(eNode));
                break;

            case PlanTypeReference.BIKING:
                System.out.println("-->>b");
                mRouteSearcher.bikingSearch((new BikingRoutePlanOption()).from(sNode).to(eNode));
                break;

        }
    }

    public void setCurrentCity(String city){
      mCurrentCity = city;
    }

    public WalkingRouteResult getWalkResult() {
        return mWalkRet;
    }

    public TransitRouteResult getTransitResult() {
        return mTransitRet;
    }

    public DrivingRouteResult getDrivingResult(){
        return mDrivingRet;
    }

    public BikingRouteResult getBikingResult(){
        return mBikingRet;
    }

    private OnGetRoutePlanResultListener mRouteListener = new OnGetRoutePlanResultListener() {
        @Override
        public void onGetWalkingRouteResult(WalkingRouteResult wResult) {
            mWalkRet = wResult;
            mHandler.sendEmptyMessage(Constant.ROUTE_PLAN_RESULT);

        }

        @Override
        public void onGetTransitRouteResult(TransitRouteResult tResult) {
            mTransitRet = tResult;
            mHandler.sendEmptyMessage(Constant.ROUTE_PLAN_RESULT);

        }

        @Override
        public void onGetDrivingRouteResult(DrivingRouteResult dResult) {

            mDrivingRet = dResult;
            mHandler.sendEmptyMessage(Constant.ROUTE_PLAN_RESULT);
        }

        @Override
        public void onGetBikingRouteResult(BikingRouteResult bResult) {
            mBikingRet = bResult;
            mHandler.sendEmptyMessage(Constant.ROUTE_PLAN_RESULT);

        }
    };


    public void onDestroy() {

        if (mRouteSearcher != null) {
            mRouteSearcher.destroy();
        }

    }
}
