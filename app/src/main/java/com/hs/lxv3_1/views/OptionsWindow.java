package com.hs.lxv3_1.views;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hs.lxv3_1.R;
import com.hs.lxv3_1.login.LoginActivity;
import com.hs.lxv3_1.login.UserInfoActivity;
import com.hs.lxv3_1.utils.BitmapDecoder;
import com.hs.lxv3_1.utils.Constant;
import com.hs.lxv3_1.utils.SharedPreHelper;

@SuppressWarnings("ALL")
public class OptionsWindow extends PopupWindow {

    public static final int DIRECTION_LEFT = 1;

    public static final int DIRECTION_TOP = 2;

    public static final int DIRECTION_RIGHT = 3;

    public static final int DIRECTION_BOTTOM = 4;

    private View mView;

    /**
     * 菜单按钮的AlertDiag下各控件声明
     */

    /*周边按钮引用*/
    private TextView mSurroundingBtn = null;
    /*足迹按钮引用*/
    private TextView mFootprintBtn = null;
    /*定制按钮引用*/
    private TextView mBookBtn = null;
    /*群组按钮引用*/
    private TextView mGroupBtn = null;
    /*记账本按钮引用*/
    private TextView mAccountBtn = null;
    /*偶遇按钮引用*/
    private TextView mMeetBtn = null;
    /*导航按钮引用*/
    private TextView mNavigateBtn = null;
    /*社区按钮引用*/
    private TextView mCommunityBtn = null;
    /*我的按钮引用 */
    private TextView mCenterBtn = null;

    private CircleImageView mHeadImg = null;

    private TextView mSettingsBtn = null;

    private TextView mAboutBtn = null;

    private boolean isLogined = false;

    private int[] viewIds = new int[]{
            R.id.pop_surr, R.id.pop_footp, R.id.pop_book,
            R.id.pop_group, R.id.pop_account, R.id.pop_meet
    };


    private Context mContext = null;

    private View.OnClickListener mListener = null;


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public OptionsWindow(Activity context, OnClickListener listener) {
        super(context);
        mContext = context;
        mListener = listener;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = inflater.inflate(R.layout.pop_options, null);

        mSurroundingBtn = (TextView) initView(viewIds[0]);
        mFootprintBtn = (TextView) initView(viewIds[1]);
        mBookBtn = (TextView) initView(viewIds[2]);
        mGroupBtn = (TextView) initView(viewIds[3]);
        mAccountBtn = (TextView) initView(viewIds[4]);
        mMeetBtn = (TextView) initView(viewIds[5]);

        mCenterBtn = (TextView) initView(R.id.pop_personal_center);
        mHeadImg = (CircleImageView) mView.findViewById(R.id.pop_ring_head);
        mCenterBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isLogined) {
                    mContext.startActivity(new Intent(mContext, UserInfoActivity.class));
                    dismiss();
                } else {
                    mContext.startActivity(new Intent(mContext, LoginActivity.class));
                    dismiss();
                }

            }
        });
        SharedPreHelper sph = new SharedPreHelper(context, Constant.SP_NAME);

        if (sph.getBoolean(Constant.SP_LOGIN_STATE)) {
            isLogined = true;
            mCenterBtn.setText(sph.getString(Constant.SP_ID));
            Bitmap b = BitmapDecoder
                    .decodeBitmapByResource(mContext.getResources(), R.drawable.user, 40, 40);
            mHeadImg.setImageBitmap(b);

        }

        int[] resIds = new int[]{R.dimen.navi_communicate_left, R.dimen.navi_communicate_top,
                R.dimen.navi_communicate_right, R.dimen.navi_communicate_bottom};
        int[] dimensions = getDimensions(resIds);

        mCommunityBtn = (TextView) initView(DIRECTION_TOP, R.id.community, dimensions,
                R.drawable.community);
        mNavigateBtn = (TextView) initView(DIRECTION_TOP, R.id.pop_navigate, dimensions,
                R.drawable.opt_navi);


        mSettingsBtn = (TextView) initView(R.id.pop_settings);
        mAboutBtn = (TextView) initView(R.id.pop_about);


        //设置SelectPicPopupWindow的View
        this.setContentView(mView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.menu_alert_dialg_anim);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x0b000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int th = mView.findViewById(R.id.pop_layout).getTop();
                int bh = mView.findViewById(R.id.pop_layout).getBottom();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < th || y > bh) {
                        System.out.println("-->>dimiss");
                        dismiss();
                    }
                }
                return true;
            }
        });


    }


    private View initView(int direction, int viewId, int[] dimensions, int resId) {

        if (dimensions == null) {
            return null;
        }
        TextView v = (TextView) mView.findViewById(viewId);

        if (direction == DIRECTION_LEFT) {
            v.setCompoundDrawables(initDrawable(dimensions, resId), null, null, null);
        } else if (direction == DIRECTION_TOP) {
            v.setCompoundDrawables(null, initDrawable(dimensions, resId), null, null);
        } else if (direction == DIRECTION_RIGHT) {
            v.setCompoundDrawables(null, null, initDrawable(dimensions, resId), null);
        } else if (direction == DIRECTION_BOTTOM) {
            v.setCompoundDrawables(null, null, null, initDrawable(dimensions, resId));
        }

        v.setOnClickListener(mListener);
        return v;
    }

    private View initView(int viewId) {
        TextView v = (TextView) mView.findViewById(viewId);
        v.setOnClickListener(mListener);
        return v;
    }

    private Drawable initDrawable(int[] dimensions, int resId) {
        Drawable d = mContext.getResources().getDrawable(resId);
        d.setBounds(dimensions[0], dimensions[1], dimensions[2], dimensions[3]);
        return d;
    }

    private int[] getDimensions(int[] resId) {
        int[] res = new int[resId.length];

        for (int i = 0; i < res.length; i++) {
            res[i] = mContext.getResources().getDimensionPixelOffset(resId[i]);
        }

        return res;
    }

}