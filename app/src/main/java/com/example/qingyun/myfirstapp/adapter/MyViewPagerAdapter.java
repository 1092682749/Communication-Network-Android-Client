package com.example.qingyun.myfirstapp.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.List;

public class MyViewPagerAdapter extends FragmentPagerAdapter {
    List<Fragment> views;

    public MyViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }
    public MyViewPagerAdapter (List<Fragment> views, FragmentManager fm) {
        this(fm);
        this.views = views;
    }
    @Override
    public Fragment getItem(int i) {
        Fragment view = views.get(i);
        return view;
    }

    @Override
    public int getCount() {
        return views.size();
    }
}
