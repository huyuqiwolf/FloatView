package com.example.viewpage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

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


    private RecyclerView recyclerView;
    private boolean change;
    private LoopAdapter adapter;
    private List<String> data;
    private LinearLayoutManager manager;
    private Listener listener = new Listener();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.image);
        recyclerView = findViewById(R.id.recycler);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                change = !change;
                data = getList(change);
                Log.d(TAG, "onClick" + data);
                adapter.setData(data);
                adapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(0);
                listener.reset();
                Glide.with(imageView).load(data.get(0)).into(imageView);
            }
        });
        adapter = new LoopAdapter();
        data = getList(change);
        adapter.setData(data);
        manager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(manager);
       // manager.setChildCount(adapter.getRealCount());
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(listener);
    }


    private class Listener extends RecyclerView.OnScrollListener{
        private int total;
        private int mLast = -1;

        public void reset(){
            total = 0;
            mLast = -1;
        }

        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            total += dx;
            int position = total / recyclerView.getWidth();
            Log.e(TAG,"po "+position);
            if (mLast != position) {
                mLast = position;
                String url = adapter.getRealUrl(mLast);
                Glide.with(imageView).load(url).into(imageView);
            }
            float alpha = total * 1f % recyclerView.getWidth() / recyclerView.getWidth();
            View now = manager.findViewByPosition(position);
            // Log.e(TAG, "now null"+now);
            if (now != null) {
                now.setAlpha(1 - alpha);
            } else {
                //Log.e(TAG, "now null");
            }
            View pre = manager.findViewByPosition(position-1);
            if (pre != null && pre.getAlpha() != 1f) {
                pre.setAlpha(1);
            } else {
                // Log.e(TAG, "pre null");
            }

            View next = manager.findViewByPosition(position + 1);
            if (next != null) {
                next.setAlpha(alpha);
            } else {
                // Log.e(TAG, "next null");
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    private List<String> getList(boolean reverse) {
        if (reverse) {
            List<String> imgs = new ArrayList<>();
            for (int i = list.size() - 1; i >= 0; i--) {
                imgs.add(list.get(i));
            }
            return imgs;
        } else {
            return list;
        }
    }

    @Override
    public void changeBackground(int position) {
        if (imageView != null) {
            Glide.with(imageView).load(list.get(position)).into(imageView);
        }
    }
}