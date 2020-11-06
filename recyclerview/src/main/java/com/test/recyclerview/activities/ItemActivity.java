package com.test.recyclerview.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.test.recyclerview.R;
import com.test.recyclerview.adapters.MyAdapter;
import com.test.recyclerview.interfaces.OnItemDrageListener;
import com.test.recyclerview.utils.ListDivider;
import com.test.recyclerview.utils.MyItemTouchHelper;

import java.util.ArrayList;

/**
 * 拖拽滑动
 */
public class ItemActivity extends AppCompatActivity {

    private RecyclerView rcv;
    private MyAdapter myAdapter;
    private MyItemTouchHelper myItemTouchHelper;
    private ItemTouchHelper itemTouchHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        rcv = findViewById(R.id.rcv);
        ArrayList<String> datas = new ArrayList<>();
        for (int i = 0; i < 28; i++) {
            datas.add("数据" + i);
        }
        myAdapter = new MyAdapter(this, datas);
        rcv.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        ListDivider myDivider = new ListDivider(this, LinearLayoutManager.VERTICAL);
        myDivider.setDividerBg(R.drawable.divider_bg);
        rcv.addItemDecoration(myDivider);

        myItemTouchHelper = new MyItemTouchHelper(myAdapter);
        itemTouchHelper = new ItemTouchHelper(myItemTouchHelper);
        itemTouchHelper.attachToRecyclerView(rcv);
        myAdapter.setOnItemDrageListener(new OnItemDrageListener() {
            @Override
            public void onItemDrage(RecyclerView.ViewHolder viewHolder) {
                itemTouchHelper.startDrag(viewHolder);
            }
        });
        rcv.setAdapter(myAdapter);
    }
}