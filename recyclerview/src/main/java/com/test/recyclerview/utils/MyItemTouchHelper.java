package com.test.recyclerview.utils;

import android.graphics.Canvas;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.test.recyclerview.R;
import com.test.recyclerview.interfaces.OnItemMoveListener;

public class MyItemTouchHelper extends ItemTouchHelper.Callback {
    private OnItemMoveListener onItemMoveListener;
    private boolean dragAbled = false;
    int dragFlags;//上下标志
    int swipeFlags;//左右标志

    public MyItemTouchHelper(OnItemMoveListener onItemMoveListener) {
        this.onItemMoveListener = onItemMoveListener;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        swipeFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
//        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
//        if (layoutManager instanceof GridLayoutManager || layoutManager instanceof StaggeredGridLayoutManager) {
//            swipeFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
//        } else {
////            swipeFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
//            swipeFlags = 0;//只允许网格可以所有方向都能拖动
//        }
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        if (dragAbled) {
            Log.e("MSG", "onMove");
            if (viewHolder.getItemViewType() != target.getItemViewType()) {
                return false;
            }
            onItemMoveListener.onItemDrageMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
            return true;
        }
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        Log.e("MSG", "onSwiped");
        onItemMoveListener.onItemSwipMove(viewHolder.getAdapterPosition());
    }


    /**
     * 是否支持长按拖拽 默认为true
     *
     * @return
     */
    @Override
    public boolean isLongPressDragEnabled() {
        Log.e("MSG", "isLongPressDragEnabled");
        dragAbled = true;
        return super.isLongPressDragEnabled();
//        return true;
    }

    /**
     * 是否支持滑动 默认为true
     *
     * @return
     */
    @Override
    public boolean isItemViewSwipeEnabled() {
        return super.isItemViewSwipeEnabled();
    }

    @Override
    public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
        //选中item效果
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            viewHolder.itemView.setBackgroundColor(viewHolder.itemView.getContext().getResources().getColor(R.color.colorPrimary));
        }
        super.onSelectedChanged(viewHolder, actionState);
    }

    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        Log.e("MSG", "clearView");
        if (dragAbled) {
            dragAbled = false;
        }
        //选中动画完成后的恢复
        viewHolder.itemView.setBackgroundColor(viewHolder.itemView.getContext().getResources().getColor(R.color.white));
        super.clearView(recyclerView, viewHolder);
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        float percent = 1 - Math.abs(dX) / viewHolder.itemView.getWidth();
        viewHolder.itemView.setAlpha(percent);
        viewHolder.itemView.setScaleX(percent);
        viewHolder.itemView.setScaleY(percent);
        if (percent == 0) {
            viewHolder.itemView.setAlpha(1);
            viewHolder.itemView.setScaleX(1);
            viewHolder.itemView.setScaleY(1);
        }
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }
}
