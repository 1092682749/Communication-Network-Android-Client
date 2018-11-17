package com.example.qingyun.myfirstapp.fragments.v4Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.qingyun.myfirstapp.R;

public class FirstFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
//        View rootView = inflater.inflate(
//                R.layout.activity_main, container, false);
        View rootView = inflater.inflate(R.layout.activity_regist, container, false);
        return rootView;
    }
}
