package com.wzf.baseutils.widget.round;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

/**
 * ================================================
 * 描    述：减少selector和部分shape文件
 * 作    者：王智凡
 * 创建日期：2016/11/29
 * 版    本：1.0
 * 修订历史：
 * ================================================
 */
public class RoundImageView extends ImageView {

    private RoundViewDelegate delegate;

    private Paint mPaint;

    public RoundImageView(Context context) {
        super(context);
        init(null);
    }

    public RoundImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public RoundImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        delegate = new RoundViewDelegate(getContext(), this, attrs);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        mPaint = new Paint();
        mPaint.setFilterBitmap(true);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int minSize = Math.min(getMeasuredWidth(), getMeasuredHeight());
        if (delegate.getCornerRadius() == minSize / 2 || delegate.isRadiusHalfHeight()) {   // 表示是圆形
            setMeasuredDimension(minSize, minSize);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (delegate.isRadiusHalfHeight()) {
            delegate.setCornerRadius(getMeasuredHeight() / 2);
        } else {
            delegate.setBackGroundSelector();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {

    }

    private void drawByXfermode(Canvas canvas) {
        int minSize = Math.min(getMeasuredHeight(), getMeasuredWidth());
        int strokeWidth = delegate.getStrokeWidth();
        if (delegate.getCornerRadius() == minSize / 2 || delegate.isRadiusHalfHeight()) {   // 绘制圆形
            canvas.drawCircle(minSize / 2, minSize / 2, (minSize - strokeWidth * 2) / 2, mPaint);
            mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        } else if (delegate.getCornerRadius_TL() != 0 || delegate.getCornerRadius_TR() != 0
                || delegate.getCornerRadius_BL() != 0 || delegate.getCornerRadius_BR() != 0) {
        } else if (delegate.getCornerRadius() != 0) {   // 所有角都是圆角的矩形
            RectF rectF = new RectF(strokeWidth, strokeWidth, getMeasuredWidth() - strokeWidth, getMeasuredHeight() - strokeWidth);
            canvas.drawRoundRect(rectF, delegate.getCornerRadius(), delegate.getCornerRadius(), mPaint);
            mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        }
        if (getDrawable() != null) {    // 绘制图片
            Bitmap bitmap = ((BitmapDrawable) getDrawable()).getBitmap();
            RectF rectF;
            if (strokeWidth == 0)
                rectF = new RectF(0, 0, getMeasuredWidth(), getMeasuredHeight());
            else
                rectF = new RectF(strokeWidth, strokeWidth, getMeasuredWidth() - strokeWidth, getMeasuredHeight() - strokeWidth);
            canvas.drawBitmap(bitmap, null, rectF, mPaint);
        }
    }

    public RoundViewDelegate getDelegate() {
        return delegate;
    }
}
