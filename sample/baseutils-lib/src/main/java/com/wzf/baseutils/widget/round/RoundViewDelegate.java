package com.wzf.baseutils.widget.round;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wzf.baseutils.R;

/**
 * ================================================
 * 描    述：
 * 作    者：王智凡
 * 创建日期：2016/6/26
 * 版    本：1.0
 * 修订历史：
 * ================================================
 */
public class RoundViewDelegate {

    // 无效属性
    public static final int INVALIABLE_VALUE = Integer.MAX_VALUE;

    private Context context;
    private View view;

    private GradientDrawable gdBackGround = new GradientDrawable();
    private GradientDrawable gdPressBackGround = new GradientDrawable();
    private BitmapDrawable bdSrc;
    private BitmapDrawable bdSrcPress;

    private int srcPress;
    private int backgroundColor;
    private int backgroundPressColor;
    private int cornerRadius;
    private int cornerRadius_TL;
    private int cornerRadius_TR;
    private int cornerRadius_BL;
    private int cornerRadius_BR;
    private int strokeWidth;
    private int strokeColor;
    private int strokePressColor;
    private int textPressColor;
    private boolean isRadiusHalfHeight;
    private boolean isWidthHeightEqual;
    private boolean isRippleEnable;

    public RoundViewDelegate(Context context, View view, AttributeSet attrs) {
        this.context = context;
        this.view = view;
        obtainStyledAttributes(attrs);
    }

    private void obtainStyledAttributes(AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RoundTextView);
        srcPress = ta.getResourceId(R.styleable.RoundTextView_rv_srcPress, INVALIABLE_VALUE);
        backgroundColor = ta.getColor(R.styleable.RoundTextView_rv_backgroundColor, Color.TRANSPARENT);
        backgroundPressColor = ta.getColor(R.styleable.RoundTextView_rv_backgroundPressColor, INVALIABLE_VALUE);
        cornerRadius = ta.getDimensionPixelSize(R.styleable.RoundTextView_rv_cornerRadius, 0);
        strokeWidth = ta.getDimensionPixelSize(R.styleable.RoundTextView_rv_strokeWidth, 0);
        strokeColor = ta.getColor(R.styleable.RoundTextView_rv_strokeColor, Color.TRANSPARENT);
        strokePressColor = ta.getColor(R.styleable.RoundTextView_rv_strokePressColor, INVALIABLE_VALUE);
        textPressColor = ta.getColor(R.styleable.RoundTextView_rv_textPressColor, INVALIABLE_VALUE);
        isRadiusHalfHeight = ta.getBoolean(R.styleable.RoundTextView_rv_isRadiusHalfHeight, false);
        isWidthHeightEqual = ta.getBoolean(R.styleable.RoundTextView_rv_isWidthHeightEqual, false);
        cornerRadius_TL = ta.getDimensionPixelSize(R.styleable.RoundTextView_rv_cornerRadius_TL, 0);
        cornerRadius_TR = ta.getDimensionPixelSize(R.styleable.RoundTextView_rv_cornerRadius_TR, 0);
        cornerRadius_BL = ta.getDimensionPixelSize(R.styleable.RoundTextView_rv_cornerRadius_BL, 0);
        cornerRadius_BR = ta.getDimensionPixelSize(R.styleable.RoundTextView_rv_cornerRadius_BR, 0);
        isRippleEnable = ta.getBoolean(R.styleable.RoundTextView_rv_isRippleEnable, true);
        ta.recycle();
    }

    public int getSrcPress() {
        return srcPress;
    }

    public void setSrcPress(int srcPress) {
        this.srcPress = srcPress;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
        setBackGroundSelector();
    }

    public int getBackgroundPressColor() {
        return backgroundPressColor;
    }

    public void setBackgroundPressColor(int backgroundPressColor) {
        this.backgroundPressColor = backgroundPressColor;
        setBackGroundSelector();
    }

    public int getCornerRadius() {
        return cornerRadius;
    }

    public void setCornerRadius(int cornerRadius) {
        this.cornerRadius = cornerRadius;
        setBackGroundSelector();
    }

    public int getCornerRadius_TL() {
        return cornerRadius_TL;
    }

    public void setCornerRadius_TL(int cornerRadius_TL) {
        this.cornerRadius_TL = cornerRadius_TL;
        setBackGroundSelector();
    }

    public int getCornerRadius_TR() {
        return cornerRadius_TR;
    }

    public void setCornerRadius_TR(int cornerRadius_TR) {
        this.cornerRadius_TR = cornerRadius_TR;
        setBackGroundSelector();
    }

    public int getCornerRadius_BL() {
        return cornerRadius_BL;
    }

    public void setCornerRadius_BL(int cornerRadius_BL) {
        this.cornerRadius_BL = cornerRadius_BL;
        setBackGroundSelector();
    }

    public int getCornerRadius_BR() {
        return cornerRadius_BR;
    }

    public void setCornerRadius_BR(int cornerRadius_BR) {
        this.cornerRadius_BR = cornerRadius_BR;
        setBackGroundSelector();
    }

    public int getStrokeWidth() {
        return strokeWidth;
    }

    public void setStrokeWidth(int strokeWidth) {
        this.strokeWidth = strokeWidth;
        setBackGroundSelector();
    }

    public int getStrokeColor() {
        return strokeColor;
    }

    public void setStrokeColor(int strokeColor) {
        this.strokeColor = strokeColor;
        setBackGroundSelector();
    }

    public int getStrokePressColor() {
        return strokePressColor;
    }

    public void setStrokePressColor(int strokePressColor) {
        this.strokePressColor = strokePressColor;
        setBackGroundSelector();
    }

    public int getTextPressColor() {
        return textPressColor;
    }

    public void setTextPressColor(int textPressColor) {
        this.textPressColor = textPressColor;
        setBackGroundSelector();
    }

    public boolean isRadiusHalfHeight() {
        return isRadiusHalfHeight;
    }

    public void setRadiusHalfHeight(boolean radiusHalfHeight) {
        isRadiusHalfHeight = radiusHalfHeight;
        setBackGroundSelector();
    }

    public boolean isWidthHeightEqual() {
        return isWidthHeightEqual;
    }

    public void setWidthHeightEqual(boolean widthHeightEqual) {
        isWidthHeightEqual = widthHeightEqual;
        setBackGroundSelector();
    }

    public boolean isRippleEnable() {
        return isRippleEnable;
    }

    public void setRippleEnable(boolean rippleEnable) {
        isRippleEnable = rippleEnable;
    }

    public void setBackGroundSelector() {
        if (view instanceof ImageView) {
            return;
//            StateListDrawable listDrawable = new StateListDrawable();
//            Resources resources = view.getResources();
//            if (src != INVALIABLE_VALUE) {
//                bdSrc = new BitmapDrawable(resources, BitmapFactory.decodeResource(resources, src));
//                listDrawable.addState(new int[]{-android.R.attr.state_pressed}, bdSrc);
//            }
//            if (srcPress != INVALIABLE_VALUE) {
//                bdSrcPress = new BitmapDrawable(resources, BitmapFactory.decodeResource(resources, srcPress));
//                listDrawable.addState(new int[]{android.R.attr.state_pressed}, bdSrcPress);
//            }
//            if (listDrawable.isStateful())
//                ((ImageView) view).setImageDrawable(listDrawable);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && isRippleEnable) {
                setCornerAndStroke(gdBackGround, backgroundColor, strokeColor);
                RippleDrawable rippleDrawable = new RippleDrawable(getColorStateList(backgroundColor, backgroundPressColor),
                        gdBackGround, null);
                view.setBackground(rippleDrawable);
            } else {
                StateListDrawable bg = new StateListDrawable();
                setCornerAndStroke(gdBackGround, backgroundColor, strokeColor);
                bg.addState(new int[]{-android.R.attr.state_pressed}, gdBackGround);
                if (backgroundPressColor != INVALIABLE_VALUE || strokePressColor != INVALIABLE_VALUE) {
                    setCornerAndStroke(gdPressBackGround,
                            backgroundPressColor != INVALIABLE_VALUE ? backgroundPressColor : backgroundColor,
                            strokePressColor != INVALIABLE_VALUE ? strokePressColor : strokeColor);
                    bg.addState(new int[]{android.R.attr.state_pressed}, gdPressBackGround);
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    view.setBackground(bg);
                } else {
                    view.setBackgroundDrawable(bg);
                }
            }
            if (view instanceof TextView && textPressColor != INVALIABLE_VALUE) {
                ColorStateList textColors = ((TextView) view).getTextColors();
                int[][] states = new int[][]{new int[]{-android.R.attr.state_pressed}, new int[]{android.R.attr.state_pressed}};
                ColorStateList colorStateList = new ColorStateList(states, new int[]{textColors.getDefaultColor(), textPressColor});
                ((TextView) view).setTextColor(colorStateList);
            }
        }
    }

    private void setCornerAndStroke(GradientDrawable gd, int color, int strokeColor) {
        gd.setColor(color);
        if (cornerRadius_TL > 0 || cornerRadius_TR > 0 || cornerRadius_BR > 0 || cornerRadius_BL > 0) {
            /**The corners are ordered top-left, top-right, bottom-right, bottom-left*/
            float[] radiusArr = new float[8];
            radiusArr[0] = cornerRadius_TL;
            radiusArr[1] = cornerRadius_TL;
            radiusArr[2] = cornerRadius_TR;
            radiusArr[3] = cornerRadius_TR;
            radiusArr[4] = cornerRadius_BR;
            radiusArr[5] = cornerRadius_BR;
            radiusArr[6] = cornerRadius_BL;
            radiusArr[7] = cornerRadius_BL;
            gd.setCornerRadii(radiusArr);
        } else {
            gd.setCornerRadius(cornerRadius);
        }
        gd.setStroke(strokeWidth, strokeColor);
    }

    private ColorStateList getColorStateList(int normalColor, int pressColor) {
        int[][] states = new int[][]{
                new int[]{android.R.attr.state_pressed},
                new int[]{android.R.attr.state_focused},
                new int[]{android.R.attr.state_activated},
                new int[]{}
        };
        int[] color = new int[]{pressColor, pressColor, pressColor, normalColor};
        return new ColorStateList(states, color);
    }
}
