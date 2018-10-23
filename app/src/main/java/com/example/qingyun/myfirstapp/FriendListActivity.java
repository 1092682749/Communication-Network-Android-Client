package com.example.qingyun.myfirstapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.qingyun.myfirstapp.utils.HttpRequestor;

public class FriendListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    setMessage(new HttpRequestor().doPost("https:/dyzhello.club/ok/getFriend",null));
//                setMessage("asd");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void setMessage(String message){
        System.out.println(message);
        TextView textView = findViewById(R.id.httpText);
        textView.append("post");
        textView.append(message);
    }
}
