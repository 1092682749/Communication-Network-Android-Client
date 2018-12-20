package com.example.qingyun.myfirstapp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.qingyun.myfirstapp.MainActivity;
import com.example.qingyun.myfirstapp.MyList;
import com.example.qingyun.myfirstapp.R;
import com.example.qingyun.myfirstapp.pojo.ChatMsgRecord;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

public class ChatMsgRecordAdapter extends ArrayAdapter<ChatMsgRecord> {
    private LinkedList<ChatMsgRecord> itemList = new LinkedList();
    private int resource;
    private Context context;

    public ChatMsgRecordAdapter(Context context, int resource, List<ChatMsgRecord> objects) {
        super(context, resource, objects);
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
        if (sendName.equals(MainActivity.user)) {
            System.out.println(chatMsgRecord.getContent());
            // 如果发送方是当前用户
            view = LayoutInflater.from(context).inflate(R.layout.right_item, null);
            if (chatMsgRecord.getMsgtype() == 2) {
                view.findViewById(R.id.right_text).setVisibility(View.GONE);
                View voiceImageRight = view.findViewById(R.id.voice_send);
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
                view.findViewById(R.id.right_text).setVisibility(View.GONE);
                view.findViewById(R.id.send_image_framework).setVisibility(View.VISIBLE);
                ImageView imageView = view.findViewById(R.id.send_image);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            final Bitmap bitmap = getURLimage("http://192.168.0.222:8443/" + chatMsgRecord.getContent());
                            MyList.handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    imageView.setImageBitmap(bitmap);
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            } else {
                // 文字消息
                ((TextView) view.findViewById(R.id.right_text)).setText(chatMsgRecord.getContent());
            }
        } else {
            // 如果不是本方用户
            view = LayoutInflater.from(context).inflate(R.layout.left_item, null);
            System.out.print(chatMsgRecord.getContent());
            if (chatMsgRecord.getMsgtype() == 2) {
                view.findViewById(R.id.left_text).setVisibility(View.GONE);
                View voiceImageRight = view.findViewById(R.id.receive_voice);
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
                view.findViewById(R.id.left_text).setVisibility(View.GONE);
                view.findViewById(R.id.receive_image_framework).setVisibility(View.VISIBLE);
                ImageView imageView = view.findViewById(R.id.receive_image);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            final Bitmap bitmap = getURLimage("http://192.168.0.222:8443/" + chatMsgRecord.getContent());
                            MyList.handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    imageView.setImageBitmap(bitmap);
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            } else {
                // 文字消息
                ((TextView) view.findViewById(R.id.left_text)).setText(chatMsgRecord.getContent());
            }
        }
        return view;
    }

    //加载图片
    public Bitmap getURLimage(String url) throws MalformedURLException {
        Bitmap bmp = null;
        URL myurl = new URL(url);
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) myurl.openConnection();
            conn.setConnectTimeout(6000);//设置超时
            conn.setDoInput(true);
            conn.setUseCaches(false);//不缓存
            conn.connect();
            InputStream is = null;//获得图片的数据流
            is = conn.getInputStream();
            bmp = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmp;
    }

}
