package com.wzf.baseutils.ptr;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wzf.baseutils.R;
import com.wzf.baseutils.ptr.indicator.PtrIndicator;

/**
 * ================================================
 * 描    述：
 * 作    者：王智凡
 * 创建日期：2016/7/27
 * 版    本：1.0
 * 修订历史：
 * ================================================
 */
public class PtrLayout extends ViewGroup {

    private int mHeaderId = 0;
    private int mContainerId = 0;
    private int mDurationToClose = 200;
    private int mDurationToCloseHeader = 1000;
    private boolean mKeepHeaderWhenRefresh = true;
    private boolean mPullToRefresh = false;

    protected View mContent;
    private View mHeaderView;
    private PtrIndicator mPtrIndicator;

    public PtrLayout(Context context) {
        super(context);
        init(null);
    }

    public PtrLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public PtrLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        mPtrIndicator = new PtrIndicator();
        TypedArray arr = getContext().obtainStyledAttributes(attrs, R.styleable.PtrFrameLayout, 0, 0);
        if (arr == null) return;
        mHeaderId = arr.getResourceId(R.styleable.PtrFrameLayout_ptr_header, mHeaderId);
        mContainerId = arr.getResourceId(R.styleable.PtrFrameLayout_ptr_content, mContainerId);
        mDurationToClose = arr.getInt(R.styleable.PtrFrameLayout_ptr_duration_to_close, mDurationToClose);
        mDurationToCloseHeader = arr.getInt(R.styleable.PtrFrameLayout_ptr_duration_to_close_header, mDurationToCloseHeader);
        mKeepHeaderWhenRefresh = arr.getBoolean(R.styleable.PtrFrameLayout_ptr_keep_header_when_refresh, mKeepHeaderWhenRefresh);
        mPullToRefresh = arr.getBoolean(R.styleable.PtrFrameLayout_ptr_pull_to_fresh, mPullToRefresh);

        float ratio = mPtrIndicator.getRatioOfHeaderHeightToRefresh();
        ratio = arr.getFloat(R.styleable.PtrFrameLayout_ptr_ratio_of_header_height_to_refresh, ratio);
        mPtrIndicator.setRatioOfHeaderHeightToRefresh(ratio);
        mPtrIndicator.setResistance(arr.getFloat(R.styleable.PtrFrameLayout_ptr_resistance, mPtrIndicator.getResistance()));

        arr.recycle();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        final int childCount = getChildCount();
        switch (childCount) {
            case 0:
                TextView errorView = new TextView(getContext());
                errorView.setClickable(true);
                errorView.setTextColor(0xffff6600);
                errorView.setGravity(Gravity.CENTER);
                errorView.setTextSize(20);
                errorView.setText("The content view in PtrFrameLayout is empty. Do you forget to specify its id in xml layout file?");
                mContent = errorView;
                addView(mContent);
                break;
            case 1:
                mContent = getChildAt(0);
                break;
            case 2:
                if (mHeaderId != 0 && mHeaderView == null) mHeaderView = findViewById(mHeaderId);
                if (mContainerId != 0 && mContent == null) mContent = findViewById(mContainerId);
                if (mHeaderView == null || mContent == null) {
                    View child1 = getChildAt(0);
                    View child2 = getChildAt(1);
                    if (child1 instanceof PtrUIHandler) {
                        mHeaderView = child1;
                        mContent = child2;
                    } else if (child2 instanceof PtrUIHandler) {
                        mHeaderView = child2;
                        mContent = child1;
                    } else {    // 表示没有header和content都没有设置资源ID
                        if (mHeaderView == null && mContent == null) {
                            mHeaderView = child1;
                            mContent = child2;
                        } else { // 表示有header和content中一个设置了资源ID
                            if (mHeaderView == null) {
                                mHeaderView = mContent == child1 ? child2 : child1;
                            } else {
                                mContent = mHeaderView == child1 ? child2 : child1;
                            }
                        }
                    }
                }
                break;
            default:
                throw new IllegalStateException("PtrFrameLayout only can include 2 elements");
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (mHeaderView != null) {
            mHeaderView.measure(widthMeasureSpec, heightMeasureSpec);
            MarginLayoutParams params = (MarginLayoutParams) mHeaderView.getLayoutParams();
            int mHeaderHeight = mHeaderView.getMeasuredHeight() + params.topMargin + params.bottomMargin;
            mPtrIndicator.setHeaderHeight(mHeaderHeight);
        }
        if (mContent != null) {
            final MarginLayoutParams lp = (MarginLayoutParams) mContent.getLayoutParams();
            final int childWidthMeasureSpec = getChildMeasureSpec(widthMeasureSpec,
                    getPaddingLeft() + getPaddingRight() + lp.leftMargin + lp.rightMargin, lp.width);
            final int childHeightMeasureSpec = getChildMeasureSpec(heightMeasureSpec,
                    getPaddingTop() + getPaddingBottom() + lp.topMargin, lp.height);
            mContent.measure(childWidthMeasureSpec, childHeightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int offsetX = mPtrIndicator.getCurrentPos();
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        if (mHeaderView != null) {
            MarginLayoutParams lp = (MarginLayoutParams) mHeaderView.getLayoutParams();
            int left = paddingLeft + lp.leftMargin;
            int top = paddingTop + lp.topMargin + offsetX - mPtrIndicator.getHeaderHeight();
            int right = left + mHeaderView.getMeasuredWidth();
            int bottom = top + mHeaderView.getMeasuredHeight();
            mHeaderView.layout(left, top, right, bottom);
        }
        if (mContent != null) {
            MarginLayoutParams lp = (MarginLayoutParams) mContent.getLayoutParams();
            int left = paddingLeft + lp.leftMargin;
            int top = paddingTop + lp.topMargin + offsetX;
            int right = left + mContent.getMeasuredWidth();
            int bottom = top + mContent.getMeasuredHeight();
            mContent.layout(left, top, right, bottom);
        }
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (!isEnabled() || mHeaderView == null || mContent == null)
            return super.dispatchTouchEvent(ev);
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
}
