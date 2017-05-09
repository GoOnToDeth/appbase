package com.wzf.baseutils.blur;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

/**
 * ================================================
 * 描    述：获取高斯模糊的Bitmap
 * 作    者：wzf
 * 创建日期：2016/7/23
 * 版    本：1.0
 * 修订历史：
 * ================================================
 */
public class BlurManager {

    private static BlurProcess mBlurProcess;

    public static Bitmap blurByStack(Bitmap original, float radius) {
        if (mBlurProcess == null || !(mBlurProcess instanceof StackBlurProcess))
            mBlurProcess = new StackBlurProcess();
        return mBlurProcess.blur(original, radius);
    }

//    public static Bitmap blurByStack(Bitmap original, float radius, int rate) {
//        Bitmap zoomBitmap = Bitmap.createBitmap(original.getWidth() / rate, original.getHeight() / rate, Bitmap.Config.RGB_565);
//    }
}
