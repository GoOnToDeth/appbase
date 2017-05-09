package com.wzf.baseutils.ptr;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * ================================================
 * 描    述：
 * 作    者：wzf
 * 创建日期：2017/3/29
 * 版    本：1.0
 * 修订历史：
 * ================================================
 */
public class XRecyclerView extends RecyclerView {

    public XRecyclerView(Context context) {
        super(context);
        init();
    }

    public XRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public XRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    //下面的ItemViewType是保留值(ReservedItemViewType),如果用户的adapter与它们重复将会强制抛出异常。不过为了简化,我们检测到重复时对用户的提示是ItemViewType必须小于10000
    private static final int TYPE_REFRESH_HEADER = 10000;//设置一个很大的数字,尽可能避免和用户的adapter冲突
    private static final int TYPE_FOOTER = 10001;

    private static final float DRAG_RATE = 3;

    private boolean pullRefreshEnabled = true;
    private boolean loadingMoreEnabled = true;

    private HeaderView mRefreshHeader;
    private FooterView mFootView;

    private WrapAdapter mWrapAdapter;
    private final AdapterDataObserver mDataObserver = new DataObserver();

    private float mLastY = -1;
    private boolean isLoadingData = false;
    private boolean isNoMore = false;

    private LoadingListener mLoadingListener;

    private void init() {
    }

    @Override
    public void setAdapter(Adapter adapter) {
        if (pullRefreshEnabled && mRefreshHeader == null)
            throw new NullPointerException("pull refresh header is null");
        if (loadingMoreEnabled && mFootView == null)
            throw new NullPointerException("footer view is null");
        mWrapAdapter = new WrapAdapter(adapter);
        super.setAdapter(mWrapAdapter);
        adapter.registerAdapterDataObserver(mDataObserver);
        mDataObserver.onChanged();
    }

    //避免用户自己调用getAdapter() 引起的ClassCastException
    @Override
    public Adapter getAdapter() {
        if (mWrapAdapter != null)
            return mWrapAdapter.getOriginalAdapter();
        else
            return null;
    }

    @Override
    public void setLayoutManager(LayoutManager layout) {
        super.setLayoutManager(layout);
        if (mWrapAdapter != null) {
            if (layout instanceof GridLayoutManager) {
                final GridLayoutManager gridManager = ((GridLayoutManager) layout);
                gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        return (mWrapAdapter.isFooter(position) || mWrapAdapter.isRefreshHeader(position))
                                ? gridManager.getSpanCount() : 1;
                    }
                });
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mLastY == -1) {
            mLastY = ev.getRawY();
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastY = ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                final float deltaY = ev.getRawY() - mLastY;
                mLastY = ev.getRawY();
                if (isOnTop() && pullRefreshEnabled) {
                    mRefreshHeader.onMove(deltaY / DRAG_RATE);
                    if (mRefreshHeader.getVisibleHeight() > 0 && mRefreshHeader.getState() < BaseRefreshHeader.STATE_START_REFRESH) {
                        return false;
                    }
                }
                break;
            default:
                mLastY = -1; // reset
                if (isOnTop() && pullRefreshEnabled) {
                    if (mRefreshHeader.releaseAction()) {
                        if (mLoadingListener != null) {
                            mLoadingListener.onRefresh();
                        }
                    }
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 状态为0时：当前屏幕停止滚动；
     * 状态为1时：屏幕在滚动 且 用户仍在触碰或手指还在屏幕上；
     * 状态为2时：随用户的操作，屏幕上产生的惯性滑动；
     *
     * @param state
     */
    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        if (state == RecyclerView.SCROLL_STATE_IDLE && mLoadingListener != null && !isLoadingData && loadingMoreEnabled) {
            LayoutManager layoutManager = getLayoutManager();
            int lastVisibleItemPosition;
            if (layoutManager instanceof GridLayoutManager) {
                lastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                int[] into = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
                ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(into);
                lastVisibleItemPosition = findMax(into);
            } else {
                lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
            }
            // layoutManager.getChildCount()当前可视的 item 的数量
            if (layoutManager.getChildCount() > 0
                    && lastVisibleItemPosition >= layoutManager.getItemCount() - 1
                    && layoutManager.getItemCount() > layoutManager.getChildCount()
                    && !isNoMore && mRefreshHeader.getState() < BaseRefreshHeader.STATE_START_REFRESH) {
                isLoadingData = true;
                if (mFootView != null)
                    mFootView.setState(FooterView.STATE_LOADING);
                mLoadingListener.onLoadMore();
            }
        }
    }

    /**
     * 设置自定义的下拉刷新头
     *
     * @param mRefreshHeader
     */
    public void setRefreshHeader(HeaderView mRefreshHeader) {
        if (pullRefreshEnabled)
            this.mRefreshHeader = mRefreshHeader;
    }

    /**
     * 设置自定义的上拉加载更多
     *
     * @param footView
     */
    public void setFootView(final FooterView footView) {
        if (loadingMoreEnabled) {
            this.mFootView = footView;
            if (this.mFootView != null)
                this.mFootView.setVisibility(GONE);
        }
    }

    private boolean isOnTop() {
        if (mRefreshHeader.getParent() != null) {
            return true;
        } else {
            return false;
        }
    }

    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    public void loadMoreComplete() {
        if (!loadingMoreEnabled) return;
        isLoadingData = false;
        if (mFootView != null)
            mFootView.setState(FooterView.STATE_COMPLETE);
    }

    public void refreshComplete() {
        mRefreshHeader.refreshComplete();
        setNoMore(false);
    }

    public void setNoMore(boolean noMore) {
        if (!loadingMoreEnabled) return;
        isLoadingData = false;
        isNoMore = noMore;
        if (mFootView != null) {
            mFootView.setState(isNoMore ? FooterView.STATE_NOMORE : FooterView.STATE_COMPLETE);
        }
    }

    public void refresh() {
        if (pullRefreshEnabled && mLoadingListener != null) {
            mRefreshHeader.updateState(BaseRefreshHeader.STATE_START_REFRESH);
            mRefreshHeader.smoothToMeasureHeight();
            mLoadingListener.onRefresh();
        }
    }

    public void setPullRefreshEnabled(boolean enabled) {
        pullRefreshEnabled = enabled;
    }

    public void setLoadingMoreEnabled(boolean enabled) {
        loadingMoreEnabled = enabled;
        if (!enabled && mFootView != null) {
            mFootView.setState(FooterView.STATE_COMPLETE);
        }
    }

    public void setLoadingListener(LoadingListener listener) {
        mLoadingListener = listener;
    }

    public interface LoadingListener {

        void onRefresh();

        void onLoadMore();
    }

    class DataObserver extends AdapterDataObserver {

        @Override
        public void onChanged() {
            if (mWrapAdapter != null)
                mWrapAdapter.notifyDataSetChanged();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeInserted(positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeChanged(positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            mWrapAdapter.notifyItemRangeChanged(positionStart, itemCount, payload);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeRemoved(positionStart, itemCount);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            mWrapAdapter.notifyItemMoved(fromPosition, toPosition);
        }
    }

    class WrapAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private Adapter adapter;

        public WrapAdapter(Adapter adapter) {
            this.adapter = adapter;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (viewType) {
                case TYPE_REFRESH_HEADER:
                    return new SimpleViewHolder(mRefreshHeader);
                case TYPE_FOOTER:
                    return new SimpleViewHolder(mFootView);
                default:
                    return adapter.onCreateViewHolder(parent, viewType);
            }
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            if (isRefreshHeader(position) || isFooter(position)) return;
            if (adapter != null) {
                int adjPosition = position - 1;
                int adapterCount = adapter.getItemCount();
                if (adjPosition < adapterCount) {
                    adapter.onBindViewHolder(holder, adjPosition);
                }
            }
        }

        // sometimes we need to override this
        @Override
        public void onBindViewHolder(ViewHolder holder, int position, List<Object> payloads) {
            if (isRefreshHeader(position)) return;
            if (adapter != null) {
                int adjPosition = position - 1;
                int adapterCount = adapter.getItemCount();
                if (adjPosition < adapterCount) {
                    if (payloads.isEmpty())
                        adapter.onBindViewHolder(holder, adjPosition);
                    else
                        adapter.onBindViewHolder(holder, adjPosition, payloads);
                }
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (isRefreshHeader(position))
                return TYPE_REFRESH_HEADER;
            if (isFooter(position))
                return TYPE_FOOTER;
            if (adapter != null) {
                int adjPosition = position - 1;
                int adapterCount = adapter.getItemCount();
                if (adjPosition < adapterCount) {
                    int type = adapter.getItemViewType(adjPosition);
                    if (isReservedItemViewType(type)) {
                        throw new IllegalStateException("XRecyclerView require itemViewType in adapter should be less than 10000 ");
                    }
                    return type;
                }
            }
            return -1;
        }

        @Override
        public int getItemCount() {
            if (loadingMoreEnabled) {
                if (adapter != null) {
                    return adapter.getItemCount() + 2;
                } else {
                    return 2;
                }
            } else {
                if (adapter != null) {
                    return adapter.getItemCount() + 1;
                } else {
                    return 1;
                }
            }
        }

        @Override
        public long getItemId(int position) {
            if (adapter != null && position >= 1) {
                int adjPosition = position - 1;
                if (adjPosition < adapter.getItemCount()) {
                    return adapter.getItemId(adjPosition);
                }
            }
            return -1;
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            LayoutManager manager = recyclerView.getLayoutManager();
            if (manager instanceof GridLayoutManager) {
                final GridLayoutManager gridManager = (GridLayoutManager) manager;
                gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        return (isRefreshHeader(position) || isFooter(position)) ? gridManager.getSpanCount() : 1;
                    }
                });
            }
            adapter.onAttachedToRecyclerView(recyclerView);
        }

        @Override
        public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
            if (adapter != null)
                adapter.onDetachedFromRecyclerView(recyclerView);
        }

        @Override
        public void onViewAttachedToWindow(ViewHolder holder) {
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
            int position = holder.getLayoutPosition();
            if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams
                    && (isRefreshHeader(position) || isFooter(position))) {
                StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
                p.setFullSpan(true);
            }
            adapter.onViewAttachedToWindow(holder);
        }

        @Override
        public void onViewDetachedFromWindow(ViewHolder holder) {
            if (adapter != null)
                adapter.onViewDetachedFromWindow(holder);
        }

        @Override
        public void onViewRecycled(ViewHolder holder) {
            adapter.onViewRecycled(holder);
        }

        @Override
        public boolean onFailedToRecycleView(ViewHolder holder) {
            return adapter.onFailedToRecycleView(holder);
        }

        @Override
        public void unregisterAdapterDataObserver(AdapterDataObserver observer) {
            adapter.unregisterAdapterDataObserver(observer);
        }

        @Override
        public void registerAdapterDataObserver(AdapterDataObserver observer) {
            adapter.registerAdapterDataObserver(observer);
        }

        //判断是否是XRecyclerView保留的itemViewType
        private boolean isReservedItemViewType(int itemViewType) {
            if (itemViewType == TYPE_REFRESH_HEADER || itemViewType == TYPE_FOOTER) {
                return true;
            } else {
                return false;
            }
        }

        public Adapter getOriginalAdapter() {
            return this.adapter;
        }

        public boolean isRefreshHeader(int position) {
            return position == 0;
        }

        public boolean isFooter(int position) {
            if (loadingMoreEnabled) {
                return position == getItemCount() - 1;
            } else {
                return false;
            }
        }

        private class SimpleViewHolder extends ViewHolder {
            public SimpleViewHolder(View itemView) {
                super(itemView);
            }
        }
    }
}
