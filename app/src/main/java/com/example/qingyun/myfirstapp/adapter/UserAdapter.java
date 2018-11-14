package com.example.qingyun.myfirstapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.qingyun.myfirstapp.R;
import com.example.qingyun.myfirstapp.pojo.User;

import java.util.List;
import java.util.zip.Inflater;

public class UserAdapter extends ArrayAdapter<User> {

    public UserAdapter(Context context, int resource,List<User> objects) {
        super(context, resource, objects);
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        User user = getItem(position);
        View view =  LayoutInflater.from(getContext()).inflate(R.layout.friend_item,null);
        TextView textView = view.findViewById(R.id.friend_name);
        ImageView imageView = view.findViewById(R.id.friend_image);
        TextView idView = view.findViewById(R.id.friend_id);
        textView.setText(user.getNickName());
        imageView.setImageResource(R.drawable.ntx);
        idView.setText(user.getUsername());
        System.out.println("zzzzzzzzzzzzzz");
        return view;
    }
}
