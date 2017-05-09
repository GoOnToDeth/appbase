package com.wzf.baseutils.ptr;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * ================================================
 * 描    述：
 * 作    者：wzf
 * 创建日期：2017/3/29
 * 版    本：1.0
 * 修订历史：
 * ================================================
 */
public abstract class HeaderView extends FrameLayout implements BaseRefreshHeader {

    public HeaderView(Context context) {
        super(context);
        initView();
    }

    public HeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public HeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public HeaderView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    private View mContainer;
    protected int mState = STATE_NORMAL;
    protected int mMeasuredHeight;

    private void initView() {
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setLayoutParams(layoutParams);
        mContainer = onCreateHeaderView();
        if (mContainer != null) {
            addView(mContainer, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0));
        }
        measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mMeasuredHeight = getMeasuredHeight();
    }

    @Override
    public void onMove(float delta) {
        int visiableHeight = getVisibleHeight();
        if (visiableHeight > 0 || delta > 0) {
            setVisibleHeight((int) delta + visiableHeight);
            if (mState <= STATE_RELEASE_TO_REFRESH) { // 未处于刷新状态，更新箭头
                if (getVisibleHeight() > mMeasuredHeight) {
                    updateState(STATE_RELEASE_TO_REFRESH);
                } else {
                    updateState(STATE_NORMAL);
                }
            }
        }
    }

    @Override
    public boolean releaseAction() {
        boolean isOnRefresh = false;
        int height = getVisibleHeight();
        if (height == 0) // not visible.
            isOnRefresh = false;

        if (getVisibleHeight() > mMeasuredHeight && mState < STATE_START_REFRESH) {
            updateState(STATE_START_REFRESH);
            isOnRefresh = true;
        }
        if (mState != STATE_START_REFRESH)
            smoothScrollTo(0);
        else
            smoothScrollTo(mMeasuredHeight);
        return isOnRefresh;
    }

    @Override
    public void refreshComplete() {
        updateState(STATE_REFRESH_DONE);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                reset();
            }
        }, 200);
    }

    public final void updateState(int state) {
        setState(state);
        mState = state;
    }

    /**
     * 下拉的View
     *
     * @return
     */
    public abstract View onCreateHeaderView();

    /**
     * 根据状态设置UI，最后需要调用mState=state
     *
     * @param state
     */
    public abstract void setState(int state);

    public int getState() {
        return mState;
    }

    public void reset() {
        smoothScrollTo(0);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                updateState(STATE_NORMAL);
            }
        }, 500);
    }

    public void setVisibleHeight(int height) {
        if (height < 0) height = 0;
        LayoutParams lp = (LayoutParams) mContainer.getLayoutParams();
        lp.height = height;
        mContainer.setLayoutParams(lp);
    }

    public int getVisibleHeight() {
        LayoutParams lp = (LayoutParams) mContainer.getLayoutParams();
        return lp.height;
    }

    public final void smoothToMeasureHeight() {
        smoothScrollTo(mMeasuredHeight);
    }

    protected void smoothScrollTo(int destHeight) {
        ValueAnimator animator = ValueAnimator.ofInt(getVisibleHeight(), destHeight);
        animator.setDuration(300).start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                setVisibleHeight((int) animation.getAnimatedValue());
            }
        });
        animator.start();
    }
}
