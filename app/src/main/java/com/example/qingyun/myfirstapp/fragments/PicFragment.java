package com.example.qingyun.myfirstapp.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.qingyun.myfirstapp.R;

import butterknife.ButterKnife;


public class PicFragment extends Fragment {
    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_pic, container, false);
            ButterKnife.bind(this, rootView);
        }
        return rootView;
    }
}
