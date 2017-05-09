package com.wzf.baseutils.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

/**
 * ================================================
 * 描    述：底部弹出
 * 作    者：wzf
 * 创建日期：2017/1/14
 * 版    本：1.0
 * 修订历史：
 * ================================================
 */
public abstract class BottomDialog extends BaseDialog {

    private final long duration = 300;
    protected Animation mInnerShowAnim;
    protected Animation mInnerDismissAnim;

    public BottomDialog(Context context) {
        super(context);
        setGravity(Gravity.BOTTOM).setWidthScale(1.0f);
    }

    @Override
    protected Animation onAttachedAnimation() {
        return getInnerShowAnim();
    }

    @Override
    protected Animation onDetachedAnimation() {
        return getInnerDismissAnim();
    }

    protected Animation getInnerShowAnim() {
        if (mInnerShowAnim == null) {
            mInnerShowAnim = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0,
                    Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF, 0);
            mInnerShowAnim.setDuration(duration);
        }
        return mInnerShowAnim;
    }

    protected Animation getInnerDismissAnim() {
        if (mInnerDismissAnim == null) {
            mInnerDismissAnim = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0,
                    Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1);
            mInnerDismissAnim.setDuration(duration);
        }
        return mInnerDismissAnim;
    }
}
