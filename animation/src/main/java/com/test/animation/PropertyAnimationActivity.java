package com.test.animation;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

/**
 * 属性动画 Property Animation（Android 3.0 之后加入）
 *
 * 重点在差值器Interpolator，估值器Evaluator，监听器UpdateListener之间的关系及设置
 */
public class PropertyAnimationActivity extends AppCompatActivity implements View.OnClickListener{

    private Button bt_translation, bt_rotate, bt_scal, bt_alpha;
    private ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_animation);

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
//                方式一
//                ObjectAnimator translation = ObjectAnimator.ofFloat(iv,"translationX", 200);
//                translation.setDuration(500);
//                translation.start();
//                方式二 设置属性值的方式(可以设置多个不同属性)
//                PropertyValuesHolder propertyValuesHolder1 = PropertyValuesHolder.ofFloat ("translationX",200);
//                PropertyValuesHolder propertyValuesHolder2 = PropertyValuesHolder.ofFloat ("translationY",200);
//                ObjectAnimator translation = ObjectAnimator.ofPropertyValuesHolder(iv,propertyValuesHolder1,propertyValuesHolder2);
//                translation.start();
//                方式三 关键帧的方式
//                Keyframe keyframe1 = Keyframe.ofFloat(0,0);
//                Keyframe keyframe2 = Keyframe.ofFloat(0.5f,200);
//                Keyframe keyframe3 = Keyframe.ofFloat(1,170);
//                PropertyValuesHolder propertyValuesHolder = PropertyValuesHolder.ofKeyframe("translationX",keyframe1,keyframe2,keyframe3);
//                ObjectAnimator translation = ObjectAnimator.ofPropertyValuesHolder(iv,propertyValuesHolder);
//                translation.setDuration(600);
//                translation.start();

//                ValueAnimator valueAnimator1 = ValueAnimator.ofInt(100,400);
//                valueAnimator1.setDuration(600);
//                valueAnimator1.start();
//                valueAnimator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                    @Override
//                    public void onAnimationUpdate(ValueAnimator animation) {
//                        float animatedFraction = animation.getAnimatedFraction();
//                        int animatedValue = (int) animation.getAnimatedValue();
//                        iv.setTranslationX(animatedValue);
////                        Log.e("MSG","animatedFraction=="+animatedFraction+"//animatedValue=="+animatedValue);
////                        Log.e("MSG",""+600*animatedFraction);
//                    }
//                });

//                ValueAnimator valueAnimator1 = ValueAnimator.ofFloat(100,400);
//                valueAnimator1.setDuration(600);
//                valueAnimator1.start();
//                valueAnimator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                    @Override
//                    public void onAnimationUpdate(ValueAnimator animation) {
//                        float animatedFraction = animation.getAnimatedFraction();
//                        float animatedValue = (float) animation.getAnimatedValue();
//                        iv.setTranslationX(animatedValue);
//                    }
//                });

                /**
                 * 差值器Interpolation只和时间有关
                 * 估值器Evaluator中的方法就是根据差值器Interpolation返回的百分比和初始及结束值进行计算（算法可自定义）
                 * 监听器addUpdateListener的getAnimatedFraction值就是差值器的返回值，getAnimatedValue就是估值器Evaluator的返回值
                 */
//                ValueAnimator valueAnimator = ValueAnimator.ofObject(new CharEvaluater(), 'a', 'z');
                ValueAnimator valueAnimator = ValueAnimator.ofFloat(100, 500);
                valueAnimator.setDuration(200);
                //设置差值器
                valueAnimator.setInterpolator(new TimeInterpolator() {
                    @Override
                    public float getInterpolation(float input) {
//                        Log.e("MSG", "input==" + input);
                        return input;
                    }
                });
                //设置估值器
                valueAnimator.setEvaluator(new TypeEvaluator<Float>() {
                    @Override
                    public Float evaluate(float fraction, Float startValue, Float endValue) {
                        float result = startValue + fraction * (endValue - startValue);
                        Log.d("MSG", "evaluate==" + result);
                        return result;
                    }
                });
                valueAnimator.start();
                //设置监听
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
//                        char animatedValue = (char) animation.getAnimatedValue();
//                        Log.e("MSG", "animatedFraction==" + animation.getAnimatedFraction());

                        Log.d("MSG", "animatedValue==" + animation.getAnimatedValue());
                    }
                });
                break;
            case R.id.bt_rotate:
//                ObjectAnimator rotate = ObjectAnimator.ofFloat(iv, "rotation", 200);
//                rotate.setDuration(500);
//                rotate.start();


                // 载入XML动画
                ValueAnimator animator = (ValueAnimator) AnimatorInflater.loadAnimator(PropertyAnimationActivity.this, R.animator.set_animator);
                // 设置动画对象
                animator.setTarget(iv);
                // 启动动画
                animator.start();
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
//                        iv.setTranslationX((Float) animation.getAnimatedValue());
//                        iv.setAlpha((Float) animation.getAnimatedValue());
//                        iv.setRotation((Float) animation.getAnimatedValue());
                        iv.setRotationX((Float) animation.getAnimatedValue());
                    }
                });

                break;
            case R.id.bt_scal:
                ObjectAnimator scal = ObjectAnimator.ofFloat(iv, "scaleX", 2);
                scal.setDuration(500);
                scal.start();
                break;
            case R.id.bt_alpha:
                ObjectAnimator alpha = ObjectAnimator.ofFloat(iv, "alpha", 0.3f);
                alpha.setDuration(500);
                alpha.start();
                break;
        }
    }

    private class CharEvaluater implements TypeEvaluator<Character> {

        @Override
        public Character evaluate(float fraction, Character startValue, Character endValue) {
            int result = (int) (startValue + fraction * (endValue - startValue));
            Log.e("MSG", "fraction==" + fraction);
            return (char) result;
        }
    }

//    1.AnimatorSet 组合动画
//
//    独立的动画能够实现的视觉效果毕竟是相当有限的，因此将多个动画组合到一起播放就显得尤为重要
//    使用：
//
//            AnimatorSet.play(Animator anim)   ：播放当前动画
//AnimatorSet.after(long delay)   ：将现有动画延迟x毫秒后执行
//AnimatorSet.with(Animator anim)   ：将现有动画和传入的动画同时执行
//AnimatorSet.after(Animator anim)   ：将现有动画插入到传入的动画之后执行
//AnimatorSet.before(Animator anim) ：  将现有动画插入到传入的动画之前执行
//
//
//            实例
//    Java方式
//
//    ObjectAnimator a1 = ObjectAnimator.ofFloat(view, "alpha", 1.0f, 0f);
//    ObjectAnimator a2 = ObjectAnimator.ofFloat(view, "translationY", 0f, viewWidth);
//......
//    AnimatorSet animSet = new AnimatorSet();
//animSet.setDuration(5000);
//animSet.setInterpolator(new LinearInterpolator());
////animSet.playTogether(a1, a2, ...); //两个动画同时执行
//animSet.play(a1).after(a2); //先后执行
//......//其他组合方式
//        animSet.start();
//
//    XML方式
//            <set
//    android:ordering=["together" | "sequentially"]>
//
//    <objectAnimator
//    android:propertyName="string"
//    android:duration="int"
//    android:valueFrom="float | int | color"
//    android:valueTo="float | int | color"
//    android:startOffset="int"
//    android:repeatCount="int"
//    android:repeatMode=["repeat" | "reverse"]
//    android:valueType=["intType" | "floatType"]/>
//
//    <animator
//    android:duration="int"
//    android:valueFrom="float | int | color"
//    android:valueTo="float | int | color"
//    android:startOffset="int"
//    android:repeatCount="int"
//    android:repeatMode=["repeat" | "reverse"]
//    android:valueType=["intType" | "floatType"]/>
//
//    <set>
//        ...
//    </set>
//</set>
//
//            2. 监听动画
//
//    监听到动画的各种事件，比如动画何时开始，何时结束，然后在开始或者结束的时候去执行一些逻辑处理
//    Animator类提供addListener()方法，说明其子类都可以使用该方法(关于继承关系，前面我们提到过了)
//    使用方法
//
//anim.addListener(new AnimatorListener() {
//        @Override
//        public void onAnimationStart(Animation animation) {
//            //动画开始时执行
//        }
//
//        @Override
//        public void onAnimationRepeat(Animation animation) {
//            //动画重复时执行
//        }
//
//        @Override
//        public void onAnimationCancel()(Animation animation) {
//            //动画取消时执行
//        }
//
//        @Override
//        public void onAnimationEnd(Animation animation) {
//            //动画结束时执行
//        }
//    });
//
//// 特别注意：每次监听必须4个方法都重写。
//
//
//    缺点：
//    很多时候我们并不想要监听那么多个事件，可能我只想要监听动画结束这一个事件，那么每次都要将四个接口全部实现一遍就显得非常繁琐。
//    如何解决
//    采用动画适配器（AnimatorListenerAdapter），解决 实现接口繁琐 的问题
//
//anim.addListener(new AnimatorListenerAdapter() {
//// 向addListener()方法中传入适配器对象AnimatorListenerAdapter()
//// 由于AnimatorListenerAdapter中已经实现好每个接口
//// 所以这里不实现全部方法也不会报错
//        @Override
//        public void onAnimationStart(Animator animation) {
//            // 如想只想监听动画开始时刻，就只需要单独重写该方法就可以
//        }
//    });
//
//    作者：whd_Alive
//    链接：https://www.jianshu.com/p/a480ca619dd9
//    来源：简书
//    简书著作权归作者所有，任何形式的转载都请联系作者获得授权并注明出处。
}