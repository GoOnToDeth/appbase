package com.libs.wzf.sample.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.libs.wzf.sample.R;
import com.wzf.baseutils.dialog.BaseDialog;
import com.wzf.baseutils.dialog.BottomDialog;
import com.wzf.baseutils.widget.NumberPickerView;

/**
 * ================================================
 * 描    述：
 * 作    者：wzf
 * 创建日期：2017/5/9
 * 版    本：1.0
 * 修订历史：
 * ================================================
 */
public class CustomPositionDialog extends BaseDialog {

    public CustomPositionDialog(Context context) {
        super(context);
    }

    @Override
    public View onCreatView() {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_position, null);
        setX(200);
        setY(200);
        setGravity(Gravity.LEFT | Gravity.TOP);
        return view;
    }
}
