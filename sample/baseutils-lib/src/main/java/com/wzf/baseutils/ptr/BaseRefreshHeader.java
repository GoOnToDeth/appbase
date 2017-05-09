package com.wzf.baseutils.ptr;

interface BaseRefreshHeader {

    int STATE_NORMAL = 0;
    int STATE_RELEASE_TO_REFRESH = 1;    // 拖拽距离超过标准距离
    int STATE_START_REFRESH = 2;            // 准备刷新
    int STATE_REFRESH_DONE = 3;                    // 刷新完成

    void onMove(float delta);

    boolean releaseAction();

    void refreshComplete();

}