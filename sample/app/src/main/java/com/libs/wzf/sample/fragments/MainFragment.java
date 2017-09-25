package com.libs.wzf.sample.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.libs.wzf.sample.R;
import com.wzf.baseutils.adapter.ListViewAdapter;
import com.wzf.baseutils.adapter.ViewHolder;

import java.util.Arrays;

/**
 * ================================================
 * 描    述：
 * 作    者：wzf
 * 创建日期：2017/5/9
 * 版    本：1.0
 * 修订历史：
 * ================================================
 */
public class MainFragment extends Fragment implements AdapterView.OnItemClickListener {

    private static final String[] items = {"RoundView和开关按钮", "流式布局", "下拉刷新和加载更多",
            "自定义Dialog", "高斯模糊", "Banner"};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_main, container, false);
        ListView listView = (ListView) view.findViewById(R.id.listview);
        ListViewAdapter<String> adapter = new ListViewAdapter<String>(R.layout.item_main, Arrays.asList(items)) {
            @Override
            protected void onBindData(ViewHolder viewHolder, int position, String item) {
                TextView textView = viewHolder.getView(R.id.tvtitle);
                textView.setText(item);
            }
        };
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                routeFragment(new RoundViewFragment());
                break;
            case 1:
                routeFragment(new FlowFragment());
                break;
            case 2:
                routeFragment(new PtrFragment());
                break;
            case 3:
                routeFragment(new DialogFragment());
                break;
            case 4:
                routeFragment(new BlurFragment());
                break;
            case 5:
                routeFragment(new BannerFragment());
                break;
        }
    }

    private void routeFragment(Fragment fragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_right_in, R.anim.slide_left_out, R.anim.pop_left_in, R.anim.pop_right_out);
        transaction.replace(R.id.layout_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
