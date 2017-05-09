package com.wzf.baseutils.dialog;

import android.animation.Animator;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.FrameLayout;

import com.wzf.baseutils.utils.PhoneUtils;
import com.wzf.baseutils.utils.StatusBarUtils;

/**
 * ================================================
 * 描    述：Dialog基类
 * 作    者：wzf
 * 创建日期：2017/1/12
 * 版    本：1.0
 * 修订历史：
 * ================================================
 */
public abstract class BaseDialog extends Dialog {

    protected Context context;

    /**
     * Dialog宽、高相对屏幕占比
     **/
    private float widthScale, heightScale;
    /**
     * 最小宽度比例
     */
    private float minWidthScale;
    /**
     * 具体长宽值
     */
    private int width, height;
    /**
     * 相对位置
     */
    private int gravity = Gravity.NO_GRAVITY;
    /**
     * 弹出时的坐标
     */
    private int x, y;
    /**
     * 布局根目录
     */
    protected View rootView;
    /**
     * Dialog的最大宽度和高度
     */
    private int maxWidth, maxHeight;

    public BaseDialog(Context context) {
        super(context);
        this.context = context;
        setDialogTheme();
        maxWidth = PhoneUtils.getScreenWidth(context);
        maxHeight = PhoneUtils.getScreenHeight(context) - StatusBarUtils.getHeight(context);
    }

    /**
     * set dialog theme(设置对话框主题)
     */
    private void setDialogTheme() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);// android:windowNoTitle
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));// android:windowBackground
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);// android:backgroundDimEnabled默认是true的
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        rootView = onCreatView();
        if (rootView == null) return;
        setContentView(rootView);
        if (minWidthScale != 0) {
            int w = (int) (minWidthScale * maxWidth);
            rootView.setMinimumWidth(w);
        }
    }

    @Override
    public void onAttachedToWindow() {
        setUiBeforShow();
        // 设置大小
        int w, h;
        if (width == 0 && height == 0) {
            w = widthScale == 0 ? FrameLayout.LayoutParams.WRAP_CONTENT : (int) (maxWidth * widthScale);
            h = heightScale == 0 ? FrameLayout.LayoutParams.WRAP_CONTENT : (int) (maxHeight * heightScale);
        } else {
            w = width;
            h = height;
        }
        FrameLayout.LayoutParams paramsRootView = (FrameLayout.LayoutParams) rootView.getLayoutParams();
        paramsRootView.width = w;
        paramsRootView.height = h;

        Window window = getWindow();
        WindowManager.LayoutParams paramsWindow = window.getAttributes();
        window.setGravity(gravity);
        paramsWindow.x = x;
        paramsWindow.y = y;
        window.setAttributes(paramsWindow);

        // 开始入场动画
        Animator animator = onAttachedAnimator();
        Animation animation = onAttachedAnimation();
        if (animator != null && animation == null) {
            animator.start();
        } else if (animator == null && animation != null) {
            rootView.startAnimation(animation);
        }
    }

    @Override
    public void dismiss() {
        // 开始出场动画
        Animator animator = onDetachedAnimator();
        Animation animation = onDetachedAnimation();
        if (animator != null && animation == null) {
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    BaseDialog.super.dismiss();
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    BaseDialog.super.dismiss();
                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            animator.start();
        } else if (animator == null && animation != null) {
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    BaseDialog.super.dismiss();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            rootView.startAnimation(animation);
        } else {
            super.dismiss();
        }
    }

    /**
     * 每次显示前调用
     */
    protected void setUiBeforShow() {

    }

    /**
     * 入场动画(Property Animation)
     *
     * @return
     */
    protected Animator onAttachedAnimator() {
        return null;
    }

    /**
     * 出场动画(Property Animation)
     *
     * @return
     */
    protected Animator onDetachedAnimator() {
        return null;
    }

    /**
     * 入场动画(View Animation)
     *
     * @return
     */
    protected Animation onAttachedAnimation() {
        return null;
    }

    /**
     * 出场动画(View Animation)
     *
     * @return
     */
    protected Animation onDetachedAnimation() {
        return null;
    }

    protected int getScreenHeight() {
        DisplayMetrics mDisplayMetrics = context.getResources().getDisplayMetrics();
        return mDisplayMetrics.heightPixels;
    }

    protected int getScreenWidth() {
        DisplayMetrics mDisplayMetrics = context.getResources().getDisplayMetrics();
        return mDisplayMetrics.widthPixels;
    }

    public abstract View onCreatView();

    public BaseDialog setWidthScale(float widthScale) {
        this.widthScale = widthScale;
        return this;
    }

    public BaseDialog setHeightScale(float heightScale) {
        this.heightScale = heightScale;
        return this;
    }

    public void setMinWidthScale(float minWidthScale) {
        this.minWidthScale = minWidthScale;
    }

    public BaseDialog setWidth(int width) {
        this.width = width;
        return this;
    }

    public BaseDialog setHeight(int height) {
        this.height = height;
        return this;
    }

    public BaseDialog setGravity(int gravity) {
        this.gravity = gravity;
        return this;
    }

    public BaseDialog setY(int y) {
        this.y = y;
        return this;
    }

    public BaseDialog setX(int x) {
        this.x = x;
        return this;
    }
}
