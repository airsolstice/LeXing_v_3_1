package com.hs.lxv3_1.navi;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.navisdk.adapter.BNOuterTTSPlayerCallback;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BaiduNaviManager;
import com.hs.lxv3_1.R;
import com.hs.lxv3_1.main.LxRouteServer;
import com.hs.lxv3_1.main.LxSearchServer;
import com.hs.lxv3_1.utils.Constant;
import com.hs.lxv3_1.utils.InputMethodSetter;
import com.hs.lxv3_1.utils.SystemBarDecoder;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("ALL")
public class NavigationOptActivity extends Activity implements View.OnClickListener {

    /*Sdcard的路径*/
    private String mSDCardPath = null;

    private AlertDialog mDiag = null;

    private AlertDialog mSearchDialog = null;

    private TextView mAlertText = null;

    private ImageView mAddImg = null;
    /*隐藏经过输入框标志*/
    private boolean isShadowed = false;
    /*经过组件*/
    private LinearLayout mPassView = null;
    /*返回按钮引用*/
    private ImageView mBackImg = null;
    /*搜索结果列表*/
    private ListView mDialogList = null;
    /*导航起点输入框*/
    private TextView mStartPos = null;

    private EditText mInput = null;

    private LinearLayout mSearchBar = null;

    private TextView mSearch = null;
    /*经过点输入框*/
    private TextView mPassPos = null;
    /*目的地输入框*/
    private TextView mEndPos = null;
    /*验证信息*/
    private String authStr = null;
    /*结果数据实体*/
    private List<String> mDatas = new ArrayList<>();

    private ArrayAdapter<String> mAdapter = null;

    private HashMap<String, LatLng> mPosMap = new HashMap<>();

    private HashMap<String, String> mAddrMap = new HashMap<>();

    private LxSearchServer mSearcher = null;

    private LxRouteServer mRouter = null;

    private TextView mCurrentText = null;

    private Button mWalkBtn = null;

    private Button mBikeBtn = null;

    private Button mBusBtn = null;

    private Button mDriveBtn = null;

    private ExpandableListView mRoutLineList = null;
    /*Activity列表*/
    public static List<Activity> activityList = new LinkedList<>();

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case Constant.MSG_SEARCH_RESULT:

                    newSearchDialog();

                    mDatas.clear();
                    mDatas.addAll(msg.getData().getStringArrayList("sugs"));
                    mAdapter =
                            new ArrayAdapter(NavigationOptActivity.this,
                                    android.R.layout.simple_list_item_1,
                                    mDatas);
                    mDialogList.setOnItemClickListener(new ListView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position,
                                                long id) {
                            mCurrentText.setText(mDatas.get(position));

                            SuggestionResult.SuggestionInfo si =
                                    mSearcher.getSugInfos().get(position);

                            if (mCurrentText.getId() == R.id.navi_start) {
                                mPosMap.put("startll", si.pt);
                                mAddrMap.put("startaddr", si.city + si.district);
                                System.out.println("-->>star=" + mAddrMap.get("startaddr"));

                            } else if (mCurrentText.getId() == R.id.navi_end) {

                                mPosMap.put("endll", si.pt);
                                mAddrMap.put("endaddr", si.city + si.district);
                                System.out.println("-->>end=" + mAddrMap.get("endaddr"));

                            } else if (mCurrentText.getId() == R.id.navi_mid) {

                                mPosMap.put("midll", si.pt);
                                mAddrMap.put("midaddr", si.city + si.district);
                                System.out.println("-->>mid=" + mAddrMap.get("midaddr"));

                            }

                            mSearchDialog.dismiss();

                        }
                    });

                    mDialogList.setAdapter(mAdapter);

                    break;


                case Constant.ROUTE_PLAN_RESULT:


                    LxExpandableListAdapter adapter =
                            new LxExpandableListAdapter(NavigationOptActivity.this, mRouter);
                    mRoutLineList.setAdapter(adapter);

                    break;


            }
        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*无标题设置*/
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        activityList.add(this);
        setContentView(R.layout.activity_navigation_opt);

        SystemBarDecoder.setSystemBartint(this, R.color.sys_bar_bg);
        /*初始化控件*/
        initViews();
        /*检查缓存文件是否存在*/
        if (initDirs()) {
            initNavi();
        }

        Bundle b = getIntent().getExtras();
        if (b != null) {
            mPosMap.put("startll", (LatLng) b.getParcelable("startll"));
            mPosMap.put("endll", (LatLng) b.getParcelable("endll"));
            mEndPos.setText(b.getString("endaddr"));
            mAddrMap.put("startaddr", b.getString("startaddr"));
            mAddrMap.put("endaddr", b.getString("endaddr"));
            mAddrMap.put("currentcity", b.getString("currentcity"));
        }


    }

    private void initViews() {

        mBackImg = (ImageView) findViewById(R.id.navi_back);
        mBackImg.setOnClickListener(this);

        mInput = (EditText) findViewById(R.id.navi_input);
        mSearchBar = (LinearLayout) findViewById(R.id.navi_linear);
        mSearch = (TextView) findViewById(R.id.navi_search);
        mSearch.setOnClickListener(this);

        mStartPos = (TextView) findViewById(R.id.navi_start);
        mStartPos.setOnClickListener(this);

        mPassPos = (TextView) findViewById(R.id.navi_mid);
        mPassView = (LinearLayout) findViewById(R.id.navi_pass);
        mAddImg = (ImageView) findViewById(R.id.navi_add);
        mAddImg.setOnClickListener(this);

        mPassPos = (TextView) findViewById(R.id.navi_mid);
        mPassPos.setOnClickListener(this);
        mEndPos = (TextView) findViewById(R.id.navi_end);
        mEndPos.setOnClickListener(this);

        mWalkBtn = (Button) findViewById(R.id.navi_walk);
        mWalkBtn.setOnClickListener(this);
        mBusBtn = (Button) findViewById(R.id.navi_bus);
        mBusBtn.setOnClickListener(this);
        mDriveBtn = (Button) findViewById(R.id.navi_drive);
        mDriveBtn.setOnClickListener(this);
        mBikeBtn = (Button) findViewById(R.id.navi_bike);
        mBikeBtn.setOnClickListener(this);

        mDiag = new AlertDialog.Builder(NavigationOptActivity.this).create();
        mDiag.show();
        mDiag.getWindow().setContentView(R.layout.navi_diag);
        mAlertText = (TextView) mDiag.getWindow().findViewById(R.id.dialog_loading);

        mRoutLineList = (ExpandableListView) findViewById(R.id.navi_opt_route_lines);

    }


    private void newSearchDialog() {

        mSearchDialog = new AlertDialog.Builder(NavigationOptActivity.this).create();
        mSearchDialog.show();
        mSearchDialog.getWindow().setContentView(R.layout.dialog_search);
        mDialogList = (ListView) mSearchDialog.getWindow().findViewById(R.id.dialog_results);

    }


    private void cleanTimeLineColor() {
        mEndPos.setTextColor(Color.WHITE);
        mPassPos.setTextColor(Color.WHITE);
        mStartPos.setTextColor(Color.WHITE);
    }


    private void updateTimeLineColor(int specIndex) {
        switch (specIndex) {
            case 1:
                mCurrentText = mStartPos;
                mStartPos.setTextColor(Color.GRAY);
                mInput.setText(mStartPos.getText());
                mInput.setSelection(mStartPos.length() - 1);
                break;

            case 2:
                mCurrentText = mPassPos;
                mPassPos.setTextColor(Color.GRAY);

                break;


            case 3:
                mCurrentText = mEndPos;
                mEndPos.setTextColor(Color.GRAY);
                mInput.setText(mEndPos.getText());
                mInput.setSelection(mEndPos.length() - 1);
                break;

        }

        if (mSearchBar.getVisibility() == View.GONE) {
            mSearchBar.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.navi_back:
                finish();
                break;

            case R.id.navi_start:
                updateTimeLineColor(1);
                break;

            case R.id.navi_mid:
                updateTimeLineColor(2);

                break;

            case R.id.navi_end:
                updateTimeLineColor(3);
                break;


            case R.id.navi_search:

                if (mSearcher == null) {
                    mSearcher = new LxSearchServer(this, mHandler);
                }
                mSearcher.searchInfoByKeyword(mInput.getText().toString().trim());

                break;


            case R.id.navi_walk:
                updateOptionBarColor(1);
                calculateroutePlan(LxRouteServer.PlanTypeReference.WALKING);
                break;

            case R.id.navi_bus:
                updateOptionBarColor(3);
                calculateroutePlan(LxRouteServer.PlanTypeReference.TRANSIT);
                break;


            case R.id.navi_bike:
                updateOptionBarColor(2);
                calculateroutePlan(LxRouteServer.PlanTypeReference.BIKING);
                break;

            case R.id.navi_drive:
                updateOptionBarColor(4);
                calculateroutePlan(LxRouteServer.PlanTypeReference.DRIVING);
                break;


            case R.id.navi_add:
                if (!isShadowed) {
                    mPassView.setVisibility(View.VISIBLE);
                } else {
                    mPassView.setVisibility(View.GONE);
                }
                isShadowed = !isShadowed;

                break;
        }

    }


    private void updateOptionBarColor(int specIndex) {
        cleanOptionBarColor();
        switch (specIndex) {
            case 1:
                mWalkBtn.setTextColor(Color.YELLOW);
                break;

            case 2:
                mBikeBtn.setTextColor(Color.YELLOW);
                break;

            case 3:
                mBusBtn.setTextColor(Color.YELLOW);
                break;

            case 4:
                mDriveBtn.setTextColor(Color.YELLOW);
                break;


        }

    }


    private void cleanOptionBarColor() {
        mWalkBtn.setTextColor(Color.WHITE);
        mBikeBtn.setTextColor(Color.WHITE);
        mBusBtn.setTextColor(Color.WHITE);
        mDriveBtn.setTextColor(Color.WHITE);
    }


    private void initNavi() {
        // BaiduNaviManager.getInstance().setNativeLibraryPath(mSDCardPath +
        // "/BaiduNaviSDK_SO");

        BNOuterTTSPlayerCallback ttsCallback = null;

        BaiduNaviManager
                .getInstance()
                .init(NavigationOptActivity.this, mSDCardPath, Constant.APP_FOLDER_NAME,
                        new BaiduNaviManager.NaviInitListener() {
                            @Override
                            public void onAuthResult(int status, String msg) {
                                if (0 == status) {
                                    authStr = "key验证成功";
                                } else {
                                    authStr = "key验证失败, " + msg;
                                }
                                mAlertText.append("\n" + authStr);
                            }

                            public void initSuccess() {

                                mAlertText.append("\n初始化成功");
                                mDiag.dismiss();
                            }

                            public void initStart() {

                                mAlertText.append("\n初始化开始");

                            }

                            public void initFailed() {
                                mAlertText.append("\n初始化失败");
                                mDiag.dismiss();
                            }

                        }, mTTSCallback);
    }


    private void startDrivingRoute(BNRoutePlanNode.CoordinateType coType) {


        BNRoutePlanNode sNode = null;
        BNRoutePlanNode eNode = null;

        switch (coType) {
            case BD09LL: {
                sNode = new BNRoutePlanNode(mPosMap.get("startll").longitude,
                        mPosMap.get("startll").latitude, mAddrMap.get("startaddr"),
                        null, coType);
                eNode = new BNRoutePlanNode(mPosMap.get("endll").longitude,
                        mPosMap.get("endll").latitude,
                        mAddrMap.get("endaddr"), null, coType);
                break;
            }

            case GCJ02: {
                break;
            }

            case WGS84: {
                break;
            }

            case BD09_MC: {
                break;
            }

            default:
                break;
        }
        if (sNode != null && eNode != null) {
            List<BNRoutePlanNode> list = new ArrayList<BNRoutePlanNode>();
            list.add(sNode);
            list.add(eNode);

            BaiduNaviManager.getInstance().launchNavigator(this, list,
                    BaiduNaviManager.RoutePlanPreference.ROUTE_PLAN_MOD_RECOMMEND, true,
                    new LxDrivingRoutePlanListener(sNode));

        }
    }

    private void calculateroutePlan(int planType) {


        if (mRouter == null) {
            mRouter = new LxRouteServer(mHandler);
        }

        PlanNode sNode = PlanNode.withLocation(mPosMap.get("startll"));
        PlanNode eNode = PlanNode.withLocation(mPosMap.get("endll"));

        if (sNode == null || eNode == null) {
            return;
        }

        switch (planType) {
            case LxRouteServer.PlanTypeReference.BIKING:
                mRouter.calculatePlan(LxRouteServer.PlanTypeReference.BIKING, sNode, eNode);
                break;

            case LxRouteServer.PlanTypeReference.DRIVING:
                startDrivingRoute(BNRoutePlanNode.CoordinateType.BD09LL);
                break;

            case LxRouteServer.PlanTypeReference.WALKING:
                mRouter.calculatePlan(LxRouteServer.PlanTypeReference.WALKING, sNode, eNode);
                break;

            case LxRouteServer.PlanTypeReference.TRANSIT:
                mRouter.setCurrentCity(mAddrMap.get("currentcity"));
                mRouter.calculatePlan(LxRouteServer.PlanTypeReference.TRANSIT, sNode, eNode);
                break;

        }

    }


    public class LxDrivingRoutePlanListener implements BaiduNaviManager.RoutePlanListener {

        private BNRoutePlanNode mBNRoutePlanNode = null;

        public LxDrivingRoutePlanListener(BNRoutePlanNode node) {
            mBNRoutePlanNode = node;
        }

        @Override
        public void onJumpToNavigator() {

            for (Activity ac : activityList) {
                if (ac.getClass().getName().endsWith("DrivingNavigationActivity")) {
                    return;
                }
            }
            Intent intent = new Intent(NavigationOptActivity.this,
                    DrivingNavigationActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constant.ROUTE_PLAN_NODE, mBNRoutePlanNode);
            intent.putExtras(bundle);
            startActivity(intent);

        }

        @Override
        public void onRoutePlanFailed() {

            Toast.makeText(NavigationOptActivity.this, "地址解析失败",
                    Toast.LENGTH_SHORT).show();
        }
    }


    private BNOuterTTSPlayerCallback mTTSCallback = new BNOuterTTSPlayerCallback() {


        @Override
        public void stopTTS() {


            Log.e("test_TTS", "-->>stopTTS");
        }

        @Override
        public void resumeTTS() {
            Log.e("test_TTS", "-->>resumeTTS");
        }

        @Override
        public void releaseTTSPlayer() {
            Log.e("test_TTS", "-->>releaseTTSPlayer");
        }

        @Override
        public int playTTSText(String speech, int bPreempt) {
            Log.e("test_TTS", "-->>playTTSText" + "_" + speech + "_" + bPreempt);

            return 1;
        }

        @Override
        public void phoneHangUp() {
            Log.e("test_TTS", "-->>phoneHangUp");
        }

        @Override
        public void phoneCalling() {
            Log.e("test_TTS", "-->>phoneCalling");
        }

        @Override
        public void pauseTTS() {
            Log.e("test_TTS", "-->>pauseTTS");
        }

        @Override
        public void initTTSPlayer() {
            Log.e("test_TTS", "-->>initTTSPlayer");
        }

        @Override
        public int getTTSState() {
            Log.e("test_TTS", "-->>getTTSState");
            return 1;
        }
    };

    private boolean initDirs() {
        mSDCardPath = getSdcardDir();
        if (mSDCardPath == null) {
            return false;
        }
        File f = new File(mSDCardPath, Constant.APP_FOLDER_NAME);
        if (!f.exists()) {
            try {
                f.mkdir();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    private String getSdcardDir() {
        if (Environment.getExternalStorageState().equalsIgnoreCase(
                Environment.MEDIA_MOUNTED)) {
            return Environment.getExternalStorageDirectory().toString();
        }
        return null;
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View view = getCurrentFocus();
            InputMethodSetter.hideInputMethod(this, view, ev);
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);

    }


    @Override
    protected void onStop() {
        super.onStop();
        System.out.println("-->>onStop");
    }


    @Override
    protected void onPause() {
        super.onPause();
        System.out.println("-->>onPause");
    }


    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("-->>onResume");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("-->>onDestroy");
    }


}






