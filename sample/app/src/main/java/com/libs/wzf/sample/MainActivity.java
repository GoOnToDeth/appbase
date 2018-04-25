package com.libs.wzf.sample;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.libs.wzf.sample.fragments.MainFragment;
import com.wzf.baseutils.adapter.ListViewAdapter;
import com.wzf.baseutils.adapter.ViewHolder;

import java.util.Arrays;
import java.util.concurrent.locks.LockSupport;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        MainFragment mainFragment = new MainFragment();
        transaction.add(R.id.layout_container, mainFragment);
        transaction.commit();
    }
}
