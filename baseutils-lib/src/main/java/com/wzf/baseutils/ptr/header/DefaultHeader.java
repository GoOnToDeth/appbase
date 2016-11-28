package com.wzf.baseutils.ptr.header;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.wzf.baseutils.R;
import com.wzf.baseutils.ptr.PtrLayout;
import com.wzf.baseutils.ptr.PtrUIHandler;
import com.wzf.baseutils.ptr.indicator.PtrIndicator;

/**
 * ================================================
 * 描    述：默认的Header
 * 作    者：wzf
 * 创建日期：2016/7/28
 * 版    本：1.0
 * 修订历史：
 * ================================================
 */
public class DefaultHeader extends FrameLayout implements PtrUIHandler {

    public DefaultHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(attrs);
    }

    public DefaultHeader(Context context) {
        super(context);
        initViews(null);
    }

    public DefaultHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DefaultHeader(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initViews(attrs);
    }

    protected void initViews(AttributeSet attrs) {
        View header = LayoutInflater.from(getContext()).inflate(R.layout.cube_ptr_classic_default_header, this);
    }

    @Override
    public void onUIReset(PtrLayout frame) {

    }

    @Override
    public void onUIRefreshPrepare(PtrLayout frame) {

    }

    @Override
    public void onUIRefreshBegin(PtrLayout frame) {

    }

    @Override
    public void onUIRefreshComplete(PtrLayout frame) {

    }

    @Override
    public void onUIPositionChange(PtrLayout frame, boolean isUnderTouch, byte status, PtrIndicator ptrIndicator) {

    }
}
