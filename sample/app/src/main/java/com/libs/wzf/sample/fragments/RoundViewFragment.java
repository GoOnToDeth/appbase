package com.libs.wzf.sample.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.libs.wzf.sample.R;
import com.wzf.baseutils.adapter.ListViewAdapter;
import com.wzf.baseutils.adapter.ViewHolder;
import com.wzf.baseutils.widget.ToggleButton;

import java.util.Arrays;

/**
 * ================================================
 * 描    述：
 * 作    者：wzf
 * 创建日期：2017/5/9
 * 版    本：1.0
 * 修订历史：
 * ================================================
 */
public class RoundViewFragment extends Fragment {

    private ToggleButton toggleButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_round_view, container, false);
        toggleButton = (ToggleButton) view.findViewById(R.id.tb_toggle);
        toggleButton.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                String msg = on ? "当前状态：打开" : "当前状态：关闭";
                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
}
