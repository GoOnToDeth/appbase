package com.libs.wzf.sample.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.libs.wzf.sample.R;
import com.wzf.baseutils.widget.FlowView;
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
public class FlowFragment extends Fragment implements View.OnClickListener {

    private static final String[] arrays = {"科比", "乔丹", "詹姆斯", "蒂姆邓肯", "詹姆斯哈登", "凯文杜兰特",
            "斯蒂芬森", "史蒂夫乔布斯", "马化腾", "马云", "李彦宏", "周鸿祎", "腾讯", "百度", "阿里巴巴", "蚂蚁金服科技有限公司",
            "京东金融", "陆金所", "Android", "iOS"};

    private FlowView flowView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_flow, container, false);
        flowView = (FlowView) view.findViewById(R.id.flowview);
        view.findViewById(R.id.btn_left).setOnClickListener(this);
        view.findViewById(R.id.btn_center).setOnClickListener(this);
        view.findViewById(R.id.btn_right).setOnClickListener(this);
        initFlowChilds();
        return view;
    }

    private void initFlowChilds() {
        for (String txt : arrays) {
            TextView textView = createView(txt);
            flowView.addView(textView);
        }
    }

    private TextView createView(String txt) {
        RoundTextView roundTextView = new RoundTextView(getActivity());
        ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams
                (ViewGroup.MarginLayoutParams.WRAP_CONTENT, ViewGroup.MarginLayoutParams.WRAP_CONTENT);
        params.setMargins(10, 10, 10, 10);
        roundTextView.setLayoutParams(params);
        roundTextView.setTextColor(Color.WHITE);
        roundTextView.setPadding(10, 10, 10, 10);
        RoundViewDelegate delegate = roundTextView.getDelegate();
        delegate.setBackgroundColor(Color.RED);
        delegate.setBackgroundPressColor(Color.GREEN);
        delegate.setCornerRadius(8);
        roundTextView.setClickable(true);
        roundTextView.setText(txt);
        roundTextView.setGravity(Gravity.CENTER);
        return roundTextView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_left:
                flowView.setOrientation(FlowView.LEFT_ALIGN);
                break;
            case R.id.btn_center:
                flowView.setOrientation(FlowView.CENTER_ALIGN);
                break;
            case R.id.btn_right:
                flowView.setOrientation(FlowView.RIGHT_ALIGN);
                break;
        }
        flowView.requestLayout();
    }
}
