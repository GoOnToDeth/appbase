package com.libs.wzf.sample.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.libs.wzf.sample.R;
import com.libs.wzf.sample.dialog.CustomPositionDialog;
import com.libs.wzf.sample.dialog.StartBottomDialog;
import com.wzf.baseutils.widget.ToggleButton;

/**
 * ================================================
 * 描    述：
 * 作    者：wzf
 * 创建日期：2017/5/9
 * 版    本：1.0
 * 修订历史：
 * ================================================
 */
public class DialogFragment extends Fragment implements View.OnClickListener {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_dialog, container, false);
        view.findViewById(R.id.btn_start_bottom).setOnClickListener(this);
        view.findViewById(R.id.btn_custom_position).setOnClickListener(this);
        view.findViewById(R.id.btn_custom_anim).setOnClickListener(this);
        return view;
    }

    StartBottomDialog startBottomDialog;
    CustomPositionDialog positionDialog;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start_bottom:
                if (startBottomDialog == null)
                    startBottomDialog = new StartBottomDialog(getActivity());
                startBottomDialog.show();
                break;
            case R.id.btn_custom_position:
                if (positionDialog == null)
                    positionDialog = new CustomPositionDialog(getActivity());
                positionDialog.show();
                break;
            case R.id.btn_custom_anim:
                new AlertDialog.Builder(getActivity())
                        .setMessage("请查看BottomDialog源码")
                        .show();
                break;
        }
    }
}
