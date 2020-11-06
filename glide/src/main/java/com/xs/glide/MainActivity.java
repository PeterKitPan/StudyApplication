package com.xs.glide;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

public class MainActivity extends AppCompatActivity {
    private ImageView iv_glide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iv_glide = findViewById(R.id.iv_glide);
        RequestOptions options = new RequestOptions()
                .placeholder(R.mipmap.holder) //加载成功之前占位图
                .error(R.mipmap.error)  //加载错误之后的错误图
                .override(400, 400)  //指定图片的尺寸
                //指定图片的缩放类型为fitCenter （等比例缩放图片，宽或者是高等于ImageView的宽或者是高。）
                .fitCenter()
                //指定图片的缩放类型为centerCrop （等比例缩放图片，直到图片的狂高都大于等于ImageView的宽度，然后截取中间的显示。）
                .centerCrop()
                .circleCrop()//指定图片的缩放类型为centerCrop （圆形）
                .skipMemoryCache(true)   //跳过内存缓存
                //.diskCacheStrategy(DiskCacheStrategy.ALL)		//缓存所有版本的图像
                .diskCacheStrategy(DiskCacheStrategy.NONE)        //跳过磁盘缓存
                //.diskCacheStrategy(DiskCacheStrategy.DATA)		//只缓存原来分辨率的图片
                //.diskCacheStrategy(DiskCacheStrategy.RESOURCE)	//只缓存最终的图片
                ;
//        http://img0.imgtn.bdimg.com/it/u=186602880,1005592543&fm=214&gp=0.jpg
//        http://5b0988e595225.cdn.sohucs.com/images/20180906/29ec52507146411fac500d6c9f46d086.jpeg
        Glide.with(this)
                .load("http://5b0988e595225.cdn.sohucs.com/images/20180906/29ec52507146411fac500d6c9f46d086.jpeg")
                .apply(options)
                .into(iv_glide);
    }
}
