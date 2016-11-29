package com.wzf.baseutils.ptr;

import com.wzf.baseutils.ptr.indicator.PtrIndicator;

/**
 * ================================================
 * 描    述：刷新开始的监听
 * 作    者：wzf
 * 版    本：1.0
 * ================================================
 */
public interface PtrUIHandler {

    /**
     * Content 重新回到顶部， Header 消失，整个下拉刷新过程完全结束以后，重置 View 。
     *
     * @param frame
     */
    public void onUIReset(PtrLayout frame);

    /**
     * 准备刷新，Header 将要出现时调用。
     *
     * @param frame
     */
    public void onUIRefreshPrepare(PtrLayout frame);

    /**
     * 开始刷新，Header 进入刷新状态之前调用。
     */
    public void onUIRefreshBegin(PtrLayout frame);

    /**
     * 刷新结束，Header 开始向上移动之前调用。
     */
    public void onUIRefreshComplete(PtrLayout frame);

    /**
     * 下拉过程中位置变化回调。
     *
     * @param frame
     * @param isUnderTouch
     * @param status
     * @param ptrIndicator
     */
    public void onUIPositionChange(PtrLayout frame, boolean isUnderTouch, byte status, PtrIndicator ptrIndicator);

}
