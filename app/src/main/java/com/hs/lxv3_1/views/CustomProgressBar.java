package com.hs.lxv3_1.views;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import com.hs.lxv3_1.R;

/**

 * @author Holy-Spirit
 *
 */

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
@SuppressLint("DrawAllocation")
public class CustomProgressBar extends View {


    private Paint mPaint = null;

    private static final int[] colors = new int[] { R.color.color_top,
            R.color.color_right_top, R.color.color_right,
            R.color.color_right_bottom, R.color.color_bottom,
            R.color.color_left_bottom, R.color.color_left,
            R.color.color_left_top };


    private Resources res = null;


    private int i = 0;

    public CustomProgressBar(Context context) {
        super(context);

    }

    public CustomProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        res = context.getResources();// ��ȡ��Դ����������ȡcolorֵ
    }

    public void refreshView() {
        invalidate();// ����onDraw�����������ػ�Progress
    }


    private int calculateIndex(int in) {
        return in % (colors.length-1);
    }

    public void setLineColor(int index) {
        mPaint.setColor(res.getColor(colors[calculateIndex(index)]));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        final float roundWidth;
        final int width = canvas.getWidth();
        final int height = canvas.getHeight();
        roundWidth = width > height ? height : width;
        final float halfRoundWidth = roundWidth / 2;// ��ȡ�����İ볤
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(roundWidth / 15);// ����Ϊ������ʮ���֮һ
        float f1 = halfRoundWidth / 5;
        float f2 = (float) Math.sqrt(f1 * f1 / 2);// ���ĵ㵽ָ��ĳ�����x��y���ϵ�ͶӰ
        float f3 = (float) Math.sqrt(f1 * f1 * 9) / 2;//ָ�볤��x��y���ϵ�ͶӰ
        float f5 = halfRoundWidth - f3 - f1;//�����ĸ�㵽ָ��ĳ�����x��y���ϵ�ͶӰ

        // top
        this.setLineColor(0 + i);
        canvas.drawLine(halfRoundWidth, halfRoundWidth / 5, halfRoundWidth,
                halfRoundWidth / 5 * 4, mPaint);

        // right top
        this.setLineColor(1 + i);
        canvas.drawLine(roundWidth - f5, f5, halfRoundWidth + f2,
                halfRoundWidth - f2, mPaint);

        // right
        this.setLineColor(2 + i);
        canvas.drawLine(halfRoundWidth / 5 * 9, halfRoundWidth,
                halfRoundWidth / 5 * 6, halfRoundWidth, mPaint);

        // right bottom
        this.setLineColor(3 + i);
        canvas.drawLine(roundWidth - f5, roundWidth - f5, halfRoundWidth + f2,
                halfRoundWidth + f2, mPaint);

        // bottom
        this.setLineColor(4 + i);
        canvas.drawLine(halfRoundWidth, halfRoundWidth / 5 * 9, halfRoundWidth,
                halfRoundWidth / 5 * 6, mPaint);

        // left bottom
        this.setLineColor(5 + i);
        canvas.drawLine(f5, roundWidth - f5, halfRoundWidth - f2,
                halfRoundWidth + f2, mPaint);

        // left
        this.setLineColor(6 + i);
        canvas.drawLine(halfRoundWidth / 5, halfRoundWidth,
                halfRoundWidth / 5 * 4, halfRoundWidth, mPaint);
        // // left top
        this.setLineColor(7 + i);
        canvas.drawLine(f5, f5, halfRoundWidth - f2, halfRoundWidth - f2,
                mPaint);

        i++;
    }

}