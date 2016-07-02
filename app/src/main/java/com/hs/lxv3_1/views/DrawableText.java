package com.hs.lxv3_1.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

import com.hs.lxv3_1.R;
import com.hs.lxv3_1.utils.InputMethodSetter;

/**
 * Created by Holy-Spirit on 2016/3/7.
 */
public class DrawableText extends EditText {

    private Drawable mRightDrawable;
    private Rect mBoundRect;
    private Context mContext;
    private DrawableListener mListener = null;
    private TextWatcher mWathcer = null;

    public DrawableText(Context context) {
        super(context);
        mContext = context;
    }

    public DrawableText(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public DrawableText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint p = new Paint();
        p.setColor(mContext.getResources().getColor(R.color.search_view_line));
        p.setAntiAlias(true);
        p.setStrokeWidth(2.5f);

        Drawable[] drawables = this.getCompoundDrawables();
        Rect r = drawables[0].getBounds();

        float startX = this.getX() + r.width() + this.getPaddingRight();
        float stopX = startX;
        float startY = this.getX() ;
        float stopY = this.getX()+this.getMeasuredHeight()/3*2 ;
        canvas.drawLine(startX, startY, stopX, stopY, p);

    }

    @Override
    public void setCompoundDrawables(Drawable left, Drawable top,
                                     Drawable right, Drawable bottom) {
        if (right != null) {
            mRightDrawable = right;
        }
        super.setCompoundDrawables(left, top, right, bottom);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_UP && mRightDrawable != null) {
            mBoundRect = mRightDrawable.getBounds();
            final int x = (int) event.getX();
            final int y = (int) event.getY();
            //check to make sure the touch event was within the bounds of the drawable
/*
            System.out.println("-->>xl:"+(this.getRight()-mBoundRect.width()-this.getPaddingRight
            ()-this.getCompoundPaddingLeft()));
            System.out.println("-->>xh:"+this.getRight());
            System.out.println("-->>yl:"+this.getPaddingTop());
            System.out.println("-->>yh:"+this.getHeight());
            System.out.println();
            System.out.println();
            System.out.println("-->>xxxx"+x);
            System.out.println("-->>yyyy"+y);
*/

            if (x >= (this.getRight() - mBoundRect.width() - this.getPaddingRight() -
                    this.getCompoundPaddingLeft()) &&
                    x <= (this.getRight())
                    && y >= this.getPaddingTop() &&
                    y <= this.getHeight()) {
                System.out.println("-->>touch right");
                InputMethodSetter.hideInputMethod(mContext, this, event);

                mListener.onClickedRight();
                /*use this to prevent the keyboard from coming up*/
                event.setAction(MotionEvent.ACTION_CANCEL);
            } else if (x <= (this.getLeft() + mBoundRect.width() + this.getPaddingRight() +
                    this.getCompoundPaddingLeft()) &&
                    x >= this.getLeft()
                    && y >= this.getPaddingTop() &&
                    y <= this.getHeight()) {
                System.out.println("-->>touch left");
                InputMethodSetter.hideInputMethod(mContext, this, event);
                mListener.onClickedLeft();
                /*use this to prevent the keyboard from coming up*/
                event.setAction(MotionEvent.ACTION_CANCEL);
            } else {
                this.addTextChangedListener(mWathcer);
            }


        }
        return super.onTouchEvent(event);
    }


    @Override
    protected void finalize() throws Throwable {
        mRightDrawable = null;
        mBoundRect = null;
        super.finalize();
    }

    /**
     * 绑定右边图片点击事件
     *
     * @param cet
     */

    public void addTouchEventListener(DrawableListener cet) {
        this.mListener = cet;
    }


    public void addTextWatcher(TextWatcher watcher) {
        this.mWathcer = watcher;

    }

}
