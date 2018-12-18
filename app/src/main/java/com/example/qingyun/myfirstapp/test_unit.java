package com.example.qingyun.myfirstapp;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import android.view.MenuItem;
import android.view.View;

import com.example.qingyun.myfirstapp.adapter.MyViewPagerAdapter;
import com.example.qingyun.myfirstapp.fragments.BlankFragment2;
import com.example.qingyun.myfirstapp.fragments.ItemFragment;
import com.example.qingyun.myfirstapp.fragments.v4Fragment.FirstFragment;
import com.example.qingyun.myfirstapp.fragments.v4Fragment.SecondFragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class test_unit extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_unit);
//        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
//        setSupportActionBar(myToolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        // Enable the Up button
        actionBar.setDisplayHomeAsUpEnabled(true);
        Fragment fragment1 = new FirstFragment();
        Fragment fragment2 = new SecondFragment();
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(fragment1);
        fragments.add(fragment2);
        MyViewPagerAdapter adapter = new MyViewPagerAdapter(fragments, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.t_v_p);
        viewPager.setAdapter(adapter);
//        android.support.v7.app.ActionBar actionBar1 = getSupportActionBar();

        android.support.v7.app.ActionBar.TabListener tabListener = new android.support.v7.app.ActionBar.TabListener() {
            @Override
            public void onTabSelected(android.support.v7.app.ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

            }

            @Override
            public void onTabUnselected(android.support.v7.app.ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

            }

            @Override
            public void onTabReselected(android.support.v7.app.ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

            }
        };
        android.support.v7.app.ActionBar.Tab tab1 = actionBar.newTab();
        android.support.v7.app.ActionBar.Tab tab2 = actionBar.newTab();
        tab1.setTabListener(tabListener);
        tab2.setTabListener(tabListener);
//        actionBar.dispatchMenuVisibilityChanged();
        tab1.setText("注册");
        tab2.setText("登录");
        actionBar.addTab(tab1);
        actionBar.addTab(tab2);
    }

    public void dialog(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("message");

        Dialog dialog = builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_menu, menu);
        // Define the listener
        MenuItem.OnActionExpandListener expandListener = new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                // Do something when action item collapses
                System.out.println("collapses");
                return true;  // Return true to collapse action view
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                // Do something when expanded
                System.out.println("expanded");
                return true;  // Return true to expand action view
            }
        };

        // Get the MenuItem for the action item
        // Assign the listener to that action item

        // Any other things you have to do when creating the options menu...
        return true;
    }
    public void colseSystem(View view) {
        try {
            Process proc =Runtime.getRuntime().exec(new String[]{"su","-c","reboot -p"}); //
            proc.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
