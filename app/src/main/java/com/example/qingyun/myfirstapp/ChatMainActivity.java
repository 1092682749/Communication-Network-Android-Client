package com.example.qingyun.myfirstapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.qingyun.myfirstapp.utils.NettyChatClient;
import com.example.qingyun.myfirstapp.utils.WebSocketClientHandler;

import org.java_websocket.client.WebSocketClient;

import java.net.URISyntaxException;

public class ChatMainActivity extends AppCompatActivity {
    NettyChatClient nettyChatClient = NettyChatClient.NETTY_CHAT_CLIENT;
//    WebSocketClientHandler handler = WebSocketClientHandler.getInstance();

    public ChatMainActivity() throws URISyntaxException {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_main);
    }

    public void write(View view){
        nettyChatClient.write("aaaaaadasdasdadaddddd1111111!!!!!");
    }
}
