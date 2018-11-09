package com.example.qingyun.myfirstapp;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.qingyun.myfirstapp.adapter.ChatMsgRecordAdapter;
import com.example.qingyun.myfirstapp.pojo.ChatMsgRecord;
import com.example.qingyun.myfirstapp.pojo.Observer;
import com.example.qingyun.myfirstapp.utils.CacheMessage;
import com.example.qingyun.myfirstapp.utils.NettyChatClient;

import java.util.LinkedList;
import java.util.List;

public class MyList extends AppCompatActivity implements Observer {
    public Handler handler = new Handler();
    private Intent intent = null;
    NettyChatClient nettyChatClient = NettyChatClient.NETTY_CHAT_CLIENT;
    List<ChatMsgRecord> chatMsgRecords = null;
    ChatMsgRecordAdapter adapter = null;
    public static String receiveName;
//    android.widget.ListView listView = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_list);
        CacheMessage.observerMap.put(MainActivity.user, MyList.this);
        intent = getIntent();
        chatMsgRecords = new LinkedList<>();
        receiveName = intent.getStringExtra("username");
        Toast.makeText(this,receiveName,Toast.LENGTH_SHORT).show();
        adapter = new ChatMsgRecordAdapter(MyList.this,
                android.R.layout.simple_list_item_1,
                chatMsgRecords
        );
        android.widget.ListView listView = findViewById(R.id.my_list);
        listView.setAdapter(adapter);
    }
    @Override
    public void setMessage(Object o) {
        if (o instanceof ChatMsgRecord){
            ChatMsgRecord msgRecord = (ChatMsgRecord)o;
            chatMsgRecords.add(msgRecord);
//            adapter.add(msgRecord);
            // 子线程无法操作主线程ui，因此交给handler
            handler.post(runnable);
        } else {
            Toast.makeText(this,"消息格式不对请联系管理员",Toast.LENGTH_SHORT).show();
        }
    }
    public void refresh(){
        finish();
        Intent intent = new Intent(MyList.this,MyList.class);
        startActivity(intent);
    }
    // 发送触发事件
    public void write(View view){
        EditText editText = findViewById(R.id.my_list_input);
        ChatMsgRecord chatMsgRecord = new ChatMsgRecord();
        chatMsgRecord.setSendname(MainActivity.user);
        chatMsgRecord.setContent(editText.getText().toString());
        chatMsgRecords.add(chatMsgRecord);
        adapter.notifyDataSetChanged();
        String content = editText.getText().toString();
        nettyChatClient.write(content);
    }
    // 更新ui的任务线程对象
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            adapter.notifyDataSetChanged();
            ListView listView = findViewById(R.id.my_list);
            listView.setSelection(listView.getBottom());
        }
    };
    // 测试使用数据
    private void testInitList(){
        ChatMsgRecord record1 = new ChatMsgRecord();
        record1.setSendname("123");
        record1.setContent("wo shi 123" + intent.getStringExtra("username"));
        ChatMsgRecord record2 = new ChatMsgRecord();
        record2.setSendname("asd");
        record2.setContent("wo bu shi");
        chatMsgRecords.add(record1);
        chatMsgRecords.add(record2);
    }
}
