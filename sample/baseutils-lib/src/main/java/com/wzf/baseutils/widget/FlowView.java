package com.wzf.baseutils.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * ================================================
 * 描    述：流式布局
 * 作    者：wzf
 * 创建日期：2016/7/18
 * 版    本：1.0
 * 修订历史：
 * ================================================
 */
public class FlowView extends ViewGroup {

    // 左对齐
    public static final int LEFT_ALIGN = 0;
    // 居中
    public static final int CENTER_ALIGN = 1;
    // 右对齐
    public static final int RIGHT_ALIGN = 2;

    private int orientation = LEFT_ALIGN;

    public FlowView(Context context) {
        super(context);
    }

    public FlowView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FlowView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FlowView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);

        int lineWith = 0, lineHeight = 0, maxWidth = 0, maxHeight = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            MarginLayoutParams params = (MarginLayoutParams) child.getLayoutParams();
            int childWith = child.getMeasuredWidth() + params.leftMargin + params.rightMargin;
            int childHeight = child.getMeasuredHeight() + params.topMargin + params.bottomMargin;
            if (childWith + lineWith > sizeWidth - getPaddingLeft() - getPaddingRight()) {
                // 对比得到最大宽度
                maxWidth = Math.max(maxWidth, lineWith);
                // 重置行宽
                lineWith = childWith;
                // 记录行高
                maxHeight += lineHeight;
                lineHeight = childHeight;
            } else {
                lineWith += childWith;
                lineHeight = Math.max(lineHeight, childHeight);
            }
            if (i == getChildCount() - 1) {
                maxWidth = Math.max(lineWith, maxWidth);
                maxHeight += lineHeight;
            }
            setMeasuredDimension(
                    modeWidth == MeasureSpec.EXACTLY ? sizeWidth : maxWidth + getPaddingLeft() + getPaddingRight(),
                    modeHeight == MeasureSpec.EXACTLY ? sizeHeight : maxHeight + getPaddingTop() + getPaddingBottom());
        }
    }

    private List<List<View>> mAllViews = new ArrayList<List<View>>();
    // 每一行的高度
    private List<Integer> mLineHeight = new ArrayList<Integer>();
    // 每一行距离左边的距离
    private List<Integer> mLineMargin = new ArrayList<Integer>();

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mAllViews.clear();
        mLineHeight.clear();
        mLineMargin.clear();
        calculateChild();
        /** 设置子View的位置 **/
        int left = getPaddingLeft(), top = getPaddingTop(), lineHeight;
        for (int i = 0; i < mAllViews.size(); i++) {
            List<View> lineViews = mAllViews.get(i);
            lineHeight = mLineHeight.get(i);
            left += mLineMargin.get(i);
            for (int j = 0; j < lineViews.size(); j++) {
                View child = lineViews.get(j);
                if (child.getVisibility() == View.GONE)
                    continue;
                MarginLayoutParams params = (MarginLayoutParams) child.getLayoutParams();
                int lc = left + params.leftMargin;
                int tc = top + params.topMargin;
                int rc = lc + child.getMeasuredWidth();
                int bc = tc + child.getMeasuredHeight();
                child.layout(lc, tc, rc, bc);
                left += child.getMeasuredWidth() + params.leftMargin
                        + params.rightMargin;
            }
            left = getPaddingLeft();
            top += lineHeight;
        }
    }

    private void calculateChild() {
        int lineWidth = 0, lineHeight = 0, width = getMeasuredWidth();
        List<View> lineViews = new ArrayList<>();
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            MarginLayoutParams params = (MarginLayoutParams) child.getLayoutParams();
            int childWith = child.getMeasuredWidth() + params.leftMargin + params.rightMargin;
            int childHeight = child.getMeasuredHeight() + params.topMargin + params.bottomMargin;
            if (childWith + lineWidth > width - getPaddingLeft() - getPaddingRight()) {
                // 计算每行距离左边距离
                int lineLeftMargin = getLeftMargin(lineWidth, width);
                mLineMargin.add(lineLeftMargin);
                Log.i("info", "" + lineLeftMargin);
                // 行高
                mLineHeight.add(lineHeight);
                // 行元素
                mAllViews.add(lineViews);
                // 重置参数
                lineWidth = 0;
                lineHeight = childHeight;
                lineViews = new ArrayList<View>();
            }
            lineWidth += childWith;
            lineHeight = Math.max(lineHeight, childHeight);
            lineViews.add(child);
        }
        // 处理最后一行
        mLineMargin.add(getLeftMargin(lineWidth, width));
        // 行高
        mLineHeight.add(lineHeight);
        mAllViews.add(lineViews);
    }

    private int getLeftMargin(int lineWidth, int width) {
        int lineLeftMargin = 0;
        switch (orientation) {
            case LEFT_ALIGN:
                lineLeftMargin = 0;
                break;
            case CENTER_ALIGN:
                lineLeftMargin = (width - lineWidth) / 2;
                break;
            case RIGHT_ALIGN:
                lineLeftMargin = width - lineWidth;
                break;
        }
        return lineLeftMargin;
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }
}
