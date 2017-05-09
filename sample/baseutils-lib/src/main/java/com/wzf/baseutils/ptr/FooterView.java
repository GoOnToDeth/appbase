package com.wzf.baseutils.ptr;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

/**
 * ================================================
 * 描    述：加载更多的View的父类
 * 作    者：wzf
 * 创建日期：2017/3/29
 * 版    本：1.0
 * 修订历史：
 * ================================================
 */
public abstract class FooterView extends FrameLayout {

    public final static int STATE_LOADING = 0;
    public final static int STATE_COMPLETE = 1;
    public final static int STATE_NOMORE = 2;

    public FooterView(Context context) {
        super(context);
        init();
    }

    public FooterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FooterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FooterView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private final void init() {
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setLayoutParams(layoutParams);
        View footerView = onCreateFooterView();
        if (footerView != null)
            addView(footerView);
    }

    public final void setState(int state) {
        switch (state) {
            case STATE_LOADING:
                this.setVisibility(View.VISIBLE);
                break;
            case STATE_COMPLETE:
                this.setVisibility(View.GONE);
                break;
            case STATE_NOMORE:
                this.setVisibility(View.VISIBLE);
                break;
        }
        setStatus(state);
    }

    /**
     * 下拉的View
     *
     * @return
     */
    public abstract View onCreateFooterView();

    protected abstract void setStatus(int state);
}
