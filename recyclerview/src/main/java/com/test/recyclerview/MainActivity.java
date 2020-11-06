package com.test.recyclerview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.test.recyclerview.activities.DividerActivity;
import com.test.recyclerview.activities.ItemActivity;

/**
 * RecyclerView相关练习
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button bt_divider, bt_item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bt_divider = findViewById(R.id.bt_divider);
        bt_item = findViewById(R.id.bt_item);
        bt_divider.setOnClickListener(this);
        bt_item.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_divider:
                startActivity(new Intent(MainActivity.this, DividerActivity.class));
                break;
            case R.id.bt_item:
                startActivity(new Intent(MainActivity.this, ItemActivity.class));
                break;
        }
    }
}
