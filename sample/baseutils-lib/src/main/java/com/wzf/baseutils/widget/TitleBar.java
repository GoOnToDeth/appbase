package com.wzf.baseutils.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wzf.baseutils.R;
import com.wzf.baseutils.utils.PhoneUtils;
import com.wzf.baseutils.widget.round.RoundTextView;
import com.wzf.baseutils.widget.round.RoundViewDelegate;

/**
 * ================================================
 * 描    述：
 * 作    者：wzf
 * 创建日期：2016/12/18
 * 版    本：1.0
 * 修订历史：
 * ================================================
 */

public class TitleBar extends ViewGroup implements View.OnClickListener {

    private static final int DEFAULT_MAIN_TEXT_SIZE = 18;
    private static final int DEFAULT_SUB_TEXT_SIZE = 12;
    private static final int DEFAULT_ACTION_TEXT_SIZE = 15;
    private static final int DEFAULT_TITLE_BAR_HEIGHT = 48;
    private static final int DEFAULT_LINE_HEIGHT = 0;

    private int DEFAULT_TITLE_COLOR = Color.parseColor("#F0FBF4");
    private int DEFAULT_DIVIDER_COLOR = Color.parseColor("#FFd2d2d2");
    private int DEFAULT_BACKGROUND = Color.parseColor("#5BD48D");

    private int DEFAULT_TITLE_BAR_WIDTH;

    private LinearLayout layoutLeft, layoutCenter, layoutRight;
    private TextView tvCenter;
    private ImageView ivLeft;
    private View line;

    private int mActionTextColor, mActionTextPressColor;

    private int ivLeftRes = R.drawable.ic_arrow_white;

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
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TitleBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        DEFAULT_TITLE_BAR_WIDTH = PhoneUtils.getScreenWidth(getContext());
        initViews();
    }

    private void initViews() {
        setBackgroundColor(DEFAULT_BACKGROUND);
        // 左边
        layoutLeft = new LinearLayout(getContext());
        ivLeft = new ImageView(getContext());
        ivLeft.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        ivLeft.setImageResource(ivLeftRes);
        LinearLayout.LayoutParams paramsLeftImage = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        int leftIvPadding = dip2px(15);
        ivLeft.setPadding(leftIvPadding, leftIvPadding, leftIvPadding, leftIvPadding);
        layoutLeft.addView(ivLeft, paramsLeftImage);

        ViewGroup.LayoutParams paramsLeftLayout = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutLeft.setGravity(Gravity.CENTER);
        addView(layoutLeft, paramsLeftLayout);

        // 中间
        layoutCenter = new LinearLayout(getContext());
        tvCenter = new TextView(getContext());
        tvCenter.setTextColor(DEFAULT_TITLE_COLOR);
        tvCenter.setTextSize(DEFAULT_MAIN_TEXT_SIZE);
        tvCenter.setGravity(Gravity.CENTER);
        tvCenter.setSingleLine(true);
        ViewGroup.LayoutParams paramsCenterTv = new LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutCenter.addView(tvCenter, paramsCenterTv);

        ViewGroup.LayoutParams paramsCenterLayout = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutCenter.setGravity(Gravity.CENTER);
        addView(layoutCenter, paramsCenterLayout);

        // 右边
        layoutRight = new LinearLayout(getContext());
        layoutRight.setGravity(Gravity.CENTER_VERTICAL);
        ViewGroup.LayoutParams paramsRightLayout = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        addView(layoutRight, paramsRightLayout);

        // 底部的线
        line = new View(getContext());
        line.setBackgroundColor(DEFAULT_DIVIDER_COLOR);
        ViewGroup.LayoutParams paramsDivider = new LayoutParams(LayoutParams.MATCH_PARENT, DEFAULT_LINE_HEIGHT);
        addView(line, paramsDivider);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);

        widthMeasureSpec = modeWidth == MeasureSpec.EXACTLY ?
                widthMeasureSpec : MeasureSpec.makeMeasureSpec(DEFAULT_TITLE_BAR_WIDTH - DEFAULT_LINE_HEIGHT, MeasureSpec.EXACTLY);
        heightMeasureSpec = modeHeight == MeasureSpec.EXACTLY ?
                heightMeasureSpec : MeasureSpec.makeMeasureSpec(PhoneUtils.dip2px(getContext(), DEFAULT_TITLE_BAR_HEIGHT) - DEFAULT_LINE_HEIGHT, MeasureSpec.EXACTLY);

        measureChild(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(modeWidth == MeasureSpec.EXACTLY ? sizeWidth : DEFAULT_TITLE_BAR_WIDTH,
                modeHeight == MeasureSpec.EXACTLY ? sizeHeight : PhoneUtils.dip2px(getContext(), DEFAULT_TITLE_BAR_HEIGHT));
    }

    private void measureChild(int widthMeasureSpec, int heightMeasureSpec) {
        // 测量左边
        measureChild(layoutLeft, heightMeasureSpec, heightMeasureSpec);
        // 测量右边
        measureChild(layoutRight, widthMeasureSpec, heightMeasureSpec);
        // 测量底部线
        measureChild(line, widthMeasureSpec, heightMeasureSpec);
        // 测量标题
        int leftWidth = layoutLeft.getVisibility() == GONE ? 0 : layoutLeft.getMeasuredWidth();
        int rightWidth = layoutRight.getVisibility() == GONE ? 0 : layoutRight.getMeasuredWidth();
        int maxWidth = Math.max(leftWidth, rightWidth);
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        measureChild(layoutCenter, MeasureSpec.makeMeasureSpec(sizeWidth - maxWidth * 2, MeasureSpec.EXACTLY), heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int w = getMeasuredWidth();
        int h = getMeasuredHeight();
        int viewHeight = h - DEFAULT_LINE_HEIGHT;
        int leftWidth = layoutLeft.getVisibility() == GONE ? 0 : layoutLeft.getMeasuredWidth();
        int rightWidth = layoutRight.getVisibility() == GONE ? 0 : layoutRight.getMeasuredWidth();


        layoutLeft.layout(0, 0, leftWidth, layoutLeft.getMeasuredHeight());
        layoutRight.layout(w - rightWidth, 0, w, layoutRight.getMeasuredHeight());
        line.layout(0, viewHeight, w, h);
        // 设置中间
        int cLeft = leftWidth + (leftWidth > rightWidth ? 0 : rightWidth - leftWidth);
        int cRight = cLeft + layoutCenter.getMeasuredWidth();
        layoutCenter.layout(cLeft, 0, cRight, layoutCenter.getMeasuredHeight());
    }

    private View inflateAction(Action action) {
        final View view;
        if (action instanceof ImageAction) {
            ImageView imageView = new ImageView(getContext());
            imageView.setImageResource(action.getDrawable());
            imageView.setPadding(10, 10, 10, 10);
            view = imageView;
        } else {
            RoundTextView textView = new RoundTextView(getContext());
            textView.setPadding(10, 5, 10, 5);
            textView.setGravity(Gravity.CENTER);
            textView.setText(action.getText());
            textView.setTextSize(DEFAULT_ACTION_TEXT_SIZE);
            if (mActionTextColor != 0)
                textView.setTextColor(mActionTextColor);
            if (mActionTextPressColor != 0) {
                RoundViewDelegate delegate = textView.getDelegate();
                delegate.setTextPressColor(mActionTextPressColor);
            }

            view = textView;
        }
        view.setTag(action);
        view.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        final Object tag = view.getTag();
        if (tag instanceof Action) {
            Action action = (Action) tag;
            action.performAction(view);
        }
    }

    public interface Action {
        String getText();

        int getDrawable();

        void performAction(View view);
    }

    public static abstract class ImageAction implements Action {
        private int mDrawable;

        public ImageAction(int drawable) {
            mDrawable = drawable;
        }

        @Override
        public int getDrawable() {
            return mDrawable;
        }

        @Override
        public String getText() {
            return null;
        }
    }

    public static abstract class TextAction implements Action {
        final private String mText;

        public TextAction(String text) {
            mText = text;
        }

        @Override
        public int getDrawable() {
            return 0;
        }

        @Override
        public String getText() {
            return mText;
        }
    }

    public int dip2px(float dpValue) {
        final float scale = getContext().getApplicationContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /////////////////////////////////////////////****************设置属性****************///////////////////////////////

    public View addAction(Action action) {
        final int index = layoutRight.getChildCount();
        return addAction(action, index);
    }

    public View addAction(Action action, int index) {
        LinearLayout.LayoutParams params;
        if (action instanceof ImageAction) {
            params = new LinearLayout.LayoutParams(dip2px(DEFAULT_TITLE_BAR_HEIGHT), LayoutParams.MATCH_PARENT);
        } else {
            params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        }
        View view = inflateAction(action);
        layoutRight.addView(view, index, params);
        return view;
    }

    public void setmActionTextColor(int colorResId) {
        this.mActionTextColor = colorResId;
    }

    public void setmActionTextPressColor(int mActionTextPressColor) {
        this.mActionTextPressColor = mActionTextPressColor;
    }

    public void setTitle(String title) {
        tvCenter.setText(title);
    }

    public void setTitleColor(int color) {
        tvCenter.setTextColor(color);
    }

    public void setLayoutLeftVisiable(boolean isVisiable) {
        if (isVisiable)
            layoutLeft.setVisibility(VISIBLE);
        else
            layoutLeft.setVisibility(GONE);
    }

    public void setOnLeftClickListener(OnClickListener onClickListener) {
        layoutLeft.setOnClickListener(onClickListener);
    }
}
