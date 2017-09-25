package com.libs.wzf.sample.fragments;

import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.libs.wzf.sample.R;
import com.wzf.baseutils.widget.FlowView;
import com.wzf.baseutils.widget.round.RoundTextView;
import com.wzf.baseutils.widget.round.RoundViewDelegate;

/**
 * ================================================
 * 描    述：下拉刷新
 * 作    者：wzf
 * 创建日期：2017/9/25
 * 版    本：1.0
 * 修订历史：
 * ================================================
 */
public class PtrFragment extends Fragment implements View.OnClickListener {

    private static final String TEXT_CONTENT = "请查看：https://github.com/GoOnToDeth/Zf_Pull_To_Refresh";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_ptr, container, false);
        view.findViewById(R.id.btn_ptr).setOnClickListener(this);
        TextView textView = (TextView) view.findViewById(R.id.textview);
        textView.setText(TEXT_CONTENT);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ptr:
                ClipboardManager cmb = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                cmb.setText(TEXT_CONTENT);
                Toast.makeText(getActivity(), "已复制到剪贴板", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
