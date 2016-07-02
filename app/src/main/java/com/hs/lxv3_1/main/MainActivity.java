package com.hs.lxv3_1.main;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.text.Editable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewStub;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.hs.lxv3_1.R;
import com.hs.lxv3_1.login.LoginActivity;
import com.hs.lxv3_1.navi.NavigationOptActivity;
import com.hs.lxv3_1.utils.Constant;
import com.hs.lxv3_1.utils.InputMethodSetter;
import com.hs.lxv3_1.utils.SystemBarDecoder;
import com.hs.lxv3_1.views.DisplayWindow;
import com.hs.lxv3_1.views.OptionsWindow;
import com.hs.lxv3_1.views.SettingsWindow;
import com.hs.lxv3_1.views.SnackView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@SuppressWarnings("ALL")
public class MainActivity extends Activity {


    /*地图控件*/
    private MapView mMapView = null;

    /*乐行地图对象*/
    private LxMap mLxMap = null;

    /*菜单按钮*/
    private FloatingActionButton mNaviBtn = null;

    /*切换群组按钮*/
    private Button mSwtBtn = null;

    /*我的位置按钮*/
    private Button mPosBtn = null;

    /*菜单按钮*/
    private ImageView mOptImg = null;

    /*搜索输入框*/
    private EditText mInputEdit = null;

    /*语音搜索按钮*/
    private ImageView mVoiceImg = null;

    /*自定义底部滑出菜单*/
    private OptionsWindow mOptWindow = null;

    /*群组切换指针*/
    private int mIndex = 0;

    /*Snackbar容器*/
    private CoordinatorLayout mSnackContainer = null;

    /*结果列表*/
    private ListView mSugListView = null;

    /*结果列表的数据对象*/
    private List<String> mDatas = new ArrayList<>();

    /*结果列表适配器*/
    private ArrayAdapter<String> mAdapter = null;

    /*延迟加载对象*/
    private ViewStub mStub = null;

    /*延迟加载的布局对象*/
    private View mStubView = null;

    /*第一次加载标志*/
    private boolean isFirInflated = true;

    /*加载布局的隐藏标志*/
    public boolean isGone = true;

    /*搜索服务对象*/
    private LxSearchServer mSearcher = null;

    /**
     * 跨线程之间的消息传递回调接口
     */
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                //将获取的群组列表显示出来
                case Constant.MSG_GROUP_INFO:
                    GroupBean gb =
                            (GroupBean) msg.getData().getSerializable(Constant.GROUP_INFO_KEY);
                    if (gb == null) {
                        break;
                    }
                    mLxMap.addGroupOverlay(gb, mSearcher);

                    break;

                //将搜索结果显示出来
                case Constant.MSG_SEARCH_RESULT:
                    if (isFirInflated) {
                        mStubView = mStub.inflate();
                        mSugListView = (ListView) mStubView.findViewById(R.id.result_list);
                        isFirInflated = !isFirInflated;
                    }

                    if (isGone) {
                        mStubView.setVisibility(View.VISIBLE);
                        mSugListView.setVisibility(View.VISIBLE);
                    }

                    mDatas.clear();
                    mDatas.addAll(msg.getData().getStringArrayList(Constant.SUG_SEARCH_KEY));
                    mAdapter =
                            new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1,
                                    mDatas);
                    mSugListView.setOnItemClickListener(mItemListener);
                    mSugListView.setAdapter(mAdapter);
                    break;

                //经纬度转化
                case Constant.MSG_CONV_LL_TO_DISPLAY:
                    DisplayWindow dw =
                            new DisplayWindow(MainActivity.this, mSearcher.getReverseResult(),
                                    isGone);
                    dw.showAtLocation(
                            MainActivity.this.findViewById(R.id.window_container),
                            Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                    break;


                case Constant.MSG_CONV_LL_TO_INFOWIN:
                    final ReverseGeoCodeResult result = mSearcher.getReverseResult();
                    View v = LayoutInflater.from(MainActivity.this)
                            .inflate(R.layout.layout_info, null);

                    TextView tv1 = (TextView) v.findViewById(R.id.info_addr);
                    ReverseGeoCodeResult.AddressComponent ac = result.getAddressDetail();
                    tv1.setText(ac.province + ac.city + ac.district + ac.street);
                    TextView tv2 = (TextView) v.findViewById(R.id.info_go_here);
                    tv2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(MainActivity.this, "GO HERE NOW", Toast.LENGTH_SHORT)
                                    .show();
                            Intent i = new Intent(MainActivity.this, NavigationOptActivity.class);
                            Bundle b = new Bundle();
                            LocBean lb = DataBaseHelper.queryLast(MainActivity.this);
                            LatLng startll = new LatLng(lb.getLat(), lb.getLng());
                            b.putString("startaddr", lb.getAddr());
                            b.putParcelable("startll", startll);
                            b.putString("currentcity", result.getAddressDetail().city);
                            b.putString("endaddr", result.getAddress());
                            LatLng endll = result.getLocation();
                            b.putParcelable("endll", endll);
                            i.putExtras(b);
                            startActivity(i);

                        }
                    });
                    mLxMap.showInfoWindow(result.getLocation(), v);

                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            MainActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mLxMap.hideInfoWindow();
                                }
                            });
                        }
                    }, 3 * 1000);

                    break;
            }
        }
    };

    /**
     * 结果列表选中监听器
     */
    private ListView.OnItemClickListener mItemListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mLxMap.moveToTarget(mSearcher.getSugInfos().get(position));
        }
    };

    /**
     * 搜索栏监听器
     */

    private SearchBarListener mSearchListener = new SearchBarListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                /*功能菜单显示*/
                case R.id.lx_search_bar_opt:

                    mOptWindow = new OptionsWindow(MainActivity.this, mOptPopListener);
                    mOptWindow
                            .showAtLocation(MainActivity.this.findViewById(R.id.window_container),
                                    Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                    break;

                /*语音模块入口处理逻辑*/
                case R.id.lx_search_bar_voice:

                    if (mStubView != null && mSugListView != null) {
                        mInputEdit.setText("");
                        mInputEdit.setHint("请输入目的地");
                        mStubView.setVisibility(View.GONE);
                        mSugListView.setVisibility(View.GONE);
                    }
                    //                    SnackView sv = new SnackView(mSnackContainer);
                    //                    sv.setText("语音模块").setViewBackGround(Color.WHITE);
                    //                    sv.show(0);
                    break;
            }

        }

        /**
         * 文本内容改变后回调业务处理
         * @param s
         */
        @Override
        public void afterTextChanged(Editable s) {

            if ("".equals(s.toString())) {
                mInputEdit.setCursorVisible(false);
                mInputEdit.setHint("请输入目的地");
                mSugListView.setVisibility(View.GONE);
                mStubView.setVisibility(View.GONE);

                isGone = true;
            } else {
                mInputEdit.setCursorVisible(true);
                mSearcher.searchInfoByKeyword(s.toString());
            }
        }

        /**
         * 文本内容改变前回调业务处理
         * @param s
         * @param start
         * @param count
         * @param after
         */
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        /**
         * 文本内容改变时回业务处理
         * @param s
         * @param start
         * @param before
         * @param count
         */
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }


    };


    /**
     * Popup菜单监听器
     */
    private View.OnClickListener mOptPopListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                /*设置页面*/
                case R.id.pop_settings:
                    mOptWindow.dismiss();
                    SettingsWindow sw = new SettingsWindow(MainActivity.this,
                            mSettingsPopListener);
                    sw.showAtLocation(
                            MainActivity.this.findViewById(R.id.window_container),
                            Gravity.BOTTOM, 0, 0);
                    break;
                
                /*导航*/
                case R.id.pop_navigate:
                    startActivity(new Intent(MainActivity.this, NavigationOptActivity.class));
                    break;

                case R.id.pop_personal_center:
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    break;

                /*周边服务*/
                case R.id.pop_surr:
                    System.out.println("-->>surrounding");
                    break;

                /*足迹服务*/
                case R.id.pop_footp:
                    System.out.println("-->>footprint");
                    break;

                /*定制服务*/
                case R.id.pop_book:
                    System.out.println("-->>book");
                    break;
                
                /*群组服务*/
                case R.id.pop_group:
                    System.out.println("-->>group");
                    break;

                /*账本服务*/
                case R.id.pop_account:
                    System.out.println("-->>account");
                    break;
                
                /*偶遇服务*/
                case R.id.pop_meet:
                    System.out.println("-->>meet");
                    break;


            }
        }
    };

    /**
     * Popup设置监听器
     */
    private CompoundButton.OnCheckedChangeListener mSettingsPopListener =
            new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    System.out.println("-->>" + isChecked);
                    mLxMap.setScaleEnable(isChecked);
                }
            };


    /**
     * UI界面控件点击监听器
     *
     * @param v
     */
    private View.OnClickListener mMainListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                /*群组切换*/
                case R.id.switch_btn:
                    mLxMap.cleanOverlay();
                    int i = mIndex % 3;
                    LoadingGroupInfoAsync lgia =
                            new LoadingGroupInfoAsync(MainActivity.this, mSwtBtn, mHandler);
                    lgia.execute(i);
                    new SnackView(mSnackContainer).setText("GROUP " + i)
                            .setViewBackGround(Color.WHITE)
                            .show(0);
                    mIndex++;

                    break;
                
                /*跟随导航服务*/
                case R.id.follow_navi_btn:

                    if (mLxMap.getOverlaylatLng() == null) {
                        new SnackView(mSnackContainer).setText("请长按地图或者搜索目的地，进行标记")
                                .setViewBackGround(Color.WHITE).show(1);
                        return;
                    }

                    if (mSugListView != null) {
                        mSugListView.setVisibility(View.GONE);
                    }

                    mSearcher.convertLatLngToAddrForDisplay(mLxMap.getOverlaylatLng());

                    break;
                /*回到本地位置*/
                case R.id.loc_myself_btn:

                    mLxMap.backLocationPoint();
                    LocBean lb = DataBaseHelper.queryLast(MainActivity.this);
                    String str = "地点：" + lb.getAddr() + "\n时间：" + lb.getTime();

                    View.OnClickListener listener = new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            System.out.println("-->>had shared to others");
                        }
                    };
                    new SnackView(mSnackContainer).setText(str).setViewBackGround(Color.WHITE)
                            .setAction("share", listener)
                            .show(1);

                    break;

            }

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*无标题栏设置*/
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        /*设置关联布局*/
        setContentView(R.layout.activity_main);
        /*系统栏和app色调自适应*/
        SystemBarDecoder.setSystemBartint(this, R.color.sys_bar_bg);
        /*控件初始化*/
        initViews();
        /*初始化地图搜索服务*/
        initLxServer();

    }

    /**
     * 初始化地图服务
     */
    private void initLxServer() {
        mSearcher = new LxSearchServer(this, mHandler);
    }

    /**
     * 初始化界面的各控件
     */
    private void initViews() {
         /*地图初始化*/
        mMapView = (MapView) findViewById(R.id.main_baidu_map);
        mLxMap = new LxMap(MainActivity.this, mMapView);

        mNaviBtn = (FloatingActionButton) findViewById(R.id.follow_navi_btn);
        mNaviBtn.setOnClickListener(mMainListener);
        mSwtBtn = (Button) findViewById(R.id.switch_btn);
        mSwtBtn.setOnClickListener(mMainListener);
        mPosBtn = (Button) findViewById(R.id.loc_myself_btn);
        mPosBtn.setOnClickListener(mMainListener);

        mOptImg = (ImageView) findViewById(R.id.lx_search_bar_opt);
        mOptImg.setOnClickListener(mSearchListener);
        mVoiceImg = (ImageView) findViewById(R.id.lx_search_bar_voice);
        mVoiceImg.setOnClickListener(mSearchListener);
        mInputEdit = (EditText) findViewById(R.id.lx_search_bar_input);
        mInputEdit.addTextChangedListener(mSearchListener);
        mStub = (ViewStub) findViewById(R.id.stub);
        mSnackContainer = (CoordinatorLayout) findViewById(R.id.coordinator);

    }


    /**
     * 实现了点击焦点之外的位置就隐藏输入法
     *
     * @param ev
     * @return
     */
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

    /**
     * 销毁地图
     */

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLxMap.onDestroy();
        mMapView.onDestroy();
    }

    /**
     * 重新运行地图
     */
    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    /**
     * 暂停使用地图
     */
    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();

    }

}