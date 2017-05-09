package com.wzf.baseutils.widget.round;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

/**
 * ================================================
 * 描    述：圆角或圆形ImageView
 * 作    者：wzf
 * 创建日期：2016/11/29
 * 版    本：1.0
 * 修订历史：
 * ================================================
 */
public class RoundImageView extends ImageView {

    private static final int TYPE_ROUND = 1, TYPE_CIRCLE = 2, TYPE_INVALIABLE = 3;
    private int type = TYPE_INVALIABLE;

    private RoundViewDelegate delegate;

    private Paint mPaint;
    private BitmapShader mBitmapShader;

    /**
     * 圆形图片的半径
     */
    private int radius;

    /**
     * 3x3 矩阵，主要用于缩小放大
     */
    private Matrix mMatrix;

    private boolean hasTouch;

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
        mMatrix = new Matrix();
        mPaint = new Paint();
        mPaint.setFilterBitmap(true);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);

        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                if (delegate.getSrcPress() == RoundViewDelegate.INVALIABLE_VALUE)
                    hasTouch = false;
                else if ((action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_MOVE)) {
                    if (!hasTouch) {
                        invalidate();
                        hasTouch = true;
                    }
                } else {
                    if (hasTouch) invalidate();
                    hasTouch = false;
                }
                return false;
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int minSize = Math.min(getMeasuredWidth(), getMeasuredHeight());
        radius = minSize / 2;
//        if (delegate.getCornerRadius() == radius || delegate.isRadiusHalfHeight()) {   // 表示是圆形
//            setMeasuredDimension(minSize, minSize);
//        }
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
//        setShader(canvas);
        setDrawType();
        clearCanvas(canvas);
        drawByXfermode(canvas);
    }

    /**
     * 清空画布
     *
     * @param canvas
     */
    private void clearCanvas(Canvas canvas) {
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        canvas.drawPaint(mPaint);
        mPaint.setXfermode(null);
    }

    private void setDrawType() {
        if (delegate.isRadiusHalfHeight()) {    // 圆形
            type = TYPE_CIRCLE;
        } else if (delegate.getCornerRadius() != 0) {   // 圆角
            type = TYPE_ROUND;
        } else {    // 正常
            type = TYPE_INVALIABLE;
        }
    }

    private void setShader(Canvas canvas) {
        if (getDrawable() == null) return;
        if (!(getDrawable() instanceof BitmapDrawable)) return;
        Bitmap bitmap = ((BitmapDrawable) getDrawable()).getBitmap();
        mBitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        float scale = 1.0f;
        if (delegate.getCornerRadius() == radius || delegate.isRadiusHalfHeight()) {    // 圆形
            // 拿到bitmap宽或高的小值
            int bSize = Math.min(bitmap.getWidth(), bitmap.getHeight());
            scale = getMeasuredWidth() * 1.0f / bSize;
        } else {    // 圆角
            if (bitmap.getWidth() != getMeasuredWidth() || bitmap.getHeight() != getMeasuredHeight()) {
                scale = Math.max(getWidth() * 1.0f / bitmap.getWidth(), getHeight() * 1.0f / bitmap.getHeight());
            }
        }
        // shader的变换矩阵，我们这里主要用于放大或者缩小
        mMatrix.setScale(scale, scale);
        // 设置变换矩阵
        mBitmapShader.setLocalMatrix(mMatrix);
        // 设置shader
        mPaint.setShader(mBitmapShader);


        if (delegate.getCornerRadius() == radius || delegate.isRadiusHalfHeight()) {    // 圆形
            canvas.drawCircle(radius, radius, radius, mPaint);
        } else {    // 圆角
            RectF mRoundRect = new RectF(0, 0, getMeasuredWidth(), getMeasuredHeight());
            canvas.drawRoundRect(mRoundRect, delegate.getCornerRadius(), delegate.getCornerRadius(), mPaint);
        }
    }

    private void drawByXfermode(Canvas canvas) {
        int minSize = Math.min(getMeasuredHeight(), getMeasuredWidth());
        int strokeWidth = delegate.getStrokeWidth();
        switch (type) {
            case TYPE_CIRCLE:   // 绘制圆形
                canvas.drawCircle(minSize / 2, minSize / 2, minSize / 2 - strokeWidth, mPaint);
                mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
                break;
            case TYPE_ROUND:    // 圆角矩形
                RectF rectF = new RectF(strokeWidth, strokeWidth, getMeasuredWidth() - strokeWidth, getMeasuredHeight() - strokeWidth);
                canvas.drawRoundRect(rectF, delegate.getCornerRadius(), delegate.getCornerRadius(), mPaint);
                mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
                break;
        }
        if (getDrawable() != null && getDrawable() instanceof BitmapDrawable) {    // 绘制图片
            Bitmap bitmap = hasTouch ?
                    BitmapFactory.decodeResource(getResources(), delegate.getSrcPress()) :
                    ((BitmapDrawable) getDrawable()).getBitmap();
//            if (!hasTouch || delegate.getSrcPress() == RoundViewDelegate.INVALIABLE_VALUE)
//                bitmap = ((BitmapDrawable) getDrawable()).getBitmap();
//            else
//                bitmap = BitmapFactory.decodeResource(getResources(), delegate.getSrcPress());
            RectF rectF = new RectF(strokeWidth, strokeWidth, getMeasuredWidth() - strokeWidth, getMeasuredHeight() - strokeWidth);
            canvas.drawBitmap(bitmap, null, rectF, mPaint);
        }
        if (strokeWidth != 0) { // 描边
            mPaint.setXfermode(null);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(strokeWidth);
            int strokeColor;
            if (delegate.getStrokePressColor() == RoundViewDelegate.INVALIABLE_VALUE) {
                strokeColor = delegate.getStrokeColor();
            } else if (hasTouch) {
                strokeColor = delegate.getStrokePressColor();
            } else {
                strokeColor = delegate.getStrokeColor();
            }

            mPaint.setColor(strokeColor);
            switch (type) {
                case TYPE_CIRCLE:   // 圆形描边
                    canvas.drawCircle(minSize / 2, minSize / 2, minSize / 2 - strokeWidth, mPaint);
                    break;
                case TYPE_ROUND:   // 圆角描边
//                    float offset = strokeWidth * 1.0f / 2;
//                    RectF rectF = new RectF(offset, offset, getMeasuredWidth() - offset, getMeasuredHeight() - offset);
//                    canvas.drawRoundRect(rectF, delegate.getCornerRadius(), delegate.getCornerRadius(), mPaint);
                    break;
            }
            mPaint.setStyle(Paint.Style.FILL);
        }
    }

    public RoundViewDelegate getDelegate() {
        return delegate;
    }
}
