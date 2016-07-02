package com.hs.lxv3_1.navi;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.baidu.mapapi.search.route.BikingRouteLine;
import com.baidu.mapapi.search.route.DrivingRouteLine;
import com.baidu.mapapi.search.route.TransitRouteLine;
import com.baidu.mapapi.search.route.WalkingRouteLine;
import com.hs.lxv3_1.R;
import com.hs.lxv3_1.main.LxRouteServer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Holy-Spirit on 2016/5/19.
 */
public class LxExpandableListAdapter extends BaseExpandableListAdapter {


    private Context mContext = null;

    private LayoutInflater mInflater = null;

    private ArrayList<String> mGroudpDatas = new ArrayList<>();

    private List<List<String>> mSubDatas = new ArrayList<>();

    public LxExpandableListAdapter(Context context, LxRouteServer router) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);

        if (router.getBikingResult() != null) {
            String str = "骑行";
            String str1;
            int c = 1;
            List<BikingRouteLine> bLines = router.getBikingResult().getRouteLines();

            if(bLines == null){
                System.out.println("-->>result is bull");
                return;
            }
            for (BikingRouteLine bl : bLines) {
                str1 = str + c;
                c++;
                mGroudpDatas.add(str1);
                List<String> stepList = new ArrayList<>();
                for (BikingRouteLine.BikingStep bs : bl.getAllStep()) {
                    stepList.add(bs.getInstructions());
                }
                mSubDatas.add(stepList);

            }

        }

        if (router.getDrivingResult() != null) {
            int c = 1;
            List<DrivingRouteLine> dLines = router.getDrivingResult().getRouteLines();


            if(dLines == null){
                System.out.println("-->>result is bull");
                return;
            }

            for (DrivingRouteLine bl : dLines) {
                String str = "自驾";
                String str1;
                str1 = str + c;
                c++;
                mGroudpDatas.add(str1);
                List<String> stepList = new ArrayList<>();
                for (DrivingRouteLine.DrivingStep ds : bl.getAllStep()) {
                    stepList.add(ds.getInstructions());
                }
                mSubDatas.add(stepList);

            }

        }

        if (router.getTransitResult() != null) {

            List<TransitRouteLine> tLines = router.getTransitResult().getRouteLines();

            if(tLines == null){
                System.out.println("-->>result is bull");
                return;
            }
            int c = 1;
            for (TransitRouteLine bl : tLines) {
                String str = "公交";
                String str1;
                str1 = str + c;
                c++;
                mGroudpDatas.add(str1);
                List<String> stepList = new ArrayList<>();
                for (TransitRouteLine.TransitStep ts : bl.getAllStep()) {
                    stepList.add(ts.getInstructions());
                }
                mSubDatas.add(stepList);

            }

        }

        if (router.getWalkResult() != null) {

            List<WalkingRouteLine> wLines = router.getWalkResult().getRouteLines();

            if(wLines == null){
                System.out.println("-->>result is bull");
                return;
            }
            int c = 1;
            for (WalkingRouteLine bl : wLines) {
                String str = "步行";
                String str1;
                str1 = str + c;
                c++;
                mGroudpDatas.add(str1);
                List<String> stepList = new ArrayList<>();
                for (WalkingRouteLine.WalkingStep ws : bl.getAllStep()) {
                    stepList.add(ws.getInstructions());
                }
                mSubDatas.add(stepList);

            }

        }

    }


    public Object getChild(int gPosition, int cPosition) {
        return mSubDatas.get(gPosition).get(cPosition);
    }

    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    public int getChildrenCount(int gPosition) {
        return mSubDatas.get(gPosition).size();
    }

    public TextView getGenericView() {
        // Layout parameters for the ExpandableListView
        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 36);

        TextView textView = new TextView(mContext);
        textView.setLayoutParams(lp);
        // Center the text vertically
        textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
        // Set the text starting position
        textView.setPadding(36, 0, 0, 0);
        textView.setTextColor(Color.BLACK);

        return textView;
    }

    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        TextView textView = getGenericView();
        textView.setText(getChild(groupPosition, childPosition).toString());
        return textView;
    }

    public Object getGroup(int gPosition) {
        return mGroudpDatas.get(gPosition);
    }

    public int getGroupCount() {
        return mGroudpDatas.size();
    }

    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    /**
     * create group view and bind data to view
     */
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        View v;
        if (convertView == null) {
            v = mInflater.inflate(R.layout.elv_result_item, null);
        } else {
            v = convertView;
        }
        TextView textView = (TextView) v.findViewById(R.id.elv_title_tv);


        textView.setText(getGroup(groupPosition).toString());
        return v;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public boolean hasStableIds() {
        return true;
    }


}
