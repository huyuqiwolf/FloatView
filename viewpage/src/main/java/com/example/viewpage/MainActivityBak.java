//package com.example.viewpage;
//
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.util.Log;
//import android.view.animation.LinearInterpolator;
//import android.widget.ImageView;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.viewpager.widget.ViewPager;
//
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.load.resource.bitmap.CircleCrop;
//import com.bumptech.glide.request.RequestOptions;
//
//import java.lang.reflect.Field;
//import java.util.ArrayList;
//import java.util.List;
//
//public class MainActivityBak extends AppCompatActivity {
//    private static final String TAG = "MainActivity";
//    private ViewPager viewPager;
//    private LoopImageAdapter adapter;
//    private static List<String> list = new ArrayList<>();
//    private ImageView imageView;
//    private int mCurrent;
//    private Handler handler = new Handler(new Handler.Callback() {
//        @Override
//        public boolean handleMessage(@NonNull Message message) {
//            viewPager.setCurrentItem(++mCurrent,true);
//            handler.sendEmptyMessageDelayed(1,3000);
//            return true;
//        }
//    });
//
//    static {
//        list.add("https://img-blog.csdnimg.cn/20201103175430643.png");
//        list.add("https://img-blog.csdnimg.cn/20201027152422823.png");
//        list.add("https://img-blog.csdnimg.cn/20201027145747611.jpg");
//        list.add("https://img-blog.csdnimg.cn/20201103175753890.jpg");
//        list.add("https://img-blog.csdnimg.cn/20201103174119832.jpg");
//    }
//
//    private List<ImageView> imageViews;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        viewPager = findViewById(R.id.viewpager);
//        imageView = findViewById(R.id.image);
//        imageViews = getList();
//        adapter = new LoopImageAdapter(imageViews);
//        viewPager.setAdapter(adapter);
//        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            private int mPosition;
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                adapter.setAlpha(position,positionOffset);
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                mPosition = position;
//                Log.d(TAG, "onPageSelected: "+position);
//                //
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//                Log.d(TAG, "onPageScrollStateChanged: "+state);
//                if(state == ViewPager.SCROLL_STATE_IDLE){
//                    Glide.with(imageView).load(MainActivityBak.list.get(adapter.getRealPosition(mPosition))).into(imageView);
//                }
//            }
//        });
//
//        Glide.with(imageView).load(MainActivityBak.list.get(0)).into(imageView);
//        bindSpeed(viewPager,2000);
//        mCurrent =0;
//        handler.sendEmptyMessageDelayed(1,3000);
//    }
//
//    private void bindSpeed(ViewPager viewPager, int duration) {
//        try {
//            Field mScroller = ViewPager.class.getDeclaredField("mScroller");
//            mScroller.setAccessible(true);
//            ViewPagerScroller viewPagerScroller  = new ViewPagerScroller(viewPager.getContext(),new LinearInterpolator());
//            viewPagerScroller.setDuration(duration);
//            mScroller.set(viewPager,viewPagerScroller);
//        } catch (NoSuchFieldException | IllegalAccessException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        handler.removeCallbacks(null);
//    }
//
//    private List<ImageView> getList() {
//        List<ImageView> imgs = new ArrayList<>();
//        for (int i = 0; i < list.size(); i++) {
//            ImageView imageView = new ImageView(this);
//            Glide.with(imageView).load(list.get(i)).apply(RequestOptions.bitmapTransform(
//                    new CircleCrop()
//            )).into(imageView);
//            imgs.add(imageView);
//        }
//        return imgs;
//    }
//}