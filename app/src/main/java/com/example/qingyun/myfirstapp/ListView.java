package com.example.qingyun.myfirstapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.qingyun.myfirstapp.adapter.FruitAdapter;
import com.example.qingyun.myfirstapp.pojo.Fruit;

import java.util.ArrayList;

public class ListView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
//                ListView.this,
//                android.R.layout.simple_list_item_1,
//                new String[]{"123","456","789"}
//        );
        Fruit[] fruits = new Fruit[5];
        fruits[0] = new Fruit("健身水果",R.drawable.one);
        fruits[1] = new Fruit("水果一",R.drawable.three);
        fruits[2] = new Fruit("水果②",R.drawable.tow);
        fruits[3] = new Fruit("水果③",R.drawable.four);
        fruits[4] = new Fruit("水果④",R.drawable.five);
        FruitAdapter adapter = new FruitAdapter(
                ListView.this,
                R.layout.fruit_item,
                fruits
                );
        System.out.print("sss");
        final android.widget.ListView listView = (android.widget.ListView)findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent(ListView.this,ChatMainActivity.class);
//                intent.putExtra("item",((TextView)view.findViewById(R.id.fruit_id)).getText());
//                startActivity(intent);
                listView.setSelection(listView.getBottom());
            }
        });
    }
}
