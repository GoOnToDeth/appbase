package com.wzf.baseutils.widget.round;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * ================================================
 * 描    述：减少selector和部分shape文件
 * 作    者：wzf
 * 创建日期：2016/6/26
 * 版    本：1.0
 * 修订历史：
 * ================================================
 */
public class RoundFrameLayout extends FrameLayout {

    private RoundViewDelegate delegate;

    public RoundFrameLayout(Context context) {
        super(context);
        init(null);
    }

    public RoundFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public RoundFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RoundFrameLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        delegate = new RoundViewDelegate(getContext(), this, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (delegate.isWidthHeightEqual() && getMeasuredWidth() > 0 && getMeasuredHeight() > 0) {
            int max = Math.max(getMeasuredHeight(), getMeasuredWidth());
            int measureSpace = MeasureSpec.makeMeasureSpec(max, MeasureSpec.EXACTLY);
            super.onMeasure(measureSpace, measureSpace);
            return;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (delegate.isRadiusHalfHeight()) {
            delegate.setCornerRadius(getMeasuredHeight() / 2);
        } else {
            delegate.setBackGroundSelector();
        }
    }

    public RoundViewDelegate getDelegate() {
        return delegate;
    }
}
