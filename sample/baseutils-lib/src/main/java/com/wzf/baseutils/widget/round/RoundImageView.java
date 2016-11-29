package com.wzf.baseutils.widget.round;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
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

        mPaint = new Paint();
        mPaint.setFilterBitmap(true);
        mPaint.setAntiAlias(true);
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
        super.onDraw(canvas);
        if (delegate.getSrc() != RoundViewDelegate.INVALIABLE_VALUE) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), delegate.getSrc());
            RectF rectF;
            int strokeWidth = delegate.getStrokeWidth();
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
