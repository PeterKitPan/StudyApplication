package com.test.animation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * 动画练习首页
 * <p>
 * Android的动画本来有俩种：
 * 1、补间动画Tween Animation
 * 2、帧动画Frame Animation
 * 但是Android3.0之后又加入了属性动画 Property Animation
 * 在Android5.X中增加了对SVG矢量图形的支持，因此产生了矢量动画SVG Animation
 */
public class MainActivity extends AppCompatActivity {

    private Button bt_tween, bt_frame, bt_property, bt_svg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bt_tween = findViewById(R.id.bt_tween);
        bt_frame = findViewById(R.id.bt_frame);
        bt_property = findViewById(R.id.bt_property);
        bt_svg = findViewById(R.id.bt_svg);

        bt_tween.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, TweenAnimationActivity.class));
            }
        });
        bt_frame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, FrameAnimationActivity.class));
            }
        });
        bt_property.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PropertyAnimationActivity.class));
            }
        });
        bt_svg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SVGAnimationActivity.class));
            }
        });
    }
}
