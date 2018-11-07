package com.example.qingyun.myfirstapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.qingyun.myfirstapp.adapter.ChatMsgRecordAdapter;
import com.example.qingyun.myfirstapp.pojo.ChatMsgRecord;

import java.util.LinkedList;
import java.util.List;

public class MyList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_list);
        List<ChatMsgRecord> chatMsgRecords = new LinkedList<>();
        ChatMsgRecord record1 = new ChatMsgRecord();
        record1.setSendname("admin");
        record1.setContent("wo shi admin");
        ChatMsgRecord record2 = new ChatMsgRecord();
        record2.setSendname("asd");
        record2.setContent("wo bu shi");
        chatMsgRecords.add(record1);
        chatMsgRecords.add(record2);
        ChatMsgRecordAdapter adapter = new ChatMsgRecordAdapter(MyList.this,
                0,
                chatMsgRecords
                );
        android.widget.ListView listView = findViewById(R.id.my_list);
        listView.setAdapter(adapter);
        ChatMsgRecord record3 = new ChatMsgRecord();
        record3.setContent("我是后来的");
        record3.setSendname("mmm");
        chatMsgRecords.add(record3);
    }
}
