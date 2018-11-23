package com.example.qingyun.myfirstapp.adapter;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.qingyun.myfirstapp.MainActivity;
import com.example.qingyun.myfirstapp.R;
import com.example.qingyun.myfirstapp.pojo.ChatMsgRecord;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class ChatMsgRecordAdapter extends ArrayAdapter<ChatMsgRecord> {
    private LinkedList<ChatMsgRecord> itemList = new LinkedList();
    private int resource;
    private Context context;
    public ChatMsgRecordAdapter(Context context, int resource, List<ChatMsgRecord> objects) {
        super(context,resource,objects);
            this.itemList = (LinkedList<ChatMsgRecord>) objects;
            this.context = context;
            this.resource = resource;
    }

//    @Override
//    public void add(ChatMsgRecord object) {
//        itemList.add(object);
//    }


    @Override
    public int getCount() {
        return itemList.size();
    }


    @Override
    public ChatMsgRecord getItem(int position) {
        return itemList.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }
    // 核心方法
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChatMsgRecord chatMsgRecord = itemList.get(position);
        String sendName = chatMsgRecord.getSendname();
        View view = null;
        if (chatMsgRecord.getMsgtype() == null) {
            chatMsgRecord.setMsgtype(1);
        }
        if (sendName.equals(MainActivity.user)){
            System.out.println(chatMsgRecord.getContent());
            // 如果发送方是当前用户
            view = LayoutInflater.from(context).inflate(R.layout.right_item,null);
            if (chatMsgRecord.getMsgtype() == 2) {
                view.findViewById(R.id.right_text).setVisibility(View.GONE);
                View  voiceImageRight = view.findViewById(R.id.voice_send);
                voiceImageRight.setVisibility(View.VISIBLE);
                // 设置点击事件
                voiceImageRight.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MediaPlayer mediaPlayer = new MediaPlayer();
                        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        try {
//                            播放录音
                            mediaPlayer.setDataSource(chatMsgRecord.getContent());
                            mediaPlayer.prepare();
                            mediaPlayer.start();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            } else if (chatMsgRecord.getMsgtype() == 3) {
                // 图片消息
            } else {
                // 文字消息
                ((TextView)view.findViewById(R.id.right_text)).setText(chatMsgRecord.getContent());
            }
        } else {
            // 如果不是本方用户
            view = LayoutInflater.from(context).inflate(R.layout.left_item, null);
            System.out.print(chatMsgRecord.getContent());
            if (chatMsgRecord.getMsgtype() == 2) {
                view.findViewById(R.id.left_text).setVisibility(View.GONE);
                View  voiceImageRight = view.findViewById(R.id.receive_voice);
                voiceImageRight.setVisibility(View.VISIBLE);
                // 设置点击事件
                voiceImageRight.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MediaPlayer mediaPlayer = new MediaPlayer();
                        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        try {
//                            播放录音
                            mediaPlayer.setDataSource(chatMsgRecord.getContent());
                            mediaPlayer.prepare();
                            mediaPlayer.start();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            } else if (chatMsgRecord.getMsgtype() == 3) {
                // 图片消息
            } else {
                // 文字消息
                ((TextView)view.findViewById(R.id.left_text)).setText(chatMsgRecord.getContent());
            }
        }
        return view;
    }

}
