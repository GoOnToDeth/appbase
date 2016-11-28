package com.wzf.baseutils.widget.titlebar;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.FrameLayout;

/**
 * ================================================
 * 描    述：渐变的背景颜色
 * 作    者：wzf
 * 创建日期：2016/7/12
 * 版    本：1.0
 * 修订历史：
 * ================================================
 */
public class FadeView extends FrameLayout {

    private static final int DEFAULT_HEIGHT = 48;

    private static final int MAX_ALPHA = 255;

    private int width, height;
    private int bgColor = Color.BLACK;
    // 开始渐变位置
    private int mStartFadePosition = 50;
    // 结束渐变位置
    private int mEndFadePosition = 380;
    // 渐变区域
    private int mFadeArea = mEndFadePosition - mStartFadePosition;

    private int lastAlpha = -1;

    private OnAlphaChangeListener alphaChangeListener;

    public FadeView(Context context) {
        super(context);
        init();
    }

    public FadeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FadeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FadeView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        width = getScreenWidth(getContext());
        height = dip2px(DEFAULT_HEIGHT);
        setTranslate(0);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);

        setMeasuredDimension(modeWidth != MeasureSpec.EXACTLY ? width : sizeWidth,
                modeHeight != MeasureSpec.EXACTLY ? height : sizeHeight);
    }

    private void setTranslate(int alpha) {
        if (getBackground() == null)
            return;
        if (lastAlpha != alpha) {
            getBackground().mutate().setAlpha(alpha);
            if (alphaChangeListener != null)
                alphaChangeListener.alphaChange(alpha);
            lastAlpha = alpha;
        }
    }

    private int calcuateAlpha(int delta) {
        if (delta <= mStartFadePosition) {
            return 0;
        } else if (delta > mStartFadePosition && delta < mEndFadePosition) {
            return ((delta - mStartFadePosition) * MAX_ALPHA / mFadeArea);
        } else
            return MAX_ALPHA;
    }

    public void onFade(int y) {
        int alpha = calcuateAlpha(y);
        setTranslate(alpha);
    }

    public void onFade(AbsListView absListView) {
        int delta = getScrollY(absListView);
        onFade(delta);
    }

    private int getScrollY(AbsListView view) {
        View c = view.getChildAt(0);
        if (c == null) {
            return 0;
        }
        int firstVisiblePosition = view.getFirstVisiblePosition();
        int top = c.getTop();
        return -top + firstVisiblePosition * c.getHeight();
    }

    public void setTransparentDistance(int start, int end) {
        mStartFadePosition = start;
        mEndFadePosition = end;
        mFadeArea = end - start;
    }

    private int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.x;
    }

    private int dip2px(int dpValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public void setOnAlphaChangeListener(OnAlphaChangeListener listener) {
        this.alphaChangeListener = listener;
    }

    public interface OnAlphaChangeListener {
        void alphaChange(int alpha);
    }
}
