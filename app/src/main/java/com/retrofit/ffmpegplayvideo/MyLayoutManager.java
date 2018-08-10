package com.retrofit.ffmpegplayvideo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class MyLayoutManager extends LinearLayoutManager implements RecyclerView.OnChildAttachStateChangeListener {
    //位移，用来判断移动方向
    private int mDrift;

    private PagerSnapHelper mPagerSnapHelper;
    private OnViewPagerListener mOnViewPagerListener;

    public MyLayoutManager(Context context) {
        super(context);
    }

    public MyLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
        mPagerSnapHelper = new PagerSnapHelper();
    }

    @Override
    public void onAttachedToWindow(RecyclerView view) {

        view.addOnChildAttachStateChangeListener(this);
        mPagerSnapHelper.attachToRecyclerView(view);
        super.onAttachedToWindow(view);
    }

    /**
     * 当Item添加进来了  调用这个方法
     * 播放视频操作 即将要播放的是上一个视频 还是下一个视频
     * @param view
     */
    @Override
    public void onChildViewAttachedToWindow(@NonNull View view) {
        if (mDrift > 0) {
//            向上
            if (mOnViewPagerListener != null) {
                mOnViewPagerListener.onPageSelected(getPosition(view), true);
            }

        } else {
            if (mOnViewPagerListener != null) {
                mOnViewPagerListener.onPageSelected(getPosition(view), false);
            }
        }
    }

    public void setOnViewPagerListener(OnViewPagerListener mOnViewPagerListener) {
        this.mOnViewPagerListener = mOnViewPagerListener;
    }

    /**
     * OnScrollListener.SCROLL_STATE_FLING; //屏幕处于甩动状态
     * OnScrollListener.SCROLL_STATE_IDLE; //停止滑动状态
     * OnScrollListener.SCROLL_STATE_TOUCH_SCROLL;// 手指接触状态
     *
     * @param state
     */
    @Override
    public void onScrollStateChanged(int state) {
        switch (state) {
            case RecyclerView.SCROLL_STATE_IDLE:
                View view = mPagerSnapHelper.findSnapView(this);
                int position = getPosition(view);
                if (mOnViewPagerListener != null) {
                    mOnViewPagerListener.onPageSelected(position, position == getItemCount() - 1);

                }


                break;
            default:
        }
        super.onScrollStateChanged(state);
    }

    /**
     * 暂停播放操作
     * @param view
     */
    @Override
    public void onChildViewDetachedFromWindow(@NonNull View view) {

        if (mDrift >= 0) {
            if (mOnViewPagerListener != null) {
                mOnViewPagerListener.onPageRelease(true, getPosition(view));
            }
        } else {
            if (mOnViewPagerListener != null) {
                mOnViewPagerListener.onPageRelease(false, getPosition(view));
            }
        }
    }


    /**
     * 监听移动方向
     * @param dy
     * @param recycler
     * @param state
     * @return
     */
    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        this.mDrift = dy;
        return super.scrollVerticallyBy(dy, recycler, state);
    }

    @Override
    public boolean canScrollVertically() {
        return true;
    }
}
