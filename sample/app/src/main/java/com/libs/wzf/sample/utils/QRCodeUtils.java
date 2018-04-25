package com.libs.wzf.sample.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Environment;
import android.text.TextUtils;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.libs.wzf.sample.R;
import com.wzf.baseutils.utils.PhoneUtils;
import com.wzf.baseutils.zxing.QRCodeWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.concurrent.ExecutionException;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * ================================================
 * 描    述：二维码相关功能
 * 作    者：wzf
 * 版    本：1.0
 * 修订历史：
 * ================================================
 */
public class QRCodeUtils {

    // 二维码中间logo的大小
    private int logoWidth = 60, logoHeight = 60;
    // 保存本地的文件名
    private static final String qrCodeFileName = "mergeQrcode.png";
    private int forgColor = Color.parseColor("#836DD9");
    private int colorBg = Color.WHITE;

    private Context context;
    private OnGenerateListener mGenerateListener;
    private String qrPath = "";
    private String headerPath = "http://ico.58pic.com/ajax/download?id=108297&format=png";

    public QRCodeUtils(Context context) {
        this.context = context;
        logoWidth = PhoneUtils.dip2px(context, logoWidth);
        logoHeight = PhoneUtils.dip2px(context, logoHeight);

        logoWidth = logoWidth > 90 ? 90 : logoWidth;
        logoHeight = logoHeight > 90 ? 90 : logoHeight;
    }

    public void generateQRCode(final String content, int frameWidth) {
        QRCodeWriter.frameWidth = frameWidth;
        Observable.create(new ObservableOnSubscribe<Bitmap>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Bitmap> e) throws Exception {
                generateQRCode(e, content);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(new Function<Bitmap, Bitmap>() {
                    @Override
                    public Bitmap apply(@NonNull Bitmap bitmap) throws Exception {
//                        qrPath = saveQRCode(bitmap);
                        return bitmap;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Bitmap>() {
                    @Override
                    public void accept(@NonNull Bitmap mergeBitmap) throws Exception {
                        if (mGenerateListener != null) {
                            mGenerateListener.onGenerate(mergeBitmap, qrPath);
                        }
                    }
                });
    }

    /**
     * 生成带logo的二维码
     *
     * @param content 二维码内容
     * @return
     */
    private void generateQRCode(ObservableEmitter<Bitmap> e, String content) throws WriterException, ExecutionException, InterruptedException {
        QRCodeWriter writer = new QRCodeWriter();
        Hashtable hashtable = new Hashtable();
        hashtable.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        BitMatrix matrix = writer.encode(content, BarcodeFormat.QR_CODE, 300, 300, hashtable);
        bitMatrix2Bitmap(e, matrix);
    }

    /**
     * 绘制二维码
     *
     * @param matrix
     * @return
     */
    private void bitMatrix2Bitmap(final ObservableEmitter<Bitmap> observableEmitter, BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        int[] rawData = new int[width * height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int color = colorBg;
                if (matrix.get(i, j)) {
                    color = forgColor;
                }
                rawData[i + (j * width)] = color;
            }
        }
        final Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(rawData, 0, width, 0, 0, width, height);

        if (TextUtils.isEmpty(headerPath)) {
            observableEmitter.onNext(callback(bitmap, R.drawable.ic_default));
        } else {
            FutureTarget<File> future = Glide.with(context)
                    .load(headerPath)
                    .downloadOnly(logoWidth, logoHeight);
            try {
                File cacheFile = future.get();
                if (cacheFile == null || !cacheFile.exists()) {
                    observableEmitter.onNext(callback(bitmap, R.drawable.ic_default));
                } else {
                    Bitmap resource = BitmapFactory.decodeFile(cacheFile.getAbsolutePath());
                    observableEmitter.onNext(callback(bitmap, resource));
                }
            } catch (Exception e) {
                observableEmitter.onNext(callback(bitmap, R.drawable.ic_default));
            }
        }
    }

    private Bitmap callback(Bitmap background, Bitmap foreground) {
        return toConformBitmap(background, foreground);
    }

    private Bitmap callback(Bitmap background, int resId) {
        return toConformBitmap(background, BitmapFactory.decodeResource(context.getResources(), resId));
    }

    /**
     * 合并二维码和logo
     *
     * @param background
     * @return
     */
    private Bitmap toConformBitmap(Bitmap background, Bitmap foreground) {
        Matrix matrix = new Matrix();
        float scaleW = logoWidth * 1f / foreground.getWidth();
        float scaleH = logoHeight * 1f / foreground.getHeight();
        matrix.postScale(scaleW, scaleH);
        Bitmap foregroundResize = Bitmap.createBitmap(foreground, 0, 0, foreground.getWidth(), foreground.getHeight(), matrix, true);

        int bgWidth = background.getWidth();
        int bgHeight = background.getHeight();
        int fgWidth = foregroundResize.getWidth();
        int fgHeight = foregroundResize.getHeight();
        Bitmap newbmp = Bitmap.createBitmap(bgWidth, bgHeight, Bitmap.Config.RGB_565);
        Canvas cv = new Canvas(newbmp);
        cv.drawBitmap(background, 0, 0, null);//在 0，0坐标开始画入bg
        cv.drawBitmap(foregroundResize, (bgWidth - fgWidth) / 2, (bgHeight - fgHeight) / 2, null);
        cv.save(Canvas.ALL_SAVE_FLAG);//保存
        cv.restore();//存储
        return newbmp;
    }

    /**
     * bitmap保存成文件
     *
     * @param bitmap
     * @return 文件路径
     */
    public String saveQRCode(Bitmap bitmap) {
        String path = Environment.getExternalStorageDirectory().getPath() + "/zima/";
        File file = new File(path, qrCodeFileName);
        if (file.exists())
            file.delete();
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            return file.getAbsolutePath();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (fos != null)
                    fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setOnGenerateListener(OnGenerateListener onGenerateListener) {
        this.mGenerateListener = onGenerateListener;
    }

    public interface OnGenerateListener {
        void onGenerate(Bitmap mergeBitmap, String qrPath);
    }
}
