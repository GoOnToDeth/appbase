package com.libs.wzf.sample.fragments;

import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

import com.libs.wzf.sample.R;
import com.wzf.baseutils.widget.FlowView;
import com.wzf.baseutils.widget.RegionView;
import com.wzf.baseutils.widget.round.RoundTextView;
import com.wzf.baseutils.widget.round.RoundViewDelegate;

/**
 * ================================================
 * 描    述：
 * 作    者：wzf
 * 创建日期：2017/5/9
 * 版    本：1.0
 * 修订历史：
 * ================================================
 */
public class CircleChartFragment extends Fragment implements View.OnClickListener {

    RegionView regionView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_circle_chart, container, false);
        regionView = (RegionView) view.findViewById(R.id.region_view);
        view.findViewById(R.id.btn_start).setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        regionView.reset();
        ObjectAnimator animator = ObjectAnimator.ofFloat(regionView, "sweepAngle", 360f);
        animator.setDuration(500);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.start();
    }
}
