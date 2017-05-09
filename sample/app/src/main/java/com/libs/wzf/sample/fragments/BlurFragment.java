package com.libs.wzf.sample.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import com.libs.wzf.sample.R;
import com.wzf.baseutils.blur.BlurManager;
import com.wzf.baseutils.widget.ToggleButton;

/**
 * ================================================
 * 描    述：高斯模糊
 * 作    者：wzf
 * 创建日期：2017/5/9
 * 版    本：1.0
 * 修订历史：
 * ================================================
 */
public class BlurFragment extends Fragment {

    private ImageView imageView;
    private SeekBar seekBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_blur, container, false);
        imageView = (ImageView) view.findViewById(R.id.image);
        seekBar = (SeekBar) view.findViewById(R.id.seekbar);
        final Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bg_circle_header);
        imageView.setImageBitmap(bitmap);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Bitmap blurBitmap = BlurManager.blurByStack(bitmap, progress);
                imageView.setImageBitmap(blurBitmap);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        return view;
    }
}
