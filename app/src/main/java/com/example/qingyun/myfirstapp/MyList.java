package com.example.qingyun.myfirstapp;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Process;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.qingyun.myfirstapp.adapter.ChatMsgRecordAdapter;
import com.example.qingyun.myfirstapp.pojo.ChatMsgRecord;
import com.example.qingyun.myfirstapp.pojo.Observer;
import com.example.qingyun.myfirstapp.utils.CacheMessage;
import com.example.qingyun.myfirstapp.utils.Constant;
import com.example.qingyun.myfirstapp.utils.HttpRequestor;
import com.example.qingyun.myfirstapp.utils.NetWorkServer;
import com.example.qingyun.myfirstapp.utils.NetWorkServerListener;
import com.example.qingyun.myfirstapp.utils.NettyChatClient;
import com.example.qingyun.myfirstapp.utils.json.JsonToBean;

import org.json.JSONArray;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Base64;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MyList extends AppCompatActivity implements Observer {
    public static Handler handler = new Handler();
    private Intent intent = null;
    NettyChatClient nettyChatClient = NettyChatClient.NETTY_CHAT_CLIENT;
    List<ChatMsgRecord> chatMsgRecords = null;
    ChatMsgRecordAdapter adapter = null;
    public static String receiveName;
    public static String nickname;
    public static String protocol = "https://";
    public ImageView voiceView;
    public PopupWindow voiceWindow;
    public Boolean isJump = false;
    public MediaRecorder recorder;
    public Long recorderStartTime;
    public Long recrderEndTime;
    public File voiceFile;
    public ChatMsgRecord responseChatMsgRecored;
    //    android.widget.ListView listView = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_list);
//        setTitle("对话");
        // 设置返回按钮
        ActionBar ab = getSupportActionBar();
        voice(); // 录音按钮响应事件
        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);
        setTitleColor(R.color.white);
        CacheMessage.observerMap.put(MainActivity.user, MyList.this);
        intent = getIntent();
        receiveName = intent.getStringExtra("receivename");
        nickname = intent.getStringExtra("receivename");
        MainActivity.receivename = receiveName;
        chatMsgRecords = new LinkedList<>();
        Toast.makeText(this, receiveName, Toast.LENGTH_SHORT).show();
        adapter = new ChatMsgRecordAdapter(MyList.this,
                android.R.layout.simple_list_item_1,
                chatMsgRecords
        );
        android.widget.ListView listView = findViewById(R.id.my_list);
        listView.setAdapter(adapter);
        initBtnEvent();
    }

    @Override
    public void setMessage(Object o) {
        if (o instanceof ChatMsgRecord) {
            ChatMsgRecord msgRecord = (ChatMsgRecord) o;
            // 判断是否后台运行，是的话通知栏广播
            isRuningOnBackground(msgRecord);
            // 将消息添加到list
            if (msgRecord.getSendname().equals(receiveName)) {
                chatMsgRecords.add(msgRecord);
            } else if (msgRecord.getSendname().equals("system")) {
                nettyChatClient.getChannelFuture().channel().close();
                chatMsgRecords.add(msgRecord);
                //展示弹出框，退出当前activity
                handler.post(showDialog);
//                new FireMissilesDialogFragment().onCreateDialog();
            } else {
                return;
            }
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
            Toast.makeText(this, "消息格式不对请联系管理员", Toast.LENGTH_SHORT).show();
        }
    }

    public void refresh() {
        finish();
        Intent intent = new Intent(MyList.this, MyList.class);
        startActivity(intent);
    }

    // 发送触发事件
    public void write(View view) throws InterruptedException {
        EditText editText = findViewById(R.id.my_list_input);
        ChatMsgRecord chatMsgRecord = new ChatMsgRecord();
        chatMsgRecord.setSendname(MainActivity.user);
        if ("".equals(editText.getText().toString())) {
            Toast.makeText(MyList.this, "输入不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        chatMsgRecord.setContent(editText.getText().toString());
        chatMsgRecords.add(chatMsgRecord);
        adapter.notifyDataSetChanged();
        // 滚动到底部
        ListView listView = findViewById(R.id.my_list);
        listView.setSelection(listView.getBottom());
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
    private void testInitList() {
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
        //        PendingIntent.FLAG_UPDATE_CURRENT该字段表示保留之前的intent并添加新的Intent属性
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
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
        for (ActivityManager.RunningAppProcessInfo info : processes) {
            if (info.processName.equals(MyList.this.getPackageName())) {
                System.out.print("info is #######" + info.processName + "and packname is######" + MyList.this.getPackageName());
                // 判断是否在后台运行，如果是发起通知栏通知
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
        nickname = intent.getStringExtra("nickname");
        System.out.println("rrrrrrrrrrrrrrrrrrrrrrr" + receiveName);
        System.out.println("sssssssssssssssssss" + MainActivity.user);
        setTitle(nickname);
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

    Runnable showDialog = new Runnable() {
        @Override
        public void run() {
            AlertDialog.Builder builder = new AlertDialog.Builder(MyList.this);
            Dialog dialog = builder.setTitle("警告").setMessage("重复登录链接关闭").setNegativeButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(MyList.this, LoginActive.class);
                    startActivity(intent);
                }
            }).create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }
    };

    public void camera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, 1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        if ()
    }

    // 点击发送图片
    // 点击录音按钮
    public void voice() {
        ImageView voiceView = findViewById(R.id.my_list_voice);
        EditText editText = findViewById(R.id.my_list_input);
        ImageView emotionButton = findViewById(R.id.emotion_button);
        TextView voiceText = findViewById(R.id.voice_text);
        voiceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (voiceText.getVisibility() == View.VISIBLE) {
                    voiceText.setVisibility(View.GONE);
                    editText.setVisibility(View.VISIBLE);
                } else {
                    voiceText.setVisibility(View.VISIBLE);
                    editText.setVisibility(View.GONE);
                }

            }
        });
        voiceText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    // 初始化按钮事件
    public void initBtnEvent() {
        TextView textView = findViewById(R.id.voice_text);
        View voiceView = getLayoutInflater().inflate(R.layout.layout, null);
        voiceWindow = new PopupWindow(voiceView, 370, 370, true);
        ImageView viewCanDraw = voiceView.findViewById(R.id.canJumpMic);
        Drawable drawable = viewCanDraw.getDrawable();
        voiceWindow.setTouchable(true);
        voiceWindow.setFocusable(true); //设置点击menu以外其他地方以及返回键退出
        voiceWindow.setOutsideTouchable(true);   //设置触摸外面时消失
        drawable.setLevel(9000);
        jump(drawable, voiceView);
        textView.setOnTouchListener(new View.OnTouchListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    // 当按钮按下
                    case MotionEvent.ACTION_DOWN:
                        recorder = new MediaRecorder();
                        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                        recorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
                        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
                        voiceWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
                        // 创建录音文件
                        File filePath = new File(MainActivity.recorderPath);
                        if (!filePath.exists()) {
                            filePath.mkdirs();
                        }
                        voiceFile = new File(filePath,System.currentTimeMillis() + "MyAppVoice.mp3");
                        if (!voiceFile.exists()) {
                            try {
                                voiceFile.createNewFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        recorder.setOutputFile(voiceFile);
                        try {
                            recorder.prepare();
                            recorder.start();
                            recorderStartTime = System.currentTimeMillis();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        isJump = true;
                        System.out.println("DOWN");
                        break;
                    case MotionEvent.ACTION_UP:
                        voiceWindow.dismiss();
                        isJump = false;
                        recorder.stop();
                        recorder.reset();
                        recorder.release();
                        if (voiceFile != null) {
                            try {
                                // 读取文件并发送
                                byte[] bytes = new byte[1024];
                                FileInputStream fis = new FileInputStream(voiceFile);
                                int len = 0;
                                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                                 while ((len = fis.read(bytes)) != -1) {
                                     bos.write(bytes, 0, len);
                                 }

                                String voiceBase64 = Base64.getEncoder().encodeToString(bos.toByteArray());
                                 Map<String, String> map = new HashMap<>();
                                 map.put("content", voiceBase64);
                                new NetWorkServer().setNetWorkServerListener(
                                        new NetWorkServerListener() {
                                            @Override
                                            public void onSuccessed(Object response) {
                                                System.out.println((String) response);
                                                responseChatMsgRecored = (ChatMsgRecord) JsonToBean.changeObject((String) response,ChatMsgRecord.class);
                                                responseChatMsgRecored.setSendname(MainActivity.user);
                                                responseChatMsgRecored.setReceivename(MainActivity.receivename);
                                                chatMsgRecords.add(responseChatMsgRecored);

                                                responseChatMsgRecored.setMsgtype(2);
                                                try {
                                                    nettyChatClient.write(responseChatMsgRecored);
                                                } catch (InterruptedException e) {
                                                    e.printStackTrace();
                                                }
                                                handler.post(runnable);
                                            }

                                            @Override
                                            public void onFailed(Object message) {
                                                System.out.println("failed");
                                            }
                                        }
                                ).request("post","https://dyzhello.club/ok/saveVoice", map);
                                System.out.println(voiceBase64);
//                                测试时是用代码
//                                File mf = File.createTempFile("mffff",".mp3");
//                                FileOutputStream mfo = new FileOutputStream(mf);
//                                mfo.write(Base64.getDecoder().decode(voiceBase64));
//                                mfo.flush();
//                                mfo.close();
//                                MediaPlayer mediaPlayer = new MediaPlayer();
//                                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//                                mediaPlayer.setDataSource("https://dyzhello.club/../uploads/voice/1542444389235androidVoice.mp3");
//                                mediaPlayer.prepare();
//                                mediaPlayer.start();
//                                new NetWorkServer().request("POST",);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
//                        recorder = null;
                        TextView timeText  = voiceView.findViewById(R.id.time_show);
                        timeText.setText("00:00");
                        System.out.println("UP");
                        break;
                }
                return true;
            }
        });
    }

    public void jump(Drawable drawable, View view) {
        new Thread() {
            @Override
            public void run() {
                while (true) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (isJump) {
//                                    Thread.sleep(500);

                                double ratio = (double) recorder.getMaxAmplitude() / 1;
                                double db = 0;// 分贝
                                db = 20 * Math.log10(ratio);
//                                int level = (int) (Math.random() * 5000);
                                drawable.setLevel((int) (3000 + 6000 * db / 100));
                                recrderEndTime = System.currentTimeMillis();
                                Long intervalTime = (recrderEndTime - recorderStartTime) / 1000;
                                String str = timeFormat(intervalTime.intValue());
                                TextView timeText  = view.findViewById(R.id.time_show);
                                timeText.setText(str);
                            }
                        }
                    });
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }
    public String timeFormat(int value) {
        int hour = 0;
        int minute = 0;
        int second = 0;
        minute = value / 60;
        value %= 60;
        hour = minute / 60;
        minute %= 60;
        if (value > 10) {
            return hour + "" + minute + ":" + value;
        }
        return hour + "" + minute + ":0" + value;
    }
}
