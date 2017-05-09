package com.libs.wzf.sample.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.libs.wzf.sample.R;
import com.wzf.baseutils.dialog.BottomDialog;
import com.wzf.baseutils.widget.NumberPickerView;

/**
 * ================================================
 * 描    述：底部弹出dialog需要继承BottomDialog
 * 作    者：wzf
 * 创建日期：2017/5/9
 * 版    本：1.0
 * 修订历史：
 * ================================================
 */
public class StartBottomDialog extends BottomDialog implements View.OnClickListener {

    private String[] mDisplayValues;

    private TextView tvCancel, tvConfirm;
    private NumberPickerView pickerView;

    public StartBottomDialog(Context context) {
        super(context);
        mDisplayValues = context.getResources().getStringArray(R.array.test_display_2);
    }

    @Override
    public View onCreatView() {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_bottom_start, null);
        tvCancel = (TextView) view.findViewById(R.id.tv_cancel);
        tvConfirm = (TextView) view.findViewById(R.id.tv_confirm);
        pickerView = (NumberPickerView) view.findViewById(R.id.picker);
        tvCancel.setOnClickListener(this);
        tvConfirm.setOnClickListener(this);
        return view;
    }

    /**
     * 每次在显示UI之前调用
     */
    @Override
    protected void setUiBeforShow() {
        if (mDisplayValues == null || mDisplayValues.length == 0)
            return;
        pickerView.refreshByNewDisplayedValues(mDisplayValues);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cancel:
                break;
            case R.id.tv_confirm:
                Toast.makeText(getContext(), "当前选中的是：" + mDisplayValues[pickerView.getValue()], Toast.LENGTH_SHORT).show();
                break;
        }
        this.dismiss();
    }
}
