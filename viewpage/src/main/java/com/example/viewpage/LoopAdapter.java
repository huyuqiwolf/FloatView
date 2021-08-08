package com.example.viewpage;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class LoopAdapter extends RecyclerView.Adapter<LoopAdapter.Holder> {
    private List<String> list;

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        int real = getRealPosition(position);
        if (real < 0) {
            return;
        }
        Glide.with(holder.imageView).load(list.get(real)).apply(RequestOptions.bitmapTransform(new CircleCrop())).into(holder.imageView);
    }

    public int getRealPosition(int position) {
        int realCount = getRealCount();
        if (realCount == 0) {
            return -1;
        }
        return position % realCount;
    }

    public String getRealUrl(int position){
        int realPosition = getRealPosition(position);
        if(realPosition <0){
            return null;
        }
        return list.get(realPosition);
    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }

    public int getRealCount() {
        return list == null ? 0 : list.size();
    }

    public void setData(List<String> list) {
        this.list = list;
    }

    protected static class Holder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public Holder(@NonNull View itemView) {
            super(itemView);
            imageView = (ImageView) itemView;
        }
    }
}
