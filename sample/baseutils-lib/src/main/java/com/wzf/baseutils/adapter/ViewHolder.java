package com.wzf.baseutils.adapter;

import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * ================================================
 * 描    述：
 * 作    者：wzf
 * 创建日期：2016/7/19
 * 版    本：1.0
 * 修订历史：
 * ================================================
 */
public class ViewHolder {

    private SparseArray<View> mViews = new SparseArray<>();
    private View mConvertView;

    public ViewHolder(View convertView) {
        this.mConvertView = convertView;
    }

    public static ViewHolder get(View convertView, ViewGroup parent, int layoutId) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        return holder;
    }

    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    public View getConvertView() {
        return mConvertView;
    }
}
