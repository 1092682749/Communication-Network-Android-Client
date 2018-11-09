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

import com.alibaba.fastjson.JSON;
import com.example.qingyun.myfirstapp.adapter.FruitAdapter;
import com.example.qingyun.myfirstapp.pojo.Fruit;
import com.example.qingyun.myfirstapp.pojo.User;
import com.example.qingyun.myfirstapp.utils.HttpRequestor;

import java.util.ArrayList;
import java.util.List;

public class ListView extends AppCompatActivity {
    List<User> users = null;
    List<Fruit> fruits = new ArrayList<>();
    Handler handler = new Handler();
    FruitAdapter adapter = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
//                ListView.this,
//                android.R.layout.simple_list_item_1,
//                new String[]{"123","456","789"}
//        );
        Thread thread = new Thread(requestFriend);
        thread.start();


        fruits.add(new Fruit("健身水果",R.drawable.one));
        fruits.add(new Fruit("水果一",R.drawable.three));
        fruits.add(new Fruit("水果②",R.drawable.tow));
        fruits.add(new Fruit("水果③",R.drawable.four));
        fruits.add(new Fruit("水果④",R.drawable.five));
        adapter = new FruitAdapter(
                ListView.this,
                R.layout.fruit_item,
                fruits
                );
        System.out.print("sss");
        final android.widget.ListView listView = (android.widget.ListView)findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ListView.this,MyList.class);
                intent.putExtra("username",((TextView)view.findViewById(R.id.fruit_id)).getText());
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
            List<User> users = JSON.parseArray(resp).toJavaList(User.class);
            users.forEach(user -> {
                Fruit fruit = new Fruit(user.getUsername(),R.drawable.five);
                fruits.add(fruit);
            });
            handler.post(new Runnable() {
                @Override
                public void run() {
                    android.widget.ListView listView = findViewById(R.id.list_view);
                    adapter.notifyDataSetChanged();
                }
            });
        }
    };
}
