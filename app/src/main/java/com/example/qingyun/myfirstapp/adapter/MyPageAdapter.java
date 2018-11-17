package com.example.qingyun.myfirstapp.adapter;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class MyPageAdapter extends PagerAdapter {
    List<View> items;
    public MyPageAdapter(List<View> items) {
        this.items = items;
    }
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View layout = items.get(position);
        container.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View layout = items.get(position);
        container.removeView(layout);
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;

    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public int getItemPosition(Object object) {
        // TODO Auto-generated method stub
        return super.getItemPosition(object);
    }
}
