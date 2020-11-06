package com.test.recyclerview.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Bundle;

import com.test.recyclerview.R;
import com.test.recyclerview.adapters.MyAdapter;
import com.test.recyclerview.utils.GridDivider;
import com.test.recyclerview.utils.GridDivider2;
import com.test.recyclerview.utils.ListDivider;

import java.util.ArrayList;

/**
 * 分割线
 */
public class DividerActivity extends AppCompatActivity {

    private RecyclerView rcv;
    private MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_divider);
        rcv = findViewById(R.id.rcv);
        ArrayList<String> datas = new ArrayList<>();
        for (int i = 0; i < 28; i++) {
            datas.add("数据" + i);
        }
        myAdapter = new MyAdapter(this, datas);
//        show1();
//        show2();
        show3();
    }

    private void show3() {
//        rcv.setLayoutManager(new StaggeredGridLayoutManager(3, RecyclerView.HORIZONTAL));
//        GridDivider myDivider = new GridDivider(this);
        rcv.setLayoutManager(new StaggeredGridLayoutManager(3, RecyclerView.VERTICAL));
        GridDivider myDivider = new GridDivider(this);
        myDivider.setDividerBg(R.drawable.divider_bg);
        rcv.addItemDecoration(myDivider);
        rcv.setAdapter(myAdapter);
    }

    private void show2() {
//        rcv.setLayoutManager(new GridLayoutManager(this,3, RecyclerView.HORIZONTAL, false));
//        GridDivider myDivider = new GridDivider(this);
        rcv.setLayoutManager(new GridLayoutManager(this, 7, RecyclerView.HORIZONTAL, false));
        GridDivider2 myDivider = new GridDivider2(this);
        myDivider.setDividerBg(R.drawable.divider_bg);
        rcv.addItemDecoration(myDivider);
        rcv.setAdapter(myAdapter);
    }

    private void show1() {
        rcv.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        ListDivider myDivider = new ListDivider(this, LinearLayoutManager.VERTICAL);
        myDivider.setDividerBg(R.drawable.divider_bg);
//        rcv.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
//        ListDivider myDivider = new ListDivider(this, LinearLayoutManager.VERTICAL);
        rcv.addItemDecoration(myDivider);
        rcv.setAdapter(myAdapter);
    }
}