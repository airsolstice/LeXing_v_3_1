package com.hs.lxv3_1.views;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.hs.lxv3_1.R;
import com.hs.lxv3_1.main.DataBaseHelper;
import com.hs.lxv3_1.main.LocBean;
import com.hs.lxv3_1.navi.NavigationOptActivity;

import java.util.ArrayList;

/**
 * Created by Holy-Spirit on 2016/4/25.
 */
public class DisplayWindow extends PopupWindow {


    private View mView = null;

    private FloatingActionButton mFab = null;



    public DisplayWindow(final Context context, final ReverseGeoCodeResult rgcr,
                         final boolean isGone) {
        super(context);

        final LayoutInflater inflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mView = inflater.inflate(R.layout.pop_display, null);
        mFab = (FloatingActionButton) mView.findViewById(R.id.pop_navi);

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, NavigationOptActivity.class);
                Bundle b = new Bundle();
                LocBean lb = DataBaseHelper.queryLast(context);
                LatLng startll = new LatLng(lb.getLat(),lb.getLng());
                b.putString("currentcity",rgcr.getAddressDetail().city);
                b.putParcelable("startll", startll);
                b.putString("startaddr",lb.getAddr());
                b.putParcelable("endll",rgcr.getLocation());
                b.putString("endaddr",rgcr.getAddress());
                i.putExtras(b);
                context.startActivity(i);
                DisplayWindow.this.dismiss();
            }
        });


        TextView tv = (TextView) mView.findViewById(R.id.result_txt);
        if (!isGone) {
            tv.setBackgroundColor(Color.WHITE);
        }

        tv.setText(rgcr.getAddress());

        ListView lv = (ListView) mView.findViewById(R.id.poi_list);


        ArrayList<String> datas = new ArrayList<>();

        for (PoiInfo p : rgcr.getPoiList()) {
            datas.add(p.name);
        }

        ArrayAdapter<String> adapter =
                new ArrayAdapter(context, android.R.layout.simple_list_item_1, datas);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


            }
        });

        //设置SelectPicPopupWindow的View
        this.setContentView(mView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.menu_alert_dialg_anim);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x00000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);


    }


}
