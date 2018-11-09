package com.example.qingyun.myfirstapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.qingyun.myfirstapp.MainActivity;
import com.example.qingyun.myfirstapp.R;
import com.example.qingyun.myfirstapp.pojo.ChatMsgRecord;

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
        if (sendName.equals(MainActivity.user)){
            // 如果发送方是当前用户
            view = LayoutInflater.from(context).inflate(R.layout.right_item,null);
            ((TextView)view.findViewById(R.id.right_text)).setText(chatMsgRecord.getContent());
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.left_item, null);
            ((TextView)view.findViewById(R.id.left_text)).setText(chatMsgRecord.getContent());
            System.out.print(chatMsgRecord.getContent());
        }
        return view;
    }

}
