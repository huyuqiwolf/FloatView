package com.example.viewpage;

import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

public class MyLayoutManager extends LinearLayoutManager {
    private int childCount;
    public MyLayoutManager(Context context) {
        super(context,LinearLayoutManager.HORIZONTAL,false);
    }

    @Override
    public View findViewByPosition(int position) {
        final int childCount = getChildCount();
        if (childCount == 0) {
            return null;
        }
        final int firstChild = getPosition(getChildAt(0));
        final int viewPosition = position - firstChild;
        if (viewPosition >= 0 && viewPosition < childCount) {
            final View child = getChildAt(viewPosition);
            if (getPosition(child) == position) {
                return child; // in pre-layout, this may not match
            }
        }
        // fallback to traversal. This might be necessary in pre-layout.
        return super.findViewByPosition(position);
    }

    @Override
    public int getChildCount() {
        return childCount;
    }

    public void setChildCount(int count){
        this.childCount =count;
    }
}
