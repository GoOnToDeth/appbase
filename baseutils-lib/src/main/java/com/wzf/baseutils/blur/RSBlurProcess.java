package com.wzf.baseutils.blur;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;

/**
 * ================================================
 * 描    述：使用系统API实现高斯模糊
 * 作    者：wzf
 * 创建日期：2016/7/23
 * 版    本：1.0
 * 修订历史：
 * ================================================
 */
public class RSBlurProcess implements BlurProcess {

    private Context context;

    public RSBlurProcess(Context context) {
        this.context = context;
    }

    /**
     *
     * @param original 需要模糊的bitmap
     * @param radius   模糊的半径，最大值25
     * @return
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public Bitmap blur(Bitmap original, float radius) {
        long start = System.currentTimeMillis();
        //Let's create an empty bitmap with the same size of the bitmap we want to blur
        Bitmap outBitmap = Bitmap.createBitmap(original.getWidth(), original.getHeight(), Bitmap.Config.ARGB_8888);

        //Instantiate a new Renderscript
        RenderScript rs = RenderScript.create(context);

        //Create an Intrinsic Blur Script using the Renderscript
        ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));

        //Create the Allocations (in/out) with the Renderscript and the in/out bitmaps
        Allocation allIn = Allocation.createFromBitmap(rs, original);
        Allocation allOut = Allocation.createFromBitmap(rs, outBitmap);

        //Set the radius of the blur
        blurScript.setRadius(radius);

        //Perform the Renderscript
        blurScript.setInput(allIn);
        blurScript.forEach(allOut);

        //Copy the final bitmap created by the out Allocation to the outBitmap
        allOut.copyTo(outBitmap);

        //recycle the original bitmap
        original.recycle();

        //After finishing everything, we destroy the Renderscript.
        rs.destroy();
        return outBitmap;
    }
}
