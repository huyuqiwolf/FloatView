package com.example.floatview;

import android.view.MotionEvent;

public class Location {
    /**
     * 按下时X坐标
     */
    public float mDownXInWindow;
    /**
     * 按下时Y坐标
     */
    public float mDownYInWindow;
    /**
     * 按下时在View中的X
     */
    public float mDownXInView;
    /**
     * 按下时在View中的Y
     */
    public float mDownYInView;
    /**
     * 当前手指在屏幕上的X
     */
    public float mCurrXInWindow;
    /**
     * 当前手指在屏幕上的Y
     */
    public float mCurrYInWindow;

    /**
     * 重置位置信息
     */
    public void reset() {
        mDownXInView = -1;
        mDownYInView = -1;
        mDownXInWindow = -1;
        mDownYInWindow = -1;
    }

    /**
     * 更新按下坐标
     *
     * @param event event
     */
    public void updateDown(MotionEvent event) {
        mDownYInWindow = event.getRawY();
        mDownXInWindow = event.getRawX();
        mDownXInView = event.getX();
        mDownYInView = event.getY();
    }

    /**
     * 更新移动时坐标
     *
     * @param event event
     */
    public void updateMove(MotionEvent event) {
        mCurrXInWindow = event.getRawX();
        mCurrYInWindow = event.getRawY();
    }

    /**
     * 更新抬起时坐标
     *
     * @param event
     */
    public void setUp(MotionEvent event) {
        mCurrXInWindow = event.getRawX();
        mCurrYInWindow = event.getRawY();
    }
}
