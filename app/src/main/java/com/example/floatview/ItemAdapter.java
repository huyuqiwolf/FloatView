package com.example.floatview;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.Holder> {
    private static final String TAG = "ItemAdapter";
    private List<String> list;

    public ItemAdapter() {
        list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add("Item " + i);
        }
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "item " + view.getTag());
            }
        });
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.textView.setText(list.get(position));
        holder.itemView.setTag(position);
        int color;
        switch (position % 3) {
            case 0:
                color = R.color.purple_200;
                break;
            case 1:
                color = R.color.purple_500;
                break;
            case 2:
            default:
                color = R.color.purple_700;
                break;
        }
        holder.cardView.setCardBackgroundColor(holder.itemView.getContext().getColor(color));
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView textView;
        CardView cardView;

        public Holder(@NonNull View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
            textView = itemView.findViewById(R.id.textview);
        }
    }
}
