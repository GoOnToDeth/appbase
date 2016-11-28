package com.wzf.baseutils.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Point;
import android.provider.MediaStore;
import android.view.Display;
import android.view.WindowManager;

import java.util.List;

/**
 * ================================================
 * 描    述：
 * 作    者：wzf
 * 创建日期：2016/7/20
 * 版    本：1.0
 * 修订历史：
 * ================================================
 */
public class PhoneUtils {

    private static int screenHeight = 0;
    private static int screenWidth = 0;

    /**
     * 获取屏幕宽度
     *
     * @return
     */
    public static int getScreenWidth(Context context) {
        if (screenWidth == 0) {
            WindowManager wm = (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            screenWidth = size.x;
        }
        return screenWidth;
    }

    /**
     * 获取屏幕高度
     *
     * @return
     */
    public static int getScreenHeight(Context context) {
        if (screenHeight == 0) {
            WindowManager wm = (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            screenHeight = size.y;
        }
        return screenHeight;
    }

    /**
     * 根据手机分辨率从dp转成px
     *
     * @param dpValue
     * @return
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getApplicationContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 判断系统中是否存在可以启动的相机应用
     * @param context
     * @return
     */
    public static boolean hasCamera(Context context) {
        PackageManager manager = context.getPackageManager();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> infos = manager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return infos.size() > 0;
    }
}
