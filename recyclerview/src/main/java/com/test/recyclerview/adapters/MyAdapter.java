package com.test.recyclerview.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.test.recyclerview.R;
import com.test.recyclerview.interfaces.OnClick;
import com.test.recyclerview.interfaces.OnItemDrageListener;
import com.test.recyclerview.interfaces.OnItemMoveListener;

import java.util.Collections;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter implements OnItemMoveListener {

    private Context context;
    private List<String> datas;
    private OnClick onClick;
    private OnItemDrageListener onItemDrageListener;

    public void setOnClickListener(OnClick onClick) {
        this.onClick = onClick;
    }

    public MyAdapter(Context context, List<String> datas) {
        this.context = context.getApplicationContext();
        this.datas = datas;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_item, parent, false);
//        View view = LayoutInflater.from(context).inflate(R.layout.adapter_item, null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        ((MyViewHolder) holder).context.setText(datas.get(position));

        ((MyViewHolder) holder).context.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClick != null) {
                    onClick.OnContextClickListener(position);
                }
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClick != null) {
                    onClick.OnItemClickListener(position);
                }
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (onItemDrageListener != null) {
                    onItemDrageListener.onItemDrage(holder);
                }
                return false;
            }
        });

//        ((MyViewHolder) holder).context.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN && onItemDrageListener != null) {
//                    onItemDrageListener.onItemDrage(holder);
//                }
//                return false;
//            }
//        });
    }

    @Override
    public int getItemCount() {
        if (datas!=null)
            return datas.size();
        return 0;
    }

    @Override
    public void onItemDrageMove(int from, int to) {
        Collections.swap(datas, from, to);
        notifyItemMoved(from, to);
    }

    @Override
    public void onItemSwipMove(int position) {
        Log.e("MSG","onItemSwipMove");
        datas.remove(position);
        notifyItemRemoved(position);
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView context;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.findViewById(R.id.context);
        }
    }

    public void setOnItemDrageListener(OnItemDrageListener onItemDrageListener) {
        this.onItemDrageListener = onItemDrageListener;
    }
}
