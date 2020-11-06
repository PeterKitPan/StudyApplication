package com.test.roundimageview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;

import androidx.appcompat.widget.AppCompatImageView;

public class RoundImageView extends AppCompatImageView {
    /**
     * 常量：圆角的半径大小默认值为 0dp
     */
    private static final int DEFAULT_BORDER_RADIUS_SIZE = 0;

    /**
     * 画笔
     */
    private Paint mPaint;

    /**
     * 圆形半径
     */
    private int mBorderRadius, mTopLeftRadius, mTopRightRadius, mBottomLeftRadius, mBottomRightRadius;

    /**
     * 3x3 矩阵，主要用于缩小放大
     */
    private Matrix mMatrix;

    /**
     * View在屏幕中的位置信息
     */
    private RectF mRoundRect;

    public RoundImageView(Context context) {
        this(context, null);
    }

    /**
     * 构造器
     *
     * @param context 上下文
     */
    public RoundImageView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // 初始化画笔
        mPaint = new Paint();
        // 防止边缘出现锯齿
        mPaint.setAntiAlias(true);
        // 初始化矩阵
        mMatrix = new Matrix();

        // 获取参数
        TypedArray ta = context.obtainStyledAttributes(attrs,
                R.styleable.RoundImageView);
        mBorderRadius = ta.getDimensionPixelSize(
                R.styleable.RoundImageView_borderRadius,
                dp2px(DEFAULT_BORDER_RADIUS_SIZE));
        mTopLeftRadius = ta.getDimensionPixelSize(
                R.styleable.RoundImageView_borderRadius_top_left,
                dp2px(DEFAULT_BORDER_RADIUS_SIZE)) == 0 ? mBorderRadius :
                ta.getDimensionPixelSize(
                        R.styleable.RoundImageView_borderRadius_top_left,
                        dp2px(DEFAULT_BORDER_RADIUS_SIZE));
        mTopRightRadius = ta.getDimensionPixelSize(
                R.styleable.RoundImageView_borderRadius_top_right,
                dp2px(DEFAULT_BORDER_RADIUS_SIZE)) == 0 ? mBorderRadius :
                ta.getDimensionPixelSize(
                        R.styleable.RoundImageView_borderRadius_top_right,
                        dp2px(DEFAULT_BORDER_RADIUS_SIZE));
        mBottomLeftRadius = ta.getDimensionPixelSize(
                R.styleable.RoundImageView_borderRadius_bottom_left,
                dp2px(DEFAULT_BORDER_RADIUS_SIZE)) == 0 ? mBorderRadius :
                ta.getDimensionPixelSize(
                        R.styleable.RoundImageView_borderRadius_bottom_left,
                        dp2px(DEFAULT_BORDER_RADIUS_SIZE));
        mBottomRightRadius = ta.getDimensionPixelSize(
                R.styleable.RoundImageView_borderRadius_bottom_right,
                dp2px(DEFAULT_BORDER_RADIUS_SIZE)) == 0 ? mBorderRadius :
                ta.getDimensionPixelSize(
                        R.styleable.RoundImageView_borderRadius_bottom_right,
                        dp2px(DEFAULT_BORDER_RADIUS_SIZE));
        // 释放资源
        ta.recycle();
    }

    /**
     * 初次：onMeasure() -> onSizeChanged() -> onLayout() -> onDraw()
     * 以后：onMeasure() -> onLayout() -> onSizeChanged() -> onDraw()
     * 后者是在执行 onLayout() 方法时监听到宽高变化会执行 onSizeChanged()
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);
        // 圆角图片的范围
        mRoundRect = new RectF(getPaddingLeft(), getPaddingTop(), w-getPaddingRight(), h-getPaddingBottom());
    }

    /**
     * 绘画
     *
     * @param canvas 画布
     */
    @Override
    protected void onDraw(Canvas canvas) {
        // 没有设置图片：src 方式
        if (getDrawable() == null) {
            return;
        }

        // 设置渲染器
        initShader();
        // 画圆角矩形
//        canvas.drawRoundRect(mRoundRect, mBorderRadius, mBorderRadius, mPaint);
        Path path = new Path();
        path.addRoundRect(mRoundRect, new float[]{mTopLeftRadius, mTopLeftRadius,
                mTopRightRadius, mTopRightRadius,
                mBottomLeftRadius, mBottomLeftRadius,
                mBottomRightRadius, mBottomRightRadius}, Path.Direction.CCW);
        canvas.drawPath(path, mPaint);
    }

    /**
     * 初始化BitmapShader
     */
    private void initShader() {
        Drawable drawable = getDrawable();
        // 如果没有图片，就没必要渲染
        if (drawable == null) {
            return;
        }
        Bitmap bitmap = drawableToBitmap(drawable);
        if (bitmap == null) {
            return;
        }

        // 将bmp作为着色器，就是在指定区域内绘制bmp
        BitmapShader shader = new BitmapShader(bitmap, TileMode.CLAMP,
                TileMode.CLAMP);
        // 缩放比例
        float scaleX = 1.0f, scaleY = 1.0f;
        // 视图大小和图片大小不一致时计算比例，否则使用比例1.0f
        if (!(bitmap.getWidth() == getWidth()
                && bitmap.getHeight() == getHeight())) {
            scaleX = getWidth() * 1.0f / bitmap.getWidth();
            scaleY = getHeight() * 1.0f / bitmap.getHeight();
        }
        // shader的变换矩阵，我们这里主要用于放大或者缩小
        mMatrix.setScale(scaleX, scaleY);
        // 设置变换矩阵
        shader.setLocalMatrix(mMatrix);
        // 设置shader
        mPaint.setShader(shader);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
    }

    /**
     * drawable转bitmap
     *
     * @param drawable Drawable
     * @return Bitmap
     */
    private Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bd = (BitmapDrawable) drawable;
            return bd.getBitmap();
        }
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        if (w > 0 && h > 0) {
            Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, w, h);
            drawable.draw(canvas);
            return bitmap;
        }
        return null;
    }

    /**
     * 单位转换：dp 转 px
     */
    public int dp2px(int dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, getResources().getDisplayMetrics());
    }
}
