package com.wzf.baseutils.blur;

import android.graphics.Bitmap;

/**
 * ================================================
 * 描    述：
 * 作    者：wzf
 * 创建日期：2016/7/23
 * 版    本：1.0
 * 修订历史：
 * ================================================
 */
public interface BlurProcess {

     Bitmap blur(Bitmap original, float radius);
}
