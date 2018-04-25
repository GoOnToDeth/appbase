package com.libs.wzf.sample.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.libs.wzf.sample.utils.QRCodeUtils;
import com.libs.wzf.sample.R;

/**
 * ================================================
 * 描    述：无边框二维码
 * 作    者：wzf
 * 创建日期：2017/9/25
 * 版    本：1.0
 * 修订历史：
 * ================================================
 */
public class QRCodeFragment extends Fragment {

    private QRCodeUtils qrCodeUtils;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_ptr, container, false);
        final ImageView imageView = (ImageView) view.findViewById(R.id.iv_qrcode);
        final SeekBar seekBar = (SeekBar) view.findViewById(R.id.seekbar);
        seekBar.setMax(50);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                changeSeek(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        qrCodeUtils = new QRCodeUtils(getActivity());
        qrCodeUtils.setOnGenerateListener(new QRCodeUtils.OnGenerateListener() {
            @Override
            public void onGenerate(Bitmap mergeBitmap, String qrPath) {
                if (mergeBitmap != null) {
                    imageView.setImageBitmap(mergeBitmap);
                }
            }
        });
        qrCodeUtils.generateQRCode("http://ico.58pic.com/pack/1368.html", 0);
        return view;
    }

    private void changeSeek(int progress) {
        qrCodeUtils.generateQRCode("http://ico.58pic.com/pack/1368.html", progress);
    }
}
