package com.wzf.baseutils.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * ================================================
 * 描    述：基础的ListView适配器
 * 作    者：wzf
 * 创建日期：2016/7/18
 * 版    本：1.0
 * 修订历史：
 * ================================================
 */
public abstract class ListViewAdapter<T> extends BaseAdapter {

    protected List<T> dataSet = new ArrayList<>();
    private int layoutId;

    public ListViewAdapter(int layoutId, List<T> dataSet) {
        this.dataSet = dataSet;
        this.layoutId = layoutId;
    }

    @Override
    public int getCount() {
        return dataSet.size();
    }

    @Override
    public T getItem(int position) {
        return dataSet.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = ViewHolder.get(convertView, parent, layoutId);
        onBindData(holder, position, getItem(position));
        return holder.getConvertView();
    }

    protected abstract void onBindData(ViewHolder viewHolder, int position, T item);

    public void notifyAdapter(List<T> dataSet) {
        this.dataSet = dataSet;
        notifyDataSetChanged();
    }
}
