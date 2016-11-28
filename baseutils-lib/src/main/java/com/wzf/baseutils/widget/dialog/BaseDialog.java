package com.wzf.baseutils.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * ================================================
 * 描    述：
 * 作    者：wzf
 * 创建日期：2016/7/20
 * 版    本：1.0
 * 修订历史：
 * ================================================
 */
public abstract class BaseDialog<T extends BaseDialog<T>> extends Dialog {

    public BaseDialog(Context context) {
        super(context);
        init();
    }

    public BaseDialog(Context context, int themeResId) {
        super(context, themeResId);
        init();
    }

    protected BaseDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init();
    }

    private void init() {
        setDialogTheme();
        View view = getContent();
        setContentView(view);
        reSize(view);
    }

    private void setDialogTheme() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);// android:windowNoTitle
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));// android:windowBackground
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);// android:backgroundDimEnabled默认是true的
    }

    public abstract View getContent();

    public abstract void reSize(View view);
}
