package com.feng.viewdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MyAdapter.ItemClickListener {
    RecyclerView mList;
    MyAdapter myAdapter;
    List<String> mData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        initView();
    }

    private void initView() {
        mList = findViewById(R.id.recycle_view);
        mList.setLayoutManager(new LinearLayoutManager(this));
        mList.setAdapter(myAdapter = new MyAdapter());
        myAdapter.refreshView(mData);
        myAdapter.setOnclickListener(this);
    }

    private void initData() {
        mData.add("LauncherView");
        mData.add("RadioRuleView");
    }

    @Override
    public void onClickItem(String str) {
        switch (str) {
            case "LauncherView":
                startActivity(new Intent(this, LauncherActivity.class));
                break;
            case "RadioRuleView":
                startActivity(new Intent(this, RadioRuleActivity.class));
                break;
        }
    }
}
