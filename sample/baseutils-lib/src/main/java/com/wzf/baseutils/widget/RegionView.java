package com.wzf.baseutils.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.RegionIterator;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * ===============================
 * 描    述：可点击的环形统计图
 * 作    者：wzf
 * ===============================
 */
public class RegionView extends View {
    public RegionView(Context context) {
        super(context);
    }

    public RegionView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RegionView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private Paint paintNormal = new Paint();
    private Paint paintSelected = new Paint();
    private Paint paintSubSelected = new Paint();
    private Region viewRegion = new Region();

    private int arcWidth = 20, selectedWidth = 26, selectedSubWidth = 8;
    private int subCircleGap = 8;

    private int[] angleArrays = {90, 165, 295, 360};
    private int[] colorArrays = {Color.parseColor("#f8d173"), Color.parseColor("#a09cf5"),
            Color.parseColor("#71e7ce"), Color.parseColor("#f37886")};
    private int[] colorSubArrays = {Color.parseColor("#64f8d173"), Color.parseColor("#64a09cf5"),
            Color.parseColor("#6471e7ce"), Color.parseColor("#64f37886")};

    private float sweepAngle;
    private RectF rectFCircle, rectFSubCircle;

    private int curSelectedIndex = -1;

    private List<Region> mRegionList = new ArrayList<>();

    {
        arcWidth = dip2px(arcWidth);
        selectedWidth = dip2px(selectedWidth);
        selectedSubWidth = dip2px(selectedSubWidth);
        subCircleGap = dip2px(subCircleGap);

        paintNormal.setAntiAlias(true);
        paintNormal.setStyle(Paint.Style.STROKE);
        paintNormal.setStrokeWidth(arcWidth);

        paintSelected.setAntiAlias(true);
        paintSelected.setStyle(Paint.Style.STROKE);
        paintSelected.setStrokeWidth(selectedWidth);

        paintSubSelected.setAntiAlias(true);
        paintSubSelected.setStyle(Paint.Style.STROKE);
        paintSubSelected.setStrokeWidth(selectedSubWidth);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int w = getMeasuredWidth();
        int h = getMeasuredHeight();
        int halfArcWidth = selectedWidth % 2 == 0 ? selectedWidth / 2 : selectedWidth / 2 + 1;
        rectFCircle = new RectF(halfArcWidth, halfArcWidth, w - halfArcWidth, h - halfArcWidth);
        int offsetX = halfArcWidth + arcWidth / 2 + subCircleGap + selectedSubWidth / 2;
        rectFSubCircle = new RectF(offsetX, offsetX, w - offsetX, h - offsetX);
        viewRegion.set(0, 0, w, h);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (sweepAngle <= 0) return;

        int endAngleIndex = 0;
        for (int i = 0; i < angleArrays.length; i++) {
            if (sweepAngle <= angleArrays[i]) {
                endAngleIndex = i;
                break;
            }
        }

        drawChart(canvas, endAngleIndex);
        if (sweepAngle == 360) {
            initRegionList();
        }
    }

    private void drawChart(Canvas canvas, int endAngleIndex) {
        for (int i = 0; i <= endAngleIndex; i++) {
            float startAngle = i == 0 ? 0 : angleArrays[i - 1];
            float endAngle = sweepAngle > angleArrays[i] ? angleArrays[i] : sweepAngle;
            drawArc(canvas, startAngle, endAngle, i);
        }
    }

    private void drawArc(Canvas canvas, float startAngle, float endAngle, int index) {
        Paint paint = index == curSelectedIndex ? paintSelected : paintNormal;
        paint.setColor(colorArrays[index]);
        canvas.drawArc(rectFCircle, startAngle, endAngle - startAngle, false, paint);
        if (index == curSelectedIndex) {
            paintSubSelected.setColor(colorSubArrays[index]);
            canvas.drawArc(rectFSubCircle, startAngle, endAngle - startAngle, false, paintSubSelected);
        }
    }

    public void initRegionList() {
        int w = getMeasuredWidth();
        int h = getMeasuredHeight();

        int halfArcWidth = selectedWidth % 2 == 0 ? selectedWidth / 2 : selectedWidth / 2 + 1;

        int innerOffset = halfArcWidth + arcWidth / 2;
        RectF rectFInner = new RectF(innerOffset, innerOffset, w - innerOffset, h - innerOffset);
        int outOffset = halfArcWidth - arcWidth / 2;
        RectF rectFOut = new RectF(outOffset, outOffset, w - outOffset, h - outOffset);

        for (int i = 0; i < angleArrays.length; i++) {
            int startAngle = i == 0 ? 0 : angleArrays[i - 1];
            int endAngle = angleArrays[i];
            Path path = new Path();
            path.arcTo(rectFInner, startAngle, endAngle - startAngle, true);
            path.arcTo(rectFOut, endAngle, startAngle - endAngle, false);
            Region region = new Region();
            region.setPath(path, viewRegion);
            mRegionList.add(region);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            int x = (int) event.getX();
            int y = (int) event.getY();
            int checkedIndex = -1;
            for (int i = 0; i < mRegionList.size(); i++) {
                if (mRegionList.get(i).contains(x, y)) {
                    checkedIndex = i;
                    break;
                }
            }
            if (checkedIndex != -1 && curSelectedIndex != checkedIndex) {
                curSelectedIndex = checkedIndex;
                invalidate();
            }
        }
        return super.onTouchEvent(event);
    }

    public float getSweepAngle() {
        return sweepAngle;
    }

    public void setSweepAngle(float sweepAngle) {
        this.sweepAngle = sweepAngle;
        invalidate();
    }

    public void reset() {
        sweepAngle = 0;
    }

    private void drawRegion(Canvas canvas, Region rgn) {
        Paint paint1 = new Paint();
        paint1.setAntiAlias(true);
        paint1.setColor(Color.parseColor("#32DC143C"));
        RegionIterator iter = new RegionIterator(rgn);
        Rect r = new Rect();
        while (iter.next(r)) {
            canvas.drawRect(r, paint1);
        }
    }

    private int dip2px(float dpValue) {
        Context context = getContext();
        final float scale = context.getApplicationContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
