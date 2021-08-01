package com.example.floatview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private TextView add, remove;
    private FloatView floatView;
    private int mTop, mLeft, mBottom, mRight;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        add = findViewById(R.id.add);
        remove = findViewById(R.id.remove);
        recyclerView = findViewById(R.id.recycler);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add();
            }
        });
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                remove();
            }
        });
        getWindow().getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (floatView == null) {
                    Log.d(TAG, "float view is null");
                    return;
                }
                int[] pos = new int[2];
                mBottom = remove.getTop();
                mTop = add.getBottom();
                mLeft = 0;
                mRight = add.getRight();
                add.getLocationInWindow(pos);
                mTop = pos[1]+add.getHeight();
                remove.getLocationInWindow(pos);
                mBottom = pos[1];
                floatView.setBounds(mLeft, mTop, mRight, mBottom);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(new ItemAdapter());
        boolean res = getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_RTL;
        Toast.makeText(this," "+res,Toast.LENGTH_LONG).show();
    }

    private void add() {
        if (floatView == null) {
            floatView = new FloatView(this);
            Log.d(TAG, "create new float view");
        }
        if (floatView.getParent() == getWindow().getDecorView()) {
            Log.d(TAG, "added");
            return;
        }
        floatView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this,"click",Toast.LENGTH_SHORT).show();
            }
        });
        View decorView = getWindow().getDecorView();
        if (decorView instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) decorView;
            Log.d(TAG, "widht " + floatView.getMeasuredWidth() + " " + floatView.getWidth() + ",height " + floatView.getMeasuredHeight() + " " + floatView.getHeight());
            group.addView(floatView, FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
            floatView.initLocation();
            floatView.setBounds(mLeft, mTop, mRight, mBottom);
            Log.d(TAG, "add view finished");
        }
    }

    private void remove() {
        if (floatView == null) {
            Log.d(TAG, "float view is null");
            return;
        }
        if (!(floatView.getParent() instanceof ViewGroup)) {
            Log.d(TAG, "float view has no parent");
            return;
        }
        ViewGroup group = (ViewGroup) floatView.getParent();
        group.removeView(floatView);
        Log.d(TAG, "float view removed");
    }
}