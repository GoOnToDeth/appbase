package com.wzf.baseutils.ptr.indicator;

/**
 * ================================================
 * 描    述：
 * 作    者：wzf
 * 创建日期：2016/7/27
 * ================================================
 */
public class PtrIndicator {

    // 阻力系数
    private float mResistance = 1.7f;
    // 刷新时移动位置的比例
    private float mRatioOfHeaderHeightToRefresh = 1.2f;
    // header的高度
    private int headerHeight;
    // 当前header的偏移值
    private int currentPos = 0;

    // 刷新时移动的位置
    protected int mOffsetToRefresh = 0;

    public float getResistance() {
        return mResistance;
    }

    public void setResistance(float mResistance) {
        this.mResistance = mResistance;
    }

    public float getRatioOfHeaderHeightToRefresh() {
        return mRatioOfHeaderHeightToRefresh;
    }

    public void setRatioOfHeaderHeightToRefresh(float mRatioOfHeaderHeightToRefresh) {
        this.mRatioOfHeaderHeightToRefresh = mRatioOfHeaderHeightToRefresh;
        updateOffset();
    }

    public int getHeaderHeight() {
        return headerHeight;
    }

    public void setHeaderHeight(int headerHeight) {
        this.headerHeight = headerHeight;
        updateOffset();
    }

    public int getCurrentPos() {
        return currentPos;
    }

    public void setCurrentPos(int currentPos) {
        this.currentPos = currentPos;
    }

    protected void updateOffset() {
        mOffsetToRefresh = (int) (mRatioOfHeaderHeightToRefresh * headerHeight);
    }
}
