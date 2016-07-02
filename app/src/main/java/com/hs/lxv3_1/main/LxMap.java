package com.hs.lxv3_1.main;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.hs.lxv3_1.R;
import com.hs.lxv3_1.utils.BitmapDecoder;
import com.hs.lxv3_1.utils.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Holy-Spirit on 2016/4/14.
 */
public class LxMap implements BDLocationListener, BaiduMap.OnMapLongClickListener,
        BaiduMap.OnMarkerDragListener {

    /*UI线程的上下文*/
    private Context mContext = null;

    /*百度地图图层引用*/
    private MapView mMapView = null;

    /*百度地图操作对象引用*/
    private BaiduMap mBaiduMap = null;

    /*百度地图定位引用*/
    private LocationClient mClient = null;

    /*百度地图图层自定义覆物处理引用*/
    private BitmapDescriptor mLocDescriptor = null;

    /*百度地图第一次加载标志，默认为true*/
    private boolean isFirstLoc = true;

    /*UI设置引用*/
    private UiSettings mSettings = null;

    /*目标覆盖物位图对象*/
    private BitmapDescriptor mTargetDescriptor = null;

    /*目标点经纬度对象*/
    private LatLng mPointLL = null;

    private SuggestionResult.SuggestionInfo mInfo;

    /*上一个目标覆盖物对象*/
    private Marker mPreMarker;

    /*群Bitmap存储回收容器*/
    private List<BitmapDescriptor> mBitmapsCollector = new ArrayList<>();

    /*群覆盖物存储回收容器*/
    private List<Marker> mMarkersCollector = new ArrayList<>();

    private InfoWindow mInfoWindow;

    public LxMap(Context context, MapView mapView) {
        this.mContext = context;
        mMapView = mapView;
        initMapSettings();
        initLocationService();
    }

    public void initMapSettings() {
        /*获得百度地图*/
        mBaiduMap = getBaiduMap();
        /*开启百度定位*/
        mBaiduMap.setMyLocationEnabled(true);
        /*隐藏缩放按钮*/
        shadowZoom();
        /*隐藏LOGO*/
        shadowLogo();
        /*获得设置对象*/
        mSettings = mBaiduMap.getUiSettings();
        /*设置缩放手势*/
        mSettings.setZoomGesturesEnabled(true);
        /*设置指南针*/
        mSettings.setCompassEnabled(false);
        /*设置俯视手势*/
        mSettings.setOverlookingGesturesEnabled(true);
        /*设置旋转手势*/
        mSettings.setRotateGesturesEnabled(true);
        /*设置平滑手势*/
        mSettings.setScrollGesturesEnabled(true);
        /*设置覆盖物监听器*/
        mBaiduMap.setOnMarkerDragListener(this);
        /*设置长按地图监听*/
        mBaiduMap.setOnMapLongClickListener(this);
        /*创建目标覆盖物位图对象*/
        createTargetDescriptor();
    }

    public void initLocationService() {
        /*获取定位使用服务*/
        mClient = new LocationClient(mContext);
        /*注册定位监听接口*/
        mClient.registerLocationListener(this);
        /*定位参数配置*/
        LocationClientOption mOption = new LocationClientOption();
        /*开启Gps*/
        mOption.setOpenGps(true);
        /*设置定位经度模式*/
        mOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        /*设置地图坐标系类型*/
        mOption.setCoorType(Constant.COORTYPE_BD09LL);
        /*设置定位刷新频率,大于1000有效*/
        mOption.setScanSpan(Constant.MAP_SCAN_SPAN);
        /*获取地址*/
        mOption.setIsNeedAddress(true);
        /*设置定位提醒*/
        mOption.setLocationNotify(true);
        /*设置需要设备方向*/
        mOption.setNeedDeviceDirect(true);
        /*设置超时限制*/
        mOption.setTimeOut(10 * 1000);
        /*可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”*/
        mOption.setIsNeedLocationDescribe(true);
        /*可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到*/
        mOption.setIsNeedLocationPoiList(true);
        /*设置进程结束*/
        mOption.setIgnoreKillProcess(true);
        /*绑定设置*/
        mClient.setLocOption(mOption);
        /*开始定位*/
        mClient.start();

    }


    public void addGroupOverlay(GroupBean gb, final LxSearchServer searcher) {

        for (GroupBean.MemberBean mb : gb.getList()) {
            String url = mb.getImg_url();
            if ("".equals(url)) {
                Bitmap b = BitmapDecoder
                        .decodeBitmapByResource(mContext.getResources(), R.drawable.head_marker,
                                20, 20);
                b = BitmapDecoder.makeRoundCorner(b);

                BitmapDescriptor bd =
                        BitmapDescriptorFactory.fromBitmap(b);
                LatLng ll = new LatLng(mb.getLat(), mb.getLng());
                MarkerOptions mo =
                        new MarkerOptions().position(ll).icon(bd).zIndex(9)
                                .draggable(true);
                Marker m = addOverlay(mo);
                mMarkersCollector.add(m);
                mBitmapsCollector.add(bd);
            }

        }

        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker) {
                searcher.convertLatLngToAddr(marker.getPosition());
                return true;
            }
        });
    }

    public void showInfoWindow(LatLng ll,View v){
        mInfoWindow = new InfoWindow(v, ll, -47);
        mBaiduMap.showInfoWindow(mInfoWindow);
    }

    public void hideInfoWindow(){
        mBaiduMap.hideInfoWindow();
    }

    public void createTargetDescriptor() {

        Bitmap b = BitmapDecoder.decodeBitmapByResource(mContext.getResources(),
                R.drawable.mark, 20, 20);
        mTargetDescriptor =
                BitmapDescriptorFactory.fromBitmap(b);
    }


    public void moveToTarget(SuggestionResult.SuggestionInfo info) {

        mInfo = info;
        if (info.pt == null || info == null || "".equals(info.key)) {
            Toast.makeText(mContext, "地址解析失败", Toast.LENGTH_LONG).show();
            return;
        }
        LatLng ll = info.pt;
        mPointLL = ll;
        setLatLngZoom(ll, 15);

        if (mTargetDescriptor != null) {

            if (mPreMarker != null) {
                mPreMarker.remove();
            }
            MarkerOptions options =
                    new MarkerOptions().position(ll).icon(mTargetDescriptor);
            mPreMarker = (Marker) mBaiduMap.addOverlay(options);
        }
    }

    public void onDestroy() {


        if (mClient != null) {
            mClient.unRegisterLocationListener(this);
            mClient.stop();
        }

    }

    /**
     * @return BaiduMap对象
     */
    public BaiduMap getBaiduMap() {
        return mMapView.getMap();
    }


    /**
     * 设置地图手势缩放
     *
     * @param isScaled
     */
    public void setScaleEnable(boolean isScaled) {
        mSettings.setZoomGesturesEnabled(isScaled);
    }

    /**
     * 添加覆盖物
     *
     * @param options
     * @return 覆盖物对象
     */
    public Marker addOverlay(MarkerOptions options) {
        return (Marker) mBaiduMap.addOverlay(options);
    }

    /**
     * 清除地图上的覆盖物
     */
    public void cleanOverlay() {
        mBaiduMap.clear();
    }

    /**
     * 返回使用者的位置
     */
    public void backLocationPoint() {

        /* 设置返回到使用者的位置*/
        MyLocationConfiguration.LocationMode mode = MyLocationConfiguration.LocationMode.FOLLOWING;
        mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
                mode, true, mLocDescriptor));
    }


    public LatLng getOverlaylatLng() {
        return mPointLL;
    }

    @Override
    public void onMarkerDrag(Marker marker) {
        //托覆盖物时的监听
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        mPointLL = marker.getPosition();
        mSettings.setAllGesturesEnabled(true);
    }

    @Override
    public void onMarkerDragStart(Marker marker) {
        mPreMarker = marker;
        mSettings.setAllGesturesEnabled(false);
    }

    @Override
    public void onMapLongClick(LatLng latLng) {

        mPointLL = latLng;
        if (mPreMarker != null) {
            mPreMarker.remove();
        }

        if (mTargetDescriptor != null) {

            MarkerOptions options =
                    new MarkerOptions().icon(mTargetDescriptor).draggable(true).position(latLng);
            mPreMarker = addOverlay(options);
        }
    }

    @Override
    public void onReceiveLocation(BDLocation mLocation) {

        String addrStr = mLocation.getAddrStr();
        String timeStr = mLocation.getTime();

        if (mMapView == null || mLocation == null) {
            return;
        }

        /* 保存定位数据到数据库*/
        DataBaseHelper.insert(mContext, addrStr, timeStr, mLocation.getLatitude(),
                mLocation.getLongitude());

        /* 本地位置的设置 */
        MyLocationData mData = new MyLocationData.Builder()
                .accuracy(mLocation.getRadius()).direction(0)
                .latitude(mLocation.getLatitude())
                .longitude(mLocation.getLongitude()).build();
        mBaiduMap.setMyLocationData(mData);

        /*设置自定义覆盖物*/
        Bitmap b = BitmapDecoder
                .decodeBitmapByResource(mContext.getResources(), R.drawable.user,
                        20, 20);
        b = BitmapDecoder.makeRoundCorner(b);

        mLocDescriptor =
                BitmapDescriptorFactory.fromBitmap(b);

        mBaiduMap
                .setMyLocationConfigeration(new MyLocationConfiguration(
                        MyLocationConfiguration.LocationMode.COMPASS,
                        true, mLocDescriptor));


        /* 从默认的天安门转移到你的位置 */
        if (isFirstLoc) {
            isFirstLoc = false;
            LatLng ll = new LatLng(mLocation.getLatitude(),
                    mLocation.getLongitude());
            setLatLngZoom(ll, 15);
        }

    }

    public void setLatLngZoom(LatLng ll, int scale) {
        MapStatusUpdate mUpdate = MapStatusUpdateFactory.newLatLngZoom(
                ll, scale);
        mBaiduMap.animateMapStatus(mUpdate);
    }


    /**
     * 隐藏百度Logo
     */
    public void shadowLogo() {
        mMapView.removeViewAt(1);
    }

    /**
     * 隐藏缩放按钮
     */
    public void shadowZoom() {
        mMapView.showZoomControls(false);

    }

    /**
     * 隐藏缩放按钮
     */
    public void shadowZoom(int flag) {

        int childCount = mMapView.getChildCount();
        View zoom = null;
        for (int i = 0; i < childCount; i++) {
            View child = mMapView.getChildAt(i);
            if (child instanceof ZoomControls) {
                zoom = child;
                break;
            }
        }
        zoom.setVisibility(flag);

    }

    /**
     * 隐藏比例尺按钮
     */

    public void shadowScaleControl() {
        mMapView.showScaleControl(false);
    }

    /**
     * 隐藏指南针
     */
    public void shadowCompass() {
        mBaiduMap.getUiSettings().setCompassEnabled(false);
    }


}
