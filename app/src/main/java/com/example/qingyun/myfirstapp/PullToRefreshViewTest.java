package com.example.qingyun.myfirstapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.yalantis.phoenix.PullToRefreshView;

public class PullToRefreshViewTest extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pull_to_refresh_view_test);
        PullToRefreshView mPullToRefreshView = (PullToRefreshView)findViewById(R.id.pull_to_refresh);
        mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPullToRefreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPullToRefreshView.setRefreshing(false);
                    }
                }, 1000);
            }
        });
    }

}
