package com.example.qingyun.myfirstapp;

import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.qingyun.myfirstapp.adapter.FruitAdapter;
import com.example.qingyun.myfirstapp.adapter.UserAdapter;
import com.example.qingyun.myfirstapp.pojo.Fruit;
import com.example.qingyun.myfirstapp.pojo.User;
import com.example.qingyun.myfirstapp.utils.HttpRequestor;
import com.example.qingyun.myfirstapp.utils.NettyChatClient;
import com.yalantis.phoenix.PullToRefreshView;

import java.util.ArrayList;
import java.util.List;

public class ListView extends AppCompatActivity {
    List<User> users = new ArrayList<>();
    List<Fruit> fruits = new ArrayList<>();
    Handler handler = new Handler();
    UserAdapter adapter = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
        setTitleColor(R.color.white);
        setTitle("好友列表");
//        下拉刷新组件
        PullToRefreshView mPullToRefreshView = (PullToRefreshView)findViewById(R.id.pull_to_refresh);
        mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                NettyChatClient.NETTY_CHAT_CLIENT.getChannelFuture();
                mPullToRefreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPullToRefreshView.setRefreshing(false);
                    }
                }, 1000);
            }
        });
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
//                ListView.this,
//                android.R.layout.simple_list_item_1,
//                new String[]{"123","456","789"}
//        );
        Thread thread = new Thread(requestFriend);
        thread.start();


        adapter = new UserAdapter(
                ListView.this,
                R.layout.friend_item,
                users
                );

        final android.widget.ListView listView = (android.widget.ListView)findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ListView.this,MyList.class);
                intent.putExtra("receivename",((TextView)view.findViewById(R.id.friend_id)).getText());
                startActivity(intent);
//                listView.setSelection(listView.getBottom());
            }
        });
    }
    Runnable requestFriend = new Runnable() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void run() {
            String resp = null;
            try {
                resp = new HttpRequestor().doGet("https://dyzhello.club/ok/getFriend");
            } catch (Exception e) {
                e.printStackTrace();
            }
            List<User> list = JSON.parseArray(resp).toJavaList(User.class);
            list.forEach(item -> ListView.this.users.add(item));
            handler.post(new Runnable() {
                @Override
                public void run() {
                    adapter.notifyDataSetChanged();
                }
            });
        }
    };
    private boolean isQuit = false;

    @Override
    public void onBackPressed() {

        if (!isQuit) {
            Toast.makeText(ListView.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            isQuit = true;

            //这段代码意思是,在两秒钟之后isQuit会变成false
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        isQuit = false;
                    }
                }
            }).start();


        } else {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    startActivity(intent);
                }
            });
        }
    }
}
