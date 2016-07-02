package com.hs.lxv3_1.main;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.baidu.mapapi.cloud.CloudListener;
import com.baidu.mapapi.cloud.CloudManager;
import com.baidu.mapapi.cloud.CloudSearchResult;
import com.baidu.mapapi.cloud.DetailSearchInfo;
import com.baidu.mapapi.cloud.DetailSearchResult;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.hs.lxv3_1.utils.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Holy-Spirit on 2016/5/17.
 */
public class LxSearchServer {

    private SuggestionSearch mSugSearcher = null;

    private GeoCoder mGeoCoder = null;

    private Handler mHandler = null;

    private ReverseGeoCodeResult mReverseResult = null;

    private CloudManager mManager = null;

    private Context mContext = null;
    /*搜索结果*/
    private List<SuggestionResult.SuggestionInfo> mSugInfos = null;

    public LxSearchServer(Context context, Handler handler) {
        this.mHandler = handler;
        this.mContext = context;
    }


    public void getTargetInfoFromCloud(int geoId, int uid) {

        geoId = 31869;
        uid = 18622266;
        mManager = CloudManager.getInstance();
        mManager.init(new CloudListener() {
            @Override
            public void onGetSearchResult(CloudSearchResult cloudSearchResult, int i) {

            }

            @Override
            public void onGetDetailSearchResult(DetailSearchResult detailSearchResult, int i) {

            }
        });
        DetailSearchInfo info = new DetailSearchInfo();
        info.ak = "B266f735e43ab207ec152deff44fec8b";
        info.geoTableId = geoId;
        info.uid = uid;
        mManager.detailSearch(info);

    }



    public void convertLatLngToAddrForDisplay(LatLng ll) {

        if (ll == null) {
            return;
        }
        if (mGeoCoder == null) {
            mGeoCoder = GeoCoder.newInstance();
        }

        mGeoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(ll));
        mGeoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
            @Override
            public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

            }

            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseResult) {

                mReverseResult = reverseResult;
                Message msg = new Message();
                msg.what = Constant.MSG_CONV_LL_TO_DISPLAY;
                mHandler.sendMessage(msg);
            }
        });

    }


    public void convertLatLngToAddr(LatLng ll) {

        if (ll == null) {
            return;
        }
        if (mGeoCoder == null) {
            mGeoCoder = GeoCoder.newInstance();
        }

        mGeoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(ll));
        mGeoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
            @Override
            public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

            }

            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseResult) {

                mReverseResult = reverseResult;
                Message msg = new Message();
                msg.what = Constant.MSG_CONV_LL_TO_INFOWIN;
                mHandler.sendMessage(msg);
            }
        });

    }


    public ReverseGeoCodeResult getReverseResult() {
        return mReverseResult;
    }

    public void searchInfoByLL(LatLng ll) {

        if (ll == null) {
            return;
        }

        if (mSugSearcher == null) {
            mSugSearcher = SuggestionSearch.newInstance();
        }
        SuggestionSearchOption option = new SuggestionSearchOption();
        option.city("");
        option.location(ll);
        mSugSearcher.setOnGetSuggestionResultListener(mSugListener);
        mSugSearcher.requestSuggestion(option);
    }

    public void searchInfoByKeyword(String keyword) {

        if ("".equals(keyword)) {
            return;
        }

        if (mSugSearcher == null) {
            mSugSearcher = SuggestionSearch.newInstance();
        }
        SuggestionSearchOption option = new SuggestionSearchOption();
        option.city("");
        option.keyword(keyword);
        mSugSearcher.setOnGetSuggestionResultListener(mSugListener);
        mSugSearcher.requestSuggestion(option);
    }


    public List<SuggestionResult.SuggestionInfo> getSugInfos() {
        return mSugInfos;
    }

    private OnGetSuggestionResultListener mSugListener = new OnGetSuggestionResultListener() {
        @Override
        public void onGetSuggestionResult(SuggestionResult result) {

            mSugInfos = result.getAllSuggestions();

            if (mSugInfos.isEmpty() || mSugInfos.size() <= 0) {
                Toast.makeText(mContext, "无效地址", Toast.LENGTH_SHORT).show();
                return;
            }

            Bundle b = new Bundle();
            ArrayList<String> list = new ArrayList<>();
            for (SuggestionResult.SuggestionInfo info : mSugInfos) {
                list.add(info.key);
            }
            b.putStringArrayList(Constant.SUG_SEARCH_KEY, list);
            Message msg = new Message();
            msg.what = Constant.MSG_SEARCH_RESULT;
            msg.setData(b);
            mHandler.sendMessage(msg);

        }
    };


    public void onDestroy() {

        if (mGeoCoder != null) {
            mGeoCoder.destroy();
        }

        if (mSugSearcher != null) {
            mSugSearcher.destroy();
        }

    }

}
