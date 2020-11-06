package com.test.recyclerview.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

/**
 * onDraw方法先于drawChildren
 * onDrawOver在drawChildren之后，一般我们选择复写其中一个即可。
 * getItemOffsets 可以通过outRect.set()为每个Item设置一定的偏移量，主要用于绘制Decorator。
 */
public class GridDivider extends RecyclerView.ItemDecoration {

    private static final int HORIZONTAL_GRID = GridLayoutManager.HORIZONTAL;
    private static final int VERTICAL_GRID = GridLayoutManager.VERTICAL;
    private Context mContext;
    private static final int[] ATTRS = new int[]{
            android.R.attr.listDivider
    };
    private Drawable mDivider;
    private int leftSpace;
    private int midSpace;
    private int rightSpace;


    public GridDivider(Context context) {
        TypedArray typedArray = context.obtainStyledAttributes(ATTRS);
        mDivider = typedArray.getDrawable(0);
        typedArray.recycle();
        mContext = context;
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
//        绘制分割线
        drawVertical(c, parent);
        drawHorizontal(c, parent);
    }

    private void drawHorizontal(Canvas c, RecyclerView parent) {
        for (int i = 0; i < parent.getChildCount(); i++) {
            View childView = parent.getChildAt(i);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) childView.getLayoutParams();
            int left = childView.getLeft() - layoutParams.leftMargin;
            int top = childView.getBottom() + layoutParams.bottomMargin;
            int right = childView.getRight() + layoutParams.rightMargin;
            int bottom = top + mDivider.getIntrinsicHeight();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    private void drawVertical(Canvas c, RecyclerView parent) {
        for (int i = 0; i < parent.getChildCount(); i++) {
            View childView = parent.getChildAt(i);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) childView.getLayoutParams();
            int left = childView.getRight() + layoutParams.rightMargin;
            int top = childView.getTop() - layoutParams.topMargin;
            int right = left + mDivider.getIntrinsicWidth();
            int bottom = childView.getBottom() + layoutParams.bottomMargin + mDivider.getIntrinsicHeight();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    public void setDividerBg(int resources) {
        mDivider = mContext.getDrawable(resources);
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
//        首先调用，确定分割线绘制位置
        int right = mDivider.getIntrinsicWidth();
        int bottom = mDivider.getIntrinsicHeight();
        if (isLastSpan(view, parent)) {
            right = 0;
        }
        if (isLastRow(view, parent)) {
            bottom = 0;
        }
        outRect.set(0, 0, right, bottom);
    }

    private boolean isLastRow(View view, RecyclerView parent) {
        boolean isLastRow = false;
        int childPosition = parent.getChildAdapterPosition(view);
        if (isHorizontal(parent)) {
            if ((childPosition + 1) % getSpanCount(parent) == 0) {
                isLastRow = true;
            } else {
                isLastRow = false;
            }
            return isLastRow;
        }
        if (isVertical(parent)) {
            int totleCount = parent.getAdapter().getItemCount();
            int beforLastPosition = totleCount - totleCount % getSpanCount(parent);
            if (childPosition >= beforLastPosition) {
                isLastRow = true;
            } else {
                isLastRow = false;
            }
            return isLastRow;
        }
        return isLastRow;
    }

    private boolean isLastSpan(View view, RecyclerView parent) {
//        int childPosition = parent.getChildLayoutPosition(view);
        boolean isLastSpan = false;
        int childPosition = parent.getChildAdapterPosition(view);
        int spanCount = getSpanCount(parent);
        if (isHorizontal(parent)) {
            int totleCount = parent.getAdapter().getItemCount();
            int beforLastPosition = totleCount - totleCount % getSpanCount(parent);
            if (childPosition >= beforLastPosition) {
                isLastSpan = true;
            } else {
                isLastSpan = false;
            }
            return isLastSpan;
        }
        if (isVertical(parent)) {
            if ((childPosition + 1) % spanCount == 0) {
                isLastSpan = true;
            } else {
                isLastSpan = false;
            }
            return isLastSpan;
        }
        return isLastSpan;
    }

    private boolean isVertical(RecyclerView parent) {
        boolean isVertical = false;
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            isVertical = RecyclerView.VERTICAL == ((GridLayoutManager) layoutManager).getOrientation();
        }
        if (layoutManager instanceof StaggeredGridLayoutManager) {
            isVertical = RecyclerView.VERTICAL == ((StaggeredGridLayoutManager) layoutManager).getOrientation();
        }
        return isVertical;
    }

    private boolean isHorizontal(RecyclerView parent) {
        boolean isHorizental = false;
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            isHorizental = RecyclerView.HORIZONTAL == ((GridLayoutManager) layoutManager).getOrientation();
        }
        if (layoutManager instanceof StaggeredGridLayoutManager) {
            isHorizental = RecyclerView.HORIZONTAL == ((StaggeredGridLayoutManager) layoutManager).getOrientation();
        }
        return isHorizental;
    }

    private int getSpanCount(RecyclerView parent) {
        int spanCount = 1;
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
        }
        if (layoutManager instanceof StaggeredGridLayoutManager) {
            spanCount = ((StaggeredGridLayoutManager) layoutManager).getSpanCount();
        }
        return spanCount;
    }
}
