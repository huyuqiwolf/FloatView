package com.example.floatview;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

public class FloatView2 extends FrameLayout {
    /**
     * 判断是否点击的时间阈值
     */
    private static final long TIME_THRESHOLD = 150;
    /**
     * 判断是否移动的位移阈值
     */
    private static final int MOVE_THRESHOLD = 100;
    private ConstraintLayout mRoot;
    private ImageView mIcon;
    private TextView mText;

    private int mTotalWidth;
    private int mTotalHeight;
    private int mLeftBound;
    private int mTopBound;
    private int mRightBound;
    private int mBottomBound;

    private boolean mAlignRight;
    private int mSlideOffset = 0;
    private boolean mIsScrolling = false;
    private int mDownX;
    private int mDownY;
    private int mLastX;
    private int mLastY;

    public FloatView2(@NonNull Context context) {
        this(context, null);
    }

    public FloatView2(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FloatView2(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.float_view2, this);
        mRoot = findViewById(R.id.root);
        mIcon = findViewById(R.id.icon);
        mText = findViewById(R.id.text);
        mAlignRight = !isMirrorLanguage();
        resetConstraintLayout();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return processTouchEvent(event);
    }

    private boolean processTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                processDown(event);
                break;
            case MotionEvent.ACTION_MOVE:
                processMove(event);
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                processUp(event);
                break;
            default:
        }
        return true;
    }

    private void processDown(MotionEvent event) {
        mDownX = (int) event.getRawX();
        mDownY = (int) event.getRawY();
        mLastX = mDownX;
        mLastY = mDownY;
    }

    private void processMove(MotionEvent event) {
        int offsetX = (int) event.getRawX() - mLastX;
        int offsetY = (int) event.getRawY() - mLastY;
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        if (layoutParams instanceof FrameLayout.LayoutParams) {
            LayoutParams params = ((FrameLayout.LayoutParams) layoutParams);
            // 这里如果需要移动时也不能越出上下边界，需要自己检查下边界后赋值
            params.topMargin += offsetY;
            if (mAlignRight) {
                params.rightMargin -= offsetX;
            } else {
                params.leftMargin += offsetX;
            }
            // 为避免出现截断问题，重新设置宽高
            params.width = mTotalWidth;
            params.height = mTotalHeight;
            setLayoutParams(params);
        }
        mLastX = (int) event.getRawX();
        mLastY = (int) event.getRawY();
    }

    private void processUp(MotionEvent event) {
        // 处理贴边
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        // 计算View在X轴方向的中心点
        int centerX = (int) (event.getRawX() - event.getX() + mTotalWidth / 2);
        if (centerX > mRightBound / 2) {
            mAlignRight = true;
        } else {
            mAlignRight = false;
        }
        // 处理上下边界
        int topY = (int) (event.getRawY() - event.getY());
        if (topY < mTopBound) {
            topY = mTopBound;
        } else if (topY + mTotalHeight > mBottomBound && mBottomBound > mTopBound + mTotalHeight) {
            topY = mBottomBound - mTotalHeight;
        }
        if (layoutParams instanceof FrameLayout.LayoutParams) {
            LayoutParams params = (LayoutParams) layoutParams;
            params.topMargin = topY;
            if (mAlignRight) {
                params.gravity = isMirrorLanguage() ? Gravity.START : Gravity.END;
                params.rightMargin = 0;
            } else {
                params.gravity = isMirrorLanguage() ? Gravity.END : Gravity.START;
                params.leftMargin = 0;
            }
            setLayoutParams(layoutParams);
        }
        // 处理点击
        if (isClick(event)) {
            performClick();
        }
        resetConstraintLayout();
    }

    private boolean isClick(MotionEvent event) {
        int offsetX = (int) (event.getRawX() - mDownX);
        int offsetY = (int) (event.getRawY() - mDownY);
        long time = event.getEventTime() - event.getDownTime();
        return Math.abs(offsetX) < MOVE_THRESHOLD && Math.abs(offsetY) < MOVE_THRESHOLD && Math.abs(time) < TIME_THRESHOLD;
    }

    /**
     * 重置布局，贴边后保证文字在外侧，图像在内侧
     */
    private void resetConstraintLayout() {
        ConstraintSet set = new ConstraintSet();
        set.clone(mRoot);
        if (mAlignRight) {
            if (isMirrorLanguage()) {
                set.connect(R.id.text, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
                set.connect(R.id.text, ConstraintSet.END, R.id.icon, ConstraintSet.START);
                set.connect(R.id.icon, ConstraintSet.START, R.id.text, ConstraintSet.END);
                set.connect(R.id.icon, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);
            } else {
                set.connect(R.id.text, ConstraintSet.START, R.id.icon, ConstraintSet.END);
                set.connect(R.id.text, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);
                set.connect(R.id.icon, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
                set.connect(R.id.icon, ConstraintSet.END, R.id.text, ConstraintSet.START);
            }
        } else {
            if (isMirrorLanguage()) {
                set.connect(R.id.text, ConstraintSet.START, R.id.icon, ConstraintSet.END);
                set.connect(R.id.text, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);
                set.connect(R.id.icon, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
                set.connect(R.id.icon, ConstraintSet.END, R.id.text, ConstraintSet.START);
            } else {
                set.connect(R.id.text, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
                set.connect(R.id.text, ConstraintSet.END, R.id.icon, ConstraintSet.START);
                set.connect(R.id.icon, ConstraintSet.START, R.id.text, ConstraintSet.END);
                set.connect(R.id.icon, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);
            }
        }
        set.applyTo(mRoot);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        this.mTotalHeight = getMeasuredHeight();
        this.mTotalWidth = getMeasuredWidth();
    }

    /**
     * 设置上下左右的边界
     *
     * @param left   左边界
     * @param top    上边界
     * @param right  右边界
     * @param bottom 下边界
     */
    public void setBounds(int left, int top, int right, int bottom) {
        this.mLeftBound = left;
        this.mTopBound = top;
        this.mRightBound = right;
        this.mBottomBound = bottom;
    }

    /**
     * 滚动时隐藏文字
     */
    public void hideText() {
        if (mIsScrolling) {
            return;
        }
        // 位移距离，保持显示头像
        if (mSlideOffset <= 0) {
            mSlideOffset = mText.getMeasuredWidth() + getResources().getDimensionPixelOffset(R.dimen.margin) * 2;
        }
        int offset = mAlignRight ? mSlideOffset : -mSlideOffset;
        ObjectAnimator animator = ObjectAnimator.ofFloat(this, "translationX", 0, offset);
        animator.setDuration(400);
        animator.start();
        mIsScrolling = true;
    }

    /**
     * 停止滚动时显示文字
     */
    public void showText() {
        if (mSlideOffset <= 0) {
            mSlideOffset = mText.getMeasuredWidth() + getResources().getDimensionPixelOffset(R.dimen.margin) * 2;
        }
        int offset = mAlignRight ? mSlideOffset : -mSlideOffset;
        ObjectAnimator animator = ObjectAnimator.ofFloat(this, "translationX", offset, 0);
        animator.setDuration(400);
        animator.start();
        mIsScrolling = false;
    }

    /**
     * 初始化位置信息，停靠在end方向
     */
    public void initLocation() {
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        if (layoutParams instanceof FrameLayout.LayoutParams) {
            FrameLayout.LayoutParams params = (LayoutParams) layoutParams;
            params.gravity = Gravity.END; // 默认就显示在end方向
            params.topMargin = 1000; // 此处根据实际需求计算或者填写
            setLayoutParams(params);
        }
    }

    /**
     * 判断是否是 RTL 布局
     *
     * @return true 是RTL布局
     */
    private boolean isMirrorLanguage() {
        return getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_RTL;
    }
}
