package com.test.recyclerview.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * onDraw方法先于drawChildren
 * onDrawOver在drawChildren之后，一般我们选择复写其中一个即可。
 * getItemOffsets 可以通过outRect.set()为每个Item设置一定的偏移量，主要用于绘制Decorator。
 */
public class ListDivider extends RecyclerView.ItemDecoration {

    private static final int HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL;
    private static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;
    private int mOrientation;
    private Context mContext;
    private static final int[] ATTRS = new int[]{
            android.R.attr.listDivider
    };
    private Drawable mDivider;

    public ListDivider(Context context, int oriention) {
        TypedArray typedArray = context.obtainStyledAttributes(ATTRS);
        mDivider = typedArray.getDrawable(0);
        typedArray.recycle();
        mOrientation = oriention;
        mContext = context;
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
//        绘制分割线
        if (mOrientation == VERTICAL_LIST) {
            final int left = parent.getPaddingLeft();
            final int right = parent.getWidth() - parent.getPaddingRight();
            for (int i = 0; i < parent.getChildCount(); i++) {
                final View childView = parent.getChildAt(i);
                final RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) childView.getLayoutParams();
                final int top = childView.getBottom() + layoutParams.bottomMargin;
                final int bottom = top + mDivider.getIntrinsicHeight();
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        } else if (mOrientation == HORIZONTAL_LIST) {
            final int top = parent.getPaddingTop();
            final int bottom = parent.getHeight() - parent.getPaddingBottom();
            for (int i = 0; i < parent.getChildCount(); i++) {
                final View childView = parent.getChildAt(i);
                final RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) childView.getLayoutParams();
                final int left = childView.getRight() + layoutParams.rightMargin;
                final int right = left + mDivider.getIntrinsicWidth();
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        } else {
            Log.e("MSG", "wrong orientation!");
        }
    }

    public void setDividerBg(int resources){
        mDivider = mContext.getDrawable(resources);
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
//        首先调用，确定分割线绘制位置
        int right = mDivider.getIntrinsicWidth();
        int bottom = mDivider.getIntrinsicHeight();
        if (mOrientation == VERTICAL_LIST) {
            right = 0;
        }
        if (mOrientation == HORIZONTAL_LIST) {
            bottom = 0;
        }
        outRect.set(0, 0, right, bottom);
    }
}
