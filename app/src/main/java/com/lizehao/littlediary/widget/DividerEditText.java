package com.lizehao.littlediary.widget;

/**
 * Created by admin on 2017/3/2.
 */

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.EditText;

/** 行之间有分隔线的edittext */

public class DividerEditText extends EditText {

    private Rect mRect;
    private Paint mPaint;

    private static final int mLineColor = Color.RED;
    private static final int mLineHeight = 3;

    public DividerEditText(Context context) {
        super(context);
        init();
    }

    public DividerEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    public DividerEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        mRect = new Rect();
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(mLineColor);
        mPaint.setStrokeWidth(mLineHeight);
    }

    protected void onDraw(android.graphics.Canvas canvas) {
//先由系统绘制好正常的界面
        super.onDraw(canvas);



//获取基本的位置信息
        int count = getLineCount();
        Rect r = mRect;
        Paint paint = mPaint;

//循环在每个行的下面绘制一条横线
        for (int i = 0; i < count - 1; i++) {
            int baseline = getLineBounds(i, r);
            canvas.drawLine(r.left, baseline + 1, r.right, baseline + 1, paint);
        }
    };
}