package com.example.viewpage;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.LinearInterpolator;
import android.widget.Scroller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;

/**
 * 自动滚动的ViewPager
 */
public class AutoScrollViewPager extends ViewPager {
    private static final String TAG = "AutoScrollViewPager";
    private static final int DEFAULT_SCROLL_DURATION = 2000;
    private static final long DEFAULT_DELAY  = 3000L;
    private int mScrollDuration = DEFAULT_SCROLL_DURATION;
    private long mDelayDuration = DEFAULT_DELAY;
    private AutoScrollTask mScrollTask;
    private Context mContext;
    private LinearScroller mLinearScroller;
    private int mCurrentPosition;

    private PagerScrollListener mListener;

    public interface PagerScrollListener {
        void changeBackground(int position);
    }

    public AutoScrollViewPager(@NonNull Context context) {
        super(context);
        mContext = context;
        initListener();
    }

    public AutoScrollViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initListener();
    }

    /**
     * 刷新数据时重新调用一次
     * @param scrollDuration 滚动时间
     * @param delayDuration 滚动周期
     */
    public void init(int scrollDuration,int delayDuration) {
        mCurrentPosition = 0;

        setScrollDuration(scrollDuration,delayDuration);
        // 初始化自动滚动，滚动速度
        mLinearScroller = new LinearScroller(mContext);
        mLinearScroller.setDuration(mScrollDuration);

        // 反射绑定
        bindSpeed();
        // 清除之前的自动滚动事件
        if(mScrollTask !=null){
            removeCallbacks(mScrollTask);
        }

        mScrollTask = new AutoScrollTask(this);
        mScrollTask.setDelay(mDelayDuration);

        // 执行
        postDelayed(mScrollTask,mDelayDuration);

        if(mListener!=null){
            mListener.changeBackground(0);
        }
        Log.d(TAG,"post ");
    }

    public void setPageScrollListener(PagerScrollListener listener){
        this.mListener = listener;
    }

    private void initListener(){
        this.addOnPageChangeListener(new OnPageChangeListener() {
            int mPosition;
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                PagerAdapter adapter = getAdapter();
                if(adapter instanceof LoopImageAdapter){
                    ((LoopImageAdapter) adapter).setAlpha(position,positionOffset);
                }
            }

            @Override
            public void onPageSelected(int position) {
                // 不间断滑动
                mPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if(state == SCROLL_STATE_IDLE && mListener!=null && getAdapter() instanceof LoopImageAdapter){
                    int position = ((LoopImageAdapter)getAdapter()).getRealPosition(mPosition);
                    mListener.changeBackground(position);
                }
            }
        });
    }

    private void bindSpeed(){
        try {
            Field mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            LinearScroller scroller  = new LinearScroller(mContext);
            scroller.setDuration(mScrollDuration);
            mScroller.set(this,scroller);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置滚动时间和周期，周期一定不比滚动时间小
     * @param scrollDuration 滚动时间
     * @param delayDuration 周期
     */
    private void setScrollDuration(int scrollDuration,long delayDuration){
        if(scrollDuration==0||delayDuration == 0 || scrollDuration>delayDuration){
            throw new IllegalArgumentException("delayDuration must bigger than scrollDuration");
        }
        this.mScrollDuration = scrollDuration;
        this.mDelayDuration = delayDuration;
    }

    /**
     * 自动滚动的帮助类
     */
    private class AutoScrollTask implements Runnable{
        private final WeakReference<AutoScrollViewPager> mPager;
        private long mDelay;
        public AutoScrollTask(AutoScrollViewPager pager) {
            this.mPager = new WeakReference<>(pager);
        }

        public void setDelay(long delay){
            this.mDelay = delay;
        }

        @Override
        public void run() {
            if(mPager.get()!=null){
                mPager.get().setCurrentItem(++mCurrentPosition,true);
                mPager.get().postDelayed(this,mDelay);
                Log.d(TAG,"run post");
            }
            Log.d(TAG,"run");
        }
    }

    /**
     * 匀速滚动
     */
    private static class LinearScroller extends Scroller {
        private int mDuration;
        public LinearScroller(Context context) {
            super(context,new LinearInterpolator());
        }

        /**
         * 设置滚动时间
         * @param duration 滚动时间
         */
        public void setDuration(int duration){
            this.mDuration = duration;
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy) {
            super.startScroll(startX, startY, dx, dy,mDuration);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, mDuration);
        }
    }
}
