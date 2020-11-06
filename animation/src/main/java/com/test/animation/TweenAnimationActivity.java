package com.test.animation;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;

/**
 * 补间动画 Tween Animation
 *
 * 公共属性
 * XML属 性：android:duration             
 * 关联方法：setDuration(long)           
 * 说明：动画持续时间， 默认值是0 (单位ms)                          
 *
 * XML属 性：android:fillAfter             
 * 关联方法：setFillAfter(boolean)           
 * 说明：表示动画结束后是否保留动画后的状态，true保留动画后状态，false恢复原来状态，默认值是false                          
 *
 * XML属 性：android:fillBefore           
 * 关联方法：setFillBefore(boolean)       
 * 说明：表示动画结束后是否保留动画前的状态，true恢复原来状态，false保留动画后状态，默认值是true
 *
 * XML属 性：android:fillEnabled           
 * 关联方法：setFillEnabled(boolean)       
 * 说明：如果设置为true，将fillBefore设置考虑在内                      
 *
 * XML属 性：android:interpolator         
 * 关联方法：setInterpolator(Interpolator) 
 * 说明：设置动画的变化速率 即插值器，改变动画变换的速度，默认值是@android:animator/accelerate_decelerate_interpolator，即加速减速插值器，在动画开始和结束的时速度较慢，中间时候加速                      
 *
 * XML属 性：android:repeatCount       
 * 关联方法：setRepeatCount(int)         
 * 说明：设置动画重复执行的次数  ，默认值是0                          
 *  
 * XML属 性：android:repeatMode       
 * 关联方法：setRepeatMode(int)           
 * 说明：设置动画重复的模式，其值可以有，restart( 1 )，表示顺序播放，reverse（2）表示重复的时候逆向播放                         
 *  
 * XML属 性：android:startOffset       
 * 关联方法：setStartOffset(long)         
 * 说明：设置开始的延迟的时间(单位ms)  ，默认值是0                 
 *  
 * 坐标类型:
 * Animation.ABSOLUTE : 绝对数值(默认以px为单位)
 * Animation.RELATIVE_TO_SELF : 百分数,如:50% (以当前视图的宽度或高度为基数来计算)   
 * Animation.RELATIVE_TO_PARENT : 百分数+p,如:50%p (以父视图的宽度或高度为基数来计算)
 *
 * 设置监听：view.setAnimationListener(AnimationListener)
 * 启动动画 : view.startAnimation(animation);
 * 结束动画: view.clearAnimation();
 */
public class TweenAnimationActivity extends AppCompatActivity implements View.OnClickListener{

    private Button bt_translation, bt_rotate, bt_scal, bt_alpha;
    private ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tween_animation);

        bt_translation = findViewById(R.id.bt_translation);
        bt_rotate = findViewById(R.id.bt_rotate);
        bt_scal = findViewById(R.id.bt_scal);
        bt_alpha = findViewById(R.id.bt_alpha);
        iv = findViewById(R.id.iv);

        bt_translation.setOnClickListener(this);
        bt_rotate.setOnClickListener(this);
        bt_scal.setOnClickListener(this);
        bt_alpha.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_translation:
////                方式一
//                Animation translationAnimation = AnimationUtils.loadAnimation(TweenAnimationActivity.this, R.animator.anim_translate);
//                iv.startAnimation(translationAnimation);

//                方式二
                TranslateAnimation translateAnimation = new TranslateAnimation(0, 150, 0, 150);
                translateAnimation.setFillAfter(true);
                translateAnimation.setDuration(300);
                iv.startAnimation(translateAnimation);
                break;
            case R.id.bt_rotate:
//                方式一
//                Animation rotateAnimation = AnimationUtils.loadAnimation(TweenAnimationActivity.this, R.animator.anim_rotate);
//                iv.startAnimation(rotateAnimation);

//                方式二
                RotateAnimation rotateAnimation1 = new RotateAnimation(0, 30,
                        Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f);
//                        Animation.RELATIVE_TO_PARENT,0.5f,
//                        Animation.RELATIVE_TO_PARENT,0.5f);
                rotateAnimation1.setFillAfter(true);
                rotateAnimation1.setDuration(300);
                iv.startAnimation(rotateAnimation1);
                break;
            case R.id.bt_scal:
//                方式一
//                Animation scaleAnimation1 = AnimationUtils.loadAnimation(TweenAnimationActivity.this, R.animator.anim_scale);
//                iv.startAnimation(scaleAnimation1);

//                方式二
                ScaleAnimation scaleAnimation = new ScaleAnimation(0.2f, 1.5f,
                        0.2f, 1.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f);
                scaleAnimation.setFillAfter(true);
                scaleAnimation.setDuration(300);
                iv.startAnimation(scaleAnimation);
                break;
            case R.id.bt_alpha:
//                方式一
//                Animation alphaAnimation = AnimationUtils.loadAnimation(TweenAnimationActivity.this, R.animator.anim_alpha);
//                iv.startAnimation(alphaAnimation);

//                方式二
                AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
                alphaAnimation.setDuration(1500);
                alphaAnimation.setFillAfter(true);
                iv.startAnimation(alphaAnimation);
                break;
        }
    }
}