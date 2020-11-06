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
 * 四周没有分割线
 */
public class GridDivider2 extends RecyclerView.ItemDecoration {
    private Context mContext;
    private static final int[] ATTRS = new int[]{
            android.R.attr.listDivider
    };
    private Drawable mDivider;
    private int dividerSpace_H;
    private int miniSpace_H;
    private int middleSpace_H;
    private int maxSpace_H;
    private int dividerSpace_V;
    private int miniSpace_V;
    private int middleSpace_V;
    private int maxSpace_V;


    public GridDivider2(Context context) {
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
            drawBottom(childView, layoutParams, c);
            drawTop(childView, layoutParams, c);
        }
    }

    private void drawTop(View childView, RecyclerView.LayoutParams layoutParams, Canvas c) {
        int left = childView.getLeft() - layoutParams.leftMargin;
        int top = childView.getBottom() + layoutParams.bottomMargin;
        int right = childView.getRight() + layoutParams.rightMargin;
        int bottom = top + dividerSpace_V;
        mDivider.setBounds(left, top, right, bottom);
        mDivider.draw(c);
    }

    private void drawBottom(View childView, RecyclerView.LayoutParams layoutParams, Canvas c) {
        int left = childView.getLeft() - layoutParams.leftMargin;
        int bottom = childView.getTop() - layoutParams.topMargin;
        int top = bottom - dividerSpace_V;
        int right = childView.getRight() + layoutParams.rightMargin;
        mDivider.setBounds(left, top, right, bottom);
        mDivider.draw(c);
    }

    private void drawVertical(Canvas c, RecyclerView parent) {
        for (int i = 0; i < parent.getChildCount(); i++) {
            View childView = parent.getChildAt(i);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) childView.getLayoutParams();
            drawLeft(childView, layoutParams, c);
            drawRight(childView, layoutParams, c);
        }
    }

    private void drawRight(View childView, RecyclerView.LayoutParams layoutParams, Canvas c) {
        int left = childView.getRight() + layoutParams.rightMargin;
        int top = childView.getTop() - layoutParams.topMargin - dividerSpace_V;
        int right = left + dividerSpace_H;
        int bottom = childView.getBottom() + layoutParams.bottomMargin + dividerSpace_V;
        mDivider.setBounds(left, top, right, bottom);
        mDivider.draw(c);
    }

    private void drawLeft(View childView, RecyclerView.LayoutParams layoutParams, Canvas c) {
        int right = childView.getLeft() - layoutParams.leftMargin;
        int top = childView.getTop() - layoutParams.topMargin - dividerSpace_V;
        int left = right - dividerSpace_H;
        int bottom = childView.getBottom() + layoutParams.bottomMargin + dividerSpace_V;
        mDivider.setBounds(left, top, right, bottom);
        mDivider.draw(c);
    }

    public void setDividerBg(int resources) {
        mDivider = mContext.getDrawable(resources);
        dividerSpace_H = mDivider.getIntrinsicWidth();
        miniSpace_H = dividerSpace_H / 3 + 1;
        middleSpace_H = dividerSpace_H / 2;
        maxSpace_H = 2 * dividerSpace_H / 3;
        dividerSpace_V = mDivider.getIntrinsicHeight();
        miniSpace_V = dividerSpace_V / 3;
        middleSpace_V = dividerSpace_V / 2;
        maxSpace_V = 2 * dividerSpace_V / 3;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
//        首先调用，确定分割线绘制位置
        int spanCount = getSpanCount(parent);
        int rowCount = getRowCount(parent);
        int left = 0;
        int right = 0;
        int top = 0;
        int bottom = 0;
        if (rowCount == 2) {
            //只有两行
            if (isFirstRow(view, parent)) {
                top = 0;
                bottom = middleSpace_V;
            } else if (isLastRow(view, parent)) {
                top = middleSpace_V;
                bottom = 0;
            }
        } else if (rowCount == 3) {
            //只有三行
            if (isFirstRow(view, parent)) {
                top = 0;
                bottom = maxSpace_V;
            } else if (isLastRow(view, parent)) {
                top = maxSpace_V;
                bottom = 0;
            } else {
                top = miniSpace_V;
                bottom = miniSpace_V;
            }
        } else if (rowCount >= 4) {
            //四行及以上
            if (isFirstRow(view, parent)) {
                top = 0;
                bottom = maxSpace_V;
            } else if (isLastRow(view, parent)) {
                top = maxSpace_V;
                bottom = 0;
            } else if (isSecondRow(view, parent)) {
                top = miniSpace_V;
                bottom = middleSpace_V;
            } else if (isSecondLastRow(view, parent)) {
                top = middleSpace_V;
                bottom = miniSpace_V;
            } else {
                top = middleSpace_V;
                bottom = middleSpace_V;
            }
        }
        if (spanCount == 2) {
            //只有两列
            if (isFirstSpan(view, parent)) {
                left = 0;
                right = middleSpace_H;
            } else if (isLastSpan(view, parent)) {
                left = middleSpace_H;
                right = 0;
            }
        } else if (spanCount == 3) {
            //只有三列
            if (isFirstSpan(view, parent)) {
                left = 0;
                right = maxSpace_H;
            } else if (isLastSpan(view, parent)) {
                left = maxSpace_H;
                right = 0;
            } else {
                left = miniSpace_H;
                right = miniSpace_H;
            }
        } else if (spanCount >= 4) {
            //四列及以上
            if (isFirstSpan(view, parent)) {
                left = 0;
                right = maxSpace_H;
            } else if (isLastSpan(view, parent)) {
                left = maxSpace_H;
                right = 0;
            } else if (isSecondSpan(view, parent)) {
                left = miniSpace_H;
                right = middleSpace_H;
            } else if (isSecondLastSpan(view, parent)) {
                left = middleSpace_H;
                right = miniSpace_H;
            } else {
                left = middleSpace_H;
                right = middleSpace_H;
            }
        }
        outRect.set(left, top, right, bottom);
    }

    private boolean isSecondLastRow(View view, RecyclerView parent) {
        boolean isSecondLastRow = false;
        int childPosition = parent.getChildAdapterPosition(view);
        int spanCount = getSpanCount(parent);
        if (isHorizontal(parent)) {
            if ((childPosition + 1) % spanCount == (spanCount - 1)) {
                isSecondLastRow = true;
            } else {
                isSecondLastRow = false;
            }
            return isSecondLastRow;
        }
        if (isVertical(parent)) {
            int totleCount = parent.getAdapter().getItemCount();
            int totalRow = (totleCount + spanCount - 1) / spanCount;
            int tempRow = (childPosition + spanCount) / spanCount;
            if (totalRow - tempRow == 1) {
                isSecondLastRow = true;
            } else {
                isSecondLastRow = false;
            }
            return isSecondLastRow;
        }
        return isSecondLastRow;
    }

    private boolean isSecondRow(View view, RecyclerView parent) {
        boolean isSecondRow = false;
        int childPosition = parent.getChildAdapterPosition(view);
        int spanCount = getSpanCount(parent);
        if (isHorizontal(parent)) {
            if ((childPosition + 1) % spanCount == 2) {
                isSecondRow = true;
            } else {
                isSecondRow = false;
            }
            return isSecondRow;
        }
        if (isVertical(parent)) {
            if (childPosition >= spanCount && childPosition < 2 * spanCount) {
                isSecondRow = true;
            } else {
                isSecondRow = false;
            }
            return isSecondRow;
        }
        return isSecondRow;
    }

    private boolean isFirstRow(View view, RecyclerView parent) {
        boolean isFirstRow = false;
        int childPosition = parent.getChildAdapterPosition(view);
        int spanCount = getSpanCount(parent);
        if (isHorizontal(parent)) {
            if ((childPosition + 1) % spanCount == 1) {
                isFirstRow = true;
            } else {
                isFirstRow = false;
            }
            return isFirstRow;
        }
        if (isVertical(parent)) {
            if (childPosition < spanCount) {
                isFirstRow = true;
            } else {
                isFirstRow = false;
            }
            return isFirstRow;
        }
        return isFirstRow;
    }

    private boolean isSecondLastSpan(View view, RecyclerView parent) {
        boolean isSecondLastSpan = false;
        int childPosition = parent.getChildAdapterPosition(view);
        int spanCount = getSpanCount(parent);
        if (isHorizontal(parent)) {
            int totleCount = parent.getAdapter().getItemCount();
            int totalSpan = (totleCount + spanCount - 1) / spanCount;
            int tempSpan = (childPosition + spanCount) / spanCount;
            if (totalSpan - tempSpan == 1) {
                isSecondLastSpan = true;
            } else {
                isSecondLastSpan = false;
            }
            return isSecondLastSpan;
        }
        if (isVertical(parent)) {
            if ((childPosition + 1) % spanCount == spanCount - 1) {
                isSecondLastSpan = true;
            } else {
                isSecondLastSpan = false;
            }
            return isSecondLastSpan;
        }
        return isSecondLastSpan;
    }

    private boolean isSecondSpan(View view, RecyclerView parent) {
        boolean isSecondSpan = false;
        int childPosition = parent.getChildAdapterPosition(view);
        int spanCount = getSpanCount(parent);
        if (isHorizontal(parent)) {
            if (childPosition >= spanCount && childPosition < spanCount * 2) {
                isSecondSpan = true;
            } else {
                isSecondSpan = false;
            }
            return isSecondSpan;
        }
        if (isVertical(parent)) {
            if ((childPosition + 1) % spanCount == 2) {
                isSecondSpan = true;
            } else {
                isSecondSpan = false;
            }
            return isSecondSpan;
        }
        return isSecondSpan;
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
            int spanCount = getSpanCount(parent);
            int totleCount = parent.getAdapter().getItemCount();
            int leftCount = totleCount % getSpanCount(parent);
            int beforLastPosition = 0;
            if (leftCount > 0) {
                beforLastPosition = totleCount - leftCount;
            } else {
                beforLastPosition = totleCount - spanCount;
            }
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
            int leftCount = totleCount % getSpanCount(parent);
            int beforLastPosition = 0;
            if (leftCount > 0) {
                beforLastPosition = totleCount - leftCount;
            } else {
                beforLastPosition = totleCount - spanCount;
            }
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

    private boolean isFirstSpan(View view, RecyclerView parent) {
//        int childPosition = parent.getChildLayoutPosition(view);
        boolean isFirstSpan = false;
        int childPosition = parent.getChildAdapterPosition(view);
        int spanCount = getSpanCount(parent);
        if (isHorizontal(parent)) {
            if (childPosition < spanCount) {
                isFirstSpan = true;
            } else {
                isFirstSpan = false;
            }
            return isFirstSpan;
        }
        if (isVertical(parent)) {
            if ((childPosition + 1) % spanCount == 1) {
                isFirstSpan = true;
            } else {
                isFirstSpan = false;
            }
            return isFirstSpan;
        }
        return isFirstSpan;
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

    private int getRowCount(RecyclerView parent) {
        int totleCount = parent.getAdapter().getItemCount();
        int totalRow = (totleCount + getSpanCount(parent) - 1) / getSpanCount(parent);
        return totalRow;
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
