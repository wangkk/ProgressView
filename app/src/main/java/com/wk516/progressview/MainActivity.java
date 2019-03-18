package com.wk516.progressview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ProgressView progressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressView = findViewById(R.id.progressView);
        List<String> texts = new ArrayList<>();
        texts.add("加工制作");
        texts.add("工厂堆放");
        texts.add("道路运输");
        texts.add("现场堆放");
        texts.add("现场安装");
        texts.add("安装完成");
        progressView.setTexts(texts, 2);
    }
}
