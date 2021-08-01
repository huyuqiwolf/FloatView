package com.example.floatview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

public class FloatView extends FrameLayout {
    private static final String TAG = "FloatView";
    private static final int CLICK = 100;
    private static final int MOVE = 10;
    private Context mContext;
    private ImageView mIcon;
    private ConstraintLayout mRoot;
    private TextView mText;
    private int mLeft, mTop, mRight, mBottom;
    private Location mLocation;
    // 默认垂直居中，靠右
    private int mGravity = Gravity.END;
    // 默认的高度
    private int mDefaultTop = 0;
    /**
     * 按下的时间
     */
    private long mDownTime;
    /**
     * 标记是否整个过程中发生过移动
     */
    private boolean mHasMoved;
    private int mFixedWidth;
    private int mFixedHeight;
    private boolean mAlignLeft;

    public FloatView(@NonNull Context context) {
        this(context, null);
    }

    public FloatView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FloatView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init() {
        inflate(mContext, R.layout.float_layout, this);
        mRoot = findViewById(R.id.root);
        mIcon = findViewById(R.id.icon);
        mText = findViewById(R.id.text);
        mLocation = new Location();
    }

    public void reset() {

    }

    public void setBounds(int left, int top, int right, int bottom) {
        this.mLeft = left;
        this.mRight = right;
        this.mTop = top;
        this.mBottom = bottom;
        //l 0 r 1080 t 120 b 1554
        Log.e(TAG, "l " + mLeft + " r " + mRight + " t " + mTop + " b " + mBottom);
    }

    // 处理移动事件
    private void processMove() {
        // left
        int x = (int) (mLocation.mCurrXInWindow - mLocation.mDownXInView);
        // top
        int y = (int) (mLocation.mCurrYInWindow - mLocation.mDownYInView);
        Log.d(TAG, "y " + y + " ,b " + mBottom + " t " + mTop + " x " + x);
        if (!mHasMoved) {
            mHasMoved = isMoved();
        }
        int top = getTop();
        int bottom = getBottom();
        int left = getLeft();
        int right = getRight();
        Log.d(TAG, "l " + left + " t " + top + " b " + bottom + " r " + right);
        if (y < mTop) {
            y = mTop;
        } else if (y + getHeight() > mBottom) {
            y = mBottom - getHeight();
        }
        // 判断镜像语言
        if (isMirrorLanguage()) {
            x = mRight - x;
            if (x > mRight) {

            } else if (x < mLeft) {

            }
        } else {
            if (x < mLeft) {
                x = mLeft;
            } else if (x + mIcon.getWidth() + mText.getWidth() > mRight) {
                x = mRight - mIcon.getWidth() - mText.getWidth();
            }
        }

        Log.d(TAG, "wi" + getWidth());
        FrameLayout.LayoutParams layoutParams = (LayoutParams) getLayoutParams();
        if (layoutParams != null) {
            layoutParams.topMargin = y;
            if (isMirrorLanguage()) {
                layoutParams.rightMargin = x;
            } else {
                layoutParams.leftMargin = x;
            }
        }
        setLayoutParams(layoutParams);
        if (isMoved() && mText != null && mText.getVisibility() == VISIBLE) {
            // 避免宽度变化
            mText.setVisibility(INVISIBLE);
        }
    }

    // 处理抬起事件，1。 是否是点击2。贴边停靠
    private void processUp() {
        if (mText != null && mText.getVisibility() != VISIBLE) {
            mText.setVisibility(VISIBLE);
        }
        if (isClicked()) {
            performClick();
            return;
        }
        // 改变text的位置
        int x = (int) (mLocation.mCurrXInWindow - mLocation.mDownXInView + mIcon.getWidth() / 2);
        Log.d(TAG, "processUp: " + x + " " + mAlignLeft);
        if (mAlignLeft) {
            x = x + mText.getWidth();
        }
        if (x > mRight / 2) {
            //right
            mAlignLeft = false;
            x = isMirrorLanguage() ? 0 : mRight - mIcon.getWidth() - mText.getWidth();
        } else {
            //left
            mAlignLeft = true;
            x = isMirrorLanguage() ? mRight - mIcon.getWidth() - mText.getWidth() : 0;
        }
        Log.d(TAG, "processUp: " + x + " " + mAlignLeft);
        int y = (int) (mLocation.mCurrYInWindow - mLocation.mDownYInView);
        Log.d(TAG, "processUp: " + y);
        if (y < mTop) {
            y = mTop;
        } else if (y + getHeight() > mBottom) {
            y = mBottom - getHeight();
        }
        Log.d(TAG, "processUp: " + y);
        FrameLayout.LayoutParams layoutParams = (LayoutParams) getLayoutParams();
        if (layoutParams != null) {
            layoutParams.topMargin = y;
            if (isMirrorLanguage()) {
                layoutParams.rightMargin = x;
            } else {
                layoutParams.leftMargin = x;
            }
        }
        setLayoutParams(layoutParams);
        // 重设布局
        resetLayout();
    }

    private void resetLayout() {
        ConstraintSet set = new ConstraintSet();
        set.clone(mRoot);
//        if (isMirrorLanguage()) {
//            if (!mAlignLeft) {
//                set.connect(R.id.text, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
//                set.connect(R.id.text, ConstraintSet.END, R.id.icon, ConstraintSet.START);
//                set.connect(R.id.icon, ConstraintSet.START, R.id.text, ConstraintSet.END);
//                set.connect(R.id.icon, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);
//            } else {
//                set.connect(R.id.icon, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
//                set.connect(R.id.icon, ConstraintSet.END, R.id.text, ConstraintSet.START);
//                set.connect(R.id.text, ConstraintSet.START, R.id.icon, ConstraintSet.END);
//                set.connect(R.id.text, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);
//            }
//        } else {
            if ((mAlignLeft && !isMirrorLanguage()) || (isMirrorLanguage() && !mAlignLeft)) {
                set.connect(R.id.text, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
                set.connect(R.id.text, ConstraintSet.END, R.id.icon, ConstraintSet.START);
                set.connect(R.id.icon, ConstraintSet.START, R.id.text, ConstraintSet.END);
                set.connect(R.id.icon, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);
            } else {
                set.connect(R.id.icon, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
                set.connect(R.id.icon, ConstraintSet.END, R.id.text, ConstraintSet.START);
                set.connect(R.id.text, ConstraintSet.START, R.id.icon, ConstraintSet.END);
                set.connect(R.id.text, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);
            }
//        }
        set.applyTo(mRoot);
    }

    /**
     * 判断是否移动过
     *
     * @return true 移动，false 没有移动
     */
    private boolean isMoved() {
        return !(Math.abs(mLocation.mCurrXInWindow - mLocation.mDownXInWindow) < MOVE && Math.abs(mLocation.mCurrYInWindow
                - mLocation.mDownYInWindow) < MOVE
        );
    }

    /**
     * 判断是否点击
     *
     * @return true 点击，false 未点击
     */
    private boolean isClicked() {
        long time = System.currentTimeMillis() - mDownTime;
        Log.d(TAG, "isClicked: " + isMoved() + " " + mHasMoved + " " + time);
        if (mHasMoved) {
            return false;
        }
        return !isMoved() && (time < CLICK);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLocation.updateDown(event);
                mDownTime = System.currentTimeMillis();
                mHasMoved = false;
                break;
            case MotionEvent.ACTION_MOVE:
                mLocation.updateMove(event);
                processMove();
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mLocation.setUp(event);
                processUp();
                break;
            default:
        }
        return true;
    }


    /**
     * 初始化位置信息
     */
    public void initLocation() {
        FrameLayout.LayoutParams layoutParams = (LayoutParams) getLayoutParams();
        if (layoutParams == null) {
            int width = getMeasuredWidth();
            int height = getMeasuredHeight();
            layoutParams = new FrameLayout.LayoutParams(width, height);
            Log.d(TAG, "create new layout params");
        }
        layoutParams.topMargin = mDefaultTop;
        setLayoutParams(layoutParams);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mFixedWidth = getMeasuredWidth();
        mFixedHeight = getMeasuredHeight();
    }

    /**
     * 判断是否是镜像语言
     *
     * @return true 镜像语言
     */
    private boolean isMirrorLanguage() {
        boolean rtl =
                getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_RTL;
        return rtl;
    }
}
