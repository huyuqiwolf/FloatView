package com.example.viewpage;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

/**
 * 无限循环的image pager adapter
 */
public class LoopImageAdapter extends PagerAdapter {
    private List<ImageView> list;

    public LoopImageAdapter(List<ImageView> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        container.addView(getItem(position));
        return list.get(position%list.size());
    }

    /**
     * 获取adapter 中数据的真实个数
     * @return 真实item个数
     */
    public int getRealSize(){
        return list.size();
    }

    /**
     * 获取真实的位置
     * @param position 滚动位置
     * @return 真实位置
     */
    public int getRealPosition(int position){
        return position % list.size();
    }

    /**
     * 获取对应位置上的item
     * @param position 滚动位置
     * @return item
     */
    public ImageView getItem(int position){
        int temp = position % getRealSize();
        return list.get(temp);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView(getItem(position));
    }

    /**
     * 设置渐隐效果
     * @param position 滚动位置
     * @param alpha 透明度
     */
    public void setAlpha(int position, float alpha) {
        getItem(position).setAlpha(1-alpha);
        getItem(position+1).setAlpha(alpha);
    }
}
