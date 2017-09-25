package com.libs.wzf.sample.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.libs.wzf.sample.R;
import com.wzf.baseutils.utils.PhoneUtils;
import com.wzf.baseutils.widget.banner.BaseBanner;

/**
 * ================================================
 * 描    述：Banner
 * 作    者：wzf
 * 创建日期：2017/9/25
 * 版    本：1.0
 * 修订历史：
 * ================================================
 */
public class BannerFragment extends Fragment {

    private BaseBanner mBaseBanner;
    private TextView tvIndex;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_banner, container, false);
        BannerAdapter mBannerAdapter = new BannerAdapter(getActivity());
        mBaseBanner = (BaseBanner) view.findViewById(R.id.banner_main);
        tvIndex = (TextView) view.findViewById(R.id.tv_pos);
        mBaseBanner.setAdapter(mBannerAdapter);
        mBaseBanner.addOnPageChangeListener(mBannerAdapter);
        return view;
    }

    private class BannerAdapter extends PagerAdapter implements ViewPager.OnPageChangeListener {

        private LayoutInflater mInflater;
        private int width, height;

        public BannerAdapter(Context context) {
            mInflater = LayoutInflater.from(context);
            width = PhoneUtils.getScreenWidth(context);
            height = PhoneUtils.dip2px(context, 200);
        }

        @Override
        public int getCount() {
            return mImagesSrc.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view == o;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = mInflater.inflate(R.layout.item, container, false);
            ImageView imageView = (ImageView) view.findViewById(R.id.image);
            loadPagBg(imageView, mImagesSrc[position], R.drawable.ic_default_color);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        public void loadPagBg(ImageView imageView, String imageUrl, int defaultBg) {
            Glide.with(getActivity())
                    .load(imageUrl)
                    .skipMemoryCache(false)  // 必须支持内存缓存
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(defaultBg)
                    .override(width, height)
                    .into(imageView);
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            int selectPos = position % mImagesSrc.length;
            tvIndex.setText((selectPos + 1) + "/" + mImagesSrc.length);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    String[] mImagesSrc = new String[]{
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1500483684883&di=7837e23f767c92fc77c97a914bb88864&imgtype=0&src=http%3A%2F%2Fa.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2Fa686c9177f3e670909f483ec31c79f3df8dc5529.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1500483684883&di=6b6e39349d5bc6da53495368040cb101&imgtype=0&src=http%3A%2F%2Ff.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2F060828381f30e924f5bf6d2246086e061d95f78e.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1500483684882&di=a4380f9e9fbcfe0e151e0ba62d32ebfc&imgtype=0&src=http%3A%2F%2Fe.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2F03087bf40ad162d9a692900f1bdfa9ec8a13cda1.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1500483684882&di=63d92a1a31a051bcd53611ba5e5da3af&imgtype=0&src=http%3A%2F%2Fc.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2F6d81800a19d8bc3e53cef2d9888ba61ea9d345db.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1500483684881&di=2e85ef49889a93f3a858e5c28d2f75d6&imgtype=0&src=http%3A%2F%2Fh.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2Fb999a9014c086e0652c2cc5808087bf40ad1cbbf.jpg"};

}
