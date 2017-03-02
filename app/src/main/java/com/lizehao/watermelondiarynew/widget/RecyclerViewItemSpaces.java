package com.lizehao.watermelondiarynew.widget;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by chunzhang on 2016/5/6.
 */
public class RecyclerViewItemSpaces extends RecyclerView.ItemDecoration {
    private int mLeft;
    private int mTop;
    private int mRight;
    private int mBottom;

    public RecyclerViewItemSpaces() {
    }

    public RecyclerViewItemSpaces(int left, int top, int right, int bottom) {
        mLeft = left;
        mTop = top;
        mRight = right;
        mBottom = bottom;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = mLeft;
        outRect.right = mRight;
        outRect.bottom = mBottom;
        outRect.top = mTop;
    }

    public void setBottom(int bottom) {
        mBottom = bottom;
    }

    public void setLeft(int left) {
        mLeft = left;
    }

    public void setRight(int right) {
        mRight = right;
    }

    public void setTop(int top) {
        mTop = top;
    }
}
