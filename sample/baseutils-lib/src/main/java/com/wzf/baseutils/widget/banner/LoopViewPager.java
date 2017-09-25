package com.wzf.baseutils.widget.banner;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * ===============================
 * 描    述：动态设置是否循环的ViewPager
 * 作    者：wzf
 * 创建日期：2017/7/11 15:18
 * ===============================
 */
public class LoopViewPager extends ViewPager {

    private BannerAdapter mBannerAdapter;
    // 默认可循环
    private boolean isLopper = true;

    public LoopViewPager(Context context) {
        super(context);
    }

    public LoopViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setAdapter(PagerAdapter adapter) {
        if (isLopper) {
            mBannerAdapter = new BannerAdapter(adapter);
            super.setAdapter(mBannerAdapter);
        } else {
            super.setAdapter(adapter);
        }
    }

    @Override
    public PagerAdapter getAdapter() {
        return isLopper ? mBannerAdapter : super.getAdapter();
    }

    public void setNextItem(int nextPosition){
        if (isLopper) {
            if (nextPosition == mBannerAdapter.FAKE_BANNER_SIZE - 1) {
                if (mBannerAdapter.BANNER_SIZE > 2) {
                    setCurrentItem(mBannerAdapter.BANNER_SIZE - 2, false);
                    setCurrentItem(mBannerAdapter.BANNER_SIZE - 1, true);
                } else {
                    setCurrentItem(mBannerAdapter.BANNER_SIZE * 2 - 2, false);
                    setCurrentItem(mBannerAdapter.BANNER_SIZE * 2 - 1, true);
                }
            } else {
                setCurrentItem(nextPosition, true);
            }
        } else {
            setCurrentItem(nextPosition, true);
        }
    }

    public void setLopper(boolean lopper) {
        if (this.isLopper == lopper) return;
        PagerAdapter curPagerAdapter = getAdapter();
        this.isLopper = lopper;
        if (curPagerAdapter == null) return;
        if (isLopper) {
            int i = getCurrentItem();
            setAdapter(curPagerAdapter);
            if (i == 0) {
                i = mBannerAdapter.BANNER_SIZE;
                setCurrentItem(i, false);
            } else if (i == mBannerAdapter.FAKE_BANNER_SIZE - 1) {
                i = mBannerAdapter.BANNER_SIZE - 1;
                setCurrentItem(i, false);
            } else
                setCurrentItem(i, false);
        } else {
            int i = getCurrentItem() % mBannerAdapter.BANNER_SIZE;
            setAdapter(mBannerAdapter.mPagerAdapter);
            setCurrentItem(i, false);
        }
    }

    private class BannerAdapter extends PagerAdapter {

        PagerAdapter mPagerAdapter;
        int BANNER_SIZE;
        int FAKE_BANNER_SIZE;

        public BannerAdapter(PagerAdapter mPagerAdapter) {
            this.mPagerAdapter = mPagerAdapter;
            BANNER_SIZE = mPagerAdapter.getCount();
            FAKE_BANNER_SIZE = BANNER_SIZE <= 2 ? BANNER_SIZE * 3 : BANNER_SIZE * 2;
        }

        @Override
        public int getCount() {
            return FAKE_BANNER_SIZE;
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return mPagerAdapter.isViewFromObject(view, o);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            position %= BANNER_SIZE;
            return this.mPagerAdapter.instantiateItem(container, position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            position %= BANNER_SIZE;
            this.mPagerAdapter.destroyItem(container, position, object);
        }

        @Override
        public void finishUpdate(ViewGroup container) {
            int position = getCurrentItem();
            if (position == 0) {
                position = BANNER_SIZE;
                setCurrentItem(position, false);
            } else if (position == FAKE_BANNER_SIZE - 1) {
                position = BANNER_SIZE - 1;
                setCurrentItem(position, false);
            }
            mPagerAdapter.finishUpdate(container);
        }
    }
}
