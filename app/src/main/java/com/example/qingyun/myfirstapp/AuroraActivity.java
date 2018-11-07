package com.example.qingyun.myfirstapp;

import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.qingyun.myfirstapp.pojo.MyMessage;

import cn.jiguang.imui.commons.ImageLoader;
import cn.jiguang.imui.commons.models.IMessage;
import cn.jiguang.imui.messages.MessageList;
import cn.jiguang.imui.messages.MsgListAdapter;

public class AuroraActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aurora);
        MessageList messageList = findViewById(R.id.msg_list);
        MsgListAdapter adapter = new MsgListAdapter<>("0", null,  new ImageLoader() {
            @Override
            public void loadAvatarImage(ImageView imageView, String s) {

            }

            @Override
            public void loadImage(ImageView imageView, String url) {

            }

            @Override
            public void loadVideo(ImageView imageView, String s) {

            }
        });
        messageList.setAdapter(adapter);
        MyMessage message = new MyMessage("123",1);
        adapter.addToStart(message, true);
    }
}
