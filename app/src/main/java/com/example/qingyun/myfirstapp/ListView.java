package com.example.qingyun.myfirstapp;

//import android.app.ActionBar;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.qingyun.myfirstapp.adapter.FruitAdapter;
import com.example.qingyun.myfirstapp.adapter.MyPageAdapter;
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
    List<View> items = new ArrayList<>();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_menu, menu);
        // Define the listener
        MenuItem.OnActionExpandListener expandListener = new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                // Do something when action item collapses
                return true;  // Return true to collapse action view
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                // Do something when expanded
                return true;  // Return true to expand action view
            }
        };

        // Get the MenuItem for the action item

        // Assign the listener to that action item

        // Any other things you have to do when creating the options menu...
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        switch (i) {
            case R.id.action_logout:
                NettyChatClient.NETTY_CHAT_CLIENT.getChannelFuture().channel().close();
                Intent intent = new Intent(ListView.this, LoginActive.class);
                startActivity(intent);
                break;
            case R.id.action_linkAuthor:
                Toast.makeText(ListView.this, "作者QQ:1092682749", Toast.LENGTH_LONG).show();
                break;
            case R.id.update_version:
                Toast.makeText(ListView.this, "测试版本不提供下载请联系作者", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

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
