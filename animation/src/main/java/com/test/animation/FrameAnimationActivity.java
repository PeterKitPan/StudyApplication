package com.test.animation;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

/**
 * 帧动画 Frame Animation
 * <p>
 * void start()：开始播放逐帧动画。
 * void stop()：停止播放逐帧动画。
 * void addFrame(Drawable frame,int duration)：为AnimationDrawable添加一帧，并设置持续时间。
 * int getDuration(int i)：得到指定index的帧的持续时间。
 * Drawable getFrame(int index)：得到指定index的帧Drawable。
 * int getNumberOfFrames()：得到当前AnimationDrawable的所有帧数量。
 * boolean isOneShot()：当前AnimationDrawable是否执行一次，返回true执行一次，false循环播放。
 * boolean isRunning()：当前AnimationDrawable是否正在播放。
 * void setOneShot(boolean oneShot)：设置AnimationDrawable是否执行一次，true执行一次，false循环播放
 */
public class FrameAnimationActivity extends AppCompatActivity {

    private ImageView iv;
    private Button bt;
    private AnimationDrawable framAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frame_animation);

        iv = findViewById(R.id.iv);
        bt = findViewById(R.id.bt);
        //                方式一
//                framAnimation = (AnimationDrawable) iv.getDrawable();

        //                方式二
        framAnimation = new AnimationDrawable();
        framAnimation.addFrame(getDrawable(R.drawable.icon6),200);
        framAnimation.addFrame(getDrawable(R.drawable.icon5),200);
        framAnimation.addFrame(getDrawable(R.drawable.icon4),200);
        framAnimation.addFrame(getDrawable(R.drawable.icon3),200);
        framAnimation.addFrame(getDrawable(R.drawable.icon2),200);
        framAnimation.addFrame(getDrawable(R.drawable.icon1),200);
        framAnimation.setOneShot(false);
        iv.setImageDrawable(framAnimation);

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (framAnimation != null && !framAnimation.isRunning()){
                    framAnimation.start();
                    return;
                }
                if (framAnimation != null && framAnimation.isRunning()){
                    framAnimation.stop();
                    return;
                }
            }
        });
    }
}