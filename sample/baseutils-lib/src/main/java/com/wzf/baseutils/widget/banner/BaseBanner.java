package com.wzf.baseutils.widget.banner;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

import com.wzf.baseutils.R;

import java.lang.reflect.Field;
import java.util.Timer;
import java.util.TimerTask;


/**
 * ===============================
 * 描    述：
 * 作    者：wzf
 * 创建日期：2017/7/19 15:04
 * ===============================
 */
public class BaseBanner extends LoopViewPager {

    public BaseBanner(Context context) {
        this(context, null);
    }

    public BaseBanner(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    private Context mContext;

    private float scale;
    // 是否循环滚动
    private boolean isLoop;
    // 多久后开始滚动
    private long mDelay;
    // 滚动间隔
    private long mPeriod;
    // 是否自动滚动
    private boolean mIsAutoScrollEnable;

    // 是否触摸Banner
    private boolean mIsTouched = false;
    // 单线程池定时任务
    private Timer mTimer = new Timer();
    // Timer定时任务
    private TimerTask mTimerTask;

    private void init(AttributeSet attrs) {
        this.mContext = getContext();
        initAttrs(attrs);
        initWidget();
    }

    private void initWidget() {
        try {
            Field mField = ViewPager.class.getDeclaredField("mScroller");
            mField.setAccessible(true);
            FixedScroller mScroller = new FixedScroller(mContext, new AccelerateInterpolator());
            mField.set(this, mScroller);
        } catch (Exception e) {
            e.printStackTrace();
        }

        setLopper(isLoop);
        setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                if (action == MotionEvent.ACTION_DOWN
                        || action == MotionEvent.ACTION_MOVE) {
                    mIsTouched = true;
                    cancelTimer();
                } else if (action == MotionEvent.ACTION_UP) {
                    mIsTouched = false;
                    startTimer();
                }
                return false;
            }
        });
    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray ta = mContext.obtainStyledAttributes(attrs, R.styleable.BaseBanner);
        scale = ta.getFloat(R.styleable.BaseBanner_bb_scale, 0.5f);
        isLoop = ta.getBoolean(R.styleable.BaseBanner_bb_isLoopEnable, true);
        mDelay = ta.getInt(R.styleable.BaseBanner_bb_delay, 3000);
        mPeriod = ta.getInt(R.styleable.BaseBanner_bb_period, 3000);
        mIsAutoScrollEnable = ta.getBoolean(R.styleable.BaseBanner_bb_isAutoScrollEnable, true);
        ta.recycle();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startTimer();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        cancelTimer();
    }

    private void startTimer() {
        if (mTimer == null || !mIsAutoScrollEnable) return;
        initTimerTask();
        mTimer.schedule(mTimerTask, mDelay, mPeriod);
    }

    private void cancelTimer() {
        if (mTimerTask == null || !mIsAutoScrollEnable) return;
        mTimerTask.cancel();
        mTimerTask = null;
    }

    private void initTimerTask() {
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                if (!mIsTouched) {
                    int mBannerPosition = getCurrentItem();
                    mBannerPosition = (mBannerPosition + 1) % getAdapter().getCount();
                    Message message = new Message();
                    message.what = 1;
                    message.arg1 = mBannerPosition;
                    mHandler.sendMessage(message);
                }
            }
        };
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what != 1) return;
            int nextPosition = msg.arg1;
            setNextItem(nextPosition);
        }
    };
}
