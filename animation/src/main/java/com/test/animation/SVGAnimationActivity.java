package com.test.animation;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.airbnb.lottie.LottieAnimationView;

/**
 * SVG动画 SVG Animation（Android 5.0 之后加入）
 */
public class SVGAnimationActivity extends AppCompatActivity {

    private ImageView iv_live;
    private LottieAnimationView iv_lottie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_s_v_g_animation);

        iv_live = findViewById(R.id.iv_live);
        iv_live.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Drawable drawable = iv_live.getDrawable();
                if (drawable instanceof Animatable) {
                    ((Animatable) drawable).start();
                }
            }
        });

        iv_lottie = findViewById(R.id.iv_lottie);
        iv_lottie.setRepeatCount(100);
//        iv_lottie.setAnimation("data.json");//在assets目录下的动画json文件名。
//        iv_lottie.setAnimation("hello-world.json");//在assets目录下的动画json文件名。
        iv_lottie.setAnimation("LottieLogo1.json");//在assets目录下的动画json文件名。
        iv_lottie.playAnimation();
    }
}