package com.wzf.baseutils.widget.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.wzf.baseutils.R;
import com.wzf.baseutils.utils.PhoneUtils;

/**
 * ================================================
 * 描    述：类似于底部弹起的PopUpWindow
 * 作    者：wzf
 * 创建日期：2016/7/20
 * 版    本：1.0
 * 修订历史：
 * ================================================
 */
public abstract class BottomDialog<T extends BottomDialog<T>> extends BaseDialog<T> {

    public BottomDialog(Context context) {
        super(context);
    }

    public BottomDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected BottomDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    public void reSize(View view) {
        Window window = getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.dialogWindowAnim);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = PhoneUtils.getScreenWidth(getContext());
        window.setAttributes(params);
    }
}
