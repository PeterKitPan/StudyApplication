package com.test.packing;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

/**
 * 65535问题解决（multiDex）
 * 多渠道打包
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
