package com.example.hw_9_webtech;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class DetailedArticleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_article);

        Intent intent = getIntent();
        String articleID = intent.getExtras().getString("articleID");

        ((TextView) findViewById(R.id.articleID)).setText(articleID);
    }
}
