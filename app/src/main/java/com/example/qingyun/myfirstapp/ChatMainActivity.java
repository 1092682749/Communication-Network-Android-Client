package com.example.qingyun.myfirstapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.qingyun.myfirstapp.pojo.ChatMsgRecord;
import com.example.qingyun.myfirstapp.pojo.Observer;
import com.example.qingyun.myfirstapp.utils.CacheMessage;
import com.example.qingyun.myfirstapp.utils.NettyChatClient;

import java.net.URISyntaxException;

public class ChatMainActivity extends AppCompatActivity implements Observer {
    NettyChatClient nettyChatClient = NettyChatClient.NETTY_CHAT_CLIENT;
//    WebSocketClientHandler handler = WebSocketClientHandler.getInstance();

    public ChatMainActivity() throws URISyntaxException {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_main);
        CacheMessage.observerMap.put("client1",this);
        Intent intent = getIntent();
        String s = intent.getStringExtra("item");
        System.out.println(s);
    }

    public void write(View view){
        EditText editText = findViewById(R.id.chatText);
        String content = editText.getText().toString();
        try {
            nettyChatClient.write(content);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void setMessage(Object o) {
        if (o instanceof String){
            String s = (String)o;
            System.out.println(s);
        }
        if (o instanceof ChatMsgRecord) {
            ChatMsgRecord chatMsgRecord = (ChatMsgRecord)o;
            System.out.println(chatMsgRecord.getContent());
//            TextView textView = findViewById(R.id.responseMsg);
//            textView.append(chatMsgRecord.getContent()+ "\r\n");
        }
    }
}
