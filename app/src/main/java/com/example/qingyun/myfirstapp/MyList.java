package com.example.qingyun.myfirstapp;

import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Process;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.qingyun.myfirstapp.adapter.ChatMsgRecordAdapter;
import com.example.qingyun.myfirstapp.pojo.ChatMsgRecord;
import com.example.qingyun.myfirstapp.pojo.Observer;
import com.example.qingyun.myfirstapp.utils.CacheMessage;
import com.example.qingyun.myfirstapp.utils.Constant;
import com.example.qingyun.myfirstapp.utils.HttpRequestor;
import com.example.qingyun.myfirstapp.utils.NettyChatClient;

import org.json.JSONArray;

import java.util.LinkedList;
import java.util.List;

public class MyList extends AppCompatActivity implements Observer {
    public static Handler handler = new Handler();
    private Intent intent = null;
    NettyChatClient nettyChatClient = NettyChatClient.NETTY_CHAT_CLIENT;
    List<ChatMsgRecord> chatMsgRecords = null;
    ChatMsgRecordAdapter adapter = null;
    public static String receiveName;
    public static String protocol = "https://";
//    android.widget.ListView listView = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_list);
        CacheMessage.observerMap.put(MainActivity.user, MyList.this);
        intent = getIntent();
        receiveName = intent.getStringExtra("receivename");
        MainActivity.receivename = receiveName;
        chatMsgRecords = new LinkedList<>();
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
            // 判断是否后台运行，是的话通知栏广播
            isRuningOnBackground(msgRecord);
            // 将消息添加到list
            chatMsgRecords.add(msgRecord);
            try {
                // 将消息设置为已读状态
                Constant.httpRequestor.doGet(protocol + MainActivity.host + "/ok/already?sendName=" + MainActivity.user + "&receiveName=" + MyList.receiveName);
            } catch (Exception e) {
                e.printStackTrace();
            }
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
        editText.setText("");
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

//    通知
    public static void showNotifictionIcon(Context context, ChatMsgRecord chatMsgRecord) {
        Intent intent = new Intent(context, MyList.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        发送过来的消息发送名是本方的接收名
        intent.putExtra("receivename", chatMsgRecord.getSendname());
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, "1")
                .setSmallIcon(R.drawable.app_icon_png)
                .setContentTitle(chatMsgRecord.getSendname())
                .setContentText(chatMsgRecord.getContent())
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

// notificationId is a unique int for each notification that you must define
        notificationManager.notify(1, mBuilder.build());
    }
//    判断当前activity是否在后台
    public void isRuningOnBackground(ChatMsgRecord chatMsgRecord) {
        ActivityManager ma = (ActivityManager) MyList.this.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processes = ma.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo info : processes){
            if (info.processName.equals(MyList.this.getPackageName())) {
                System.out.print("info is #######" + info.processName + "and packname is######" + MyList.this.getPackageName());
                if (info.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            createNotificationChannel();
                            showNotifictionIcon(MyList.this, chatMsgRecord);
                        }
                    });
                }
            }
        }
    }
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "channel";
            String description = "消息通知";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("1", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    protected void onResume() {
        // 查询历史聊天记录
//        intent = getIntent();
        receiveName = intent.getStringExtra("receivename");
        super.onResume();
        new Thread() {
            @Override
            public void run() {
                HttpRequestor httpRequestor = new HttpRequestor();
                try {
                    String response = httpRequestor.doGet(protocol + MainActivity.host + "/ok/getMsgListByName" +
                            "?sendName=" + MainActivity.user + "&receiveName=" + MyList.receiveName);
                    System.out.println(response);
                    com.alibaba.fastjson.JSONArray jsonArray = JSON.parseArray(response);
                    List<ChatMsgRecord> historyList = jsonArray.toJavaList(ChatMsgRecord.class);
                    chatMsgRecords.addAll(historyList);
                    handler.post(runnable);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
