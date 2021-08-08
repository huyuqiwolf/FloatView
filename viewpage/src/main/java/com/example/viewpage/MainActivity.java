package com.example.viewpage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AutoScrollViewPager.PagerScrollListener {
    private static final String TAG = "MainActivity";
    private static List<String> list = new ArrayList<>();
    static {
        list.add("https://img-blog.csdnimg.cn/20201103175430643.png");
        list.add("https://img-blog.csdnimg.cn/20201027152422823.png");
        list.add("https://img-blog.csdnimg.cn/20201027145747611.jpg");
        list.add("https://img-blog.csdnimg.cn/20201103175753890.jpg");
        list.add("https://img-blog.csdnimg.cn/20201103174119832.jpg");
    }
    private ImageView imageView;

    private LoopImageAdapter adapter;


    private List<ImageView> imageViews;
    private AutoScrollViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.image);
        mViewPager = findViewById(R.id.autoPager);
        imageViews = getList();
        adapter = new LoopImageAdapter(imageViews);
        mViewPager.setAdapter(adapter);
        mViewPager.setPageScrollListener(this);
        mViewPager.init(2000,4000);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private List<ImageView> getList() {
        List<ImageView> imgs = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            ImageView imageView = new ImageView(this);
            Glide.with(imageView).load(list.get(i)).apply(RequestOptions.bitmapTransform(
                    new CircleCrop()
            )).into(imageView);
            imgs.add(imageView);
        }
        return imgs;
    }

    @Override
    public void changeBackground(int position) {
        if(imageView!=null){
            Glide.with(imageView).load(list.get(position)).into(imageView);
        }
    }
}