package com.wzf.baseutils.widget.titlebar;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.wzf.baseutils.R;
import com.wzf.baseutils.utils.PhoneUtils;
import com.wzf.baseutils.widget.round.RoundTextView;

/**
 * ================================================
 * 描    述：标题栏
 * 作    者：wzf
 * 创建日期：2016/7/12
 * 版    本：1.0
 * 修订历史：
 * ================================================
 */
public class TitleBar extends ViewGroup {

    private static final int DEFAULT_MAIN_TEXT_SIZE = 18;   // 主标题文字大小
    private static final int DEFAULT_SUB_TEXT_SIZE = 12;    // 副标题文字大小
    private static final int DEFAULT_ACTION_TEXT_SIZE = 15; //
    private static final int DEFAULT_TITLE_BAR_HEIGHT = 48; // 标题栏默认高度

    private int leftTextColor, centerTextColor;
    private int DEFAULT_DIVIDER_COLOR = Color.parseColor("#FFd2d2d2");

    private RoundTextView mLeftText, mCenterText;
    private ImageView mLeftImage;
    private LinearLayout mCenterLayout, mRightLayout;
    private View mDivider;

    // 判断SDK版本是否大于4.4
    private boolean isAboveKITKAT;

    public TitleBar(Context context) {
        super(context);
        init();
    }

    public TitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TitleBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        isAboveKITKAT = Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT ? false : true;
        leftTextColor = Color.parseColor("#FF333333");
        initViews(getContext());
    }

    private void initViews(Context context) {
        mLeftText = new RoundTextView(context);
        mLeftImage = new ImageView(context);
        mCenterLayout = new LinearLayout(context);
        mRightLayout = new LinearLayout(context);
        mDivider = new View(context);

        mLeftText.setTextSize(DEFAULT_SUB_TEXT_SIZE);
        mLeftText.setTextColor(leftTextColor);
        mLeftText.setGravity(Gravity.CENTER);
        mLeftText.setSingleLine(true);

        mLeftImage.setImageResource(R.drawable.icon_title_back);
        mLeftImage.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        mLeftImage.setPadding(dip2px(14), 0, dip2px(14), 0);

        mCenterText = new RoundTextView(context);
        mCenterText.setTextSize(DEFAULT_MAIN_TEXT_SIZE);
        mCenterText.setTextColor(centerTextColor);
        mCenterText.setGravity(Gravity.CENTER);
        mCenterText.setSingleLine(true);

        mCenterLayout.setGravity(Gravity.CENTER);
        mCenterLayout.addView(mCenterText);

        mRightLayout.setGravity(Gravity.CENTER);
        mRightLayout.setPadding(dip2px(8), 0, dip2px(8), 0);

        mDivider.setBackgroundColor(DEFAULT_DIVIDER_COLOR);

        addView(mLeftImage);
        addView(mCenterLayout);
        addView(mRightLayout);
        addView(mDivider, new LayoutParams(LayoutParams.MATCH_PARENT, 1));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int screenWidth = PhoneUtils.getScreenWidth(getContext());

        setMeasuredDimension(
                widthMode == MeasureSpec.EXACTLY ? widthSize : screenWidth,
                heightMode == MeasureSpec.EXACTLY ? heightSize : dip2px(DEFAULT_TITLE_BAR_HEIGHT));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    private int dip2px(int dpValue) {
        return PhoneUtils.dip2px(getContext(), dpValue);
    }
}
