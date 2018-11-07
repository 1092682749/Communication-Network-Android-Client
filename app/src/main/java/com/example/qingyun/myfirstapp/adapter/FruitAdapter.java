package com.example.qingyun.myfirstapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.qingyun.myfirstapp.R;
import com.example.qingyun.myfirstapp.pojo.Fruit;

import java.util.logging.Logger;

public class FruitAdapter extends ArrayAdapter<Fruit> {
    private int resourceID;
    private final Logger logger = Logger.getLogger(FruitAdapter.class.getName());
    public FruitAdapter( Context context,  int textViewResourceId, Fruit[] objects) {
        super(context, textViewResourceId, objects);
        this.resourceID = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Fruit fruit = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceID,null);
        logger.info("##################"+resourceID);
        ImageView imageView = view.findViewById(R.id.fruit_image);
        TextView textView = view.findViewById(R.id.fruit_name);
        TextView idView = view.findViewById(R.id.fruit_id);
        idView.setText(fruit.getName());
//        idView.setVisibility(View.INVISIBLE);
        imageView.setImageResource(fruit.getImageID());
        textView.setText(fruit.getName());
        return view;
    }
}
