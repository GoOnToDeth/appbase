package com.wzf.baseutils.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;

/**
 * ================================================
 * 描    述：带滑动监听的ScrollView
 * 作    者：wzf
 * 创建日期：2016/7/13
 * 版    本：1.0
 * 修订历史：
 * ================================================
 */
public class ObservableScrollView extends ScrollView {

    /**
     * 向下滑
     */
    public static final int SCROLL_DOWN = 0;
    /**
     * 向上滑
     */
    public static final int SCROLL_UP = 1;
    /**
     * ScrollView滑到底部
     */
    public static final int SCROLL_BOTTOM = 2;
    /**
     * ScrollView滑到顶部
     */
    public static final int SCROLL_TOP = 3;

    /**
     * 最小的滑动距离
     */
    private static final int SCROLLLIMIT = 5;

    private OnScrollerListener listener;

    public ObservableScrollView(Context context) {
        super(context);
    }

    public ObservableScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ObservableScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ObservableScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (listener == null)
            return;
        int scrollY = getScrollY();
        int childHeight = getChildAt(0).getHeight();
        if (oldt > t && oldt - t > SCROLLLIMIT) {// 向上滑动
            listener.onScroll(SCROLL_UP);
        } else if (oldt < t && t - oldt > SCROLLLIMIT) {// 向下滑动
            listener.onScroll(SCROLL_DOWN);
        } else {
            if (scrollY == 0) {
                listener.onScroll(SCROLL_TOP);
            } else if ((scrollY + getHeight()) == childHeight) {
                listener.onScroll(SCROLL_BOTTOM);
            }
        }
    }

    public void setOnScrollerListener(OnScrollerListener listener) {
        this.listener = listener;
    }

    public interface OnScrollerListener {
        void onScroll(int oritention);
    }
}
